package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.RefCategoriesStream;
import io.bot.personnal.application.repository.RefCategoriesStreamRepository;
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
 * REST controller for managing RefCategoriesStream.
 */
@RestController
@RequestMapping("/api")
public class RefCategoriesStreamResource {

    private final Logger log = LoggerFactory.getLogger(RefCategoriesStreamResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchRefCategoriesStream";

    private final RefCategoriesStreamRepository refCategoriesStreamRepository;

    public RefCategoriesStreamResource(RefCategoriesStreamRepository refCategoriesStreamRepository) {
        this.refCategoriesStreamRepository = refCategoriesStreamRepository;
    }

    /**
     * POST  /ref-categories-streams : Create a new refCategoriesStream.
     *
     * @param refCategoriesStream the refCategoriesStream to create
     * @return the ResponseEntity with status 201 (Created) and with body the new refCategoriesStream, or with status 400 (Bad Request) if the refCategoriesStream has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ref-categories-streams")
    public ResponseEntity<RefCategoriesStream> createRefCategoriesStream(@RequestBody RefCategoriesStream refCategoriesStream) throws URISyntaxException {
        log.debug("REST request to save RefCategoriesStream : {}", refCategoriesStream);
        if (refCategoriesStream.getId() != null) {
            throw new BadRequestAlertException("A new refCategoriesStream cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RefCategoriesStream result = refCategoriesStreamRepository.save(refCategoriesStream);
        return ResponseEntity.created(new URI("/api/ref-categories-streams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ref-categories-streams : Updates an existing refCategoriesStream.
     *
     * @param refCategoriesStream the refCategoriesStream to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated refCategoriesStream,
     * or with status 400 (Bad Request) if the refCategoriesStream is not valid,
     * or with status 500 (Internal Server Error) if the refCategoriesStream couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ref-categories-streams")
    public ResponseEntity<RefCategoriesStream> updateRefCategoriesStream(@RequestBody RefCategoriesStream refCategoriesStream) throws URISyntaxException {
        log.debug("REST request to update RefCategoriesStream : {}", refCategoriesStream);
        if (refCategoriesStream.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RefCategoriesStream result = refCategoriesStreamRepository.save(refCategoriesStream);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, refCategoriesStream.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ref-categories-streams : get all the refCategoriesStreams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of refCategoriesStreams in body
     */
    @GetMapping("/ref-categories-streams")
    public List<RefCategoriesStream> getAllRefCategoriesStreams() {
        log.debug("REST request to get all RefCategoriesStreams");
        return refCategoriesStreamRepository.findAll();
    }

    /**
     * GET  /ref-categories-streams/:id : get the "id" refCategoriesStream.
     *
     * @param id the id of the refCategoriesStream to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the refCategoriesStream, or with status 404 (Not Found)
     */
    @GetMapping("/ref-categories-streams/{id}")
    public ResponseEntity<RefCategoriesStream> getRefCategoriesStream(@PathVariable Long id) {
        log.debug("REST request to get RefCategoriesStream : {}", id);
        Optional<RefCategoriesStream> refCategoriesStream = refCategoriesStreamRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(refCategoriesStream);
    }

    /**
     * DELETE  /ref-categories-streams/:id : delete the "id" refCategoriesStream.
     *
     * @param id the id of the refCategoriesStream to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ref-categories-streams/{id}")
    public ResponseEntity<Void> deleteRefCategoriesStream(@PathVariable Long id) {
        log.debug("REST request to delete RefCategoriesStream : {}", id);
        refCategoriesStreamRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
