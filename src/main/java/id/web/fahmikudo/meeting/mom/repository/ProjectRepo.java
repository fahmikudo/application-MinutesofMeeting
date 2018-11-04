package id.web.fahmikudo.meeting.mom.repository;

import id.web.fahmikudo.meeting.mom.model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProjectRepo extends PagingAndSortingRepository<Project, String>{
    Optional<Project> findByNamaProject(String nama);
}