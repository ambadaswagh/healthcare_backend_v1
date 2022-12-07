package com.healthcare.service;

import com.healthcare.model.entity.TrainingEmployee;

import java.util.List;

/**
 * Created by jean on 03/07/17.
 */
public interface TrainingEmployeeService extends IService<TrainingEmployee>, IFinder<TrainingEmployee> {

    TrainingEmployee save(TrainingEmployee trainingEmployee);

    List<TrainingEmployee> findAll();

    TrainingEmployee findById(Long id);

    Long deleteById(Long id);

    Long deleteByTrainingEmployee(TrainingEmployee trainingEmployee);

    Long findByTrainingEmployee(TrainingEmployee trainingEmployee);
}

