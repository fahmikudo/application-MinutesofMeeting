package id.web.fahmikudo.meeting.mom.repository;

import id.web.fahmikudo.meeting.mom.model.DaftarPeserta;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DaftarPesertaRepo extends PagingAndSortingRepository<DaftarPeserta, String> {

    Optional<DaftarPeserta> findById(String id);
}
