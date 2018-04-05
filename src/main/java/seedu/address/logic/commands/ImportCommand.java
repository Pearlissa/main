package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.XmlFileStorage;

/**
 * Imports data from a xml file and overwrites the current data stored
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports data from an external xml data file with "
            + "the provided path and overwrites the current data stored.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " ~/DOWNLOADS/NewDataSet.xml";

    public static final String MESSAGE_SUCCESS = "Data imported successfully";
    public static final String MESSAGE_INVALID_PATH = "File not found";
    public static final String MESSAGE_INVALID_FILE = "Data configuration failed";

    private final String filePath;

    public ImportCommand(String filePath) {
        this.filePath = filePath.trim();
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        try {
            ReadOnlyAddressBook newDataSet = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
            model.resetData(newDataSet);
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_INVALID_PATH);
        } catch (DataConversionException e) {
            throw new CommandException(MESSAGE_INVALID_FILE);
        }
    }
}
