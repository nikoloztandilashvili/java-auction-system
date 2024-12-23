import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {

    @FXML
    private VBox auctionCardsContainer;

    @FXML
    private Label userEmail;

    @FXML
    private Label userName;


    private User user;

    private AuctionManager auctionManager;

    private Main mainApplication;
    private User loggedInUser;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());

        auctionManager = new AuctionManager();

        ArrayList<Auction> auctions = auctionManager.getAuctionsByUser(user);

        displayAuctions(auctions);
    }



    private void displayAuctions(ArrayList<Auction> auctions) {
        auctionCardsContainer.getChildren().clear();

        for (Auction auction : auctions) {
            createAuctionCard(auction);
        }
    }

    private void createAuctionCard(Auction auction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserPageCard.fxml"));
            Node auctionCard = loader.load();
            UserPageCardController userPageCardController = loader.getController();
            userPageCardController.setUser(loggedInUser);
            userPageCardController.setAuctionData(auction);
            auctionCardsContainer.getChildren().add(auctionCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
