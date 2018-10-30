package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.dao.DaftarPesertaDao;
import id.web.fahmikudo.meeting.mom.dao.MeetingDao;
import id.web.fahmikudo.meeting.mom.model.DaftarPeserta;
import id.web.fahmikudo.meeting.mom.model.Meeting;
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
@RequestMapping(value = "api/daftarpeserta")
public class DaftarPesertaController {

    private static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private DaftarPesertaDao daftarPesertaDao;

    @Autowired
    private MeetingDao meetingDao;


    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDaftarPeserta(@PathVariable String id){
        return daftarPesertaDao.findById(id)
                .map(peserta -> new ResponseEntity<>(peserta, HttpStatus.OK))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDaftarPeserta(@Valid @RequestBody DaftarPeserta daftarPeserta){

        Optional<Meeting> mt = meetingDao.findById(daftarPeserta.getMeeting().getId());
        boolean valid = false;
        if (mt.isPresent()){
            valid = true;
        }
        if (valid){
            daftarPeserta.setMeeting(mt.get());
            daftarPesertaDao.save(daftarPeserta);
            return new ResponseEntity<>(daftarPeserta, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(daftarPeserta, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editDaftarPeserta(@PathVariable("id") String id, @Valid @RequestBody DaftarPeserta daftarPeserta){
        Optional<DaftarPeserta> dp = daftarPesertaDao.findById(id);
        if (!dp.isPresent()){
            return new ResponseEntity<>(daftarPeserta, HttpStatus.BAD_REQUEST);
        } else {
            DaftarPeserta savedPeserta = dp.get();
            savedPeserta.setNama(daftarPeserta.getNama());
            savedPeserta.setInstansi(daftarPeserta.getInstansi());
            savedPeserta.setAbsen(daftarPeserta.getAbsen());
            savedPeserta.setMeeting(daftarPeserta.getMeeting());

            DaftarPeserta update = daftarPesertaDao.save(savedPeserta);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDaftarPeserta(@PathVariable("id") String id){
        Optional<DaftarPeserta> dp = daftarPesertaDao.findById(id);
        if (dp == null){
            return new ResponseEntity<>("Daftar Peserta Not Found", HttpStatus.NOT_FOUND);
        }
        daftarPesertaDao.delete(dp.get());
        return ResponseEntity.ok().build();
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DaftarPeserta>> getAllDaftarPeserta(@PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
                                                                  @RequestParam(required = false, defaultValue = "id") String sort,
                                                                  @RequestParam(required = false, defaultValue = "asc") String order){
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc" .equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<DaftarPeserta> dpPage = daftarPesertaDao.findAll(pr);

        if (dpPage.getContent().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            long totalDaftarPeserta = dpPage.getTotalElements();
            int nbPageDaftarPeserta = dpPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalDaftarPeserta));

            if (nbPageDaftarPeserta < nbPageDaftarPeserta) {
                headers.add("first", buildPageUri(PageRequest.of(0, dpPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(dpPage.getTotalPages() - 1, dpPage.getSize())));

                if (dpPage.hasNext()) {
                    headers.add("next", buildPageUri(dpPage.nextPageable()));
                }

                if (dpPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(dpPage.previousPageable()));
                }

                return new ResponseEntity<>(dpPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity(dpPage.getContent(), headers, HttpStatus.OK);
            }
        }
    }

    private String buildPageUri(Pageable page) {
        return fromUriString("/api/daftarpeserta")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }
}
