package id.web.fahmikudo.meeting.mom.repository;

import id.web.fahmikudo.meeting.mom.model.DetailPokokBahasan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DetailPokokBahasanRepo extends PagingAndSortingRepository<DetailPokokBahasan, String> {

    Optional<DetailPokokBahasan> findById(String id);

}
