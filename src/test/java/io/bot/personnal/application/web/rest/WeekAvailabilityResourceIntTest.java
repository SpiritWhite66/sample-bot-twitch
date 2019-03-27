package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.WeekAvailability;
import io.bot.personnal.application.repository.WeekAvailabilityRepository;
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
 * Test class for the WeekAvailabilityResource REST controller.
 *
 * @see WeekAvailabilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class WeekAvailabilityResourceIntTest {

    @Autowired
    private WeekAvailabilityRepository weekAvailabilityRepository;

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

    private MockMvc restWeekAvailabilityMockMvc;

    private WeekAvailability weekAvailability;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeekAvailabilityResource weekAvailabilityResource = new WeekAvailabilityResource(weekAvailabilityRepository);
        this.restWeekAvailabilityMockMvc = MockMvcBuilders.standaloneSetup(weekAvailabilityResource)
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
    public static WeekAvailability createEntity(EntityManager em) {
        WeekAvailability weekAvailability = new WeekAvailability();
        return weekAvailability;
    }

    @Before
    public void initTest() {
        weekAvailability = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeekAvailability() throws Exception {
        int databaseSizeBeforeCreate = weekAvailabilityRepository.findAll().size();

        // Create the WeekAvailability
        restWeekAvailabilityMockMvc.perform(post("/api/week-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weekAvailability)))
            .andExpect(status().isCreated());

        // Validate the WeekAvailability in the database
        List<WeekAvailability> weekAvailabilityList = weekAvailabilityRepository.findAll();
        assertThat(weekAvailabilityList).hasSize(databaseSizeBeforeCreate + 1);
        WeekAvailability testWeekAvailability = weekAvailabilityList.get(weekAvailabilityList.size() - 1);
    }

    @Test
    @Transactional
    public void createWeekAvailabilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weekAvailabilityRepository.findAll().size();

        // Create the WeekAvailability with an existing ID
        weekAvailability.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeekAvailabilityMockMvc.perform(post("/api/week-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weekAvailability)))
            .andExpect(status().isBadRequest());

        // Validate the WeekAvailability in the database
        List<WeekAvailability> weekAvailabilityList = weekAvailabilityRepository.findAll();
        assertThat(weekAvailabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWeekAvailabilities() throws Exception {
        // Initialize the database
        weekAvailabilityRepository.saveAndFlush(weekAvailability);

        // Get all the weekAvailabilityList
        restWeekAvailabilityMockMvc.perform(get("/api/week-availabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weekAvailability.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getWeekAvailability() throws Exception {
        // Initialize the database
        weekAvailabilityRepository.saveAndFlush(weekAvailability);

        // Get the weekAvailability
        restWeekAvailabilityMockMvc.perform(get("/api/week-availabilities/{id}", weekAvailability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weekAvailability.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWeekAvailability() throws Exception {
        // Get the weekAvailability
        restWeekAvailabilityMockMvc.perform(get("/api/week-availabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeekAvailability() throws Exception {
        // Initialize the database
        weekAvailabilityRepository.saveAndFlush(weekAvailability);

        int databaseSizeBeforeUpdate = weekAvailabilityRepository.findAll().size();

        // Update the weekAvailability
        WeekAvailability updatedWeekAvailability = weekAvailabilityRepository.findById(weekAvailability.getId()).get();
        // Disconnect from session so that the updates on updatedWeekAvailability are not directly saved in db
        em.detach(updatedWeekAvailability);

        restWeekAvailabilityMockMvc.perform(put("/api/week-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWeekAvailability)))
            .andExpect(status().isOk());

        // Validate the WeekAvailability in the database
        List<WeekAvailability> weekAvailabilityList = weekAvailabilityRepository.findAll();
        assertThat(weekAvailabilityList).hasSize(databaseSizeBeforeUpdate);
        WeekAvailability testWeekAvailability = weekAvailabilityList.get(weekAvailabilityList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingWeekAvailability() throws Exception {
        int databaseSizeBeforeUpdate = weekAvailabilityRepository.findAll().size();

        // Create the WeekAvailability

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeekAvailabilityMockMvc.perform(put("/api/week-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weekAvailability)))
            .andExpect(status().isBadRequest());

        // Validate the WeekAvailability in the database
        List<WeekAvailability> weekAvailabilityList = weekAvailabilityRepository.findAll();
        assertThat(weekAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWeekAvailability() throws Exception {
        // Initialize the database
        weekAvailabilityRepository.saveAndFlush(weekAvailability);

        int databaseSizeBeforeDelete = weekAvailabilityRepository.findAll().size();

        // Delete the weekAvailability
        restWeekAvailabilityMockMvc.perform(delete("/api/week-availabilities/{id}", weekAvailability.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WeekAvailability> weekAvailabilityList = weekAvailabilityRepository.findAll();
        assertThat(weekAvailabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeekAvailability.class);
        WeekAvailability weekAvailability1 = new WeekAvailability();
        weekAvailability1.setId(1L);
        WeekAvailability weekAvailability2 = new WeekAvailability();
        weekAvailability2.setId(weekAvailability1.getId());
        assertThat(weekAvailability1).isEqualTo(weekAvailability2);
        weekAvailability2.setId(2L);
        assertThat(weekAvailability1).isNotEqualTo(weekAvailability2);
        weekAvailability1.setId(null);
        assertThat(weekAvailability1).isNotEqualTo(weekAvailability2);
    }
}
