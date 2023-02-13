package model.locator;

public class DistributionRecordBean {
    public String workMode;
    public int sendStatus;
    public int replyStatus;
    public long sendTimeOne;
    public long sendTimeTwo;

    public DistributionRecordBean(String workMode, int sendStatus, int replyStatus, long sendTimeOne, long sendTimeTwo) {
        this.workMode = workMode;
        this.sendStatus = sendStatus;
        this.replyStatus = replyStatus;
        this.sendTimeOne = sendTimeOne;
        this.sendTimeTwo = sendTimeTwo;
    }
}
