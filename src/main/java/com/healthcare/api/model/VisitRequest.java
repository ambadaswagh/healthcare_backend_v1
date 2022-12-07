package com.healthcare.api.model;

public class VisitRequest {

	private Long id;
	private String customPin;
	private Long agencyId;
	private Long companyId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomPin() {
		return customPin;
	}
	public void setCustomPin(String customPin) {
		this.customPin = customPin;
	}
	public Long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	
}
