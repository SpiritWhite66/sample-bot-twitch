package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.DefaultCalendar;
import io.bot.personnal.application.repository.DefaultCalendarRepository;
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
 * REST controller for managing DefaultCalendar.
 */
@RestController
@RequestMapping("/api")
public class DefaultCalendarResource {

    private final Logger log = LoggerFactory.getLogger(DefaultCalendarResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchDefaultCalendar";

    private final DefaultCalendarRepository defaultCalendarRepository;

    public DefaultCalendarResource(DefaultCalendarRepository defaultCalendarRepository) {
        this.defaultCalendarRepository = defaultCalendarRepository;
    }

    /**
     * POST  /default-calendars : Create a new defaultCalendar.
     *
     * @param defaultCalendar the defaultCalendar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new defaultCalendar, or with status 400 (Bad Request) if the defaultCalendar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/default-calendars")
    public ResponseEntity<DefaultCalendar> createDefaultCalendar(@RequestBody DefaultCalendar defaultCalendar) throws URISyntaxException {
        log.debug("REST request to save DefaultCalendar : {}", defaultCalendar);
        if (defaultCalendar.getId() != null) {
            throw new BadRequestAlertException("A new defaultCalendar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DefaultCalendar result = defaultCalendarRepository.save(defaultCalendar);
        return ResponseEntity.created(new URI("/api/default-calendars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /default-calendars : Updates an existing defaultCalendar.
     *
     * @param defaultCalendar the defaultCalendar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated defaultCalendar,
     * or with status 400 (Bad Request) if the defaultCalendar is not valid,
     * or with status 500 (Internal Server Error) if the defaultCalendar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/default-calendars")
    public ResponseEntity<DefaultCalendar> updateDefaultCalendar(@RequestBody DefaultCalendar defaultCalendar) throws URISyntaxException {
        log.debug("REST request to update DefaultCalendar : {}", defaultCalendar);
        if (defaultCalendar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DefaultCalendar result = defaultCalendarRepository.save(defaultCalendar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, defaultCalendar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /default-calendars : get all the defaultCalendars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of defaultCalendars in body
     */
    @GetMapping("/default-calendars")
    public List<DefaultCalendar> getAllDefaultCalendars() {
        log.debug("REST request to get all DefaultCalendars");
        return defaultCalendarRepository.findAll();
    }

    /**
     * GET  /default-calendars/:id : get the "id" defaultCalendar.
     *
     * @param id the id of the defaultCalendar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the defaultCalendar, or with status 404 (Not Found)
     */
    @GetMapping("/default-calendars/{id}")
    public ResponseEntity<DefaultCalendar> getDefaultCalendar(@PathVariable Long id) {
        log.debug("REST request to get DefaultCalendar : {}", id);
        Optional<DefaultCalendar> defaultCalendar = defaultCalendarRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(defaultCalendar);
    }

    /**
     * DELETE  /default-calendars/:id : delete the "id" defaultCalendar.
     *
     * @param id the id of the defaultCalendar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/default-calendars/{id}")
    public ResponseEntity<Void> deleteDefaultCalendar(@PathVariable Long id) {
        log.debug("REST request to delete DefaultCalendar : {}", id);
        defaultCalendarRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
