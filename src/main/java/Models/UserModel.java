package Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private int id;
    private String name;
    private int age;
    private String email;
    private Timestamp date;


    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getAge() {return age;}

    public void setAge(int age) {this.age = age;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public Timestamp getDate() {return date;}

    public void setDate(Timestamp date) {this.date = date;}


    public boolean saveUser() throws SQLException {
        try (Connection connection = DataBase.connection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO user (Name, Age, Email, Creation_date) VALUES (?,?,?,?)")) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);
            statement.setTimestamp(4, date);

            int result = statement.executeUpdate();
            return result > 0;
        }
    }

    public List<UserModel> getUsers() throws SQLException {
        List<UserModel> users = new ArrayList<>();
        try (Connection connection = DataBase.connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM user")) {

            while (resultSet.next()) {
                UserModel user = extractUserFromResultSet(resultSet);
                users.add(user);
            }
        }
        return users;
    }

    public UserModel getUser(int id) throws SQLException {
        try (Connection connection = DataBase.connection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public boolean updateUser(String name, int age, String email, int id) throws SQLException {
        if (verificarUser(name) && !getUser(id).getName().equals(name)) {
            return false;
        }

        try (Connection connection = DataBase.connection();
             PreparedStatement statement = connection.prepareStatement("UPDATE user SET Name = ?, Age = ?, Email = ? WHERE id = ?")) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);
            statement.setInt(4, id);

            int result = statement.executeUpdate();
            return result > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection connection = DataBase.connection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {

            statement.setInt(1, id);

            int result = statement.executeUpdate();
            return result > 0;
        }
    }

    private UserModel extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        UserModel user = new UserModel();
        user.setId(resultSet.getInt("Id"));
        user.setName(resultSet.getString("Name"));
        user.setAge(resultSet.getInt("Age"));
        user.setEmail(resultSet.getString("Email"));
        user.setDate(resultSet.getTimestamp("Creation_date"));
        return user;
    }

    public boolean verificarUser(String name) throws SQLException {
        try (Connection connection = DataBase.connection();
             PreparedStatement statement = connection.prepareStatement("SELECT Name FROM user WHERE Name = ?")) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
