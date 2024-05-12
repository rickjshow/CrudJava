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
                    JOptionPane.showMessageDialog(null, "Por favor preencha todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                    if (age <= 0){
                        JOptionPane.showMessageDialog(null,"Coloque uma idade válida","Erro",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException exception){
                    JOptionPane.showMessageDialog(null,"A idade deve ser um numero inteiro", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    JOptionPane.showMessageDialog(null, "O email inserido não é válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    if (controller.saveUser(name, age, email) == true){
                        JOptionPane.showMessageDialog(null,"Usuário inserido com sucesso","Sucesso",JOptionPane.INFORMATION_MESSAGE);
                        loadUsersIntoTable();
                        clearFields();
                    }else if (controller.saveUser(name, age, email) == false){
                        JOptionPane.showMessageDialog(null,"Esse nome de usuário já existe!","Erro",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception){
                    JOptionPane.showMessageDialog(null,"Ocorreu um erro ao salvar o Usuário","Erro",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadUsersIntoTable();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = textField4.getText();

                if (idText.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Por favor preencha o campo","Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = 0;

                try {
                    id = Integer.parseInt(idText);

                    UserModel searchedUser = controller.getUser(id);

                    if(searchedUser == null) {
                        JOptionPane.showMessageDialog(null,"Nenhum usuário encontrado","Erro",JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);

                    model.addRow(new Object[]{searchedUser.getId(), searchedUser.getName(), searchedUser.getAge(), searchedUser.getEmail(), searchedUser.getDate()});

                    textField1.setText(searchedUser.getName());
                    textField2.setText(String.valueOf(searchedUser.getAge()));
                    textField3.setText(searchedUser.getEmail());
                } catch (SQLException exception){
                    JOptionPane.showMessageDialog(null,"Erro ao buscar usuário","Erro", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Por favor preencha todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                    if (age <= 0){
                        JOptionPane.showMessageDialog(null,"Coloque uma idade válida","Erro",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException exception){
                    JOptionPane.showMessageDialog(null,"A idade deve ser um numero inteiro", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = 0;
                try {
                    id = Integer.parseInt(Idtext);
                } catch (NumberFormatException exception){
                   JOptionPane.showMessageDialog(null, "O id deve ser um numero inteiro", "Erro", JOptionPane.ERROR_MESSAGE);
                   return;
                }

                if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    JOptionPane.showMessageDialog(null, "O email inserido não é válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try{
                    if(controller.updateUser(name, age, email, id) == true){
                        JOptionPane.showMessageDialog(null,"Usuário atualizado com sucesso!","Sucesso",JOptionPane.INFORMATION_MESSAGE);
                        loadUsersIntoTable();
                        clearFields();
                    }else{
                        JOptionPane.showMessageDialog(null,"Esse nome ja existe!","Erro",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception){
                    JOptionPane.showMessageDialog(null,"Nao foi possível atualizar o usuário","Erro",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = textField4.getText();

                if(idText.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Selecione um usuário","Erro",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = 0;
                try {
                    id = Integer.parseInt(idText);
                } catch (NumberFormatException exception){
                    JOptionPane.showMessageDialog(null, "O id deve ser um numero inteiro", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    controller.deleteUser(id);
                    JOptionPane.showMessageDialog(null,"Usuário excluido com sucesso!","Erro",JOptionPane.INFORMATION_MESSAGE);
                    loadUsersIntoTable();
                    clearFields();
                } catch (Exception exception){
                    JOptionPane.showMessageDialog(null,"Nao foi possível excluir o usuário","Erro",JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados da tabela: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }
}