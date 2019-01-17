package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.model.Gallery;
import id.web.fahmikudo.meeting.mom.repository.GalleryRepo;
import id.web.fahmikudo.meeting.mom.service.GalleryService;
import id.web.fahmikudo.meeting.mom.service.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private GalleryRepo galleryRepo;

    @GetMapping(value = "/list")
    public Page<Gallery> getList(Pageable pageable){
        return galleryRepo.findAll(pageable);
    }

    @PostMapping(value = "/upload")
    public UploadFileResponse uploadFile(@RequestParam("file")MultipartFile file){
        Gallery dbGallery = galleryService.store(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dbGallery.getId())
                .toUriString();

        return new UploadFileResponse(dbGallery.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        Gallery dbFile = galleryService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

}
