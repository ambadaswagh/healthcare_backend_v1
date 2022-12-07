package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.AdminPost;

public interface AdminPostService extends IService<AdminPost>, IFinder<AdminPost> {
	List<AdminPost> findAll();
}
