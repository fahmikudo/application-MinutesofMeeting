package id.web.fahmikudo.meeting.mom.repository;

import id.web.fahmikudo.meeting.mom.model.PokokBahasan;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;


public interface PokokBahasanRepo extends PagingAndSortingRepository<PokokBahasan, String> {
    Optional<PokokBahasan> findById(String id);
}
