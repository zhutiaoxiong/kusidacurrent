package common;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AppExecutors {
    private static final String TAG = "AppExecutors";
    /**
     * 磁盘IO线程池
     **/
    private final ExecutorService diskIO;
    /**
     * 网络IO线程池
     **/
    private final ExecutorService networkIO;
    /**
     * UI线程
     **/
    private final Executor mainThread;
    /**
     * 定时任务线程池
     **/
    private final ScheduledExecutorService scheduledExecutor;

    private volatile static AppExecutors appExecutors;

    public static AppExecutors getInstance() {
        if (appExecutors == null) {
            synchronized (AppExecutors.class) {
                if (appExecutors == null) {
                    appExecutors = new AppExecutors();
                }
            }
        }
        return appExecutors;
    }

    public AppExecutors(ExecutorService diskIO, ExecutorService networkIO, Executor mainThread, ScheduledExecutorService scheduledExecutor) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.scheduledExecutor = scheduledExecutor;
    }

    public AppExecutors() {
        this(diskIoExecutor(), networkExecutor(), new MainThreadExecutor(), scheduledThreadPoolExecutor());
    }

    /**
     * 定时(延时)任务线程池
     * <p>
     * 替代Timer,执行定时任务,延时任务
     */
    public ScheduledExecutorService scheduledExecutor() {
        return scheduledExecutor;
    }

    /**
     * 磁盘IO线程池（单线程）
     * <p>
     * 和磁盘操作有关的进行使用此线程(如读写数据库,读写文件)
     * 禁止延迟,避免等待
     * 此线程不用考虑同步问题
     */
    public ExecutorService diskIO() {
        return diskIO;
    }

    /**
     * 网络IO线程池
     * <p>
     * 网络请求,异步任务等适用此线程
     * 不建议在这个线程 sleep 或者 wait
     */
    public ExecutorService networkIO() {
        return networkIO;
    }

    /**
     * UI线程
     * <p>
     * Android 的MainThread
     * UI线程不能做的事情这个都不能做
     */
    public Executor mainThread() {
        return mainThread;
    }

    private static ScheduledExecutorService scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(16, r -> new Thread(r, "scheduled_executor"), (r, executor) -> Log.e(TAG, "rejectedExecution: scheduled executor queue overflow"));
    }

    private static ExecutorService diskIoExecutor() {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), r -> new Thread(r, "disk_executor"), (r, executor) -> Log.e(TAG, "rejectedExecution: disk io executor queue overflow"));
    }

    private static ExecutorService networkExecutor() {
        return new ThreadPoolExecutor(3, 6, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(6), r -> new Thread(r, "network_executor"), (r, executor) -> Log.e(TAG, "rejectedExecution: network executor queue overflow"));
    }


    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    /*
    用法：
UI线程：

	AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                //do something
            }
        });
磁盘IO线程池

	AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //do something
            }
        });
网络IO线程池

	AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                //do something
            }
        });
定时(延时)任务线程池

延时3秒后执行：

AppExecutors.getInstance().scheduledExecutor().schedule(new Runnable() {
           @Override
           public void run() {
   			// do something
           }
       },3,TimeUnit.SECONDS);

1
2
3
4
5
6
7
5秒后启动第一次,每3秒执行一次(第一次开始执行和第二次开始执行之间间隔3秒)

AppExecutors.getInstance().scheduledExecutor().scheduleAtFixedRate(new Runnable() {
           @Override
           public void run() {
   			// do something
           }
       }, 5, 3, TimeUnit.MILLISECONDS);

5秒后启动第一次,每3秒执行一次(第一次执行完成和第二次开始之间间隔3秒)

AppExecutors.getInstance().scheduledExecutor().scheduleWithFixedDelay(new Runnable() {
           @Override
           public void run() {
   			// do something
           }
       }, 5, 3, TimeUnit.MILLISECONDS);

取消 定时(延时)任务

上面3个方法都会有一个如下返回值:

ScheduledFuture<?> scheduledFuture;
1
取消定时器(等待当前任务结束后，取消定时器)

scheduledFuture.cancel(false);
1
取消定时器(不等待当前任务结束，取消定时器)

scheduledFuture.cancel(true);
1
当然，以上用的都是双重检查（double-check）单例模式。
    * **/


}



