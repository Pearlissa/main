package seedu.address.ui;

import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;


/**
 * Create an anchor pane that can store additional data.
 */
public class CalendarNode extends AnchorPane {

    // Deadline associated with this pane
    private LocalDate date;

    /**
     * Create a anchor pane node. Deadline is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public CalendarNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> System.out.println("This pane's date is: " + this.getDate()));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

