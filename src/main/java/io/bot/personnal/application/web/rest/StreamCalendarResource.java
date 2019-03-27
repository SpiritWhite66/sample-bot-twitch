package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.StreamCalendar;
import io.bot.personnal.application.repository.StreamCalendarRepository;
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
 * REST controller for managing StreamCalendar.
 */
@RestController
@RequestMapping("/api")
public class StreamCalendarResource {

    private final Logger log = LoggerFactory.getLogger(StreamCalendarResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchStreamCalendar";

    private final StreamCalendarRepository streamCalendarRepository;

    public StreamCalendarResource(StreamCalendarRepository streamCalendarRepository) {
        this.streamCalendarRepository = streamCalendarRepository;
    }

    /**
     * POST  /stream-calendars : Create a new streamCalendar.
     *
     * @param streamCalendar the streamCalendar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new streamCalendar, or with status 400 (Bad Request) if the streamCalendar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stream-calendars")
    public ResponseEntity<StreamCalendar> createStreamCalendar(@RequestBody StreamCalendar streamCalendar) throws URISyntaxException {
        log.debug("REST request to save StreamCalendar : {}", streamCalendar);
        if (streamCalendar.getId() != null) {
            throw new BadRequestAlertException("A new streamCalendar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StreamCalendar result = streamCalendarRepository.save(streamCalendar);
        return ResponseEntity.created(new URI("/api/stream-calendars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stream-calendars : Updates an existing streamCalendar.
     *
     * @param streamCalendar the streamCalendar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated streamCalendar,
     * or with status 400 (Bad Request) if the streamCalendar is not valid,
     * or with status 500 (Internal Server Error) if the streamCalendar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stream-calendars")
    public ResponseEntity<StreamCalendar> updateStreamCalendar(@RequestBody StreamCalendar streamCalendar) throws URISyntaxException {
        log.debug("REST request to update StreamCalendar : {}", streamCalendar);
        if (streamCalendar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StreamCalendar result = streamCalendarRepository.save(streamCalendar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, streamCalendar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stream-calendars : get all the streamCalendars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of streamCalendars in body
     */
    @GetMapping("/stream-calendars")
    public List<StreamCalendar> getAllStreamCalendars() {
        log.debug("REST request to get all StreamCalendars");
        return streamCalendarRepository.findAll();
    }

    /**
     * GET  /stream-calendars/:id : get the "id" streamCalendar.
     *
     * @param id the id of the streamCalendar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the streamCalendar, or with status 404 (Not Found)
     */
    @GetMapping("/stream-calendars/{id}")
    public ResponseEntity<StreamCalendar> getStreamCalendar(@PathVariable Long id) {
        log.debug("REST request to get StreamCalendar : {}", id);
        Optional<StreamCalendar> streamCalendar = streamCalendarRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(streamCalendar);
    }

    /**
     * DELETE  /stream-calendars/:id : delete the "id" streamCalendar.
     *
     * @param id the id of the streamCalendar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stream-calendars/{id}")
    public ResponseEntity<Void> deleteStreamCalendar(@PathVariable Long id) {
        log.debug("REST request to delete StreamCalendar : {}", id);
        streamCalendarRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
