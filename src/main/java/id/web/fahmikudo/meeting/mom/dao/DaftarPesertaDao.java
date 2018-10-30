package id.web.fahmikudo.meeting.mom.dao;

import id.web.fahmikudo.meeting.mom.model.DaftarPeserta;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DaftarPesertaDao extends PagingAndSortingRepository<DaftarPeserta, String> {

    Optional<DaftarPeserta> findById(String id);
}
