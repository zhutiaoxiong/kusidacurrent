package model.locator;

public class MessageSelectBean {
    public String name;
    public boolean isSelect;
    public boolean isShowLine=true;

    public MessageSelectBean(String name) {
        this.name = name;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
