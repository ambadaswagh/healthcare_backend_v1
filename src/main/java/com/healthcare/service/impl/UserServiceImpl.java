package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.google.common.base.Strings;
import com.google.maps.model.GeocodingResult;
import com.healthcare.dto.SeniorMapDTO;
import com.healthcare.model.entity.*;
import com.healthcare.model.enums.RoleLevelEnum;
import com.healthcare.repository.*;
import com.healthcare.service.FoodAllergyService;
import com.healthcare.service.GoogleApiService;
import com.healthcare.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.dto.UserSearchDTO;
import com.healthcare.model.entity.assessment.HealthStatus;
import com.healthcare.model.entity.assessment.Housing;
import com.healthcare.model.entity.assessment.Nutrition;
import com.healthcare.model.enums.StatusEnum;
import com.healthcare.model.response.Response;
import com.healthcare.repository.assessment.HealthStatusRepository;
import com.healthcare.repository.assessment.HousingRepository;
import com.healthcare.repository.assessment.NutritionRepository;
import com.healthcare.service.UserService;
import com.healthcare.util.PasswordUtils;

import io.jsonwebtoken.lang.Collections;

import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;
import static com.healthcare.model.enums.RoleLevelEnum.*;

@Service
@Transactional
public class UserServiceImpl extends BasicService<User, UserRepository> implements UserService {
    private static final String KEY = User.class.getSimpleName();
    public static final String DEFAULT_RIDE_LINE_COLOR = "Yellow";
    public static final String DEFAULT_RIDE_LINE_COLOR_CODE = "#FFFF00";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepository userRepository;

    @Autowired
 	private HousingRepository housingRepository;
 	@Autowired
 	private HealthStatusRepository healthStatusRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideLineColorRepository rideLineColorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private GoogleApiService googleApiService;

    @Autowired
    private FoodAllergyRepository foodAllergyRepository;

    @Autowired
    private AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;

    @Value("${analytic.birthday.dateformat}")
    private String birthdayDateFormat;

    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @Autowired
    private RedisTemplate<String, Housing> redisTemplateHousing;
    @Autowired
    RedisTemplate<String, HealthStatus> redisTemplateHealthStatus;
    @Autowired
    private RedisTemplate<String, Nutrition> redisTemplateNutrition;
    @Autowired
	EmployeeRepository employeeRepository;
    @Autowired
    private FoodAllergyService foodAllergyService;

    @Override
    @Transactional
    public User save(User user) {
    	if(user.getId() != null){
    		user.setUpdatedAt(new Timestamp(new Date().getTime()));
    	} else {
    		user.setCreatedAt(new Timestamp(new Date().getTime()));
    		user.setUpdatedAt(new Timestamp(new Date().getTime()));
    	}
    	if(user.getDateOfBirth() != null){
	    	user.getDateOfBirth().setHours(12);
	    	user.getDateOfBirth().setMinutes(0);
	    	user.getDateOfBirth().setSeconds(0);
    	}
    	if(user.getAuthorizationStart() != null){
    		user.getAuthorizationStart().setHours(12);
        	user.getAuthorizationStart().setMinutes(0);
        	user.getAuthorizationStart().setSeconds(0);
    	}
    	if(user.getAuthorizationEnd() != null){
    		user.getAuthorizationEnd().setHours(12);
        	user.getAuthorizationEnd().setMinutes(0);
        	user.getAuthorizationEnd().setSeconds(0);
    	}
    	if(user.getEligiableStart() != null){
    		user.getEligiableStart().setHours(12);
        	user.getEligiableStart().setMinutes(0);
        	user.getEligiableStart().setSeconds(0);
    	}
    	if(user.getEligiableEnd() != null){
    		user.getEligiableEnd().setHours(12);
        	user.getEligiableEnd().setMinutes(0);
        	user.getEligiableEnd().setSeconds(0);
    	}
    	if(user.getAssessmentStartDate() != null){
    		user.getAssessmentStartDate().setHours(12);
        	user.getAssessmentStartDate().setMinutes(0);
        	user.getAssessmentStartDate().setSeconds(0);
    	}
    	if(user.getVacationStart() != null){
    		user.getVacationStart().setHours(12);
        	user.getVacationStart().setMinutes(0);
        	user.getVacationStart().setSeconds(0);
    	}
    	if(user.getVacationEnd() != null){
    		user.getVacationEnd().setHours(12);
        	user.getVacationEnd().setMinutes(0);
        	user.getVacationEnd().setSeconds(0);
    	}
        user = userRepository.save(user);
        redisTemplate.opsForHash().put(KEY, user.getId(), user);
        return user;
    }

    @Override
    @Transactional
    public User findById(Long id) {
        if (redisTemplate.opsForHash().hasKey(KEY, id)) {
          try {
            return (User) redisTemplate.opsForHash().get(KEY, id);
          } catch (Exception e) {
            logger.error(e.getMessage());
          }
        }
        return userRepository.findOne(id);
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        userRepository.delete(id);
        return redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    @Transactional
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Response login(AuthRequest authenticationRequest) {
        Response response = null;
        User user = null;
        try {
            user = userRepository.findByUsername(authenticationRequest.getUsername());
            if (user != null) {
                if (PasswordUtils.checkPassword(authenticationRequest.getPassword(), user.getPassword())) {
                    response = new Response(Response.ResultCode.SUCCESS, user);
                } else {
                    response = new Response(Response.ResultCode.INVALID_PASSWORD, null, "Invalid password");
                }
            } else {
                response = new Response(Response.ResultCode.INVALID_USERNAME, null, "Invalid username");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception in AdminServiceImpl, login(), e: " + e.toString());
            response = new Response(Response.ResultCode.ERROR, null, e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional
    public Response logout(String sessionId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        Map<Object, Object> userMap = redisTemplate.opsForHash().entries(KEY);
        List<User> userList = Collections.arrayToList(userMap.values().toArray());
        if (userMap.isEmpty())
            userList = userRepository.findAll();
        return userList;
    }

    @Override
    @Transactional
    public List<User> findByAgencyId(Long id) {
        return userRepository.findUserByAgency(id);
    }

    public List<User> findByCompanyId(Long id) {
        List<User> userList = userRepository.findAll();
        return userList.stream().filter(user -> user.getCompany().getId() == id).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long disableById(Long id) {
        User user = null;
        if (redisTemplate.opsForHash().hasKey(KEY, id))
            user = (User) redisTemplate.opsForHash().get(KEY, id);
        else
            user = userRepository.findOne(id);
        if (user != null && user.getId() != null) {
            user.setStatus(StatusEnum.INACTIVE);
            userRepository.save(user);
            redisTemplate.opsForHash().put(KEY, user.getId(), user);
            return user.getId();
        }
        return null;
    }

    @Override
    public boolean validPin(String pin, Long agencyId) {
        User user = userRepository.findUserByPIN(pin);
        if (user == null) {
        	Employee emp = employeeRepository.findByPin(pin);
        	if(emp == null)
        		return true;
        	else
        		return false;// invalid pin - exists in db
        } else {
            return false;// invalid pin - exists in db
        }
    }

    @Override
    public String generatePin(Long agencyId) {
        Random rand = new Random();
        String pin = rand.nextInt(10000) + "";  // 0000 - 9999
        while (!validPin(pin, agencyId)) {
            pin = rand.nextInt(10000) + "";  // 0000 - 9999
        }
        return pin;
    }

    @Override
    public boolean isPinUsed(String pin, Long agencyId, Long userId) {
        User user = getUserByPIN(pin, agencyId);

        if (user == null) {
            return false;
        }

        if (userId != null && user.getId() == userId) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isBoxNumberUsed(String boxNumber, Long agencyId, Long userId) {
        User user = getUserByBoxNumber(boxNumber, agencyId);

        if (user == null) {
            return false;
        }

        if (userId != null && user.getId() == userId) {
            return false;
        }

        return true;
    }

    @Override
    public List<String> findAllPIN(Long adminId) {
        Admin admin = adminRepository.findOne(adminId);
        List<String> pins = new ArrayList<String>();
        if (admin == null)
            return pins;

        AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(admin.getId());
        if (adminAgencyCompanyOrganization == null)
            return pins;
        Agency agency = adminAgencyCompanyOrganization.getAgency();
        if (agency == null)
            return pins;
        return userRepository.findAllPINForAgency(agency.getId());
    }

    @Override
    public User getUserByPIN(String pin, Long agencyId) {
        return userRepository.findUserByPINForAgency(pin, agencyId);
    }

    @Override
    public User getUserByBoxNumber(String boxNumber, Long agencyId) {
        List<User> users = userRepository.findUserByBoxNumberForAgency(boxNumber, agencyId);
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public List<User> getUsersByAgency(Long agencyId) {
        return userRepository.findUserByAgency(agencyId);
    }

    public List<User> searchUser(String search) {
        return userRepository.searchUser(search);
    }

    @Override
    public List<User> searchUserForAdmin(String search, AdminAgencyCompanyOrganization adminAgencyCompanyOrganization) {
        switch(RoleLevelEnum.getRoleLevelEnum((int) adminAgencyCompanyOrganization.getAdmin().getRole().getLevel())) {
            case SUPER_ADMIN:
            case SUB_SUPER_ADMIN: return userRepository.searchUser(search);
            case COMPANY_ADMIN:
            case SUB_COMPANY_ADMIN: return userRepository.searchUserForCompany(search, adminAgencyCompanyOrganization.getCompany().getId());
            case SENIOR_CENTER_ADMIN:
            case SUB_SENIOR_CENTER_ADMIN: return userRepository.searchUserForAgency(search, adminAgencyCompanyOrganization.getAgency().getId());
            default: return new ArrayList();
        }
    }

    public List<User> searchUserForRide(Long agenyId, Long companyId) {
        return userRepository.searchUserForRide(agenyId, companyId);
    }

    public List<User> searchUser(UserSearchDTO search) {
        List<User> returnList = new ArrayList<User>();

        List<User> result = findAll();

        for (User user : result) {

            boolean userMatched = true;

            if (!checkContains(user.getFirstName(), search.getFirstName())) {
                userMatched = false;
            }

            if (userMatched && !checkContains(user.getLastName(), search.getLastName())) {
                userMatched = false;
            }

            if (userMatched && !checkContains(user.getEmergencyContactPhone(), search.getEmergencyContactPhone())) {
                userMatched = false;
            }

            // if (userMatched && !checkContains(user.getFamilyDoctor(),
            // search.getFamilyDoctor())) {
            // userMatched = false;
            // }
            //
            // if (userMatched && !checkContains(user.getFamilyDoctorTel(),
            // search.getFamilyDoctorTel())) {
            // userMatched = false;
            // }

            if (userMatched && !checkContains(user.getPin(), search.getPin())) {
                userMatched = false;
            }

            if (userMatched && !checkContains(user.getEmail(), search.getEmail())) {
                userMatched = false;
            }

            if (userMatched && !checkContains(user.getEmail(), search.getEmail())) {
                userMatched = false;
            }

            if (userMatched && !checkContains(user.getMedicareNumber(), search.getMedicareNumber())) {
                userMatched = false;
            }

            if (userMatched && !checkContains(user.getUsername(), search.getUsername())) {
                userMatched = false;
            }

            if (userMatched) {
                returnList.add(user);
            }
        }

        return returnList;
    }

    public boolean checkContains(String userString, String searchString) {
        if (StringUtils.isEmpty(searchString)) {
            return true;
        }

        if (userString == null) {
            return false;
        }

        if (userString.contains(searchString)) {
            return true;
        }

        return false;
    }

    public List<User> getUserHaveBirthday(DateTime today) {
        if (today == null)
            return null;
        return userRepository.getUserBornInDateAndMonth(today.getMonthOfYear(), today.getDayOfMonth());
    }

    public List<User> getUserHaveBirthday(String date) {
        DateTime today = null;
        if (date == null || date.isEmpty()) {
            today = new DateTime();
        } else {
            final DateTimeFormatter formatter = DateTimeFormat.forPattern(birthdayDateFormat);
            today = formatter.parseDateTime(date);
        }
        return getUserHaveBirthday(today);
    }

    public Page<User> getAssessmentReminder(int days, Admin permissionAdmin, Pageable pageable) {
        if (days < 0)
            return new PageImpl<User>(new ArrayList<>());

        List<User> users = new ArrayList<User>();

        if (isSuperAdmin(permissionAdmin)) {
            users = userRepository.searchUserForAssessmentReminder();
        }

        List<User> assUser = new ArrayList<>();
        for(User user : users) {
            Long time = new Date().getTime() - user.getAssessmentStartDate().getTime();
            long difference = time.longValue()/(24 * 60 * 60 * 1000);

            difference = difference/(30 * user.getAssessmentDuration());


            int diffMonths = (int)((difference == 0 ? 1 : difference) * user.getAssessmentDuration());
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(user.getAssessmentStartDate());
            startDate.add(Calendar.MONTH, + diffMonths);
            startDate.set(Calendar.HOUR, 12);
            boolean userAdded = false;
            Date nextAssessmentDate = startDate.getTime();
            int dayDiff = Days.daysBetween(new DateTime(new Date()), new DateTime(nextAssessmentDate)).getDays();
            if(dayDiff <= days && dayDiff >= 0) {
                user.setNextAssessmentDate(nextAssessmentDate);
                assUser.add(user);
                userAdded = true;
            }

            if(!userAdded) {
                int months = (int) ((difference + 1) * user.getAssessmentDuration());
                Calendar startDateNextIteration = Calendar.getInstance();
                startDateNextIteration.setTime(user.getAssessmentStartDate());
                startDateNextIteration.add(Calendar.MONTH, +months);
                startDateNextIteration.set(Calendar.HOUR, 12);

                Date nextIterationAssessmentDate = startDateNextIteration.getTime();

                if (Days.daysBetween(new DateTime(new Date()), new DateTime(nextIterationAssessmentDate)).getDays() <= days) {
                    user.setNextAssessmentDate(nextIterationAssessmentDate);
                    assUser.add(user);
                }
            }
        }

        java.util.Collections.sort(assUser, new Comparator<User>() {
            public int compare(User e1, User e2) {
                return e1.getNextAssessmentDate().compareTo(e2.getNextAssessmentDate());
            }
        });

        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > assUser.size() ? assUser.size() : (start + pageable.getPageSize());
        Page<User> pages = new PageImpl<User>(assUser.subList(start, end), pageable, assUser.size());
        return pages;
    }

    public Page<User> getUserAuthorizationEndNextDays(int days, Admin permissionAdmin, Pageable pageable) {
        if (days < 0)
            return new PageImpl<User>(new ArrayList<>());
        DateTime today = new DateTime();
        DateTime endDay = today.plusDays(days);


        Page<User> returnedPages = null;

        if(isSuperAdmin(permissionAdmin)){
            returnedPages = userRepository.getAuthorizationEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
        } else if (isCompanyAdmin(permissionAdmin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

            if (adminAgencyCompanyOrganization != null) {
                returnedPages = userRepository.getAuthorizationEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                        adminAgencyCompanyOrganization.getCompany().getId(), pageable);
            }

        } else if (isAgencyAdmin(permissionAdmin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                returnedPages = userRepository.getAuthorizationEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                        adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
            }
        }

        return returnedPages;
    }

    public Page<User> getUserAssessmentEndNextDays(int days, Pageable pageable) {
        if (days < 0)
            return new PageImpl<User>(new ArrayList<>());
        DateTime today = new DateTime();
        DateTime endDay = today.plusDays(days);
        return userRepository.getAssessmentEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
    }

    public List<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Page<User> getAuthorizationBornFromDateToDate(int days, Admin permissionAdmin, Pageable pageable) {

      String toDate = DateUtils.getMonthAndDateFromDate(DateUtils.getFutureDayBetweenInterval(days));
      String fromDate =  DateUtils.getMonthAndDateFromDate(new Date());
      Page<User> returnedPages = null;

      if(isSuperAdmin(permissionAdmin)){
        returnedPages = userRepository.getAuthorizationBornFromDateToDate(fromDate, toDate, pageable);
      } else if (isCompanyAdmin(permissionAdmin)) {
        AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());
        if (adminAgencyCompanyOrganization != null) {
            returnedPages = userRepository.getAuthorizationBornFromDateToDateByCompany(
                    fromDate, toDate, adminAgencyCompanyOrganization.getCompany().getId(), pageable);
        }
      } else if (isAgencyAdmin(permissionAdmin)) {
        AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());
        if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
            returnedPages = userRepository.getAuthorizationBornFromDateToDateByCompanyAgency(
                     fromDate, toDate,
                    adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
        }
      }
      return returnedPages;
    }

    public List<User> findAllSeniorsForRideLine(Long id) {
        List<User> users = userRepository.findAllSeniorsForRideLine(id);
        return users;
    }

    public List<User> findAllSeniorsWithoutRideLine(Long agencyId, Long companyId) {
        List<User> users = userRepository.findAllSeniorsWithoutRideLine(agencyId, companyId);
        return users;
    }


    @Override
    @Transactional
    public List<SeniorMapDTO> getSeniorMapLocations(Date date) {

        List<User> users = userRepository.findAll();
        if (!CollectionUtils.isEmpty(users)) {
            List<Ride> rides = rideRepository.findByDate(date);

            List<SeniorMapDTO> seniorMapDTOs = users.stream().map(this::getSeniorMapDTO).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(rides)) {
                updateColor(seniorMapDTOs, rides);
            }

            return seniorMapDTOs;
        }


        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<SeniorMapDTO> getSeniorMapLocations(Date date, Long companyId) {

        List<User> users = userRepository.findByCompanyId(companyId);
        if (!CollectionUtils.isEmpty(users)) {
            List<Ride> rides = rideRepository.findByDate(date);

            List<SeniorMapDTO> seniorMapDTOs = users.stream().map(this::getSeniorMapDTO).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(rides)) {
                updateColor(seniorMapDTOs, rides);
            }

            return seniorMapDTOs;
        }


        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<SeniorMapDTO> getSeniorMapLocations(Date date, Long companyId, Long agencyId) {

        List<User> users = userRepository.findAllSeniorsForCompanyAndAgency(companyId, agencyId);
        if (!CollectionUtils.isEmpty(users)) {
            List<Ride> rides = rideRepository.findByDate(date);

            List<SeniorMapDTO> seniorMapDTOs = users.stream().map(this::getSeniorMapDTO).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(rides)) {
                updateColor(seniorMapDTOs, rides);
            }

            return seniorMapDTOs;
        }


        return new ArrayList<>();
    }

    private void updateColor(List<SeniorMapDTO> seniorMapDTOs, List<Ride> rides) {
        for (SeniorMapDTO seniorMapDTO : seniorMapDTOs) {
            seniorMapDTO.setColor(getColor(seniorMapDTO.getId(), rides));
        }
    }

    private String getColor(Long id, List<Ride> rides) {
        String color = rides.stream().filter(ride -> (ride.getUser().getId() == id)).findFirst().map(Ride::getRideLine).map(RideLine::getRideLineColor).orElse(DEFAULT_RIDE_LINE_COLOR);
        RideLineColor rideLineColor = rideLineColorRepository.findByName(color);
        if (rideLineColor == null || rideLineColor.getColorCode() == null) {
            return DEFAULT_RIDE_LINE_COLOR_CODE;
        } else {
            return  rideLineColor.getColorCode();
        }
    }

    private SeniorMapDTO  getSeniorMapDTO(User user) {
        SeniorMapDTO seniorMapDTO = new SeniorMapDTO();
        seniorMapDTO.setId(user.getId());
        seniorMapDTO.setFirstName(user.getFirstName());
        seniorMapDTO.setLastName(user.getLastName());

        String address = user.getAddressOne() + " " + Optional.ofNullable(user.getAddressTwo()).orElse("") + " " + Optional.ofNullable(user.getCity()).orElse("") + " " + Optional.ofNullable(user.getState()).orElse("") + " " + Optional.ofNullable(user.getZipcode()).orElse("");

        address = address.trim();

        seniorMapDTO.setAddress(address);

        if (user.getAddrLat() == null || user.getAddLng()== null || StringUtils.isEmpty(user.getAddrLat()) || StringUtils.isEmpty(user.getAddLng())){
            if (!Strings.isNullOrEmpty(address)){
                GeocodingResult geo = googleApiService.getGeocoding(address);
                if (geo != null) {
                    String lat = String.format("%.6f", geo.geometry.location.lat) ;
                    String lng =  String.format("%.6f", geo.geometry.location.lng);

                    user.setAddrLat(lat);
                    user.setAddLng(lng);
                    userRepository.save(user);
                    seniorMapDTO.setAddrLat(lat);
                    seniorMapDTO.setAddrLng(lng);
                }
            }
        } else {
            seniorMapDTO.setAddrLat(user.getAddrLat());
            seniorMapDTO.setAddrLng(user.getAddLng());
        }

        seniorMapDTO.setColor(DEFAULT_RIDE_LINE_COLOR);
        return seniorMapDTO;
    }

    @Override
    public Housing save(Housing housing) {
        housing = housingRepository.save(housing);
        redisTemplateHousing.opsForHash().put(KEY, housing.getId(), housing);
        return housing;
    }
    @Override
    public HealthStatus save(HealthStatus healthStatus) {
        healthStatus = healthStatusRepository.save(healthStatus);
        redisTemplateHealthStatus.opsForHash().put(KEY, healthStatus.getId(), healthStatus);
        return healthStatus;
    }

    @Override
    public Nutrition save(Nutrition nutrition) {
        nutrition = nutritionRepository.save(nutrition);
        redisTemplateNutrition.opsForHash().put(KEY, nutrition.getId(), nutrition);
        return nutrition;
    }

    @Override
    public HealthStatus findAllHealthStatus() {
        HealthStatus healthStatus = healthStatusRepository.findAllHealthStatus();
        return healthStatus;
    }

    @Override
    public Housing findAllHousing() {
        Housing housing = housingRepository.findAllHousing();
        return housing;
    }

	/*
	@Override
	public Nutrition findAllNutrition() {
		Nutrition nutrition = nutritionRepository.findAllNutrition();
		return nutrition;
	}
	*/

    @Override
    public List<FoodAllergy> findAllAllergies(Long id) {
      User user = findById(id);
      if(user.getFoodAllergies() ==  null)
        return new ArrayList<>();

      return user.getFoodAllergies();
    }

    @Override
    public Page<User> findByCompanyAndAgency(Long companyId, Long agencyId, Pageable pageable) {
        return userRepository.findByCompanyAndAgency(companyId, agencyId, pageable);
    }
    
    

    @Override
    public Page<User> findByCompanyPage(Long companyId, Pageable pageable) {
        return userRepository.findByCompanyPage(companyId, pageable);
    }

    @Override
    public List<FoodAllergy> saveAllAllergies(Long id, List<FoodAllergy> allergies) {
      List<FoodAllergy> newAllergyList = new ArrayList<>();
      for (FoodAllergy allergy : allergies ) {
        if(allergy.getId() == null) {
          FoodAllergy oldAllergy = foodAllergyRepository.findByName(allergy.getName());
          allergy.setAddedByUser(true);
          if(oldAllergy == null) {
            allergy = foodAllergyService.save(allergy);
          }else{
            allergy = oldAllergy;
          }
        }
        newAllergyList.add(allergy);
      }
      User user = findById(id);
      user.setFoodAllergies(newAllergyList);
      user = save(user);
      return user.getFoodAllergies();
    }

    public Page<User> getVacationSeniorReminder(int days, Admin permissionAdmin, Pageable pageable) {
        if (days < 0)
            return new PageImpl<User>(new ArrayList<>());
        DateTime today = new DateTime();
        DateTime endDay = today.plusDays(days);

        Page<User> returnedPages = null;

        if(isSuperAdmin(permissionAdmin)){
            returnedPages = userRepository.getVacationSeniorFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
        } else if (isCompanyAdmin(permissionAdmin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

            if (adminAgencyCompanyOrganization != null) {
                returnedPages = userRepository.getVacationSeniorFromDayToDayByCompany(
                        new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                        adminAgencyCompanyOrganization.getCompany().getId(), pageable);
            }

        } else if (isAgencyAdmin(permissionAdmin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                returnedPages = userRepository.getVacationSeniorFromDayToDayByCompanyAgency(
                        new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                        adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
            }
        }

        return returnedPages;
    }

	@Override
	public void update_note(Long id, String note) {
		// TODO Auto-generated method stub
		userRepository.updateNotes(id, note);
	}

	@Override
    public List<User> searchUserByFirstName(String search) {
        return userRepository.searchUserByFirstName(search);
    }

    @Override
    public List<User> searchUserByFirstNameByCompany(Long companyId, String search) {
        return userRepository.searchUserByFirstNameByCompany(companyId, search);
    }

    @Override
    public List<User> searchUserByFirstNameByCompanyAgency(Long companyId, Long agencyId, String search) {
        return userRepository.searchUserByFirstNameByCompanyAgency(companyId, agencyId, search);
    }

	@Override
	public Page<User> findByAgencyPage(Long agencyId, Pageable pageable) {
		return userRepository.findByAgencyPage(agencyId, pageable);
	}
}
