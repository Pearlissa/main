package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommandParser object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an Import Command object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportCommand parse(String userInput) throws ParseException {
        String trimmedInput = userInput.trim();

        String exceptionMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);
        if (trimmedInput.isEmpty()) {
            throw new ParseException(exceptionMessage);
        }

        return new ImportCommand(userInput);
    }
}
