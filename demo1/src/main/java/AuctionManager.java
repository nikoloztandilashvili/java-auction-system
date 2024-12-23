import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AuctionManager {
    Auction auction;
     List<Auction> auctions;
    DatabaseHandler databaseHandler;
    UserDao userDao;

    public AuctionManager() {
        auctions = new ArrayList<>();
        databaseHandler = new DatabaseHandler();
        userDao = new UserDao(databaseHandler);
    }

    public Auction createAuction(User seller, String name, String description, double startingBid, LocalDateTime startTime, LocalDateTime endTime, Boolean ended) {
        Auction auction = new Auction(auctions.size() + 1, name, description, startingBid, startTime, endTime, seller, new ArrayList<>(), ended);
        auctions.add(auction);
        saveAuctionToDatabase(auction);
        System.out.println(auction);
        return auction;
    }


     void saveAuctionToDatabase(Auction auction) {
        try {
            Connection connection = databaseHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auctions (name, description, starting_bid, start_time, end_time, seller_id, ended) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, auction.getName());
            statement.setString(2, auction.getDescription());
            statement.setDouble(3, auction.getStartingBid());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(auction.getStartTime()));
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(auction.getEndTime()));
            statement.setInt(6, auction.getSeller().getId());
            statement.setBoolean(7, auction.isEnded());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public ArrayList<Auction> loadAuctionsFromDatabase() {
        ArrayList<Auction> loadedAuctions = new ArrayList<>();
        try {
            Connection connection = databaseHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM test_jdbc.auctions;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int auctionId = resultSet.getInt(1);
                LocalDateTime startTime = resultSet.getTimestamp(5).toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp(6).toLocalDateTime();

                String name = resultSet.getString(2);
                String description = resultSet.getString(3);
                double startingBid = resultSet.getDouble(4);
                int sellerId = resultSet.getInt(7);
                boolean ended = resultSet.getBoolean(8);

                UserDao userDao = new UserDao(databaseHandler);
                User seller = userDao.getUserById(sellerId);

                Auction auction = new Auction(auctionId, name, description, startingBid, startTime, endTime, seller, new ArrayList<>(), ended);
                loadedAuctions.add(auction);


            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return loadedAuctions;
    }



    private boolean isAuctionsTableEmpty() throws SQLException {
        boolean isEmpty = true;

        // Query the "auctions" table to check if it is empty
        String query = "SELECT COUNT(*) FROM auctions";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_jdbc",  "root", "Password123");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int rowCount = resultSet.getInt(1);
                isEmpty = (rowCount == 0);
            }
        }

        return isEmpty;
    }


    public void updateAuctionInDatabase(Auction auction, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            if (isAuctionsTableEmpty()) {

                System.out.println("The auctions table is empty. Cannot update the auction.");
                return;
            }

            if (auction == null) {
                throw new IllegalArgumentException("Auction cannot be null");
            }

            // Check if startTime or endTime is null
            if (startTime == null || endTime == null) {
                throw new IllegalArgumentException("Start time and end time must be specified");
            }

            Connection connection = databaseHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE auctions SET start_time = ?, end_time = ? WHERE id = ?");
            statement.setTimestamp(1, java.sql.Timestamp.valueOf(startTime));
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(endTime));
            statement.setInt(3, auction.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public ArrayList<Auction> getActiveAuctions() {
        ArrayList<Auction> activeAuctions = new ArrayList<>();
        for (Auction auction : loadAuctionsFromDatabase()) {
            if (auction.isActive()) {
                activeAuctions.add(auction);
            }
        }
        return activeAuctions;
    }

    public ArrayList<Auction> searchAuctions(String searchCriteria) {
        ArrayList<Auction> matchingAuctions = new ArrayList<>();
        ArrayList<Auction> allAuctions = getActiveAuctions();

        for (Auction auction : allAuctions) {
            if (auction.getName().contains(searchCriteria) || auction.getSeller().getUsername().contains(searchCriteria)) {
                matchingAuctions.add(auction);
            }
        }

        return matchingAuctions;
    }

    public ArrayList<Auction> getAuctionsByUser(User user) {
        ArrayList<Auction> auctionsByUser = new ArrayList<>();

        for (Auction auction : auctions) {
            if (auction.getSeller().equals(user)) {
                auctionsByUser.add(auction);
            }
        }

        return auctionsByUser;
    }


    public void displayAuctionDetails(Auction auction) {
        System.out.println(auction.toString());
    }




}
