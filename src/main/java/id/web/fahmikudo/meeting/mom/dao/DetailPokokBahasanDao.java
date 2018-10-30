package id.web.fahmikudo.meeting.mom.dao;

import id.web.fahmikudo.meeting.mom.model.DetailPokokBahasan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DetailPokokBahasanDao extends PagingAndSortingRepository<DetailPokokBahasan, String> {

    Optional<DetailPokokBahasan> findById(String id);

}
