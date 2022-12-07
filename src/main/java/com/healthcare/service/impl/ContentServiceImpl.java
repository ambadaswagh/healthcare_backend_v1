package com.healthcare.service.impl;

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.Content;
import com.healthcare.model.entity.ContentType;
import com.healthcare.repository.AgencyRepository;
import com.healthcare.repository.ContentRepository;
import com.healthcare.repository.ContentTypeRepository;
import com.healthcare.service.ContentService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl extends BasicService<Content, ContentRepository> implements ContentService {
    private static final String KEY = Content.class.getSimpleName();

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    ContentTypeRepository contentTypeRepository;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    private RedisTemplate<String, Content> contentRedisTemplate;

    @Override
    @Transactional
    public Content save(Content content) {
        content = contentRepository.save(content);
        contentRedisTemplate.opsForHash().put(KEY, content.getId(), content);
        return content;
    }

    @Override
    @Transactional
    public Content findById(Long id) {
        Content content = (Content) contentRedisTemplate.opsForHash().get(KEY, id);
        if (content == null)
            content = contentRepository.findOne(id);
        return content;
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        contentRepository.delete(id);
        return contentRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    @Transactional
    public List<Content> findAll() {
        Map<Object, Object> homeVisitMap = contentRedisTemplate.opsForHash().entries(KEY);
        List<Content> homeVisitList = Collections.arrayToList(homeVisitMap.values().toArray());
        if (homeVisitMap.isEmpty())
            homeVisitList = contentRepository.findAll();
        return homeVisitList;
    }

    @Override
    @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    @Override
    public Page<Content> findAllContent(Pageable pageable) {
        return contentRepository.findAllContent(pageable);
    }

    @Override
    public Page<Content> findAllContentByCompanyId(Long companyId, Pageable pageable) {
        return contentRepository.findAllContentByCompanyId(companyId, pageable);
    }

    @Override
    public Page<Content> findAllContentByAgencyId(Long agencyId, Pageable pageable) {
        return contentRepository.findAllContentByAgencyId(agencyId, pageable);
    }

    @Override
    public Page<Content> findAllContentByCompanyAgencyId(Long agencyId, Long companyId, Pageable pageable) {
    	
        return contentRepository.findAllContentByCompanyAgencyId(agencyId, companyId, pageable);
    }

    @Override
    public List<ContentType> getAllContentType() {
        List<ContentType> rs = contentTypeRepository.findAll();
        return rs;
    }

    @Override
    @Transactional
    public Content resetContentType(Long id) {
        Content content = contentRepository.findOne(id);
        if(content == null) return null;
        Agency agency = agencyRepository.findOne(content.getAgency().getId());
        agency.setAgreementSignatureId(null);
        agencyRepository.save(agency);
        contentRepository.delete(id);
        contentRedisTemplate.opsForHash().put(KEY, content.getId(), content);
        return content;
    }

	@Override
	public Content getContentTypeAgencyAgreementByAgencyId(Long agencyId) {
		return contentRepository.findContentTypeAgencyAgreementByAgencyId(agencyId);
	}

	@Override
	public Content getContentTypeAgencyAnnouncementByAgencyId(Long agencyId) {
		return contentRepository.findContentTypeAgencyAnnouncementByAgencyId(agencyId);
	}

	@Override
	public List<Content> findAllContentAnnouncementsByCompanyId(Long companyId) {
		// TODO Auto-generated method stub
		return contentRepository.findAllContentAnnouncementsByCompanyId(companyId);
	}

	@Override
	public List<Content> findAllContentAnnouncementsByCompanyAgencyId(Long agencyId, Long companyId) {
		// TODO Auto-generated method stub
		return contentRepository.findAllContentAnnouncementsByCompanyAgencyId(agencyId, companyId);
	}

}
