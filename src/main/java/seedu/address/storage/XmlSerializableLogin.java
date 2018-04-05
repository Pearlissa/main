package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.LoginManager;

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
