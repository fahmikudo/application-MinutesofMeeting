package id.web.fahmikudo.meeting.mom.dao;

import id.web.fahmikudo.meeting.mom.model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProjectDao extends PagingAndSortingRepository<Project, String>{
    Optional<Project> findByNamaProject(String nama);
}