package id.web.fahmikudo.meeting.mom.repository;

import id.web.fahmikudo.meeting.mom.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepo extends PagingAndSortingRepository<Gallery, String>, JpaRepository<Gallery, String> {

}
