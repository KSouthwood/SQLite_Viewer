package viewer;

import javax.swing.*;
import java.sql.*;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");

        init();

        setVisible(true);
    }

    void init() {
        // filename text field
        JTextField filename = new JTextField();
        filename.setBounds(20, 10, 540, 20);
        filename.setName("FileNameTextField");
        add(filename);

        JButton openButton = new JButton("Open");
        openButton.setBounds(580, 10, 100, 20);
        openButton.setName("OpenFileButton");
        add(openButton);


        // list of tables
        JComboBox<String> tableComboBox = new JComboBox<>();
        tableComboBox.setBounds(20, 50, 660, 20);
        tableComboBox.setName("TablesComboBox");
        add(tableComboBox);

        // text area for queries
        JTextArea queryText = new JTextArea();
        queryText.setBounds(20, 90, 660, 400);
        queryText.setName("QueryTextArea");
        add(queryText);

        // execute button
        JButton executeButton = new JButton("Execute");
        executeButton.setBounds(20, 510, 100, 20);
        executeButton.setName("ExecuteQueryButton");
        add(executeButton);

        openButton.addActionListener(e -> {
            Connection conn;
            tableComboBox.removeAllItems();
            try {
                String url = "jdbc:sqlite:" + filename.getText().trim();
                conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement();
                ResultSet resultSet =
                        statement.executeQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';");
                while (resultSet.next()) {
                    tableComboBox.addItem(resultSet.getString("name"));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        tableComboBox.addActionListener(e -> {
            queryText.setText("SELECT * FROM " + tableComboBox.getSelectedItem() + ";");
        });
    }
}