package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.ActionBot;
import io.bot.personnal.application.repository.ActionBotRepository;
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

import io.bot.personnal.application.domain.enumeration.Role;
/**
 * Test class for the ActionBotResource REST controller.
 *
 * @see ActionBotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class ActionBotResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMAND = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.MODERATOR;
    private static final Role UPDATED_ROLE = Role.STREAMER;

    @Autowired
    private ActionBotRepository actionBotRepository;

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

    private MockMvc restActionBotMockMvc;

    private ActionBot actionBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActionBotResource actionBotResource = new ActionBotResource(actionBotRepository);
        this.restActionBotMockMvc = MockMvcBuilders.standaloneSetup(actionBotResource)
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
    public static ActionBot createEntity(EntityManager em) {
        ActionBot actionBot = new ActionBot()
            .name(DEFAULT_NAME)
            .command(DEFAULT_COMMAND)
            .role(DEFAULT_ROLE);
        return actionBot;
    }

    @Before
    public void initTest() {
        actionBot = createEntity(em);
    }

    @Test
    @Transactional
    public void createActionBot() throws Exception {
        int databaseSizeBeforeCreate = actionBotRepository.findAll().size();

        // Create the ActionBot
        restActionBotMockMvc.perform(post("/api/action-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionBot)))
            .andExpect(status().isCreated());

        // Validate the ActionBot in the database
        List<ActionBot> actionBotList = actionBotRepository.findAll();
        assertThat(actionBotList).hasSize(databaseSizeBeforeCreate + 1);
        ActionBot testActionBot = actionBotList.get(actionBotList.size() - 1);
        assertThat(testActionBot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActionBot.getCommand()).isEqualTo(DEFAULT_COMMAND);
        assertThat(testActionBot.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createActionBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actionBotRepository.findAll().size();

        // Create the ActionBot with an existing ID
        actionBot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionBotMockMvc.perform(post("/api/action-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionBot)))
            .andExpect(status().isBadRequest());

        // Validate the ActionBot in the database
        List<ActionBot> actionBotList = actionBotRepository.findAll();
        assertThat(actionBotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActionBots() throws Exception {
        // Initialize the database
        actionBotRepository.saveAndFlush(actionBot);

        // Get all the actionBotList
        restActionBotMockMvc.perform(get("/api/action-bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actionBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].command").value(hasItem(DEFAULT_COMMAND.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }
    
    @Test
    @Transactional
    public void getActionBot() throws Exception {
        // Initialize the database
        actionBotRepository.saveAndFlush(actionBot);

        // Get the actionBot
        restActionBotMockMvc.perform(get("/api/action-bots/{id}", actionBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actionBot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.command").value(DEFAULT_COMMAND.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActionBot() throws Exception {
        // Get the actionBot
        restActionBotMockMvc.perform(get("/api/action-bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActionBot() throws Exception {
        // Initialize the database
        actionBotRepository.saveAndFlush(actionBot);

        int databaseSizeBeforeUpdate = actionBotRepository.findAll().size();

        // Update the actionBot
        ActionBot updatedActionBot = actionBotRepository.findById(actionBot.getId()).get();
        // Disconnect from session so that the updates on updatedActionBot are not directly saved in db
        em.detach(updatedActionBot);
        updatedActionBot
            .name(UPDATED_NAME)
            .command(UPDATED_COMMAND)
            .role(UPDATED_ROLE);

        restActionBotMockMvc.perform(put("/api/action-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActionBot)))
            .andExpect(status().isOk());

        // Validate the ActionBot in the database
        List<ActionBot> actionBotList = actionBotRepository.findAll();
        assertThat(actionBotList).hasSize(databaseSizeBeforeUpdate);
        ActionBot testActionBot = actionBotList.get(actionBotList.size() - 1);
        assertThat(testActionBot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActionBot.getCommand()).isEqualTo(UPDATED_COMMAND);
        assertThat(testActionBot.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingActionBot() throws Exception {
        int databaseSizeBeforeUpdate = actionBotRepository.findAll().size();

        // Create the ActionBot

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionBotMockMvc.perform(put("/api/action-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionBot)))
            .andExpect(status().isBadRequest());

        // Validate the ActionBot in the database
        List<ActionBot> actionBotList = actionBotRepository.findAll();
        assertThat(actionBotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActionBot() throws Exception {
        // Initialize the database
        actionBotRepository.saveAndFlush(actionBot);

        int databaseSizeBeforeDelete = actionBotRepository.findAll().size();

        // Delete the actionBot
        restActionBotMockMvc.perform(delete("/api/action-bots/{id}", actionBot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActionBot> actionBotList = actionBotRepository.findAll();
        assertThat(actionBotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionBot.class);
        ActionBot actionBot1 = new ActionBot();
        actionBot1.setId(1L);
        ActionBot actionBot2 = new ActionBot();
        actionBot2.setId(actionBot1.getId());
        assertThat(actionBot1).isEqualTo(actionBot2);
        actionBot2.setId(2L);
        assertThat(actionBot1).isNotEqualTo(actionBot2);
        actionBot1.setId(null);
        assertThat(actionBot1).isNotEqualTo(actionBot2);
    }
}
