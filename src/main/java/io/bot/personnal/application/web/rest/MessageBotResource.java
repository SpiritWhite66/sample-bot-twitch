package io.bot.personnal.application.web.rest;
import io.bot.personnal.application.domain.MessageBot;
import io.bot.personnal.application.repository.MessageBotRepository;
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
 * REST controller for managing MessageBot.
 */
@RestController
@RequestMapping("/api")
public class MessageBotResource {

    private final Logger log = LoggerFactory.getLogger(MessageBotResource.class);

    private static final String ENTITY_NAME = "sampleBotTwitchMessageBot";

    private final MessageBotRepository messageBotRepository;

    public MessageBotResource(MessageBotRepository messageBotRepository) {
        this.messageBotRepository = messageBotRepository;
    }

    /**
     * POST  /message-bots : Create a new messageBot.
     *
     * @param messageBot the messageBot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new messageBot, or with status 400 (Bad Request) if the messageBot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/message-bots")
    public ResponseEntity<MessageBot> createMessageBot(@RequestBody MessageBot messageBot) throws URISyntaxException {
        log.debug("REST request to save MessageBot : {}", messageBot);
        if (messageBot.getId() != null) {
            throw new BadRequestAlertException("A new messageBot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageBot result = messageBotRepository.save(messageBot);
        return ResponseEntity.created(new URI("/api/message-bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /message-bots : Updates an existing messageBot.
     *
     * @param messageBot the messageBot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated messageBot,
     * or with status 400 (Bad Request) if the messageBot is not valid,
     * or with status 500 (Internal Server Error) if the messageBot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/message-bots")
    public ResponseEntity<MessageBot> updateMessageBot(@RequestBody MessageBot messageBot) throws URISyntaxException {
        log.debug("REST request to update MessageBot : {}", messageBot);
        if (messageBot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MessageBot result = messageBotRepository.save(messageBot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, messageBot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /message-bots : get all the messageBots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of messageBots in body
     */
    @GetMapping("/message-bots")
    public List<MessageBot> getAllMessageBots() {
        log.debug("REST request to get all MessageBots");
        return messageBotRepository.findAll();
    }

    /**
     * GET  /message-bots/:id : get the "id" messageBot.
     *
     * @param id the id of the messageBot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the messageBot, or with status 404 (Not Found)
     */
    @GetMapping("/message-bots/{id}")
    public ResponseEntity<MessageBot> getMessageBot(@PathVariable Long id) {
        log.debug("REST request to get MessageBot : {}", id);
        Optional<MessageBot> messageBot = messageBotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(messageBot);
    }

    /**
     * DELETE  /message-bots/:id : delete the "id" messageBot.
     *
     * @param id the id of the messageBot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/message-bots/{id}")
    public ResponseEntity<Void> deleteMessageBot(@PathVariable Long id) {
        log.debug("REST request to delete MessageBot : {}", id);
        messageBotRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
