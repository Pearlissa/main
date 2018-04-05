package seedu.address.model.task;

import java.util.function.Predicate;

/**
 * Tests that a {@code Task}'s month matches the value of month given.
 */
public class DeadlineIsCurrentMonthPredicate implements Predicate<Task> {
    private final int month;

    public DeadlineIsCurrentMonthPredicate(int month) {
        this.month = month;
    }

    @Override
    public boolean test(Task task) {
        return task.getDeadlineMonth() == month;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeadlineIsCurrentMonthPredicate // instanceof handles nulls
                && this.month == ((DeadlineIsCurrentMonthPredicate) other).month); // state check
    }
}
