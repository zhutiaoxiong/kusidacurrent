package annualreminder.model;

import com.google.gson.JsonArray;
import annualreminder.view.clip.PopChooseCarWarp;

import java.util.ArrayList;
import java.util.List;

/**
 * Annual页面显示，需要先设此list
 */

public class ManagerAnnual {
    private        List<PopChooseCarWarp.DataCarChoose> carActiveList;
    private        List<RecodeCar>                      recodeCarList;
    public static long currentCarId = -1;
    // ========================out======================
    private static ManagerAnnual                        _instance;

    private ManagerAnnual() {
    }
    //退出时
    public void exit(){
        currentCarId = -1;
        recodeCarList = null;
        carActiveList = null;
    }

    public static ManagerAnnual getInstance() {
        if (_instance == null)
            _instance = new ManagerAnnual();
        return _instance;
    }
    public List<PopChooseCarWarp.DataCarChoose> getCarActiveList() {
        return carActiveList;
    }
    public PopChooseCarWarp.DataCarChoose getCurrentCarChoose(){
        if(carActiveList == null)return null;
        for(PopChooseCarWarp.DataCarChoose carChoose : carActiveList){
            if(carChoose.carId == currentCarId)return carChoose;
        }
        return null;
    }
    public List<RecodeCar> getAnnualRecode() {
        return recodeCarList;
    }

    public RecodeCar getRecodeByCarId(long carId) {
        if(recodeCarList == null)return null;
        for(RecodeCar recodeCar : recodeCarList){
            if(recodeCar.carId == carId)return recodeCar;
        }
        return null;
    }
    public List<AnnualRecode> getCurrentRecodeList() {
        if(recodeCarList == null)return null;
        for(RecodeCar recodeCar : recodeCarList){
            if(recodeCar.carId == currentCarId)return recodeCar.recodeList;
        }
        return null;
    }
    // ==========================set=================================
    public void saveCarActiveList(List<PopChooseCarWarp.DataCarChoose> carActiveList) {
        ManagerAnnual.currentCarId = -1;//初始化未选中车
        this.carActiveList = carActiveList;
    }

    public void saveCarAnnualRecode(long carId, int remainingDay, JsonArray annualRecords) {
        if(recodeCarList == null) recodeCarList = new ArrayList<RecodeCar>();
        RecodeCar car = new RecodeCar();
        car.carId = carId;
        car.remainingDay = remainingDay;
        car.recodeList = AnnualRecode.fromJsonArray(annualRecords);
        //排序,不需要
//        MyComparator myComparator = new MyComparator();
//        Collections.sort(car.recodeCarList, myComparator);
        if(car.recodeList!=null) {
            for (RecodeCar recodeCar : recodeCarList) {
                if (recodeCar.carId == carId) {
                    recodeCar.remainingDay = remainingDay;
                    recodeCar.recodeList = car.recodeList;
                    return;
                }
            }
        }
        recodeCarList.add(car);//如果是没有此ID就新增
    }
    public void setCarIsAlert(long carId,int isAlert){
        if(recodeCarList == null) recodeCarList = new ArrayList<RecodeCar>();
        for(RecodeCar recodeCar : recodeCarList){
            if(recodeCar.carId == carId){
                recodeCar.isAlert = isAlert;
                return;
            }
        }
    }

//    private class MyComparator implements Comparator<AnnualRecode> {
//        public int compare(AnnualRecode o1, AnnualRecode o2) {
//            if (o1!=null&&o2!=null){
//                if (o1.inspectionTime >o2.inspectionTime) {//大于放前
//                    return -1;
//                } else {
//                    return 1;
//                }
//            }
//            return Long.valueOf(o1.inspectionTime).compareTo(o2.inspectionTime);
//        }
//    }
    public class RecodeCar {
        public long               carId;
        public int                remainingDay;
        public int                isAlert = 0;//是否已设提醒,0:未设置提醒 1:已设置
        public List<AnnualRecode> recodeList;
    }
}
