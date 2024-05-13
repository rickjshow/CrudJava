package Controllers;

import Models.UserModel;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UserController {

    private final UserModel userModel;

    public UserController() {
        this.userModel = new UserModel();
    }

    public boolean saveUser(String name, int age, String email) throws SQLException {
        if (!userModel.verificarUser(name)) {
            UserModel newUser = new UserModel();
            newUser.setName(name);
            newUser.setAge(age);
            newUser.setEmail(email);
            newUser.setDate(new Timestamp(System.currentTimeMillis()));
            return newUser.saveUser();
        }
        return false;
    }

    public List<UserModel> getUsers() throws SQLException {
        return userModel.getUsers();
    }

    public UserModel getUser(int id) throws SQLException {
        return userModel.getUser(id);
    }

    public boolean updateUser(String name, int age, String email, int id) throws SQLException {
        UserModel existingUser = userModel.getUser(id);
        if (existingUser != null) {
            existingUser.setName(name);
            existingUser.setAge(age);
            existingUser.setEmail(email);
            return userModel.updateUser(name, age, email, id);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
    }

    public void deleteUser(int id) throws SQLException {
        userModel.delete(id);
    }
}

