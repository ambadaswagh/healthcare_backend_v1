package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.Iadls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface IadlsRepository extends JpaRepository<Iadls, Long>, JpaSpecificationExecutor<Iadls> {

    List<Iadls> findByUserId(Long userId);
}
