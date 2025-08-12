package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.ufund.api.ufundapi.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test the UserFileDAO class
 * 
 * @author Danny Catorcini
 */
@Tag("Persistence")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    ArrayList<User> usersTest;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test we will create an inject a Mock ObjectMapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        this.mockObjectMapper = mock(ObjectMapper.class);
        this.usersTest = new ArrayList<>();
        this.usersTest.add(new User(0, "George", new ArrayList<>(Arrays.asList(new Integer[] { 0, 1, 2 })),
                new ArrayList<>(Arrays.asList(new Integer[] { 1, 1, 1 })), 5));
        this.usersTest.add(new User(1, "Gerald", new ArrayList<>(Arrays.asList(new Integer[] { 0 })),
                new ArrayList<>(Arrays.asList(new Integer[] { 2 })), 15));
        this.usersTest.add(new User(2, "Geramy", new ArrayList<>(), new ArrayList<>(), 150));

        when(mockObjectMapper
                .readValue(new File("abc123.txt"), User[].class))
                .thenReturn(usersTest.toArray(new User[usersTest.size()]));
        this.userFileDAO = new UserFileDAO("abc123.txt", mockObjectMapper);
    }

    @Test
    public void testFindAllUsers() {
        User[] allusers = userFileDAO.getUsers();

        assertEquals(allusers.length, usersTest.size());
        for (int i = 0; i < allusers.length; i++) {
            assertEquals(usersTest.get(i), allusers[i]);
        }
    }

    @Test
    public void testFindSpecifiedUsers() {
        User[] allusers = userFileDAO.findUsers("Ge");

        assertEquals(allusers.length, usersTest.size());
        for (int i = 0; i < allusers.length; i++) {
            assertEquals(usersTest.get(i), allusers[i]);
        }
    }

    @Test
    public void testFindNoUsers() {
        User[] empty = userFileDAO.findUsers("ABCDE");

        assertEquals(Arrays.toString(new User[] {}), Arrays.toString(empty));
    }

    @Test
    public void testGetUser() {
        User user = userFileDAO.getUser(1);

        assertEquals(usersTest.get(1).getId(), user.getId());
        assertEquals(usersTest.get(1).getName(), user.getName());
        assertTrue(usersTest.get(1).getNeedList().equals(user.getNeedList()));
        assertEquals(usersTest.get(1).getSpent(), user.getSpent());
    }

    @Test
    public void testGetNoUser() {
        int expected_id = 3;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        assertNull(userFileDAO.getUser(user.getId()));
    }

    @Test
    public void testCreateUser() {
        int expected_id = 3;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<>();
        ArrayList<Integer> expected_quantity = new ArrayList<>();
        int expected_amount = 0;

        User new_user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        User result = assertDoesNotThrow(() -> userFileDAO.createUser(new_user), "Unexpected Exception Thrown");

        assertNotNull(result);
        User actual = userFileDAO.getUser(new_user.getId());
        assertEquals(new_user.getId(), actual.getId());
        assertEquals(new_user.getName(), actual.getName());
        assertEquals(Arrays.toString(new_user.getNeedList().toArray()),
                Arrays.toString(actual.getNeedList().toArray()));
        assertEquals(new_user.getSpent(), actual.getSpent());
    }

    @Test
    public void testUpdateUserExists() {
        User update = usersTest.get(1);
        update.setName("Germaine");

        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(update), "Unexpected Exception Thrown");

        assertNotNull(result);
        User actual = userFileDAO.getUser(update.getId());
        assertEquals(update.getId(), actual.getId());
        assertEquals(update.getName(), actual.getName());
        assertTrue(update.getNeedList().equals(actual.getNeedList()));
        assertEquals(update.getSpent(), actual.getSpent());
    }

    @Test
    public void testUpdateUserNotExists() {
        int expected_id = 3;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User update = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(update), "Unexpected Exception Thrown");

        assertNull(result);
    }

    @Test
    public void testDeleteUserExists() {
        User user = usersTest.get(0);

        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(user.getId()));

        assertTrue(result);
        assertEquals(userFileDAO.users.size(), usersTest.size() - 1);
    }

    @Test
    public void testDeleteUserNotExists() {
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(5));

        assertEquals(usersTest.size(), userFileDAO.users.size());
        assertFalse(result);
    }

    @Test
    public void testSaveException() {
        try {
            doThrow(new IOException())
                    .when(mockObjectMapper)
                    .writeValue(any(File.class), any(User[].class));
        } catch (Exception e) {
        }

        int expected_id = 3;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        assertThrows(IOException.class, () -> userFileDAO.createUser(user), "IOException Not Thrown");
    }

    @Test
    public void testConstructorException() throws IOException {
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);

        doThrow(new IOException()).when(mockObjectMapper).readValue(new File("abc123.txt"), User[].class);

        assertThrows(IOException.class, () -> new UserFileDAO("abc123.txt", mockObjectMapper),
                "IOException Not thrown");
    }

}
