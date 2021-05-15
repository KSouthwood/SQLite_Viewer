package viewer;

import javax.swing.*;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");

        // JTextField
        JTextField jTextField = new JTextField();
        jTextField.setBounds(5, 5, 500, 20);
        jTextField.setName("FileNameTextField");
        add(jTextField);

        // JButton
        JButton jButton = new JButton();
        jButton.setBounds(550, 5, 100, 20);
        jButton.setName("OpenFileButton");
        jButton.setText("Open");
        add(jButton);

        setVisible(true);
    }

}
