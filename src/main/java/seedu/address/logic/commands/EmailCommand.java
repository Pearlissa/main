package seedu.address.logic.commands;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.UnsupportDesktopException;
import seedu.address.model.person.Person;

/**
 * Emails a person identified using it's last displayed index from the address book.
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String MAIL_SYNTAX = "mailto:";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Email the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EMAIL_PERSON_SUCCESS = "Email Person: %1$s";

    private final Index targetIndex;

    public EmailCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToEmail = lastShownList.get(targetIndex.getZeroBased());

        String emailAddress = personToEmail.getEmail().toString();
        String emailName = personToEmail.getName().toString();

        try {
            Desktop.getDesktop().mail(new URI(MAIL_SYNTAX + emailAddress));
        } catch (HeadlessException hlError) {
            throw new UnsupportDesktopException(Messages.MESSAGE_UNSUPPORTED_DESKTOP);
        } catch (URISyntaxException | IOException error) {
            throw new CommandException(Messages.MESSAGE_MAIL_APP_ERROR);
        }

        return new CommandResult(String.format(MESSAGE_EMAIL_PERSON_SUCCESS, emailName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && this.targetIndex.equals(((EmailCommand) other).targetIndex)); // state check
    }
}
