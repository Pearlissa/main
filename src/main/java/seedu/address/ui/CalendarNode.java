package seedu.address.ui;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.task.Task;


/**
 * Create an anchor pane that can store additional data.
 */
public class CalendarNode extends UiPart<Region> {

    private static final String FXML = "CalendarNode.fxml";

    @FXML
    private Label date;

    @FXML
    private ListView<CalendarTaskCard> tasks;

    /**
     * Create a calendar node.
     * @param txt the date of the node
     * @param taskList the task list linked to it
     */
    public CalendarNode(String txt, ObservableList<Task> taskList, int day, int month, int year) {
        super(FXML);
        date.setText(txt);
        setConnections(taskList, day, month, year);
    }

    private void setConnections(ObservableList<Task> taskList, int day, int month, int year) {
        ObservableList<CalendarTaskCard> mappedList = EasyBind.map(
                taskList, (task) -> mapFunction(task, day, month, year));
        tasks.setItems(mappedList);
        tasks.setCellFactory(listView -> new TasksCell());
    }

    /**
     * Only maps tasks due on that date.
     * @param task
     * @param month
     * @param year
     * @return
     */
    private CalendarTaskCard mapFunction(Task task, int day, int month, int year) {
        if (task.getDeadlineDay() == day && task.getDeadlineMonth() == month && task.getDeadlineYear() == year) {
            return new CalendarTaskCard(task);
        } else {
            return null;
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code CalendarTaskCard}.
     */
    class TasksCell extends ListCell<CalendarTaskCard> {

        @Override
        protected void updateItem(CalendarTaskCard task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(task.getRoot());
            }
        }
    }

}
