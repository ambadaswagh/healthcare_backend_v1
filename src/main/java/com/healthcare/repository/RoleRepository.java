package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthcare.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

	Role findByLevel(long level);

	List<Role> findByStatus(long status);

	@Query(value = "select * from role r where r.id = ?1 and r.status = ?2", nativeQuery = true)
	List<Role> findByIdAndStatus(@Param("id") long id, @Param("status") long status);

}
