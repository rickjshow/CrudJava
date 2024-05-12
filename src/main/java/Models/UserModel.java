package Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Controllers.UserController;

import javax.xml.transform.Result;

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
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DataBase.connection();

            String sql = "INSERT INTO user (Name, Age, Email, Creation_date) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);
            statement.setTimestamp(4, date);

            int result = statement.executeUpdate();

            if (result > 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    public List<UserModel> getUsers() throws SQLException {
        List<UserModel> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DataBase.connection();

            statement = connection.prepareStatement("SELECT * FROM user");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UserModel user = new UserModel();
                user.setId(resultSet.getInt("Id"));
                user.setName(resultSet.getString("Name"));
                user.setAge(resultSet.getInt("Age"));
                user.setEmail(resultSet.getString("Email"));
                user.setDate(resultSet.getTimestamp("Creation_date"));

                users.add(user);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public UserModel getUser(int id) throws SQLException {
        UserModel user = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DataBase.connection();

            statement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new UserModel();
                user.setId(resultSet.getInt("Id"));
                user.setName(resultSet.getString("Name"));
                user.setAge(resultSet.getInt("Age"));
                user.setEmail(resultSet.getString("Email"));
                user.setDate(resultSet.getTimestamp("Creation_date"));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updateUser(String name, int age, String email, int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            if(verificarUser(name) && !getUser(id).getName().equals(name)){
                return false;
            }

            connection = DataBase.connection();
            String sql = "UPDATE user SET Name = ?, Age = ?, Email = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);
            statement.setInt(4, id);

           int result = statement.executeUpdate();

           if(result == 0){
               return false;
           }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            // Fechar recursos
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return true;
    }

    public boolean verificarUser (String name) throws  SQLException{
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DataBase.connection();
            String sql = "SELECT Name FROM user WHERE Name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    public boolean delete(int id) throws SQLException{
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DataBase.connection();
            String sql = "DELETE FROM user WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);

            int result = statement.executeUpdate();

            if (result > 0){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }
}
