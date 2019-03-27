package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.ProgramTwitch;
import io.bot.personnal.application.repository.ProgramTwitchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing ProgramTwitch.
 */
@RestController
@RequestMapping("/api")
public class ProgramTwitchResource {

    private final Logger log = LoggerFactory.getLogger(ProgramTwitchResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchProgramTwitch";

    private final ProgramTwitchRepository programTwitchRepository;

    public ProgramTwitchResource(ProgramTwitchRepository programTwitchRepository) {
        this.programTwitchRepository = programTwitchRepository;
    }

    /**
     * POST  /program-twitches : Create a new programTwitch.
     *
     * @param programTwitch the programTwitch to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programTwitch, or with status 400 (Bad Request) if the programTwitch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/program-twitches")
    public ResponseEntity<ProgramTwitch> createProgramTwitch(@RequestBody ProgramTwitch programTwitch) throws URISyntaxException {
        log.debug("REST request to save ProgramTwitch : {}", programTwitch);
        if (programTwitch.getId() != null) {
            throw new BadRequestAlertException("A new programTwitch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgramTwitch result = programTwitchRepository.save(programTwitch);
        return ResponseEntity.created(new URI("/api/program-twitches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /program-twitches : Updates an existing programTwitch.
     *
     * @param programTwitch the programTwitch to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programTwitch,
     * or with status 400 (Bad Request) if the programTwitch is not valid,
     * or with status 500 (Internal Server Error) if the programTwitch couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/program-twitches")
    public ResponseEntity<ProgramTwitch> updateProgramTwitch(@RequestBody ProgramTwitch programTwitch) throws URISyntaxException {
        log.debug("REST request to update ProgramTwitch : {}", programTwitch);
        if (programTwitch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProgramTwitch result = programTwitchRepository.save(programTwitch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programTwitch.getId().toString()))
            .body(result);
    }

    /**
     * GET  /program-twitches : get all the programTwitches.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of programTwitches in body
     */
    @GetMapping("/program-twitches")
    public List<ProgramTwitch> getAllProgramTwitches(@RequestParam(required = false) String filter) {
        if ("streamcalendar-is-null".equals(filter)) {
            log.debug("REST request to get all ProgramTwitchs where streamCalendar is null");
            return StreamSupport
                .stream(programTwitchRepository.findAll().spliterator(), false)
                .filter(programTwitch -> programTwitch.getStreamCalendar() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all ProgramTwitches");
        return programTwitchRepository.findAll();
    }

    /**
     * GET  /program-twitches/:id : get the "id" programTwitch.
     *
     * @param id the id of the programTwitch to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programTwitch, or with status 404 (Not Found)
     */
    @GetMapping("/program-twitches/{id}")
    public ResponseEntity<ProgramTwitch> getProgramTwitch(@PathVariable Long id) {
        log.debug("REST request to get ProgramTwitch : {}", id);
        Optional<ProgramTwitch> programTwitch = programTwitchRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(programTwitch);
    }

    /**
     * DELETE  /program-twitches/:id : delete the "id" programTwitch.
     *
     * @param id the id of the programTwitch to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/program-twitches/{id}")
    public ResponseEntity<Void> deleteProgramTwitch(@PathVariable Long id) {
        log.debug("REST request to delete ProgramTwitch : {}", id);
        programTwitchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
