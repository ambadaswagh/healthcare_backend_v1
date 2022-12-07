package com.healthcare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.ui.context.Theme;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {

	Admin findByUsername(String username);
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (1,2) and a.id=?1 ")
	Admin findOnePlatformAdmin(long id);
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (3,4) and a.id=?1 ")
	Admin findOneCompanyAdmin(long id);
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (5,6) and a.id=?1 ")
	Admin findOneAgencyAdmin(long id);
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (1,2) ")
	List<Admin> findAllPlatformAdmin();
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (1,2) ")
	Page<Admin> findAllPlatformAdminPageable(Pageable pageable);

	@Query("select a from Admin a JOIN  a.role r where r.level in (3,4) ")
	List<Admin> findAllCompanyAdmin();
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (3,4) ")
	Page<Admin> findAllCompanyAdminPageable(Pageable pageable);

	@Query("delete from Admin a where a.role.level in (3,4) and a.id=?1")
	void deleteCompanyAdmin(Long id);
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (5,6) ")
	List<Admin> findAllAgencyAdmin();
	
	@Query("select a from Admin a JOIN  a.role r where r.level in (5,6) ")
	Page<Admin> findAllAgencyAdminPageable(Pageable pageable);

	public List<Admin> findByEmail(String email);
	
	@Query(value ="select t.* from admin_theme t WHERE t.user_Id = ?1 ", nativeQuery = true)
	Theme selectTheme(int userId);
	
	@Query(value ="insert into admin_theme values theme_selected = ?1 WHERE user_Id = ?2 ", nativeQuery = true)
	boolean insertTheme(String theme_selected, int userId);
	
	
	@Query(value ="delete from admin_theme WHERE user_Id = ?1 ", nativeQuery = true)
	boolean deleteTheme(int userId);

	@Query("select a from Admin a JOIN a.role r where a.id in ?1 AND r.level in ?2")
	Page<Admin> findAllAdminByAdminList(List<Long> adminCompanyId, List<Long> roles, Pageable pageable);

	@Query("select a from Admin a where exists (select 1 from  AdminAgencyCompanyOrganization r where r.admin.id = a.id and r.company.id =?1)")
	Page<Admin> findAdminByCompanyId(Long companyId, Pageable pageable);

}