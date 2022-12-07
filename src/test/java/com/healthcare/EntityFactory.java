package com.healthcare;

import com.healthcare.model.entity.*;
import com.healthcare.model.enums.*;
import com.healthcare.util.DateUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class EntityFactory {
	public Calendar eligiableStart = Calendar.getInstance();
	public Calendar eligiableEnd = Calendar.getInstance();
	public String insuranceEligiable = "insuranceEligiable";
	public Calendar insuranceStart = Calendar.getInstance();
	public Calendar insuranceEnd = Calendar.getInstance();
	public String username = "user";
	public String password = "PASS";
	public String PIN = "0000";

	public String firstName = "Homer";
	public String middleName = "J";
	public String lastName = "Simpson";
	public String socialSecurityNumber = "1234";
	public String phone = "1234560000";
	public String addressType = "Home";
	public String addressOne = "SpringField N345";
	public String city = "City ABC";
	public String zipcode = "00000";
	public Calendar dob = Calendar.getInstance();
	public String payerUserId = "99999";
	public String medicaIdNumber = "556677";
	public String medicareNumber = "223344";
	public String emergencyContactPhone = "9876545555";
	public String emergencyContactFirstName = "Marge";
	public String emergencyContactMiddleName = "J";
	public String emergencyContactLastName = "Bouvier";
	public String emergencyContactCity = "Miami";
	public String emergencyContactState = "Florida";
	public String emergencyContactZipcode = "32656";
	public String relationshipToParticipant = "Wife";
	public String familyDoctor = "Dr. Z";
	public String familyDoctorAddress = "Z St";
	public String familyDoctorTel = "9996663334";
	public String comment = "No comments";
	public String vacationNote = ".";

	public String licenseNo = "12D31";
	public int trackingMode = 1;
	public String contactPerson = "Joe";
	public String addressTwo = "A st";
	public String state = StateEnum.FLORIDA.name();
	public String timezone = "UTC";
	public String holiday = "12";
	public String fax = "12212444";
	public String email = "firstname@yahoo.com";
	public String federalTax = "federalTax";
	public Calendar federalTaxStart = Calendar.getInstance();
	public Calendar federalTaxExpire = Calendar.getInstance();
	public String stateTax = "stateTax";
	public Calendar stateTaxStart = Calendar.getInstance();
	public Calendar stateTaxExpire = Calendar.getInstance();
	public Calendar worktimeStart = Calendar.getInstance();
	public Calendar worktimeEnd = Calendar.getInstance();

	public Timestamp checkInTime = new Timestamp(new Date().getTime());
	public Timestamp checkOutTime = new Timestamp(new Date().getTime());
	public String userComments = "all ok";
	public String notes = ".";
	public String selectedTable = "TABLE 1";
	public String selectedSeat = "AB";
	public String userSignature = "userSignature";

	public String approvedBy = "Manager";
	public Timestamp planStart = new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2017-06-01").getTime());
	public Timestamp planEnd = new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2017-12-01").getTime());


	// public Timestamp outcomeTargetDate = new
	// Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2017-10-01").getTime());;
	// public Timestamp outcomeDateAchieved = new
	// Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2017-11-01").getTime());;
	// public String medicalStatus = "1";
	// public String nutritionStatus ="1";
	// public String sensoryStatus = "1";
	// public String medicationStatus = "1";
	// public String painStatus = "1";
	// public String congonitiveStatus = "1";
	// public String psychosocialStatus = "1";
	// public String spiritualStatus = "1";
	// public String communicationStatus = "1";
	// public String expectedOutcome= "expectedOutcome";
	// public String outcomeCriteria = "outcomeCriteria";
	// public String activityLevelEngagement = "activityLevelEngagement";
	// public String capacitySelfEstimate ="capacitySelfEstimate";
	// public String adlsLevelCare ="adlsLevelCare";
	// public String capacityIndependence ="adlsLevelCare";
	// public String selfCare = "selfCare";
	// public String signaturePath ="/doc/signature";
	String days = DayEnum.MONDAY.name() + "," + DayEnum.THURSDAY.name();
	public String docUrl = "/doc/a";

	public String gender = GenderEnum.MAN.name();
	public String physicalExam = "physicalExam";
	public String certificateName = "certificateName";
	public Calendar certificateStart = Calendar.getInstance();
	public Calendar certificateEnd = Calendar.getInstance();
	public Calendar workStart = Calendar.getInstance();
	public Calendar workEnd = Calendar.getInstance();
	public String position = "position";
	public String manager = "manager";
	public String type = "type";
	public String statusString = "status";
	public String backgroundCheck = "backgroundCheck";

	public String seat = "10A";

	public String itemName = "help on shopping";
	public String itemNote = "help on shopping note";

	public String insuranceBrokerCompanyType = "MTM";
	public String insuranceBrokerCompanyName = "QBE Insurance";
	public String insuranceBrokerCompanyCode = "QBE";
	public double actualMoney = 50.0;
	public double expectedMoney = 20.0;
	public String billingCode = "billing code";
	public String pin = "pin";

	protected String title = "title";
	protected Calendar startTime = Calendar.getInstance();
	protected Calendar endTime = Calendar.getInstance();
	protected String trainer = "trainer";
	protected String trainee = "1,23,45"; // #128 employee_id or user_id, and
											// those IDs should be divided by
											// comma
	protected String location = "location";
	protected String note = "note";

	protected void init() {
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

	protected Organization createNewOrganization() {
		Organization organization = new Organization();
		organization.setName("Org Name");
		organization.setAddressOne(addressOne);
		organization.setAddressTwo(addressTwo);
		organization.setCity(city);
		organization.setEmail(email);
		organization.setFax(fax);
		organization.setLicenseNo(licenseNo);
		organization.setPhone(phone);
		organization.setState(state);
		organization.setZipcode(zipcode);
		organization.setCode("1234");
		organization.setMainContact("5436346");
		organization.setStatus(1);
		organization.setType(type);
		organization.setZipcode(emergencyContactZipcode);
		organization.setWorktimeStart(new Time(System.currentTimeMillis()));
		organization.setWorktimeEnd(new Time(System.currentTimeMillis()));
		return organization;
	}

	protected Agency createNewAgency(AgencyType agencyType, Company company) {
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
		return agency;
	}

	protected Company createNewCompany() {
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
		company.setType(type);
		return company;
	}

	protected AgencyType createNewAgencyType() {
		AgencyType agencyType = new AgencyType();
		agencyType.setName("Agency Type Name");
		agencyType.setStatus(1);
		return agencyType;
	}

	protected User createNewUser(Agency agency) {
		User user = new User();
		user.setAgency(agency);
		return doCreateNewUser(user);
	}

	protected User createNewUser() {
		return doCreateNewUser(new User());
	}

	private User doCreateNewUser(User user) {
		user.setEligiableStart(new Timestamp(eligiableStart.getTimeInMillis()));
		user.setEligiableEnd(new Timestamp(eligiableEnd.getTimeInMillis()));
		user.setInsuranceEligiable(insuranceEligiable);
		user.setAuthorizationStart(new Timestamp(insuranceStart.getTimeInMillis()));
		user.setAuthorizationEnd(new Timestamp(insuranceEnd.getTimeInMillis()));
		user.setUsername(username);
		user.setPassword(password);
		user.setPin(PIN);
		//user.setUserType(1);

		user.setVerificationCode("1234");
		user.setWorkBackground("12345");
		user.setZipcode(emergencyContactZipcode);

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

	protected ServicePlan createNewServicePlan(User user) {
		ServicePlan servicePlan = new ServicePlan();
		servicePlan.setApprovedBy(approvedBy);
		servicePlan.setDays(days);
		// servicePlan.setDocUrl(docUrl);
		// servicePlan.setEmployee(null);
		servicePlan.setPlanEnd(planEnd);
		servicePlan.setPlanStart(planStart);
		servicePlan.setUser(user);
		// servicePlan.setOutcomeTargetDate(outcomeTargetDate);
		// servicePlan.setOutcomeDateAchieved(outcomeDateAchieved);
		// servicePlan.setMedicalStatus(medicalStatus);
		// servicePlan.setNutritionStatus(nutritionStatus);
		// servicePlan.setSensoryStatus(sensoryStatus);
		// servicePlan.setMedicationStatus(medicationStatus);
		// servicePlan.setPainStatus(painStatus);
		// servicePlan.setCongonitiveStatus(congonitiveStatus);
		// servicePlan.setPsychosocialStatus(psychosocialStatus);
		// servicePlan.setSpiritualStatus(spiritualStatus);
		// servicePlan.setCommunicationStatus(communicationStatus);
		// servicePlan.setExpectedOutcome(expectedOutcome);
		// servicePlan.setOutcomeCriteria(outcomeCriteria);
		// servicePlan.setActivityLevelEngagement(activityLevelEngagement);
		// servicePlan.setCapacitySelfEstimate(capacitySelfEstimate);
		// servicePlan.setAdlsLevelCare(adlsLevelCare);
		// servicePlan.setCapacityIndependence(capacityIndependence);
		// servicePlan.setSelfCare(selfCare);
		// servicePlan.setSignaturePath(signaturePath);
		return servicePlan;
	}

	protected Visit createNewVisit(User user, Agency agency) {
		Visit visit = new Visit();
		visit.setUser(user);
		visit.setAgency(agency);
		visit.setCheckInTime(checkInTime);
		visit.setCheckOutTime(checkOutTime);
		visit.setSelectedSeat(selectedTable);
		visit.setSelectedSeat(selectedSeat);
		// visit.setUserSignature(null);
		visit.setPin(PIN);
		// visit.setServicePlan(servicePlan);// TODO not yet finished
		// ServicePlan CRUD
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

		visit.setActive(1);
		return visit;
	}

	protected Employee createNewEmployee(Agency agency) {
		Employee employee = new Employee();
		employee.setAgency(agency);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setGender(gender);
		employee.setSocialSecurityNumber(socialSecurityNumber);
		employee.setDateOfBirth(new Timestamp(dob.getTimeInMillis()));
		employee.setPhysicalExam(physicalExam);
		employee.setWorkStart(new Timestamp(workStart.getTimeInMillis()));
		employee.setWorkEnd(new Timestamp(workEnd.getTimeInMillis()));
		employee.setCertificateName(certificateName);
		employee.setCertificateStart(new Timestamp(certificateStart.getTimeInMillis()));
		employee.setCertificateEnd(new Timestamp(certificateEnd.getTimeInMillis()));
		employee.setPosition(position);
		// employee.setManager(manager);
		employee.setType(type);
		employee.setStatus(1);
		employee.setBackgroundCheck(backgroundCheck);
		employee.setPin(pin);
		employee.setEmployeeType(EmployeeTypeEnum.SALARY);
		return employee;
	}

	protected Activity createNewActivity(Employee employee) {
		Activity activity = new Activity();
		activity.setCreatedAt(new Timestamp(workStart.getTimeInMillis()));
		activity.setInstructorEmployee(employee);
		activity.setName(firstName);
		activity.setStatus(Integer.getInteger("1"));
		return activity;
	}

	protected VisitActivity createNewVisitActivity(Visit visit, Activity activity) {
		VisitActivity visitActivity = new VisitActivity();
		// visitActivity.setId(new VisitActivityPK(visit.getId(),
		// activity.getId()));
		// visitActivity.setActivity(activity);
		visitActivity.setActivityId(activity.getId());
		visitActivity.setSeat(seat);
		// visitActivity.setVisit(visit);
		visitActivity.setVisitId(visit.getId());
		return visitActivity;
	}

	protected WorkItem createNewWorkItem() {
		WorkItem workItem = new WorkItem();
		workItem.setItemName(itemName);
		workItem.setItemNote(itemNote);
		return workItem;
	}

	protected InsuranceBrokerCompany createNewInsuranceBrokerCompany() {
		InsuranceBrokerCompany insuranceBrokerCompany = new InsuranceBrokerCompany();
		insuranceBrokerCompany.setType(insuranceBrokerCompanyType);
		insuranceBrokerCompany.setName(insuranceBrokerCompanyName);
		insuranceBrokerCompany.setCode(insuranceBrokerCompanyCode);
		return insuranceBrokerCompany;
	}

	protected Training createNewTraining(String trainees) {
		Training training = new Training();
		training.setTrainee(trainees);
		return doCreateTraning(training);
	}

	protected Training createNewTraining() {
		Training training = new Training();
		training.setTrainee(trainee);
		return doCreateTraning(training);
	}

	private Training doCreateTraning(Training training) {
		training.setTitle(title);
		training.setStartTime(new Timestamp(startTime.getTimeInMillis()));
		training.setEndTime(new Timestamp(endTime.getTimeInMillis()));
		training.setType(type);

		training.setTitle(title);
		training.setStartTime(new Timestamp(startTime.getTimeInMillis()));
		training.setEndTime(new Timestamp(endTime.getTimeInMillis()));
		training.setType(type);
		training.setTrainer(trainer);
		training.setLocation(location);
		training.setNote(note);
		training.setStatus(1);

		return training;
	}
}
