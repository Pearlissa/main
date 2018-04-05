package seedu.address.model.user;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a User registered in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class User implements Comparable<User> {

    private static String xmlFilepath;

    private final Username username;
    private final Password password;

    /**
     * Every field must be present and not null.
     */
    public User(Username username, Password password) {
        requireAllNonNull(username, password);
        this.username = username;
        this.password = password;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password; }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof User)) {
            return false;
        }

        User otherUser = (User) other;
        return otherUser.getUsername().equals(this.getUsername())
                && otherUser.getPassword().equals(this.getPassword());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Username: ")
                .append(getUsername())
                .append(" Password: ")
                .append(getPassword());
        return builder.toString();
    }

    public int compareTo(User otherUser) {
        return this.username.toString().compareTo(otherUser.username.toString());
    }

}
