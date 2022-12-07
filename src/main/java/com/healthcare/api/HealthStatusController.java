package com.healthcare.api;

import com.healthcare.dto.HealthStatusDTO;
import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.HealthStatus;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.HealthStatusRepository;
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
@RequestMapping(value = "/api/healthStatus")
public class HealthStatusController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HealthStatusRepository healthStatusRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    @ApiOperation(value = "save healthStatus", notes = "save healthStatus")
    @ApiParam(name = "healthStatus", value = "healthStatus to save", required = true)
    @PostMapping()
    public ResponseEntity<HealthStatusDTO> create(@RequestBody HealthStatusDTO healthStatusDTO) {
        List<String> list = new ArrayList<>();

        for(ASSISTIVE_DEVICES assistiveDevices : ASSISTIVE_DEVICES.values()){
            switch (assistiveDevices){
                case WHEELCHAIR:
                    updateList(ASSISTIVE_DEVICES.WHEELCHAIR.getValue(), healthStatusDTO.isWheelchair(), list);
                    break;
                case WALKER :
                    updateList(ASSISTIVE_DEVICES.WALKER.getValue(), healthStatusDTO.isWalker(), list);
                    break;
                case SCOOTER :
                    updateList(ASSISTIVE_DEVICES.SCOOTER.getValue(), healthStatusDTO.isScooter(), list);
                    break;
                case DENTURE :
                    updateList(ASSISTIVE_DEVICES.DENTURE.getValue(), healthStatusDTO.isDenture(), list);
                    break;
                case PARTIAL :
                    updateList(ASSISTIVE_DEVICES.PARTIAL.getValue(), healthStatusDTO.isPartial(), list);
                    break;
                case HEARING_AID:
                    updateList(ASSISTIVE_DEVICES.HEARING_AID.getValue(), healthStatusDTO.isHearingAid(), list);
                    break;
                case CANE :
                    updateList(ASSISTIVE_DEVICES.CANE.getValue(), healthStatusDTO.isCane(), list);
                    break;
                case BED_RAIL :
                    updateList(ASSISTIVE_DEVICES.BED_RAIL.getValue(), healthStatusDTO.isBedRail(), list);
                    break;
                case ACCESSIBLE_VEHICLE :
                    updateList(ASSISTIVE_DEVICES.ACCESSIBLE_VEHICLE.getValue(), healthStatusDTO.isAccessibleVehicle(), list);
                    break;
                case GLASSES :
                    updateList(ASSISTIVE_DEVICES.GLASSES.getValue(), healthStatusDTO.isGlasses(), list);
                    break;
                case FULL :
                    updateList(ASSISTIVE_DEVICES.FULL.getValue(), healthStatusDTO.isFull(), list);
                    break;
            }
        }

        List<String> healthList = new ArrayList<>();
        for(HEALTH_STATUSES healthStatuses : HEALTH_STATUSES.values()){
            switch (healthStatuses){
                case ALZHEIMERS:
                    updateList(HEALTH_STATUSES.ALZHEIMERS.getValue(), healthStatusDTO.isAlzheimers(), healthList);
                    break;
                case ARTHRITIS :
                    updateList(HEALTH_STATUSES.ARTHRITIS.getValue(), healthStatusDTO.isArthritis(), healthList);
                    break;
                case CANCER :
                    updateList(HEALTH_STATUSES.CANCER.getValue(), healthStatusDTO.isCancer(), healthList);
                    break;
                case CELLULITIS :
                    updateList(HEALTH_STATUSES.CELLULITIS.getValue(), healthStatusDTO.isCellulitis(), healthList);
                    break;
                case COLITIS :
                    updateList(HEALTH_STATUSES.COLITIS.getValue(), healthStatusDTO.isColitis(), healthList);
                    break;
                case COLOSTOMY:
                    updateList(HEALTH_STATUSES.COLOSTOMY.getValue(), healthStatusDTO.isColostomy(), healthList);
                    break;
                case CONSTIPATION :
                    updateList(HEALTH_STATUSES.CONSTIPATION.getValue(), healthStatusDTO.isConstipation(), healthList);
                    break;
                case DIABETES :
                    updateList(HEALTH_STATUSES.DIABETES.getValue(), healthStatusDTO.isDiabetes(), healthList);
                    break;
                case DECUBITUS_ULCERS :
                    updateList(HEALTH_STATUSES.DECUBITUS_ULCERS.getValue(), healthStatusDTO.isDecubitusUlcers(), healthList);
                    break;
                case DEVELOPMENTAL_DISABILITIES :
                    updateList(HEALTH_STATUSES.DEVELOPMENTAL_DISABILITIES.getValue(), healthStatusDTO.isDevelopmentalDisabilities(), healthList);
                    break;
                case DIGESTIVE_PROBLEMS :
                    updateList(HEALTH_STATUSES.DIGESTIVE_PROBLEMS.getValue(), healthStatusDTO.isDigestiveProblems(), healthList);
                    break;
                case DIVERTICULITIS:
                    updateList(HEALTH_STATUSES.DIVERTICULITIS.getValue(), healthStatusDTO.isDiverticulitis(), healthList);
                    break;
                case GALL_BLADDER_DISEASE :
                    updateList(HEALTH_STATUSES.GALL_BLADDER_DISEASE.getValue(), healthStatusDTO.isGallBladderDisease(), healthList);
                    break;
                case HEARING_IMPAIRMENT :
                    updateList(HEALTH_STATUSES.HEARING_IMPAIRMENT.getValue(), healthStatusDTO.isHearingImpairment(), healthList);
                    break;
                case HIATAL_HERNIA :
                    updateList(HEALTH_STATUSES.HIATAL_HERNIA.getValue(), healthStatusDTO.isHiatalHernia(), healthList);
                    break;
                case HIGH_CHOLESTEROL :
                    updateList(HEALTH_STATUSES.HIGH_CHOLESTEROL.getValue(), healthStatusDTO.isHighCholesterol(), healthList);
                    break;
                case LEGALLY_BLIND:
                    updateList(HEALTH_STATUSES.LEGALLY_BLIND.getValue(), healthStatusDTO.isLegallyBlind(), healthList);
                    break;
                case LIVER_DISEASE :
                    updateList(HEALTH_STATUSES.LIVER_DISEASE.getValue(), healthStatusDTO.isLiverDisease(), healthList);
                    break;
                case LOW_BLOOD_PRESSURE :
                    updateList(HEALTH_STATUSES.LOW_BLOOD_PRESSURE.getValue(), healthStatusDTO.isLowBloodPressure(), healthList);
                    break;
                case RENAL_DISEASE :
                    updateList(HEALTH_STATUSES.RENAL_DISEASE.getValue(), healthStatusDTO.isRenalDisease(), healthList);
                    break;
                case DEPENDENT :
                    updateList(HEALTH_STATUSES.DEPENDENT.getValue(), healthStatusDTO.isDependent(), healthList);
                    break;
                case STROKE :
                    updateList(HEALTH_STATUSES.STROKE.getValue(), healthStatusDTO.isStroke(), healthList);
                    break;
                case PARALYSIS:
                    updateList(HEALTH_STATUSES.PARALYSIS.getValue(), healthStatusDTO.isParalysis(), healthList);
                    break;
                case SPEECH_PROBLEMS :
                    updateList(HEALTH_STATUSES.SPEECH_PROBLEMS.getValue(), healthStatusDTO.isSpeechProblems(), healthList);
                    break;
                case SWALLOWING_DIFFICULTIES :
                    updateList(HEALTH_STATUSES.SWALLOWING_DIFFICULTIES.getValue(), healthStatusDTO.isSwallowingDifficulties(), healthList);
                    break;
                case TASTE_IMPAIRMENT :
                    updateList(HEALTH_STATUSES.TASTE_IMPAIRMENT.getValue(), healthStatusDTO.isTasteImpairment(), healthList);
                    break;
                case ULCER :
                    updateList(HEALTH_STATUSES.ULCER.getValue(), healthStatusDTO.isUlcer(), healthList);
                    break;
                case VISUAL_IMPAIRMENT:
                    updateList(HEALTH_STATUSES.VISUAL_IMPAIRMENT.getValue(), healthStatusDTO.isVisualImpairment(), healthList);
                    break;
                case ALCOHOLISM :
                    updateList(HEALTH_STATUSES.ALCOHOLISM.getValue(), healthStatusDTO.isAlcoholism(), healthList);
                    break;
                case ASTHMA :
                    updateList(HEALTH_STATUSES.ASTHMA.getValue(), healthStatusDTO.isAsthma(), healthList);
                    break;
                case CARDIOVASCULAR_DISORDER :
                    updateList(HEALTH_STATUSES.CARDIOVASCULAR_DISORDER.getValue(), healthStatusDTO.isCardiovascularDisorder(), healthList);
                    break;
                case CHRONIC_OBSTRUCTIVE_PULMONARY_DISEASE :
                    updateList(HEALTH_STATUSES.CHRONIC_OBSTRUCTIVE_PULMONARY_DISEASE.getValue(), healthStatusDTO.isChronicObstructivePulmonaryDisease(), healthList);
                    break;
                case CONGESTIVE_HEART_FAILURE :
                    updateList(HEALTH_STATUSES.CONGESTIVE_HEART_FAILURE.getValue(), healthStatusDTO.isCongestiveHeartFailure(), healthList);
                    break;
                case CHRONIC_PAIN:
                    updateList(HEALTH_STATUSES.CHRONIC_PAIN.getValue(), healthStatusDTO.isChronicPain(), healthList);
                    break;
                case DIARRHEA :
                    updateList(HEALTH_STATUSES.DIARRHEA.getValue(), healthStatusDTO.isDiarrhea(), healthList);
                    break;
                case DIALYSIS :
                    updateList(HEALTH_STATUSES.DIALYSIS.getValue(), healthStatusDTO.isDialysis(), healthList);
                    break;
                case DEHYDRATION :
                    updateList(HEALTH_STATUSES.DEHYDRATION.getValue(), healthStatusDTO.isDehydration(), healthList);
                    break;
                case DENTAL_PROBLEMS :
                    updateList(HEALTH_STATUSES.DENTAL_PROBLEMS.getValue(), healthStatusDTO.isDentalProblems(), healthList);
                    break;
                case DEMENTIA:
                    updateList(HEALTH_STATUSES.DEMENTIA.getValue(), healthStatusDTO.isDementia(), healthList);
                    break;
                case FREQUENT_FALLS :
                    updateList(HEALTH_STATUSES.FREQUENT_FALLS.getValue(), healthStatusDTO.isFrequentFalls(), healthList);
                    break;
                case FRACTURES :
                    updateList(HEALTH_STATUSES.FRACTURES.getValue(), healthStatusDTO.isFractures(), healthList);
                    break;
                case HEART_DISEASE :
                    updateList(HEALTH_STATUSES.HEART_DISEASE.getValue(), healthStatusDTO.isHeartDisease(), healthList);
                    break;
                case HIGH_BLOOD_PRESSURE :
                    updateList(HEALTH_STATUSES.HIGH_BLOOD_PRESSURE.getValue(), healthStatusDTO.isHighBloodPressure(), healthList);
                    break;
                case OXYGEN :
                    updateList(HEALTH_STATUSES.OXYGEN.getValue(), healthStatusDTO.isOxygen(), healthList);
                    break;
                case HYPOGLYCEMIA:
                    updateList(HEALTH_STATUSES.HYPOGLYCEMIA.getValue(), healthStatusDTO.isHypoglycemia(), healthList);
                    break;
                case HIV :
                    updateList(HEALTH_STATUSES.HIV.getValue(), healthStatusDTO.isHiv(), healthList);
                    break;
                case PERNICIOUS_ANEMIA :
                    updateList(HEALTH_STATUSES.PERNICIOUS_ANEMIA.getValue(), healthStatusDTO.isPerniciousAnemia(), healthList);
                    break;
                case OSTEOPOROSIS :
                    updateList(HEALTH_STATUSES.OSTEOPOROSIS.getValue(), healthStatusDTO.isOsteoporosis(), healthList);
                    break;
                case PARKINSON :
                    updateList(HEALTH_STATUSES.PARKINSON.getValue(), healthStatusDTO.isParkinson(), healthList);
                    break;
                case RESPIRATORY_PROBLEMS:
                    updateList(HEALTH_STATUSES.RESPIRATORY_PROBLEMS.getValue(), healthStatusDTO.isRespiratoryProblems(), healthList);
                    break;
                case SHINGLES :
                    updateList(HEALTH_STATUSES.SHINGLES.getValue(), healthStatusDTO.isShingles(), healthList);
                    break;
                case URINARY_TRACT_INFECTION :
                    updateList(HEALTH_STATUSES.URINARY_TRACT_INFECTION.getValue(), healthStatusDTO.isUrinaryTractInfection(), healthList);
                    break;
                case TRAUMATICBRAIN_INJURY :
                    updateList(HEALTH_STATUSES.TRAUMATICBRAIN_INJURY.getValue(), healthStatusDTO.isTraumaticbrainInjury(), healthList);
                    break;
                case TREMORS :
                    updateList(HEALTH_STATUSES.TREMORS.getValue(), healthStatusDTO.isTremors(), healthList);
                    break;
                case OTHERS :
                    updateList(HEALTH_STATUSES.OTHERS.getValue(), healthStatusDTO.isOthers(), healthList);
                    break;
            }
        }

        HealthStatus healthStatus = new HealthStatus();
        try {
            propertyUtilsBean.copyProperties(healthStatus, healthStatusDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        healthStatus.setHaveAssistiveDevice(String.join(",", list));
        healthStatus.setParticipantIllnessAndDisability(String.join(",", healthList));
        healthStatus = healthStatusRepository.save(healthStatus);
        saveAssessmentUser(healthStatus);
//      saveAssessmentUser(healthStatus);
        return new ResponseEntity<HealthStatusDTO>(healthStatusDTO, HttpStatus.OK);
    }

    private void saveAssessmentUser(HealthStatus healthStatus) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(healthStatus.getUserId());
        if(assessmentUser != null){
            assessmentUser.setHealthStatus(healthStatus);
            assessmentUserRepository.save(assessmentUser);
        }
    }


    private void updateList(long value, boolean isSelected, List<String> list) {
        if(isSelected == true) {
            list.add(Long.toString(value));
        }
    }
//    private void saveAssessmentUser(@RequestBody HealthStatus healthStatus) {
//        AssessmentUser assessmentUser = assessmentUserRepository.findOne(healthStatus.getUserId());
//        if(assessmentUser != null){
//            assessmentUser.setHealthStatus(healthStatus);
//            assessmentUserRepository.save(assessmentUser);
//        }
//    }

    @ApiOperation(value = "get healthStatus by id", notes = "get healthStatus by id")
    @ApiImplicitParam(name = "id", value = "housing id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public HealthStatusDTO read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<HealthStatus> healthStatusList = healthStatusRepository.findByUserId(id);
        if(healthStatusList.size() > 0) {

            HealthStatus healthStatus = healthStatusList.get(0);
            String services = healthStatus.getHaveAssistiveDevice();
            String healthServices = healthStatus.getParticipantIllnessAndDisability();


            HealthStatusDTO healthStatusDTO = new HealthStatusDTO();
            try {
                propertyUtilsBean.copyProperties(healthStatusDTO, healthStatus);
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
                    switch (ASSISTIVE_DEVICES.getAssistiveDevices(intId)) {
                        case WHEELCHAIR :
                            healthStatusDTO.setWheelchair(true);
                            break;
                        case WALKER :
                            healthStatusDTO.setWalker(true);
                            break;
                        case SCOOTER :
                            healthStatusDTO.setScooter(true);
                            break;
                        case DENTURE :
                            healthStatusDTO.setDenture(true);
                            break;
                        case PARTIAL :
                            healthStatusDTO.setPartial(true);
                            break;
                        case HEARING_AID :
                            healthStatusDTO.setHearingAid(true);
                            break;
                        case CANE :
                            healthStatusDTO.setCane(true);
                            break;
                        case BED_RAIL :
                            healthStatusDTO.setBedRail(true);
                            break;
                        case ACCESSIBLE_VEHICLE :
                            healthStatusDTO.setAccessibleVehicle(true);
                            break;
                        case GLASSES :
                            healthStatusDTO.setGlasses(true);
                            break;
                        case FULL :
                            healthStatusDTO.setFull(true);
                            break;
                    }
                }
            }

            if(StringUtils.isNotBlank(healthServices)) {
                List<Integer> intIds = Stream.of(healthServices.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (HEALTH_STATUSES.getHealthStatuses(intId)) {
                        case ALZHEIMERS :
                            healthStatusDTO.setAlzheimers(true);
                            break;
                        case ARTHRITIS :
                            healthStatusDTO.setArthritis(true);
                            break;
                        case CANCER :
                            healthStatusDTO.setCancer(true);
                            break;
                        case CELLULITIS :
                            healthStatusDTO.setCellulitis(true);
                            break;
                        case COLITIS :
                            healthStatusDTO.setColitis(true);
                            break;
                        case COLOSTOMY :
                            healthStatusDTO.setColostomy(true);
                            break;
                        case CONSTIPATION :
                            healthStatusDTO.setConstipation(true);
                            break;
                        case DIABETES :
                            healthStatusDTO.setDiabetes(true);
                            break;
                        case DECUBITUS_ULCERS :
                            healthStatusDTO.setDecubitusUlcers(true);
                            break;
                        case DEVELOPMENTAL_DISABILITIES :
                            healthStatusDTO.setDevelopmentalDisabilities(true);
                            break;
                        case DIGESTIVE_PROBLEMS :
                            healthStatusDTO.setDigestiveProblems(true);
                            break;
                        case DIVERTICULITIS :
                            healthStatusDTO.setDiverticulitis(true);
                            break;
                        case GALL_BLADDER_DISEASE :
                            healthStatusDTO.setGallBladderDisease(true);
                            break;
                        case HEARING_IMPAIRMENT :
                            healthStatusDTO.setHearingImpairment(true);
                            break;
                        case HIATAL_HERNIA :
                            healthStatusDTO.setHiatalHernia(true);
                            break;
                        case HIGH_CHOLESTEROL :
                            healthStatusDTO.setHighCholesterol(true);
                            break;
                        case LEGALLY_BLIND :
                            healthStatusDTO.setLegallyBlind(true);
                            break;
                        case LIVER_DISEASE :
                            healthStatusDTO.setLiverDisease(true);
                            break;
                        case LOW_BLOOD_PRESSURE :
                            healthStatusDTO.setLowBloodPressure(true);
                            break;
                        case RENAL_DISEASE :
                            healthStatusDTO.setRenalDisease(true);
                            break;
                        case DEPENDENT :
                            healthStatusDTO.setDependent(true);
                            break;
                        case STROKE :
                            healthStatusDTO.setStroke(true);
                            break;
                        case PARALYSIS :
                            healthStatusDTO.setParalysis(true);
                            break;
                        case SPEECH_PROBLEMS :
                            healthStatusDTO.setSpeechProblems(true);
                            break;
                        case SWALLOWING_DIFFICULTIES :
                            healthStatusDTO.setSwallowingDifficulties(true);
                            break;
                        case TASTE_IMPAIRMENT :
                            healthStatusDTO.setTasteImpairment(true);
                            break;
                        case ULCER :
                            healthStatusDTO.setUlcer(true);
                            break;
                        case VISUAL_IMPAIRMENT :
                            healthStatusDTO.setVisualImpairment(true);
                            break;
                        case ALCOHOLISM :
                            healthStatusDTO.setAlcoholism(true);
                            break;
                        case ASTHMA :
                            healthStatusDTO.setAsthma(true);
                            break;
                        case CARDIOVASCULAR_DISORDER :
                            healthStatusDTO.setCardiovascularDisorder(true);
                            break;
                        case CHRONIC_OBSTRUCTIVE_PULMONARY_DISEASE :
                            healthStatusDTO.setChronicObstructivePulmonaryDisease(true);
                            break;
                        case CONGESTIVE_HEART_FAILURE :
                            healthStatusDTO.setCongestiveHeartFailure(true);
                            break;
                        case CHRONIC_PAIN :
                            healthStatusDTO.setChronicPain(true);
                            break;
                        case DIARRHEA :
                            healthStatusDTO.setDiarrhea(true);
                            break;
                        case DIALYSIS :
                            healthStatusDTO.setDialysis(true);
                            break;
                        case DEHYDRATION :
                            healthStatusDTO.setDehydration(true);
                            break;
                        case DENTAL_PROBLEMS :
                            healthStatusDTO.setDentalProblems(true);
                            break;
                        case DEMENTIA :
                            healthStatusDTO.setDementia(true);
                            break;
                        case FREQUENT_FALLS :
                            healthStatusDTO.setFrequentFalls(true);
                            break;
                        case FRACTURES :
                            healthStatusDTO.setFractures(true);
                            break;
                        case HEART_DISEASE :
                            healthStatusDTO.setHeartDisease(true);
                            break;
                        case HIGH_BLOOD_PRESSURE :
                            healthStatusDTO.setHighBloodPressure(true);
                            break;
                        case OXYGEN :
                            healthStatusDTO.setOxygen(true);
                            break;
                        case HYPOGLYCEMIA :
                            healthStatusDTO.setHypoglycemia(true);
                            break;
                        case HIV :
                            healthStatusDTO.setHiv(true);
                            break;
                        case PERNICIOUS_ANEMIA :
                            healthStatusDTO.setPerniciousAnemia(true);
                            break;
                        case OSTEOPOROSIS :
                            healthStatusDTO.setOsteoporosis(true);
                            break;
                        case PARKINSON :
                            healthStatusDTO.setParkinson(true);
                            break;
                        case RESPIRATORY_PROBLEMS :
                            healthStatusDTO.setRespiratoryProblems(true);
                            break;
                        case SHINGLES :
                            healthStatusDTO.setShingles(true);
                            break;
                        case URINARY_TRACT_INFECTION :
                            healthStatusDTO.setUrinaryTractInfection(true);
                            break;
                        case TRAUMATICBRAIN_INJURY :
                            healthStatusDTO.setTraumaticbrainInjury(true);
                            break;
                        case TREMORS :
                            healthStatusDTO.setTremors(true);
                            break;
                        case OTHERS :
                            healthStatusDTO.setOthers(true);
                            break;
                    }
                }
            }

            return healthStatusDTO;
        } else {
            return new HealthStatusDTO();
        }
    }
}
