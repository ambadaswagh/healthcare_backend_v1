package com.healthcare.api;

import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.api.common.RoleUtil;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Content;
import com.healthcare.model.entity.ContentType;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.ContentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;

/**
 * Created by Hitesh on 07/06/17.
 * 
 * Updated By Adonay: deleted some columns as requested by ticket #657 BE
 */
@CrossOrigin
@RestController(value = "ContentRestAPI")
@RequestMapping(value = "/api/content")
public class ContentController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContentService contentService;

    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @ApiOperation(value = "save content", notes = "save content")
    @ApiParam(name = "content", value = "content to save", required = true)
    @PostMapping()
    public ResponseEntity create(@RequestBody Content content) {
    	/*
        if (checkCyle(content)) {
            return ResponseEntity.status(HttpStatus.LOOP_DETECTED).body("Updating this parent will cause cycle");
        } */

        content = contentService.save(content);
        return new ResponseEntity<Content>(content, HttpStatus.OK);
    }

    @ApiOperation(value = "get content by id", notes = "get content by id")
    @ApiImplicitParam(name = "id", value = "content id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{id}")
    public Content read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        return contentService.findById(id);
    }
    
    @ApiOperation(value = "get content by type agency agreement and agency id", notes = "get content by type agency agreement and agency id")
    @ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/type/agencyagreement/agency/{agencyId}")
    public Content getContentAgreementTypeByAgency(@PathVariable("agencyId") Long agencyId) {
        return contentService.getContentTypeAgencyAgreementByAgencyId(agencyId);
    }
    
    @ApiOperation(value = "get content by type agency agreement and agency id", notes = "get content by type agency agreement and agency id")
    @ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/type/announcement/agency/{agencyId}")
    public Content getContentAnnouncementTypeByAgency(@PathVariable("agencyId") Long agencyId) {
        return contentService.getContentTypeAgencyAnnouncementByAgencyId(agencyId);
    }

    @ApiOperation(value = "get all content", notes = "get all content")
    @GetMapping()
    public ResponseEntity<Page<Content>> readAll(HttpServletRequest req,
                                                 @RequestParam MultiValueMap<String, String> attributes) {

        MultiValueMapConverter<Content> converter = new MultiValueMapConverter<>(attributes, Content.class);

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }

        if (RoleUtil.isCompanyAdmin(admin)) {

            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                return ResponseEntity.ok(contentService.findAllContentByCompanyId(adminAgencyCompanyOrganization.getCompany().getId(),
                        converter.getPageable()));
            }
        }

        if (RoleUtil.isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return ResponseEntity.ok(contentService.findAllContentByCompanyAgencyId(
                        adminAgencyCompanyOrganization.getAgency().getId(),
                        adminAgencyCompanyOrganization.getCompany().getId(), converter.getPageable()));
            }
        }

        return ResponseEntity.ok(contentService.findAllContent(converter.getPageable()));
    }
    
    @ApiOperation(value = "get all content announcements", notes = "get all content announcements")
    @GetMapping("/announcements")
    public ResponseEntity<List<Content>> get_all_announcements(HttpServletRequest req) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }

        if (RoleUtil.isCompanyAdmin(admin)) {
        	AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
            	return new ResponseEntity<List<Content>>(contentService.findAllContentAnnouncementsByCompanyId(adminAgencyCompanyOrganization.getCompany().getId()), HttpStatus.OK);
            }
        }

        if (RoleUtil.isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return new ResponseEntity<List<Content>>(contentService.findAllContentAnnouncementsByCompanyAgencyId(adminAgencyCompanyOrganization.getAgency().getId(), adminAgencyCompanyOrganization.getCompany().getId()), HttpStatus.OK);
            }
        }

        return new ResponseEntity<List<Content>>(new ArrayList<>(),HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "update content", notes = "update content")
    @ApiParam(name = "content", value = "content to update", required = true)
    @PutMapping()
    public ResponseEntity update(@RequestBody Content content) {
    	/*
        if (checkCyle(content)) {
            return ResponseEntity.status(HttpStatus.LOOP_DETECTED).body("Updating this parent will cause cycle");
        }*/
        content = contentService.save(content);
        return new ResponseEntity<Content>(content, HttpStatus.OK);
    }
/*
    private boolean checkCyle(Content firstContent) {
    	
        Content curParent = firstContent.getParent();

        // If try to nullify parent
        if (curParent == null) {
            return false;
        }

        // Check If content id and parent id are not same
        if (curParent != null && curParent.getId() != null) {
            if (firstContent.getId().equals(curParent.getId())) {
                return true;
            }
        }

        // Get parent details from db.
        Content curParentDetails = findParent(curParent);
        if (curParentDetails == null) {
            return false;
        }

        // Check for cycle
        while (curParentDetails != null) {
            Content newParent = curParentDetails.getParent();
            if (newParent == null) {
                break;
            }

            if (firstContent.getId().equals(newParent.getId())) {
                return true;
            }

            curParentDetails = newParent;
        }

        return false;
    }

    public Content findParent(Content content) {
        Content contentDetail = contentService.findById(content.getId());
        if (contentDetail != null && (contentDetail.getParent() == null || contentDetail.getParent().getId() == null))
            return null;
        else
            return contentDetail;
    }
*/
    @ApiOperation(value = "delete content", notes = "delete content")
    @ApiImplicitParam(name = "id", value = "content id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        contentService.deleteById(id);
    }

    @ApiOperation(value = "get all training by company id", notes = "get all training by company id")
    @ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<Content>> readAllByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
        MultiValueMapConverter<Content> converter = new MultiValueMapConverter<>(attributes, Content.class);
        return ResponseEntity.ok(contentService.findAllContentByCompanyId(companyId, converter.getPageable()));
    }

    @ApiOperation(value = "get all content by agency id", notes = "get all content by agency id")
    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<Page<Content>> readAllByAgency(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("agencyId") Long agencyId) {
        MultiValueMapConverter<Content> converter = new MultiValueMapConverter<>(attributes, Content.class);
        return ResponseEntity.ok(contentService.findAllContentByAgencyId(agencyId, converter.getPageable()));
    }

    @ApiOperation(value = "get all content by company and agency id", notes = "get all content by company and agency id")
    @GetMapping("/company/{companyId}/agency/{agencyId}")
    public ResponseEntity<Page<Content>> readAll(@RequestParam MultiValueMap<String, String> attributes,
                                                 @PathVariable("companyId") Long companyId, @PathVariable("agencyId") Long agencyId) {
        MultiValueMapConverter<Content> converter = new MultiValueMapConverter<>(attributes, Content.class);
        return ResponseEntity.ok(contentService.findAllContentByCompanyAgencyId(agencyId, companyId, converter.getPageable()));
    }

    @GetMapping("get_content_type_list")
    public ResponseEntity<List<ContentType>> getListContentType(HttpServletRequest request) {
        return ResponseEntity.ok(contentService.getAllContentType());
    }
    @GetMapping("reset_content_type/{id}")
    public ResponseEntity<Content> resetContentType(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.resetContentType(id));
    }
}
