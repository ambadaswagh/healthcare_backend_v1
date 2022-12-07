package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.InformationSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface InformationSupportRepository extends JpaRepository<InformationSupport, Long>, JpaSpecificationExecutor<InformationSupport> {
    List<InformationSupport> findByUserId(Long userId);
}
