import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Auction {
    private int id;
    private String name;
    private String description;
    private double startingBid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User seller;
    private User highestBidder;
    private double highestBid;
    private ArrayList<Bid> bids;
    private boolean ended;

    AuctionManager auctionManager ;


    public Auction(int id, String name, String description, double startingBid, LocalDateTime startTime, LocalDateTime endTime, User seller, ArrayList<Bid> bids, boolean ended) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startingBid = startingBid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seller = seller;
        this.highestBid = startingBid;
        this.bids = bids;
        this.ended = false;
    }

    public int getId() {
        return id;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getStartingBid() {
        return startingBid;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public User getSeller() {
        return seller;
    }

    public User getHighestBidder() {
        return highestBidder;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public static class InvalidBidException extends Exception {
        public InvalidBidException(String message) {
            super(message);
        }
    }


    public void placeBid(User bidder, double amount) throws InvalidBidException {
        if (amount <= highestBid) {
            throw new InvalidBidException("Bid must be higher than the current highest bid.");
        }

        if (bidder.equals(seller)) {
            throw new InvalidBidException("Seller cannot bid on their own auction.");
        }

        if (isEnded()) {
            throw new InvalidBidException("Auction has already ended.");
        }

        Bid bid = new Bid(bidder, amount);

        bids.add(bid);

        highestBidder = bidder;
        highestBid = amount;
    }



    public boolean isExpired() {
        if (!ended && LocalDateTime.now().isAfter(endTime)) {
            ended = true;
        }
        return ended;
    }



    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    public String toString() {
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isActive = currentTime.isBefore(endTime);

        return "Auction: " + name + "\n" +
                "Description: " + description + "\n" +
                "Starting bid: " + startingBid + "\n" +
                "Current highest bid: " + highestBid + "\n" +
                "Seller: " + seller.getUsername() + "\n" +
                "Highest bidder: " + (highestBidder != null ? highestBidder.getUsername() : "none") + "\n" +
                "Start time: " + startTime + "\n" +
                "End time: " + endTime + "\n" +
                "Auction active or not: " + isActive + "\n";
    }


    public User getWinner() {
        if (bids.isEmpty()) {
            return null; // No winner if no bids
        }

        Bid highestBid = Collections.max(bids, Comparator.comparingDouble(Bid::getAmount));
        return highestBid.getBidder();
    }




}
