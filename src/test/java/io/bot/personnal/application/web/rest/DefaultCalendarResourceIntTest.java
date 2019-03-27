package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.DefaultCalendar;
import io.bot.personnal.application.repository.DefaultCalendarRepository;
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
 * Test class for the DefaultCalendarResource REST controller.
 *
 * @see DefaultCalendarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class DefaultCalendarResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private DefaultCalendarRepository defaultCalendarRepository;

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

    private MockMvc restDefaultCalendarMockMvc;

    private DefaultCalendar defaultCalendar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DefaultCalendarResource defaultCalendarResource = new DefaultCalendarResource(defaultCalendarRepository);
        this.restDefaultCalendarMockMvc = MockMvcBuilders.standaloneSetup(defaultCalendarResource)
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
    public static DefaultCalendar createEntity(EntityManager em) {
        DefaultCalendar defaultCalendar = new DefaultCalendar()
            .date(DEFAULT_DATE)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE);
        return defaultCalendar;
    }

    @Before
    public void initTest() {
        defaultCalendar = createEntity(em);
    }

    @Test
    @Transactional
    public void createDefaultCalendar() throws Exception {
        int databaseSizeBeforeCreate = defaultCalendarRepository.findAll().size();

        // Create the DefaultCalendar
        restDefaultCalendarMockMvc.perform(post("/api/default-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(defaultCalendar)))
            .andExpect(status().isCreated());

        // Validate the DefaultCalendar in the database
        List<DefaultCalendar> defaultCalendarList = defaultCalendarRepository.findAll();
        assertThat(defaultCalendarList).hasSize(databaseSizeBeforeCreate + 1);
        DefaultCalendar testDefaultCalendar = defaultCalendarList.get(defaultCalendarList.size() - 1);
        assertThat(testDefaultCalendar.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDefaultCalendar.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDefaultCalendar.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createDefaultCalendarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = defaultCalendarRepository.findAll().size();

        // Create the DefaultCalendar with an existing ID
        defaultCalendar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDefaultCalendarMockMvc.perform(post("/api/default-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(defaultCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the DefaultCalendar in the database
        List<DefaultCalendar> defaultCalendarList = defaultCalendarRepository.findAll();
        assertThat(defaultCalendarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDefaultCalendars() throws Exception {
        // Initialize the database
        defaultCalendarRepository.saveAndFlush(defaultCalendar);

        // Get all the defaultCalendarList
        restDefaultCalendarMockMvc.perform(get("/api/default-calendars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(defaultCalendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getDefaultCalendar() throws Exception {
        // Initialize the database
        defaultCalendarRepository.saveAndFlush(defaultCalendar);

        // Get the defaultCalendar
        restDefaultCalendarMockMvc.perform(get("/api/default-calendars/{id}", defaultCalendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(defaultCalendar.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDefaultCalendar() throws Exception {
        // Get the defaultCalendar
        restDefaultCalendarMockMvc.perform(get("/api/default-calendars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDefaultCalendar() throws Exception {
        // Initialize the database
        defaultCalendarRepository.saveAndFlush(defaultCalendar);

        int databaseSizeBeforeUpdate = defaultCalendarRepository.findAll().size();

        // Update the defaultCalendar
        DefaultCalendar updatedDefaultCalendar = defaultCalendarRepository.findById(defaultCalendar.getId()).get();
        // Disconnect from session so that the updates on updatedDefaultCalendar are not directly saved in db
        em.detach(updatedDefaultCalendar);
        updatedDefaultCalendar
            .date(UPDATED_DATE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE);

        restDefaultCalendarMockMvc.perform(put("/api/default-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDefaultCalendar)))
            .andExpect(status().isOk());

        // Validate the DefaultCalendar in the database
        List<DefaultCalendar> defaultCalendarList = defaultCalendarRepository.findAll();
        assertThat(defaultCalendarList).hasSize(databaseSizeBeforeUpdate);
        DefaultCalendar testDefaultCalendar = defaultCalendarList.get(defaultCalendarList.size() - 1);
        assertThat(testDefaultCalendar.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDefaultCalendar.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDefaultCalendar.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingDefaultCalendar() throws Exception {
        int databaseSizeBeforeUpdate = defaultCalendarRepository.findAll().size();

        // Create the DefaultCalendar

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultCalendarMockMvc.perform(put("/api/default-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(defaultCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the DefaultCalendar in the database
        List<DefaultCalendar> defaultCalendarList = defaultCalendarRepository.findAll();
        assertThat(defaultCalendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDefaultCalendar() throws Exception {
        // Initialize the database
        defaultCalendarRepository.saveAndFlush(defaultCalendar);

        int databaseSizeBeforeDelete = defaultCalendarRepository.findAll().size();

        // Delete the defaultCalendar
        restDefaultCalendarMockMvc.perform(delete("/api/default-calendars/{id}", defaultCalendar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DefaultCalendar> defaultCalendarList = defaultCalendarRepository.findAll();
        assertThat(defaultCalendarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DefaultCalendar.class);
        DefaultCalendar defaultCalendar1 = new DefaultCalendar();
        defaultCalendar1.setId(1L);
        DefaultCalendar defaultCalendar2 = new DefaultCalendar();
        defaultCalendar2.setId(defaultCalendar1.getId());
        assertThat(defaultCalendar1).isEqualTo(defaultCalendar2);
        defaultCalendar2.setId(2L);
        assertThat(defaultCalendar1).isNotEqualTo(defaultCalendar2);
        defaultCalendar1.setId(null);
        assertThat(defaultCalendar1).isNotEqualTo(defaultCalendar2);
    }
}
