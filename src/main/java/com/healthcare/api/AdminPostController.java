package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.AdminPost;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminPostService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/adminpost")
public class AdminPostController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AdminPostService adminPostService;

	@ApiOperation(value = "save admin post", notes = "save admin post")
	@ApiParam(name = "adminPost", value = "admin post to save", required = true)
	@PostMapping()
	public ResponseEntity<AdminPost> create(@RequestBody AdminPost adminPost) {
		adminPost = adminPostService.save(adminPost);
		return new ResponseEntity<AdminPost>(adminPost, HttpStatus.OK);
	}

	@ApiOperation(value = "get admin post by id", notes = "get admin post by id")
	@ApiImplicitParam(name = "id", value = "admin post id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public AdminPost read(@PathVariable Long id) {
		logger.info("id : " + id);
		return adminPostService.findById(id);
	}

	@ApiOperation(value = "get all admin post", notes = "get all admin post")
	@GetMapping()
	public ResponseEntity<Page<AdminPost>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<AdminPost> converter = new MultiValueMapConverter<>(attributes, AdminPost.class);
		return ResponseEntity.ok(adminPostService.findAll(converter.getData(), converter.getPageable()));
	}


	@ApiOperation(value = "update admin post", notes = "update admin post")
	@ApiParam(name = "adminPost", value = "admin post to update", required = true)
	@PutMapping()
	public ResponseEntity<AdminPost> update(@RequestBody AdminPost adminPost) {
		adminPost = adminPostService.save(adminPost);
		return new ResponseEntity<AdminPost>(adminPost, HttpStatus.OK);
	}

	@ApiOperation(value = "delete admin post", notes = "delete admin post")
	@ApiImplicitParam(name = "id", value = "admin post id", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		adminPostService.deleteById(id);
	}
}
