package common.map;

import com.baidu.mapapi.model.LatLng;
/**
 * Created by Administrator on 2017/3/11.
 */

public class DataCar_Area_Pos {
    public LatLng carPos;                // pos
    public String address = "";
    public int    direction;//汽车方向	 0～359，正北为0，顺时针

    public LatLng areaPos;    // 围栏坐标
    public int areaMeter = 0;    // 围栏半径,单位米
    public int areaOpen  = 0;    // 围栏打开
}
