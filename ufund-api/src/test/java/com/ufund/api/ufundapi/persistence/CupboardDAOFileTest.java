package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

/**
 * Test the Need File DAO class
 * 
 * @author Matt Spring
 */
@Tag("Persistence-tier")
public class CupboardDAOFileTest {
    CupboardFileDAO cupboardFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(0, "Canned Corn", 1.29, 7, Need.Category.NON_PERISHABLES);
        testNeeds[1] = new Need(1, "Beans", 2.59, 4, Need.Category.NON_PERISHABLES);
        testNeeds[2] = new Need(2, "Apples", 1.00, 12, Need.Category.PRODUCE);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the hero array above
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Need[].class))
                .thenReturn(testNeeds);
        cupboardFileDAO = new CupboardFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetNeeds() {
        // Invoke
        Need[] needs = cupboardFileDAO.getNeeds();

        // Analyze
        assertEquals(needs.length, testNeeds.length);
        for (int i = 0; i < testNeeds.length; ++i)
            assertEquals(needs[i], testNeeds[i]);
    }

    @Test
    public void testFindNeeds() {
        // Invoke
        Need[] needs = cupboardFileDAO.findNeeds("s");

        // Analyze
        assertEquals(needs.length, 2);
        assertEquals(needs[0], testNeeds[1]);
        assertEquals(needs[1], testNeeds[2]);
    }

    @Test
    public void testGetNeed() {
        // Invoke
        Need need = cupboardFileDAO.getNeed(0);

        // Analzye
        assertEquals(need, testNeeds[0]);
    }

    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cupboardFileDAO.deleteNeed(1),
                "Unexpected exception thrown");

        // Analzye
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of CupboardFileDAO is package private
        // we can access it directly
        assertEquals(cupboardFileDAO.needs.size(), testNeeds.length - 1);
    }

    @Test
    public void testCreateNeed() {
        // Setup
        Need need = new Need(3, "Pasta", 4.59, 5, Need.Category.NON_PERISHABLES);

        // Invoke
        Need result = assertDoesNotThrow(() -> cupboardFileDAO.createNeed(need),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = cupboardFileDAO.getNeed(need.getId());
        assertEquals(actual.getId(), need.getId());
        assertEquals(actual.getType(), need.getType());
        assertEquals(actual.getCost(), need.getCost());
        assertEquals(actual.getQuantity(), need.getQuantity());
    }

    @Test
    public void testUpdateNeed() {
        // Setup
        Need need = new Need(1, "Pasta", 4.59, 5, Need.Category.NON_PERISHABLES);

        // Invoke
        Need result = assertDoesNotThrow(() -> cupboardFileDAO.updateNeed(need),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = cupboardFileDAO.getNeed(need.getId());
        assertEquals(actual, need);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(Need[].class));

        Need need = new Need(3, "Canned Carrots", 2.00, 4, Need.Category.NON_PERISHABLES);

        assertThrows(IOException.class,
                () -> cupboardFileDAO.createNeed(need),
                "IOException not thrown");
    }

    @Test
    public void testGetNeedNotFound() {
        // Invoke
        Need need = cupboardFileDAO.getNeed(98);

        // Analyze
        assertEquals(need, null);
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cupboardFileDAO.deleteNeed(98),
                "Unexpected exception thrown");

        // Analyze
        assertEquals(result, false);
        assertEquals(cupboardFileDAO.needs.size(), testNeeds.length);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Setup
        Need need = new Need(17, "Bolts", 1.23, 3, Need.Category.NON_PERISHABLES);

        // Invoke
        Need result = assertDoesNotThrow(() -> cupboardFileDAO.updateNeed(need),
                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the NeedFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
                .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), Need[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                () -> new CupboardFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }
}
