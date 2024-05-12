import Controllers.UserController;
import Views.UserView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UserController controller = new UserController();
        UserView userView = new UserView(controller);

        JFrame frame = new JFrame("User Management");
        frame.setContentPane(userView.Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

