package com.healthcare.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.api.S3Wrapper;
import com.healthcare.model.entity.Document;
import com.healthcare.model.enums.DocumentStatusEnum;
import com.healthcare.service.DocumentService;
import com.healthcare.service.UploadService;

/**
 * Upload service
 */
@Service
@Transactional
public class UploadServiceImpl implements UploadService {

	@Autowired
	private S3Wrapper s3Wrapper;
	private static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
	@Autowired
	private DocumentService documentService;

	public String uploadFileToS3(MultipartFile file) {
		if (file == null) {
			logger.info("1");
			return null;
		}
		try {
			String uploadKey = UUID.randomUUID().toString();
			s3Wrapper.upload(file.getInputStream(), uploadKey);
			return uploadKey;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Upload file error: {}", e);
		}
		return null;
	}

	public String uploadFileToS3(File file) {
		if (file == null) {
			return null;
		}
		try {
			String uploadKey = UUID.randomUUID().toString();
			s3Wrapper.upload(new FileInputStream(file), uploadKey);
			return uploadKey;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Long saveDocument(MultipartFile file, String url) {
		Document document = new Document();

		document.setEntity("FILE");
		document.setEntityId(1L);
		document.setFileClass("");
		document.setStatus(DocumentStatusEnum.ACTIVE.getValue());

		document.setFilePath(url);
		document.setFileUrl(s3Wrapper.getS3URL() + url);
		document.setFileName(file.getOriginalFilename());
		document.setFileSize(file.getSize());

		String[] fileFrags = file.getOriginalFilename().split("\\.");
		String extension = fileFrags[fileFrags.length - 1];
		document.setFileType(extension);
		document = documentService.save(document);
		return document.getId();
	}

}
