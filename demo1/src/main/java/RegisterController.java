import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordRegisterField;

    @FXML
    private Button registerButton;

    private UserDao userDao;

    public void initialize() {
        userDao = new UserDao(new DatabaseHandler());
    }

    @FXML
    public void handleRegister() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordRegisterField.getText();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        User user = new User(0, name, username, email, password, UserType.BUYER);

        if (userDao.addUser(user)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Registration successful!");
            alert.showAndWait();
            closeWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Registration failed. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void LoginButtonHandle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void closeWindow() {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }

}
