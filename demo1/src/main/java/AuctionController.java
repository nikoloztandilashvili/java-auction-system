import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AuctionController {

    AuctionManager auctionManager = new AuctionManager();

    private User seller;
    public User loggedInUser;


    @FXML
    private TextField auctionNameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField startingBidTextField;

    @FXML
    private Button createAuctionButton;

    @FXML
    private DatePicker startTimePicker;

    @FXML
    private DatePicker endTimePicker;

    @FXML
    private Button backButton;

    private Auction auction;

    private Main main;


    public void setMain(Main main) {
        this.main = main;
    }



    public void setAuctionManager(AuctionManager auctionManager) {
        this.auctionManager = auctionManager;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }



    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("auctions_page.fxml"));
            Parent root = loader.load();

            System.out.println("user: " + loggedInUser);
            System.out.println("main: " + main);

            AuctionsPageController auctionsPageController = loader.getController();
            auctionsPageController.setLoggedInUser(loggedInUser);
            auctionsPageController.setMainApplication(main);

            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleCreateAuction() {
        seller = loggedInUser;
        String name = auctionNameTextField.getText();
        String description = descriptionTextArea.getText();
        double startingBid = Double.parseDouble(startingBidTextField.getText());

        LocalDate startDate = startTimePicker.getValue();
        LocalDate endDate = endTimePicker.getValue();

        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 59);

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        System.out.println(name);
        System.out.println(description);
        System.out.println(startingBid);
        System.out.println(startDateTime);
        System.out.println(endDateTime);
        System.out.println(seller.getUsername());

        Auction auction = auctionManager.createAuction(seller, name, description, startingBid, startDateTime, endDateTime, false);


    }
}
