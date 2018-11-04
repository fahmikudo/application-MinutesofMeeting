package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.repository.ProjectRepo;
import id.web.fahmikudo.meeting.mom.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final static int MAX_PAGE_SIZE = 50;

    @Autowired
    private ProjectRepo projectRepo;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProject(@Valid @RequestBody Project project){
        if (projectRepo.findByNamaProject(project.getNamaProject()).isPresent() && projectRepo.findById(project.getId()).isPresent()){
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        } else {
            projectRepo.save(project);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProject(@PathVariable("id") String id) {
        return projectRepo.findById(id)
                .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getAllProject(@PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "id") String sort,
                                                       @RequestParam(required = false, defaultValue = "asc") String order){
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc" .equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<Project> projectPage = projectRepo.findAll(pr);

        if (projectPage.getContent().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            long totalProjects = projectPage.getTotalElements();
            int nbPageProjects = projectPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalProjects));

            if (nbPageProjects < totalProjects) {
                headers.add("first", buildPageUri(PageRequest.of(0, projectPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(projectPage.getTotalPages() - 1, projectPage.getSize())));

                if (projectPage.hasNext()) {
                    headers.add("next", buildPageUri(projectPage.nextPageable()));
                }

                if (projectPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(projectPage.previousPageable()));
                }

                return new ResponseEntity<>(projectPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity(projectPage.getContent(), headers, HttpStatus.OK);
            }
        }

    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editNotulen(@PathVariable("id") String id, @Valid @RequestBody Project project) {
        Optional<Project> p = projectRepo.findById(id);
        if (!p.isPresent()){
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        } else {
            Project savedProject = p.get();
            savedProject.setNamaProject(project.getNamaProject());

            Project update = projectRepo.save(savedProject);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteNotulen(@PathVariable("id") String id){
        Optional<Project> p = projectRepo.findById(id);
        if (p == null){
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        projectRepo.delete(p.get());
        return ResponseEntity.ok().build();
    }


    private String buildPageUri(Pageable page) {
        return fromUriString("/api/project")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }



}
