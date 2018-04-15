# Pearlissa
###### \java\seedu\address\logic\commands\SortCommand.java
``` java
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
```
###### \java\seedu\address\model\Login.java
``` java
/**
 * The API of the Login component.
 */
public interface Login {

    /** Adds the given user. */
    void addUser(String username, String password) throws DuplicateUserException;

    /**
     * Checks if user entered (username and password included) is valid.
     * @param username
     * @param password
     * @throws UserNotFoundException
     */
    void authenticate(String username, String password) throws UserNotFoundException, DuplicateUserException;

    /**
     * Loads addressbook storage of the user and initializes addressbook.
     * @param filepath
     */
    void loginUser(String filepath);

}
```
###### \java\seedu\address\model\LoginManager.java
``` java
/**
 * Represents the in-memory model of the login data.
 * All changes to any model should be synchronized.
 */
public class LoginManager extends ComponentManager implements Login {
    private static final Logger logger = LogsCenter.getLogger(LoginManager.class);

    private Ui ui;
    private Logic logic;
    private Storage storage;
    private Model model;
    private Config config;
    private UserPrefs userPrefs;

    private Stage primaryStage;
    private UniqueUserList userList;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Initializes a LoginManager with the given username and password.
     */

    public LoginManager() {
        userList = new UniqueUserList();
    }

    public void setUserPrefsStorage(UserPrefsStorage userPrefsStorage) {
        this.userPrefsStorage = userPrefsStorage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setUserPrefs(UserPrefs userPrefs) {
        this.userPrefs = userPrefs;
    }

    public Ui getUi() {
        return ui;
    }
    public Logic getLogic() {
        return logic;
    }
    public Storage getStorage() {
        return storage;
    }
    public Model getModel() {
        return model;
    }
    public UserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    /**
     * Adds user into the userList hashmap.
     */
    public synchronized void addUser(String username, String password) throws DuplicateUserException {
        if (!userList.getUserList().containsKey(username)) {
            Username addUsername = new Username(username);
            Password addPassword = new Password(password);
            User toAdd = new User(addUsername, addPassword);
            userList.add(toAdd);
        }
    }

    public synchronized void addUser(User user) throws DuplicateUserException {
        userList.add(user);
    }

    @Override
    public void authenticate(String username, String password) throws DuplicateUserException {

        logger.fine("Authenticating user: " + username);
        String filepath = username + ".xml";
        if (userList.getUserList().containsKey(username)) {
            if (userList.getUserList().get(username).getPassword().getPassword().equals(password)) {
                loginUser(filepath);
            } else {
                throw new DuplicateUserException();
            }
        } else {
            addUser(username, password);
            loginUser(filepath);
        }

    }

    public ObservableList<User> getUserList() {
        return userList.asObservableList();
    }

    @Override
    public void loginUser(String filePath) {
        String properFilePath = userPrefs.getAddressBookFilePath() + filePath;
        AddressBookStorage addressBookStorage = new XmlAddressBookStorage(properFilePath);
        storage = new StorageManager(addressBookStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        ui.start(primaryStage);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or an empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook initialData;
        try {
            addressBookOptional = storage.readAddressBook();
            if (!addressBookOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample AddressBook");
            }
            initialData = addressBookOptional.orElseGet(SampleDataUtil::getSampleAddressBook);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AddressBook");
            initialData = new AddressBook();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initialData = new AddressBook();
        }

        return new ModelManager(initialData, userPrefs);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LoginManager // instanceof handles nulls
                && this.userList.equals(((LoginManager) other).userList));
    }
}
```
###### \java\seedu\address\model\user\exceptions\DuplicateUserException.java
``` java
/**
 * Signals that the operation will result in duplicate User objects.
 */
public class DuplicateUserException extends DuplicateDataException {
    public DuplicateUserException() {
        super("Operation would result in duplicate users");
    }
}
```
###### \java\seedu\address\model\user\exceptions\UserNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified user.
 */
public class UserNotFoundException extends Exception {}
```
###### \java\seedu\address\model\user\Password.java
``` java
/**
 * Represents a User's stored password in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPassword(String)}
 */
public class Password {

    private static  final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_PASSWORD_CONSTRAINTS = "Password should adhere to the following constraints:\n"
            + "Password must only consist of alphanumeric characters.\n"
            + "Minimum length: 8 characters.\n"
            + "Maximum length: 30 characters.\n"
            + "Should not contain any of the special characters: ( " + SPECIAL_CHARACTERS + " ).\n";

    // alphanumeric and special characters
    private static final String PASSWORD_REGEX = "[^\\W_]{8,30}$"; // alphanumeric characters except underscore
    public static final String PASSWORD_VALIDATION_REGEX = PASSWORD_REGEX;

    public final String password;

    /**
     * Constructs a {@code Password}.
     *
     * @param password A valid password.
     */
    public Password(String password) {
        requireNonNull(password);
        checkArgument(isValidPassword(password), MESSAGE_PASSWORD_CONSTRAINTS);
        this.password = password;
    }

    /**
     * Returns if a given string is a valid password.
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return password;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Username // instanceof handles nulls
                && this.password.equals(((Password) other).password)); // state check
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }

}
```
###### \java\seedu\address\model\user\UniqueUserList.java
``` java
/**
 * A list of users that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see User#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueUserList implements Iterable<User> {

    private final HashMap<String, User> userList = new HashMap<String, User>();
    private final ObservableList<User> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains a equivalent user as the given argument.
     */
    public boolean contains(String toCheck) {
        requireNonNull(toCheck);
        return userList.containsKey(toCheck);
    }

    /**
     * Adds a user to the hashmap
     * @param toAdd
     * @throws DuplicateUserException
     */
    public void add(User toAdd) throws DuplicateUserException {
        requireNonNull(toAdd);
        if (userList.containsKey(toAdd.getUsername().getUsername())) {
            throw new DuplicateUserException();
        }
        userList.put(toAdd.getUsername().getUsername(), toAdd);
        internalList.add(toAdd);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueUserList // instanceof handles nulls
                && this.internalList.equals(((UniqueUserList) other).internalList));
    }

    @Override
    public int hashCode() {
        return userList.hashCode();
    }

    public ObservableList<User> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    public HashMap<String, User> getUserList() {
        return userList;
    }

    @Override
    public Iterator<User> iterator() {
        return internalList.iterator();
    }
}
```
###### \java\seedu\address\model\user\User.java
``` java
/**
 * Represents a User registered in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class User implements Comparable<User> {

    private final Username username;
    private final Password password;

    /**
     * Every field must be present and not null.
     */
    public User(Username username, Password password) {
        requireAllNonNull(username, password);
        this.username = username;
        this.password = password;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password; }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof User)) {
            return false;
        }

        User otherUser = (User) other;
        return otherUser.getUsername().equals(this.getUsername())
                && otherUser.getPassword().equals(this.getPassword());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Username: ")
                .append(getUsername())
                .append(" Password: ")
                .append(getPassword());
        return builder.toString();
    }

    public int compareTo(User otherUser) {
        return this.username.toString().compareTo(otherUser.username.toString());
    }

}
```
###### \java\seedu\address\model\user\Username.java
``` java
/**
 * Represents a User's stored username in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUsername(String)}
 */
public class Username {

    private static  final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_USERNAME_CONSTRAINTS = "Username should adhere to the following constraints:\n"
            + "Username must only consist of alphanumeric characters.\n"
            + "Minimum length: 3 characters.\n"
            + "Maximum length: 15 characters.\n"
            + "Should not contain any of the special characters: ( " + SPECIAL_CHARACTERS + " ).\n";

    // alphanumeric and special characters
    private static final String USERNAME_REGEX = "[^\\W_]{3,15}$"; // alphanumeric characters except underscore
    public static final String USERNAME_VALIDATION_REGEX = USERNAME_REGEX;

    public final String username;

    /**
     * Constructs a {@code Username}.
     *
     * @param username A valid username.
     */
    public Username(String username) {
        requireNonNull(username);
        checkArgument(isValidUsername(username), MESSAGE_USERNAME_CONSTRAINTS);
        this.username = username;
    }

    /**
     * Returns if a given string is a valid username.
     */
    public static boolean isValidUsername(String test) {
        return test.matches(USERNAME_VALIDATION_REGEX);
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Username // instanceof handles nulls
                && this.username.equals(((Username) other).username)); // state check
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

}
```
###### \java\seedu\address\model\util\SampleLoginDataUtil.java
``` java
/**
 * Contains utility methods for populating {@code LoginManager} with sample data users.
 */
public class SampleLoginDataUtil {
    public static LoginManager getSampleLoginManager() {

        LoginManager sampleLoginManager = new LoginManager();

        return sampleLoginManager;
    }
}
```
###### \java\seedu\address\storage\LoginStorageManager.java
``` java
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
        return "login.xml";
    }

    public Optional<LoginManager> readLogin() throws DataConversionException, IOException {
        return readLogin(xmlLoginStorage.getLoginFilePath());
    }

    /**
     *
     * @param filePath
     * @return
     * @throws DataConversionException
     * @throws IOException
     */
    public Optional<LoginManager> readLogin(String filePath) throws DataConversionException, IOException {
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
```
###### \java\seedu\address\storage\XmlAdaptedUser.java
``` java
/**
 * JAXB-friendly version of the User.
 */
public class XmlAdaptedUser {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "User's %s field is missing!";

    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String password;

    /**
     * Constructs an XmlAdaptedUser.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedUser() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Converts a given User into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedUser
     */
    public XmlAdaptedUser(User source) {
        username = source.getUsername().getUsername();
        password = source.getPassword().getPassword();
    }

    /**
     * Converts this jaxb-friendly adapted user object into the model's User object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public User toModelType() throws IllegalValueException {
        if (this.username == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Username.class.getSimpleName()));
        }
        if (!Username.isValidUsername(this.username)) {
            throw new IllegalValueException(Username.MESSAGE_USERNAME_CONSTRAINTS);
        }
        final Username username = new Username(this.username);

        if (this.password == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Password.class.getSimpleName()));
        }
        if (!Password.isValidPassword(this.password)) {
            throw new IllegalValueException(Password.MESSAGE_PASSWORD_CONSTRAINTS);
        }
        final Password password = new Password(this.password);
        return new User(username, password);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedUser)) {
            return false;
        }

        XmlAdaptedUser otherUser = (XmlAdaptedUser) other;
        return Objects.equals(username, otherUser.username)
                && Objects.equals(password, otherUser.password);
    }
}
```
###### \java\seedu\address\storage\XmlLoginFileStorage.java
``` java
/**
 * Stores addressbook data in an XML file
 */
public class XmlLoginFileStorage {
    /**
     * Saves the given addressbook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableLogin login)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, login);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableLogin loadDataFromSaveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableLogin.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
```
###### \java\seedu\address\storage\XmlLoginStorage.java
``` java
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

    private Optional<LoginManager> readLogin() throws DataConversionException, IOException {
        return readLogin(filePath);
    }

    /**
     * Similar to {@link #readLogin()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<LoginManager> readLogin(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File loginFile = new File(filePath);

        if (!loginFile.exists()) {
            logger.info("AddressBook file "  + loginFile + " not found");
            return Optional.empty();
        }

        XmlSerializableLogin xmlSerializableLogin = XmlLoginFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlSerializableLogin.toModelType());
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
```
###### \java\seedu\address\storage\XmlSerializableLogin.java
``` java
/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableLogin {

    @XmlElement
    private List<XmlAdaptedUser> users;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableLogin() {
        users = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableLogin(LoginManager src) {
        this();
        users.addAll(src.getUserList().stream().map(XmlAdaptedUser::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson} or {@code XmlAdaptedTag}.
     */
    public LoginManager toModelType() throws IllegalValueException {
        LoginManager loginManager = new LoginManager();
        for (XmlAdaptedUser u : users) {
            loginManager.addUser(u.toModelType());
        }
        return loginManager;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableLogin)) {
            return false;
        }

        XmlSerializableLogin otherLogin = (XmlSerializableLogin) other;
        return users.equals(otherLogin.users);
    }

}
```
###### \java\seedu\address\ui\Login.java
``` java
/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class Login extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "Login.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final LoginManager login;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;


    @FXML
    private Button loginButton;

    public Login(LoginManager login) {
        super(FXML);
        this.login = login;
        loginButton.setDefaultButton(true);
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            login.authenticate(username.getText(), password.getText());
        } catch (DuplicateUserException e) {
            e.printStackTrace();
        }
    }
}
```
###### \java\seedu\address\ui\LoginMainWindow.java
``` java
/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class LoginMainWindow extends UiPart<Stage> {

    private static final String FXML = "LoginMainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Stage primaryStage;
    private LoginManager loginManager;

    private Login login;

    @FXML
    private StackPane loginPlaceholder;

    public LoginMainWindow(Stage primaryStage, LoginManager loginManager) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.loginManager = loginManager;

        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        login = new Login(loginManager);
        loginPlaceholder.getChildren().add(login.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }
}
```
###### \java\seedu\address\ui\LoginUiManager.java
``` java
/**
 * The manager of the UI component.
 */
public class LoginUiManager extends ComponentManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";
    public static final String FILE_OPS_ERROR_DIALOG_STAGE_TITLE = "File Op Error";
    public static final String FILE_OPS_ERROR_DIALOG_HEADER_MESSAGE = "Could not save data";
    public static final String FILE_OPS_ERROR_DIALOG_CONTENT_MESSAGE = "Could not save data to file";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/address_book_32.png";

    private LoginManager loginManager;
    private LoginMainWindow loginMainWindow;

    public LoginUiManager(LoginManager login) {
        super();
        this.loginManager = login;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");

        //Set the application icon.
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));

        try {
            loginMainWindow = new LoginMainWindow(primaryStage, loginManager);
            loginMainWindow.show(); //This should be called before creating other UI parts
            loginMainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    @Override
    public void stop() {
        loginMainWindow.hide();
    }

    private void showFileOperationAlertAndWait(String description, String details, Throwable cause) {
        final String content = details + ":\n" + cause.toString();
        showAlertDialogAndWait(AlertType.ERROR, FILE_OPS_ERROR_DIALOG_STAGE_TITLE, description, content);
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    void showAlertDialogAndWait(AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(loginMainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    //==================== Event Handling Code ===============================================================

    @Subscribe
    private void handleDataSavingExceptionEvent(DataSavingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showFileOperationAlertAndWait(FILE_OPS_ERROR_DIALOG_HEADER_MESSAGE, FILE_OPS_ERROR_DIALOG_CONTENT_MESSAGE,
                event.exception);
    }
}
```
###### \resources\view\LightTheme.css
``` css
.background {
    -fx-background-color: derive(#e3e3e3, 20%);
    background-color: #c7c7c7; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #ababab;
    -fx-opacity: 0.9;
}

.label-date {
    -fx-text-fill: black;
}

.label-small {
    -fx-font-size: 9pt;
    -fx-text-fill: black;
    -fx-background-color: grey;
}

.label-small-red {
    -fx-font-size: 9pt;
    -fx-text-fill: black;
    -fx-background-color: derive(#d26a6a, 20%);
}

.label-small-yellow {
    -fx-font-size: 9pt;
    -fx-text-fill: black;
    -fx-background-color: derive(#e4d858, 20%);
}

.label-small-green {
    -fx-font-size: 9pt;
    -fx-text-fill: black;
    -fx-background-color: derive(#8ac973, 20%);
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #1d1d1d;
    -fx-control-inner-background: #e3e3e3;
    -fx-background-color: #e3e3e3;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#e3e3e3, 20%);
    -fx-border-color: transparent transparent transparent #b3b3b3;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#e3e3e3, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#e3e3e3, 20%);
}

.list-view-calendar {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#e3e3e3, 20%);
}

.list-view-calendar .scroll-bar:horizontal ,
.list-view-calendar  .scroll-bar:vertical{
    -fx-background-color:transparent;

}
list-view-calendar .list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-view-calendar .list-cell:filled:even {
    -fx-background-color: derive(#e3e3e3, 20%);
}

.list-view-calendar .list-cell:filled:odd {
    -fx-background-color: derive(#e3e3e3, 20%);
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: #c1c2c3;
}

.list-cell:filled:odd {
    -fx-background-color: #c2c5c7;
}

.list-cell:filled:selected {
    -fx-background-color: #9faabc;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #6daac0;
    -fx-border-width: 1;
}

.list-cell:empty {
    /* Empty cells will not have alternating colours */
    -fx-background: #e3e3e3;
}

.list-cell .label {
    -fx-text-fill: black;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: #fbfefd;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 13px;
    -fx-text-fill: #fbfefd;
}

.calendar_text {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: black;
}

.calendar_title {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 20px;
    -fx-text-fill: black;
}

.calendar-grid-pane {
    -fx-background-color: derive(#e3e3e3, 20%);
}

.anchor-pane {
     -fx-background-color: derive(#e3e3e3, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#e3e3e3, 20%);
     -fx-border-color: grey;
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#e3e3e3, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: white !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
}

.status-bar-with-border {
    -fx-background-color: derive(#e3e3e3, 30%);
    -fx-border-color: derive(#e3e3e3, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: black;
}

.grid-pane {
    -fx-background-color: derive(#e3e3e3, 30%);
    -fx-border-color: derive(#e3e3e3, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#e3e3e3, 30%);
}

.context-menu {
    -fx-background-color: derive(#e3e3e3, 50%);
}

.context-menu .label {
    -fx-text-fill: black;
}

.menu-bar {
    -fx-background-color: derive(#e3e3e3, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: white;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: darkgrey, darkgrey;
    -fx-background-radius: 0;
    -fx-background-color: darkgrey;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #262626;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #c4c4c4;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: black;
  -fx-text-fill: #e3e3e3;
}

.button:focused {
    -fx-border-color: black, black;
    -fx-border-radius: 0, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #e3e3e3;
    -fx-text-fill: black;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #000000;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #e3e3e3;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #e3e3e3;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: black;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#e3e3e3, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: black;
    -fx-text-fill: black;
}

.scroll-bar {
    -fx-background-color: derive(#e3e3e3, 20%);
}

.scroll-bar .thumb {
    -fx-background-color: derive(#e3e3e3, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #f20713;
}

#commandTextField {
    -fx-background-color: transparent #c7c7c7 transparent #c7c7c7;
    -fx-background-insets: 0;
    -fx-border-color: #c7c7c7 #c7c7c7 #000000 #c7c7c7;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, white, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, #c7c7c7, transparent, #c7c7c7;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

#tags .teal {
    -fx-text-fill: black;
    -fx-background-color: #6daac0;
}

#tags .red {
    -fx-text-fill: black;
    -fx-background-color: red;
}

#tags .yellow {
    -fx-background-color: yellow;
    -fx-text-fill: black;
}

#tags .blue {
    -fx-text-fill: black;
    -fx-background-color: blue;
}

#tags .orange {
    -fx-text-fill: black;
    -fx-background-color: orange;
}

#tags .brown {
    -fx-text-fill: white;
    -fx-background-color: brown;
}

#tags .green {
    -fx-text-fill: black;
    -fx-background-color: green;
}

#tags .pink {
    -fx-text-fill: black;
    -fx-background-color: pink;
}

#tags .black {
    -fx-text-fill: white;
    -fx-background-color: black;
}

#tags .grey {
    -fx-text-fill: black;
    -fx-background-color: grey;
}

.tab-pane {
    -fx-padding: 0 0 0 1;
    -fx-background-color: #cbd2dc;
}

.tab-pane .tab-header-area {
    -fx-background-color: #cbd2dc;
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.tab-pane .tab-header-area .tab-header-background {
    -fx-opacity: 0;
}

.tab-pane {
    -fx-tab-min-width:150px;
}

.tab {
    -fx-background-insets: 0 1 0 1,0,0;
}

.tab-pane .tab {
    -fx-background-color: #bfbfbf;

}

.tab-pane .tab:selected {
    -fx-border-color: transparent !important;
    -fx-background-color: #97a1a1;
}

.tab .tab-label {
    -fx-alignment: CENTER;
    -fx-text-fill: #0d0d0d;
    -fx-font-size: 12px;
    -fx-font-weight: bold;
}

.tab:selected .tab-label {
    -fx-border-color: transparent !important;
    -fx-text-fill: black;
}
```
###### \resources\view\Login.fxml
``` fxml
<?import javafx.geometry.*?>
<?import javafx.scene.text.*?>
<?import java.lang.*?>
<?import java.util.*?>
<?import javafx.scene.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<Pane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="300.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <children>
        <VBox prefHeight="300.0" prefWidth="600.0">
            <children>
                <Label alignment="CENTER" prefHeight="100.0" prefWidth="600.0" text="Welcome!" textAlignment="CENTER">
                    <font>
                        <Font size="24.0" />
                    </font>
                </Label>
                <HBox prefHeight="50.0" prefWidth="600.0">
                    <children>
                        <Label alignment="CENTER_RIGHT" prefHeight="50.0" prefWidth="250.0" text="Username:" textAlignment="CENTER" />
                        <TextField fx:id="username" promptText="username">
                            <HBox.margin>
                                <Insets left="5.0" top="14.0" />
                            </HBox.margin>
                        </TextField>
                    </children>
                </HBox>
                <HBox prefHeight="70.0" prefWidth="200.0">
                    <children>
                        <Label alignment="CENTER_RIGHT" prefHeight="70.0" prefWidth="250.0" text="Password:" textAlignment="CENTER" />
                        <PasswordField fx:id="password" promptText="password">
                            <HBox.margin>
                                <Insets left="5.0" top="23.0" />
                            </HBox.margin>
                        </PasswordField>
                    </children>
                </HBox>
                <HBox prefHeight="80.0" prefWidth="200.0">
                    <children>
                        <Button fx:id="loginButton" alignment="CENTER_RIGHT" mnemonicParsing="false" onAction="#handleCommandInputChanged" text="Login">
                            <HBox.margin>
                                <Insets left="440.0" top="20.0" />
                            </HBox.margin>
                        </Button>
                    </children>
                </HBox>
            </children>
        </VBox>
    </children>
</Pane>
```
###### \resources\view\LoginMainWindow.fxml
``` fxml
<?import java.net.URL?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.Scene?>
<?import javafx.scene.control.Menu?>
<?import javafx.scene.control.MenuBar?>
<?import javafx.scene.control.MenuItem?>
<?import javafx.scene.control.SplitPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>

<?import javafx.scene.control.TabPane?>
<?import javafx.scene.control.Tab?>
<fx:root type="javafx.stage.Stage" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
         minWidth="450" minHeight="600">
    <icons>
        <Image url="@/images/address_book_32.png" />
    </icons>
    <scene>
        <Scene>
            <stylesheets>
                <URL value="@LightTheme.css" />
                <URL value="@Extensions.css" />
            </stylesheets>
            <StackPane fx:id="loginPlaceholder">
            </StackPane>
        </Scene>
    </scene>
</fx:root>
```
