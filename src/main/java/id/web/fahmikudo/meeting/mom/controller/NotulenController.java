package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.dao.NotulenDao;
import id.web.fahmikudo.meeting.mom.model.Notulen;
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
@RequestMapping("/api/notulen")
public class NotulenController {

    private static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private NotulenDao notulenDao;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNotulen(@Valid @RequestBody Notulen notulen){
        if (notulenDao.findByNama(notulen.getNama()).isPresent() && notulenDao.findById(notulen.getId()).isPresent()){
            return new ResponseEntity<>(notulen, HttpStatus.BAD_REQUEST);
        } else {
            notulenDao.save(notulen);
            return new ResponseEntity<>(notulen, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotulen(@PathVariable("id") String id) {
        return notulenDao.findById(id)
                .map(notulen -> new ResponseEntity<>(notulen, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Notulen>> getAllNotulen(@PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "id") String sort,
                                                       @RequestParam(required = false, defaultValue = "asc") String order){
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc" .equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<Notulen> notulenPage = notulenDao.findAll(pr);

        if (notulenPage.getContent().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            long totalNotulens = notulenPage.getTotalElements();
            int nbPageNotulens = notulenPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalNotulens));

            if (nbPageNotulens < totalNotulens) {
                headers.add("first", buildPageUri(PageRequest.of(0, notulenPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(notulenPage.getTotalPages() - 1, notulenPage.getSize())));

                if (notulenPage.hasNext()) {
                    headers.add("next", buildPageUri(notulenPage.nextPageable()));
                }

                if (notulenPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(notulenPage.previousPageable()));
                }

                return new ResponseEntity<>(notulenPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity(notulenPage.getContent(), headers, HttpStatus.OK);
            }
        }
    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editNotulen(@PathVariable("id") String id, @Valid @RequestBody Notulen notulen) {
        Optional<Notulen> n = notulenDao.findById(id);
        if (!n.isPresent()){
            return new ResponseEntity<>(notulen, HttpStatus.BAD_REQUEST);
        } else {
            Notulen savedNotulen = n.get();
            savedNotulen.setNama(notulen.getNama());
            savedNotulen.setJabatan(notulen.getJabatan());
            savedNotulen.setNoHp(notulen.getNoHp());

            Notulen update = notulenDao.save(savedNotulen);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteNotulen(@PathVariable("id") String id){
        Optional<Notulen> n = notulenDao.findById(id);
        if (n == null){
            return new ResponseEntity("Notulen not found", HttpStatus.NOT_FOUND);
        }
        notulenDao.delete(n.get());
        return ResponseEntity.ok().build();
    }


    private String buildPageUri(Pageable page) {
        return fromUriString("/api/notulen")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }



}
