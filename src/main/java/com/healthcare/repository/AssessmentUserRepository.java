package com.healthcare.repository;

import com.healthcare.model.entity.Review;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.assessment.AssessmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * AssessmentUser Repo
 */
public interface AssessmentUserRepository extends JpaRepository<AssessmentUser, Long>, JpaSpecificationExecutor<AssessmentUser> {


    List<AssessmentUser> findByUserId(Long userId);
}
