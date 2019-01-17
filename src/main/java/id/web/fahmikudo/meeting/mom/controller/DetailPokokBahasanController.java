package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.repository.DetailPokokBahasanRepo;
import id.web.fahmikudo.meeting.mom.repository.PokokBahasanRepo;
import id.web.fahmikudo.meeting.mom.model.DetailPokokBahasan;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@RestController
@RequestMapping(value = "api/detail")
public class DetailPokokBahasanController {


    @Autowired
    private DetailPokokBahasanRepo detailPokokBahasanRepo;

    @Autowired
    private PokokBahasanRepo pokokBahasanRepo;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<DetailPokokBahasan> getListDetail(Pageable pageable){
        return detailPokokBahasanRepo.findAll(pageable);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDetail(@Valid @RequestBody DetailPokokBahasan detailPokokBahasan){
        Optional<PokokBahasan> pb = pokokBahasanRepo.findById(detailPokokBahasan.getPokokBahasan().getId());
        boolean valid = false;
        if (pb.isPresent()){
            valid = true;
        }
        if (valid){
            detailPokokBahasan.setPokokBahasan(pb.get());
            detailPokokBahasanRepo.save(detailPokokBahasan);
            return new ResponseEntity<>(detailPokokBahasan, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(detailPokokBahasan, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editDetail(@PathVariable("id") String id, @Valid @RequestBody DetailPokokBahasan detailPokokBahasan){
        Optional<DetailPokokBahasan> dpk = detailPokokBahasanRepo.findById(id);
        if (!dpk.isPresent()){
            return new ResponseEntity<>(detailPokokBahasan, HttpStatus.NOT_FOUND);
        } else {
            DetailPokokBahasan savedDetail = dpk.get();
            savedDetail.setPokokBahasan(detailPokokBahasan.getPokokBahasan());
            savedDetail.setDetailPokokBahasan(detailPokokBahasan.getDetailPokokBahasan());
            savedDetail.setNomor(detailPokokBahasan.getNomor());

            DetailPokokBahasan update = detailPokokBahasanRepo.save(savedDetail);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDetail(@PathVariable("id") String id){
        Optional<DetailPokokBahasan> dpk = detailPokokBahasanRepo.findById(id);
        if (dpk == null){
            return new ResponseEntity<>("Detail Pokok Bahasan Not Found", HttpStatus.NOT_FOUND);
        }
        detailPokokBahasanRepo.delete(dpk.get());
        return ResponseEntity.ok().build();
    }



}
