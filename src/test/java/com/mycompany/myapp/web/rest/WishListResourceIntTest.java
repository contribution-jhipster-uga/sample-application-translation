package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AutoTranslatedSampleApp;

import com.mycompany.myapp.domain.WishList;
import com.mycompany.myapp.repository.WishListRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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


import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WishListResource REST controller.
 *
 * @see WishListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AutoTranslatedSampleApp.class)
public class WishListResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESTRICTED = false;
    private static final Boolean UPDATED_RESTRICTED = true;

    @Autowired
    private WishListRepository wishListRepository;

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

    private MockMvc restWishListMockMvc;

    private WishList wishList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WishListResource wishListResource = new WishListResource(wishListRepository);
        this.restWishListMockMvc = MockMvcBuilders.standaloneSetup(wishListResource)
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
    public static WishList createEntity(EntityManager em) {
        WishList wishList = new WishList()
            .title(DEFAULT_TITLE)
            .restricted(DEFAULT_RESTRICTED);
        return wishList;
    }

    @Before
    public void initTest() {
        wishList = createEntity(em);
    }

    @Test
    @Transactional
    public void createWishList() throws Exception {
        int databaseSizeBeforeCreate = wishListRepository.findAll().size();

        // Create the WishList
        restWishListMockMvc.perform(post("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isCreated());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate + 1);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWishList.isRestricted()).isEqualTo(DEFAULT_RESTRICTED);
    }

    @Test
    @Transactional
    public void createWishListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wishListRepository.findAll().size();

        // Create the WishList with an existing ID
        wishList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishListMockMvc.perform(post("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = wishListRepository.findAll().size();
        // set the field null
        wishList.setTitle(null);

        // Create the WishList, which fails.

        restWishListMockMvc.perform(post("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isBadRequest());

        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWishLists() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get all the wishListList
        restWishListMockMvc.perform(get("/api/wish-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishList.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].restricted").value(hasItem(DEFAULT_RESTRICTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get the wishList
        restWishListMockMvc.perform(get("/api/wish-lists/{id}", wishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wishList.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.restricted").value(DEFAULT_RESTRICTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWishList() throws Exception {
        // Get the wishList
        restWishListMockMvc.perform(get("/api/wish-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList
        WishList updatedWishList = wishListRepository.findById(wishList.getId()).get();
        // Disconnect from session so that the updates on updatedWishList are not directly saved in db
        em.detach(updatedWishList);
        updatedWishList
            .title(UPDATED_TITLE)
            .restricted(UPDATED_RESTRICTED);

        restWishListMockMvc.perform(put("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWishList)))
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWishList.isRestricted()).isEqualTo(UPDATED_RESTRICTED);
    }

    @Test
    @Transactional
    public void updateNonExistingWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Create the WishList

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc.perform(put("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeDelete = wishListRepository.findAll().size();

        // Delete the wishList
        restWishListMockMvc.perform(delete("/api/wish-lists/{id}", wishList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WishList.class);
        WishList wishList1 = new WishList();
        wishList1.setId(1L);
        WishList wishList2 = new WishList();
        wishList2.setId(wishList1.getId());
        assertThat(wishList1).isEqualTo(wishList2);
        wishList2.setId(2L);
        assertThat(wishList1).isNotEqualTo(wishList2);
        wishList1.setId(null);
        assertThat(wishList1).isNotEqualTo(wishList2);
    }
}
