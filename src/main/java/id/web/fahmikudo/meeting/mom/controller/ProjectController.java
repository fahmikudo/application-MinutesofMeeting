package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.repository.ProjectRepo;
import id.web.fahmikudo.meeting.mom.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/project")
public class ProjectController {


    @Autowired
    private ProjectRepo projectRepo;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page<Project> listProjects(Pageable page){
        return projectRepo.findAll(page);
    }


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




}
