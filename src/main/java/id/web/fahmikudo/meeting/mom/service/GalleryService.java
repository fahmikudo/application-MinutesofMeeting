package id.web.fahmikudo.meeting.mom.service;

import id.web.fahmikudo.meeting.mom.model.Gallery;
import id.web.fahmikudo.meeting.mom.repository.GalleryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;



@Service
public class GalleryService {

    @Autowired
    private GalleryRepo galleryRepo;

    public Gallery store(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("...")){
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Gallery dbFile = new Gallery(fileName, file.getContentType(), file.getBytes());

            return galleryRepo.save(dbFile);
        } catch (Exception e){
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    public Gallery getFile(String fileId) {
        return galleryRepo.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
