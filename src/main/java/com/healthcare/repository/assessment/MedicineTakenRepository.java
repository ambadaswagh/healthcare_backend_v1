package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.MedicineTaken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface MedicineTakenRepository extends JpaRepository<MedicineTaken, Long>, JpaSpecificationExecutor<MedicineTaken> {
    List<MedicineTaken> findByUserId(Long userId);
}
