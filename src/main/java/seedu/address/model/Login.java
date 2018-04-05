package seedu.address.model;

import seedu.address.model.user.exceptions.DuplicateUserException;
import seedu.address.model.user.exceptions.UserNotFoundException;

/**
 * The API of the Login component.
 */
public interface Login {

    /** Adds the given user. */
    void addUser(String username, String password) throws DuplicateUserException;

    /**
     * Checks if user entered (username and password included) is valid.
     * @param username
     * @param password
     * @throws UserNotFoundException
     */
    void authenticate(String username, String password) throws UserNotFoundException, DuplicateUserException;

    /**
     * Loads addressbook storage of the user and initializes addressbook.
     * @param filepath
     */
    void loginUser(String filepath);

}
