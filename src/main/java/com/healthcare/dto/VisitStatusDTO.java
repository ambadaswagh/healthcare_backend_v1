package com.healthcare.dto;

import com.healthcare.model.entity.Visit;
import com.healthcare.model.enums.VisitTypeEnum;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

@Data
public class VisitStatusDTO {
    private ArrayList<VisitResponseDTO> validVisitors = new ArrayList<VisitResponseDTO>();
    private ArrayList<VisitResponseDTO> inValidVisitors = new ArrayList<VisitResponseDTO>();

    private Page<VisitResponseDTO> validVisitorsPage = null;
    private Page<VisitResponseDTO> inValidVisitorsPage = null;

    public VisitStatusDTO(){}

}
