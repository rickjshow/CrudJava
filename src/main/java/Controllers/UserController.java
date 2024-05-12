package Controllers;

import Models.UserModel;
import Views.UserView;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UserController {

    public boolean saveUser(String name, int age, String email) throws SQLException {
        UserModel userModel = new UserModel();

        userModel.setName(name);
        userModel.setAge(age);
        userModel.setEmail(email);
        userModel.setDate(new Timestamp(System.currentTimeMillis()));

        if(!userModel.verificarUser(name)) {
           return userModel.saveUser();
        }else{
            return false;
        }
    }

    public List<UserModel> getUsers() throws SQLException {
        UserModel userModel = new UserModel();
        return userModel.getUsers();
    }

    public UserModel getUser(int id) throws SQLException {
        UserModel userModel = new UserModel();
        return userModel.getUser(id);
    }

    public boolean updateUser(String name, int age, String email, int id) throws SQLException {

        try{
            UserModel userModel = new UserModel();

            userModel.setName(name);
            userModel.setAge(age);
            userModel.setEmail(email);
            userModel.setId(id);

            return userModel.updateUser(name, age, email, id);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void deleteUser(int id) throws SQLException{
        UserModel userModel = new UserModel();

        userModel.setId(id);

        userModel.delete(id);
    }
}
