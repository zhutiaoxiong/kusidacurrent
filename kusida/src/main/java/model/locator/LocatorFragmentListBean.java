package model.locator;

public class LocatorFragmentListBean {
    public int  isOnLine;
    public String equipmentNumber;
    public String msiNumber;
    public int recordCount;
    public String note;
    public boolean isSelect;
    public String username;
    public String time;

    public LocatorFragmentListBean(String time,String username,int isOnLine, String equipmentNumber, String msiNumber, int recordCount, String note) {
        this.isOnLine = isOnLine;
        this.equipmentNumber = equipmentNumber;
        this.msiNumber = msiNumber;
        this.recordCount = recordCount;
        this.note = note;
        this.username = username;
        this.time = time;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
