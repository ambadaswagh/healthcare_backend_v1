package com.healthcare.api;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.api.model.OrderRequest;
import com.healthcare.model.entity.Order;
import com.healthcare.model.response.Response;
import com.healthcare.service.OrderService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UtilsResponse responseBuilder;
	
	@ApiOperation(value = "list all order", notes = "list all order")
	@RequestMapping(method = RequestMethod.GET)
	public Response list(HttpServletRequest req, HttpServletResponse res) {
		List<Order> orders = orderService.findAll();
		return responseBuilder.successResponse(orders, "OK");
	}
	
	@ApiOperation(value = "updateStatus", notes = "updateStatus")
	@ApiParam(name = "orderRequest", value = "orderRequest", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@RequestMapping(method = RequestMethod.POST)
	public Response updateStatus(HttpServletRequest req, HttpServletResponse res, @RequestBody OrderRequest orderRequest) {
		Order order = orderService.findById(orderRequest.getId());
		order.setStatus(orderRequest.getStatus());
		orderService.update(order);
		return responseBuilder.successResponse("Update status successfully", "OK");
	}
	
	@ApiOperation(value = "deliver order", notes = "deliver order")
	@ApiParam(name = "orderRequest", value = "orderRequest", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@RequestMapping(value="/deliver" ,method = RequestMethod.POST)
	public Response deliver(HttpServletRequest req, HttpServletResponse res, @RequestBody OrderRequest orderRequest) {
		Order order = orderService.findById(orderRequest.getId());
		order.setDeliveryTime(new Date());
		orderService.update(order);
		return responseBuilder.successResponse("Update DeliverTime successfully", "OK");
	}

}
