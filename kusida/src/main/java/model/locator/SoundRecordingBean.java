package model.locator;

public class SoundRecordingBean {
    public int type;
    public long time;
    public boolean isShow;
    public boolean isSelect;
    public float percent;
    public boolean isPlay;

    public SoundRecordingBean(float percent, int type, long time) {
        this.percent = percent;
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
