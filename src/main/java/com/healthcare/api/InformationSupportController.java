package com.healthcare.api;

import com.healthcare.model.entity.assessment.AssessmentUser;
import static com.healthcare.api.common.HealthcareConstants.*;
import com.healthcare.dto.InformationSupportDTO;
import com.healthcare.model.entity.assessment.InformationSupport;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.InformationSupportRepository;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/informationSupport")
public class InformationSupportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InformationSupportRepository informationSupportRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    @ApiOperation(value = "save informationSupport", notes = "save informationSupport")
    @ApiParam(name = "informationSupport", value = "informationSupport to save", required = true)
    @PostMapping()
    public ResponseEntity<InformationSupportDTO> create(@RequestBody InformationSupportDTO informationSupportDTO) {
        List<String> listFactors = new ArrayList<>();
        for(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT factor : FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.values()) {
            switch (factor) {
                case TRANSPORTATION:
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.TRANSPORTATION.getValue(), informationSupportDTO.isFactorsTransportation(), listFactors);
                    break;
                case FINANCES :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.FINANCES.getValue(), informationSupportDTO.isFactorsFinances(), listFactors);
                    break;
                case FAMILY :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.FAMILY.getValue(), informationSupportDTO.isFactorsFamily(), listFactors);
                    break;
                case JOB :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.JOB.getValue(), informationSupportDTO.isFactorsJob(), listFactors);
                    break;
                case RESPONSIBILITIES :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.RESPONSIBILITIES.getValue(), informationSupportDTO.isFactorsResponsibilities(), listFactors);
                    break;
                case PHYSICAL_BURDEN :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.PHYSICAL_BURDEN.getValue(), informationSupportDTO.isFactorsPhysicalBurden(), listFactors);
                    break;
                case RELIABILITY :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.RELIABILITY.getValue(), informationSupportDTO.isFactorsReliability(), listFactors);
                    break;
                case HEALTH_PROBLEMS :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.HEALTH_PROBLEMS.getValue(), informationSupportDTO.isFactorsHealthProblems(), listFactors);
                    break;
                case EMOTIONAL_BURDEN:
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.EMOTIONAL_BURDEN.getValue(), informationSupportDTO.isFactorsEmotionalBurden(), listFactors);
                    break;
                case LIVING_DISTANCE :
                    updateList(FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.LIVING_DISTANCE.getValue(), informationSupportDTO.isFactorsLivingDistance(), listFactors);
                    break;
            }
        }

        List<String> listServices = new ArrayList<>();
        for(SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER service : SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.values()) {
            switch (service) {
                case ADULT_DAY_SERVICES:
                    updateList(SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.ADULT_DAY_SERVICES.getValue(), informationSupportDTO.isSerciceAdultDayServices(), listServices);
                    break;
                case PERSONAL_CARE_LEVEL_1 :
                    updateList(SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.PERSONAL_CARE_LEVEL_1.getValue(), informationSupportDTO.isSercicePersonalCareLevelOne(), listServices);
                    break;
                case PERSONAL_CARE_LEVEL_2 :
                    updateList(SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.PERSONAL_CARE_LEVEL_2.getValue(), informationSupportDTO.isSercicePersonalCareLevelTwo(), listServices);
                    break;
                case IN_HOME_CONTACT_SUPPORT:
                    updateList(SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.IN_HOME_CONTACT_SUPPORT.getValue(), informationSupportDTO.isSerciceHomeContact(), listServices);
                    break;
            }
        }

        InformationSupport informationSupport = new InformationSupport();
        try {
            propertyUtilsBean.copyProperties(informationSupport, informationSupportDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        informationSupport.setFactorsLimitInformalSupportInvolvement(String.join(",", listFactors));
        informationSupport.setSerciceAsRespiteForCaregiver(String.join(",", listServices));
        informationSupport = informationSupportRepository.save(informationSupport);
        saveAssessmentUser(informationSupport);
        return new ResponseEntity<InformationSupportDTO>(informationSupportDTO, HttpStatus.OK);
    }

    private void updateList(long value, boolean isSelected, List<String> list) {
        if(isSelected == true) {
            list.add(Long.toString(value));
        }
    }

    private void saveAssessmentUser(@RequestBody InformationSupport informationSupport) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(informationSupport.getUserId());
        if(assessmentUser != null){
            assessmentUser.setInformationSupport(informationSupport);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get informationSupport by id", notes = "get informationSupport by id")
    @ApiImplicitParam(name = "id", value = "informationSupport id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public InformationSupportDTO read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<InformationSupport> informationSupportList = informationSupportRepository.findByUserId(id);
        if(informationSupportList.size() > 0) {
            InformationSupport informationSupport = informationSupportList.get(0);
            String factors = informationSupport.getFactorsLimitInformalSupportInvolvement();
            String services = informationSupport.getSerciceAsRespiteForCaregiver();


            InformationSupportDTO informationSupportDTO = new InformationSupportDTO();
            try {
                propertyUtilsBean.copyProperties(informationSupportDTO, informationSupport);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if(StringUtils.isNotBlank(factors)) {
                List<Integer> intIds = Stream.of(factors.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (FACTORS_THAT_LIMIT_INFORMAL_SUPPORT_INVOLVEMENT.getFactors(intId)) {
                        case TRANSPORTATION :
                            informationSupportDTO.setFactorsTransportation(true);
                            break;
                        case FINANCES :
                            informationSupportDTO.setFactorsFinances(true);
                            break;
                        case FAMILY :
                            informationSupportDTO.setFactorsFamily(true);
                            break;
                        case JOB :
                            informationSupportDTO.setFactorsJob(true);
                            break;
                        case RESPONSIBILITIES:
                            informationSupportDTO.setFactorsResponsibilities(true);
                            break;
                        case PHYSICAL_BURDEN:
                            informationSupportDTO.setFactorsPhysicalBurden(true);
                            break;
                        case RELIABILITY:
                            informationSupportDTO.setFactorsReliability(true);
                            break;
                        case HEALTH_PROBLEMS:
                            informationSupportDTO.setFactorsHealthProblems(true);
                            break;
                        case EMOTIONAL_BURDEN:
                            informationSupportDTO.setFactorsEmotionalBurden(true);
                            break;
                        case LIVING_DISTANCE:
                            informationSupportDTO.setFactorsLivingDistance(true);
                            break;
                    }
                }
            }

            if(StringUtils.isNotBlank(services)) {
                List<Integer> intIds = Stream.of(services.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (SERVICES_COULD_BE_PROVIDED_AS_RESPITE_FOR_CAREGIVER.getServices(intId)) {
                        case ADULT_DAY_SERVICES :
                            informationSupportDTO.setSerciceAdultDayServices(true);
                            break;
                        case PERSONAL_CARE_LEVEL_1:
                            informationSupportDTO.setSercicePersonalCareLevelOne(true);
                            break;
                        case PERSONAL_CARE_LEVEL_2:
                            informationSupportDTO.setSercicePersonalCareLevelTwo(true);
                            break;
                        case IN_HOME_CONTACT_SUPPORT:
                            informationSupportDTO.setSerciceHomeContact(true);
                            break;
                    }
                }
            }
            return informationSupportDTO;
        } else {
            return new InformationSupportDTO();
        }
    }
}
