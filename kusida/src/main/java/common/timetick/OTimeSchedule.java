package common.timetick;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时器，多线程
 * @author Administrator
 *
 */
public class OTimeSchedule {
	private ScheduledExecutorService scheduledExecutorService;
	// ========================out======================
	private static OTimeSchedule	_instance;
	protected OTimeSchedule() {
		init();
	}
	public static OTimeSchedule getInstance() {
		if (_instance == null)
			_instance = new OTimeSchedule();
		return _instance;
	}
	// ========================out======================

	public void init() {
		if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
			scheduledExecutorService.shutdown();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new TimeTickTask(), 1, 1, TimeUnit.SECONDS);
	}
	private class TimeTickTask implements Runnable {
		public void run() {
			ODispatcher.dispatchEvent(OEventName.TIME_TICK_SECOND);
		}
	}
	private class TimeTickTaskMill implements Runnable {
		public void run() {
			if(runother == null){
				return;
			}else{
				runother.run();
			}
		}
	}
	private Runnable runother;
	public void timeRunableMillisecond(Runnable run,int millisecond){
		runother = run;
		if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
			scheduledExecutorService.shutdown();
		scheduledExecutorService = Executors.newScheduledThreadPool(2);
		scheduledExecutorService.scheduleAtFixedRate(new TimeTickTask(), 1, 1, TimeUnit.SECONDS);   
        scheduledExecutorService.scheduleAtFixedRate(new TimeTickTaskMill(), millisecond, millisecond, TimeUnit.MILLISECONDS);
	}
	public void timeStop(){
		runother = null;
		if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
			scheduledExecutorService.shutdown();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new TimeTickTask(), 1, 1, TimeUnit.SECONDS);
	}
}
