package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.repository.MeetingRepo;
import id.web.fahmikudo.meeting.mom.repository.PokokBahasanRepo;
import id.web.fahmikudo.meeting.mom.model.Meeting;
import id.web.fahmikudo.meeting.mom.model.PokokBahasan;
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
@RequestMapping(value = "api/pokokbahasan")
public class PokokBahasanController {


    @Autowired
    private PokokBahasanRepo pokokBahasanRepo;

    @Autowired
    private MeetingRepo meetingRepo;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PokokBahasan> getListPokokBahasan(Pageable pageable){
        return pokokBahasanRepo.findAll(pageable);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPokokBahasan(@PathVariable("id") String id){
        return pokokBahasanRepo.findById(id)
                .map(pokokBahasan -> new ResponseEntity<>(pokokBahasan, HttpStatus.OK))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPokokBahasan(@Valid @RequestBody PokokBahasan pokokBahasan){
        Optional<Meeting> meeting = meetingRepo.findById(pokokBahasan.getMeeting().getId());
        boolean valid = false;
        if (meeting.isPresent()){
            valid = true;
        }
        if (valid){
            pokokBahasan.setMeeting(meeting.get());
            pokokBahasanRepo.save(pokokBahasan);
            return new ResponseEntity<>(pokokBahasan, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(pokokBahasan, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editPokokBahasan(@PathVariable("id") String id, @Valid @RequestBody PokokBahasan pokokBahasan){
        Optional<PokokBahasan> pb = pokokBahasanRepo.findById(id);
        if (!pb.isPresent()){
            return new ResponseEntity<>(pokokBahasan, HttpStatus.BAD_REQUEST);
        } else {
            PokokBahasan savedPokokBahasan = pb.get();
            savedPokokBahasan.setNomor(pokokBahasan.getNomor());
            savedPokokBahasan.setPokokBahasan(pokokBahasan.getPokokBahasan());
            savedPokokBahasan.setStatus(pokokBahasan.getStatus());
            savedPokokBahasan.setMeeting(pokokBahasan.getMeeting());

            PokokBahasan update = pokokBahasanRepo.save(savedPokokBahasan);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePokokBahasan(@PathVariable("id") String id){
        Optional<PokokBahasan> pb = pokokBahasanRepo.findById(id);
        if (pb == null){
            return new ResponseEntity<>("Pokok Bahasan Not Found", HttpStatus.NOT_FOUND);
        }
        pokokBahasanRepo.delete(pb.get());
        return ResponseEntity.ok().build();
    }




}
