package com.healthcare.api.model;

import com.healthcare.model.entity.Document;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExpressEmployeeRequestDTO implements Serializable {

    private static final long serialVersionUID = 8109925376598549926L;

    private Integer id;
    private Document signature;
    private String pin;

}
