package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.dao.MeetingDao;
import id.web.fahmikudo.meeting.mom.dao.UserDao;
import id.web.fahmikudo.meeting.mom.dao.ProjectDao;
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

    private static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private MeetingDao meetingDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserDao userDao;


    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMeeting(@PathVariable String id){
        return meetingDao.findById(id)
                .map(meeting -> new ResponseEntity<>(meeting, HttpStatus.OK))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMeeting(@Valid @RequestBody Meeting meeting){

        Optional<Project> p = projectDao.findById(meeting.getProject().getId());
        Optional<User> n = userDao.findById(meeting.getUser().getId());

        boolean valid = false;
        if (p.isPresent() && n.isPresent()){
            valid = true;
        }
        if (valid){
            meeting.setProject(p.get());
            meeting.setUser(n.get());
            meetingDao.save(meeting);
            return new ResponseEntity<>(meeting, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(meeting, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMeeting(@PathVariable("id") String id, @Valid @RequestBody Meeting meeting){
        Optional<Meeting> mt = meetingDao.findById(id);
        if (!mt.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Meeting savedMeeting = mt.get();
            savedMeeting.setAgenda(meeting.getAgenda());
            savedMeeting.setKesimpulan(meeting.getKesimpulan());
            savedMeeting.setLokasi(meeting.getLokasi());
            savedMeeting.setTanggalWaktu(meeting.getTanggalWaktu());
            savedMeeting.setProject(meeting.getProject());
            savedMeeting.setUser(meeting.getUser());

            Meeting update = meetingDao.save(savedMeeting);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProject(@PathVariable(value = "id") String id) {
        Optional<Meeting> meeting = meetingDao.findById(id);
        if (meeting == null){
            return new ResponseEntity<>("Meeting not found", HttpStatus.NOT_FOUND);
        }
        meetingDao.delete(meeting.get());

        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Meeting>> getAllMeeting(@PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "id") String sort,
                                                       @RequestParam(required = false, defaultValue = "asc") String order){
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc" .equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<Meeting> meetingPage = meetingDao.findAll(pr);

        if (meetingPage.getContent().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            long totalMeetings = meetingPage.getTotalElements();
            int nbPageMeetings = meetingPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalMeetings));

            if (nbPageMeetings < totalMeetings) {
                headers.add("first", buildPageUri(PageRequest.of(0, meetingPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(meetingPage.getTotalPages() - 1, meetingPage.getSize())));

                if (meetingPage.hasNext()) {
                    headers.add("next", buildPageUri(meetingPage.nextPageable()));
                }

                if (meetingPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(meetingPage.previousPageable()));
                }

                return new ResponseEntity<>(meetingPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity(meetingPage.getContent(), headers, HttpStatus.OK);
            }
        }
    }


    private String buildPageUri(Pageable page) {
        return fromUriString("/api/meeting")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }



}
