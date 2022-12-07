package com.healthcare.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.api.model.DocumentRequest;
import com.healthcare.model.entity.Document;
import com.healthcare.model.enums.DocumentStatusEnum;
import com.healthcare.service.DocumentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/document")
public class DocumentController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/*@Autowired
	private MultipartConfigElement multipartConfigElement;*/

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private S3Wrapper s3Wrapper;

	@ApiOperation(value = "upload document", notes = "upload a new document")
	@ApiParam(name = "documentRequest", value = "document to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	@PostMapping
	public ResponseEntity<Document> upload(@RequestParam("file") MultipartFile file, DocumentRequest documentRequest) {
		logger.info("upload document");
		Document document = new Document();
		if ((file != null && file.isEmpty()) || StringUtils.isBlank(documentRequest.getEntity())
				|| (documentRequest.getEntityId() == null || documentRequest.getEntityId() == 0)
				|| StringUtils.isBlank(documentRequest.getFileClass())) {
			return new ResponseEntity<Document>(document, HttpStatus.BAD_REQUEST);
		}

		try {
			// Get attributes
			document.setEntity(documentRequest.getEntity());
			document.setEntityId(documentRequest.getEntityId());
			document.setFileClass(documentRequest.getFileClass());
			document.setStatus(DocumentStatusEnum.ACTIVE.getValue());
			
			prepareFile(file, document);

		} catch (Exception e) {
			logger.error("Error in loading file", e);
			return new ResponseEntity<Document>(document, HttpStatus.BAD_REQUEST);
		}
		document = documentService.save(document);
		return new ResponseEntity<Document>(document, HttpStatus.OK);
	}

	private void prepareFile(MultipartFile file, Document document) throws UnsupportedEncodingException, IOException {
		String url;
		String uploadKey;
		String fileName;
		fileName = file.getOriginalFilename();
		/*Matcher match = Pattern.compile("[^a-zA-Z0-9]").matcher(fileName.toLowerCase());
		while (match.find()) {
			fileName = fileName.replaceAll("\\" + match.group(), "");
		}*/
		uploadKey = UUID.randomUUID() + "_" + URLEncoder.encode(fileName, "UTF-8");
		s3Wrapper.upload(file.getInputStream(), uploadKey);
		url = uploadKey;
		
		document.setFilePath(url);
		document.setFileUrl(s3Wrapper.getS3URL()+url);
		document.setFileName(file.getOriginalFilename());
		document.setFileSize(file.getSize());

		String[] fileFrags = file.getOriginalFilename().split("\\.");
		String extension = fileFrags[fileFrags.length - 1];
		document.setFileType(extension);
	}

	@ApiOperation(value = "get document by Id", notes = "get document detail by id")
	@ApiImplicitParam(name = "id", value = "document Id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping(value = "/{id}")
	public Document read(@PathVariable Long id) {
		return documentService.findById(id);
	}

	@ApiOperation(value = "s3 URL", notes = "s3 URL")
	@GetMapping("/s3Url")
	public URL getS3URL() {
		return s3Wrapper.getS3URL();
	}
	
	@ApiOperation(value = "update document", notes = "update a document")
	@ApiParam(name = "documentRequest", value = "document to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	@PutMapping
	public ResponseEntity<Document> update(@RequestParam("file") MultipartFile file, DocumentRequest documentRequest) {
		Document document = documentService.findById(documentRequest.getId());

		if (file != null && file.isEmpty()) {
			return new ResponseEntity<Document>(document, HttpStatus.BAD_REQUEST);
		}
		try {
			if (StringUtils.isNotBlank(documentRequest.getEntity())) {
				document.setEntity(documentRequest.getEntity());
			}
			if (documentRequest.getEntityId() != null && documentRequest.getEntityId() > 0) {
				document.setEntityId(documentRequest.getEntityId());
			}
			if (StringUtils.isNotBlank(documentRequest.getFileClass())) {
				document.setFileClass(documentRequest.getFileClass());
			}

			// Store and get values from file
			if (file != null) {
				prepareFile(file, document);
			}
		} catch (IOException e) {
			logger.error("Error in loading file", e);
			return new ResponseEntity<Document>(document, HttpStatus.BAD_REQUEST);
		}
		document = documentService.save(document);
		return new ResponseEntity<Document>(document, HttpStatus.OK);
	}

	@ApiOperation(value = "delete document", notes = "delete a document")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "document Id", required = true, dataType = "Long" ,paramType = "path")
	,@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	})
	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable Long id) {
		Document document = documentService.findById(id);
		s3Wrapper.delete(document.getFilePath());
		documentService.deleteById(id);
	}

}
