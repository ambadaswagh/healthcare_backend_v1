package com.healthcare.api;

import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.dto.ServiceForClientDTO;
import com.healthcare.model.entity.assessment.ServiceForClient;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.ServiceForClientRepository;
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
@RequestMapping(value = "/api/serviceForClient")
public class ServiceForClientController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceForClientRepository serviceForClientRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    PropertyUtilsBean  propertyUtilsBean = new PropertyUtilsBean();

    @ApiOperation(value = "save serviceForClient", notes = "save serviceForClient")
    @ApiParam(name = "serviceForClient", value = "serviceForClient to save", required = true)
    @PostMapping()
    public ResponseEntity<ServiceForClientDTO> create(@RequestBody ServiceForClientDTO serviceForClientDTO) {
        List<String> list = new ArrayList<>();
        for(SERVICE_FOR_CLIENT serviceForClient : SERVICE_FOR_CLIENT.values()) {
            switch (serviceForClient) {
                case NONE_UTILIZED :
                    updateList(SERVICE_FOR_CLIENT.NONE_UTILIZED.getValue(), serviceForClientDTO.isNoneUtilized(), list);
                    break;
                case ADULT_DAY_HEALTH_CARE :
                    updateList(SERVICE_FOR_CLIENT.ADULT_DAY_HEALTH_CARE.getValue(), serviceForClientDTO.isAdultDayHealthCare(), list);
                    break;
                case CAREGIVER_SUPPORT :
                    updateList(SERVICE_FOR_CLIENT.CAREGIVER_SUPPORT.getValue(), serviceForClientDTO.isCaregiverSupport(), list);
                    break;
                case ASSISTED_TRANSPORTATION :
                    updateList(SERVICE_FOR_CLIENT.ASSISTED_TRANSPORTATION.getValue(), serviceForClientDTO.isAssistedTransportation(), list);
                    break;
                case CASE_MANAGEMENT :
                    updateList(SERVICE_FOR_CLIENT.CASE_MANAGEMENT.getValue(), serviceForClientDTO.isCaseManagement(), list);
                    break;
                case COMMUNITY_BASED_FOOD_PROGRAM :
                    updateList(SERVICE_FOR_CLIENT.COMMUNITY_BASED_FOOD_PROGRAM.getValue(), serviceForClientDTO.isCommunityBasedFoodProgram(), list);
                    break;
                case CONSUMER_DIRECTED_IN_HOME_SERVICES :
                    updateList(SERVICE_FOR_CLIENT.CONSUMER_DIRECTED_IN_HOME_SERVICES.getValue(), serviceForClientDTO.isConsumerDirectedInHomeServices(), list);
                    break;
                case CONGREGATE_MEALS :
                    updateList(SERVICE_FOR_CLIENT.CONGREGATE_MEALS.getValue(), serviceForClientDTO.isCongregateMeals(), list);
                    break;
                case EQUIPMENT_SUPPLIES :
                    updateList(SERVICE_FOR_CLIENT.EQUIPMENT_SUPPLIES.getValue(), serviceForClientDTO.isEquipment(), list);
                    break;
                case FRIENDLY_VISITOR_TELEPHONE_REASSURANCE :
                    updateList(SERVICE_FOR_CLIENT.FRIENDLY_VISITOR_TELEPHONE_REASSURANCE.getValue(), serviceForClientDTO.isFriendlyVisitor(), list);
                    break;
                case HEALTH_PROMOTION :
                    updateList(SERVICE_FOR_CLIENT.HEALTH_PROMOTION.getValue(), serviceForClientDTO.isHealthPromotion(), list);
                    break;
                case HEALTH_INSURANCE_COUNSELING :
                    updateList(SERVICE_FOR_CLIENT.HEALTH_INSURANCE_COUNSELING.getValue(), serviceForClientDTO.isHealthInsuranceCounseling(), list);
                    break;
                case HOME_HEALTH_AIDE :
                    updateList(SERVICE_FOR_CLIENT.HOME_HEALTH_AIDE.getValue(), serviceForClientDTO.isHomeHealthAide(), list);
                    break;
                case HOME_DELIVERED_MEALS :
                    updateList(SERVICE_FOR_CLIENT.HOME_DELIVERED_MEALS.getValue(), serviceForClientDTO.isHomeDeliveredMeals(), list);
                    break;
                case HOSPICE :
                    updateList(SERVICE_FOR_CLIENT.HOSPICE.getValue(), serviceForClientDTO.isHospice(), list);
                    break;
                case LEGAL_SERVICES :
                    updateList(SERVICE_FOR_CLIENT.LEGAL_SERVICES.getValue(), serviceForClientDTO.isLegalServices(), list);
                    break;
                case HOUSING_ASSISTANCE :
                    updateList(SERVICE_FOR_CLIENT.HOUSING_ASSISTANCE.getValue(), serviceForClientDTO.isHousingAssistance(), list);
                    break;
                case MENTAL_HEALTH_SERVICES :
                    updateList(SERVICE_FOR_CLIENT.MENTAL_HEALTH_SERVICES.getValue(), serviceForClientDTO.isMentalHealthServices(), list);
                    break;
                case NUTRITION_COUNSELING :
                    updateList(SERVICE_FOR_CLIENT.NUTRITION_COUNSELING.getValue(), serviceForClientDTO.isNutritionCounseling(), list);
                    break;
                case OUTREACH :
                    updateList(SERVICE_FOR_CLIENT.OUTREACH.getValue(), serviceForClientDTO.isOutreach(), list);
                    break;
                case SPEECH_THERAPY :
                    updateList(SERVICE_FOR_CLIENT.SPEECH_THERAPY.getValue(), serviceForClientDTO.isSpeechTherapy(), list);
                    break;
                case OCCUPATIONAL_THERAPY :
                    updateList(SERVICE_FOR_CLIENT.OCCUPATIONAL_THERAPY.getValue(), serviceForClientDTO.isOccupationalTherapy(), list);
                    break;
                case PERSONAL_CARE_LEVEL_1 :
                    updateList(SERVICE_FOR_CLIENT.PERSONAL_CARE_LEVEL_1.getValue(), serviceForClientDTO.isPersonalCareLevelOne(), list);
                    break;
                case PERSONAL_CARE_LEVEL_2 :
                    updateList(SERVICE_FOR_CLIENT.PERSONAL_CARE_LEVEL_2.getValue(), serviceForClientDTO.isPersonalCareLevelTwo(), list);
                    break;
                case PERSONAL_EMERGENCY_RESPONSE_SYSTEM :
                    updateList(SERVICE_FOR_CLIENT.PERSONAL_EMERGENCY_RESPONSE_SYSTEM.getValue(), serviceForClientDTO.isPersonalEmergencyResponseSystem(), list);
                    break;
                case PHYSICAL_THERAPY :
                    updateList(SERVICE_FOR_CLIENT.PHYSICAL_THERAPY.getValue(), serviceForClientDTO.isPhysicalTherapy(), list);
                    break;
                case PROTECTIVE_SERVICES :
                    updateList(SERVICE_FOR_CLIENT.PROTECTIVE_SERVICES.getValue(), serviceForClientDTO.isProtectiveServices(), list);
                    break;
                case RESPITE :
                    updateList(SERVICE_FOR_CLIENT.RESPITE.getValue(), serviceForClientDTO.isRespite(), list);
                    break;
                case RESPIRATORY_THERAPY :
                    updateList(SERVICE_FOR_CLIENT.RESPIRATORY_THERAPY.getValue(), serviceForClientDTO.isRespiratoryTherapy(), list);
                    break;
                case SENIOR_CENTER :
                    updateList(SERVICE_FOR_CLIENT.SENIOR_CENTER.getValue(), serviceForClientDTO.isSeniorCenter(), list);
                    break;
                case SENIOR_COMPANIONS :
                    updateList(SERVICE_FOR_CLIENT.SENIOR_COMPANIONS.getValue(), serviceForClientDTO.isSeniorCompanions(), list);
                    break;
                case SERVICES_FOR_THE_BLIND :
                    updateList(SERVICE_FOR_CLIENT.SERVICES_FOR_THE_BLIND.getValue(), serviceForClientDTO.isServicesForTheBlind(), list);
                    break;
                case SHOPPING :
                    updateList(SERVICE_FOR_CLIENT.SHOPPING.getValue(), serviceForClientDTO.isShopping(), list);
                    break;
                case SKILLED_NURSING :
                    updateList(SERVICE_FOR_CLIENT.SKILLED_NURSING.getValue(), serviceForClientDTO.isSkilledNursing(), list);
                    break;
                case SOCIAL_ADULT_DAY_CARE :
                    updateList(SERVICE_FOR_CLIENT.SOCIAL_ADULT_DAY_CARE.getValue(), serviceForClientDTO.isSocialAdultDayCare(), list);
                    break;
                case OTHER:
                    updateList(SERVICE_FOR_CLIENT.OTHER.getValue(), serviceForClientDTO.isOther(), list);
                    break;
            }
        }
        ServiceForClient serviceForClient = new ServiceForClient();
        try {
            propertyUtilsBean.copyProperties(serviceForClient, serviceForClientDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        serviceForClient.setCurrentServiceReceiving(String.join(",", list));
        serviceForClient = serviceForClientRepository.save(serviceForClient);
        saveAssessmentUser(serviceForClient);
        return new ResponseEntity<ServiceForClientDTO>(serviceForClientDTO, HttpStatus.OK);
    }

    private void updateList(long value, boolean isSelected, List<String> list) {
        if(isSelected == true) {
            list.add(Long.toString(value));
        }
    }

    private void saveAssessmentUser(@RequestBody ServiceForClient serviceForClient) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(serviceForClient.getUserId());
        if(assessmentUser != null){
            assessmentUser.setServiceForClient(serviceForClient);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get serviceForClient by id", notes = "get serviceForClient by id")
    @ApiImplicitParam(name = "id", value = "serviceForClient id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public ServiceForClientDTO read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<ServiceForClient> serviceForClientList = serviceForClientRepository.findByUserId(id);
        if(serviceForClientList.size() > 0) {
            ServiceForClient serviceForClient = serviceForClientList.get(0);
            String services = serviceForClient.getCurrentServiceReceiving();


            ServiceForClientDTO serviceForClientDTO = new ServiceForClientDTO();
            try {
                propertyUtilsBean.copyProperties(serviceForClientDTO, serviceForClient);
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
                    switch (SERVICE_FOR_CLIENT.getServiceForClient(intId)) {
                        case NONE_UTILIZED :
                            serviceForClientDTO.setNoneUtilized(true);
                            break;
                        case ADULT_DAY_HEALTH_CARE :
                            serviceForClientDTO.setAdultDayHealthCare(true);
                            break;
                        case CAREGIVER_SUPPORT :
                            serviceForClientDTO.setCaregiverSupport(true);
                            break;
                        case ASSISTED_TRANSPORTATION :
                            serviceForClientDTO.setAssistedTransportation(true);
                            break;
                        case CASE_MANAGEMENT :
                            serviceForClientDTO.setCaseManagement(true);
                            break;
                        case COMMUNITY_BASED_FOOD_PROGRAM :
                            serviceForClientDTO.setCommunityBasedFoodProgram(true);
                            break;
                        case CONSUMER_DIRECTED_IN_HOME_SERVICES :
                            serviceForClientDTO.setConsumerDirectedInHomeServices(true);
                            break;
                        case CONGREGATE_MEALS :
                            serviceForClientDTO.setCongregateMeals(true);
                            break;
                        case EQUIPMENT_SUPPLIES :
                            serviceForClientDTO.setEquipment(true);
                            break;
                        case FRIENDLY_VISITOR_TELEPHONE_REASSURANCE :
                            serviceForClientDTO.setFriendlyVisitor(true);
                            break;
                        case HEALTH_PROMOTION :
                            serviceForClientDTO.setHealthPromotion(true);
                            break;
                        case HEALTH_INSURANCE_COUNSELING :
                            serviceForClientDTO.setHealthInsuranceCounseling(true);
                            break;
                        case HOME_HEALTH_AIDE :
                            serviceForClientDTO.setHomeHealthAide(true);
                            break;
                        case HOME_DELIVERED_MEALS :
                            serviceForClientDTO.setHomeDeliveredMeals(true);
                            break;
                        case HOSPICE :
                            serviceForClientDTO.setHospice(true);
                            break;
                        case LEGAL_SERVICES :
                            serviceForClientDTO.setLegalServices(true);
                            break;
                        case HOUSING_ASSISTANCE :
                            serviceForClientDTO.setHousingAssistance(true);
                            break;
                        case MENTAL_HEALTH_SERVICES :
                            serviceForClientDTO.setMentalHealthServices(true);
                            break;
                        case NUTRITION_COUNSELING :
                            serviceForClientDTO.setNutritionCounseling(true);
                            break;
                        case OUTREACH :
                            serviceForClientDTO.setOutreach(true);
                            break;
                        case SPEECH_THERAPY :
                            serviceForClientDTO.setSpeechTherapy(true);
                            break;
                        case OCCUPATIONAL_THERAPY :
                            serviceForClientDTO.setOccupationalTherapy(true);
                            break;
                        case PERSONAL_CARE_LEVEL_1 :
                            serviceForClientDTO.setPersonalCareLevelOne(true);
                            break;
                        case PERSONAL_CARE_LEVEL_2 :
                            serviceForClientDTO.setPersonalCareLevelTwo(true);
                            break;
                        case PERSONAL_EMERGENCY_RESPONSE_SYSTEM :
                            serviceForClientDTO.setPersonalEmergencyResponseSystem(true);
                            break;
                        case PHYSICAL_THERAPY :
                            serviceForClientDTO.setPhysicalTherapy(true);
                            break;
                        case PROTECTIVE_SERVICES :
                            serviceForClientDTO.setProtectiveServices(true);
                            break;
                        case RESPITE :
                            serviceForClientDTO.setRespite(true);
                            break;
                        case RESPIRATORY_THERAPY :
                            serviceForClientDTO.setRespiratoryTherapy(true);
                            break;
                        case SENIOR_CENTER :
                            serviceForClientDTO.setSeniorCenter(true);
                            break;
                        case SENIOR_COMPANIONS :
                            serviceForClientDTO.setSeniorCompanions(true);
                            break;
                        case SERVICES_FOR_THE_BLIND :
                            serviceForClientDTO.setServicesForTheBlind(true);
                            break;
                        case SHOPPING :
                            serviceForClientDTO.setShopping(true);
                            break;
                        case SKILLED_NURSING :
                            serviceForClientDTO.setSkilledNursing(true);
                            break;
                        case SOCIAL_ADULT_DAY_CARE :
                            serviceForClientDTO.setSocialAdultDayCare(true);
                            break;
                        case OTHER:
                            serviceForClientDTO.setOther(true);
                            break;
                    }
                }
            }
            return serviceForClientDTO;
        } else {
            return new ServiceForClientDTO();
        }
    }
}
