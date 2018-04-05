package seedu.address.model.user;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a User's stored password in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPassword(String)}
 */
public class Password {

    private static  final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_PASSWORD_CONSTRAINTS = "Password should adhere to the following constraints:\n"
            + "Password must only consist of alphanumeric characters.\n"
            + "Minimum length: 8 characters.\n"
            + "Maximum length: 30 characters.\n"
            + "Should not contain any of the special characters: ( " + SPECIAL_CHARACTERS + " ).\n";

    // alphanumeric and special characters
    private static final String PASSWORD_REGEX = "[^\\W_]{8,30}$"; // alphanumeric characters except underscore
    public static final String PASSWORD_VALIDATION_REGEX = PASSWORD_REGEX;

    public final String password;

    /**
     * Constructs a {@code Password}.
     *
     * @param password A valid password.
     */
    public Password(String password) {
        requireNonNull(password);
        checkArgument(isValidPassword(password), MESSAGE_PASSWORD_CONSTRAINTS);
        this.password = password;
    }

    /**
     * Returns if a given string is a valid password.
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return password;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Username // instanceof handles nulls
                && this.password.equals(((Password) other).password)); // state check
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }

}
