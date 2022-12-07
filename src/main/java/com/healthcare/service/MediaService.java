package com.healthcare.service;

import com.healthcare.model.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MediaService extends IService<Media>, IFinder<Media> {

    Media save(Media media);

    List<Media> findAll();

    public Page<Media> findAllMedia(Long agencyId, Long companyId, Pageable pageable);
}
