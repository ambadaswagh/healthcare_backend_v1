package com.healthcare.model.entity;

import com.healthcare.model.enums.StatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/*** Created by Mostapha on 07/01/2018.*/
@Entity
@Table(name = "vehicle")
@Data
public class Vehicle extends Audit implements Serializable {
    private static final long serialVersionUID = 8756897253090002699L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(name = "vin")
    private String vin;
    @Column(name = "year")
    private Integer year;
    @Column(name = "model")
    private String model;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "vehicle_brand_id")
    private VehicleBrand vehicleBrand;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "profile_photo_id")
    private Document profilePhoto;

    @Column(name = "vehicle_license_state")
    private String vehicleLicenseState;

    @Column(name = "vehicle_plate_num")
    private String vehiclePlateNum;

    @Column(name = "vehicle_registration")
    private String vehicleRegistration;

    @Column(name = "vehicle_registration_start")
    private Date vehicleRegistrationStart;

    @Column(name = "vehicle_registration_expire")
    private Date vehicleRegistrationExpire;

    @Column(name = "vehicle_registration_status")
    private Integer vehicleRegistrationStatus;

    @Column(name = "liability_insurance")
    private String liabilityInsurance;

    @Column(name = "liability_amount")
    private Double liabilityAmount;

    @Column(name = "liability_ecarride_certificate_holder")
    private Integer liabilityEcarrideCertificateHolder;

    @Column(name = "liability_ecarride_additional_insured")
    private Integer liabilityEcarrideAdditionalInsured;

    @Column(name = "liability_self_insured")
    private Integer liabilitySelfInsured;

    @Column(name = "liability_name_insurer")
    private String liabilityNameInsurer;

    @Column(name = "liability_insurance_start")
    private Date liabilityInsuranceStart;

    @Column(name = "liability_insurance_expire")
    private Date liabilityInsuranceExpire;

    @Column(name = "liability_insurance_status")
    private Integer liabilityInsuranceStatus;

    @Column(name = "extra_insurance")
    private String extraInsurance;

    @Column(name = "extra_insurance_start")
    private Date extraInsuranceStart;

    @Column(name = "extra_insurance_expire")
    private Date extraInsuranceExpire;

    @Column(name = "extra_insurance_status")
    private Integer extraInsuranceStatus;

    @Column(name = "vehicle_tlc_fhv_license")
    private String vehicleTlcFhvLicense;

    @Column(name = "vehicle_tlc_fhv_license_num")
    private String vehicleTlcFhvLicenseNum;

    @Column(name = "vehicle_tlc_fhv_license_start")
    private Date vehicleTlcFhvLicenseStart;

    @Column(name = "vehicle_tlc_fhv_license_expire")
    private Date vehicleTlcFhvLicenseExpire;

    @Column(name = "vehicle_tlc_fhv_license_status")
    private Integer vehicleTlcFhvLicenseStatus;

    @Column(name = "vehicle_inspection")
    private String vehicleInspection;

    @Column(name = "vehicle_inspection_start")
    private Date vehicleInspectionStart;

    @Column(name = "vehicle_inspection_expire")
    private Date vehicleInspectionExpire;

    @Column(name = "vehicle_inspection_status")
    private Integer vehicleInspectionStatus;

    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "allow_wheelchair")
    private Integer allowWheelchair;

    @Column(name = "allow_stretcher")
    private Integer allowStretcher;

    @Column(name = "is_ambulatory")
    private Integer isAmbulatory;

    @Column(name = "color")
    private String color;

    @Column(name = "cab_type")
    private String cabType;

    @Column(name = "driver_id")
    private String driver;
}