package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.OverWriteCalendar;
import io.bot.personnal.application.repository.OverWriteCalendarRepository;
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
 * REST controller for managing OverWriteCalendar.
 */
@RestController
@RequestMapping("/api")
public class OverWriteCalendarResource {

    private final Logger log = LoggerFactory.getLogger(OverWriteCalendarResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchOverWriteCalendar";

    private final OverWriteCalendarRepository overWriteCalendarRepository;

    public OverWriteCalendarResource(OverWriteCalendarRepository overWriteCalendarRepository) {
        this.overWriteCalendarRepository = overWriteCalendarRepository;
    }

    /**
     * POST  /over-write-calendars : Create a new overWriteCalendar.
     *
     * @param overWriteCalendar the overWriteCalendar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new overWriteCalendar, or with status 400 (Bad Request) if the overWriteCalendar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/over-write-calendars")
    public ResponseEntity<OverWriteCalendar> createOverWriteCalendar(@RequestBody OverWriteCalendar overWriteCalendar) throws URISyntaxException {
        log.debug("REST request to save OverWriteCalendar : {}", overWriteCalendar);
        if (overWriteCalendar.getId() != null) {
            throw new BadRequestAlertException("A new overWriteCalendar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OverWriteCalendar result = overWriteCalendarRepository.save(overWriteCalendar);
        return ResponseEntity.created(new URI("/api/over-write-calendars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /over-write-calendars : Updates an existing overWriteCalendar.
     *
     * @param overWriteCalendar the overWriteCalendar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated overWriteCalendar,
     * or with status 400 (Bad Request) if the overWriteCalendar is not valid,
     * or with status 500 (Internal Server Error) if the overWriteCalendar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/over-write-calendars")
    public ResponseEntity<OverWriteCalendar> updateOverWriteCalendar(@RequestBody OverWriteCalendar overWriteCalendar) throws URISyntaxException {
        log.debug("REST request to update OverWriteCalendar : {}", overWriteCalendar);
        if (overWriteCalendar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OverWriteCalendar result = overWriteCalendarRepository.save(overWriteCalendar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, overWriteCalendar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /over-write-calendars : get all the overWriteCalendars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of overWriteCalendars in body
     */
    @GetMapping("/over-write-calendars")
    public List<OverWriteCalendar> getAllOverWriteCalendars() {
        log.debug("REST request to get all OverWriteCalendars");
        return overWriteCalendarRepository.findAll();
    }

    /**
     * GET  /over-write-calendars/:id : get the "id" overWriteCalendar.
     *
     * @param id the id of the overWriteCalendar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the overWriteCalendar, or with status 404 (Not Found)
     */
    @GetMapping("/over-write-calendars/{id}")
    public ResponseEntity<OverWriteCalendar> getOverWriteCalendar(@PathVariable Long id) {
        log.debug("REST request to get OverWriteCalendar : {}", id);
        Optional<OverWriteCalendar> overWriteCalendar = overWriteCalendarRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(overWriteCalendar);
    }

    /**
     * DELETE  /over-write-calendars/:id : delete the "id" overWriteCalendar.
     *
     * @param id the id of the overWriteCalendar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/over-write-calendars/{id}")
    public ResponseEntity<Void> deleteOverWriteCalendar(@PathVariable Long id) {
        log.debug("REST request to delete OverWriteCalendar : {}", id);
        overWriteCalendarRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
