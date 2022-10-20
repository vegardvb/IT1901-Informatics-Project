package bibtek.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bibtek.core.User;
import bibtek.json.StorageHandler;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController extends SceneChangerController implements Initializable {

    @FXML
    Button logInButton;

    @FXML
    TextField userNameInput;

    @FXML
    Label createNewUserLabel;

    @FXML
    Label errorLabel;

    private StorageHandler storageHandler;

    /**
     * Initializes the scene.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        createNewUserLabel.setOnMouseClicked((agent) -> this.createNewUser());
        storageHandler = new StorageHandler();

    }

    /**
     * Checks if there is a user of written username and logs them in. Then sends
     * the user to the library page.
     */
    @FXML
    public void logIn() {

        final Stage stage = (Stage) logInButton.getScene().getWindow();

        final String username = userNameInput.getText();
        final User user = storageHandler.getUser(username);

        if (user == null) {
            ToastUtil.makeToast(stage, Toast.ToastState.INFO, "No user with given username");
            return;
        }

        update(user);

        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
        }
    }

    /**
     * Links the user to the page that creates a new user.
     */
    @FXML
    public void createNewUser() {

        final Stage stage = (Stage) createNewUserLabel.getScene().getWindow();
        try {
            this.changeScene(stage, "/bibtek/ui/fxml/CreateUser.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing the create user page");
            e.printStackTrace();
        }

    }
}
