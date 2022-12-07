package com.healthcare.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.service.UploadService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/download")
public class DownloadController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private S3Wrapper s3Wrapper;

	@ApiOperation(value = "Download a file", notes = "Download a file")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "file", value = "file", required = true, dataType = "String", paramType = "path") })
	@GetMapping("/{file}")
	public ResponseEntity<byte[]> download(@PathVariable("file") String file) {
		try {
			return s3Wrapper.download(file);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
	}

}
