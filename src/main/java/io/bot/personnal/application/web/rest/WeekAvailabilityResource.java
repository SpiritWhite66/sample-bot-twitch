package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.WeekAvailability;
import io.bot.personnal.application.repository.WeekAvailabilityRepository;
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
 * REST controller for managing WeekAvailability.
 */
@RestController
@RequestMapping("/api")
public class WeekAvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(WeekAvailabilityResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchWeekAvailability";

    private final WeekAvailabilityRepository weekAvailabilityRepository;

    public WeekAvailabilityResource(WeekAvailabilityRepository weekAvailabilityRepository) {
        this.weekAvailabilityRepository = weekAvailabilityRepository;
    }

    /**
     * POST  /week-availabilities : Create a new weekAvailability.
     *
     * @param weekAvailability the weekAvailability to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weekAvailability, or with status 400 (Bad Request) if the weekAvailability has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/week-availabilities")
    public ResponseEntity<WeekAvailability> createWeekAvailability(@RequestBody WeekAvailability weekAvailability) throws URISyntaxException {
        log.debug("REST request to save WeekAvailability : {}", weekAvailability);
        if (weekAvailability.getId() != null) {
            throw new BadRequestAlertException("A new weekAvailability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeekAvailability result = weekAvailabilityRepository.save(weekAvailability);
        return ResponseEntity.created(new URI("/api/week-availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /week-availabilities : Updates an existing weekAvailability.
     *
     * @param weekAvailability the weekAvailability to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weekAvailability,
     * or with status 400 (Bad Request) if the weekAvailability is not valid,
     * or with status 500 (Internal Server Error) if the weekAvailability couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/week-availabilities")
    public ResponseEntity<WeekAvailability> updateWeekAvailability(@RequestBody WeekAvailability weekAvailability) throws URISyntaxException {
        log.debug("REST request to update WeekAvailability : {}", weekAvailability);
        if (weekAvailability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeekAvailability result = weekAvailabilityRepository.save(weekAvailability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, weekAvailability.getId().toString()))
            .body(result);
    }

    /**
     * GET  /week-availabilities : get all the weekAvailabilities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of weekAvailabilities in body
     */
    @GetMapping("/week-availabilities")
    public List<WeekAvailability> getAllWeekAvailabilities() {
        log.debug("REST request to get all WeekAvailabilities");
        return weekAvailabilityRepository.findAll();
    }

    /**
     * GET  /week-availabilities/:id : get the "id" weekAvailability.
     *
     * @param id the id of the weekAvailability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weekAvailability, or with status 404 (Not Found)
     */
    @GetMapping("/week-availabilities/{id}")
    public ResponseEntity<WeekAvailability> getWeekAvailability(@PathVariable Long id) {
        log.debug("REST request to get WeekAvailability : {}", id);
        Optional<WeekAvailability> weekAvailability = weekAvailabilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weekAvailability);
    }

    /**
     * DELETE  /week-availabilities/:id : delete the "id" weekAvailability.
     *
     * @param id the id of the weekAvailability to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/week-availabilities/{id}")
    public ResponseEntity<Void> deleteWeekAvailability(@PathVariable Long id) {
        log.debug("REST request to delete WeekAvailability : {}", id);
        weekAvailabilityRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
