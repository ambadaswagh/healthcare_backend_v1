package com.healthcare.service;

import com.healthcare.model.entity.User;

public interface CronJobAuthorizationExpiringService extends IService<User> {
    void updateAuthorizationExpiring();
}
