package com.healthcare.service.impl;
import com.healthcare.model.entity.User;
import com.healthcare.model.enums.StatusEnum;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.CronJobAuthorizationExpiringService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CronJobAuthorizationExpiringServiceImpl implements CronJobAuthorizationExpiringService {

    private static final String KEY = CronJobAuthorizationExpiringServiceImpl.class.getSimpleName();

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, User> cronJobUserRedisTemplate;

    @Override @Transactional
    public User save(User user) {
        user = userRepository.save(user);
        cronJobUserRedisTemplate.opsForHash().put(KEY, user.getId(), user);
        return user;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        userRepository.delete(id);
        return cronJobUserRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    public Long disableById(Long id) {
        return null;
    }

    @Override @Transactional
    public User findById(Long id) {
        if (cronJobUserRedisTemplate.opsForHash().hasKey(KEY, id))
            return (User) cronJobUserRedisTemplate.opsForHash().get(KEY, id);
        return userRepository.findOne(id);
    }

    @Override @Transactional
    public void updateAuthorizationExpiring() {
        List<User> expiredUsers = userRepository.getAuthorizationExpired();
        if (expiredUsers != null && expiredUsers.size() > 0) {
            for (User user : expiredUsers) {
                user.setStatus(StatusEnum.INACTIVE);
                userRepository.save(user);
            }
        }
    }
}
