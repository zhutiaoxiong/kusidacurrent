package model.locator;

public class MessageBean {
    public String aderess;
    public int type;
    public long time;
    public boolean isShow;
    public boolean isSelect;

    public MessageBean(String aderess, int type,long time) {
        this.aderess = aderess;
        this.type = type;
        this.time = time;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
