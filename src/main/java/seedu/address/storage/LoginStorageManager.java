package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.LoginManager;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class LoginStorageManager extends ComponentManager {

    private static final Logger logger = LogsCenter.getLogger(LoginStorageManager.class);
    private XmlLoginStorage xmlLoginStorage;


    public LoginStorageManager(XmlLoginStorage xmlLoginStorage) {
        super();
        this.xmlLoginStorage = xmlLoginStorage;
    }

    // ================ Login methods ==============================

    public String getLoginFilePath() {
        return "Login.xml";
    }

    public LoginManager readLogin() throws DataConversionException, IOException {
        return readLogin(xmlLoginStorage.getLoginFilePath());
    }

    /**
     *
     * @param filePath
     * @return
     * @throws DataConversionException
     * @throws IOException
     */
    public LoginManager readLogin(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return xmlLoginStorage.readLogin(filePath);
    }

    public void saveLogin(LoginManager login) throws IOException {
        saveAddressBook(login, xmlLoginStorage.getLoginFilePath());
    }

    /**
     *
     * @param login
     * @param filePath
     * @throws IOException
     */
    public void saveAddressBook(LoginManager login, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        xmlLoginStorage.saveLogin(login, filePath);
    }

}
