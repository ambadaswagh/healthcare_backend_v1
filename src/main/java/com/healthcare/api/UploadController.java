package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.dto.FileUploadDTO;
import com.healthcare.service.UploadService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/upload")
public class UploadController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UploadService uploadService;

	@ApiOperation(value = "upload document", notes = "upload a new document")
	@ApiParam(name = "documentRequest", value = "document to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/file")
	public ResponseEntity<FileUploadDTO> upload(@RequestParam("file") MultipartFile file) {
		String url = uploadService.uploadFileToS3(file);
		Long id = uploadService.saveDocument(file, url);
		FileUploadDTO dto = new FileUploadDTO();
		dto.setFilename(file.getOriginalFilename());
		dto.setId(id);
		if (url != null) {
			dto.setUrl(url);
			return new ResponseEntity<FileUploadDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FileUploadDTO>(dto, HttpStatus.BAD_REQUEST);
	}
}
