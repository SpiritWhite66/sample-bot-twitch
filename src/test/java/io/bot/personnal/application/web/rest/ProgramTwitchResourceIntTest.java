package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.ProgramTwitch;
import io.bot.personnal.application.repository.ProgramTwitchRepository;
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
 * Test class for the ProgramTwitchResource REST controller.
 *
 * @see ProgramTwitchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class ProgramTwitchResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Autowired
    private ProgramTwitchRepository programTwitchRepository;

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

    private MockMvc restProgramTwitchMockMvc;

    private ProgramTwitch programTwitch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramTwitchResource programTwitchResource = new ProgramTwitchResource(programTwitchRepository);
        this.restProgramTwitchMockMvc = MockMvcBuilders.standaloneSetup(programTwitchResource)
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
    public static ProgramTwitch createEntity(EntityManager em) {
        ProgramTwitch programTwitch = new ProgramTwitch()
            .name(DEFAULT_NAME)
            .link(DEFAULT_LINK);
        return programTwitch;
    }

    @Before
    public void initTest() {
        programTwitch = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgramTwitch() throws Exception {
        int databaseSizeBeforeCreate = programTwitchRepository.findAll().size();

        // Create the ProgramTwitch
        restProgramTwitchMockMvc.perform(post("/api/program-twitches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programTwitch)))
            .andExpect(status().isCreated());

        // Validate the ProgramTwitch in the database
        List<ProgramTwitch> programTwitchList = programTwitchRepository.findAll();
        assertThat(programTwitchList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramTwitch testProgramTwitch = programTwitchList.get(programTwitchList.size() - 1);
        assertThat(testProgramTwitch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgramTwitch.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createProgramTwitchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programTwitchRepository.findAll().size();

        // Create the ProgramTwitch with an existing ID
        programTwitch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramTwitchMockMvc.perform(post("/api/program-twitches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programTwitch)))
            .andExpect(status().isBadRequest());

        // Validate the ProgramTwitch in the database
        List<ProgramTwitch> programTwitchList = programTwitchRepository.findAll();
        assertThat(programTwitchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProgramTwitches() throws Exception {
        // Initialize the database
        programTwitchRepository.saveAndFlush(programTwitch);

        // Get all the programTwitchList
        restProgramTwitchMockMvc.perform(get("/api/program-twitches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programTwitch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }
    
    @Test
    @Transactional
    public void getProgramTwitch() throws Exception {
        // Initialize the database
        programTwitchRepository.saveAndFlush(programTwitch);

        // Get the programTwitch
        restProgramTwitchMockMvc.perform(get("/api/program-twitches/{id}", programTwitch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(programTwitch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProgramTwitch() throws Exception {
        // Get the programTwitch
        restProgramTwitchMockMvc.perform(get("/api/program-twitches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgramTwitch() throws Exception {
        // Initialize the database
        programTwitchRepository.saveAndFlush(programTwitch);

        int databaseSizeBeforeUpdate = programTwitchRepository.findAll().size();

        // Update the programTwitch
        ProgramTwitch updatedProgramTwitch = programTwitchRepository.findById(programTwitch.getId()).get();
        // Disconnect from session so that the updates on updatedProgramTwitch are not directly saved in db
        em.detach(updatedProgramTwitch);
        updatedProgramTwitch
            .name(UPDATED_NAME)
            .link(UPDATED_LINK);

        restProgramTwitchMockMvc.perform(put("/api/program-twitches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgramTwitch)))
            .andExpect(status().isOk());

        // Validate the ProgramTwitch in the database
        List<ProgramTwitch> programTwitchList = programTwitchRepository.findAll();
        assertThat(programTwitchList).hasSize(databaseSizeBeforeUpdate);
        ProgramTwitch testProgramTwitch = programTwitchList.get(programTwitchList.size() - 1);
        assertThat(testProgramTwitch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramTwitch.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void updateNonExistingProgramTwitch() throws Exception {
        int databaseSizeBeforeUpdate = programTwitchRepository.findAll().size();

        // Create the ProgramTwitch

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramTwitchMockMvc.perform(put("/api/program-twitches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programTwitch)))
            .andExpect(status().isBadRequest());

        // Validate the ProgramTwitch in the database
        List<ProgramTwitch> programTwitchList = programTwitchRepository.findAll();
        assertThat(programTwitchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProgramTwitch() throws Exception {
        // Initialize the database
        programTwitchRepository.saveAndFlush(programTwitch);

        int databaseSizeBeforeDelete = programTwitchRepository.findAll().size();

        // Delete the programTwitch
        restProgramTwitchMockMvc.perform(delete("/api/program-twitches/{id}", programTwitch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProgramTwitch> programTwitchList = programTwitchRepository.findAll();
        assertThat(programTwitchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramTwitch.class);
        ProgramTwitch programTwitch1 = new ProgramTwitch();
        programTwitch1.setId(1L);
        ProgramTwitch programTwitch2 = new ProgramTwitch();
        programTwitch2.setId(programTwitch1.getId());
        assertThat(programTwitch1).isEqualTo(programTwitch2);
        programTwitch2.setId(2L);
        assertThat(programTwitch1).isNotEqualTo(programTwitch2);
        programTwitch1.setId(null);
        assertThat(programTwitch1).isNotEqualTo(programTwitch2);
    }
}
