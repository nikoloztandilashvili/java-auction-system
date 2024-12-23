import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final DatabaseHandler databaseHandler;

    public UserDao(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public User getUserById(int id) {
        String query = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement preparedStatement = databaseHandler.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String roleString = resultSet.getString("user_type");
                UserType role = UserType.valueOf(roleString);
                return new User(id , name, username, email, password, role);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean addUser(User user) {
        String query = "INSERT INTO users (name, email, username, password, user_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseHandler.getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().toString());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET name=?, username=?, email=?, password=?, user_type=? WHERE id=?";
        try (PreparedStatement preparedStatement = databaseHandler.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().toString());
            preparedStatement.setInt(6, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id=?";
        try (PreparedStatement preparedStatement = databaseHandler.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = databaseHandler.getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                UserType role = UserType.valueOf(resultSet.getString("user_type"));
                user = new User(id, name, email, username, password, role);
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return user;
    }

}
