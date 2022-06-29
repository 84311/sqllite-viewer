package viewer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MyTable extends AbstractTableModel {

    private List<String> columns = new ArrayList<>();
    private List<List<Object>> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columns.get(column);
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
        fireTableStructureChanged();
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
        fireTableDataChanged();
    }
}
