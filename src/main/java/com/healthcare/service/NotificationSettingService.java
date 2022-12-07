package com.healthcare.service;

import com.healthcare.model.entity.Notification;

import java.util.List;

public interface NotificationSettingService extends IService<Notification>, IFinder<Notification> {

    Notification save(Notification notification);

    List<Notification> findAll();

    Notification findByAgency(Long agencyId);

    void doSending();

}
