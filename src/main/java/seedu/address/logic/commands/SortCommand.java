package seedu.address.logic.commands;

/**
 * Lists all persons in the address book to the user.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String COMMAND_ALIAS = "st";

    public static final String MESSAGE_SUCCESS = "List is sorted";


    @Override
    public CommandResult execute() {
        model.sortPersons();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
