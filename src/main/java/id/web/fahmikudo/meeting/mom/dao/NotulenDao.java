package id.web.fahmikudo.meeting.mom.dao;

import id.web.fahmikudo.meeting.mom.model.Notulen;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface NotulenDao extends PagingAndSortingRepository<Notulen, String> {
    Optional<Notulen> findById(String id);
    Optional<Notulen> findByNama(String nama);
}