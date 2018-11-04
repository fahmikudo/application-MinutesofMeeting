package id.web.fahmikudo.meeting.mom.dao;

import id.web.fahmikudo.meeting.mom.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserDao extends PagingAndSortingRepository<User, String> {
    Optional<User> findById(String id);
    Optional<User> findByNamaLengkap(String nama);
    Optional<User> findByUsername(String username);
}