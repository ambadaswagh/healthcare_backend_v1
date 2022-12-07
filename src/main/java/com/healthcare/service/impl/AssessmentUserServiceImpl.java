package com.healthcare.service.impl;

import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;

import com.healthcare.model.entity.Review;
import com.healthcare.model.entity.User;
import com.healthcare.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.service.AssessmentUserService;

import io.jsonwebtoken.lang.Collections;

/**
 * Assessment user service
 */
@Service
@Transactional
public class AssessmentUserServiceImpl extends BasicService<AssessmentUser, AssessmentUserRepository> implements AssessmentUserService {

    private static final String REDIS_KEY = AssessmentUser.class.getSimpleName();

    @Autowired
    private AssessmentUserRepository assessmentUserRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, AssessmentUser> redisTemplate;

    @Autowired
    public AssessmentUserServiceImpl(AssessmentUserRepository assessmentUserRepository, RedisTemplate<String, AssessmentUser> redisTemplate) {
        this.assessmentUserRepository = assessmentUserRepository;
        this.redisTemplate = redisTemplate;
    }

    @Nonnull
    @Override
    @Transactional
    public AssessmentUser save(@Nonnull AssessmentUser assessmentUser) {
        notNull(assessmentUser, "Assessment user must not be null");

        AssessmentUser savedAssessmentUser = assessmentUserRepository.save(assessmentUser);
        redisTemplate.opsForHash().put(REDIS_KEY, savedAssessmentUser.getId(), savedAssessmentUser);

        return savedAssessmentUser;
    }

    @Override
    @Transactional
    public Review findReviewByAssessmentUserId(Long id) {
        return reviewRepository.findbyassessmentuserid(id);
    }

    @Override
    public User findUserByAssessmentUserId(Long id) {
        return userRepository.findByAssessmentUserId(id);
    }

    @Override
    @Transactional
    public AssessmentUser findById(Long id) {
        return assessmentUserRepository.findOne(id);
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        assessmentUserRepository.delete(id);
        return redisTemplate.opsForHash().delete(REDIS_KEY, id);
    }

    @Override
    @Transactional
    public Long disableById(Long id) {
		/*AssessmentUser assessmentUser = null;
		if (redisTemplate.opsForHash().hasKey(REDIS_KEY, id))
			assessmentUser = (AssessmentUser) redisTemplate.opsForHash().get(REDIS_KEY, id);
		else
			assessmentUser = assessmentUserRepository.findOne(id);
		if (assessmentUser != null) {
			assessmentUser.setStatus(EntityStatusEnum.DISABLE.getValue());
			assessmentUserRepository.save(assessmentUser);
			redisTemplate.opsForHash().put(REDIS_KEY, assessmentUser.getId(), assessmentUser);
			return assessmentUser.getId();
		}*/
        return null;
    }

    @Override @Transactional
    public List<AssessmentUser> findAll() {
        Map<Object, Object> assessmentUserMap = redisTemplate.opsForHash().entries(REDIS_KEY);
        List<AssessmentUser> rideList = Collections.arrayToList(assessmentUserMap.values().toArray());
        if (assessmentUserMap.isEmpty())
            rideList = assessmentUserRepository.findAll();
        return rideList;
    }

    @Override
    public List<AssessmentUser> findByUserId(Long userId) {
        return assessmentUserRepository.findByUserId(userId);
    }
}