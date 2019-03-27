package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.StreamCalendar;
import io.bot.personnal.application.repository.StreamCalendarRepository;
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
 * Test class for the StreamCalendarResource REST controller.
 *
 * @see StreamCalendarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class StreamCalendarResourceIntTest {

    private static final Boolean DEFAULT_ACTIVATE = false;
    private static final Boolean UPDATED_ACTIVATE = true;

    @Autowired
    private StreamCalendarRepository streamCalendarRepository;

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

    private MockMvc restStreamCalendarMockMvc;

    private StreamCalendar streamCalendar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StreamCalendarResource streamCalendarResource = new StreamCalendarResource(streamCalendarRepository);
        this.restStreamCalendarMockMvc = MockMvcBuilders.standaloneSetup(streamCalendarResource)
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
    public static StreamCalendar createEntity(EntityManager em) {
        StreamCalendar streamCalendar = new StreamCalendar()
            .activate(DEFAULT_ACTIVATE);
        return streamCalendar;
    }

    @Before
    public void initTest() {
        streamCalendar = createEntity(em);
    }

    @Test
    @Transactional
    public void createStreamCalendar() throws Exception {
        int databaseSizeBeforeCreate = streamCalendarRepository.findAll().size();

        // Create the StreamCalendar
        restStreamCalendarMockMvc.perform(post("/api/stream-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streamCalendar)))
            .andExpect(status().isCreated());

        // Validate the StreamCalendar in the database
        List<StreamCalendar> streamCalendarList = streamCalendarRepository.findAll();
        assertThat(streamCalendarList).hasSize(databaseSizeBeforeCreate + 1);
        StreamCalendar testStreamCalendar = streamCalendarList.get(streamCalendarList.size() - 1);
        assertThat(testStreamCalendar.isActivate()).isEqualTo(DEFAULT_ACTIVATE);
    }

    @Test
    @Transactional
    public void createStreamCalendarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = streamCalendarRepository.findAll().size();

        // Create the StreamCalendar with an existing ID
        streamCalendar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStreamCalendarMockMvc.perform(post("/api/stream-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streamCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the StreamCalendar in the database
        List<StreamCalendar> streamCalendarList = streamCalendarRepository.findAll();
        assertThat(streamCalendarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStreamCalendars() throws Exception {
        // Initialize the database
        streamCalendarRepository.saveAndFlush(streamCalendar);

        // Get all the streamCalendarList
        restStreamCalendarMockMvc.perform(get("/api/stream-calendars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(streamCalendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].activate").value(hasItem(DEFAULT_ACTIVATE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getStreamCalendar() throws Exception {
        // Initialize the database
        streamCalendarRepository.saveAndFlush(streamCalendar);

        // Get the streamCalendar
        restStreamCalendarMockMvc.perform(get("/api/stream-calendars/{id}", streamCalendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(streamCalendar.getId().intValue()))
            .andExpect(jsonPath("$.activate").value(DEFAULT_ACTIVATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStreamCalendar() throws Exception {
        // Get the streamCalendar
        restStreamCalendarMockMvc.perform(get("/api/stream-calendars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStreamCalendar() throws Exception {
        // Initialize the database
        streamCalendarRepository.saveAndFlush(streamCalendar);

        int databaseSizeBeforeUpdate = streamCalendarRepository.findAll().size();

        // Update the streamCalendar
        StreamCalendar updatedStreamCalendar = streamCalendarRepository.findById(streamCalendar.getId()).get();
        // Disconnect from session so that the updates on updatedStreamCalendar are not directly saved in db
        em.detach(updatedStreamCalendar);
        updatedStreamCalendar
            .activate(UPDATED_ACTIVATE);

        restStreamCalendarMockMvc.perform(put("/api/stream-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStreamCalendar)))
            .andExpect(status().isOk());

        // Validate the StreamCalendar in the database
        List<StreamCalendar> streamCalendarList = streamCalendarRepository.findAll();
        assertThat(streamCalendarList).hasSize(databaseSizeBeforeUpdate);
        StreamCalendar testStreamCalendar = streamCalendarList.get(streamCalendarList.size() - 1);
        assertThat(testStreamCalendar.isActivate()).isEqualTo(UPDATED_ACTIVATE);
    }

    @Test
    @Transactional
    public void updateNonExistingStreamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = streamCalendarRepository.findAll().size();

        // Create the StreamCalendar

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStreamCalendarMockMvc.perform(put("/api/stream-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streamCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the StreamCalendar in the database
        List<StreamCalendar> streamCalendarList = streamCalendarRepository.findAll();
        assertThat(streamCalendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStreamCalendar() throws Exception {
        // Initialize the database
        streamCalendarRepository.saveAndFlush(streamCalendar);

        int databaseSizeBeforeDelete = streamCalendarRepository.findAll().size();

        // Delete the streamCalendar
        restStreamCalendarMockMvc.perform(delete("/api/stream-calendars/{id}", streamCalendar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StreamCalendar> streamCalendarList = streamCalendarRepository.findAll();
        assertThat(streamCalendarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StreamCalendar.class);
        StreamCalendar streamCalendar1 = new StreamCalendar();
        streamCalendar1.setId(1L);
        StreamCalendar streamCalendar2 = new StreamCalendar();
        streamCalendar2.setId(streamCalendar1.getId());
        assertThat(streamCalendar1).isEqualTo(streamCalendar2);
        streamCalendar2.setId(2L);
        assertThat(streamCalendar1).isNotEqualTo(streamCalendar2);
        streamCalendar1.setId(null);
        assertThat(streamCalendar1).isNotEqualTo(streamCalendar2);
    }
}
