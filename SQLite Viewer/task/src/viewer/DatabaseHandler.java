package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private final SQLiteDataSource dataSource;

    public DatabaseHandler(String dbPath) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath);
    }

    public List<String> getTablesNames(String query) {
        List<String> tablesList = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                    tablesList.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablesList;
    }

    public List<String> getColumns(String query) {
        List<String> columns = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();

                columns = getColumnsNames(rsmd);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    private List<String> getColumnsNames(ResultSetMetaData rsmd) throws SQLException {
        List<String> columns = new ArrayList<>();
        int length = rsmd.getColumnCount();
        for (int i = 1; i <= length; i++) {
            columns.add(rsmd.getColumnName(i));
        }
        return columns;
    }

    public List<List<Object>> getData(String query, List<String> columnNames) {
        List<List<Object>> data = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery(query);
                data = getData(rs, columnNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private List<List<Object>> getData(ResultSet rs, List<String> columnNames) throws SQLException {
        List<List<Object>> data = new ArrayList<>();
        List<Object> list;
        while (rs.next()) {
            list = new ArrayList<>();
            data.add(list);
            for (int i = 1; i <= columnNames.size(); i++) {
                list.add(rs.getString(i));
            }
        }
        return data;
    }
}
