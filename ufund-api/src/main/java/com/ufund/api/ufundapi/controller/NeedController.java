package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import com.ufund.api.ufundapi.persistence.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 *
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Matt Spring
 */

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private CupboardDAO cupboardDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param needDao The {@link CupboardDAO Cupboard Data Access Object} to perform
     *                CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public NeedController(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }

    /**
     * Responds to the GET request for a {@linkplain Need need} for the given id
     * 
     * @param id The id used to locate the {@link Need need}
     * 
     * @return ResponseEntity with {@link Need need} object and HTTP status of OK if
     *         found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        LOG.info("GET /needs/" + id);
        try {
            Need need = cupboardDAO.getNeed(id);
            if (need != null)
                return new ResponseEntity<Need>(need, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs}
     * 
     * @return ResponseEntity with array of {@link Need need} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds() {
        LOG.info("GET /needs");

        try {
            Need needs[] = cupboardDAO.getNeeds();
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs} whose type
     * contains
     * the text in type
     * 
     * @param type The type parameter which contains the text used to find the
     *             {@link Need needs}
     * 
     * @return ResponseEntity with array of {@link Need need} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find all needs that contain the text "ma"
     *         GET http://localhost:8080/needs/?type=ma
     */
    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String type) {
        LOG.info("GET /needs/?type=" + type);

        try {
            ArrayList<Need> needArrayList = new ArrayList<>();
            Need needs[] = cupboardDAO.getNeeds();

            for (int i = 0; i < needs.length; i++) {
                if (type == null || needs[i].getType().contains(type))
                    needArrayList.add(needs[i]);
            }

            Need[] needArray = new Need[needArrayList.size()];
            needArrayList.toArray(needArray);
            return new ResponseEntity<Need[]>(needArray, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Need need} with the provided need object
     * 
     * @param need - The {@link Need need} to create
     * 
     * @return ResponseEntity with created {@link Need need} object and HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Need need}
     *         object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /needs " + need);

        try {
            Need[] needs = cupboardDAO.getNeeds();

            // Check if the needs array is null, and handle it
            if (needs == null) {
                needs = new Need[0]; // Initialize to an empty array if null
            }

            // Check for conflicts based on ID or type
            for (Need oldNeed : needs) {
                if (oldNeed.getId() == need.getId() || oldNeed.getType().equals(need.getType())) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }

            // Create the new need
            cupboardDAO.createNeed(need);
            return new ResponseEntity<>(need, HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Need need} with the provided {@linkplain Need need}
     * object, if it exists
     * 
     * @param need The {@link Need need} to update
     * 
     * @return ResponseEntity with updated {@link Need need} object and HTTP status
     *         of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {
        LOG.info("PUT /needs " + need);

        try {
            if (cupboardDAO.updateNeed(need) != null)
                return new ResponseEntity<Need>(need, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} with the given id
     * 
     * @param id The id of the {@link Need need} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
        LOG.info("DELETE /needs/" + id);

        try {

            if (cupboardDAO.deleteNeed(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
