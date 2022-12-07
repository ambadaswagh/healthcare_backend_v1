package com.healthcare.api.model;

import java.io.Serializable;
import lombok.Data;


/**
 * 
 * @author Moustafa Hamed
 *
 */
@Data
public class SeatDTO implements Serializable{
    private static final long serialVersionUID = -8628606551462730290L;

    private Long seatId;
    private boolean available;


}
