package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
	
	@Query(value="select r.*,u.id,u.first_name from review r inner join user u on r.user_id=u.id where r.user_id=:userId",nativeQuery=true)
	List<Review> findReviewByUserId(@Param("userId") Long userId);
	
	@Query(value="select r.id as id, r.created_at as created_at,r.updated_at as updated_at,r.assessment_date as assessment_date,u.id as id,u.firstName as firstName from review as r inner join user as u on r.user_id = u.id",nativeQuery=true)
	List<Review> findAllAssessments();

	@Query(value="select r.* from review r inner join assessment_user au on r.id=au.review_id where au.id=:assessmentUserId", nativeQuery=true)
	Review findbyassessmentuserid(@Param("assessmentUserId") Long assessmentUserId);

	List<Review> findByAssessmentUserId(Long id);
}