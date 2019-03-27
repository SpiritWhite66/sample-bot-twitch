package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.CommentAvailability;
import io.bot.personnal.application.repository.CommentAvailabilityRepository;
import io.bot.personnal.application.web.rest.errors.BadRequestAlertException;
import io.bot.personnal.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CommentAvailability.
 */
@RestController
@RequestMapping("/api")
public class CommentAvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(CommentAvailabilityResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchCommentAvailability";

    private final CommentAvailabilityRepository commentAvailabilityRepository;

    public CommentAvailabilityResource(CommentAvailabilityRepository commentAvailabilityRepository) {
        this.commentAvailabilityRepository = commentAvailabilityRepository;
    }

    /**
     * POST  /comment-availabilities : Create a new commentAvailability.
     *
     * @param commentAvailability the commentAvailability to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentAvailability, or with status 400 (Bad Request) if the commentAvailability has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comment-availabilities")
    public ResponseEntity<CommentAvailability> createCommentAvailability(@RequestBody CommentAvailability commentAvailability) throws URISyntaxException {
        log.debug("REST request to save CommentAvailability : {}", commentAvailability);
        if (commentAvailability.getId() != null) {
            throw new BadRequestAlertException("A new commentAvailability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentAvailability result = commentAvailabilityRepository.save(commentAvailability);
        return ResponseEntity.created(new URI("/api/comment-availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comment-availabilities : Updates an existing commentAvailability.
     *
     * @param commentAvailability the commentAvailability to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentAvailability,
     * or with status 400 (Bad Request) if the commentAvailability is not valid,
     * or with status 500 (Internal Server Error) if the commentAvailability couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comment-availabilities")
    public ResponseEntity<CommentAvailability> updateCommentAvailability(@RequestBody CommentAvailability commentAvailability) throws URISyntaxException {
        log.debug("REST request to update CommentAvailability : {}", commentAvailability);
        if (commentAvailability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentAvailability result = commentAvailabilityRepository.save(commentAvailability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentAvailability.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comment-availabilities : get all the commentAvailabilities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commentAvailabilities in body
     */
    @GetMapping("/comment-availabilities")
    public List<CommentAvailability> getAllCommentAvailabilities() {
        log.debug("REST request to get all CommentAvailabilities");
        return commentAvailabilityRepository.findAll();
    }

    /**
     * GET  /comment-availabilities/:id : get the "id" commentAvailability.
     *
     * @param id the id of the commentAvailability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentAvailability, or with status 404 (Not Found)
     */
    @GetMapping("/comment-availabilities/{id}")
    public ResponseEntity<CommentAvailability> getCommentAvailability(@PathVariable Long id) {
        log.debug("REST request to get CommentAvailability : {}", id);
        Optional<CommentAvailability> commentAvailability = commentAvailabilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commentAvailability);
    }

    /**
     * DELETE  /comment-availabilities/:id : delete the "id" commentAvailability.
     *
     * @param id the id of the commentAvailability to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comment-availabilities/{id}")
    public ResponseEntity<Void> deleteCommentAvailability(@PathVariable Long id) {
        log.debug("REST request to delete CommentAvailability : {}", id);
        commentAvailabilityRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
