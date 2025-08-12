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
import java.util.List;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.model.User;

/**
 * Handles the REST API requests for the User resource
 * <p>
 *
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Danny Catorcini, Matt Spring
 */

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param userDao The {@link UserDAO Hero Data Access Object} to perform
     *                CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Responds to the GET request for a {@linkplain User user} for the given id
     * 
     * @param id The id used to locate the {@link User user}
     * 
     * @return ResponseEntity with {@link User user} object and HTTP status of OK if
     *         found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        LOG.info("GET /users/" + id);
        try {
            User user = userDAO.getUser(id);
            if (user != null)
                return new ResponseEntity<User>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User users}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");

        try {
            User users[] = userDAO.getUsers();
            return new ResponseEntity<User[]>(users, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User users} whose type
     * contains
     * the text in type
     * 
     * @param type The type parameter which contains the text used to find the
     *             {@link User users}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find all users that contain the text "ma"
     *         GET http://localhost:8080/users/?type=ma
     */
    @GetMapping("/")
    public ResponseEntity<User[]> searchUsers(@RequestParam String name) {
        LOG.info("GET /users/?name=" + name);

        try {
            ArrayList<User> userArrayList = new ArrayList<>();
            User users[] = userDAO.getUsers();

            for (int i = 0; i < users.length; i++) {
                if (name == null || users[i].getName().contains(name))
                    userArrayList.add(users[i]);
            }

            User[] userArray = new User[userArrayList.size()];
            userArrayList.toArray(userArray);
            return new ResponseEntity<User[]>(userArray, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain User user} with the provided user object
     * 
     * @param user - The {@link User user} to create
     * 
     * @return ResponseEntity with created {@link User user} object and HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link User user}
     *         object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users " + user);

        try {
            User same_id = userDAO.getUser(user.getId());
            if (same_id != null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            User[] similar_usernames = userDAO.findUsers(user.getName());
            if (similar_usernames != null) {
                for (User u : similar_usernames) {
                    if (u.getName().equals(user.getName())) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                }
            }

            userDAO.createUser(user);
            return new ResponseEntity<User>(user, HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain User user} with the provided {@linkplain User user}
     * object, if it exists
     * 
     * @param user The {@link User user} to update
     * 
     * @return ResponseEntity with updated {@link User user} object and HTTP status
     *         of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users " + user);

        try {
            if (userDAO.updateUser(user) != null)
                return new ResponseEntity<User>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /users/" + id);

        try {

            if (userDAO.deleteUser(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}