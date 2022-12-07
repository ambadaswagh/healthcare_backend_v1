package com.healthcare.api;

import com.healthcare.model.entity.Notification;
import com.healthcare.service.NotificationSettingService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/notification_setting")
public class NotificationSettingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotificationSettingService notificationSettingService;

    @ApiOperation(value = "save notification", notes = "save notification")
    @ApiParam(name = "notification", value = "notification to save", required = true)
    @PostMapping()
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        notification = notificationSettingService.save(notification);
        return new ResponseEntity<Notification>(notification, HttpStatus.OK);
    }

    @ApiOperation(value = "get notification by id", notes = "get notification by id")
    @ApiImplicitParam(name = "id", value = "notification id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public Notification read(@PathVariable Long id) {
        logger.info("id : " + id);
        return notificationSettingService.findById(id);
    }

    @ApiOperation(value = "get all notification", notes = "get all notification")
    @GetMapping()
    public List<Notification> readAll() {
        return notificationSettingService.findAll();
    }

    @ApiOperation(value = "update notification", notes = "update notification")
    @ApiParam(name = "notification", value = "notification to update", required = true)
    @PutMapping()
    public ResponseEntity<Notification> update(@RequestBody Notification notification) {
        notification = notificationSettingService.save(notification);
        return new ResponseEntity<Notification>(notification, HttpStatus.OK);
    }

    @ApiOperation(value = "get notification by agencyId", notes = "get notification by agencyId")
    @ApiImplicitParam(name = "agencyId", value = "agencyId", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/agency/{agencyId}")
    public Notification getByAgency(@PathVariable Long agencyId) {
        logger.info("agencyId : " + agencyId);
        return notificationSettingService.findByAgency(agencyId);
    }
}
