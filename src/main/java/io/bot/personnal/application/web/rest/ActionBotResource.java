package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.ActionBot;
import io.bot.personnal.application.repository.ActionBotRepository;
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
 * REST controller for managing ActionBot.
 */
@RestController
@RequestMapping("/api")
public class ActionBotResource {

    private final Logger log = LoggerFactory.getLogger(ActionBotResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchActionBot";

    private final ActionBotRepository actionBotRepository;

    public ActionBotResource(ActionBotRepository actionBotRepository) {
        this.actionBotRepository = actionBotRepository;
    }

    /**
     * POST  /action-bots : Create a new actionBot.
     *
     * @param actionBot the actionBot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actionBot, or with status 400 (Bad Request) if the actionBot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/action-bots")
    public ResponseEntity<ActionBot> createActionBot(@RequestBody ActionBot actionBot) throws URISyntaxException {
        log.debug("REST request to save ActionBot : {}", actionBot);
        if (actionBot.getId() != null) {
            throw new BadRequestAlertException("A new actionBot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActionBot result = actionBotRepository.save(actionBot);
        return ResponseEntity.created(new URI("/api/action-bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /action-bots : Updates an existing actionBot.
     *
     * @param actionBot the actionBot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actionBot,
     * or with status 400 (Bad Request) if the actionBot is not valid,
     * or with status 500 (Internal Server Error) if the actionBot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/action-bots")
    public ResponseEntity<ActionBot> updateActionBot(@RequestBody ActionBot actionBot) throws URISyntaxException {
        log.debug("REST request to update ActionBot : {}", actionBot);
        if (actionBot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActionBot result = actionBotRepository.save(actionBot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actionBot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /action-bots : get all the actionBots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actionBots in body
     */
    @GetMapping("/action-bots")
    public List<ActionBot> getAllActionBots() {
        log.debug("REST request to get all ActionBots");
        return actionBotRepository.findAll();
    }

    /**
     * GET  /action-bots/:id : get the "id" actionBot.
     *
     * @param id the id of the actionBot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actionBot, or with status 404 (Not Found)
     */
    @GetMapping("/action-bots/{id}")
    public ResponseEntity<ActionBot> getActionBot(@PathVariable Long id) {
        log.debug("REST request to get ActionBot : {}", id);
        Optional<ActionBot> actionBot = actionBotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actionBot);
    }

    /**
     * DELETE  /action-bots/:id : delete the "id" actionBot.
     *
     * @param id the id of the actionBot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/action-bots/{id}")
    public ResponseEntity<Void> deleteActionBot(@PathVariable Long id) {
        log.debug("REST request to delete ActionBot : {}", id);
        actionBotRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
