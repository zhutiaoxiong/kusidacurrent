package model.store;

import com.google.gson.JsonObject;

public class ManagerStore {
    public String shopLicense;
    public String frontDoorPicture;
    public String carPicture;
    private static ManagerStore _instance;
    public ShopCarExamplesPid shopCarExamplesPid;
    public static ManagerStore getInstance() {
        if (_instance == null)
            _instance = new ManagerStore();
        return _instance;
    }

    public void saveImgUrl(String url, String type ){
        if(type.equals("1")){
            shopLicense=url;
        }else  if(type.equals("2")){
            frontDoorPicture=url;
        }else  if(type.equals("3")){
            carPicture=url;
        }
    }
    public void saveShopExamolePid(JsonObject shopexpid){
        shopCarExamplesPid=ShopCarExamplesPid.fromJsonObject(shopexpid);
    }
    public String getShopLicense() {
        return shopLicense;
    }

    public String getFrontDoorPicture() {
        return frontDoorPicture;
    }

    public String getCarPicture() {
        return carPicture;
    }
}
