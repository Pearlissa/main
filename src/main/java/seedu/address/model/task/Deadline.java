package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Represents a Task's deadline in the address book.
 * Guarantees: immutable;
 */
public class Deadline {


    public static final String MESSAGE_DEADLINE_CONSTRAINTS =
            "Deadline should be a valid date in the format dd-mm-yyyy. Tasks cannot be scheduled in the past. ";
    public final String dateString;
    public final LocalDate value;
    public final long daysBetween;
    public final int day;
    public final int month;
    public final int year;

    /**
     * Constructs a {@code Deadline}.
     *
     * @param deadline A valid deadline.
     */
    public Deadline(String deadline) {
        requireNonNull(deadline);
        dateString = deadline;
        try {
            String[] splitDeadline = deadline.split("-");
            day = Integer.parseInt(splitDeadline[0]);
            month = Integer.parseInt(splitDeadline[1]);
            year = Integer.parseInt(splitDeadline[2]);
            LocalDate deadlineDate = LocalDate.now().withDayOfMonth(Integer.valueOf(day))
                .withMonth(Integer.valueOf(month)).withYear(Integer.valueOf(year));
            this.value = deadlineDate;
            LocalDate now = LocalDate.now();
            daysBetween = ChronoUnit.DAYS.between(now, deadlineDate);
            int diff;

            if (deadlineDate.getYear() < now.getYear() || (deadlineDate.getYear() - now.getYear() > 1)) {
                throw new IllegalArgumentException(MESSAGE_DEADLINE_CONSTRAINTS);
            }
            if (deadlineDate.getYear() == now.getYear()) {
                diff = deadlineDate.getMonthValue() - now.getMonthValue();
            } else {
                diff = (12 - now.getMonthValue()) + deadlineDate.getMonthValue();
            }
            if (diff < 0 || diff > 6) {
                throw new IllegalArgumentException(MESSAGE_DEADLINE_CONSTRAINTS);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(MESSAGE_DEADLINE_CONSTRAINTS);
        }
    }

    /**
     * Returns true if a given string is a valid deadline.
     */
    public static boolean isValidDeadline(String test) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate deadlineDate = LocalDate.parse(test, formatter);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && this.value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

