package com.healthcare.api.common;

public class HealthcareConstants {

	public static final String AUTHENTICATED_ADMIN = "AUTHENTICATED_ADMIN";
	
	public static final String NOT_AUTHORIZED = "You are not authorized to perform this operation!";
	public static final String NOT_ACCESS_AGENCY = "You are not authorized for this agency!";
	public static final String NOT_ACCESS_COMPANY = "You are not authorized for this company!";
	public static final String NOT_ACCESS_SUPER_ADMIN = "You are not authorized. Super admin can only access it!";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";


	/*NUTRITION ENUM*/

	public enum NUTRITION {
		CALORIE_CONTROLLED_DIET(1),
		SODIUM_RESTRICTED(2),
		FAT_RESTRICTED(3),
		RENAL(4),
		HIGH_CALORIE(5),
		VEGETARIAN(6),
		HIGH_FIBER(7),
		NUTRITIONAL_SUPPLEMENTS(8),
		SUGAR_RESTRICTED(9),
		OTHER(10);

		private long value;

		NUTRITION(long value) {
			this.value = value;
		}

		public static NUTRITION getNutrition(long value) {
			for (NUTRITION nutrition : NUTRITION.values()) {
				if (value == nutrition.getValue()) {
					return nutrition;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum SCREENING {
		HAVE_YOU_EVER_FELT_YOU_SHOULD_CUT_DOWN_ON_YOUR_DRINKING(1),
		HAVE_PEOPLE_ANNOYED_YOU_BY_CRITICIZING_YOUR_DRINKING(2),
		HAVE_YOU_EVER_FELT_BAD_OR_GUILTY_ABOUT_YOUR_DRINKING(3),
		HAVE_YOU_EVER_HAD_A_DRINK_FIRST_THING_IN_THE_MORNING_TO_STEADY_YOUR_NERVES_OR_GET_RID_OF_A_HANGOVER(4);

		private long value;

		SCREENING(long value) {
			this.value = value;
		}

		public static SCREENING getScreening(long value) {
			for (SCREENING screening : SCREENING.values()) {
				if (value == screening.getValue()) {
					return screening;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum NUTRITION_RISK_STATUS {
		HAS_AN_ILLNESS_OR_CONDITIONS_THAT_MADE_ME_CHANGE_THE_KIND_AMOUNT_OF_FOOD_EAT(1),
		EATS_FEWER_THAN_MEALS_PER_DAY(2),
		EATS_FEW_FRUITS_OR_VEGETABLES_OR_MILK_PRODUCTS(3),
		HAS_OR_MORE_DRINKS_OF_BEER_LIQUOR_OR_WINE_ALMOST_EVERY_DAY(4),
		HAS_TOOTH_OR_MOUTH_PROBLEMS_THAT_MAKE_IT_HARD_FOR_ME_TO_EAT(5),
		DOES_NOT_ALWAYS_HAVE_ENOUGH_MONEY_TO_BUY_THE_FOOD_I_NEED(6),
		EAT_ALONE_MOST_OF_THE_TIME(7),
		TAKE_OR_MORE_DIFFERENT_PRESCRIBED_OR_OVER_THE_COUNTER_DRUGS_A_DAY(8),
		WITHOUT_WANTING_TO_I_LOST_OR_GAINED_OR_MORE_POUNDS_ON_THE_LAST_MONTHS(9),
		NOT_ALWAYS_PHYSICALLY_ABLE_TO_SHOO_COOK_AND_FEED_MYSELF(10);

		private long value;

		NUTRITION_RISK_STATUS(long value) {
			this.value = value;
		}

		public static NUTRITION_RISK_STATUS getNutritionRiskStatus(long value) {
			for (NUTRITION_RISK_STATUS nutritionriskstatus : NUTRITION_RISK_STATUS.values()) {
				if (value == nutritionriskstatus.getValue()) {
					return nutritionriskstatus;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum CONCLUSIONS {
		High_Risk(1),
		Moderate_Risk(2),
		Low_Risk(3);

		private long value;

		CONCLUSIONS(long value) {
			this.value = value;
		}

		public static CONCLUSIONS getConclusions(long value) {
			for (CONCLUSIONS conclusions : CONCLUSIONS.values()) {
				if (value == conclusions.getValue()) {
					return conclusions;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	/*HEALTHSTATUSES*/

	public enum HEALTH_STATUSES {
		ALZHEIMERS(1),
		ARTHRITIS(2),
		CANCER(3),
		CELLULITIS(4),
		COLITIS(5),
		COLOSTOMY(6),
		CONSTIPATION(7),
		DIABETES(8),
		DECUBITUS_ULCERS(9),
		DEVELOPMENTAL_DISABILITIES(10),
		DIGESTIVE_PROBLEMS(11),
		DIVERTICULITIS(12),
		GALL_BLADDER_DISEASE(13),
		HEARING_IMPAIRMENT(14),
		HIATAL_HERNIA(15),
		HIGH_CHOLESTEROL(16),
		LEGALLY_BLIND(17),
		LIVER_DISEASE(18),
		LOW_BLOOD_PRESSURE(19),
		RENAL_DISEASE(20),
		DEPENDENT(21),
		STROKE(22),
		PARALYSIS(23),
		SPEECH_PROBLEMS(24),
		SWALLOWING_DIFFICULTIES(25),
		TASTE_IMPAIRMENT(26),
		ULCER(27),
		VISUAL_IMPAIRMENT(28),
		ALCOHOLISM(29),
		ASTHMA(30),
		CARDIOVASCULAR_DISORDER(31),
		CHRONIC_OBSTRUCTIVE_PULMONARY_DISEASE(32),
		CONGESTIVE_HEART_FAILURE(33),
		CHRONIC_PAIN(34),
		DIARRHEA(35),
		DIALYSIS(36),
		DEHYDRATION(37),
		DENTAL_PROBLEMS(38),
		DEMENTIA(39),
		FREQUENT_FALLS(40),
		FRACTURES(41),
		HEART_DISEASE(42),
		HIGH_BLOOD_PRESSURE(43),
		OXYGEN(44),
		HYPOGLYCEMIA(45),
		HIV(46),
		PERNICIOUS_ANEMIA(47),
		OSTEOPOROSIS(48),
		PARKINSON(49),
		RESPIRATORY_PROBLEMS(50),
		SHINGLES(51),
		URINARY_TRACT_INFECTION(52),
		TRAUMATICBRAIN_INJURY(53),
		TREMORS(54),
		OTHERS(55);

		private long value;

		HEALTH_STATUSES(long value) {
			this.value = value;
		}

		public static HEALTH_STATUSES getHealthStatuses(long value) {
			for (HEALTH_STATUSES healthstatuses : HEALTH_STATUSES.values()) {
				if (value == healthstatuses.getValue()) {
					return healthstatuses;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum ASSISTIVE_DEVICES {
		WHEELCHAIR(1),
		WALKER(2),
		SCOOTER(3),
		DENTURE(4),
		PARTIAL(5),
		HEARING_AID(6),
		CANE(7),
		BED_RAIL(8),
		ACCESSIBLE_VEHICLE(9),
		GLASSES(10),
		FULL(11);

		private long value;

		ASSISTIVE_DEVICES(long value) {
			this.value = value;
		}

		public static ASSISTIVE_DEVICES getAssistiveDevices(long value) {
			for (ASSISTIVE_DEVICES assistiveDevices : ASSISTIVE_DEVICES.values()) {
				if (value == assistiveDevices.getValue()) {
					return assistiveDevices;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	/*HOUSING STATUS*/

	public enum TYPE_OF_HOUSING {
		MULTI_UNIT_HOUSING(1),
		SINGLE_FAMILY_HOME(2),
		OWNS(3),
		RENTS(4),
		OTHER(5);

		private long value;

		TYPE_OF_HOUSING(long value) {
			this.value = value;
		}

		public static TYPE_OF_HOUSING getTypeOfHousing(long value) {
			for (TYPE_OF_HOUSING typeofhousing : TYPE_OF_HOUSING.values()) {
				if (value == typeofhousing.getValue()) {
					return typeofhousing;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum PSYCHO_SOCIAL_CONDITION {
		DEMENTIA(1),
		COOPERATIVE(2),
		ANGER(3),
		ALERT(4),
		DELIRIUM(5),
		DEPRESSED_SADNESS_RECURRENT_CRYING_TEARFULNESS(6),
		DIMINISHED_INTERPERSONAL_SKILLS(7),
		DISRUPTIVE_SOCIALLY(8),
		HALLUCINATIONS(9),
		HOARDING(10),
		IMPAIRED_DECISION_MAKING_ISOLATION(11),
		LONELY(12),
		PHYSICAL_AGGRESSIVE(13),
		MEMORY_DEFICIT(14),
		RESISTANCE_TO_CARE(15),
		SLEEPING_PROBLEMS(16),
		SUICIDAL_THOUGHTS(17),
		SELF_NEGLECT(18),
		SHORT_TERM_MEMORY_DEFICIT(19),
		SUICIDAL_BEHAVIOR(20),
		VERBAL_DISRUTIVE(21),
		WANDERING(22),
		WORRIED_OR_ANXIOUS(23),
		WITHDRAWL(24),
		OTHER(25);

		private long value;

		PSYCHO_SOCIAL_CONDITION(long value) {
			this.value = value;
		}

		public static PSYCHO_SOCIAL_CONDITION getPsychoSocialCondition(long value) {
			for (PSYCHO_SOCIAL_CONDITION psychoSocialCondition : PSYCHO_SOCIAL_CONDITION.values()) {
				if (value == psychoSocialCondition.getValue()) {
					return psychoSocialCondition;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER {
		ADULT_DAY_SERVICES(1),
		PERSONAL_CARE_LEVEL_1(2),
		PERSONAL_CARE_LEVEL_2(3),
		IN_HOME_CONTACT_SUPPORT(4);

		private long value;

		SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER(long value) {
			this.value = value;
		}

		public static SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER getServices(long value) {
			for (SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER service : SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.values()) {
				if (value == service.getValue()) {
					return service;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT {
		TRANSPORTATION(1),
		FINANCES(2),
		FAMILY(3),
		JOB(4),
		RESPONSIBILITIES(5),
		PHYSICAL_BURDEN(6),
		RELIABILITY(7),
		HEALTH_PROBLEMS(8),
		EMOTIONAL_BURDEN(9),
		LIVING_DISTANCE(10);

		private long value;

		FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT(long value) {
			this.value = value;
		}

		public static FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT getFactors(long value) {
			for (FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT factor : FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.values()) {
				if (value == factor.getValue()) {
					return factor;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum SERVICE_FOR_CLIENT {
		NONE_UTILIZED(1),
		ADULT_DAY_HEALTH_CARE(2),
		CAREGIVER_SUPPORT(3),
		ASSISTED_TRANSPORTATION(4),
		CASE_MANAGEMENT(5),
		COMMUNITY_BASED_FOOD_PROGRAM(6),
		CONSUMER_DIRECTED_IN_HOME_SERVICES(7),
		CONGREGATE_MEALS(8),
		EQUIPMENT_SUPPLIES(9),
		FRIENDLY_VISITOR_TELEPHONE_REASSURANCE(10),
		HEALTH_PROMOTION(11),
		HEALTH_INSURANCE_COUNSELING(12),
		HOME_HEALTH_AIDE(13),
		HOME_DELIVERED_MEALS(14),
		HOSPICE(15),
		LEGAL_SERVICES(16),
		HOUSING_ASSISTANCE(17),
		MENTAL_HEALTH_SERVICES(18),
		NUTRITION_COUNSELING(19),
		OUTREACH(20),
		SPEECH_THERAPY(20),
		OCCUPATIONAL_THERAPY(22),
		PERSONAL_CARE_LEVEL_1(23),
		PERSONAL_CARE_LEVEL_2(24),
		PERSONAL_EMERGENCY_RESPONSE_SYSTEM(25),
		PHYSICAL_THERAPY(26),
		PROTECTIVE_SERVICES(27),
		RESPITE(28),
		RESPIRATORY_THERAPY(29),
		SENIOR_CENTER(30),
		SENIOR_COMPANIONS(31),
		SERVICES_FOR_THE_BLIND(32),
		SHOPPING(33),
		SKILLED_NURSING(34),
		SOCIAL_ADULT_DAY_CARE(35),
		OTHER(36);

		private long value;

		SERVICE_FOR_CLIENT(long value) {
			this.value = value;
		}

		public static SERVICE_FOR_CLIENT getServiceForClient(long value) {
			for (SERVICE_FOR_CLIENT serviceForClient : SERVICE_FOR_CLIENT.values()) {
				if (value == serviceForClient.getValue()) {
					return serviceForClient;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}

	public enum HOUSING {
		ACCUMULATED_GARBADGE(1),
		CARBON_MONOXIDE_DETECTORS_NOT_PRESENT_WORKING(2),
		DOORWAY_WIDTHS_ARE_INADEQUATE(3),
		LOOSE_SCATTER_RUGS_PRESENT_IN_ONE_OR_MORE_ROOMS(4),
		LAMP_OR_LIGHT_SWITCH_WITHIN_EASY_REACH_OF_THE_BED(5),
		NO_RUBBER_MATS_OR_NON_SLIP_DECALS_IN_THE_TUB_OR_SHOWER(6),
		SMOKE_DETECTORS_NOT_PRESENT_WORKING(7),
		TELEPHONE_AND_APPLIANCE_CORDS_ARE_STRUNG_ACROSS_AREAS_WHERE_PEOPLE_WALK(8),
		TRAFFIC_LANE_FROM_THE_BEDROOM_TO_THE_BATHROOM_IS_NOT_CLEAR_OF_OBSTACLES(9),
		NO_HANDRAILS_ON_THE_STAIRWAYS(10),
		BAD_ODORS(11),
		FLOORS_AND_STAIRWAYS_DIRTY_AND_CLUTTEREDS(12),
		NO_LIGHTS_IN_THE_BATHROOM_OR_IN_THE_HALLWAY(13),
		NO_LOCKS_ON_DOORS_OR_NOT_WORKING(14),
		NO_GRAB_BAR_IN_TUB_AND_SHOWER(15),
		STAIRWAYS_ARE_NOT_IN_GOOD_CONDITION(16),
		OTHER(17);

		private long value;

		HOUSING(long value) {
			this.value = value;
		}

		public static HOUSING getHousing(long value) {
			for (HOUSING housing : HOUSING.values()) {
				if (value == housing.getValue()) {
					return housing;
				}
			}
			return null;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
	}
}
