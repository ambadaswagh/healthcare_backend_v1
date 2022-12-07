package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.healthcare.model.entity.Review;
import com.healthcare.repository.ReviewRepository;
import com.healthcare.service.ReviewService;

@Service
@Transactional
public class ReviewServiceImpl extends BasicService<Review, ReviewRepository>
implements ReviewService {

	private static final String REDIS_KEY = Review.class.getSimpleName();

	private ReviewRepository reviewRepository;
	private RedisTemplate<String, Review> redisTemplate;

	@Autowired
	public ReviewServiceImpl(ReviewRepository reviewRepository, RedisTemplate<String, Review> redisTemplate) {
		this.reviewRepository = reviewRepository;
		this.redisTemplate = redisTemplate;
	}

	@Override @Transactional
	public Review save(Review review) {
		Review savedReview = reviewRepository.save(review);
		redisTemplate.opsForHash().put(REDIS_KEY, savedReview.getId(), savedReview);

		return savedReview;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		reviewRepository.delete(id);

		return redisTemplate.opsForHash().delete(REDIS_KEY, id);
	}

	@Override @Transactional
	public Review findById(Long id) {
		/*Object review = redisTemplate.opsForHash().get(REDIS_KEY, id);
		if (review != null) {
			return (Review) review;
		}*/

		return reviewRepository.findOne(id);
	}

	@Override @Transactional
	public List<Review> findAll() {
		/*Map<Object, Object> reviewMap = redisTemplate.opsForHash().entries(REDIS_KEY);
		if (!reviewMap.isEmpty()) {
			return CollectionUtils.arrayToList(reviewMap.values().toArray());
		}*/

		return reviewRepository.findAllAssessments();
	}
	
	@Override
	public List<Review> findByUser(Long userId) {
		/*List<Review> list = null;
		Map<Object, Object> reviewMap = redisTemplate.opsForHash().entries(REDIS_KEY);
		if (!reviewMap.isEmpty()) {
			list =  CollectionUtils.arrayToList(reviewMap.values().toArray());
		}

		if(list==null || list.isEmpty()){
			System.out.println("userId is"+userId);
			return reviewRepository.findReviewByUserId(userId);	
		}else{
			List<Review> returnList = new ArrayList<Review>();
			for(Review review : list){
				if(userId.intValue() == review.getUser().getId().intValue()){
					returnList.add(review);
				}
			}
			return returnList;
		}*/
		return reviewRepository.findReviewByUserId(userId);
		
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
