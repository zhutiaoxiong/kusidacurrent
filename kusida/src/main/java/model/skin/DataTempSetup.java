package model.skin;

import android.net.Uri;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ZipUtil;
import com.kulala.staticsfunc.static_view_change.OPos;
import com.kulala.staticsfunc.static_view_change.OSize;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DataTempSetup {
    public List<String> txt_actions;//控制,应用,服务,我的
    public String color_action_on;//#ff0000
    public String color_action_off;//#00ff00
    public OSize control_center_size;//80*80
    public OPos control_center_location;//control_center_location:1,17
    public OSize control_second_button_size;//80*80
    public int control_second_button_location=0;//control_second_button_location:97
    public int control_pop_confirm = 1;//control_pop_confirm:1,#ffffff,#ffcc0000
    public String colorPopNomal = "#ffffff";
    public String colorPopPress = "#cc0000";


    public void saveSkin(String name, String value) {
        if (name == null || value == null || name.length() == 0 || value.length() == 0) return;
        String[] arrCh = value.split(",");
        if (name.equals("txt_actions")) {
            txt_actions = Arrays.asList(arrCh);
        } else if (name.equals("color_action_on")) {
            color_action_on = value;
        } else if (name.equals("color_action_off")) {
            color_action_off = value;
        } else if (name.equals("control_center_size")) {
            control_center_size = new OSize(Integer.parseInt(arrCh[0]), Integer.parseInt(arrCh[1]));
        } else if (name.equals("control_center_location")) {
            control_center_location = new OPos(Integer.parseInt(arrCh[0]), Integer.parseInt(arrCh[1]));
        } else if (name.equals("control_second_button_size")) {
            control_second_button_size = new OSize(Integer.parseInt(arrCh[0]), Integer.parseInt(arrCh[1]));
        } else if (name.equals("control_second_button_location")) {
            control_second_button_location = Integer.parseInt(arrCh[0]);
        } else if (name.equals("control_pop_confirm")) {
            control_pop_confirm = Integer.parseInt(arrCh[0]);
            colorPopNomal = arrCh[1];
            colorPopPress = arrCh[2];
        }
    }
}
