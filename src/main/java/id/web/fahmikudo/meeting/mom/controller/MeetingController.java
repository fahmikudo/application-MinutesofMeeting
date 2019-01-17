package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.repository.MeetingRepo;
import id.web.fahmikudo.meeting.mom.repository.UserRepo;
import id.web.fahmikudo.meeting.mom.repository.ProjectRepo;
import id.web.fahmikudo.meeting.mom.model.Meeting;
import id.web.fahmikudo.meeting.mom.model.User;
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
@RequestMapping("/api/meeting")
public class MeetingController {

    @Autowired
    private MeetingRepo meetingRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Meeting> getListMeetings(Pageable page){
        return meetingRepo.findAll(page);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMeeting(@PathVariable String id){
        return meetingRepo.findById(id)
                .map(meeting -> new ResponseEntity<>(meeting, HttpStatus.OK))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMeeting(@Valid @RequestBody Meeting meeting){

        Optional<Project> p = projectRepo.findById(meeting.getProject().getId());
        Optional<User> n = userRepo.findById(meeting.getUsers().getId());

        boolean valid = false;
        if (p.isPresent() && n.isPresent()){
            valid = true;
        }
        if (valid){
            meeting.setProject(p.get());
            meeting.setUsers(n.get());
            meetingRepo.save(meeting);
            return new ResponseEntity<>(meeting, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(meeting, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMeeting(@PathVariable("id") String id, @Valid @RequestBody Meeting meeting){
        Optional<Meeting> mt = meetingRepo.findById(id);
        if (!mt.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Meeting savedMeeting = mt.get();
            savedMeeting.setAgenda(meeting.getAgenda());
            savedMeeting.setKesimpulan(meeting.getKesimpulan());
            savedMeeting.setLokasi(meeting.getLokasi());
            savedMeeting.setTanggalWaktu(meeting.getTanggalWaktu());
            savedMeeting.setProject(meeting.getProject());
            savedMeeting.setUsers(meeting.getUsers());

            Meeting update = meetingRepo.save(savedMeeting);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProject(@PathVariable(value = "id") String id) {
        Optional<Meeting> meeting = meetingRepo.findById(id);
        if (meeting == null){
            return new ResponseEntity<>("Meeting not found", HttpStatus.NOT_FOUND);
        }
        meetingRepo.delete(meeting.get());

        return ResponseEntity.ok().build();
    }





}
