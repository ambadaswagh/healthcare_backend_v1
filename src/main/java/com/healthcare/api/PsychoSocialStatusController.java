package com.healthcare.api;

import com.healthcare.model.entity.assessment.AssessmentUser;
import static com.healthcare.api.common.HealthcareConstants.*;

import com.healthcare.dto.PsychoSocialStatusDTO;
import com.healthcare.model.entity.assessment.PsychoSocialStatus;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.PsychoSocialStatusRepository;
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
@RequestMapping(value = "/api/psychoSocialStatus")
public class PsychoSocialStatusController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PsychoSocialStatusRepository psychoSocialStatusRepository;

    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    @ApiOperation(value = "save psychoSocialStatus", notes = "save psychoSocialStatus")
    @ApiParam(name = "psychoSocialStatus", value = "psychoSocialStatus to save", required = true)
    @PostMapping()
    public ResponseEntity<PsychoSocialStatusDTO> create(@RequestBody PsychoSocialStatusDTO psychoSocialStatusDTO) {
        List<String> list = new ArrayList<>();
        for(PSYCHO_SOCIAL_CONDITION psychoSocialCondition : PSYCHO_SOCIAL_CONDITION.values()) {
            switch (psychoSocialCondition) {
                case DEMENTIA :
                    updateList(PSYCHO_SOCIAL_CONDITION.DEMENTIA.getValue(), psychoSocialStatusDTO.isDementia(), list);
                    break;
                case COOPERATIVE :
                    updateList(PSYCHO_SOCIAL_CONDITION.COOPERATIVE.getValue(), psychoSocialStatusDTO.isCooperative(), list);
                    break;
                case ANGER :
                    updateList(PSYCHO_SOCIAL_CONDITION.ANGER.getValue(), psychoSocialStatusDTO.isAnger(), list);
                    break;
                case ALERT :
                    updateList(PSYCHO_SOCIAL_CONDITION.ALERT.getValue(), psychoSocialStatusDTO.isAlert(), list);
                    break;
                case DELIRIUM :
                    updateList(PSYCHO_SOCIAL_CONDITION.DELIRIUM.getValue(), psychoSocialStatusDTO.isDelirium(), list);
                    break;
                case DEPRESSED_SADNESS_RECURRENT_CRYING_TEARFULNESS :
                    updateList(PSYCHO_SOCIAL_CONDITION.DEPRESSED_SADNESS_RECURRENT_CRYING_TEARFULNESS.getValue(), psychoSocialStatusDTO.isDepressed(), list);
                    break;
                case DIMINISHED_INTERPERSONAL_SKILLS :
                    updateList(PSYCHO_SOCIAL_CONDITION.DIMINISHED_INTERPERSONAL_SKILLS.getValue(), psychoSocialStatusDTO.isDiminishedInterpersonalSkills(), list);
                    break;
                case DISRUPTIVE_SOCIALLY :
                    updateList(PSYCHO_SOCIAL_CONDITION.DISRUPTIVE_SOCIALLY.getValue(), psychoSocialStatusDTO.isDisruptiveSocially(), list);
                    break;
                case HALLUCINATIONS :
                    updateList(PSYCHO_SOCIAL_CONDITION.HALLUCINATIONS.getValue(), psychoSocialStatusDTO.isHallucinations(), list);
                    break;
                case HOARDING :
                    updateList(PSYCHO_SOCIAL_CONDITION.HOARDING.getValue(), psychoSocialStatusDTO.isHoarding(), list);
                    break;
                case IMPAIRED_DECISION_MAKING_ISOLATION :
                    updateList(PSYCHO_SOCIAL_CONDITION.IMPAIRED_DECISION_MAKING_ISOLATION.getValue(), psychoSocialStatusDTO.isImpairedDecisionMakingIsolation(), list);
                    break;
                case LONELY :
                    updateList(PSYCHO_SOCIAL_CONDITION.LONELY.getValue(), psychoSocialStatusDTO.isLonely(), list);
                    break;
                case PHYSICAL_AGGRESSIVE :
                    updateList(PSYCHO_SOCIAL_CONDITION.PHYSICAL_AGGRESSIVE.getValue(), psychoSocialStatusDTO.isPhysicalAggressive(), list);
                    break;
                case MEMORY_DEFICIT :
                    updateList(PSYCHO_SOCIAL_CONDITION.MEMORY_DEFICIT.getValue(), psychoSocialStatusDTO.isMemoryDeficit(), list);
                    break;
                case RESISTANCE_TO_CARE :
                    updateList(PSYCHO_SOCIAL_CONDITION.RESISTANCE_TO_CARE.getValue(), psychoSocialStatusDTO.isResistanceToCare(), list);
                    break;
                case SLEEPING_PROBLEMS :
                    updateList(PSYCHO_SOCIAL_CONDITION.SLEEPING_PROBLEMS.getValue(), psychoSocialStatusDTO.isSleepingProblems(), list);
                    break;
                case SUICIDAL_THOUGHTS :
                    updateList(PSYCHO_SOCIAL_CONDITION.SUICIDAL_THOUGHTS.getValue(), psychoSocialStatusDTO.isSuicidalThoughts(), list);
                    break;
                case SELF_NEGLECT :
                    updateList(PSYCHO_SOCIAL_CONDITION.SELF_NEGLECT.getValue(), psychoSocialStatusDTO.isSelfNeglect(), list);
                    break;
                case SHORT_TERM_MEMORY_DEFICIT :
                    updateList(PSYCHO_SOCIAL_CONDITION.SHORT_TERM_MEMORY_DEFICIT.getValue(), psychoSocialStatusDTO.isShortTermMemoryDeficit(), list);
                    break;
                case SUICIDAL_BEHAVIOR :
                    updateList(PSYCHO_SOCIAL_CONDITION.SUICIDAL_BEHAVIOR.getValue(), psychoSocialStatusDTO.isSuicidalBehavior(), list);
                    break;
                case VERBAL_DISRUTIVE :
                    updateList(PSYCHO_SOCIAL_CONDITION.VERBAL_DISRUTIVE.getValue(), psychoSocialStatusDTO.isVerbalDisrutive(), list);
                    break;
                case WANDERING :
                    updateList(PSYCHO_SOCIAL_CONDITION.WANDERING.getValue(), psychoSocialStatusDTO.isWandering(), list);
                    break;
                case WORRIED_OR_ANXIOUS :
                    updateList(PSYCHO_SOCIAL_CONDITION.WORRIED_OR_ANXIOUS.getValue(), psychoSocialStatusDTO.isWorriedOrAnxious(), list);
                    break;
                case WITHDRAWL :
                    updateList(PSYCHO_SOCIAL_CONDITION.WITHDRAWL.getValue(), psychoSocialStatusDTO.isWithdraw(), list);
                    break;
                case OTHER :
                    updateList(PSYCHO_SOCIAL_CONDITION.OTHER.getValue(), psychoSocialStatusDTO.isOther(), list);
                    break;
            }
        }
        PsychoSocialStatus psychoSocialStatus = new PsychoSocialStatus();
        try {
            propertyUtilsBean.copyProperties(psychoSocialStatus, psychoSocialStatusDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        psychoSocialStatus.setPsychoSocialCondition(String.join(",", list));
        psychoSocialStatus = psychoSocialStatusRepository.save(psychoSocialStatus);
        saveAssessmentUser(psychoSocialStatus);
        return new ResponseEntity<PsychoSocialStatusDTO>(psychoSocialStatusDTO, HttpStatus.OK);
    }

    private void updateList(long value, boolean isSelected, List<String> list) {
        if(isSelected == true) {
            list.add(Long.toString(value));
        }
    }

    private void saveAssessmentUser(PsychoSocialStatus psychoSocialStatus) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(psychoSocialStatus.getUserId());
        if(assessmentUser != null){
            assessmentUser.setPsychoSocialStatus(psychoSocialStatus);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get psychoSocialStatus by id", notes = "get psychoSocialStatus by id")
    @ApiImplicitParam(name = "id", value = "psychoSocialStatus id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public PsychoSocialStatusDTO read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<PsychoSocialStatus> psychoSocialStatusList = psychoSocialStatusRepository.findByUserId(id);
        if(psychoSocialStatusList.size() > 0) {
            PsychoSocialStatus psychoSocialStatus = psychoSocialStatusList.get(0);
            String conditions = psychoSocialStatus.getPsychoSocialCondition();


            PsychoSocialStatusDTO psychoSocialStatusDTO = new PsychoSocialStatusDTO();
            try {
                propertyUtilsBean.copyProperties(psychoSocialStatusDTO, psychoSocialStatus);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if(StringUtils.isNotBlank(conditions)) {
                List<Integer> intIds = Stream.of(conditions.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (PSYCHO_SOCIAL_CONDITION.getPsychoSocialCondition(intId)) {
                        case DEMENTIA :
                            psychoSocialStatusDTO.setDementia(true);
                            break;
                        case COOPERATIVE :
                            psychoSocialStatusDTO.setCooperative(true);
                            break;
                        case ANGER :
                            psychoSocialStatusDTO.setAnger(true);
                            break;
                        case ALERT :
                            psychoSocialStatusDTO.setAlert(true);
                            break;
                        case DELIRIUM: 
                            psychoSocialStatusDTO.setDelirium(true);
                            break;
                        case DEPRESSED_SADNESS_RECURRENT_CRYING_TEARFULNESS: 
                            psychoSocialStatusDTO.setDepressed(true);
                            break;
                        case DIMINISHED_INTERPERSONAL_SKILLS: 
                            psychoSocialStatusDTO.setDiminishedInterpersonalSkills(true);
                            break;
                        case DISRUPTIVE_SOCIALLY: 
                            psychoSocialStatusDTO.setDisruptiveSocially(true);
                            break;
                        case HALLUCINATIONS: 
                            psychoSocialStatusDTO.setHallucinations(true);
                            break;
                        case HOARDING: 
                            psychoSocialStatusDTO.setHoarding(true);
                            break;
                        case IMPAIRED_DECISION_MAKING_ISOLATION: 
                            psychoSocialStatusDTO.setImpairedDecisionMakingIsolation(true);
                            break;
                        case LONELY: 
                            psychoSocialStatusDTO.setLonely(true);
                            break;
                        case PHYSICAL_AGGRESSIVE: 
                            psychoSocialStatusDTO.setPhysicalAggressive(true);
                            break;
                        case MEMORY_DEFICIT: 
                            psychoSocialStatusDTO.setMemoryDeficit(true);
                            break;
                        case RESISTANCE_TO_CARE: 
                            psychoSocialStatusDTO.setResistanceToCare(true);
                            break;
                        case SLEEPING_PROBLEMS: 
                            psychoSocialStatusDTO.setSleepingProblems(true);
                            break;
                        case SUICIDAL_THOUGHTS: 
                            psychoSocialStatusDTO.setSuicidalThoughts(true);
                            break;
                        case SELF_NEGLECT :
                            psychoSocialStatusDTO.setSelfNeglect(true);
                            break;
                        case SHORT_TERM_MEMORY_DEFICIT:
                            psychoSocialStatusDTO.setShortTermMemoryDeficit(true);
                            break;
                        case SUICIDAL_BEHAVIOR :
                            psychoSocialStatusDTO.setSuicidalBehavior(true);
                            break;
                        case VERBAL_DISRUTIVE :
                            psychoSocialStatusDTO.setVerbalDisrutive(true);
                            break;
                        case WANDERING :
                            psychoSocialStatusDTO.setWandering(true);
                            break;
                        case WORRIED_OR_ANXIOUS :
                            psychoSocialStatusDTO.setWorriedOrAnxious(true);
                            break;
                        case WITHDRAWL :
                            psychoSocialStatusDTO.setWithdraw(true);
                            break;
                        case OTHER :
                            psychoSocialStatusDTO.setOther(true);
                            break;
                    }
                }
            }
            return psychoSocialStatusDTO;
        } else {
            return new PsychoSocialStatusDTO();
        }
    }
}
