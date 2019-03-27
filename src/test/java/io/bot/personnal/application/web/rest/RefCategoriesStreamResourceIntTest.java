package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.RefCategoriesStream;
import io.bot.personnal.application.repository.RefCategoriesStreamRepository;
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
 * Test class for the RefCategoriesStreamResource REST controller.
 *
 * @see RefCategoriesStreamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class RefCategoriesStreamResourceIntTest {

    private static final String DEFAULT_CODE_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private RefCategoriesStreamRepository refCategoriesStreamRepository;

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

    private MockMvc restRefCategoriesStreamMockMvc;

    private RefCategoriesStream refCategoriesStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RefCategoriesStreamResource refCategoriesStreamResource = new RefCategoriesStreamResource(refCategoriesStreamRepository);
        this.restRefCategoriesStreamMockMvc = MockMvcBuilders.standaloneSetup(refCategoriesStreamResource)
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
    public static RefCategoriesStream createEntity(EntityManager em) {
        RefCategoriesStream refCategoriesStream = new RefCategoriesStream()
            .codeCategory(DEFAULT_CODE_CATEGORY)
            .labelCategory(DEFAULT_LABEL_CATEGORY);
        return refCategoriesStream;
    }

    @Before
    public void initTest() {
        refCategoriesStream = createEntity(em);
    }

    @Test
    @Transactional
    public void createRefCategoriesStream() throws Exception {
        int databaseSizeBeforeCreate = refCategoriesStreamRepository.findAll().size();

        // Create the RefCategoriesStream
        restRefCategoriesStreamMockMvc.perform(post("/api/ref-categories-streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(refCategoriesStream)))
            .andExpect(status().isCreated());

        // Validate the RefCategoriesStream in the database
        List<RefCategoriesStream> refCategoriesStreamList = refCategoriesStreamRepository.findAll();
        assertThat(refCategoriesStreamList).hasSize(databaseSizeBeforeCreate + 1);
        RefCategoriesStream testRefCategoriesStream = refCategoriesStreamList.get(refCategoriesStreamList.size() - 1);
        assertThat(testRefCategoriesStream.getCodeCategory()).isEqualTo(DEFAULT_CODE_CATEGORY);
        assertThat(testRefCategoriesStream.getLabelCategory()).isEqualTo(DEFAULT_LABEL_CATEGORY);
    }

    @Test
    @Transactional
    public void createRefCategoriesStreamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = refCategoriesStreamRepository.findAll().size();

        // Create the RefCategoriesStream with an existing ID
        refCategoriesStream.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefCategoriesStreamMockMvc.perform(post("/api/ref-categories-streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(refCategoriesStream)))
            .andExpect(status().isBadRequest());

        // Validate the RefCategoriesStream in the database
        List<RefCategoriesStream> refCategoriesStreamList = refCategoriesStreamRepository.findAll();
        assertThat(refCategoriesStreamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRefCategoriesStreams() throws Exception {
        // Initialize the database
        refCategoriesStreamRepository.saveAndFlush(refCategoriesStream);

        // Get all the refCategoriesStreamList
        restRefCategoriesStreamMockMvc.perform(get("/api/ref-categories-streams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refCategoriesStream.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeCategory").value(hasItem(DEFAULT_CODE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].labelCategory").value(hasItem(DEFAULT_LABEL_CATEGORY.toString())));
    }
    
    @Test
    @Transactional
    public void getRefCategoriesStream() throws Exception {
        // Initialize the database
        refCategoriesStreamRepository.saveAndFlush(refCategoriesStream);

        // Get the refCategoriesStream
        restRefCategoriesStreamMockMvc.perform(get("/api/ref-categories-streams/{id}", refCategoriesStream.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(refCategoriesStream.getId().intValue()))
            .andExpect(jsonPath("$.codeCategory").value(DEFAULT_CODE_CATEGORY.toString()))
            .andExpect(jsonPath("$.labelCategory").value(DEFAULT_LABEL_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRefCategoriesStream() throws Exception {
        // Get the refCategoriesStream
        restRefCategoriesStreamMockMvc.perform(get("/api/ref-categories-streams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRefCategoriesStream() throws Exception {
        // Initialize the database
        refCategoriesStreamRepository.saveAndFlush(refCategoriesStream);

        int databaseSizeBeforeUpdate = refCategoriesStreamRepository.findAll().size();

        // Update the refCategoriesStream
        RefCategoriesStream updatedRefCategoriesStream = refCategoriesStreamRepository.findById(refCategoriesStream.getId()).get();
        // Disconnect from session so that the updates on updatedRefCategoriesStream are not directly saved in db
        em.detach(updatedRefCategoriesStream);
        updatedRefCategoriesStream
            .codeCategory(UPDATED_CODE_CATEGORY)
            .labelCategory(UPDATED_LABEL_CATEGORY);

        restRefCategoriesStreamMockMvc.perform(put("/api/ref-categories-streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRefCategoriesStream)))
            .andExpect(status().isOk());

        // Validate the RefCategoriesStream in the database
        List<RefCategoriesStream> refCategoriesStreamList = refCategoriesStreamRepository.findAll();
        assertThat(refCategoriesStreamList).hasSize(databaseSizeBeforeUpdate);
        RefCategoriesStream testRefCategoriesStream = refCategoriesStreamList.get(refCategoriesStreamList.size() - 1);
        assertThat(testRefCategoriesStream.getCodeCategory()).isEqualTo(UPDATED_CODE_CATEGORY);
        assertThat(testRefCategoriesStream.getLabelCategory()).isEqualTo(UPDATED_LABEL_CATEGORY);
    }

    @Test
    @Transactional
    public void updateNonExistingRefCategoriesStream() throws Exception {
        int databaseSizeBeforeUpdate = refCategoriesStreamRepository.findAll().size();

        // Create the RefCategoriesStream

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefCategoriesStreamMockMvc.perform(put("/api/ref-categories-streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(refCategoriesStream)))
            .andExpect(status().isBadRequest());

        // Validate the RefCategoriesStream in the database
        List<RefCategoriesStream> refCategoriesStreamList = refCategoriesStreamRepository.findAll();
        assertThat(refCategoriesStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRefCategoriesStream() throws Exception {
        // Initialize the database
        refCategoriesStreamRepository.saveAndFlush(refCategoriesStream);

        int databaseSizeBeforeDelete = refCategoriesStreamRepository.findAll().size();

        // Delete the refCategoriesStream
        restRefCategoriesStreamMockMvc.perform(delete("/api/ref-categories-streams/{id}", refCategoriesStream.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RefCategoriesStream> refCategoriesStreamList = refCategoriesStreamRepository.findAll();
        assertThat(refCategoriesStreamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefCategoriesStream.class);
        RefCategoriesStream refCategoriesStream1 = new RefCategoriesStream();
        refCategoriesStream1.setId(1L);
        RefCategoriesStream refCategoriesStream2 = new RefCategoriesStream();
        refCategoriesStream2.setId(refCategoriesStream1.getId());
        assertThat(refCategoriesStream1).isEqualTo(refCategoriesStream2);
        refCategoriesStream2.setId(2L);
        assertThat(refCategoriesStream1).isNotEqualTo(refCategoriesStream2);
        refCategoriesStream1.setId(null);
        assertThat(refCategoriesStream1).isNotEqualTo(refCategoriesStream2);
    }
}
