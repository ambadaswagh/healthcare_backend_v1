package com.healthcare.integration.service;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Menu;
import com.healthcare.model.entity.Role;
import com.healthcare.repository.MenuRepository;
import com.healthcare.service.MenuService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MenuServiceRedisTest {
	@Autowired
	private MenuService menuService;

	@MockBean
	private MenuRepository menuRepository;


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
	private Long id = 7L;
	
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
		menu.setId(id);
		Mockito.when(menuRepository.save(menu)).thenReturn(menu);
		menuService.save(menu);
		Menu savedMenu = menuService.findById(menu.getId());
		Assert.assertNotNull(savedMenu);
	}

	@Test
	public void testUpdateMenu() {
		String newImgUrl = "/img/new.jpg";

		menu = createNewMenu();
		menu.setId(id);
		Mockito.when(menuRepository.save(menu)).thenReturn(menu);
		menuService.save(menu);
		Menu menuSaved = menuService.findById(menu.getId());
		menuSaved.setImgUrl(newImgUrl);
		Mockito.when(menuRepository.save(menuSaved)).thenReturn(menuSaved);
		menuService.save(menuSaved);
		Menu menuMofified = menuService.findById(menu.getId());
		Assert.assertEquals(menuMofified.getImgUrl(), newImgUrl);
	}

	@Test
	public void testDeleteMenu() {
		Menu menu = createNewMenu();
		menu.setId(id);
		Mockito.when(menuRepository.save(menu)).thenReturn(menu);
		menuService.save(menu);
		Mockito.doNothing().when(menuRepository).delete(menu.getId());
		Assert.assertNotNull(menuService.deleteById(menu.getId()));
	}

	@Test
	public void testDisableMenu() {
		Menu menu = createNewMenu();
		menu.setId(id);
		Mockito.when(menuRepository.save(menu)).thenReturn(menu);
		menuService.save(menu);
		Menu savedMenu = menuService.findById(menu.getId());
		savedMenu.setStatus(0);
		Mockito.when(menuRepository.save(savedMenu)).thenReturn(savedMenu);
		Long disableMenuId = menuService.disableById(savedMenu.getId());
		Assert.assertNotNull(disableMenuId);
		Menu disableMenu = menuService.findById(disableMenuId);
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
