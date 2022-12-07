package com.healthcare.api.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;


/**
 * 
 * @author Moustafa Hamed
 *
 */
@Data
public class TableSeatDTO implements Serializable{
    
    private static final long serialVersionUID = 7615650345222877700L;
    
    private Integer totalSeats;
    private List<SeatDTO> seats;

}
