package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> , JpaSpecificationExecutor<Menu> {
	
	@Query(value="SELECT a.name from Menu a where a.id in ?1")
	public List<String> findMenu(List<Long> ids);
}