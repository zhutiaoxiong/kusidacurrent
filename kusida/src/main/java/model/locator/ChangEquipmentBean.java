package model.locator;

public class ChangEquipmentBean {
    public int  isOnLine;
    public String username;
    public String time;
    public int type;

    public ChangEquipmentBean( String username, String time, int isOnLine,int type) {
        this.isOnLine = isOnLine;
        this.username = username;
        this.time = time;
        this.type = type;
    }
}
