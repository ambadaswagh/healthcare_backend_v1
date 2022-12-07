package com.healthcare.integration.service;

import com.healthcare.model.entity.*;
import com.healthcare.model.enums.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class TestEntityFactory  {
	public static Calendar eligiableStart = Calendar.getInstance();
	public static Calendar eligiableEnd = Calendar.getInstance();
	public static String insuranceEligiable = "insuranceEligiable";
	public static Calendar insuranceStart = Calendar.getInstance();
	public static Calendar insuranceEnd = Calendar.getInstance();
	public static String username = "user";
	public static String password = "PASS";

	public static String firstName = "Homer";
	public static String middleName = "J";
	public static String lastName = "Simpson";
	public static String socialSecurityNumber = "1234";
	public static String phone = "1234560000";
	public static String addressType = "Home";
	public static String addressOne = "SpringField N345";
	public static String city = "City ABC";
	public static String zipcode = "00000";
	public static Calendar dob = Calendar.getInstance();
	public static String payerUserId = "99999";
	public static String medicaIdNumber = "556677";
	public static String medicareNumber = "223344";
	public static String emergencyContactPhone = "9876545555";
	public static String emergencyContactFirstName = "Marge";
	public static String emergencyContactMiddleName = "J";
	public static String emergencyContactLastName = "Bouvier";
	public static String emergencyContactCity = "Miami";
	public static String emergencyContactState = "Florida";
	public static String emergencyContactZipcode = "32656";
	public static String relationshipToParticipant = "Wife";
	public static String familyDoctor = "Dr. Z";
	public static String familyDoctorAddress = "Z St";
	public static String familyDoctorTel = "9996663334";
	public static String comment = "No comments";
	public static String vacationNote = ".";

	public static String licenseNo = "12D31";
	public static int trackingMode = 1;
	public static String contactPerson = "Joe";
	public static String addressTwo = "A st";
	public static String state = StateEnum.FLORIDA.name();
	public static String timezone = "UTC";
	public static String holiday = "12";
	public static String fax = "12212444";
	public static String email = "firstname@yahoo.com";
	public static String federalTax = "federalTax";
	public static Calendar federalTaxStart = Calendar.getInstance();
	public static Calendar federalTaxExpire = Calendar.getInstance();
	public static String stateTax = "stateTax";
	public static Calendar stateTaxStart = Calendar.getInstance();
	public static Calendar stateTaxExpire = Calendar.getInstance();
	public static Calendar worktimeStart = Calendar.getInstance();
	public static Calendar worktimeEnd = Calendar.getInstance();

	public static String ip = "127.0.0.1";
	public static String secondaryPhone = "1234560001";
	public static String profilePhoto = "XXXXXXXXXX";
	public static String deviceAddress = "City ABC";
	public static String rememberToken = "00000";

	public static Timestamp checkInTime = new Timestamp(new Date().getTime());
	public static Timestamp checkOutTime = new Timestamp(new Date().getTime());
	public static String userComments = "all ok";
	public static String notes = ".";
	public static String selectedTable = "TABLE 1";
	public static String selectedSeat = "AB";
	public static String userSignature = "userSignature";

	public static String billingCode = "billing code";
	public static double expectedMoney = 20.0;
	public static double actualMoney = 50.0;

	public static String itemName = "help on shopping";
	public static String itemNote = "help on shopping note";

	public static String levelName = "Level Name";
	public static long level = 99999999;
	public static long status = 1;
	public static int iStatus = 1;

	public static String pin = "pin";

	/**
	 * Employee
	 */
	public static Long id = 1L;
	public static String eFirstName = "firstName";
	public static String eLastName = "lastName";
	public static String gender = "gender";
	public static String eSocialSecurityNumber = "socialSecurityNumber";
	public static Calendar dateOfBirth = Calendar.getInstance();
	public static String physicalExam = "physicalExam";
	public static String certificateName = "certificateName";
	public static Calendar certificateStart = Calendar.getInstance();
	public static Calendar certificateEnd = Calendar.getInstance();
	public static Calendar workStart = Calendar.getInstance();
	public static Calendar workEnd = Calendar.getInstance();
	public static String position = "biller";
	public static String manager = "manager";
	public static String type = "type";
	public static String statusEmp = "status";
	public static String backgroundCheck = "backgroundCheck";

	/**
	 * ServicePlan
	 */
	public static String approvedBy = "Manager";
	public static Calendar planStart = Calendar.getInstance();
	public static Calendar planEnd = Calendar.getInstance();
	public static String days = "MONDAY";
	public static String docUrl = "/doc/a";

	/**
	 * Activity
	 */

	public static String name = "name";
	public static Calendar createdAt = Calendar.getInstance();
	public static Calendar updatedAt = Calendar.getInstance();
	public static Calendar timeStart = Calendar.getInstance();
	public static Calendar timeEnd = Calendar.getInstance();
	public static String date = "date";
	public static String location = "location";
	public static String note = "note";

	public static Date from = Calendar.getInstance().getTime();
	public static Date to = Calendar.getInstance().getTime();


	public void init() {
		eligiableStart.set(Calendar.YEAR, 2017);
		eligiableStart.set(Calendar.MONTH, 1);
		eligiableStart.set(Calendar.DAY_OF_MONTH, 1);
		eligiableEnd.set(Calendar.YEAR, 2017);
		eligiableEnd.set(Calendar.MONTH, 12);
		eligiableEnd.set(Calendar.DAY_OF_MONTH, 31);
		insuranceStart.set(Calendar.YEAR, 2016);
		insuranceStart.set(Calendar.MONTH, 1);
		insuranceStart.set(Calendar.DAY_OF_MONTH, 1);
		insuranceEnd.set(Calendar.YEAR, 2018);
		insuranceEnd.set(Calendar.MONTH, 12);
		insuranceEnd.set(Calendar.DAY_OF_MONTH, 31);
		dob.set(Calendar.YEAR, 1950);
		dob.set(Calendar.MONTH, 1);
		dob.set(Calendar.DAY_OF_MONTH, 1);
	}

	public static Agency createNewAgency(Company company, AgencyType agencyType) {
		Agency agency = new Agency();
		agency.setAgencyType(agencyType);
		agency.setCompany(company);
		agency.setAddressOne(addressOne);
		agency.setAddressTwo(addressTwo);
		agency.setCity(city);
		agency.setContactPerson(contactPerson);
		agency.setEmail(email);
		agency.setFax(fax);
		agency.setHoliday(holiday);
		agency.setLicenseNo(licenseNo);
		agency.setName("Agency Name");
		agency.setPhone(phone);
		agency.setState(state);
		agency.setTimezone(timezone);
		agency.setTrackingMode(trackingMode);
		agency.setZipcode(zipcode);
		agency.setStatus(1);
		return agency;
	}

	public static Company createNewCompany() {
		Company company = new Company();
		company.setAddressOne(addressOne);
		company.setAddressTwo(addressTwo);
		company.setCity(city);
		company.setEmail(email);
		company.setFax(fax);
		company.setFederalTax(federalTax);
		company.setFederalTaxExpire(new Timestamp(federalTaxExpire.getTimeInMillis()));
		company.setFederalTaxStart(new Timestamp(federalTaxStart.getTimeInMillis()));
		company.setFederalTaxStatus(1);
		company.setLicenseNo(licenseNo);
		company.setName("Company Name");
		company.setPhone(phone);
		company.setState(state);
		company.setStateTax(stateTax);
		company.setStateTaxExpire(new Timestamp(stateTaxExpire.getTimeInMillis()));
		company.setStateTaxStart(new Timestamp(stateTaxStart.getTimeInMillis()));
		company.setStateTaxStatus(1);
		company.setStatus(1);
		company.setWorktimeEnd(new Time(worktimeEnd.getTimeInMillis()));
		company.setWorktimeStart(new Time(worktimeStart.getTimeInMillis()));
		company.setZipcode(zipcode);
		return company;
	}

	public static AgencyType createNewAgencyType() {
		AgencyType agencyType = new AgencyType();
		agencyType.setName("Agency Type Name");
		agencyType.setStatus(1);
		return agencyType;
	}

	public static User createNewUser(Agency agency) {
		User user = new User();
		user.setEligiableStart(new Timestamp(eligiableStart.getTimeInMillis()));
		user.setEligiableEnd(new Timestamp(eligiableEnd.getTimeInMillis()));
		user.setInsuranceEligiable(insuranceEligiable);
		user.setAuthorizationStart(new Timestamp(insuranceStart.getTimeInMillis()));
		user.setAuthorizationEnd(new Timestamp(insuranceEnd.getTimeInMillis()));
		user.setUsername(username);
		user.setPassword(password);

		user.setFirstName(firstName);
		user.setMiddleName(middleName);
		user.setLastName(lastName);
		user.setGender(GenderEnum.MAN.name());
		user.setDateOfBirth(dob.getTime());
		user.setSocialSecurityNumber(socialSecurityNumber);
		user.setMedicaIdNumber(medicaIdNumber);
		user.setMedicareNumber(medicareNumber);
		user.setLanguage(LanguageEnum.ENGLISH.name());
		user.setAddressType(addressType);
		user.setAddressOne(addressOne);
		user.setCity(city); // Enum?
		user.setState(StateEnum.FLORIDA.name());
		user.setZipcode(zipcode);
		user.setPhone(phone);
		user.setStatus(StatusEnum.ACTIVE);
		user.setAgency(agency);

		user.setEmergencyContactPhone(emergencyContactPhone);
		user.setEmergencyContactFirstName(emergencyContactFirstName);
		user.setEmergencyContactMiddleName(emergencyContactMiddleName);
		user.setEmergencyContactLastName(emergencyContactLastName);
		user.setRelationshipToParticipant(relationshipToParticipant);
		user.setEmergencyContactCity(emergencyContactCity);
		user.setEmergencyContactState(emergencyContactState);
		user.setEmergencyContactZipcode(emergencyContactZipcode);


		user.setComment(comment);

		return user;
	}

	public static User createNewUser() {
		return createNewUser(null);
	}

	public static Visit createNewVisit(User user, Agency agency, ServicePlan servicePlan) {
		Visit visit = new Visit();
		visit.setUser(user);
		visit.setAgency(agency);
		visit.setCheckInTime(checkInTime);
		visit.setCheckOutTime(checkOutTime);
		visit.setSelectedSeat(selectedTable);
		visit.setSelectedSeat(selectedSeat);
//		visit.setUserSignature(null);
		visit.setServicePlan(servicePlan);
		// visit.setSelectedMeal(selectedMeal);// TODO not yet finished Meal
		// CRUD
		// visit.setPin(pin);// TODO not yet validates there
		// is asigned when agency accepts Patient
		visit.setUserComments(userComments);
		visit.setNotes(notes);
		visit.setStatus(VisitStatusEnum.RESERVED.name());
		visit.setBillingCode(billingCode);
		visit.setExpectedMoney(expectedMoney);
		visit.setActualMoney(actualMoney);

		return visit;
	}

	public static Role createNewRole(Agency agency, Employee manager, long level) {
		Role role = new Role();
		role.setLevel(level);
		role.setLevelName(levelName);
		role.setStatus(status);
		return role;
	}

	public static WorkItem createNewWorkItem() {
		WorkItem workItem = new WorkItem();
		workItem.setItemName(itemName);
		workItem.setItemNote(itemNote);
		return workItem;
	}

	public static Employee createNewEmployee(Agency agency) {
		return createNewEmployee(agency,id);
	}

	public static Employee createNewEmployee(Agency agency,Long id) {
		Employee employee = new Employee();
		employee.setId(id);
		employee.setFirstName(eFirstName);
		employee.setLastName(eLastName);
		employee.setGender(gender);
		employee.setSocialSecurityNumber(eSocialSecurityNumber);
		employee.setDateOfBirth(new Timestamp(dateOfBirth.getTimeInMillis()));
		employee.setPhysicalExam(physicalExam);
		employee.setCertificateName(certificateName);
		employee.setCertificateStart(new Timestamp(certificateStart.getTimeInMillis()));
		employee.setCertificateEnd(new Timestamp(certificateEnd.getTimeInMillis()));
		employee.setWorkStart(new Timestamp(workStart.getTimeInMillis()));
		employee.setWorkEnd(new Timestamp(workEnd.getTimeInMillis()));
		employee.setPosition(position);
//		employee.setManager(manager);
		employee.setType(type);
//		employee.setStatus(statusEmp);
		employee.setBackgroundCheck(backgroundCheck);
		employee.setAgency(agency);
		employee.setPin(pin);
		employee.setEmployeeType(EmployeeTypeEnum.SALARY);
		return employee;
	}

	public static Admin createNewAdmin(Role role) {
		Admin admin = new Admin();
		admin.setUsername(username);
		admin.setPassword(password);
		admin.setFirstName(firstName);
		admin.setMiddleName(middleName);
		admin.setLastName(lastName);
		admin.setGender(GenderEnum.MAN.name());
		admin.setPhone(phone);
		admin.setEmail(email);
		admin.setDeviceAddress(deviceAddress);
		admin.setIp(ip);
		admin.setProfilePhoto(null);
		admin.setRememberToken(rememberToken);
		admin.setSecondaryPhone(secondaryPhone);
		admin.setStatus(1);
		admin.setRole(role);
		return admin;
	}

	public static ServicePlan createNewServicePlan(Employee employee, User user) {
		ServicePlan servicePlan = new ServicePlan();
		servicePlan.setApprovedBy(approvedBy);
		servicePlan.setDays(days);
//		servicePlan.setDocUrl(docUrl);
//		servicePlan.setEmployee(employee);
		servicePlan.setPlanEnd(new Timestamp(planEnd.getTimeInMillis()));
		servicePlan.setPlanStart(new Timestamp(planStart.getTimeInMillis()));
		servicePlan.setUser(user);
		return servicePlan;
	}

	public static Activity createNewActivity(Employee employee) {
		Activity activity = new Activity();
		activity.setName(name);
		activity.setStatus(iStatus);
		// activity.setCreatedAt(new Timestamp(createdAt.getTimeInMillis()));
		// activity.setUpdatedAt(new Timestamp(updatedAt.getTimeInMillis()));
		activity.setInstructorEmployee(employee);
		activity.setTimeStart(String.valueOf(new Time(timeStart.getTimeInMillis())));
		activity.setTimeEnd(String.valueOf(new Time(timeEnd.getTimeInMillis())));
		activity.setDate(date);
		activity.setLocation(location);
		activity.setNote(note);
		return activity;
	}

	public static HealthInsuranceClaim createNewHealthInsuraceClaim(User user,Document document){
		Date signDate = Calendar.getInstance().getTime();
		HealthInsuranceClaim hic = new HealthInsuranceClaim();
		hic.setUser(user);
		hic.setReservedLocalUse("Yes");
		hic.setTotalCharge(new Double(10.11));
//		hic.setSignatureDate(signDate);
		hic.setServiceFacilityLocationInformationA("serviceFacilityLocationInformationA");
		hic.setServiceFacilityLocationInformationB("serviceFacilityLocationInformationB");
		hic.setReservedForLocalUse("Yes");
		hic.setRelationshipToInsured("Spouse");
		hic.setReferringProviderNpi("Npi");
		hic.setReferringProviderName("provider1");
		hic.setPriorAuthorizationNo("auth1");
//		hic.setPhysicianOrSupplierSignatureDate(signDate);
		hic.setPhysicianOrSupplierSignatureId(document);
		hic.setPatientUnableWorkCurrentOccupationFromDate(from);
		hic.setPatientUnableWorkCurrentOccupationFromTo(to);
		hic.setPatientSignatureDocId(document);
		hic.setPatientMaritalStatus("Married");
		hic.setPatientEmploymentStatus("Working");
		hic.setPatientCondtionRelation("patientCondtionRelation");
		hic.setPatientAccountNo("78585566");
		hic.setOutsideLab(0);
		hic.setOriginalRefNo("ref10223");
		hic.setMedicaidResubmissionCode("code11");
		hic.setInsuredZipcode("500001");
		hic.setInsuredState("NW");
		hic.setInsuredSignatureDocId(document);
		hic.setInsuredSex("Male");
		hic.setInsuredPolicyGroupOrFecaNo("2566000");
		hic.setInsuredPhoneNo("9857556525");
		hic.setInsuredLastName("last name");
		hic.setInsuredIdNo("1200");
		hic.setInsuredFirstName("first name");
		hic.setInsuredDob(dob.getTime());
		hic.setInsuredCity(city);
		hic.setInsuredAnotherHealthPlan(1);
		hic.setInsuredAddress(addressOne);
		hic.setInsurancePlanName("Bajaj");
		hic.setHospitalizationRelatedToCurrentServicesDateFrom(from);
		hic.setHospitalizationRelatedToCurrentServicesDateTo(to);
		hic.setFederalTaxIdNoEin("4552525");
		hic.setFederalTaxIdNoSsn("4552525");
		hic.setCurrentIllnessInjuryPregnancyDate(from);
		hic.setCharges(new Double(10.11));
		hic.setBillingProviderInfoAndPhNoA("billingProviderInfoAndPhNoA");
		hic.setBillingProviderInfoAndPhNoB("billingProviderInfoAndPhNoB");
		hic.setBalanceDue(new Double(9.11));
		hic.setAmountPaid(new Double(112.55));
		hic.setAcceptAssignment(1);
		hic.setCreatedAt(new Timestamp(createdAt.getTimeInMillis()));
		hic.setUpdatedAt(new Timestamp(updatedAt.getTimeInMillis()));

		PatientDiagnosis p = new PatientDiagnosis();
		p.setPatientDateOfServiceFrom(new Date());
		p.setPatientDateOfServiceTo(new Date());
		p.setDaysOrUnits("1");
		p.setPatientCharges(10);
		p.setEpsdtFamilyPlan("Family plan");
		p.setIdQual("idQual");
		p.setPatientDiagnosisPointer("NA");
		p.setPatientPlaceOfService("NY");
		p.setPatientProceduresServicesSuppliesCptHcpcs("NA");
		p.setPatientProceduresServicesSuppliesCptModifier("NA");
		p.setPatientSupplierEmgergency("NA");
		p.setRenderingProviderIdNo("NA");

		hic.setPatientDiagnosis(Collections.singletonList(p));

		return hic;
	}

	public static Document createNewDocument() {
		Document document = new Document();
		document.setEntity("User");
		document.setEntityId(123L);
		document.setFileClass("Transtportation");
		document.setStatus(DocumentStatusEnum.ACTIVE.getValue());
		document.setFilePath("/data/upload/");
		document.setFileUrl("/data/upload/test.txt");
		document.setFileName("test.txt");
		document.setFileSize(12566L);

		return document;
	}

	public static Admin getRequestAdmin(){
		Admin admin = new Admin();
		return admin;
	}
}
