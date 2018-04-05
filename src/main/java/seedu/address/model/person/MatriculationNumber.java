package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's matriculation number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMatricNumber(String)}
 */
public class MatriculationNumber {


    public static final String MESSAGE_MATRIC_NUMBER_CONSTRAINTS =
            "Matric numbers can only contain capital letters and numbers, and should contain 9 characters";
    public static final String MATRIC_NUMBER_VALIDATION_REGEX_FIRST = "[AU]{1}";
    public static final String MATRIC_NUMBER_VALIDATION_REGEX_SECOND = "\\d{7}";
    public static final String MATRIC_NUMBER_VALIDATION_REGEX_LAST = "[A-Z]{1}";
    public final String value;

    /**
     * Constructs a {@code MatriculationNumber}.
     *
     * @param matricNumber A valid matriculation number.
     */
    public MatriculationNumber(String matricNumber) {
        requireNonNull(matricNumber);
        checkArgument(isValidMatricNumber(matricNumber), MESSAGE_MATRIC_NUMBER_CONSTRAINTS);
        this.value = matricNumber;
    }

    /**
     * Returns true if a given string is a valid person matriculation number.
     */
    public static boolean isValidMatricNumber(String test) {
        if (test.length() != 9) {
            return false;
        }
        String firstCharacter = test.substring(0, 1);
        String nextCharacters = test.substring(1, test.length() - 1);
        String lastCharacter = test.substring(test.length() - 1, test.length());
        return firstCharacter.matches(MATRIC_NUMBER_VALIDATION_REGEX_FIRST)
                && nextCharacters.matches(MATRIC_NUMBER_VALIDATION_REGEX_SECOND)
                && lastCharacter.matches(MATRIC_NUMBER_VALIDATION_REGEX_LAST);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MatriculationNumber // instanceof handles nulls
                && this.value.equals(((MatriculationNumber) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
