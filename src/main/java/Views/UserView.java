package Views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import Controllers.UserController;
import Models.UserModel;

public class UserView {
    public JPanel Main;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField textField4;
    private JScrollPane table_1;
    private UserController controller;

    public UserView(UserController controller) {
        this.controller = controller;

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField1.getText();
                String ageText = textField2.getText();
                String email = textField3.getText();

                if (name.isEmpty() || ageText.isEmpty() || email.isEmpty()){
                    showError("Por favor preencha todos os campos");
                    return;
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                    if (age <= 0){
                        showError("Coloque uma idade válida");
                        return;
                    }
                } catch (NumberFormatException exception){
                    showError("A idade deve ser um numero inteiro");
                    return;
                }

                if (!verifyEmail(email)) {
                    showError("O email inserido não é válido.");
                    return;
                }

                try {
                    if (controller.saveUser(name, age, email) == true){
                        showSucess("Usuário inserido com sucesso");
                        loadUsersIntoTable();
                        clearFields();
                    }else{
                        showError("Esse nome de usuário já existe!");
                    }
                } catch (Exception exception){
                    showError("Ocorreu um erro ao salvar o Usuário");
                }
            }
        });

        loadUsersIntoTable();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = textField4.getText();

                if (idText.isEmpty()){
                    showError("Por favor preencha o campo");
                    return;
                }

                int id = 0;

                try {
                    id = Integer.parseInt(idText);

                    UserModel searchedUser = controller.getUser(id);

                    if(searchedUser == null) {
                        showError("Nenhum usuário encontrado");
                        return;
                    }

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);

                    model.addRow(new Object[]{searchedUser.getId(), searchedUser.getName(), searchedUser.getAge(), searchedUser.getEmail(), searchedUser.getDate()});

                    textField1.setText(searchedUser.getName());
                    textField2.setText(String.valueOf(searchedUser.getAge()));
                    textField3.setText(searchedUser.getEmail());
                } catch (SQLException exception){
                    showError("Erro ao buscar usuário");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField1.getText();
                String ageText = textField2.getText();
                String email = textField3.getText();
                String Idtext = textField4.getText();

                if (name.isEmpty() || ageText.isEmpty() || email.isEmpty() || Idtext.isEmpty()){
                    showError("Por favor preencha todos os campos");
                    return;
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                    if (age <= 0){
                        showError("Coloque uma idade válida");
                        return;
                    }
                } catch (NumberFormatException exception){
                    showError("A idade deve ser um numero inteiro");
                    return;
                }

                int id = 0;
                try {
                    id = Integer.parseInt(Idtext);
                } catch (NumberFormatException exception){
                   showError("O id deve ser um numero inteiro");
                   return;
                }

                if (!verifyEmail(email)) {
                    showError("O email inserido é invalido!");
                    return;
                }

                try{
                    if(controller.updateUser(name, age, email, id) == true){
                        showSucess("Usuario atualizado com sucesso!");
                        loadUsersIntoTable();
                        clearFields();
                    }else{
                        showError("Esse nome de usuário já existe!");
                    }
                } catch (Exception exception){
                    showError("Nao foi possível atualizar o usuário!");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = textField4.getText();

                if(idText.isEmpty()){
                    showError("Selecione um usuário");
                    return;
                }

                int id = 0;
                try {
                    id = Integer.parseInt(idText);
                } catch (NumberFormatException exception){
                    showError("O id deve ser um numero inteiro");
                    return;
                }
                try{
                    controller.deleteUser(id);
                    showSucess("Usuário excluido com sucesso!");
                    loadUsersIntoTable();
                    clearFields();
                } catch (Exception exception){
                    showError("Nao foi possível excluir o usuário");
                }
            }
        });
    }

    private void loadUsersIntoTable() {
        try {

            List<UserModel> users = controller.getUsers();

            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("Id");
            model.addColumn("Name");
            model.addColumn("Age");
            model.addColumn("Email");
            model.addColumn("Creation Date");

            for (UserModel user : users) {
                model.addRow(new Object[]{user.getId(), user.getName(), user.getAge(), user.getEmail(), user.getDate()});
            }

            table1.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao carregar dados da tabela: " + e.getMessage());
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    private void showError(String message){
        JOptionPane.showMessageDialog(null,message,"Erro",JOptionPane.ERROR_MESSAGE);
    }

    private void showSucess(String message){
        JOptionPane.showMessageDialog(null,message,"Sucesso",JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean verifyEmail(String email){
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }
}