package com.healthcare.api;

import com.healthcare.model.entity.AdminSetting;
import com.healthcare.repository.*;
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
@RequestMapping(value = "/api/adminsetting")
public class AdminSettingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminSettingRepository adminSettingRepository;

    @ApiOperation(value = "save adminSetting", notes = "save adminSetting")
    @ApiParam(name = "adminSetting", value = "adminSetting to save", required = true)
    @PostMapping()
    public ResponseEntity<AdminSetting> create(@RequestBody AdminSetting adminSetting) {
        adminSetting = adminSettingRepository.save(adminSetting);
        return new ResponseEntity<AdminSetting>(adminSetting, HttpStatus.OK);
    }

    @ApiOperation(value = "get adminSetting by id", notes = "get adminSetting by id")
    @ApiImplicitParam(name = "id", value = "adminSetting id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public AdminSetting read(@PathVariable Long id) {
        AdminSetting adminSetting = null;
        List<AdminSetting> list = adminSettingRepository.findByAdminId(id);
        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }
}
