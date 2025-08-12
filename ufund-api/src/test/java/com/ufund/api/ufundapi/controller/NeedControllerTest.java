package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.CupboardDAO;

/**
 * Test the Need Controller class
 * 
 * @author Brock Weiler
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private CupboardDAO mockCupboardDAO;

    /**
     * Before each test, create a new NeedController object and inject
     * a mock Need DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockCupboardDAO = mock(CupboardDAO.class);
        needController = new NeedController(mockCupboardDAO);
    }

    @Test
    public void testGetNeed() throws IOException { // getNeed may throw IOException
        // Setup
        Need need = new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES);
        // When the same id is passed in, our mock Cupboard DAO will return the Hero
        // object
        when(mockCupboardDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 1;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no hero found
        when(mockCupboardDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 1;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateNeed() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES);
        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockCupboardDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testCreateNeedConflict() throws IOException {
        // Setup
        Need existingNeed = new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES); // Existing need with ID
                                                                                                // 1 and type "Canned
                                                                                                // Corn"
        Need conflictingNeedById = new Need(1, "Beans", 1.50, 3, Need.Category.NON_PERISHABLES); // New need with the
                                                                                                 // same ID but
                                                                                                 // different type
        Need conflictingNeedByType = new Need(2, "Canned Corn", 2.00, 10, Need.Category.NON_PERISHABLES); // New need
                                                                                                          // with a
                                                                                                          // different
                                                                                                          // ID but same
                                                                                                          // type

        // Mock the DAO to return the existing need when getNeeds is called
        when(mockCupboardDAO.getNeeds()).thenReturn(new Need[] { existingNeed });

        // Test conflict by ID
        ResponseEntity<Need> responseById = needController.createNeed(conflictingNeedById);
        assertEquals(HttpStatus.CONFLICT, responseById.getStatusCode());
        verify(mockCupboardDAO, never()).createNeed(any(Need.class)); // Ensure createNeed() was not called for
                                                                      // conflicting need by ID

        // Test conflict by type
        ResponseEntity<Need> responseByType = needController.createNeed(conflictingNeedByType);
        assertEquals(HttpStatus.CONFLICT, responseByType.getStatusCode());
        verify(mockCupboardDAO, never()).createNeed(any(Need.class)); // Ensure createNeed() was not called for
                                                                      // conflicting need by type
    }

    @Test
    public void testCreateNeedHandleException() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need(1, "Canned Beans", 1.25, 5, Need.Category.NON_PERISHABLES);

        // When createNeed is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockCupboardDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setCost(2);
        ;

        // Invoke
        response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockCupboardDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES);
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockCupboardDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockCupboardDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // When deleteHero is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).deleteNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException {
        // Setup
        Need[] needs = {
                new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES),
                new Need(2, "Rice", 0.50, 10, Need.Category.NON_PERISHABLES)
        };

        // When getNeeds is called, return the array of needs
        when(mockCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testGetNeedsEmpty() throws IOException {
        // Setup
        Need[] emptyNeeds = new Need[0];

        // When getNeeds is called, return an empty array
        when(mockCupboardDAO.getNeeds()).thenReturn(emptyNeeds);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length);
    }

    @Test
    public void testGetNeedsHandleException() throws IOException {
        // When getNeeds is called, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchNeedsFound() throws IOException {
        // Setup
        Need[] needs = {
                new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES),
                new Need(2, "Rice", 0.50, 10, Need.Category.NON_PERISHABLES),
                new Need(3, "Corn Flour", 2.00, 3, Need.Category.NON_PERISHABLES)
        };

        // Mock the DAO to return all needs
        when(mockCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds("Corn");

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Need[] searchResults = response.getBody();
        assertEquals(2, searchResults.length); // 2 items contain "Corn"
        assertEquals("Canned Corn", searchResults[0].getType());
        assertEquals("Corn Flour", searchResults[1].getType());
    }

    @Test
    public void testSearchNeedsNotFound() throws IOException {
        // Setup
        Need[] needs = {
                new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES),
                new Need(2, "Rice", 0.50, 10, Need.Category.NON_PERISHABLES)
        };

        // Mock the DAO to return all needs
        when(mockCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds("Water");

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Need[] searchResults = response.getBody();
        assertEquals(0, searchResults.length); // No items contain "Water"
    }

    @Test
    public void testSearchNeedsNullOrEmptyType() throws IOException {
        // Setup
        Need[] needs = {
                new Need(1, "Canned Corn", 1.00, 5, Need.Category.NON_PERISHABLES),
                new Need(2, "Rice", 0.50, 10, Need.Category.NON_PERISHABLES)
        };

        // Mock the DAO to return all needs
        when(mockCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke with null type
        ResponseEntity<Need[]> responseNull = needController.searchNeeds(null);

        // Invoke with empty type
        ResponseEntity<Need[]> responseEmpty = needController.searchNeeds("");

        // Analyze
        assertEquals(HttpStatus.OK, responseNull.getStatusCode());
        assertEquals(HttpStatus.OK, responseEmpty.getStatusCode());
        assertEquals(2, responseNull.getBody().length); // Should return all needs
        assertEquals(2, responseEmpty.getBody().length); // Should return all needs
    }

    @Test
    public void testSearchNeedsHandleException() throws IOException {
        // Setup
        // Mock the DAO to throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds("Corn");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}