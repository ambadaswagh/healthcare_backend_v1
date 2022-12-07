package com.healthcare.integration.service;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Menu;
import com.healthcare.model.entity.Role;
import com.healthcare.service.MenuService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MenuServiceTest {
	@Autowired
	private MenuService menuService;

	String name = "Menu A";
	String url = "/menu/menu";
	String angularUrl = "/angular/a";
	String page = "A";
	String clazz = "Clazz";
	String imgUrl = "/img/a.jpg";
	Calendar createdAt = Calendar.getInstance();
	Integer displayOrder = 1;


	private Role role;
	private Menu menu;
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
	
	@Before
	public void setup() {
		testEntitySericeFactory.saveData = true;
		role = testEntitySericeFactory.createRole(1L);
		menu=null;
	}

	@After
	public void rollback() {
		if(menu!=null){
			menuService.deleteById(menu.getId());
		}
		testEntitySericeFactory.redisCleanUp();
	}


	@Test
	public void testSaveMenu() {
		menu = createNewMenu();
		menuService.save(menu);
		Assert.assertNotNull(menu.getId());
	}

	@Test
	public void testGetMenu() {
		menu = createNewMenu();
		menuService.save(menu);
		Assert.assertNotNull(menuService.findById(menu.getId()));
	}

	@Test
	public void testUpdateMenu() {
		String newImgUrl = "/img/new.jpg";

		menu = createNewMenu();
		menuService.save(menu);
		Assert.assertEquals(menu.getImgUrl(), imgUrl);
		Menu menuSaved = menuService.findById(menu.getId());
		menuSaved.setImgUrl(newImgUrl);
		menuService.save(menuSaved);
		Menu menuMofified = menuService.findById(menu.getId());
		Assert.assertEquals(menuMofified.getImgUrl(), newImgUrl);
	}

	@Test
	public void testDeleteMenu() {
		Menu menu = createNewMenu();
		menuService.save(menu);
		Assert.assertNotNull(menu.getId());
		menuService.deleteById(menu.getId());
		Assert.assertNull(menuService.findById(menu.getId()));
	}

	@Test
	public void testDisableMenu() {
		Menu menu = createNewMenu();
		menuService.save(menu);
		Assert.assertNotNull(menu.getId());
		Long disableMenuId = menuService.disableById(menu.getId());
		Assert.assertNotNull(disableMenuId);
		Menu disableMenu = menuService.findById(disableMenuId);
		Assert.assertNotNull(disableMenu.getId());
		Assert.assertEquals(0, disableMenu.getStatus().intValue());
	}

	private Menu createNewMenu() {
		Menu menu = new Menu();
		menu.setAngularUrl(angularUrl);
		menu.setClazz(clazz);
		menu.setCreatedAt(new Timestamp(createdAt.getTimeInMillis()));
		menu.setDisplayOrder(displayOrder);
		menu.setImgUrl(imgUrl);
		menu.setName(name);
		menu.setPage(page);
		//menu.setRole(role);
		menu.setStatus(1);
		menu.setUrl(url);
		return menu;
	}

}
