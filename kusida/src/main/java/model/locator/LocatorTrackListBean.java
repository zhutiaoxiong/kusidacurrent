package model.locator;

public class LocatorTrackListBean {

    public String name;
    public String startAderess;
    public String endAderess;
    public long startTime;
    public long endTime;
    public boolean isColect;
    public boolean isSelect;
    public boolean isShow;
    public LocatorTrackListBean(String name, String startAderess, String endAderess, long startTime, long endTime, boolean isColect) {
        this.name = name;
        this.startAderess = startAderess;
        this.endAderess = endAderess;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isColect = isColect;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
