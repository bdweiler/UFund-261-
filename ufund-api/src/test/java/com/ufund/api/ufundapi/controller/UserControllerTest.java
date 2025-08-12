package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.model.User;

/**
 * Test the User Controller class
 * 
 * @author Danny Catorcini
 */
@Tag("Controller")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new UserController
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetLogin() throws IOException {
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        when(mockUserDAO.getUser(user.getId())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // createUser may throw IOException
        // Setup
        int userId = 1;
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no User found
        when(mockUserDAO.getUser(userId)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception { // createUser may throw IOException
        // Setup
        int userId = 1;
        // When getHero is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException { // createUser may throw IOException
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException { // createUser may throw IOException
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);
        // when createUser is called, return false simulating failed
        // creation and save
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserConflictByUsername() throws IOException {
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        // Simulate no conflict by ID, but conflict by username
        when(mockUserDAO.getUser(user.getId())).thenReturn(null);
        User similarUser = new User(2, expected_username, expected_needList, expected_quantity, expected_amount);
        when(mockUserDAO.findUsers(user.getName())).thenReturn(new User[] { similarUser });

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserInternalServerError() throws IOException {
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);

        // Simulate an exception being thrown
        when(mockUserDAO.getUser(user.getId())).thenReturn(null);
        when(mockUserDAO.findUsers(user.getName())).thenReturn(null);
        doThrow(new IOException("Database error")).when(mockUserDAO).createUser(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException { // updateUser may throw IOException
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user);
        user.setName("green");

        // Invoke
        response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException { // updateUser may throw IOException
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException { // updateUser may throw IOException
        // Setup
        int expected_id = 1;
        String expected_username = "Greg";
        ArrayList<Integer> expected_needList = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
        ArrayList<Integer> expected_quantity = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 1, 1 }));
        int expected_amount = 100;

        User user = new User(expected_id, expected_username, expected_needList, expected_quantity, expected_amount);
        // When updateUser is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException {
        // Setup
        User[] users = {
                new User(1, "John", new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(1, 1)), 100),
                new User(2, "Jane", new ArrayList<>(Arrays.asList(3, 4)), new ArrayList<>(Arrays.asList(2, 1)), 200)
        };

        // Mock the DAO to return the users array
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUsersEmpty() throws IOException {
        // Setup: return an empty array when no users are found
        when(mockUserDAO.getUsers()).thenReturn(new User[0]);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length); // Expecting an empty array
    }

    @Test
    public void testGetUsersHandleException() throws IOException {
        // Setup: Simulate an exception thrown by the DAO
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchUsersByName() throws IOException {
        // Setup
        User[] users = {
                new User(1, "John", new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(1, 1)), 100),
                new User(2, "Jane", new ArrayList<>(Arrays.asList(3, 4)), new ArrayList<>(Arrays.asList(2, 1)), 200)
        };
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Test searching for "John"
        ResponseEntity<User[]> response = userController.searchUsers("John");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
        assertEquals("John", response.getBody()[0].getName());
    }

    @Test
    public void testSearchUsersEmptyResult() throws IOException {
        // Setup
        User[] users = {
                new User(1, "John", new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(1, 1)), 100),
                new User(2, "Jane", new ArrayList<>(Arrays.asList(3, 4)), new ArrayList<>(Arrays.asList(2, 1)), 200)
        };
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Test searching for a name that doesn't exist
        ResponseEntity<User[]> response = userController.searchUsers("Alex");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length); // No users found with the name "Alex"
    }

    @Test
    public void testSearchUsersHandleException() throws IOException {
        // Setup: Simulate an exception thrown by the DAO
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers("John");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException {
        // Setup
        int userId = 1;

        // when deleteUser is called, return true to simulate successful deletion
        when(mockUserDAO.deleteUser(userId)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        // Setup
        int userId = 1;

        // when deleteUser is called, return false to simulate user not found
        when(mockUserDAO.deleteUser(userId)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException {
        // Setup
        int userId = 1;

        // when deleteUser is called, throw an IOException to simulate an error
        doThrow(new IOException()).when(mockUserDAO).deleteUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
