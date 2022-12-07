package com.healthcare.service.impl;

import com.healthcare.dto.*;
import com.healthcare.model.entity.*;
import com.healthcare.repository.ReportRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.ReportService;
import com.healthcare.util.DateUtils;
import com.healthcare.util.FileUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.io.FileUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
import java.util.List;

@Service
@Transactional
public class ReportServiceImpl extends BasicService<Report, ReportRepository> implements ReportService {
    private static final String KEY = Report.class.getSimpleName();

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd-MMM-yyyy");
    private static final DateTimeFormatter hourFormat = DateTimeFormat.forPattern("HH:mm:ss");

    @Value("${report.title.size}")
    private Integer titleSize;
    @Value("${report.title.color}")
    private String titleColor;

    @Autowired
    private RedisTemplate<String, Report> reportRedisTemplate;

    @Override
    @Transactional
    public Report save(Report report) {
        report = reportRepository.save(report);
        reportRedisTemplate.opsForHash().put(KEY, report.getId(), report);
        return report;
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        reportRepository.delete(id);
        return reportRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    @Transactional
    public Report findById(Long id) {
        if (reportRedisTemplate.opsForHash().hasKey(KEY, id))
            return (Report) reportRedisTemplate.opsForHash().get(KEY, id);
        return reportRepository.findOne(id);
    }

    @Override
    @Transactional
    public List<Report> findAll() {
        Map<Object, Object> reportMap = reportRedisTemplate.opsForHash().entries(KEY);
        List<Report> reportList = Collections.arrayToList(reportMap.values().toArray());
        if (reportMap.isEmpty())
            reportList = reportRepository.findAll();
        return reportList;
    }

    @Override
    @Transactional
    public Long disableById(Long id) {
        // TODO
        return null;
    }

    @Override
    public File generateVisitReport(VisitReportContext context) throws FileNotFoundException, DocumentException {

        String fileName = FileUtil.getPDFFileName();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // document.addTitle("Visit report");
        // Font titleFont = new Font(FontFamily.HELVETICA, titleSize, Font.BOLD,
        // BaseColor.RED);
        // titleFont.setColor(Integer.valueOf(titleColor.substring(0, 2), 16),
        // Integer.valueOf(titleColor.substring(2, 4), 16),
        // Integer.valueOf(titleColor.substring(4, 6), 16));
        // Paragraph title = new Paragraph(new Chunk("Visit Report", titleFont));
        // title.setSpacingAfter(50f);
        // title.setAlignment(Element.ALIGN_CENTER);
        // document.add(title);
        List<Visit> visits = context.getVisits();
        float[] columnWidths = {1, 2, 6, 5, 5, 5};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        Font f = new Font(FontFamily.HELVETICA, 14, Font.NORMAL, GrayColor.GRAYWHITE);

        PdfPCell cell = new PdfPCell(new Phrase("Visit report", f));
        cell.setBackgroundColor(GrayColor.GRAYBLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        cell.setRowspan(2);
        table.addCell(cell);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        table.addCell(getColumnHeader("#"));
        table.addCell(getColumnHeader("User id"));
        table.addCell(getColumnHeader("Fullname"));
        table.addCell(getColumnHeader("Check in time"));
        table.addCell(getColumnHeader("Check out time"));
        table.addCell(getColumnHeader("Note"));
        if (visits != null && !visits.isEmpty()) {
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            for (int i = 0; i < visits.size(); i++) {
                Visit visit = visits.get(i);
                table.addCell(getNormalCell(visit.getId().toString(), 2));
                table.addCell(getNormalCell(visit.getUser() != null ? visit.getUser().getId().toString() : null, 2));
                table.addCell(getNormalCell(
                        visit.getUser() != null
                                ? visit.getUser().getFirstName() + " " + visit.getUser().getMiddleName() + " "
                                + visit.getUser().getLastName()
                                : null,
                        1));
                table.addCell(getNormalCell(dtf.print(visit.getCheckInTime().getTime()), 1));
                table.addCell(getNormalCell(dtf.print(visit.getCheckOutTime().getTime()), 1));
                table.addCell(getNormalCell(visit.getNotes(), 1));
            }
            document.add(table);
        }
        document.close();
        writer.close();
        return new File(fileName);
    }

    @Override
    public File generateVisitBillingReport(VisitStatusDTO visitStatusDtos, int isValidVisitor, HttpServletRequest request) {

        String fileNameOutput = "billing_report_" + dateFormat.print(new Date().getTime()) + "-" + new Date().getTime() + ".pdf";

        //get real path
        String filePathDirectory = request.getServletContext().getRealPath("/upload");

        ArrayList<VisitResponseDTO> visitors;
        if (isValidVisitor == 1) {
            visitors = visitStatusDtos.getValidVisitors();
        } else {
            visitors = visitStatusDtos.getInValidVisitors();
        }

        StringBuffer sBuffer = new StringBuffer();

        if (visitors != null && !visitors.isEmpty()) {

            visitors.sort(Comparator.comparingLong(VisitResponseDTO::getUserId));

            String templateURL = filePathDirectory + "/visit_billing_export_template.html";

            File f = new File(templateURL);

            Long userId = null;
            StringBuffer rowBody = new StringBuffer();
            String content = null;
            for (VisitResponseDTO visitor : visitors) {

                if (userId == null || !userId.equals(visitor.getUserId())) {

                    // Update content
                    if (content != null && rowBody.length() > 0) {
                        content = content.replace("{body}", rowBody.toString());
                        sBuffer.append(content);
                    }

                    userId = visitor.getUserId();
                    String seniorInfoRow = createSeniorInfoRowTable(userId);


                    try {
                        // Read template content
                        content = FileUtils.readFileToString(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // Update content
                    content = content.replace("{agency}", visitor.getAgency());
                    content = content.replace("{seniorInfo}", seniorInfoRow);

                    rowBody = new StringBuffer();

                }

                String userIdStr = visitor.getUserId().toString();
                String userName = visitor.getFirstName() + " " + visitor.getLastName();
                String checkInDate = visitor.getCheckInTime() != null ? dateFormat.print(visitor.getCheckInTime().getTime()) : "";
                String checkInTime = visitor.getCheckInTime() != null ? hourFormat.print(visitor.getCheckInTime().getTime()) : "";
                String checkOutDate = visitor.getCheckOutTime() != null ? dateFormat.print(visitor.getCheckOutTime().getTime()) : "";
                String checkOutTime = visitor.getCheckOutTime() != null ? hourFormat.print(visitor.getCheckOutTime().getTime()) : "";
                String insuranceCompany = visitor.getInsuranceCompany() != null ? visitor.getInsuranceCompany() : "";
                String medicalId = visitor.getMedicalId() != null ? visitor.getMedicalId() : "";
                String authorizationCode = visitor.getAuthorizationCode() != null ? visitor.getAuthorizationCode() : "";
                String reason = visitor.getReason() != null ? visitor.getReason() : "";
                String checkInSignature = visitor.getCheckInSignature() != null ? visitor.getCheckInSignature() : "";
                String checkOutSignature = visitor.getCheckOutSignature() != null ? visitor.getCheckOutSignature() : "";

                // Create table row HTML
                String row = createVisitBillingRowTable(userIdStr, userName, checkInDate, checkInTime, checkOutDate, checkOutTime,
                        insuranceCompany, medicalId, authorizationCode, reason, checkInSignature, checkOutSignature);
                rowBody.append(row);


            }
            if (content != null && rowBody.length() > 0) {
                content = content.replace("{body}", rowBody.toString());
                sBuffer.append(content);
            }
        }


        FileWriter fWriter = null;
        BufferedWriter bufferedWriter = null;
        String fileName = String.valueOf(new Date().getTime());
        String fileFullPathTheme = filePathDirectory + "/" + fileName + ".html";

        try {

            String allContent = "<html><body>\n" + sBuffer.toString() + "</body></html>";

            fWriter = new FileWriter(fileFullPathTheme);
            bufferedWriter = new BufferedWriter(fWriter);
            bufferedWriter.write(allContent);
            bufferedWriter.newLine(); //this is not actually needed for html files - can make your code more readable though
            bufferedWriter.close(); //make sure you close the writer object


            String fileFullPath = filePathDirectory + "/" + fileNameOutput;

            File file = new File(fileFullPath);

            OutputStream fop = new FileOutputStream(file);

            Document document = new Document(PageSize.A2);
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document, fop);
            // step 3
            document.open();
            // step 4
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(fileFullPathTheme));
            // step 5
            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(filePathDirectory + "/" + fileNameOutput);
    }

    @Override
    public File generateRideBillingReport(RideValidInvalidDTO rideValidInvalidDTO, int isValidVisitor, HttpServletRequest request) {

        String fileNameOutput = "billing_report_" + dateFormat.print(new Date().getTime()) + "-" + new Date().getTime() + ".pdf";

        //get real path
        String filePathDirectory = request.getServletContext().getRealPath("/upload");

        List<BillingRideResponseDTO> rides;
        if (isValidVisitor == 1) {
            rides = rideValidInvalidDTO.getValidRides();
        } else {
            rides = rideValidInvalidDTO.getInvalidRides();
        }

        StringBuffer sBuffer = new StringBuffer();

        if (rides != null && !rides.isEmpty()) {

            rides.sort(Comparator.comparingLong(ride -> ride.getRide().getUser().getId()));

            String templateURL = filePathDirectory + "/ride_billing_export_template.html";

            File f = new File(templateURL);

            Long userId = null;
            StringBuffer rowBody = new StringBuffer();
            String content = null;
            for (BillingRideResponseDTO ride : rides) {

                if (userId == null || !userId.equals(ride.getRide().getUser().getId())) {

                    // Update content
                    if (content != null && rowBody.length() > 0) {
                        content = content.replace("{body}", rowBody.toString());
                        sBuffer.append(content);
                    }

                    userId = ride.getRide().getUser().getId();
                    String seniorInfoRow = createSeniorInfoRowTable(userId);


                    try {
                        // Read template content
                        content = FileUtils.readFileToString(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // Update content
                    content = content.replace("{agency}", ride.getRide().getUser().getAgency().getName());
                    content = content.replace("{seniorInfo}", seniorInfoRow);

                    rowBody = new StringBuffer();

                }

                String userIdStr = ride.getRide().getUser().getId().toString();
                String userName = ride.getRide().getUser().getFirstName() + " " + ride.getRide().getUser().getLastName();
                String insuranceCompany = ride.getInsuranceCompany() != null ? ride.getInsuranceCompany() : "";
                String medicalId = ride.getMedicalId() != null ? ride.getMedicalId() : "";
                String authorizationCode = ride.getAuthorizationCode() != null ? ride.getAuthorizationCode() : "";
                String reason = ride.getReason() != null ? ride.getReason() : "";

                // Create table row HTML
                String row = createRideBillingRowTable(userIdStr, userName, insuranceCompany, medicalId, authorizationCode, reason);
                rowBody.append(row);


            }
            if (content != null && rowBody.length() > 0) {
                content = content.replace("{body}", rowBody.toString());
                sBuffer.append(content);
            }
        }


        FileWriter fWriter = null;
        BufferedWriter bufferedWriter = null;
        String fileName = String.valueOf(new Date().getTime());
        String fileFullPathTheme = filePathDirectory + "/" + fileName + ".html";

        try {

            String allContent = "<html><body>\n" + sBuffer.toString() + "</body></html>";

            fWriter = new FileWriter(fileFullPathTheme);
            bufferedWriter = new BufferedWriter(fWriter);
            bufferedWriter.write(allContent);
            bufferedWriter.newLine(); //this is not actually needed for html files - can make your code more readable though
            bufferedWriter.close(); //make sure you close the writer object


            String fileFullPath = filePathDirectory + "/" + fileNameOutput;

            File file = new File(fileFullPath);

            OutputStream fop = new FileOutputStream(file);

            Document document = new Document(PageSize.A2);
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document, fop);
            // step 3
            document.open();
            // step 4
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(fileFullPathTheme));
            // step 5
            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(filePathDirectory + "/" + fileNameOutput);
    }

    @Override
    public File generateMealOrderReport(List<Object[]> results, Date startDate) throws FileNotFoundException, DocumentException {
        String fileName = FileUtil.getPDFFileName();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        float[] columnWidths = {6, 6, 6, 6, 6};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        Font f = new Font(FontFamily.HELVETICA, 14, Font.NORMAL, GrayColor.GRAYWHITE);

        PdfPCell cell = new PdfPCell(new Phrase("Meal Order report", f));
        cell.setBackgroundColor(GrayColor.GRAYBLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        cell.setRowspan(2);
        table.addCell(cell);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        table.addCell(getColumnHeader("Date"));
        table.addCell(getColumnHeader("Senior Name"));
        table.addCell(getColumnHeader("PIN"));
        table.addCell(getColumnHeader("Meal"));
        table.addCell(getColumnHeader("Meal Class"));
        if (results != null && !results.isEmpty()) {
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            for (Object[] mealOrder : results) {
                table.addCell(getNormalCell(dateFormat.print(startDate.getTime()), 2));
                table.addCell(getNormalCell(new StringBuilder().append(mealOrder[0]).append(" ").append(mealOrder[1]).toString(), 2));
                table.addCell(getNormalCell(mealOrder[3].toString(), 2));
                table.addCell(getNormalCell(mealOrder[2].toString(), 2));
                table.addCell(getNormalCell(mealOrder[4].toString(), 2));
            }
            document.add(table);
        }
        document.close();
        writer.close();
        return new File(fileName);
    }

    public PdfPCell getColumnHeader(String title) {
        Font headerFont = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.RED);
        headerFont.setColor(BaseColor.DARK_GRAY);
        Phrase phrase = new Phrase(title, headerFont);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public PdfPCell getNormalCell(String title, int align) {
        Font f = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.RED);
        f.setColor(BaseColor.DARK_GRAY);
        Phrase phrase = new Phrase(title, f);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        if (align == 2) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        return cell;
    }

    private String createVisitBillingRowTable(String userId,
                                              String userName,
                                              String checkInDate,
                                              String checkInTime,
                                              String checkOutDate,
                                              String checkOutTime,
                                              String insuranceCompany,
                                              String medicalId,
                                              String authorizationCode,
                                              String reason,
                                              String checkInSignature,
                                              String checkOutSignature
    ) {

        String checkInSignatureURL = "";
        if (checkInSignature != null || !"".equals(checkInSignature)) {
            checkInSignatureURL = "<img height='100' src=\"{URL}\"/>";
            checkInSignatureURL = checkInSignatureURL.replace("{URL}", checkInSignature);
        }
        String checkOutSignatureURL = "";
        if (checkOutSignature != null || !"".equals(checkOutSignature)) {
            checkOutSignatureURL = "<img height='100' src=\"{URL}\"/>";
            checkOutSignatureURL = checkOutSignatureURL.replace("{URL}", checkOutSignature);
        }
        String row = " <tr style=\"border: 1px solid gray; height: 50px; \">\n"
                + "                        <td style=\" border: 1px solid gray;\"> {user_id}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {user_name}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {check_in_date}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {check_in_time}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {check_out_date}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {check_out_time}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {insurance_company}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {medical_id}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {authorization_code}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\">{reason}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\">{check_in_signature}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\">{check_out_signature}</td>\n"
                + "                    </tr>";
        row = row.replace("{user_id}", userId)
                .replace("{user_name}", userName)
                .replace("{check_in_date}", checkInDate)
                .replace("{check_in_time}", checkInTime)
                .replace("{check_out_date}", checkOutDate)
                .replace("{check_out_time}", checkOutTime)
                .replace("{insurance_company}", insuranceCompany)
                .replace("{medical_id}", medicalId)
                .replace("{authorization_code}", authorizationCode)
                .replace("{reason}", reason)
                .replace("{check_in_signature}", checkInSignatureURL)
                .replace("{check_out_signature}", checkOutSignatureURL);

        return row;
    }

    private String createRideBillingRowTable(String userId,
                                             String userName,
                                             String insuranceCompany,
                                             String medicalId,
                                             String authorizationCode,
                                             String reason
    ) {

        String row = " <tr style=\"border: 1px solid gray; height: 50px; \">\n"
                + "                        <td style=\" border: 1px solid gray;\"> {user_id}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {user_name}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {insurance_company}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {medical_id}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\"> {authorization_code}</td>\n"
                + "                        <td style=\" border: 1px solid gray;\">{reason}</td>\n"
                + "                    </tr>";
        row = row.replace("{user_id}", userId)
                .replace("{user_name}", userName)
                .replace("{insurance_company}", insuranceCompany)
                .replace("{medical_id}", medicalId)
                .replace("{authorization_code}", authorizationCode)
                .replace("{reason}", reason);

        return row;
    }

    private String createSeniorInfoRowTable(long userId) {
        StringBuilder row = new StringBuilder();
        User user = userRepository.findUserById(userId);

        if (user != null) {
            row = new StringBuilder("<tr>\n");

            row.append("<td>Senior's Name: ").append(user.getFirstName()).append(" ")
                    .append(user.getMiddleName() != null ? user.getMiddleName() : "").append(" ")
                    .append(user.getLastName()).append("</td>\n");

            row.append("<td>Gender: ").append(user.getGender()).append("</td>\n");

            Organization organization = user.getInsurance();

            if (organization != null && organization.getCheckboxList() != null) {
                for (String id : organization.getCheckboxList()) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            row.append("<td>Company Name: ").append(user.getCompany() != null ? user.getCompany().getName() : "").append("</td>\n");
                            break;
                        case 2:
                            row.append("<td>Senior Center Name: ").append(user.getAgency() != null ? user.getAgency().getName() : "").append("</td>\n");
                            break;
                        case 3:
                            row.append("<td>User Name: ").append(user.getUsername() != null ? user.getUsername() : "").append("</td>\n");
                            break;
                        case 8:
                            row.append("<td>Language: ").append(user.getLanguage() != null ? user.getLanguage() : "").append("</td>\n");
                            break;
                        case 9:
                            row.append("<td>Social Security Number: ").append(user.getSocialSecurityNumber() != null ? user.getSocialSecurityNumber() : "").append("</td>\n");
                            break;
                        case 10:
                            row.append("<td>Date of Birth: ").append(user.getDateOfBirth() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getDateOfBirth()) : "").append("</td>\n");
                            break;
                        case 11:
                            row.append("<td>Email: ").append(user.getEmail() != null ? user.getEmail() : "").append("</td>\n");
                            break;
                        case 12:
                            row.append("<td>Phone: ").append(user.getPhone() != null ? user.getPhone() : "").append("</td>\n");
                            break;
                        case 13:
                            row.append("<td>Secondary Phone: ").append(user.getSecondaryPhone() != null ? user.getSecondaryPhone() : "").append("</td>\n");
                            break;
                        case 14:
                            row.append("<td>Verification Phone: ").append(user.getVerificationCode() != null ? user.getVerificationCode() : "").append("</td>\n");
                            break;
                        case 15:
                            row.append("<td>Address Type: ").append(user.getAddressType() != null ? user.getAddressType() : "").append("</td>\n");
                            break;
                        case 16:
                            row.append("<td>Address One: ").append(user.getAddressOne() != null ? user.getAddressOne() : "").append("</td>\n");
                            break;
                        case 17:
                            row.append("<td>Address Two: ").append(user.getAddressTwo() != null ? user.getAddressTwo() : "").append("</td>\n");
                            break;
                        case 18:
                            row.append("<td>City: ").append(user.getCity() != null ? user.getCity() : "").append("</td>\n");
                            break;
                        case 19:
                            row.append("<td>State: ").append(user.getState() != null ? user.getState() : "").append("</td>\n");
                            break;
                        case 20:
                            row.append("<td>Zipcode: ").append(user.getZipcode() != null ? user.getZipcode() : "").append("</td>\n");
                            break;
                        case 21:
                            row.append("<td>Comment: ").append(user.getComment() != null ? user.getComment() : "").append("</td>\n");
                            break;
                        case 22:
                            row.append("<td>Pre-approve Status: ").append(user.getStatus() != null ? user.getStatus().getValue() : "").append("</td>\n");
                            break;
                        case 23:
                            row.append("td>Approve Status: ").append(user.getStatusSecond() != null ? user.getStatusSecond().getValue() : "").append("</td>\n");
                            break;
                        case 24:
                            row.append("<td>Vacation Start Date: ").append(user.getVacationStart() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getVacationStart()) : "").append("</td>\n");
                            break;
                        case 25:
                            row.append("<td>Vacation End Date: ").append(user.getVacationEnd() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getVacationEnd()) : "").append("</td>\n");
                            break;
                        case 26:
                            String profilePhotoURL = "";
                            if (user.getProfilePhoto() != null &&
                                    (user.getProfilePhoto().getFileUrl() != null || !"".equals(user.getProfilePhoto().getFileUrl()))) {
                                profilePhotoURL = "<img height='20' src=\"" + user.getProfilePhoto().getFileUrl() + "\"/>";
                            }
                            row.append("<td>Profile Photo: ").append(profilePhotoURL).append("</td>\n");
                            break;
                        case 27:
                            row.append("<td>PIN: ").append(user.getPin() != null ? user.getPin() : "").append("</td>\n");
                            break;
                        case 28:
                            row.append("<td>Box Number: ").append(user.getBoxNumber() != null ? user.getBoxNumber() : "").append("</td>\n");
                            break;
                        case 29:
                            row.append("<td>Citizenship: ").append(user.getCitizenship() != null ? user.getCitizenship().getValue() : "").append("</td>\n");
                            break;
                        case 30:
                            String educationWorkStr = (user.getEducation() != null ? user.getEducation().getValue() + ", " : "")
                                    + (user.getWorkBackground() != null ? user.getWorkBackground() : "");
                            row.append("<td>Education Work Background: ").append(educationWorkStr).append("</td>\n");
                            break;
                        case 31:
                            row.append("<td>Religious Preference: ").append(user.getReligiousPreference() != null ? user.getReligiousPreference() : "").append("</td>\n");
                            break;
                        case 32:
                            row.append("<td>Children: ").append(user.getChildren() != null ? user.getChildren().toString() : "").append("</td>\n");
                            break;
                        case 33:
                            row.append("<td>Preferred Activity: ").append(user.getPreferredActivity() != null ? user.getPreferredActivity().getName() : "").append("</td>\n");
                            break;
                        case 34:
                            row.append("<td>Rules and Regulations Form: ").append(user.getRulesAndRegusDocId() != null ? user.getRulesAndRegusDocId().getFileName() : "").append("</td>\n");
                            break;
                        case 35:
                            row.append("<td>Assessment Start: ").append(user.getAssessmentStartDate() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getAssessmentStartDate()) : "").append("</td>\n");
                            break;
                        case 36:
                            row.append("<td>Assessment Duration: ").append(user.getAssessmentDuration() != null ? user.getAssessmentDuration().toString() : "").append("</td>\n");
                            break;
                        case 37:
                            row.append("<td>Authorization Start: ").append(user.getAuthorizationStart() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getAuthorizationStart()) : "").append("</td>\n");
                            break;
                        case 38:
                            row.append("<td>Authorization End: ").append(user.getAuthorizationEnd() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getAuthorizationEnd()) : "").append("</td>\n");
                            break;
                        case 39:
                            row.append("<td>Days In Week Authorized: ").append(user.getDaysInWeek() != null ? user.getDaysInWeek() : "").append("</td>\n");
                            break;
                        case 40:
                            row.append("<td>Authorization Fixed or Unfixed: ").append(user.getFixedStatus() != null ? user.getFixedStatus().toString() : "").append("</td>\n");
                            break;
                        case 41:
                            String tripBookingStr = "";
                            if (user.getNeedTrip() != null) {
                                tripBookingStr = user.getNeedTrip() == 1 ? "outbound" : "inbound";
                            }
                            row.append("<td>Trip Booking (Inbound and/or Outbound): ").append(tripBookingStr).append("</td>\n");
                            break;
                        case 42:
                            row.append("<td>Route: ").append(user.getRideLine() != null ? user.getRideLine().getName() : "").append("</td>\n");
                            break;
                        case 43:
                            row.append("<td>Medic ID Number: ").append(user.getMedicaIdNumber() != null ? user.getMedicaIdNumber() : "").append("</td>\n");
                            break;
                        case 44:
                            row.append("<td>Medicare Number: ").append(user.getMedicareNumber() != null ? user.getMedicareNumber() : "").append("</td>\n");
                            break;
                        case 45:
                            row.append("<td>Insurance Name: ").append(user.getInsurance() != null ? user.getInsurance().getName() : "").append("</td>\n");
                            break;
                        case 46:
                            row.append("<td>Insurance Eligibility Start Date: ").append(user.getEligiableStart() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getEligiableStart()) : "").append("</td>\n");
                            break;
                        case 47:
                            row.append("<td>Insurance Eligibility End Date: ").append(user.getEligiableEnd() != null ? DateUtils.dateToString("MM/dd/YYYY", user.getEligiableEnd()) : "").append("</td>\n");
                            break;
                        case 48:
                            row.append("<td>Family Doctor: ").append(user.getFamilyDoctor() != null ? user.getFamilyDoctor().getName() : "").append("</td>\n");
                            break;
                        case 49:
                            row.append("<td>Expert Doctor: ").append(user.getExpertDoctor() != null ? user.getExpertDoctor().getName() : "").append("</td>\n");
                            break;
                        case 50:
                            row.append("<td>Medical Condition: ").append(user.getMedicalCondition() != null ? user.getMedicalCondition() : "").append("</td>\n");
                            break;
                        case 51:
                            row.append("<td>Ambulatory Status: ").append(user.getMedication() != null ? user.getMedication().getAmbulatoryStatus() : "").append("</td>\n");
                            break;
                        case 52:
                            row.append("<td>Diagnosis: ").append(user.getMedication() != null ? user.getMedication().getDiagonosis() : "").append("</td>\n");
                            break;
                        case 53:
                            row.append("<td>Authorization Code: ").append(user.getAuthorizationCode() != null ? user.getAuthorizationCode() : "").append("</td>\n");
                            break;
                        case 54:
                            String mltcCaseManager = "";
                            if (user.getMedication() != null && user.getMedication().getMltc() != null) {
                                mltcCaseManager += user.getMedication().getMltc().getName();
                            }
                            if (user.getCaseManager() != null) {
                                mltcCaseManager += user.getCaseManager().getFirstName() + " " + user.getCaseManager().getLastName();
                            }
                            row.append("<td>Mltc Case Manager: ").append(mltcCaseManager).append("</td>\n");
                            break;
                        case 55:
                            row.append("<td>Visiting Nurse: ").append(user.getMedication() != null ? user.getMedication().getVisitingNurse() : "").append("</td>\n");
                            break;
                        case 56:
                            row.append("td>Visiting Nurse Schedule: ").append(user.getMedication() != null ? user.getMedication().getVisitingNurseSchedule() : "").append("</td>\n");
                            break;
                        case 57:
                            String mltcCaseManagerSecond = ""; // ?? duplicate
                            if (user.getMedication() != null && user.getMedication().getMltc() != null) {
                                mltcCaseManagerSecond += user.getMedication().getMltc().getName();
                            }
                            if (user.getCaseManager() != null) {
                                mltcCaseManagerSecond += user.getCaseManager().getFirstName() + " " + user.getCaseManager().getLastName();
                            }
                            row.append("<td>Mltc Case Manager: ").append(mltcCaseManagerSecond).append("</td>\n");
                            break;
                        case 58:
                            row.append("<td>Emergency Contact First Name: ").append(user.getEmergencyContactFirstName() != null ? user.getEmergencyContactFirstName() : "").append("</td>\n");
                            break;
                        case 59:
                            row.append("<td>Emergency Contact Middle Name: ").append(user.getEmergencyContactMiddleName() != null ? user.getEmergencyContactMiddleName() : "").append("</td>\n");
                            break;
                        case 60:
                            row.append("<td>Emergency Contact Last Name: ").append(user.getEmergencyContactLastName() != null ? user.getEmergencyContactLastName() : "").append("</td>\n");
                            break;
                        case 61:
                            row.append("<td>Relationship to Participant: ").append(user.getRelationshipToParticipant() != null ? user.getRelationshipToParticipant() : "").append("</td>\n");
                            break;
                        case 62:
                            row.append("<td>Emergency Contact Phone: ").append(user.getEmergencyContactPhone() != null ? user.getEmergencyContactPhone() : "").append("</td>\n");
                            break;
                        case 63:
                            row.append("<td>Emergency Contact Address One: ").append(user.getEmergencyContactAddressOne() != null ? user.getEmergencyContactAddressOne() : "").append("</td>\n");
                            break;
                        case 64:
                            row.append("<td>Emergency Contact Address Two: ").append(user.getEmergencyContactAddressTwo() != null ? user.getEmergencyContactAddressTwo() : "").append("</td>\n");
                            break;
                        case 65:
                            row.append("<td>Emergency Contact City: ").append(user.getEmergencyContactCity() != null ? user.getEmergencyContactCity() : "").append("</td>\n");
                            break;
                        case 66:
                            row.append("<td>Emergency Contact State: ").append(user.getEmergencyContactState() != null ? user.getEmergencyContactState() : "").append("</td>\n");
                            break;
                        case 67:
                            row.append("<td>Emergency Contact Zipcode: ").append(user.getEmergencyContactZipcode() != null ? user.getEmergencyContactZipcode() : "").append("</td>\n");
                            break;
                        case 68:
                            StringBuilder getFoodAllergiesStr = new StringBuilder();
                            if (user.getFoodAllergies() != null && !user.getFoodAllergies().isEmpty()) {
                                for (FoodAllergy foodAllergy : user.getFoodAllergies()) {
                                    getFoodAllergiesStr.append(foodAllergy.getName()).append(",");
                                }
                            }
                            row.append("<td>Food Allergies: ").append(getFoodAllergiesStr.toString()).append("</td>\n");
                            break;
                        case 69:
                            row.append("<td>Medical Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getMedicalStatus() : "").append("</td>\n");
                            break;
                        case 70:
                            row.append("<td>Nutrition Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getNutritionStatus() : "").append("</td>\n");
                            break;
                        case 71:
                            row.append("<td>Sensory Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getSensoryStatus() : "").append("</td>\n");
                            break;
                        case 72:
                            row.append("<td>Medication Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getMedicationStatus() : "").append("</td>\n");
                            break;
                        case 73:
                            row.append("<td>Pain Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getPainStatus() : "").append("</td>\n");
                            break;
                        case 74:
                            row.append("<td>Congonitive Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getCongonitiveStatus() : "").append("</td>\n");
                            break;
                        case 75:
                            row.append("<td>Psychosocial Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getPsychosocialStatus() : "").append("</td>\n");
                            break;
                        case 76:
                            row.append("<td>Spiritual Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getSpiritualStatus() : "").append("</td>\n");
                            break;
                        case 77:
                            row.append("<td>Communication Status: ").append(user.getServicePlan() != null ? user.getServicePlan().getCommunicationStatus() : "").append("</td>\n");
                            break;
                        case 78:
                            row.append("<td>Expected Outcome: ").append(user.getServicePlan() != null ? user.getServicePlan().getExpectedOutcome() : "").append("</td>\n");
                            break;
                        case 79:
                            row.append("<td>Insurance Start Date: ").append((user.getServicePlan() != null && user.getServicePlan().getInsuranceStart() != null) ? DateUtils.dateToString("MM/dd/YYYY", user.getServicePlan().getInsuranceStart()) : "").append("</td>\n");
                            break;
                        case 80:
                            row.append("<td>Insurance End Date: ").append((user.getServicePlan() != null && user.getServicePlan().getInsuranceEnd() != null) ? DateUtils.dateToString("MM/dd/YYYY", user.getServicePlan().getInsuranceEnd()) : "").append("</td>\n");
                            break;
                        case 81:
                            row.append("<td>Outcome Approved By Target Date: ").append((user.getServicePlan() != null && user.getServicePlan().getOutcomeTargetDate() != null) ? DateUtils.dateToString("MM/dd/YYYY", user.getServicePlan().getOutcomeTargetDate()) : "").append("</td>\n");
                            break;
                        case 82:
                            row.append("<td>Outcome Approved By Date Achieved: ").append((user.getServicePlan() != null && user.getServicePlan().getOutcome_date_achieved() != null) ? DateUtils.dateToString("MM/dd/YYYY", user.getServicePlan().getOutcome_date_achieved()) : "").append("</td>\n");
                            break;
                        case 83:
                            row.append("<td>Plan Start: ").append((user.getServicePlan() != null && user.getServicePlan().getPlanStart() != null) ? DateUtils.dateToString("MM/dd/YYYY", user.getServicePlan().getPlanStart()) : "").append("</td>\n");
                            break;
                        case 84:
                            row.append("<td>Plan End: ").append((user.getServicePlan() != null && user.getServicePlan().getPlanEnd() != null) ? DateUtils.dateToString("MM/dd/YYYY", user.getServicePlan().getPlanEnd()) : "").append("</td>\n");
                            break;
                        case 85:
                            row.append("<td>Activity And Level Of Engagement: ").append(user.getServicePlan() != null ? user.getServicePlan().getActivityAndEngagementLevel() : "").append("</td>\n");
                            break;
                        case 86:
                            row.append("<td>Capacity For Self Estimate: ").append(user.getServicePlan() != null ? user.getServicePlan().getCapacity_self_estimate() : "").append("</td>\n");
                            break;
                        case 87:
                            row.append("<td>ADLs Level Of Care: ").append(user.getServicePlan() != null ? user.getServicePlan().getAdlsLevelCare() : "").append("</td>\n");
                            break;
                        case 88:
                            row.append("<td>Days: ").append(user.getServicePlan() != null ? user.getServicePlan().getDays() : "").append("</td>\n");
                            break;
                        case 89:
                            row.append("<td>Capacity Independence: ").append(user.getServicePlan() != null ? user.getServicePlan().getCapacity_independence_self_care() : "").append("</td>\n");
                            break;
                        case 90:
                            row.append("<td>Service Plan Doc File: ").append((user.getServicePlan() != null && user.getServicePlan().getDocFile() != null) ? user.getServicePlan().getDocFile().getFileName() : "").append("</td>\n");
                            break;
                        case 91:
                            String servicePlanSignatureURL = "";
                            if (user.getServicePlan() != null && user.getServicePlan().getSignature() != null
                                    && (user.getServicePlan().getSignature().getSignatureFile().getFileUrl() != null || !"".equals(user.getServicePlan().getSignature().getSignatureFile().getFileUrl()))) {
                                servicePlanSignatureURL = "<img height='20' src=\"" + user.getProfilePhoto().getFileUrl() + "\"/>";
                            }
                            row.append("<td>Service Plan Signature: ").append(servicePlanSignatureURL).append("</td>\n");
                            break;
                    }
                }
            }


            row.append("</tr>");
        }


        return row.toString();
    }


}
