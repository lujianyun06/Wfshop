package cn.bupt.wfshop.event;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDataBundle {
    private HashMap<String, Object> dataList;

    public EventDataBundle(){
        dataList = new HashMap<>();
    }

    public void addDataItem(String key, Object item){
        if (dataList!=null){
            dataList.put(key, item);
        }
    }

    public int getDataSize(){
        if (dataList!=null){
            return dataList.size();
        }
        return 0;
    }

    public Object getDataItem(String key){
        return dataList.get(key);
    }

}
