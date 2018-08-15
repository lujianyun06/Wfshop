package cn.bupt.wfshop.event;

public class WFEvent {
     private EventDataBundle bundle = new EventDataBundle();
     private WFEventTag mEventTag = null;

     public WFEvent(WFEventTag eventTag){
         mEventTag = eventTag;
     }

     public WFEventTag getEventTag(){
         return mEventTag;
     }

     public WFEvent addData(String key, Object item){
         bundle.addDataItem(key, item);
         return this;
     }

     public Object getData(String key){
         return bundle.getDataItem(key);
     }
}
