package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.Bot;
import io.bot.personnal.application.repository.BotRepository;
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
 * REST controller for managing Bot.
 */
@RestController
@RequestMapping("/api")
public class BotResource {

    private final Logger log = LoggerFactory.getLogger(BotResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchBot";

    private final BotRepository botRepository;

    public BotResource(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    /**
     * POST  /bots : Create a new bot.
     *
     * @param bot the bot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bot, or with status 400 (Bad Request) if the bot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bots")
    public ResponseEntity<Bot> createBot(@RequestBody Bot bot) throws URISyntaxException {
        log.debug("REST request to save Bot : {}", bot);
        if (bot.getId() != null) {
            throw new BadRequestAlertException("A new bot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bot result = botRepository.save(bot);
        return ResponseEntity.created(new URI("/api/bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bots : Updates an existing bot.
     *
     * @param bot the bot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bot,
     * or with status 400 (Bad Request) if the bot is not valid,
     * or with status 500 (Internal Server Error) if the bot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bots")
    public ResponseEntity<Bot> updateBot(@RequestBody Bot bot) throws URISyntaxException {
        log.debug("REST request to update Bot : {}", bot);
        if (bot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bot result = botRepository.save(bot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bots : get all the bots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bots in body
     */
    @GetMapping("/bots")
    public List<Bot> getAllBots() {
        log.debug("REST request to get all Bots");
        return botRepository.findAll();
    }

    /**
     * GET  /bots/:id : get the "id" bot.
     *
     * @param id the id of the bot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bot, or with status 404 (Not Found)
     */
    @GetMapping("/bots/{id}")
    public ResponseEntity<Bot> getBot(@PathVariable Long id) {
        log.debug("REST request to get Bot : {}", id);
        Optional<Bot> bot = botRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bot);
    }

    /**
     * DELETE  /bots/:id : delete the "id" bot.
     *
     * @param id the id of the bot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bots/{id}")
    public ResponseEntity<Void> deleteBot(@PathVariable Long id) {
        log.debug("REST request to delete Bot : {}", id);
        botRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
