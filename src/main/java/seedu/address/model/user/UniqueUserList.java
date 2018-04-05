package seedu.address.model.user;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.user.exceptions.DuplicateUserException;

/**
 * A list of users that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see User#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueUserList implements Iterable<User> {

    private final HashMap<String, User> userList = new HashMap<String, User>();
    private final ObservableList<User> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains a equivalent user as the given argument.
     */
    public boolean contains(String toCheck) {
        requireNonNull(toCheck);
        return userList.containsKey(toCheck);
    }

    /**
     * Adds a user to the hashmap
     * @param toAdd
     * @throws DuplicateUserException
     */
    public void add(User toAdd) throws DuplicateUserException {
        requireNonNull(toAdd);
        if (userList.containsKey(toAdd.getUsername().getUsername())) {
            throw new DuplicateUserException();
        }
        userList.put(toAdd.getUsername().getUsername(), toAdd);
        internalList.add(toAdd);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueUserList // instanceof handles nulls
                && this.userList.equals(((UniqueUserList) other).userList));
    }

    @Override
    public int hashCode() {
        return userList.hashCode();
    }

    public ObservableList<User> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    public HashMap<String, User> getUserList() {
        return userList;
    }

    @Override
    public Iterator<User> iterator() {
        return internalList.iterator();
    }
}
