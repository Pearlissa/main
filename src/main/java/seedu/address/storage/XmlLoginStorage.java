package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.LoginManager;

/**
 * A class to access Login data stored as an xml file on the hard disk. <- READ THIS :)
 */
public class XmlLoginStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlLoginStorage.class);

    private String filePath;

    public XmlLoginStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getLoginFilePath() {
        return filePath;
    }

    private LoginManager readLogin() throws DataConversionException, IOException {
        return readLogin(filePath);
    }

    /**
     * Similar to {@link #readLogin()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public LoginManager readLogin(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File loginFile = new File(filePath);

        if (!loginFile.exists()) {
            logger.info("AddressBook file "  + loginFile + " not found");
            return null;
        }

        XmlSerializableLogin xmlSerializableLogin = XmlLoginFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return xmlSerializableLogin.toModelType();
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + loginFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    private void saveLogin(LoginManager loginManager) throws IOException {
        saveLogin(loginManager, filePath);
    }

    /**
     * Similar to {@link #saveLogin(LoginManager)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveLogin(LoginManager loginManager, String filePath) throws IOException {
        requireNonNull(loginManager);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlLoginFileStorage.saveDataToFile(file, new XmlSerializableLogin(loginManager));
    }

}
