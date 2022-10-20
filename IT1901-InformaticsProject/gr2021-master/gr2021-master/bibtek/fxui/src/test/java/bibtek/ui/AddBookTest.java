package bibtek.ui;

import bibtek.core.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static bibtek.ui.TestConstants.ROBOT_PAUSE_MS;
import static bibtek.ui.TestConstants.WRITE_ROBOT_PAUSE_MILLIS;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class AddBookTest extends WireMockApplicationTest {

    private Parent parent;
    private AddBookController controller;
    private Stage stage;

    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/AddBook.fxml"));
        this.parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        controller.update(TestConstants.userDante()); // Dummy user
        stage.setScene(new Scene(parent));
        stage.show();

    }

    /**
     * Testing that the TextFields works properly, and tests if it creates the
     * Library and stores the books correctly.
     */
    @Test
    public void createBookEntryTest() {

        final TextField addBookTitleInput = (TextField) parent.lookup("#bookTitleInput");
        clickOn(addBookTitleInput).write("Finnegans Wake", WRITE_ROBOT_PAUSE_MILLIS);
        assertEquals("Finnegans Wake", addBookTitleInput.getText(), "Book Title should be \"Finnegans Wake\" ");

        final TextField addBookAuthorInput = (TextField) parent.lookup("#bookAuthorInput");
        clickOn(addBookAuthorInput).write("James Joyce", WRITE_ROBOT_PAUSE_MILLIS);
        assertEquals("James Joyce", addBookAuthorInput.getText(), "Book Author should be \"James Joyce\" ");

        final DigitsField addBookYearPublishedInput = (DigitsField) parent.lookup("#bookYearPublishedInput");
        clickOn(addBookYearPublishedInput).write("1939", WRITE_ROBOT_PAUSE_MILLIS);
        assertEquals(1939, addBookYearPublishedInput.getInputAsInt(), "Book Year should be " + 1939);

        final TextField addBookImagePathInput = (TextField) parent.lookup("#bookImagePathInput");
        clickOn(addBookImagePathInput);
        addBookImagePathInput.setText(
                "http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        assertEquals("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                addBookImagePathInput.getText(),
                "Book Cover image path should be \"http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api\"");

        final LocalDate startDate = LocalDate.of(2020, 9, 21);
        final LocalDate targetDate = LocalDate.of(2020, 9, 30);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        addBookDatePicker.setValue(startDate);
        final TextField addBookDatePickerDisplay = (TextField) parent.lookup("#bookDatePickerDisplay");
        clickOn(addBookDatePickerDisplay).sleep(ROBOT_PAUSE_MS).type(KeyCode.DOWN).sleep(ROBOT_PAUSE_MS) // 21.08.2020
                // ->
                // 28.08.2020
                .type(KeyCode.RIGHT).sleep(ROBOT_PAUSE_MS) // 28.08.2020 -> 29.08.2020
                .type(KeyCode.RIGHT).sleep(ROBOT_PAUSE_MS) // 29.08.2020 -> 30.08.2020
                .type(KeyCode.ENTER).sleep(ROBOT_PAUSE_MS) // Pick date
                .type(KeyCode.ESCAPE); // Hide datepicker
        assertEquals(targetDate, addBookDatePicker.getValue(), "Book date acquired should be " + targetDate);

        final ComboBox<BookReadingState> addBookReadingStateCombo = (ComboBox<BookReadingState>) parent
                .lookup("#bookReadingStateCombo");
        clickOn(addBookReadingStateCombo).sleep(ROBOT_PAUSE_MS).press(KeyCode.DOWN).sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.ENTER); // Select second element
        assertEquals(BookReadingState.READING, addBookReadingStateCombo.getValue(),
                "BookReadingState should be READING");

        // Mock request response
        stubFor(put(urlEqualTo("/bibtek/users/dante"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        // Create book entry
        clickOn("#confirmAddBookButton");

        // Testing if it has created a correct library object with the given input
        final BookEntry expectedEntry = new BookEntry(new Book("Finnegans Wake", "James Joyce", 1939,
                "http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api"),
                LocalDate.of(2020, 9, 30), BookReadingState.READING);
        final Set<BookEntry> expectedEntries = controller.getUser().getLibrary().getBookEntries();
        expectedEntries.add(expectedEntry);
        final Library library = controller.getUser().getLibrary();
        final String expected = expectedEntries.stream().map(BookEntry::toString).reduce("", (a, b) -> a + b);
        final String actual = library.getBookEntries().stream().map(BookEntry::toString).reduce("",
                (a, b) -> a + b);
        assertEquals(expected, actual, "Expected book entries was not equal to the actual book entries");

    }

    /**
     * Make sure ISBN field is accessible and functional.
     */
    // @Test
    // public void validISBNSearchTest() {

    // clickOn("#showSearchButton").sleep(ROBOT_PAUSE_MS);

    // // Make sure popup container is visible
    // assertTrue(parent.lookup("#addBookISBNContainer").isManaged());

    // // Make sure field is accessible and
    // final DigitsField searchBookField = (DigitsField)
    // parent.lookup("#searchBookField");

    // // Make sure field is digits-only
    // clickOn(searchBookField).write("9rty7807xz6539Q486kj6",
    // WRITE_ROBOT_PAUSE_MILLIS);
    // assertEquals("9780765394866", searchBookField.getText(), "ISBN input should
    // be 9780765394866");

    // clickOn("#addBookshowSearchButton");

    // clickOn("#showSearchButton");

    // // Make sure data is retrieved and loaded
    // assertEquals("Ender's Game", ((TextField)
    // parent.lookup("#bookTitleInput")).getText(),
    // "Book Title should be Ender's Game");
    // assertEquals("Orson Scott Card", ((TextField)
    // parent.lookup("#bookAuthorInput")).getText(),
    // "Book Author should be Orson Scott Card");
    // assertEquals(2017, ((DigitsField)
    // parent.lookup("#bookYearPublishedInput")).getInputAsInt(),
    // "Book Year should be " + 2017);
    // assertEquals("http://books.google.com/books/content?id=jaM7DwAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
    // ((TextField) parent.lookup("#bookImagePathInput")).getText(),
    // "Book Cover image path should be
    // \"http://books.google.com/books/content?id=jaM7DwAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api\"");

    // }

    // @Test
    // public void invalidISBNSearchTest() {

    // clickOn("#showSearchButton").sleep(ROBOT_PAUSE_MS);

    // final DigitsField searchBookField = (DigitsField)
    // parent.lookup("#searchBookField");
    // clickOn(searchBookField).write("1513852", WRITE_ROBOT_PAUSE_MILLIS);
    // clickOn("#addBookshowSearchButton");
    // FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "Looks like an invalid
    // ISBN", parent);

    // }

    // @Test
    // public void noResultsSearchTest() {

    // clickOn("#showSearchButton").sleep(ROBOT_PAUSE_MS);

    // final TextField searchBookField = (TextField)
    // parent.lookup("#searchBookField");
    // clickOn(searchBookField).write("1234567890", WRITE_ROBOT_PAUSE_MILLIS);
    // clickOn("#searchBookButton");
    // FxTestUtil.assertToast(Toast.ToastState.INFO, "No results for that search
    // :(", parent);

    // }

    /**
     * Testing if the year published field does not register letters.
     */
    @Test
    public void yearPublishedInputTest() {
        // testing if the year published field does not register letters
        final TextField addBookYearPublishedInput = (TextField) parent.lookup("#bookYearPublishedInput");
        addBookYearPublishedInput.setText("Hello123World");
        assertEquals("123", addBookYearPublishedInput.getText(),
                "This is a numbers only field, letters are not allowed");

    }

    /**
     * Make sure date picker text field displays same date as in date picker.
     */
    @Test
    public void datePickerDisplayInputTest() {

        final LocalDate timeStamp = LocalDate.of(2020, 9, 30);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        addBookDatePicker.setValue(timeStamp);
        final TextField addBookDatePickerDisplay = (TextField) parent.lookup("#bookDatePickerDisplay");
        assertEquals(addBookDatePicker.getConverter().toString(timeStamp), addBookDatePickerDisplay.getText(),
                "Book date acquired displayed in text field should be 30.09.2020");

    }

    /**
     * Make sure all possible values are represented in dropdown.
     */
    @Test
    public void bookReadingStateDropdownTest() {

        final ComboBox<BookReadingState> addBookReadingStateCombo = (ComboBox<BookReadingState>) parent
                .lookup("#bookReadingStateCombo");
        assertEquals(List.of(BookReadingState.values()),
                List.of(addBookReadingStateCombo.getItems().toArray()));

    }

    /**
     * Make sure user can change their mind and go back to their library without
     * adding a book.
     */
    @Test
    public void backToLibraryTest() {

        clickOn("#cancelButton");

        final AnchorPane libraryRoot = (AnchorPane) stage.getScene().getRoot().lookup("#libraryRoot");
        assertNotNull(libraryRoot);
        assertTrue(libraryRoot.isVisible());

    }

}
