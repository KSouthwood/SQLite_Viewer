package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class SQLiteViewer extends JFrame {
    JTextField filename = new JTextField();
    JButton openButton = new JButton("Open");
    JComboBox<String> tableComboBox = new JComboBox<>();
    JTextArea queryText = new JTextArea();
    JButton executeButton = new JButton("Execute");

    Vector<Object> columnNames = new Vector<>();
    Vector<Vector<Object>> tableData = new Vector<>();
    DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
    JTable table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);

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
        // filename text field properties
        filename.setBounds(20, 10, 540, 20);
        filename.setName("FileNameTextField");
        add(filename);

        // open button properties
        openButton.setBounds(580, 10, 100, 20);
        openButton.setName("OpenFileButton");
        add(openButton);


        // combo box properties (holds list of tables)
        tableComboBox.setBounds(20, 50, 660, 20);
        tableComboBox.setName("TablesComboBox");
        add(tableComboBox);

        // text area for queries properties
        queryText.setBounds(20, 90, 660, 200);
        queryText.setName("QueryTextArea");
        add(queryText);

        // execute button properties
        executeButton.setBounds(580, 310, 100, 20);
        executeButton.setName("ExecuteQueryButton");
        add(executeButton);

        // table properties
        table.setName("Table");
        scrollPane.setBounds(20, 350, 660, 400);
        add(scrollPane);

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

        tableComboBox.addActionListener(e -> queryText.setText("SELECT * FROM " + tableComboBox.getSelectedItem() + ";"));

        executeButton.addActionListener(e -> {
            Connection conn;
            columnNames.clear();
            tableData.clear();
            try {
                String url = "jdbc:sqlite:" + filename.getText().trim();
                conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement();
                ResultSet resultSet =
                        statement.executeQuery(queryText.getText());
                ResultSetMetaData metaData = resultSet.getMetaData();

                // get number of columns and read the names
                int columnCount = metaData.getColumnCount();
                if (columnCount > 0) {
                    for (int col = 1; col <= columnCount; col++) {
                        columnNames.add(metaData.getColumnName(col));
                    }
                    model.setColumnIdentifiers(columnNames);
                }

                // read the data row by row
                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<>();
                    for (int col = 1; col <= columnCount; col++) {
                        rowData.add(resultSet.getString(col));
                    }
                    tableData.add(rowData);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}