package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class CalendarViewHandle extends NodeHandle<Node> {
    public static final String CALENDAR_TITLE_ID = "#calendarTitle";

    private final Label calendarTitleLabel;

    public CalendarViewHandle(Node calendarViewNode) {
        super(calendarViewNode);

        this.calendarTitleLabel = getChildNode(CALENDAR_TITLE_ID);
    }

    public String getCalendarTitle() {
        return calendarTitleLabel.getText();
    }
}
