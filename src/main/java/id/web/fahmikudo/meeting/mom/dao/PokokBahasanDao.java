package id.web.fahmikudo.meeting.mom.dao;

import id.web.fahmikudo.meeting.mom.model.PokokBahasan;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;


public interface PokokBahasanDao extends PagingAndSortingRepository<PokokBahasan, String> {
    Optional<PokokBahasan> findById(String id);
    Optional<PokokBahasan> findByPokokBahasan(String pokokBahasan);
}
