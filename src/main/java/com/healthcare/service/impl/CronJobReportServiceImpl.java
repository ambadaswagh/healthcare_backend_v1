package com.healthcare.service.impl;

import com.healthcare.model.entity.CronJobReport;
import com.healthcare.repository.CronJobReportRepository;
import com.healthcare.service.CronJobReportService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CronJobReportServiceImpl implements CronJobReportService {
	private static final String KEY = CronJobReportServiceImpl.class.getSimpleName();

	@Autowired
    CronJobReportRepository cronJobReportRepository;

	@Autowired
	private RedisTemplate<String, CronJobReport> cronJobReportRedisTemplate;

	@Override @Transactional
	public CronJobReport save(CronJobReport cronJobReport) {
		cronJobReport = cronJobReportRepository.save(cronJobReport);
		cronJobReportRedisTemplate.opsForHash().put(KEY, cronJobReport.getId(), cronJobReport);
		return cronJobReport;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		cronJobReportRepository.delete(id);
		return cronJobReportRedisTemplate.opsForHash().delete(KEY, id);
	}

    @Override
    public Long disableById(Long id) {
        return null;
    }

    @Override @Transactional
	public CronJobReport findById(Long id) {
		if (cronJobReportRedisTemplate.opsForHash().hasKey(KEY, id))
			return (CronJobReport) cronJobReportRedisTemplate.opsForHash().get(KEY, id);
		return cronJobReportRepository.findOne(id);
	}

	@Override
	@Transactional
	public List<CronJobReport> findAll() {
		Map<Object, Object> cronJobReportMap = cronJobReportRedisTemplate.opsForHash().entries(KEY);
		List<CronJobReport> cronJobReportList = Collections.arrayToList(cronJobReportMap.values().toArray());
		if (cronJobReportMap.isEmpty())
			cronJobReportList = cronJobReportRepository.findAll();
		return cronJobReportList;
	}


}
