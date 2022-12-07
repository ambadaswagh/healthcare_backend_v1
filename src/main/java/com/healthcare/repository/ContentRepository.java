package com.healthcare.repository;

import com.healthcare.model.entity.Content;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>, JpaSpecificationExecutor<Content> {

    @Query("select c from Content c where c.agency.id = ?1")
    public Page<Content> findAllContentByAgencyId(Long agencyId, Pageable pageable);

    @Query("select c from Content c where c.company.id = ?1")
    public Page<Content> findAllContentByCompanyId(Long companyId, Pageable pageable);

    @Query("select c from Content c")
    public Page<Content> findAllContent(Pageable pageable);

    @Query("select c from Content c where c.agency.id = ?1 AND c.company.id = ?2")
    public Page<Content> findAllContentByCompanyAgencyId(Long agencyId, Long companyId, Pageable pageable);
    
    @Query("select c from Content c where c.agency.id = ?1 AND c.contentType.name = 'Agency Agreement'")
    public Content findContentTypeAgencyAgreementByAgencyId(Long agencyId);
    
    @Query("select c from Content c where c.agency.id = ?1 AND c.contentType.name = 'Announcement'")
    public Content findContentTypeAgencyAnnouncementByAgencyId(Long agencyId);
    
    @Query("select c from Content c where c.company.id = ?1 AND c.contentType.name = 'Announcement'")
    public List<Content> findAllContentAnnouncementsByCompanyId(Long companyId);

    @Query("select c from Content c where c.agency.id = ?1 AND c.company.id = ?2 AND c.contentType.name = 'Announcement'")
    public List<Content> findAllContentAnnouncementsByCompanyAgencyId(Long agencyId, Long companyId);
    
}