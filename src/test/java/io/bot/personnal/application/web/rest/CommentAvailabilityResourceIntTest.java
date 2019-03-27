package io.bot.personnal.application.web.rest;

import io.bot.personnal.application.SampleBotTwitchApp;

import io.bot.personnal.application.domain.CommentAvailability;
import io.bot.personnal.application.repository.CommentAvailabilityRepository;
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
 * Test class for the CommentAvailabilityResource REST controller.
 *
 * @see CommentAvailabilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBotTwitchApp.class)
public class CommentAvailabilityResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private CommentAvailabilityRepository commentAvailabilityRepository;

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

    private MockMvc restCommentAvailabilityMockMvc;

    private CommentAvailability commentAvailability;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommentAvailabilityResource commentAvailabilityResource = new CommentAvailabilityResource(commentAvailabilityRepository);
        this.restCommentAvailabilityMockMvc = MockMvcBuilders.standaloneSetup(commentAvailabilityResource)
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
    public static CommentAvailability createEntity(EntityManager em) {
        CommentAvailability commentAvailability = new CommentAvailability()
            .comment(DEFAULT_COMMENT);
        return commentAvailability;
    }

    @Before
    public void initTest() {
        commentAvailability = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommentAvailability() throws Exception {
        int databaseSizeBeforeCreate = commentAvailabilityRepository.findAll().size();

        // Create the CommentAvailability
        restCommentAvailabilityMockMvc.perform(post("/api/comment-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentAvailability)))
            .andExpect(status().isCreated());

        // Validate the CommentAvailability in the database
        List<CommentAvailability> commentAvailabilityList = commentAvailabilityRepository.findAll();
        assertThat(commentAvailabilityList).hasSize(databaseSizeBeforeCreate + 1);
        CommentAvailability testCommentAvailability = commentAvailabilityList.get(commentAvailabilityList.size() - 1);
        assertThat(testCommentAvailability.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createCommentAvailabilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentAvailabilityRepository.findAll().size();

        // Create the CommentAvailability with an existing ID
        commentAvailability.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentAvailabilityMockMvc.perform(post("/api/comment-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentAvailability)))
            .andExpect(status().isBadRequest());

        // Validate the CommentAvailability in the database
        List<CommentAvailability> commentAvailabilityList = commentAvailabilityRepository.findAll();
        assertThat(commentAvailabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCommentAvailabilities() throws Exception {
        // Initialize the database
        commentAvailabilityRepository.saveAndFlush(commentAvailability);

        // Get all the commentAvailabilityList
        restCommentAvailabilityMockMvc.perform(get("/api/comment-availabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentAvailability.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getCommentAvailability() throws Exception {
        // Initialize the database
        commentAvailabilityRepository.saveAndFlush(commentAvailability);

        // Get the commentAvailability
        restCommentAvailabilityMockMvc.perform(get("/api/comment-availabilities/{id}", commentAvailability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commentAvailability.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommentAvailability() throws Exception {
        // Get the commentAvailability
        restCommentAvailabilityMockMvc.perform(get("/api/comment-availabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentAvailability() throws Exception {
        // Initialize the database
        commentAvailabilityRepository.saveAndFlush(commentAvailability);

        int databaseSizeBeforeUpdate = commentAvailabilityRepository.findAll().size();

        // Update the commentAvailability
        CommentAvailability updatedCommentAvailability = commentAvailabilityRepository.findById(commentAvailability.getId()).get();
        // Disconnect from session so that the updates on updatedCommentAvailability are not directly saved in db
        em.detach(updatedCommentAvailability);
        updatedCommentAvailability
            .comment(UPDATED_COMMENT);

        restCommentAvailabilityMockMvc.perform(put("/api/comment-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommentAvailability)))
            .andExpect(status().isOk());

        // Validate the CommentAvailability in the database
        List<CommentAvailability> commentAvailabilityList = commentAvailabilityRepository.findAll();
        assertThat(commentAvailabilityList).hasSize(databaseSizeBeforeUpdate);
        CommentAvailability testCommentAvailability = commentAvailabilityList.get(commentAvailabilityList.size() - 1);
        assertThat(testCommentAvailability.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingCommentAvailability() throws Exception {
        int databaseSizeBeforeUpdate = commentAvailabilityRepository.findAll().size();

        // Create the CommentAvailability

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentAvailabilityMockMvc.perform(put("/api/comment-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentAvailability)))
            .andExpect(status().isBadRequest());

        // Validate the CommentAvailability in the database
        List<CommentAvailability> commentAvailabilityList = commentAvailabilityRepository.findAll();
        assertThat(commentAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommentAvailability() throws Exception {
        // Initialize the database
        commentAvailabilityRepository.saveAndFlush(commentAvailability);

        int databaseSizeBeforeDelete = commentAvailabilityRepository.findAll().size();

        // Delete the commentAvailability
        restCommentAvailabilityMockMvc.perform(delete("/api/comment-availabilities/{id}", commentAvailability.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CommentAvailability> commentAvailabilityList = commentAvailabilityRepository.findAll();
        assertThat(commentAvailabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentAvailability.class);
        CommentAvailability commentAvailability1 = new CommentAvailability();
        commentAvailability1.setId(1L);
        CommentAvailability commentAvailability2 = new CommentAvailability();
        commentAvailability2.setId(commentAvailability1.getId());
        assertThat(commentAvailability1).isEqualTo(commentAvailability2);
        commentAvailability2.setId(2L);
        assertThat(commentAvailability1).isNotEqualTo(commentAvailability2);
        commentAvailability1.setId(null);
        assertThat(commentAvailability1).isNotEqualTo(commentAvailability2);
    }
}
