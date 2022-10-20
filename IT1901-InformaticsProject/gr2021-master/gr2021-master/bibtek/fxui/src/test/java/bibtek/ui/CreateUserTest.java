package bibtek.ui;

import bibtek.core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserTest extends WireMockApplicationTest {

    private Parent parent;
    private Stage stage;

    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/CreateUser.fxml"));
        this.parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Test
    public void usernameMismatchTest() {

        clickOn("#userNameInput").write("heinrich");
        clickOn("#userNameConfirmInput").write("wilhelm");

        clickOn("#createUserButton");

        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "Usernames don't match", parent);

    }

    @Test
    public void usernameEmptyTest(){

        clickOn("#createUserButton");

        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "Username required", parent);

    }

    @Test
    public void badAgeTest() {

        ((TextField) parent.lookup("#userNameInput")).setText("heinrich");
        ((TextField) parent.lookup("#userNameConfirmInput")).setText("heinrich");

        // No age
        clickOn("#createUserButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "You must enter a valid age", parent);

        // Age under 13
        clickOn("#ageInput").write("8");
        clickOn("#createUserButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "You must be at least 13 years old", parent);

    }

    @Test
    public void declineTermsTest() {

        ((TextField) parent.lookup("#userNameInput")).setText("heinrich");
        ((TextField) parent.lookup("#userNameConfirmInput")).setText("heinrich");
        ((DigitsField) parent.lookup("#ageInput")).setText("17");

        clickOn("#createUserButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "You must consent to the terms", parent);

    }

    @Test
    public void createUserTest(){

        ((TextField) parent.lookup("#userNameInput")).setText("heinrich");
        ((TextField) parent.lookup("#userNameConfirmInput")).setText("heinrich");
        ((DigitsField) parent.lookup("#ageInput")).setText("17");
        clickOn("#confirmCheckbox");

        // Mock request response
        stubFor(put(urlEqualTo("/bibtek/users/heinrich"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        clickOn("#createUserButton");

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#loginRoot"),"Failed to create user");

    }

    /**
     * Make sure user can view terms page
     */
    @Test
    public void showAndCloseTermsTest() {

        clickOn("#termsLabel");

        final AnchorPane termsRoot = (AnchorPane) stage.getScene().getRoot().lookup("#termsRoot");
        assertNotNull(termsRoot);
        assertTrue(termsRoot.isVisible());

        clickOn("#close");

        final AnchorPane createUserRoot = (AnchorPane) stage.getScene().getRoot().lookup("#createUserRoot");
        assertNotNull(createUserRoot);
        assertTrue(createUserRoot.isVisible());

    }

    /**
     * Make sure user can return to login page.
     */
    @Test
    public void backToLoginTest() {

        clickOn("#cancelButton");

        final AnchorPane loginRoot = (AnchorPane) stage.getScene().getRoot().lookup("#loginRoot");
        assertNotNull(loginRoot);
        assertTrue(loginRoot.isVisible());

    }

}
