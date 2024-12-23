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

public class UserPageCardController {

    private User user;

    @FXML
    private Label auctionNameLabel;

    @FXML
    private Label auctionDescriptionLabel;

    @FXML
    private Label auctionStartingBidLabel;

    public void setUser(User user) {
        this.user = user;
    }


    public void setAuctionData(Auction auction) {
        AuctionManager auctionManager = new AuctionManager();
        ArrayList<Auction> auctions = auctionManager.getAuctionsByUser(user);

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



}
