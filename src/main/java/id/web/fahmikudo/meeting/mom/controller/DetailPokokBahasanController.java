package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.dao.DetailPokokBahasanDao;
import id.web.fahmikudo.meeting.mom.dao.PokokBahasanDao;
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

    private static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private DetailPokokBahasanDao detailPokokBahasanDao;

    @Autowired
    private PokokBahasanDao pokokBahasanDao;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDetail(@Valid @RequestBody DetailPokokBahasan detailPokokBahasan){
        Optional<PokokBahasan> pb = pokokBahasanDao.findById(detailPokokBahasan.getPokokBahasan().getId());
        boolean valid = false;
        if (pb.isPresent()){
            valid = true;
        }
        if (valid){
            detailPokokBahasan.setPokokBahasan(pb.get());
            detailPokokBahasanDao.save(detailPokokBahasan);
            return new ResponseEntity<>(detailPokokBahasan, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(detailPokokBahasan, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editDetail(@PathVariable("id") String id, @Valid @RequestBody DetailPokokBahasan detailPokokBahasan){
        Optional<DetailPokokBahasan> dpk = detailPokokBahasanDao.findById(id);
        if (!dpk.isPresent()){
            return new ResponseEntity<>(detailPokokBahasan, HttpStatus.NOT_FOUND);
        } else {
            DetailPokokBahasan savedDetail = dpk.get();
            savedDetail.setPokokBahasan(detailPokokBahasan.getPokokBahasan());
            savedDetail.setDetailPokokBahasan(detailPokokBahasan.getDetailPokokBahasan());
            savedDetail.setNomor(detailPokokBahasan.getNomor());

            DetailPokokBahasan update = detailPokokBahasanDao.save(savedDetail);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDetail(@PathVariable("id") String id){
        Optional<DetailPokokBahasan> dpk = detailPokokBahasanDao.findById(id);
        if (dpk == null){
            return new ResponseEntity<>("Detail Pokok Bahasan Not Found", HttpStatus.NOT_FOUND);
        }
        detailPokokBahasanDao.delete(dpk.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DetailPokokBahasan>> getAllDeatil(@PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
                                                                  @RequestParam(required = false, defaultValue = "id") String sort,
                                                                  @RequestParam(required = false, defaultValue = "asc") String order){
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc" .equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<DetailPokokBahasan> detailPage = detailPokokBahasanDao.findAll(pr);

        if (detailPage.getContent().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            long totalDetails = detailPage.getTotalElements();
            int nbPageDetails = detailPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalDetails));

            if (nbPageDetails < totalDetails) {
                headers.add("first", buildPageUri(PageRequest.of(0, detailPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(detailPage.getTotalPages() - 1, detailPage.getSize())));

                if (detailPage.hasNext()) {
                    headers.add("next", buildPageUri(detailPage.nextPageable()));
                }

                if (detailPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(detailPage.previousPageable()));
                }

                return new ResponseEntity<>(detailPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity(detailPage.getContent(), headers, HttpStatus.OK);
            }
        }
    }

    private String buildPageUri(Pageable page) {
        return fromUriString("/api/detail")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }


}
