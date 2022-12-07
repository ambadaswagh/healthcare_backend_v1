package com.healthcare.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the admin_setting database
 * table.
 * 
 */
@Entity
@Table(name = "admin_setting")
@EqualsAndHashCode(callSuper = true)
public @Data class AdminSetting extends Audit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "admin_id")
	private Long adminId;

	@Column(name = "language_code")
	private String languageCode;

	@Column(name = "theme")
	private String theme;

	@Column(name = "main_white_label")
	private String mainWhiteLabel;

	@Column(name = "upper_left_label")
	private String upperLeftLabel;

	@OneToOne
	@JoinColumn(name = "logo_id")
	private Document logo;

	@Column(name = "auto_check_out_time")
	private String autoCheckoutTime;
        
        @Column(name = "meals_selected")
	private String mealsSelected;
        
    @Column(name = "shortcuts")
	private String shortcuts;

}