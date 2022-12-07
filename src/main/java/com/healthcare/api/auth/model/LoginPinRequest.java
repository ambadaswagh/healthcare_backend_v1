package com.healthcare.api.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Hitesh on 10/7/2017.
 */
@Data
public class LoginPinRequest implements Serializable{
    private String pinOrBarcode;
    private long agencyId;
}
