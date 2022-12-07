package com.healthcare.service.impl;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Medication;
import com.healthcare.repository.MedicationRepository;
import com.healthcare.service.MedicationService;

/**
 * MedicationService
 */
@Service
@Transactional
public class MedicationServiceImpl extends BasicService<Medication, MedicationRepository> implements MedicationService {

	@Autowired
	private MedicationRepository medicationRepository;

	@Autowired
	public MedicationServiceImpl(MedicationRepository medicationRepository) {
		this.medicationRepository = medicationRepository;
	}

	@Nonnull
	@Override
	@Transactional
	public Medication save(@Nonnull Medication medication) {
		notNull(medication, "Activity must not be null");
		return medicationRepository.save(medication);
	}

	@Nullable
	@Override
	@Transactional
	public Medication findById(@Nonnull Long id) {
		notNull(id, "Medication Id must not be null");
		return medicationRepository.findOne(id);
	}

	@Override
	@Transactional
	public Long deleteById(@Nonnull Long id) {
		notNull(id, "Medication Id must not be null");
		if (medicationRepository.exists(id)) {
			medicationRepository.delete(id);
			return id;
		}
		return null;
	}

	@Override
	@Transactional
	public Long disableById(@Nonnull Long id) {
		return null;
	}

	@Override
	@Transactional
	public List<Medication> findAll() {
		return medicationRepository.findAll();
	}

	@Override
	public Long update(Medication medication) {
		save(medication);
		return medication.getId();
	}

}
