package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Document;
import com.healthcare.model.entity.User;
import com.healthcare.repository.DocumentRepository;
import com.healthcare.service.DocumentService;

@Service
@Transactional
public class DocumentServiceImpl  extends BasicService<Document, DocumentRepository> implements DocumentService {
	private static final String KEY = Document.class.getSimpleName();

	@Autowired
	public DocumentRepository documentRepository;

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@Override @Transactional
	public Document save(Document document) {
		if (document.getId() == null) {
			document.setCreatedAt(new Timestamp(new Date().getTime()));
		} else {
			document.setUpdatedAt(new Timestamp(new Date().getTime()));
		}
		document = documentRepository.save(document);
		redisTemplate.opsForHash().put(KEY, document.getId(), document);
		return document;
	}

	@Override @Transactional
	public Document findById(Long id) {
		if (redisTemplate.opsForHash().hasKey(KEY, id)) {
			return (Document) redisTemplate.opsForHash().get(KEY, id);
		}
		return documentRepository.findOne(id);
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		documentRepository.delete(id);
		return redisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
