package com.healthcare.api;

import com.healthcare.dto.NutritionDTO;
import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.Nutrition;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.NutritionRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.healthcare.api.common.HealthcareConstants.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/nutrition")
public class NutritionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    @ApiOperation(value = "save healthStatus", notes = "save healthStatus")
    @ApiParam(name = "healthStatus", value = "healthStatus to save", required = true)
    @PostMapping()
    public ResponseEntity<NutritionDTO> create(@RequestBody NutritionDTO nutritionDTO) {
        List<String> list = new ArrayList<>();

        for(NUTRITION nutrition : NUTRITION.values()){
            switch (nutrition){
                case CALORIE_CONTROLLED_DIET:
                    updateList(NUTRITION.CALORIE_CONTROLLED_DIET.getValue(), nutritionDTO.isCalorieControlledDiet(), list);
                    break;
                case SODIUM_RESTRICTED :
                    updateList(NUTRITION.SODIUM_RESTRICTED.getValue(), nutritionDTO.isSodiumRestricted(), list);
                    break;
                case FAT_RESTRICTED :
                    updateList(NUTRITION.FAT_RESTRICTED.getValue(), nutritionDTO.isFatRestricted(), list);
                    break;
                case RENAL :
                    updateList(NUTRITION.RENAL.getValue(), nutritionDTO.isRenal(), list);
                    break;
                case HIGH_CALORIE :
                    updateList(NUTRITION.HIGH_CALORIE.getValue(), nutritionDTO.isHighCalorie(), list);
                    break;
                case VEGETARIAN:
                    updateList(NUTRITION.VEGETARIAN.getValue(), nutritionDTO.isVegetarian(), list);
                    break;
                case HIGH_FIBER :
                    updateList(NUTRITION.HIGH_FIBER.getValue(), nutritionDTO.isHighFiber(), list);
                    break;
                case NUTRITIONAL_SUPPLEMENTS :
                    updateList(NUTRITION.NUTRITIONAL_SUPPLEMENTS.getValue(), nutritionDTO.isNutritionalSupplements(), list);
                    break;
                case SUGAR_RESTRICTED :
                    updateList(NUTRITION.SUGAR_RESTRICTED.getValue(), nutritionDTO.isSugarRestricted(), list);
                    break;
                case OTHER :
                    updateList(NUTRITION.OTHER.getValue(), nutritionDTO.isOther(), list);
                    break;
            }
        }

        List<String> screeningList = new ArrayList<>();
        for(SCREENING screening : SCREENING.values()){
            switch (screening){
                case HAVE_YOU_EVER_FELT_YOU_SHOULD_CUT_DOWN_ON_YOUR_DRINKING:
                    updateList(SCREENING.HAVE_YOU_EVER_FELT_YOU_SHOULD_CUT_DOWN_ON_YOUR_DRINKING.getValue(), nutritionDTO.isFeltYouCutDownYourDrinking(), screeningList);
                    break;
                case HAVE_PEOPLE_ANNOYED_YOU_BY_CRITICIZING_YOUR_DRINKING :
                    updateList(SCREENING.HAVE_PEOPLE_ANNOYED_YOU_BY_CRITICIZING_YOUR_DRINKING.getValue(), nutritionDTO.isCriticizingYourDrinking(), screeningList);
                    break;
                case HAVE_YOU_EVER_FELT_BAD_OR_GUILTY_ABOUT_YOUR_DRINKING :
                    updateList(SCREENING.HAVE_YOU_EVER_FELT_BAD_OR_GUILTY_ABOUT_YOUR_DRINKING.getValue(), nutritionDTO.isGuiltyAboutYourDrinking(), screeningList);
                    break;
                case HAVE_YOU_EVER_HAD_A_DRINK_FIRST_THING_IN_THE_MORNING_TO_STEADY_YOUR_NERVES_OR_GET_RID_OF_A_HANGOVER :
                    updateList(SCREENING.HAVE_YOU_EVER_HAD_A_DRINK_FIRST_THING_IN_THE_MORNING_TO_STEADY_YOUR_NERVES_OR_GET_RID_OF_A_HANGOVER.getValue(), nutritionDTO.isGetRidOfHangover(), screeningList);
                    break;
            }
        }

        List<String> nutritionRiskList = new ArrayList<>();
        for(NUTRITION_RISK_STATUS nutritionRiskStatus : NUTRITION_RISK_STATUS.values()){
            switch (nutritionRiskStatus){
                case HAS_AN_ILLNESS_OR_CONDITIONS_THAT_MADE_ME_CHANGE_THE_KIND_AMOUNT_OF_FOOD_EAT:
                    updateList(NUTRITION_RISK_STATUS.HAS_AN_ILLNESS_OR_CONDITIONS_THAT_MADE_ME_CHANGE_THE_KIND_AMOUNT_OF_FOOD_EAT.getValue(), nutritionDTO.isNutritionalRiskOne(), nutritionRiskList);
                    break;
                case EATS_FEWER_THAN_MEALS_PER_DAY :
                    updateList(NUTRITION_RISK_STATUS.EATS_FEWER_THAN_MEALS_PER_DAY.getValue(), nutritionDTO.isNutritionalRiskTwo(), nutritionRiskList);
                    break;
                case EATS_FEW_FRUITS_OR_VEGETABLES_OR_MILK_PRODUCTS :
                    updateList(NUTRITION_RISK_STATUS.EATS_FEW_FRUITS_OR_VEGETABLES_OR_MILK_PRODUCTS.getValue(), nutritionDTO.isNutritionalRiskThree(), nutritionRiskList);
                    break;
                case HAS_OR_MORE_DRINKS_OF_BEER_LIQUOR_OR_WINE_ALMOST_EVERY_DAY :
                    updateList(NUTRITION_RISK_STATUS.HAS_OR_MORE_DRINKS_OF_BEER_LIQUOR_OR_WINE_ALMOST_EVERY_DAY.getValue(), nutritionDTO.isNutritionalRiskFour(), nutritionRiskList);
                    break;
                case HAS_TOOTH_OR_MOUTH_PROBLEMS_THAT_MAKE_IT_HARD_FOR_ME_TO_EAT :
                    updateList(NUTRITION_RISK_STATUS.HAS_TOOTH_OR_MOUTH_PROBLEMS_THAT_MAKE_IT_HARD_FOR_ME_TO_EAT.getValue(), nutritionDTO.isNutritionalRiskFive(), nutritionRiskList);
                    break;
                case DOES_NOT_ALWAYS_HAVE_ENOUGH_MONEY_TO_BUY_THE_FOOD_I_NEED:
                    updateList(NUTRITION_RISK_STATUS.DOES_NOT_ALWAYS_HAVE_ENOUGH_MONEY_TO_BUY_THE_FOOD_I_NEED.getValue(), nutritionDTO.isNutritionalRiskSix(), nutritionRiskList);
                    break;
                case EAT_ALONE_MOST_OF_THE_TIME :
                    updateList(NUTRITION_RISK_STATUS.EAT_ALONE_MOST_OF_THE_TIME.getValue(), nutritionDTO.isNutritionalRiskSeven(), nutritionRiskList);
                    break;
                case TAKE_OR_MORE_DIFFERENT_PRESCRIBED_OR_OVER_THE_COUNTER_DRUGS_A_DAY :
                    updateList(NUTRITION_RISK_STATUS.TAKE_OR_MORE_DIFFERENT_PRESCRIBED_OR_OVER_THE_COUNTER_DRUGS_A_DAY.getValue(), nutritionDTO.isNutritionalRiskEight(), nutritionRiskList);
                    break;
                case WITHOUT_WANTING_TO_I_LOST_OR_GAINED_OR_MORE_POUNDS_ON_THE_LAST_MONTHS :
                    updateList(NUTRITION_RISK_STATUS.WITHOUT_WANTING_TO_I_LOST_OR_GAINED_OR_MORE_POUNDS_ON_THE_LAST_MONTHS.getValue(), nutritionDTO.isNutritionalRiskNine(), nutritionRiskList);
                    break;
                case NOT_ALWAYS_PHYSICALLY_ABLE_TO_SHOO_COOK_AND_FEED_MYSELF :
                    updateList(NUTRITION_RISK_STATUS.NOT_ALWAYS_PHYSICALLY_ABLE_TO_SHOO_COOK_AND_FEED_MYSELF.getValue(), nutritionDTO.isNutritionalRiskTen(), nutritionRiskList);
                    break;
            }
        }

        List<String> conclusionsList = new ArrayList<>();
        for(CONCLUSIONS conclusions : CONCLUSIONS.values()){
            switch (conclusions){
                case High_Risk:
                    updateList(CONCLUSIONS.High_Risk.getValue(), nutritionDTO.isHighRisk(), conclusionsList);
                    break;
                case Moderate_Risk :
                    updateList(CONCLUSIONS.Moderate_Risk.getValue(), nutritionDTO.isModerateRisk(), conclusionsList);
                    break;
                case Low_Risk :
                    updateList(CONCLUSIONS.Low_Risk.getValue(), nutritionDTO.isLowRisk(), conclusionsList);
                    break;
            }
        }

        Nutrition nutrition = new Nutrition();
        try {
            propertyUtilsBean.copyProperties(nutrition, nutritionDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        nutrition.setPhysicianPrescribedTherapeuticDiet(String.join(",", list));
        nutrition.setAlcoholScreeningTest(String.join(",", screeningList));
        nutrition.setNutritionalRiskStatus(String.join(",", nutritionRiskList));
        nutrition.setConclusion(String.join(",", conclusionsList));
        nutrition = nutritionRepository.save(nutrition);
        saveAssessmentUser(nutrition);
        return new ResponseEntity<NutritionDTO>(nutritionDTO, HttpStatus.OK);
    }

    private void saveAssessmentUser(Nutrition nutrition) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(nutrition.getUserId());
        if(assessmentUser != null){
            assessmentUser.setNutrition(nutrition);
            assessmentUserRepository.save(assessmentUser);
        }
    }


    private void updateList(long value, boolean isSelected, List<String> list) {
        if(isSelected == true) {
            list.add(Long.toString(value));
        }
    }
//    private void saveAssessmentUser(@RequestBody Nutrition nutrition) {
//        AssessmentUser assessmentUser = assessmentUserRepository.findOne(nutrition.getUserId());
//        if(assessmentUser != null){
//            assessmentUser.setHealthStatus(nutrition);
//            assessmentUserRepository.save(assessmentUser);
//        }
//    }

    @ApiOperation(value = "get healthStatus by id", notes = "get healthStatus by id")
    @ApiImplicitParam(name = "id", value = "housing id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public NutritionDTO read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<Nutrition> nutritionList = nutritionRepository.findByUserId(id);
        if(nutritionList.size() > 0) {

            Nutrition nutrition = nutritionList.get(0);
            String services = nutrition.getPhysicianPrescribedTherapeuticDiet();
            String nutritionService = nutrition.getAlcoholScreeningTest();
            String nutritionRiskList = nutrition.getNutritionalRiskStatus();
            String conclusion = nutrition.getConclusion();


            NutritionDTO nutritionDTO = new NutritionDTO();
            try {
                propertyUtilsBean.copyProperties(nutritionDTO, nutrition);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }


            if(StringUtils.isNotBlank(services)) {
                List<Integer> intIds = Stream.of(services.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (NUTRITION.getNutrition(intId)) {
                        case CALORIE_CONTROLLED_DIET :
                            nutritionDTO.setCalorieControlledDiet(true);
                            break;
                        case SODIUM_RESTRICTED :
                            nutritionDTO.setSodiumRestricted(true);
                            break;
                        case FAT_RESTRICTED :
                            nutritionDTO.setFatRestricted(true);
                            break;
                        case RENAL :
                            nutritionDTO.setRenal(true);
                            break;
                        case HIGH_CALORIE :
                            nutritionDTO.setHighCalorie(true);
                            break;
                        case VEGETARIAN :
                            nutritionDTO.setVegetarian(true);
                            break;
                        case HIGH_FIBER :
                            nutritionDTO.setHighFiber(true);
                            break;
                        case NUTRITIONAL_SUPPLEMENTS :
                            nutritionDTO.setNutritionalSupplements(true);
                            break;
                        case SUGAR_RESTRICTED :
                            nutritionDTO.setSugarRestricted(true);
                            break;
                        case OTHER :
                            nutritionDTO.setOther(true);
                            break;
                    }
                }
            }

            if(StringUtils.isNotBlank(nutritionService)) {
                List<Integer> intIds = Stream.of(nutritionService.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (SCREENING.getScreening(intId)) {
                        case HAVE_YOU_EVER_FELT_YOU_SHOULD_CUT_DOWN_ON_YOUR_DRINKING :
                            nutritionDTO.setFeltYouCutDownYourDrinking(true);
                            break;
                        case HAVE_PEOPLE_ANNOYED_YOU_BY_CRITICIZING_YOUR_DRINKING :
                            nutritionDTO.setCriticizingYourDrinking(true);
                            break;
                        case HAVE_YOU_EVER_FELT_BAD_OR_GUILTY_ABOUT_YOUR_DRINKING :
                            nutritionDTO.setGuiltyAboutYourDrinking(true);
                            break;
                        case HAVE_YOU_EVER_HAD_A_DRINK_FIRST_THING_IN_THE_MORNING_TO_STEADY_YOUR_NERVES_OR_GET_RID_OF_A_HANGOVER :
                            nutritionDTO.setGetRidOfHangover(true);
                            break;
                    }
                }
            }

            if(StringUtils.isNotBlank(nutritionRiskList)) {
                List<Integer> intIds = Stream.of(nutritionRiskList.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (NUTRITION_RISK_STATUS.getNutritionRiskStatus(intId)) {
                        case HAS_AN_ILLNESS_OR_CONDITIONS_THAT_MADE_ME_CHANGE_THE_KIND_AMOUNT_OF_FOOD_EAT :
                            nutritionDTO.setNutritionalRiskOne(true);
                            break;
                        case EATS_FEWER_THAN_MEALS_PER_DAY :
                            nutritionDTO.setNutritionalRiskTwo(true);
                            break;
                        case EATS_FEW_FRUITS_OR_VEGETABLES_OR_MILK_PRODUCTS :
                            nutritionDTO.setNutritionalRiskThree(true);
                            break;
                        case HAS_OR_MORE_DRINKS_OF_BEER_LIQUOR_OR_WINE_ALMOST_EVERY_DAY :
                            nutritionDTO.setNutritionalRiskFour(true);
                            break;
                        case HAS_TOOTH_OR_MOUTH_PROBLEMS_THAT_MAKE_IT_HARD_FOR_ME_TO_EAT :
                            nutritionDTO.setNutritionalRiskFive(true);
                            break;
                        case DOES_NOT_ALWAYS_HAVE_ENOUGH_MONEY_TO_BUY_THE_FOOD_I_NEED :
                            nutritionDTO.setNutritionalRiskSix(true);
                            break;
                        case EAT_ALONE_MOST_OF_THE_TIME :
                            nutritionDTO.setNutritionalRiskSeven(true);
                            break;
                        case TAKE_OR_MORE_DIFFERENT_PRESCRIBED_OR_OVER_THE_COUNTER_DRUGS_A_DAY :
                            nutritionDTO.setNutritionalRiskEight(true);
                            break;
                        case WITHOUT_WANTING_TO_I_LOST_OR_GAINED_OR_MORE_POUNDS_ON_THE_LAST_MONTHS :
                            nutritionDTO.setNutritionalRiskNine(true);
                            break;
                        case NOT_ALWAYS_PHYSICALLY_ABLE_TO_SHOO_COOK_AND_FEED_MYSELF :
                            nutritionDTO.setNutritionalRiskTen(true);
                            break;
                    }
                }
            }

            if(StringUtils.isNotBlank(conclusion)) {
                List<Integer> intIds = Stream.of(conclusion.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (CONCLUSIONS.getConclusions(intId)) {
                        case High_Risk :
                            nutritionDTO.setHighRisk(true);
                            break;
                        case Moderate_Risk :
                            nutritionDTO.setModerateRisk(true);
                            break;
                        case Low_Risk :
                            nutritionDTO.setLowRisk(true);
                            break;
                    }
                }
            }

            return nutritionDTO;
        } else {
            return new NutritionDTO();
        }
    }
}
