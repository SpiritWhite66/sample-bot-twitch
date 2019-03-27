package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.Bot;
import io.bot.personnal.application.repository.BotRepository;
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
 * Test class for the BotResource REST controller.
 *
 * @see BotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class BotResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BotRepository botRepository;

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

    private MockMvc restBotMockMvc;

    private Bot bot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BotResource botResource = new BotResource(botRepository);
        this.restBotMockMvc = MockMvcBuilders.standaloneSetup(botResource)
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
    public static Bot createEntity(EntityManager em) {
        Bot bot = new Bot()
            .name(DEFAULT_NAME);
        return bot;
    }

    @Before
    public void initTest() {
        bot = createEntity(em);
    }

    @Test
    @Transactional
    public void createBot() throws Exception {
        int databaseSizeBeforeCreate = botRepository.findAll().size();

        // Create the Bot
        restBotMockMvc.perform(post("/api/bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bot)))
            .andExpect(status().isCreated());

        // Validate the Bot in the database
        List<Bot> botList = botRepository.findAll();
        assertThat(botList).hasSize(databaseSizeBeforeCreate + 1);
        Bot testBot = botList.get(botList.size() - 1);
        assertThat(testBot.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = botRepository.findAll().size();

        // Create the Bot with an existing ID
        bot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBotMockMvc.perform(post("/api/bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bot)))
            .andExpect(status().isBadRequest());

        // Validate the Bot in the database
        List<Bot> botList = botRepository.findAll();
        assertThat(botList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBots() throws Exception {
        // Initialize the database
        botRepository.saveAndFlush(bot);

        // Get all the botList
        restBotMockMvc.perform(get("/api/bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBot() throws Exception {
        // Initialize the database
        botRepository.saveAndFlush(bot);

        // Get the bot
        restBotMockMvc.perform(get("/api/bots/{id}", bot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBot() throws Exception {
        // Get the bot
        restBotMockMvc.perform(get("/api/bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBot() throws Exception {
        // Initialize the database
        botRepository.saveAndFlush(bot);

        int databaseSizeBeforeUpdate = botRepository.findAll().size();

        // Update the bot
        Bot updatedBot = botRepository.findById(bot.getId()).get();
        // Disconnect from session so that the updates on updatedBot are not directly saved in db
        em.detach(updatedBot);
        updatedBot
            .name(UPDATED_NAME);

        restBotMockMvc.perform(put("/api/bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBot)))
            .andExpect(status().isOk());

        // Validate the Bot in the database
        List<Bot> botList = botRepository.findAll();
        assertThat(botList).hasSize(databaseSizeBeforeUpdate);
        Bot testBot = botList.get(botList.size() - 1);
        assertThat(testBot.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBot() throws Exception {
        int databaseSizeBeforeUpdate = botRepository.findAll().size();

        // Create the Bot

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBotMockMvc.perform(put("/api/bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bot)))
            .andExpect(status().isBadRequest());

        // Validate the Bot in the database
        List<Bot> botList = botRepository.findAll();
        assertThat(botList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBot() throws Exception {
        // Initialize the database
        botRepository.saveAndFlush(bot);

        int databaseSizeBeforeDelete = botRepository.findAll().size();

        // Delete the bot
        restBotMockMvc.perform(delete("/api/bots/{id}", bot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bot> botList = botRepository.findAll();
        assertThat(botList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bot.class);
        Bot bot1 = new Bot();
        bot1.setId(1L);
        Bot bot2 = new Bot();
        bot2.setId(bot1.getId());
        assertThat(bot1).isEqualTo(bot2);
        bot2.setId(2L);
        assertThat(bot1).isNotEqualTo(bot2);
        bot1.setId(null);
        assertThat(bot1).isNotEqualTo(bot2);
    }
}
