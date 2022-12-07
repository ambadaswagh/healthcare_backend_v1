package com.healthcare.api.model;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by Moustafa Hamed
 */
@Data
public class TableArrangementResponseDTO implements Serializable{

    private static final long serialVersionUID = -1831382670376350377L;

    private Integer totalTables;
    private List<TableSeatDTO> tables;

}
