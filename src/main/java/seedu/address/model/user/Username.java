package seedu.address.model.user;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a User's stored username in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUsername(String)}
 */
public class Username {

    private static  final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_USERNAME_CONSTRAINTS = "Username should adhere to the following constraints:\n"
            + "Username must only consist of alphanumeric characters.\n"
            + "Minimum length: 3 characters.\n"
            + "Maximum length: 15 characters.\n"
            + "Should not contain any of the special characters: ( " + SPECIAL_CHARACTERS + " ).\n";

    // alphanumeric and special characters
    private static final String USERNAME_REGEX = "[^\\W_]{3,15}$"; // alphanumeric characters except underscore
    public static final String USERNAME_VALIDATION_REGEX = USERNAME_REGEX;

    public final String username;

    /**
     * Constructs a {@code Username}.
     *
     * @param username A valid username.
     */
    public Username(String username) {
        requireNonNull(username);
        checkArgument(isValidUsername(username), MESSAGE_USERNAME_CONSTRAINTS);
        this.username = username;
    }

    /**
     * Returns if a given string is a valid username.
     */
    public static boolean isValidUsername(String test) {
        return test.matches(USERNAME_VALIDATION_REGEX);
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Username // instanceof handles nulls
                && this.username.equals(((Username) other).username)); // state check
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

}
