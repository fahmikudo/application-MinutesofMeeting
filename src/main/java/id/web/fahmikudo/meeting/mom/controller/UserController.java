package id.web.fahmikudo.meeting.mom.controller;

import id.web.fahmikudo.meeting.mom.repository.UserRepo;
import id.web.fahmikudo.meeting.mom.model.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@RestController
@RequestMapping("/api/notulen")
public class UserController {

    private static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private UserRepo userRepo;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@Valid @RequestBody User user){
        if (userRepo.findByNamaLengkap(user.getNamaLengkap()).isPresent()){
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        } else {
            userRepo.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") String id) {
        return userRepo.findById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUser(@PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
                                                    @RequestParam(required = false, defaultValue = "id") String sort,
                                                    @RequestParam(required = false, defaultValue = "asc") String order){
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc" .equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<User> notulenPage = userRepo.findAll(pr);

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
    public ResponseEntity<?> editUser(@PathVariable("id") String id, @Valid @RequestBody User user) {
        Optional<User> n = userRepo.findById(id);
        if (!n.isPresent()){
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        } else {
            User savedUser = n.get();
            savedUser.setNamaLengkap(user.getNamaLengkap());
            savedUser.setJabatan(user.getJabatan());
            savedUser.setNoHp(user.getNoHp());
            savedUser.setUsername(user.getUsername());
            savedUser.setPassword(user.getPassword());

            User update = userRepo.save(savedUser);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id){
        Optional<User> n = userRepo.findById(id);
        if (n == null){
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        userRepo.delete(n.get());
        return ResponseEntity.ok().build();
    }


    private String buildPageUri(Pageable page) {
        return fromUriString("/api/user")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }



}
