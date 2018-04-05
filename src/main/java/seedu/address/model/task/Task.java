package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Task {

    private final TaskDescription taskDesc;
    private final Deadline deadline;
    private final Priority priority;

    /**
     * Every field must be present and not null.
     */
    public Task(TaskDescription taskDesc, Deadline deadline, Priority priority) {
        requireAllNonNull(taskDesc, deadline, priority);
        this.taskDesc = taskDesc;
        this.deadline = deadline;
        this.priority = priority;
    }

    public TaskDescription getTaskDesc() {
        return taskDesc;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getDeadlineDay() {
        return deadline.day;
    }

    public int getDeadlineYear() {
        return deadline.year;
    }

    public int getDeadlineMonth() {
        return deadline.month;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof seedu.address.model.person.Person)) {
            return false;
        }

        seedu.address.model.task.Task otherPerson = (seedu.address.model.task.Task) other;
        return otherPerson.getTaskDesc().equals(this.getTaskDesc())
                && otherPerson.getDeadline().equals(this.getDeadline())
                && otherPerson.getPriority().equals(this.getPriority());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(taskDesc, deadline, priority);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Task TaskDescription: ")
                .append(getTaskDesc())
                .append(" Deadline: ")
                .append(getDeadline())
                .append(" Calculated Priority: ")
                .append(getPriority());
        return builder.toString();
    }

}

