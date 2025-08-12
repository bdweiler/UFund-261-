package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/*
 * The unit test for the Need class
 * 
 * @author Matt Spring and Brock Weiler 
 */
@Tag("Modle-tier")
public class NeedTest {
    @Test
    public void testCreateNeed() {
        int expected_id = 1;
        String expected_type = "Canned Bean";
        double expected_cost = 1.59;
        int expected_quantity = 5;
        Need.Category expected_category = Need.Category.NON_PERISHABLES;

        Need need = new Need(expected_id, expected_type, expected_cost, expected_quantity, expected_category);

        assertEquals(expected_id, need.getId());
        assertEquals(expected_type, need.getType());
        assertEquals(expected_cost, need.getCost());
        assertEquals(expected_quantity, need.getQuantity());
        assertEquals(expected_category, need.getCategory());
    }

    @Test
    public void testQuantity() {
        int id = 1;
        String type = "Canned Bean";
        double cost = 1.59;
        int quantity = 5;
        Need.Category category = Need.Category.NON_PERISHABLES;
        Need need = new Need(id, type, cost, quantity, category);

        int expected_quantity = 3;
        need.setQuantity(expected_quantity);

        assertEquals(expected_quantity, need.getQuantity());
    }

    @Test
    public void testCategoryToString() {
        Need.Category nonPerishables = Need.Category.NON_PERISHABLES;
        assertEquals("Non-Perishables", nonPerishables.toString(),
                "NON_PERISHABLES should format as 'Non-Perishables'.");

        Need.Category beverages = Need.Category.BEVERAGES;
        assertEquals("Beverages", beverages.toString(), "BEVERAGES should format as 'Beverages'.");
    }

    @Test
    public void testToString() {
        int id = 1;
        String type = "Canned Bean";
        double cost = 1.59;
        int quantity = 5;
        Need.Category category = Need.Category.NON_PERISHABLES;
        String expected_string = String.format(Need.STRING_FORMAT, id, type, cost, quantity, category);
        Need need = new Need(id, type, cost, quantity, category);

        String actual_string = need.toString();

        assertEquals(expected_string, actual_string);
    }

    @Test
    public void testToStringWithDifferentCategory() {
        int id = 2;
        String type = "Orange Juice";
        double cost = 3.29;
        int quantity = 10;
        Need.Category category = Need.Category.BEVERAGES;

        String expected_string = String.format(Need.STRING_FORMAT, id, type, cost, quantity, category);
        Need need = new Need(id, type, cost, quantity, category);

        String actual_string = need.toString();

        assertEquals(expected_string, actual_string,
                "The toString method should correctly handle different categories.");
    }

    @Test
    public void testSetType() {
        int id = 1;
        String type = "Canned Bean";
        double cost = 1.59;
        int quantity = 5;
        Need.Category category = Need.Category.NON_PERISHABLES;
        Need need = new Need(id, type, cost, quantity, category);

        // Test setType method
        String newType = "Rice";
        need.setType(newType);

        assertEquals(newType, need.getType(), "The type should be updated to 'Rice'.");
    }

    @Test
    public void testSetCategory() {
        int id = 1;
        String type = "Canned Bean";
        double cost = 1.59;
        int quantity = 5;
        Need.Category initialCategory = Need.Category.NON_PERISHABLES;
        Need need = new Need(id, type, cost, quantity, initialCategory);

        Need.Category newCategory = Need.Category.BEVERAGES;
        need.setCategory(newCategory);

        assertEquals(newCategory, need.getCategory(), "The category should be updated to BEVERAGES.");
    }

}
