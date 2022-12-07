package com.healthcare.service.impl;

import javax.transaction.Transactional;

import com.healthcare.notification.NotificationService;
import com.healthcare.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.healthcare.dto.AppointmentSearchDTO;
import com.healthcare.model.entity.Appointment;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.service.AppointmentService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Appointment service
 */
@Service
@Transactional
public class AppointmentServiceImpl extends BasicService<Appointment, AppointmentRepository>
		implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	NotificationService notificationService;

	@Override
	public Appointment save(Appointment entity) {
		if(entity.getId() == null) {
			entity.setCreatedAt(new Timestamp(new Date().getTime()));
		}
		else {
			entity.setUpdatedAt(new Timestamp(new Date().getTime()));
		}
		appointmentRepository.save(entity);
		return entity;
	}

	@Override
	public Appointment findById(Long id) {
		return appointmentRepository.findOne(id);
	}

	@Override
	public Long deleteById(Long id) {
		appointmentRepository.delete(id);
		return id;
	}

	@Override
	public Long disableById(Long id) {
		return null;
	}

	@Override
	public Page<Appointment> search(AppointmentSearchDTO dto, Pageable pageable) {
		return appointmentRepository.search(dto, pageable);
	}

	@Override
	public Page<Appointment> searchByCompany(Long companyId, AppointmentSearchDTO dto, Pageable pageable) {
		return appointmentRepository.searchByCompany(companyId, dto, pageable);
	}

	@Override
	public Page<Appointment> searchByAgency(Long agencyId, AppointmentSearchDTO dto, Pageable pageable) {
		return appointmentRepository.searchByAgency(agencyId, dto, pageable);
	}

	@Override
	public void doSendingReminder() {
		List<Appointment> appointmentList = appointmentRepository.findAll();
		if (appointmentList != null && appointmentList.size() > 0) {
			for (Appointment appointment: appointmentList) {
				// get type of notifcation
				Integer notificationType = appointment.getAlertType();
				// only sending active reminder
				if (1 == appointment.getStatus()) {
					Date scheduledTime = appointment.getAppointmentTime();
					DateTime currentTime = new DateTime();
					DateTime dateTime = null;
					if (scheduledTime != null) {
						dateTime = new DateTime(scheduledTime, DateTimeZone.UTC);
					}
					// 1 - on time
					// 2 - 10 min before
					// 3 - 30 min before
					// 4 - 1 hour before
					// 5 - 1 day before
					boolean isSending = false;
					if (notificationType != null && dateTime != null) {
						switch (notificationType) {
							// for on time
							case 1:
								break;
							case 2:
								// get time before 10 minutes
								currentTime = currentTime.plusMinutes(10);
								break;
							case 3:
								// get time before 30 minutes
								currentTime = currentTime.plusMinutes(30);
								break;
							case 4:
								// get time before 1 hour
								currentTime = currentTime.plusHours(1);
								break;
							case 5:
								// get time before 1 day
								// get time before 10 minutes
								currentTime = currentTime.plusDays(1);
								break;
							default:
								isSending = false;
								break;
						}

						if (currentTime.getYear() == dateTime.getYear()
								&& currentTime.getMonthOfYear() == dateTime.getMonthOfYear()
								&& currentTime.getDayOfWeek() == dateTime.getDayOfWeek()
								&& currentTime.getHourOfDay() == dateTime.getHourOfDay()
								&& currentTime.getMinuteOfHour() == dateTime.getMinuteOfHour()) {
							isSending = true;
						}

						// sending
						if (isSending) {
							Integer needNotification = appointment.getNeedNotification();
							if (needNotification != null && needNotification > 0) {
								//  {id: 1, label: 'SMS'},
								if (needNotification == 1) {
									// send to user
									if (appointment.getUser() != null) {
										// sending email or sms
										if (appointment.getUser().getPhone() != null && appointment.getUser().getPhone().length() > 0) {
											notificationService.sendSMS(appointment.getUser().getPhone(), appointment.getReason());
										}
									}
								}

								// email
								if (needNotification == 2) {
									// send to user
									if (appointment.getUser() != null) {
										// sending email or sms
										if (appointment.getUser().getEmail() != null && appointment.getUser().getEmail().length() > 0) {
											notificationService.sendEmail(appointment.getUser().getEmail(), appointment.getReason(), appointment.getComment());
										}
									}
								}
								// both
								if (needNotification == 3) {
									if (appointment.getUser() != null) {
										// sending email or sms
										if (appointment.getUser().getPhone() != null && appointment.getUser().getPhone().length() > 0) {
											notificationService.sendSMS(appointment.getUser().getPhone(), appointment.getReason());
										}

										// sending email or sms
										if (appointment.getUser().getEmail() != null && appointment.getUser().getEmail().length() > 0) {
											notificationService.sendEmail(appointment.getUser().getEmail(), appointment.getReason(), appointment.getComment());
										}
									}
								}
							}
						}
					}

				}
			}
		}
	}
}
