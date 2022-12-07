package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Order;

public interface OrderService  {

	public List<Order> findAll();
	public Order findById(Long id);
	public void update(Order order);
	public Order save(Order order);
}
