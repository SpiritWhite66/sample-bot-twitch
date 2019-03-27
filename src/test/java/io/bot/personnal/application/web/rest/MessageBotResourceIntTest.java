package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.MessageBot;
import io.bot.personnal.application.repository.MessageBotRepository;
import io.bot.personnal.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static io.bot.personnal.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MessageBotResource REST controller.
 *
 * @see MessageBotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class MessageBotResourceIntTest {

    private static final String DEFAULT_CODE_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FREQUENCE = 1;
    private static final Integer UPDATED_FREQUENCE = 2;

    @Autowired
    private MessageBotRepository messageBotRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMessageBotMockMvc;

    private MessageBot messageBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageBotResource messageBotResource = new MessageBotResource(messageBotRepository);
        this.restMessageBotMockMvc = MockMvcBuilders.standaloneSetup(messageBotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageBot createEntity(EntityManager em) {
        MessageBot messageBot = new MessageBot()
            .codeMessage(DEFAULT_CODE_MESSAGE)
            .labelMessage(DEFAULT_LABEL_MESSAGE)
            .frequence(DEFAULT_FREQUENCE);
        return messageBot;
    }

    @Before
    public void initTest() {
        messageBot = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessageBot() throws Exception {
        int databaseSizeBeforeCreate = messageBotRepository.findAll().size();

        // Create the MessageBot
        restMessageBotMockMvc.perform(post("/api/message-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageBot)))
            .andExpect(status().isCreated());

        // Validate the MessageBot in the database
        List<MessageBot> messageBotList = messageBotRepository.findAll();
        assertThat(messageBotList).hasSize(databaseSizeBeforeCreate + 1);
        MessageBot testMessageBot = messageBotList.get(messageBotList.size() - 1);
        assertThat(testMessageBot.getCodeMessage()).isEqualTo(DEFAULT_CODE_MESSAGE);
        assertThat(testMessageBot.getLabelMessage()).isEqualTo(DEFAULT_LABEL_MESSAGE);
        assertThat(testMessageBot.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
    }

    @Test
    @Transactional
    public void createMessageBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageBotRepository.findAll().size();

        // Create the MessageBot with an existing ID
        messageBot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageBotMockMvc.perform(post("/api/message-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageBot)))
            .andExpect(status().isBadRequest());

        // Validate the MessageBot in the database
        List<MessageBot> messageBotList = messageBotRepository.findAll();
        assertThat(messageBotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMessageBots() throws Exception {
        // Initialize the database
        messageBotRepository.saveAndFlush(messageBot);

        // Get all the messageBotList
        restMessageBotMockMvc.perform(get("/api/message-bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeMessage").value(hasItem(DEFAULT_CODE_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].labelMessage").value(hasItem(DEFAULT_LABEL_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE)));
    }
    
    @Test
    @Transactional
    public void getMessageBot() throws Exception {
        // Initialize the database
        messageBotRepository.saveAndFlush(messageBot);

        // Get the messageBot
        restMessageBotMockMvc.perform(get("/api/message-bots/{id}", messageBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messageBot.getId().intValue()))
            .andExpect(jsonPath("$.codeMessage").value(DEFAULT_CODE_MESSAGE.toString()))
            .andExpect(jsonPath("$.labelMessage").value(DEFAULT_LABEL_MESSAGE.toString()))
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE));
    }

    @Test
    @Transactional
    public void getNonExistingMessageBot() throws Exception {
        // Get the messageBot
        restMessageBotMockMvc.perform(get("/api/message-bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageBot() throws Exception {
        // Initialize the database
        messageBotRepository.saveAndFlush(messageBot);

        int databaseSizeBeforeUpdate = messageBotRepository.findAll().size();

        // Update the messageBot
        MessageBot updatedMessageBot = messageBotRepository.findById(messageBot.getId()).get();
        // Disconnect from session so that the updates on updatedMessageBot are not directly saved in db
        em.detach(updatedMessageBot);
        updatedMessageBot
            .codeMessage(UPDATED_CODE_MESSAGE)
            .labelMessage(UPDATED_LABEL_MESSAGE)
            .frequence(UPDATED_FREQUENCE);

        restMessageBotMockMvc.perform(put("/api/message-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessageBot)))
            .andExpect(status().isOk());

        // Validate the MessageBot in the database
        List<MessageBot> messageBotList = messageBotRepository.findAll();
        assertThat(messageBotList).hasSize(databaseSizeBeforeUpdate);
        MessageBot testMessageBot = messageBotList.get(messageBotList.size() - 1);
        assertThat(testMessageBot.getCodeMessage()).isEqualTo(UPDATED_CODE_MESSAGE);
        assertThat(testMessageBot.getLabelMessage()).isEqualTo(UPDATED_LABEL_MESSAGE);
        assertThat(testMessageBot.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingMessageBot() throws Exception {
        int databaseSizeBeforeUpdate = messageBotRepository.findAll().size();

        // Create the MessageBot

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageBotMockMvc.perform(put("/api/message-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageBot)))
            .andExpect(status().isBadRequest());

        // Validate the MessageBot in the database
        List<MessageBot> messageBotList = messageBotRepository.findAll();
        assertThat(messageBotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMessageBot() throws Exception {
        // Initialize the database
        messageBotRepository.saveAndFlush(messageBot);

        int databaseSizeBeforeDelete = messageBotRepository.findAll().size();

        // Delete the messageBot
        restMessageBotMockMvc.perform(delete("/api/message-bots/{id}", messageBot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MessageBot> messageBotList = messageBotRepository.findAll();
        assertThat(messageBotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageBot.class);
        MessageBot messageBot1 = new MessageBot();
        messageBot1.setId(1L);
        MessageBot messageBot2 = new MessageBot();
        messageBot2.setId(messageBot1.getId());
        assertThat(messageBot1).isEqualTo(messageBot2);
        messageBot2.setId(2L);
        assertThat(messageBot1).isNotEqualTo(messageBot2);
        messageBot1.setId(null);
        assertThat(messageBot1).isNotEqualTo(messageBot2);
    }
}
