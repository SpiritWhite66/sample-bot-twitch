package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.OverWriteCalendar;
import io.bot.personnal.application.repository.OverWriteCalendarRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static io.bot.personnal.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OverWriteCalendarResource REST controller.
 *
 * @see OverWriteCalendarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class OverWriteCalendarResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private OverWriteCalendarRepository overWriteCalendarRepository;

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

    private MockMvc restOverWriteCalendarMockMvc;

    private OverWriteCalendar overWriteCalendar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OverWriteCalendarResource overWriteCalendarResource = new OverWriteCalendarResource(overWriteCalendarRepository);
        this.restOverWriteCalendarMockMvc = MockMvcBuilders.standaloneSetup(overWriteCalendarResource)
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
    public static OverWriteCalendar createEntity(EntityManager em) {
        OverWriteCalendar overWriteCalendar = new OverWriteCalendar()
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
        return overWriteCalendar;
    }

    @Before
    public void initTest() {
        overWriteCalendar = createEntity(em);
    }

    @Test
    @Transactional
    public void createOverWriteCalendar() throws Exception {
        int databaseSizeBeforeCreate = overWriteCalendarRepository.findAll().size();

        // Create the OverWriteCalendar
        restOverWriteCalendarMockMvc.perform(post("/api/over-write-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(overWriteCalendar)))
            .andExpect(status().isCreated());

        // Validate the OverWriteCalendar in the database
        List<OverWriteCalendar> overWriteCalendarList = overWriteCalendarRepository.findAll();
        assertThat(overWriteCalendarList).hasSize(databaseSizeBeforeCreate + 1);
        OverWriteCalendar testOverWriteCalendar = overWriteCalendarList.get(overWriteCalendarList.size() - 1);
        assertThat(testOverWriteCalendar.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOverWriteCalendar.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOverWriteCalendarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = overWriteCalendarRepository.findAll().size();

        // Create the OverWriteCalendar with an existing ID
        overWriteCalendar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOverWriteCalendarMockMvc.perform(post("/api/over-write-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(overWriteCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the OverWriteCalendar in the database
        List<OverWriteCalendar> overWriteCalendarList = overWriteCalendarRepository.findAll();
        assertThat(overWriteCalendarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOverWriteCalendars() throws Exception {
        // Initialize the database
        overWriteCalendarRepository.saveAndFlush(overWriteCalendar);

        // Get all the overWriteCalendarList
        restOverWriteCalendarMockMvc.perform(get("/api/over-write-calendars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(overWriteCalendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOverWriteCalendar() throws Exception {
        // Initialize the database
        overWriteCalendarRepository.saveAndFlush(overWriteCalendar);

        // Get the overWriteCalendar
        restOverWriteCalendarMockMvc.perform(get("/api/over-write-calendars/{id}", overWriteCalendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(overWriteCalendar.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOverWriteCalendar() throws Exception {
        // Get the overWriteCalendar
        restOverWriteCalendarMockMvc.perform(get("/api/over-write-calendars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOverWriteCalendar() throws Exception {
        // Initialize the database
        overWriteCalendarRepository.saveAndFlush(overWriteCalendar);

        int databaseSizeBeforeUpdate = overWriteCalendarRepository.findAll().size();

        // Update the overWriteCalendar
        OverWriteCalendar updatedOverWriteCalendar = overWriteCalendarRepository.findById(overWriteCalendar.getId()).get();
        // Disconnect from session so that the updates on updatedOverWriteCalendar are not directly saved in db
        em.detach(updatedOverWriteCalendar);
        updatedOverWriteCalendar
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restOverWriteCalendarMockMvc.perform(put("/api/over-write-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOverWriteCalendar)))
            .andExpect(status().isOk());

        // Validate the OverWriteCalendar in the database
        List<OverWriteCalendar> overWriteCalendarList = overWriteCalendarRepository.findAll();
        assertThat(overWriteCalendarList).hasSize(databaseSizeBeforeUpdate);
        OverWriteCalendar testOverWriteCalendar = overWriteCalendarList.get(overWriteCalendarList.size() - 1);
        assertThat(testOverWriteCalendar.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOverWriteCalendar.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingOverWriteCalendar() throws Exception {
        int databaseSizeBeforeUpdate = overWriteCalendarRepository.findAll().size();

        // Create the OverWriteCalendar

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOverWriteCalendarMockMvc.perform(put("/api/over-write-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(overWriteCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the OverWriteCalendar in the database
        List<OverWriteCalendar> overWriteCalendarList = overWriteCalendarRepository.findAll();
        assertThat(overWriteCalendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOverWriteCalendar() throws Exception {
        // Initialize the database
        overWriteCalendarRepository.saveAndFlush(overWriteCalendar);

        int databaseSizeBeforeDelete = overWriteCalendarRepository.findAll().size();

        // Delete the overWriteCalendar
        restOverWriteCalendarMockMvc.perform(delete("/api/over-write-calendars/{id}", overWriteCalendar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OverWriteCalendar> overWriteCalendarList = overWriteCalendarRepository.findAll();
        assertThat(overWriteCalendarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OverWriteCalendar.class);
        OverWriteCalendar overWriteCalendar1 = new OverWriteCalendar();
        overWriteCalendar1.setId(1L);
        OverWriteCalendar overWriteCalendar2 = new OverWriteCalendar();
        overWriteCalendar2.setId(overWriteCalendar1.getId());
        assertThat(overWriteCalendar1).isEqualTo(overWriteCalendar2);
        overWriteCalendar2.setId(2L);
        assertThat(overWriteCalendar1).isNotEqualTo(overWriteCalendar2);
        overWriteCalendar1.setId(null);
        assertThat(overWriteCalendar1).isNotEqualTo(overWriteCalendar2);
    }
}
