package viewer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SQLiteViewer extends JFrame {

    private transient DatabaseHandler dbHandler;

    private JTextField fileNameTextField;
    private JButton openButton;
    private JComboBox<String> tables;
    private JTextArea queryTextArea;
    private JButton executeButton;

    private JTable jTable;
    private MyTable model;

    public SQLiteViewer() {
        super("SQLite Viewer");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        setResizable(false);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        initFileNameTextField();
        initOpenButton();
        initTables();
        initQueryTextArea();
        initExecuteButton();
        initJTable();
    }

    private void initFileNameTextField() {
        this.fileNameTextField = new JTextField();
        this.fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setPreferredSize(new Dimension(540, 30));
        add(this.fileNameTextField);
    }

    private void initOpenButton() {
        this.openButton = new JButton("Open");
        this.openButton.setName("OpenFileButton");
        add(this.openButton);

        this.openButton.addActionListener(e -> {
            String dbPath = fileNameTextField.getText();
            dbHandler = new DatabaseHandler(dbPath);

            String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%';";
            List<String> tablesNames = dbHandler.getTablesNames(query);

            if (tablesNames.isEmpty()) {
                JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
                executeButton.setEnabled(false);
                queryTextArea.setEnabled(false);
            } else {
                this.tables.removeAllItems();
                for (String tablesName : tablesNames) {
                    this.tables.addItem(tablesName);
                }
                executeButton.setEnabled(true);
                queryTextArea.setEnabled(true);
            }
        });
    }

    private void initTables() {
        this.tables = new JComboBox<>();
        tables.setPreferredSize(new Dimension(640, 20));
        this.tables.setName("TablesComboBox");
        add(this.tables);

        this.tables.addActionListener(e ->
                queryTextArea.setText("SELECT * FROM " + this.tables.getSelectedItem() + ";"));
    }

    private void initQueryTextArea() {
        this.queryTextArea = new JTextArea();
        this.queryTextArea.setName("QueryTextArea");
        queryTextArea.setPreferredSize(new Dimension(540, 120));
        add(this.queryTextArea);
        queryTextArea.setEnabled(false);
    }

    private void initExecuteButton() {
        this.executeButton = new JButton("Execute");
        this.executeButton.setName("ExecuteQueryButton");
        add(this.executeButton);
        this.executeButton.addActionListener(e -> getAndDisplayQueryResult());
        executeButton.setEnabled(false);
    }

    private void initJTable() {
        model = new MyTable();
        model.addTableModelListener(e -> {
            //
        });

        jTable = new JTable(model);
        jTable.setName("Table");

        add(new JScrollPane(jTable));
    }

    private void getAndDisplayQueryResult() {
        String query = queryTextArea.getText();

        List<String> columns = dbHandler.getColumns(query);

        if (columns.isEmpty()) {
            JOptionPane.showMessageDialog(new Frame(), "Wrong query");
        }

        List<List<Object>> data = dbHandler.getData(query, columns);

        model.setColumns(columns);
        model.setData(data);
    }
}