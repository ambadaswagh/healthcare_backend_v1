package com.healthcare.service.impl;

import com.healthcare.model.entity.Media;
import com.healthcare.model.entity.RideLine;
import com.healthcare.repository.MediaRepository;
import com.healthcare.service.MediaService;
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
@Transactional
public class MediaServiceImpl extends BasicService<Media, MediaRepository> implements MediaService{

    private static final String KEY = RideLine.class.getSimpleName();

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private RedisTemplate<String, Media> mediaRedisTemplate;

    @Override @Transactional
    public Media save(Media media) {
        media = mediaRepository.save(media);
        mediaRedisTemplate.opsForHash().put(KEY, media.getId(), media);
        return media;
    }

    @Override @Transactional
    public Media findById(Long id) {
        Media media = (Media) mediaRedisTemplate.opsForHash().get(KEY, id);
        if (media == null)
            media = mediaRepository.findOne(id);
        return media;
    }

    @Override @Transactional
    public List<Media> findAll() {
        Map<Object, Object> mediaMap = mediaRedisTemplate.opsForHash().entries(KEY);
        List<Media> mediaList = Collections.arrayToList(mediaMap.values().toArray());
        if (mediaMap.isEmpty())
            mediaList = mediaRepository.findAll();
        return mediaList;
    }
    @Override @Transactional
    public Long deleteById(Long id) {
        mediaRepository.delete(id);
        return mediaRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    public Page<Media> findAllMedia(Long agencyId, Long companyId, Pageable pageable) {
        pageable = checkPageable(pageable);
        Page<Media> media = mediaRepository.findAllMedia(agencyId, companyId, pageable);
        return media;
    }
}
