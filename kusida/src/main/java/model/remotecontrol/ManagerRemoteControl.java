package model.remotecontrol;

import android.text.TextUtils;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class ManagerRemoteControl {
    private static ManagerRemoteControl _instance;
    private BeanRemoteControl beanRemoteControl;
    private List<BeanRemoteForRecycleView> list;
    public static ManagerRemoteControl getInstance() {
        if (_instance == null)
            _instance = new ManagerRemoteControl();
        return _instance;
    }
    public void saveBeanRemoteControl(JsonObject beanRemoteControlm){
        beanRemoteControl=BeanRemoteControl.fromJsonObject(beanRemoteControlm);
        List<BeanRemoteForRecycleView> myList=new ArrayList<>();
        if(beanRemoteControl!=null){
            if(TextUtils.isEmpty(beanRemoteControl.oneName)){
                myList.add(new BeanRemoteForRecycleView("遥控器1",beanRemoteControl.slotOne));
            }else{
                myList.add(new BeanRemoteForRecycleView(beanRemoteControl.oneName,beanRemoteControl.slotOne));
            }
            if(TextUtils.isEmpty(beanRemoteControl.towName)){
                myList.add(new BeanRemoteForRecycleView("遥控器2",beanRemoteControl.slotTwo));
            }else{
                myList.add(new BeanRemoteForRecycleView(beanRemoteControl.towName,beanRemoteControl.slotTwo));
            }
        }
        list=myList;
    }
    public String getIsOpen(){
        if(beanRemoteControl!=null){
            return beanRemoteControl.isAutomatic;
        }
        return "";
    }
    public List<BeanRemoteForRecycleView> getTemControlDataForView(){
        return list;
    }

    public BeanRemoteControl getBeanRemoteControl() {
        return beanRemoteControl;
    }
    public List<BeanRemoteForRecycleView> createinitRemoteControlList(){
        List<BeanRemoteForRecycleView> mysList=new ArrayList<>();
        mysList.add(new BeanRemoteForRecycleView("默认遥控器","0"));
        mysList.add(new BeanRemoteForRecycleView("默认遥控器","0"));
        return mysList;
    }
}
