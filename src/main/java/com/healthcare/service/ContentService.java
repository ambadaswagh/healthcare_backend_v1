package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.healthcare.model.entity.Content;

/**
 * Created by Hitesh on 07/06/17.
 */
public interface ContentService extends IService<Content>, IFinder<Content> {

    Content save(Content content);

    List<Content> findAll();

    Page<Content> findAllContent(Pageable pageable);

    Page<Content> findAllContentByCompanyId(Long companyId, Pageable pageable);

    Page<Content> findAllContentByAgencyId(Long agencyId, Pageable pageable);

    Page<Content> findAllContentByCompanyAgencyId(Long agencyId, Long companyId, Pageable pageable);

    List<ContentType> getAllContentType();

    Content resetContentType(Long id);
    
    Content getContentTypeAgencyAgreementByAgencyId(Long agencyId);
    Content getContentTypeAgencyAnnouncementByAgencyId(Long agencyId);
    
    List<Content> findAllContentAnnouncementsByCompanyId(Long companyId);

    List<Content> findAllContentAnnouncementsByCompanyAgencyId(Long agencyId, Long companyId);
    
}
