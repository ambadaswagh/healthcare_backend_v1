package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.MedicineTaken;
import com.healthcare.model.entity.assessment.ServiceForClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface ServiceForClientRepository extends JpaRepository<ServiceForClient, Long>, JpaSpecificationExecutor<ServiceForClient> {

    List<ServiceForClient> findByUserId(Long userId);
}
