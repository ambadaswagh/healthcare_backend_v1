package com.healthcare.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	public String uploadFileToS3(MultipartFile file);
	public String uploadFileToS3(File file);
	public Long saveDocument(MultipartFile file, String url);
}
