# Pearlissa
###### \java\seedu\address\logic\commands\SortCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class SortCommandTest {
    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        sortCommand = new SortCommand();
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_sortsList() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_sortsList() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_sortsEverything() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nullList_sortsList() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
```
###### \java\seedu\address\model\LoginManagerTest.java
``` java
public class LoginManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final LoginManager loginManager = new LoginManager();

    @Test
    public void equals() {

        // same values -> returns true
        LoginManager loginManager = new LoginManager();

        // same object -> returns true
        assertTrue(loginManager.equals(loginManager));

        // null -> returns false
        assertFalse(loginManager.equals(null));

        // different types -> returns false
        assertFalse(loginManager.equals(5));
    }
}
```
###### \java\seedu\address\model\UniqueUserListTest.java
``` java
public class UniqueUserListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueUserList uniqueUserList = new UniqueUserList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueUserList.asObservableList().remove(0);
    }
}
```
###### \java\seedu\address\model\user\PasswordTest.java
``` java
public class PasswordTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Password(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidPass = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Password(invalidPass));
    }

    @Test
    public void isValidPassword() {
        // null password
        Assert.assertThrows(NullPointerException.class, () -> Password.isValidPassword(null));

        // invalid password
        assertFalse(Password.isValidPassword("")); // empty string
        assertFalse(Password.isValidPassword(" ")); // spaces only
        assertFalse(Password.isValidPassword("^")); // only non-alphanumeric characters
        assertFalse(Password.isValidPassword("john*")); // contains non-alphanumeric characters
        assertFalse(Password.isValidPassword("john doe")); // contains whitespace
        assertFalse(Password.isValidPassword("abcdefg")); // contains less than 7 characters
        assertFalse(Password.isValidPassword("abcde12345abcde12345abcde12345a")); // contains more than 30 characters
        assertFalse(Password.isValidPassword("john_doe")); // contains underscore

        // valid password
        assertTrue(Password.isValidPassword("johndoe1")); // alphabets only
        assertTrue(Password.isValidPassword("12345678")); // numbers only
        assertTrue(Password.isValidPassword("JohnDoe1")); // with capital letters
        assertTrue(Password.isValidPassword("abcdefgh")); // at least 8 characters
        assertTrue(Password.isValidPassword("abcde12345abcde12345abcde12345")); // at most 30 characters
    }
}
```
###### \java\seedu\address\model\user\UsernameTest.java
``` java
public class UsernameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Username(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidUser = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(invalidUser));
    }

    @Test
    public void isValidUsername() {
        // null username
        Assert.assertThrows(NullPointerException.class, () -> Username.isValidUsername(null));

        // invalid username
        assertFalse(Username.isValidUsername("")); // empty string
        assertFalse(Username.isValidUsername(" ")); // spaces only
        assertFalse(Username.isValidUsername("^")); // only non-alphanumeric characters
        assertFalse(Username.isValidUsername("john*")); // contains non-alphanumeric characters
        assertFalse(Username.isValidUsername("john doe")); // contains whitespace
        assertFalse(Username.isValidUsername("ab")); // contains less than 3 characters
        assertFalse(Username.isValidUsername("abcdefghijklmnop")); // contains more than 15 characters
        assertFalse(Username.isValidUsername("john_doe")); // contains underscore

        // valid username
        assertTrue(Username.isValidUsername("johndoe")); // alphabets only
        assertTrue(Username.isValidUsername("12345")); // numbers only
        assertTrue(Username.isValidUsername("JohnDoe")); // with capital letters
        assertTrue(Username.isValidUsername("abc")); // at least 3 characters
        assertTrue(Username.isValidUsername("abcde12345abcde")); // at most 3 characters
    }
}
```
###### \java\seedu\address\storage\XmlSerializableLoginTest.java
``` java
public class XmlSerializableLoginTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableLoginTest/");
    private static final File TYPICAL_USERS_FILE = new File(TEST_DATA_FOLDER + "typicalUsersLogin.xml");
    private static final File INVALID_USER_FILE = new File(TEST_DATA_FOLDER + "invalidUserLogin.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalUsersFile_success() throws Exception {
        XmlSerializableLogin dataFromFile = XmlUtil.getDataFromFile(TYPICAL_USERS_FILE,
                XmlSerializableLogin.class);
        LoginManager loginFromFile = dataFromFile.toModelType();
        LoginManager typicalUsersLogin = TypicalUsers.getTypicalLoginManager();
        assertTrue(checkUsers(loginFromFile, typicalUsersLogin));
    }

    /**
     * Checks if the info on the 2 Login Managers are the same.
     * @param loginManager1
     * @param loginManager2
     * @return
     */
    private boolean checkUsers(LoginManager loginManager1, LoginManager loginManager2) {
        if (loginManager1.getUserList().size() != loginManager2.getUserList().size()) {
            return false;
        }
        for (int i = 0; i < loginManager1.getUserList().size(); i++) {
            if (!loginManager1.getUserList().get(i).getUsername().getUsername()
                    .equals(loginManager2.getUserList().get(i).getUsername().getUsername())
                    || !loginManager1.getUserList().get(i).getPassword().getPassword()
                    .equals(loginManager2.getUserList().get(i).getPassword().getPassword())) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void toModelType_invalidUserFile_throwsIllegalValueException() throws Exception {
        XmlSerializableLogin dataFromFile = XmlUtil.getDataFromFile(INVALID_USER_FILE,
                XmlSerializableLogin.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
}
```
###### \java\seedu\address\testutil\TypicalUsers.java
``` java
/**
 * A utility class containing a list of {@code User} objects to be used in tests.
 */
public class TypicalUsers {

    public static final User JOHNDOE = new UserBuilder().withUsername("JOHNDOE")
            .withPassword("12345678").build();
    public static final User ALICE = new UserBuilder().withUsername("ALICE")
            .withPassword("abcdefgh").build();
    public static final User BENSON = new UserBuilder().withUsername("BENSON")
            .withPassword("1234ABCDE").build();
    public static final User CARL = new UserBuilder().withUsername("CARL")
            .withPassword("1a2b3c4d5e").build();
    public static final User DANIEL = new UserBuilder().withUsername("DANIEL")
            .withPassword("password").build();
    public static final User ELLE = new UserBuilder().withUsername("ELLE")
            .withPassword("elleelle").build();
    public static final User FIONA = new UserBuilder().withUsername("FIONA")
            .withPassword("1223334444").build();
    public static final User GEORGE = new UserBuilder().withUsername("GEORGE")
            .withPassword("george123").build();

    // Manually added
    public static final User HOON = new UserBuilder().withUsername("HOON")
            .withPassword("meehoonkueh").build();
    public static final User IDA = new UserBuilder().withUsername("IDA")
            .withPassword("idanotaho").build();

    private TypicalUsers() {} // prevents instantiation

    /**
     * Returns a {@code LoginManager} with all the typical users.
     */
    public static LoginManager getTypicalLoginManager() {
        LoginManager lm = new LoginManager();
        for (User user : getTypicalUsers()) {
            try {
                lm.addUser(user);
            } catch (DuplicateUserException e) {
                throw new AssertionError("not possible");
            }
        }
        return lm;
    }

    public static List<User> getTypicalUsers() {
        return new ArrayList<>(Arrays.asList(JOHNDOE, ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
```
###### \java\seedu\address\testutil\UserBuilder.java
``` java
/**
 * A utility class to help with building User objects.
 */
public class UserBuilder {

    public static final String DEFAULT_USERNAME = "JOHNDOE";
    public static final String DEFAULT_PASSWORD = "12345678";

    private Username username;
    private Password password;

    public UserBuilder() {
        username = new Username(DEFAULT_USERNAME);
        password = new Password(DEFAULT_PASSWORD);
    }

    /**
     * Initializes the UserBuilder with the data of {@code userToCopy}.
     */
    public UserBuilder(User userToCopy) {
        username = userToCopy.getUsername();
        password = userToCopy.getPassword();
    }

    /**
     * Sets the {@code Userame} of the {@code User} that we are building.
     */
    public UserBuilder withUsername(String username) {
        this.username = new Username(username);
        return this;
    }

    /**
     * Sets the {@code Userame} of the {@code User} that we are building.
     */
    public UserBuilder withPassword(String password) {
        this.password = new Password(password);
        return this;
    }

    /**
     * Builds the User object
     * @return A User object
     */
    public User build() {
        return new User(username, password);
    }
}
```
