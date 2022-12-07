package com.healthcare.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Order;
import com.healthcare.repository.OrderRepository;
import com.healthcare.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public List<Order> findAll() {
		return orderRepository.findOrders();
	}

	@Override
	public void update(Order order) {
		orderRepository.save(order);
	}

	@Override
	public Order findById(Long id) {
		// TODO Auto-generated method stub
		return orderRepository.findOne(id);
	}

	@Override
	public Order save(Order order) {
		Order savedOrder = orderRepository.save(order);
		return savedOrder;
	}

}
