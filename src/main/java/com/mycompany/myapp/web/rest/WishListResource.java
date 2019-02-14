package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.WishList;
import com.mycompany.myapp.repository.WishListRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WishList.
 */
@RestController
@RequestMapping("/api")
public class WishListResource {

    private final Logger log = LoggerFactory.getLogger(WishListResource.class);

    private static final String ENTITY_NAME = "wishList";

    private final WishListRepository wishListRepository;

    public WishListResource(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    /**
     * POST  /wish-lists : Create a new wishList.
     *
     * @param wishList the wishList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wishList, or with status 400 (Bad Request) if the wishList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wish-lists")
    public ResponseEntity<WishList> createWishList(@Valid @RequestBody WishList wishList) throws URISyntaxException {
        log.debug("REST request to save WishList : {}", wishList);
        if (wishList.getId() != null) {
            throw new BadRequestAlertException("A new wishList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WishList result = wishListRepository.save(wishList);
        return ResponseEntity.created(new URI("/api/wish-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wish-lists : Updates an existing wishList.
     *
     * @param wishList the wishList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wishList,
     * or with status 400 (Bad Request) if the wishList is not valid,
     * or with status 500 (Internal Server Error) if the wishList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wish-lists")
    public ResponseEntity<WishList> updateWishList(@Valid @RequestBody WishList wishList) throws URISyntaxException {
        log.debug("REST request to update WishList : {}", wishList);
        if (wishList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WishList result = wishListRepository.save(wishList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wishList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wish-lists : get all the wishLists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wishLists in body
     */
    @GetMapping("/wish-lists")
    public List<WishList> getAllWishLists() {
        log.debug("REST request to get all WishLists");
        return wishListRepository.findAll();
    }

    /**
     * GET  /wish-lists/:id : get the "id" wishList.
     *
     * @param id the id of the wishList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wishList, or with status 404 (Not Found)
     */
    @GetMapping("/wish-lists/{id}")
    public ResponseEntity<WishList> getWishList(@PathVariable Long id) {
        log.debug("REST request to get WishList : {}", id);
        Optional<WishList> wishList = wishListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wishList);
    }

    /**
     * DELETE  /wish-lists/:id : delete the "id" wishList.
     *
     * @param id the id of the wishList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wish-lists/{id}")
    public ResponseEntity<Void> deleteWishList(@PathVariable Long id) {
        log.debug("REST request to delete WishList : {}", id);
        wishListRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
