/**
 * 
 */
package com.healthcare.api;

import com.healthcare.api.model.Email;
import com.healthcare.model.entity.Meal;
import com.healthcare.model.entity.Order;
import com.healthcare.model.entity.Visit;
import com.healthcare.service.MealService;
import com.healthcare.service.OrderService;
import com.healthcare.service.VisitService;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Mail controller
 *
 */
@RestController
@RequestMapping("/api/email")
public class MailController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JavaMailSender mailSender;
	
//	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private VisitService visitService;
	
	@Autowired
	private MealService mealService;
	
	@RequestMapping(value = "send", method = RequestMethod.POST, produces = { "application/xml", "application/json" })
	public ResponseEntity<Email> template(@RequestBody Email email) throws Exception {
		
		Visit visit = visitService.findById(email.getVisitId());
		
		Meal meal = mealService.findById(visit.getSelectedBreakfast().getId());
		
		Order order = new Order();
		
		order.setUser(visit.getUser().getId().intValue());
	    order.setMeal(meal);
	    //order.setOrganization(meal.getOrganization());
	    order.setOrderTime(new Date());
	    order.setStatus(0);
	    order = orderService.save(order);
	    
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("companyName", order.getOrganization().getName());
		model.put("companyAddress1", order.getOrganization().getAddressOne());
		model.put("companyAddress2", order.getOrganization().getAddressTwo());
		model.put("companyCity", order.getOrganization().getCity());
		model.put("companyState", order.getOrganization().getState());
		model.put("companyZipcode", order.getOrganization().getCode());
		
		model.put("orderId", order.getId());
		model.put("orderTime", order.getOrderTime());
		model.put("deliverTime", order.getDeliveryTime());
		model.put("orderStatus", (order.getStatus() == 1 ? "Accepted" : "Rejected" ));
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "email.vm", "UTF-8", model);
		
		System.out.println(text);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		//mimeMessageHelper.setFrom(email.getMailFrom());
		mimeMessageHelper.setTo(order.getOrganization().getEmail());
		mimeMessageHelper.setSubject("Order Information " + order.getId());
		mimeMessageHelper.setText(text, true);

		mailSender.send(mimeMessage);
		
		return new ResponseEntity<Email>(email, HttpStatus.OK);
	}
}
