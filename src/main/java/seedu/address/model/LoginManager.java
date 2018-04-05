package seedu.address.model;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.user.Password;
import seedu.address.model.user.UniqueUserList;
import seedu.address.model.user.User;
import seedu.address.model.user.Username;
import seedu.address.model.user.exceptions.DuplicateUserException;
import seedu.address.storage.LoginStorageManager;

/**
 * Represents the in-memory model of the login data.
 * All changes to any model should be synchronized.
 */
public class LoginManager extends ComponentManager implements Login {
    private static final Logger logger = LogsCenter.getLogger(LoginManager.class);

    private UniqueUserList userList;

    /**
     * Initializes a LoginManager with the given username and password.
     */

    public LoginManager(){

    }

    public LoginManager(LoginStorageManager loginInfo) {
        super();
    }

    @Override
    /**
     * Adds user into the userList hashmap.
     */
    public synchronized void addUser(String username, String password) throws DuplicateUserException {
        if (!userList.getUserList().containsKey(username)) {
            Username addUsername = new Username(username);
            Password addPassword = new Password(password);
            User toAdd = new User(addUsername, addPassword);
            userList.add(toAdd);
        }
    }

    public synchronized void addUser(User user) throws DuplicateUserException {
        userList.add(user);
    }

    @Override
    public void authenticate(String username, String password) throws DuplicateUserException {

        logger.fine("Authenticating user: " + username);
        String filepath = username + ".xml";
        if (userList.getUserList().containsKey(username)) {
            loginUser(filepath);
        } else {
            addUser(username, password);
            loginUser(filepath);
        }

    }

    public ObservableList<User> getUserList() {
        return userList.asObservableList();
    }

    @Override
    public void loginUser(String filepath) {

    }

}
