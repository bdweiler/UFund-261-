package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User class
 * 
 * @author Danny Catorcini
 */
@Tag("Model")
public class UserTest {
    @Test
    public void testCreateUser() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        assertNotNull(user);
        assertEquals(expected_id, user.getId());
        assertEquals(expected_username, user.getName());
        assertTrue(expected_needList.equals(user.getNeedList()));
        assertEquals(expected_amount, user.getSpent());
    }

    @Test
    public void testSetUsername() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        String newUsername = "george";
        user.setName(newUsername);

        assertEquals(user.getName(), newUsername);
    }

    @Test
    public void testResetAmount() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        user.resetSpent();

        assertEquals(user.getSpent(), 0);
    }

    @Test
    public void testIncrementAmount() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        int increment = 50;
        expected_amount = expected_amount + increment;
        user.addSpent(increment);

        assertEquals(user.getSpent(), expected_amount);
    }

    @Test
    public void testRemoveFromNeedsList() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        Need need = new Need(2, "hamburger", 1.00, 1, Need.Category.PERISHABLES);
        user.removeNeed(need);
        assertTrue(user.getNeedList().equals(new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1 }))));
    }

    @Test
    public void testAppendNeedsList() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        Need need = new Need(3, "Rice", 2.50, 1, Need.Category.NON_PERISHABLES);
        user.addNeed(need);
        assertEquals(need.getId(), user.getNeedList().get(3));
    }

    @Test
    public void testToString() {
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        assertEquals(user.toString(), "User [id=1, name=Greg, needList=[0,1,2], quantity=[1,1,1], spent=100.00]");
    }
}
