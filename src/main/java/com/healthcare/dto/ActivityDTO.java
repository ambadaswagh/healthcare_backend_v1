package com.healthcare.dto;

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Employee;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Hitesh on 10/13/2017.
 */
@Data
public class ActivityDTO {

    private Long id;
    private String name;
    private Integer status;
    private String timeStart;
    private String timeEnd;
    private String date;
    private String location;
    private String note;

    public ActivityDTO(Activity activity){
        this.id = activity.getId();
        this.name = activity.getName();
        this.status = activity.getStatus();
        this.timeStart = activity.getTimeStart();
        this.timeEnd  = activity.getTimeEnd();
        this.date = activity.getDate();
        this.location = activity.getLocation();
        this.note = activity.getNote();
    }
}
