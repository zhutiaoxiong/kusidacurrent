package view.find;

import java.util.Objects;

public class ProSetData {
    public int windowRiseInterval;//升窗间隔时间
    public int windowRiseTime;//升窗时间
    public int hornVolume;//喇叭音量
    public int trunkOpenWith;//尾箱开启方式
    public int electrifyBeforehandTime;//提前通电时间
    public int switchesOffDelayTime;//延迟断电时间
    public int pressKayTime;//按键短按时间
    public int avoidanceDeviceTechnique=-1;//回避器控制方
    public int avoidanceDeviceOperation;//回避器控制
    public int unlockWay;//开锁方法
    public int lockWay;//关锁方法
    public int carLockWindowRise;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProSetData that = (ProSetData) o;
        return windowRiseInterval == that.windowRiseInterval &&
                windowRiseTime == that.windowRiseTime &&
                hornVolume == that.hornVolume &&
                trunkOpenWith == that.trunkOpenWith &&
                electrifyBeforehandTime == that.electrifyBeforehandTime &&
                switchesOffDelayTime == that.switchesOffDelayTime &&
                pressKayTime == that.pressKayTime &&
                avoidanceDeviceTechnique == that.avoidanceDeviceTechnique &&
                avoidanceDeviceOperation == that.avoidanceDeviceOperation &&
                unlockWay == that.unlockWay &&
                lockWay == that.lockWay &&
                carLockWindowRise == that.carLockWindowRise;
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowRiseInterval, windowRiseTime, hornVolume, trunkOpenWith, electrifyBeforehandTime, switchesOffDelayTime, pressKayTime, avoidanceDeviceTechnique, avoidanceDeviceOperation, unlockWay, lockWay, carLockWindowRise);
    }
}
