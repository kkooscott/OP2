package npa.op.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.aspose.cells.ICellsDataTable;

public class MapData implements ICellsDataTable {
    //數據集合  
    @SuppressWarnings("rawtypes")
    private List<Map> dataList = null;  
 
    //索引  
    private int index;  
       
    //存放dataList當中Map<String, Object>的key  
    private String[] columns = null;  
   
    @SuppressWarnings("rawtypes")
    public MapData(Map data) {  
        if(this.dataList == null) {  
            this.dataList = new ArrayList<Map>();  
        }  
        dataList.add(data);
    }  
       
    @SuppressWarnings("rawtypes")
    public MapData(List<Map> data) {  
        this.dataList = data;  
    }  
       
    /** 
     * 初始化方法 
     */ 
    public void beforeFirst() {  
        index = -1;  
        columns = this.getColumns();  
    }  
   
    /** 
     * WorkbookDesigner自動調用 
     * 會將this.getColumns()方法所返回的列 按照順序調用改方法 
     */ 
    @SuppressWarnings("rawtypes")
    public Object get(int columnIndex) {  
        if(index < 0 || index >= this.getCount()) {  
            return null;  
        }  
        Map record = this.dataList.get(index);  
        String columnName = this.columns[columnIndex];  
        return record.get(columnName);  
    }  
   
    /** 
     * 根據columnName返回數據 
     */ 
    @SuppressWarnings("rawtypes")
    public Object get(String columnName) {  
        Map record = this.dataList.get(index);  
        return record.get(columnName);  
    }  
   
    /** 
     * 獲得列集合 
     */ 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String[] getColumns() {  
        Map temp = this.dataList.get(0);  
        Set<Entry> entrys = temp.entrySet();  
        List<String> columns = new ArrayList<String>();  
        for (Entry e : entrys) {  
            columns.add((String)e.getKey());  
        }  
        String[] s = new String[entrys.size()];  
        columns.toArray(s);  
        return s;  
    }  
   
    public int getCount() {  
        return this.dataList.size();  
    }  
   
    public boolean next() {  
        index += 1;  
        if(index >= this.getCount())  
        {  
            return false;  
        }  
        return true;  
    }  
   
}
