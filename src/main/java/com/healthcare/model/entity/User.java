package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonValue;
//import com.healthcare.model.enums.FixedStatusEnum;
import com.healthcare.conf.ObjectListConverter;
import com.healthcare.conf.StringListConverter;
import com.healthcare.dto.BlackListDTO;
import com.healthcare.model.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.action.internal.OrphanRemovalAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
public @Data class User extends Audit implements Serializable {

	private static final long serialVersionUID = 8716797253090002699L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	//@Column(name = "user_type")
	//private Integer userType;
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	private String lastName;
	private String gender;
	private String language;
	@Column(name = "social_security_number")
	private String socialSecurityNumber;
	@Column(name = "dob")
	private Date dateOfBirth;
	private String email;
	private String phone;
	@Column(name = "secondary_phone")
	private String secondaryPhone;
	@Column(name = "verification_code")
	private String verificationCode;
	@Column(name = "address_type")
	private String addressType;
	@Column(name = "address_one")
	private String addressOne;
	@Column(name = "address_two")
	private String addressTwo;
	private String city;
	private String state;
	private String zipcode;
	@Column(name = "emergency_contact_first_name")
	private String emergencyContactFirstName;
	@Column(name = "emergency_contact_middle_name")
	private String emergencyContactMiddleName;
	@Column(name = "emergency_contact_last_name")
	private String emergencyContactLastName;
	@Column(name = "relationship_to_paticipant")
	private String relationshipToParticipant;
	@Column(name = "emergency_contact_phone")
	private String emergencyContactPhone;
	@Column(name = "emergency_contact_address_one")
	private String emergencyContactAddressOne;
	@Column(name = "emergency_contact_address_two")
	private String emergencyContactAddressTwo;
	@Column(name = "emergency_contact_city")
	private String emergencyContactCity;
	@Column(name = "emergency_contact_state")
	private String emergencyContactState;
	@Column(name = "emergency_contact_zipcode")
	private String emergencyContactZipcode;
	@Column(name = "comment")
	private String comment;
	@ManyToOne
	@JoinColumn(name = "preferred_meal_id")
	private Meal preferredMeal;
	@ManyToOne
	@JoinColumn(name = "preferred_activity_id")
	private Activity preferredActivity;
	@Column(name = "preferred_seat_id")
	private String preferredSeat;

	@Column(name = "preferred_table_id")
	private String preferredTable;

	@Column(name = "preferred_training_id")
	private String preferredTraining;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "profile_photo_id")
	private Document profilePhoto;

	@Column(name = "approvable_mail")
	private Integer approvableMail;
	@Column(name = "medicaid_no")
	private String medicaIdNumber;
	@Column(name = "medicare_no")
	private String medicareNumber;
	@ManyToOne
	@JoinColumn(name = "insurance_id")
	private Organization insurance;

	@Column(name = "authorization_start")
	private Timestamp authorizationStart;

	@Column(name = "authorization_end")
	private Timestamp authorizationEnd;

	@Column(name = "assessment_duration")
	private Integer assessmentDuration;

	@Column(name = "assessment_start_date")
	private Timestamp assessmentStartDate;

	@Column(name = "need_trip")
	private Integer needTrip;

	@ManyToOne
	@JoinColumn(name = "ride_line_id")
	private RideLine rideLine;

	@Column(name = "insurance_eligiable")
	private String insuranceEligiable;
	@Column(name = "eligiable_start")
	private Timestamp eligiableStart;
	@Column(name = "eligiable_end")
	private Timestamp eligiableEnd;

	@OneToOne
	@JoinColumn(name = "family_doctor_id")
	private Organization familyDoctor;

	@OneToOne
	@JoinColumn(name = "expert_doctor_id")
	private Organization expertDoctor;

	@Column(name = "medical_condition")
	private String medicalCondition;

	@Enumerated(EnumType.STRING)
	private StatusEnum status = StatusEnum.REGISTERED;
//
//	 @Column(name = "vacation_note")
//	 private String vacationNote;
	 @Column(name = "vacation_start")
	 private Timestamp vacationStart;
	 @Column(name = "vacation_end")
	 private Timestamp vacationEnd;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;


	@Column(name = "status_second")
	@Enumerated(EnumType.STRING)
	private StatusEnum statusSecond = StatusEnum.REGISTERED;
	@Column(name = "remember_token")
	private String rememberToken;
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean generatePin = false;

	@Transient
	private Date nextAssessmentDate;

	private String pin;

	@Column(name = "barcode")
	private String qrCode;

	@Column(name = "box_number")
	private String boxNumber;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "add_by_admin_id")
	private Admin addByAdmin;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "recommended_by_employee_id")
	private Admin recommendedByEmployee;

	@Column(name = "authorization_code")
	private String authorizationCode;
	@Column(name = "days_in_week")
	private String daysInWeek;

	@Enumerated(value = EnumType.STRING)
	private Citizenship citizenship = Citizenship.US_CITIZEN;

	@Enumerated(value = EnumType.STRING)
	private Education education = Education.COLLEAGUE;

	@Column(name = "work_background")
	private String workBackground;
	@Column(name = "recreation_hobbies_skills")
	private String recreationHobbiesSkills;
	@Column(name = "religious_preference")
	private String religiousPreference;

	private Integer children;
	@Column(name = "authorization_file_id")
	private String authorizationPath;

	@OneToOne(cascade= CascadeType.ALL, orphanRemoval = true)
	private ServicePlan servicePlan;

	@OneToOne(cascade= CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "medication_id")
	private Medication medication;

	@OneToOne(cascade = CascadeType.MERGE )
	@JoinColumn(name = "case_manager_id")
	private Employee caseManager;

	@OneToOne(cascade = CascadeType.MERGE )
	@JoinColumn(name = "rules_and_regus_doc_id")
	private Document rulesAndRegusDocId;

	@Column(name = "period_type")
	private PeriodTypeEnum periodType = PeriodTypeEnum.WEEKLY;

	@Column(name = "unit_type")
	private UnitTypeEnum unitType = UnitTypeEnum.HOUR;

	@Column(name = "maximum_units")
	private Integer maximumUnits;

	@Column(name = "auth_week_start")
	private AuthWeekStartEnum authWeekStart = AuthWeekStartEnum.MONDAY_WEEK;

	@Column(name = "fixed_status")
	@Enumerated(EnumType.STRING)
	private FixedStatusEnum fixedStatus;

	@Column(name = "addr_lat")
	private String addrLat;
	@Column(name = "addr_lng")
	private String addLng;
	@Column(name = "note")
	private String note;
	@Column(name = "visit_price")
	private Double visit_price;
	@Column(name = "trip_price")
	private Double trip_price;

	@Column(name = "blacklist_user")
	@Convert(converter = ObjectListConverter.class)
	private List<BlackListDTO> blacklistUser;
	@Column(name = "blacklist_driver")
	@Convert(converter = ObjectListConverter.class)
	private List<BlackListDTO> blacklistDriver;


	public enum Citizenship {
		US_CITIZEN("US Citizen"), GREEN_CARD("Green card"), NA("NA");
		private String value;

		Citizenship(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

	}

	public enum Education {
		PRIMARY_SCHOOL("primary school"), MIDDLE_SCHOOL("middle school"), HIGH_SCHOOL("high school"), COLLEAGUE(
				"colleague"), MASTER("master"), PHD("phd");
		private String value;

		Education(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}
	}

  @JsonIgnore
  @ManyToMany()
  @JoinTable(
    name = "User_Has_FoodAllergies",
    joinColumns = { @JoinColumn(name = "user_id") },
    inverseJoinColumns = { @JoinColumn(name = "food_allergies_id") }
  )
  private List<FoodAllergy> foodAllergies;
}
