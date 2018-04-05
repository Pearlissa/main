package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DESC;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.task.Task;

/**
 * Adds a task to the todo list and calendar.
 */
public class AddTaskCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addTask";
    public static final String COMMAND_ALIAS = "at";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the todo list and calendar. "
            + "Parameters: "
            + PREFIX_TASK_DESC + "TASK "
            + PREFIX_DEADLINE + "DEADLINE "
            + PREFIX_PRIORITY + "PRIORITY LEVEL(from 0 - 50, 0 being lowest priority, 50 being highest priority) \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TASK_DESC + "Dance till I drop "
            + PREFIX_DEADLINE + "20-03-2018 "
            + PREFIX_PRIORITY + "1";

    public static final String MESSAGE_SUCCESS = "New Task added: %1$s";

    private final Task toAdd;

    /**
     * Creates an AddTaskCommand to add the specified {@code Task}
     */
    public AddTaskCommand(Task task) {
        requireNonNull(task);
        toAdd = task;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        model.addTask(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));


    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddTaskCommand) other).toAdd));
    }
}

