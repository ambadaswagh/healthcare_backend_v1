package com.healthcare.api;

import com.healthcare.model.entity.Media;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.MediaRepository;
import com.healthcare.service.MediaService;
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

import java.util.List;

@RestController
@RequestMapping(value = "/api/media")
public class MediaController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaRepository mediaRepository;

    @ApiOperation(value = "save media", notes = "save media")
    @ApiParam(name = "media", value = "media to save", required = true)
    @PostMapping()
    public ResponseEntity<Media> create(@RequestBody Media media) {
        media = mediaRepository.save(media);
        return new ResponseEntity<Media>(media, HttpStatus.OK);
    }

    @ApiOperation(value = "get media by id", notes = "get media by id")
    @ApiImplicitParam(name = "id", value = "media id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public Media read(@PathVariable Long id) {
        logger.info("id : " + id);
        return mediaRepository.findOne(id);
    }

    @ApiOperation(value = "get all media", notes = "get all media")
    @GetMapping()
    public ResponseEntity<Page<Media>> readAll(@RequestParam MultiValueMap<String, String> attributes,
                                               @RequestParam(value = "agencyId") Long agencyId,
                                               @RequestParam(value = "companyId") Long companyId) {
        MultiValueMapConverter<Media> converter = new MultiValueMapConverter<>(attributes, Media.class);
        return ResponseEntity.ok(mediaService.findAllMedia(agencyId, companyId, converter.getPageable()));
    }

    @ApiOperation(value = "delete media", notes = "delete media")
    @ApiImplicitParam(name = "id", value = "media id", required = true, dataType = "Long" ,paramType = "path")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        mediaService.deleteById(id);
    }

//    @GetMapping("/findAllMedia")
//    public List<Media> findAllMedia(@RequestParam(value = "agencyId") Long agencyId,
//                                                    @RequestParam(value = "companyId") Long companyId){
//        return mediaService.findAllMedia(agencyId, companyId);
//    }
}
