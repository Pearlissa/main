package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.NamingUtil;
import seedu.address.storage.DisplayPicStorage;

/**
 * Represents the filepath of a Person's displayPic in the address book.
 */
public class DisplayPic {

    public static final String DEFAULT_DISPLAY_PIC = "/images/displayPic/default.png";
    public static final String DEFAULT_IMAGE_LOCATION = "data/displayPic/";

    public final String originalPath;
    private String value;

    public DisplayPic() {
        this.originalPath = DEFAULT_DISPLAY_PIC;
        this.value = DEFAULT_DISPLAY_PIC;
    }

    /**
     * Constructs an {@code DisplayPic}.
     *
     * @param filePath A valid string containing the path to the file.
     */
    public DisplayPic(String filePath, String personDetails) throws IllegalValueException {
        requireNonNull(filePath);
        String trimmedFilePath = filePath.trim();
        this.originalPath = trimmedFilePath;
        checkArgument(DisplayPicStorage.isValidPath(trimmedFilePath),
                Messages.MESSAGE_DISPLAY_PIC_NONEXISTENT_CONSTRAINTS);
        checkArgument(DisplayPicStorage.isValidImage(trimmedFilePath), Messages.MESSAGE_DISPLAY_PIC_NOT_IMAGE);
        String fileType = FileUtil.getFileType(trimmedFilePath);
        String uniqueFileName = NamingUtil.generateUniqueName(personDetails);
        this.value = DEFAULT_IMAGE_LOCATION + uniqueFileName + '.' + fileType;
    }

    public DisplayPic(String filePath) {
        requireNonNull(filePath);
        checkArgument(DisplayPicStorage.isValidPath(filePath), Messages.MESSAGE_DISPLAY_PIC_NONEXISTENT_CONSTRAINTS);
        checkArgument(DisplayPicStorage.isValidImage(filePath), Messages.MESSAGE_DISPLAY_PIC_NOT_IMAGE);
        this.originalPath = filePath;
        this.value = filePath;
    }

    /**
     * Saves the display picture to the specified storage location.
     */
    public boolean saveDisplay(String personDetails) throws IllegalValueException {
        if (originalPath.equals(value)) {
            return true;
        }
        String fileType = FileUtil.getFileType(originalPath);
        String uniqueFileName = NamingUtil.generateUniqueName(personDetails);
        return DisplayPicStorage.saveDisplayPic(uniqueFileName, originalPath, fileType);
    }

    public void updateToDefault() {
        this.value = DEFAULT_DISPLAY_PIC;
    }

    /**
     * Updates the path the DisplayPic object points to
     * @param personDetails are the details to hash to ensure a unique value
     */
    public void updateDisplay(String personDetails) {
        if (!this.value.equals(DEFAULT_DISPLAY_PIC)) {
            String uniqueFileName = NamingUtil.generateUniqueName(personDetails);
            try {
                String fileType = FileUtil.getFileType(value);
                DisplayPicStorage.saveDisplayPic(uniqueFileName, value, fileType);
                //mark for delete the old photo(value) here
                this.value = DEFAULT_IMAGE_LOCATION + uniqueFileName + '.' + fileType;
            } catch (IllegalValueException ive) {
                assert false;
            }
        }
    }

    public boolean isDefault() {
        return value.equals(DEFAULT_DISPLAY_PIC);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DisplayPic // instanceof handles nulls
                && this.value.equals(((DisplayPic) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
