import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class AuctionCardController {

    private User user;

    private Auction selectedAuction;

    private Main mainApplication;

    private AuctionController auctionController;

    public void setAuctionController(AuctionController auctionController) {
        this.auctionController = auctionController;
    }

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label auctionNameLabel;

    @FXML
    private Label auctionDescriptionLabel;

    @FXML
    private Label auctionStartingBidLabel;

    public void setMainApplication(Main mainApplication) {
        this.mainApplication = mainApplication;
    }

    public void setUser(User loggedInUser) {
        this.user = loggedInUser;
    }

    public void setAuctionData(Auction auction) {
        AuctionManager auctionManager = new AuctionManager();
        ArrayList<Auction> auctions = auctionManager.getActiveAuctions();

        Optional<Auction> foundAuction = auctions.stream()
                .filter(a -> a.getId() == auction.getId())
                .findFirst();

        if (foundAuction.isPresent()) {
            Auction loadedAuction = foundAuction.get();
            auctionNameLabel.setText(loadedAuction.getName());
            auctionDescriptionLabel.setText(loadedAuction.getDescription());
            auctionStartingBidLabel.setText(String.valueOf(loadedAuction.getStartingBid()));
        } else {
            System.out.println("Auction not found in the loaded data.");
        }
    }

    public void setSelectedAuction(Auction auction) {
        selectedAuction = auction;
    }

    public Auction getSelectedAuction() {
        return selectedAuction;
    }

    @FXML
    private void handleAuctionClick(MouseEvent event) {
        if (selectedAuction == null) {
            System.out.println("No auction selected.");
            return;
        }

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("auction_page.fxml"));
            Parent root = loader.load();

            AuctionPageController auctionPageController = loader.getController();
            auctionPageController.setMain(mainApplication);
            auctionPageController.setAuctionController(auctionController);
            auctionPageController.setAuctionCardController(this);
            auctionPageController.setAuction(selectedAuction);
            auctionPageController.setLoggedInUser(user);
            System.out.println(selectedAuction.getName());

            Scene auctionPageScene = new Scene(root);
            currentStage.setScene(auctionPageScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
