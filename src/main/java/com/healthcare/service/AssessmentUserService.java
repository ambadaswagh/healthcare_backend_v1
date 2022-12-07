package com.healthcare.service;

import com.healthcare.model.entity.Review;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.assessment.AssessmentUser;

import javax.transaction.Transactional;
import java.util.List;

/**
 * AssessmentUser service methods
 */
public interface AssessmentUserService extends IService<AssessmentUser>, IFinder<AssessmentUser> {

    List<AssessmentUser> findAll();

    @Transactional
    List<AssessmentUser>findByUserId(Long userId);

    Review findReviewByAssessmentUserId(Long id);

    User findUserByAssessmentUserId(Long id);
}