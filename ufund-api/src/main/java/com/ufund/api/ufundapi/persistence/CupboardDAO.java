package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;

/**
 * Defines the interface for Need object persistence
 * 
 * @author Matthew Spring, Team 6F
 */
public interface CupboardDAO {
    /**
     * Retrieves all {@linkplain Need Needs}
     * 
     * @return An array of {@link Need Need} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] getNeeds() throws IOException;

    /**
     * Finds all {@linkplain Need Needs} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Need Needs} whose nemes contains the given text,
     *         may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] findNeeds(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Need Need} with the given id
     * 
     * @param id The id of the {@link Need Need} to get
     * 
     * @return a {@link Need Need} object with the matching id
     *         <br>
     *         null if no {@link Need Need} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need getNeed(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Need Need}
     * 
     * @param Need {@linkplain Need Need} object to be created and saved
     *             <br>
     *             The id of the Need object is ignored and a new uniqe id is
     *             assigned
     *
     * @return new {@link Need Need} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need createNeed(Need Need) throws IOException;

    /**
     * Updates and saves a {@linkplain Need Need}
     * 
     * @param {@link Need Need} object to be updated and saved
     * 
     * @return updated {@link Need Need} if successful, null if
     *         {@link Need Need} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Need updateNeed(Need Need) throws IOException;

    /**
     * Deletes a {@linkplain Need Need} with the given id
     * 
     * @param id The id of the {@link Need Need}
     * 
     * @return true if the {@link Need Need} was deleted
     *         <br>
     *         false if Need with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteNeed(int id) throws IOException;
}
