package com.healthcare.service.impl;

import com.healthcare.model.entity.Notification;
import com.healthcare.notification.NotificationService;
import com.healthcare.repository.NotificationRepository;
import com.healthcare.service.EmailSenderService;
import com.healthcare.service.NotificationSettingService;
import com.healthcare.service.SmsSenderService;
import com.healthcare.util.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NotificationSettingServiceImpl extends BasicService<Notification, NotificationRepository> implements NotificationSettingService {

    private static final String KEY = Notification.class.getSimpleName();


    @Autowired
    private RedisTemplate<String, Notification> mediaRedisTemplate;

    @Autowired
    private NotificationRepository notificationRepository;


    @Autowired
    NotificationService notificationService;

    @Override @Transactional
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        notificationRepository.delete(id);
        return mediaRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    @Override
    public Notification save(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        return savedNotification;
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findOne(id);
    }

    @Override
    public Notification findByAgency(Long agencyId) {
        return notificationRepository.findByAgencyId(agencyId);
    }

    @Override
    public void doSending() {
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications != null && notifications.size() > 0) {
            for(Notification notification: notifications) {
                // get time/
                // get frequency
                Integer frequency = notification.getFrequency();
                Timestamp timestamp = notification.getNotificationTime();

                //0 for 'everyday', 1,2,3,4,5,6,7 for 'specific week days', 8 for 'notification based on changes'
                if (frequency != null) {
                    // for everyday
                    DateTime currentTime = new DateTime();
                    DateTime dateTime = null;
                    if (timestamp != null) {
                        dateTime = new DateTime(DateUtils.timestampToDate(timestamp));
                    }
                    // for day of week
                    boolean isSending = false;
                    if (frequency == 8) {
                        isSending = true;
                    } else if (frequency >= 1 && frequency < 8) {
                        if (currentTime.getDayOfWeek() == frequency && currentTime.getHourOfDay() == dateTime.getHourOfDay()
                                && currentTime.getMinuteOfHour() == dateTime.getMinuteOfHour()) {
                            isSending = true;
                        }
                    } else if (frequency == 0) {
                        if (currentTime.getHourOfDay() == dateTime.getHourOfDay()
                                && currentTime.getMinuteOfHour() == dateTime.getMinuteOfHour()) {
                            isSending = true;
                        }
                    }

                    if (isSending) {
                        // sending email or sms
                        if (notification.getEmail() != null && notification.getEmail().length() > 0) {
                            String[] splitted = notification.getEmail().split(",");
                            for (int i = 0; i < splitted.length; i++) {
                                // sending email
                                notificationService.sendEmail(splitted[i], "Reminder", "Please check reminder for driver, vihicles..");
                            }
                        }

                        if (notification.getSms() != null && notification.getSms().length() > 0) {
                            String[] splitted = notification.getSms().split(",");
                            for (int i = 0; i < splitted.length; i++) {
                                // sending message
                                notificationService.sendSMS(splitted[i], "Please check reminder for driver, vihicles..");
                            }
                        }
                    }
                }

            }
        }
    }

}
