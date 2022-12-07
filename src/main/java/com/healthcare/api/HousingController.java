package com.healthcare.api;

import com.healthcare.dto.HousingDTO;
import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.Housing;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.HousingRepository;
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
@RequestMapping(value = "/api/housing")
public class HousingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HousingRepository housingRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    @ApiOperation(value = "save housing", notes = "save housing")
    @ApiParam(name = "housing", value = "housing to save", required = true)
    @PostMapping()
    public ResponseEntity<HousingDTO> create(@RequestBody HousingDTO housingDTO) {
        List<String> list = new ArrayList<>();

        for(TYPE_OF_HOUSING typeOfHousing : TYPE_OF_HOUSING.values()){
            switch (typeOfHousing){
                case MULTI_UNIT_HOUSING:
                    updateList(TYPE_OF_HOUSING.MULTI_UNIT_HOUSING.getValue(), housingDTO.isMultiUnitHousing(), list);
                    break;
                case SINGLE_FAMILY_HOME :
                    updateList(TYPE_OF_HOUSING.SINGLE_FAMILY_HOME.getValue(), housingDTO.isSingleFamilyHome(), list);
                    break;
                case OWNS :
                    updateList(TYPE_OF_HOUSING.OWNS.getValue(), housingDTO.isOwns(), list);
                    break;
                case RENTS :
                    updateList(TYPE_OF_HOUSING.RENTS.getValue(), housingDTO.isRents(), list);
                    break;
                case OTHER :
                    updateList(TYPE_OF_HOUSING.OTHER.getValue(), housingDTO.isOther(), list);
                    break;
            }
        }

        List<String> housingList = new ArrayList<>();
        for(HOUSING housing : HOUSING.values()){
            switch (housing){
                case ACCUMULATED_GARBADGE:
                    updateList(HOUSING.ACCUMULATED_GARBADGE.getValue(), housingDTO.isHscAccumulatedGarbadge(), housingList);
                    break;
                case CARBON_MONOXIDE_DETECTORS_NOT_PRESENT_WORKING :
                    updateList(HOUSING.CARBON_MONOXIDE_DETECTORS_NOT_PRESENT_WORKING.getValue(), housingDTO.isHscCarbonMonoxide(), housingList);
                    break;
                case DOORWAY_WIDTHS_ARE_INADEQUATE :
                    updateList(HOUSING.DOORWAY_WIDTHS_ARE_INADEQUATE.getValue(), housingDTO.isHscDoorwayWidths(), housingList);
                    break;
                case LOOSE_SCATTER_RUGS_PRESENT_IN_ONE_OR_MORE_ROOMS :
                    updateList(HOUSING.LOOSE_SCATTER_RUGS_PRESENT_IN_ONE_OR_MORE_ROOMS.getValue(), housingDTO.isHscLooseScatterRugs(), housingList);
                    break;
                case LAMP_OR_LIGHT_SWITCH_WITHIN_EASY_REACH_OF_THE_BED :
                    updateList(HOUSING.LAMP_OR_LIGHT_SWITCH_WITHIN_EASY_REACH_OF_THE_BED.getValue(), housingDTO.isHscLampOrLightSwitch(), housingList);
                    break;
                case NO_RUBBER_MATS_OR_NON_SLIP_DECALS_IN_THE_TUB_OR_SHOWER:
                    updateList(HOUSING.NO_RUBBER_MATS_OR_NON_SLIP_DECALS_IN_THE_TUB_OR_SHOWER.getValue(), housingDTO.isHscNoRubberMats(), housingList);
                    break;
                case SMOKE_DETECTORS_NOT_PRESENT_WORKING :
                    updateList(HOUSING.SMOKE_DETECTORS_NOT_PRESENT_WORKING.getValue(), housingDTO.isHscSmokeDetectors(), housingList);
                    break;
                case TELEPHONE_AND_APPLIANCE_CORDS_ARE_STRUNG_ACROSS_AREAS_WHERE_PEOPLE_WALK :
                    updateList(HOUSING.TELEPHONE_AND_APPLIANCE_CORDS_ARE_STRUNG_ACROSS_AREAS_WHERE_PEOPLE_WALK.getValue(), housingDTO.isHscTelephoneAndAppliance(), housingList);
                    break;
                case TRAFFIC_LANE_FROM_THE_BEDROOM_TO_THE_BATHROOM_IS_NOT_CLEAR_OF_OBSTACLES :
                    updateList(HOUSING.TRAFFIC_LANE_FROM_THE_BEDROOM_TO_THE_BATHROOM_IS_NOT_CLEAR_OF_OBSTACLES.getValue(), housingDTO.isHscTrafficLane(), housingList);
                    break;
                case NO_HANDRAILS_ON_THE_STAIRWAYS :
                    updateList(HOUSING.NO_HANDRAILS_ON_THE_STAIRWAYS.getValue(), housingDTO.isHscHandrails(), housingList);
                    break;
                case BAD_ODORS:
                    updateList(HOUSING.BAD_ODORS.getValue(), housingDTO.isHscBadOdors(), housingList);
                    break;
                case FLOORS_AND_STAIRWAYS_DIRTY_AND_CLUTTEREDS :
                    updateList(HOUSING.FLOORS_AND_STAIRWAYS_DIRTY_AND_CLUTTEREDS.getValue(), housingDTO.isHscFloorsAndStairways(), housingList);
                    break;
                case NO_LIGHTS_IN_THE_BATHROOM_OR_IN_THE_HALLWAY :
                    updateList(HOUSING.NO_LIGHTS_IN_THE_BATHROOM_OR_IN_THE_HALLWAY.getValue(), housingDTO.isHscNoLightsIn(), housingList);
                    break;
                case NO_LOCKS_ON_DOORS_OR_NOT_WORKING :
                    updateList(HOUSING.NO_LOCKS_ON_DOORS_OR_NOT_WORKING.getValue(), housingDTO.isHscNoLocksOnDoors(), housingList);
                    break;
                case NO_GRAB_BAR_IN_TUB_AND_SHOWER :
                    updateList(HOUSING.NO_GRAB_BAR_IN_TUB_AND_SHOWER.getValue(), housingDTO.isHscNoGrabBar(), housingList);
                    break;
                case STAIRWAYS_ARE_NOT_IN_GOOD_CONDITION :
                    updateList(HOUSING.NO_LOCKS_ON_DOORS_OR_NOT_WORKING.getValue(), housingDTO.isHscStairways(), housingList);
                    break;
                case OTHER :
                    updateList(HOUSING.NO_GRAB_BAR_IN_TUB_AND_SHOWER.getValue(), housingDTO.isHscOther(), housingList);
                    break;
            }
        }

        Housing housing = new Housing();
        try {
            propertyUtilsBean.copyProperties(housing, housingDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        housing.setTypeOfHousing(String.join(",", list));
        housing.setHomeSafetyChecklist(String.join(",", housingList));
        housing = housingRepository.save(housing);
        saveAssessmentUser(housing);
        return new ResponseEntity<HousingDTO>(housingDTO, HttpStatus.OK);
    }


    private void updateList(long value, boolean isSelected, List<String> list) {
        if(isSelected == true) {
            list.add(Long.toString(value));
        }
    }
    private void saveAssessmentUser(Housing housing) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(housing.getUserId());
        if(assessmentUser != null){
            assessmentUser.setHousing(housing);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get housing by id", notes = "get housing by id")
    @ApiImplicitParam(name = "id", value = "housing id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public HousingDTO read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<Housing> housingList = housingRepository.findByUserId(id);
        if(housingList.size() > 0) {

            Housing housing = housingList.get(0);
            String services = housing.getTypeOfHousing();
            String housingHomeServices = housing.getHomeSafetyChecklist();


            HousingDTO housingDTO = new HousingDTO();
            try {
                propertyUtilsBean.copyProperties(housingDTO, housing);
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
                    switch (TYPE_OF_HOUSING.getTypeOfHousing(intId)) {
                        case MULTI_UNIT_HOUSING :
                            housingDTO.setMultiUnitHousing(true);
                            break;
                        case SINGLE_FAMILY_HOME :
                            housingDTO.setSingleFamilyHome(true);
                            break;
                        case OWNS :
                            housingDTO.setOwns(true);
                            break;
                        case RENTS :
                            housingDTO.setRents(true);
                            break;
                        case OTHER :
                            housingDTO.setOther(true);
                            break;
                    }
                }
            }

            if(StringUtils.isNotBlank(housingHomeServices)) {
                List<Integer> intIds = Stream.of(housingHomeServices.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for(Integer intId : intIds) {
                    switch (HOUSING.getHousing(intId)) {
                        case ACCUMULATED_GARBADGE :
                            housingDTO.setHscAccumulatedGarbadge(true);
                            break;
                        case CARBON_MONOXIDE_DETECTORS_NOT_PRESENT_WORKING :
                            housingDTO.setHscCarbonMonoxide(true);
                            break;
                        case DOORWAY_WIDTHS_ARE_INADEQUATE :
                            housingDTO.setHscDoorwayWidths(true);
                            break;
                        case LOOSE_SCATTER_RUGS_PRESENT_IN_ONE_OR_MORE_ROOMS :
                            housingDTO.setHscLooseScatterRugs(true);
                            break;
                        case LAMP_OR_LIGHT_SWITCH_WITHIN_EASY_REACH_OF_THE_BED :
                            housingDTO.setHscLampOrLightSwitch(true);
                            break;
                        case NO_RUBBER_MATS_OR_NON_SLIP_DECALS_IN_THE_TUB_OR_SHOWER :
                            housingDTO.setHscNoRubberMats(true);
                            break;
                        case SMOKE_DETECTORS_NOT_PRESENT_WORKING :
                            housingDTO.setHscSmokeDetectors(true);
                            break;
                        case TELEPHONE_AND_APPLIANCE_CORDS_ARE_STRUNG_ACROSS_AREAS_WHERE_PEOPLE_WALK :
                            housingDTO.setHscTelephoneAndAppliance(true);
                            break;
                        case TRAFFIC_LANE_FROM_THE_BEDROOM_TO_THE_BATHROOM_IS_NOT_CLEAR_OF_OBSTACLES :
                            housingDTO.setHscTrafficLane(true);
                            break;
                        case NO_HANDRAILS_ON_THE_STAIRWAYS :
                            housingDTO.setHscHandrails(true);
                            break;
                        case BAD_ODORS :
                            housingDTO.setHscBadOdors(true);
                            break;
                        case FLOORS_AND_STAIRWAYS_DIRTY_AND_CLUTTEREDS :
                            housingDTO.setHscFloorsAndStairways(true);
                            break;
                        case NO_LIGHTS_IN_THE_BATHROOM_OR_IN_THE_HALLWAY :
                            housingDTO.setHscNoLightsIn(true);
                            break;
                        case NO_LOCKS_ON_DOORS_OR_NOT_WORKING :
                            housingDTO.setHscNoLocksOnDoors(true);
                            break;
                        case NO_GRAB_BAR_IN_TUB_AND_SHOWER :
                            housingDTO.setHscNoGrabBar(true);
                            break;
                        case STAIRWAYS_ARE_NOT_IN_GOOD_CONDITION :
                            housingDTO.setHscStairways(true);
                            break;
                        case OTHER :
                            housingDTO.setHscOther(true);
                            break;
                    }
                }
            }

            return housingDTO;
        } else {
            return new HousingDTO();
        }
    }
}
