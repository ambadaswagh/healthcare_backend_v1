package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Billing;
import com.healthcare.model.entity.Ride;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.repository.BillingRepository;
import com.healthcare.service.BillingService;
import com.healthcare.service.RideService;
import com.healthcare.service.UserService;
import com.healthcare.service.VisitService;

@Service
@Transactional
public class BillingServiceImpl  extends BasicService<Billing, BillingRepository> implements BillingService {
	private static final String KEY = Billing.class.getSimpleName();
	
	@Autowired
	BillingRepository billingRepository;
	@Autowired
	UserService userService;
	@Autowired
	RideService rideService;
	@Autowired
	VisitService visitService;
	
	@Autowired
	private RedisTemplate<String, Visit> redisTemplate;
	
	@Override
	public Billing save(Billing entity) {
		if(entity.getId() != null){
			entity.setUpdatedAt(new Timestamp(new Date().getTime()));
    	} else {
    		entity.setCreatedAt(new Timestamp(new Date().getTime()));
    		entity.setUpdatedAt(new Timestamp(new Date().getTime()));
    	}
		Long user_id = null;
		if(entity.getTripId() != null){
			Ride ride = rideService.findById(entity.getTripId());
			user_id = ride.getUser().getId();
			User user = userService.findById(user_id);
			entity.setBillingPrice(user.getTrip_price());
		}
		if(entity.getVisitId() != null){
			Visit visit = visitService.findById(entity.getVisitId());
			user_id = visit.getUser().getId();
			User user = userService.findById(user_id);
			entity.setBillingPrice(user.getVisit_price());
		}
		if(entity.getBillingPrice() == null)
			entity.setBillingPrice(0.0);
		if(entity.getBillingAdjustment() == null)
			entity.setBillingAdjustment(0.0);
		Double final_price = entity.getBillingPrice() + entity.getBillingAdjustment();
		if(final_price < 0){
			final_price = 0.0;
		}
		entity.setBillingFinalPrice(final_price);
		entity = billingRepository.save(entity);
		redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
		return entity;
	}

	@Override
	public Billing findById(Long id) {
		// TODO Auto-generated method stub
		return billingRepository.findOne(id);
	}

	@Override
	public Long deleteById(Long id) {
		// TODO Auto-generated method stub
		billingRepository.delete(id);
		return redisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	public Long disableById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Billing findByVisitId(Long visitId) {
		// TODO Auto-generated method stub
		return billingRepository.findByVisit(visitId);
	}

	@Override
	public Billing findByTripId(Long tripId) {
		// TODO Auto-generated method stub
		return billingRepository.findByTrip(tripId);
	}

	@Override
	public Billing save(Billing entity, Long adminId) {
		if(entity.getId() != null){
			Billing saved_billing = findById(entity.getId());
			entity.setBillingDate(saved_billing.getBillingDate());
			entity.setVerifiedDate(saved_billing.getVerifiedDate());
			if(entity.getStatus().equals("Billed") && saved_billing.getStatus().equals("Pending")){
				entity.setBillingDate(new Date());
				entity.setBilledBy(adminId);
			}
			if(entity.getStatus().equals("Verified") && saved_billing.getStatus().equals("Billed")){
				entity.setVerifiedDate(new Date());
				entity.setVerifiedBy(adminId);
			}
		} else {
			if(entity.getStatus().equals("Billed")){
				entity.setBillingDate(new Date());
				entity.setBilledBy(adminId);
			}
		}
		return save(entity);
	}

}
