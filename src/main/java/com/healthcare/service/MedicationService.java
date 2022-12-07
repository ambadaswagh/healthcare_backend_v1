package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Medication;

/**
 * Medication service methods
 */
public interface MedicationService extends IService<Medication>, IFinder<Medication> {
	List<Medication> findAll();
	Long update(Medication medication);
}
