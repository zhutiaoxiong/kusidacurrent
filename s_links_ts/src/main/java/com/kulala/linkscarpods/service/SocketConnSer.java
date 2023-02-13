package com.kulala.linkscarpods.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.interfaces.OnSocketStateListener;
import com.kulala.linkspods.BuildConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 这是TCP底层SocketChannel连接，服务端不支持tcpClient阻塞连接和WEbSocket连接
 * socket链接 cmd1: cmd2:心跳 cmd3:服务器回包
 */
public class SocketConnSer {
    private Context context;
    private static long    RECONNTIME      = 1000L;
    private static int     CONN_TIMEOUT    = 13000;
    private        boolean needBackMessage = false;
    public static long SOCKET_SEND_TIME, SOCKET_RECEIVE_TIME;
    public static String SOCKET_RECEIVE_MESSAGE = "SOCKET_RECEIVE_MESSAGE";
    private Selector        mSelector;//信道选择器
    private SocketChannel   mChannel;//信道

    //    private PowerManager.WakeLock mWakeLock;
    private ReceiveWatchThread readThread;

    private OnSocketStateListener onSocketStateListener;
    //=================================================================
    private static SocketConnSer _instance     = null;
    public static synchronized SocketConnSer getInstance() {
        if (_instance == null) {
            _instance = new SocketConnSer();
        }
        return _instance;
    }

    public void init(Context context) {
        this.context = context;
    }
    //=================================================================
    public void changeUserId() {
        reConnect("changeUserId");
    }
    /**
     * Socket连接是否是正常的
     */
    public boolean isConnected() {//是正在连接，连接上的
        boolean isConnect = false;
        if (mChannel!=null)isConnect = mChannel.isConnected();
        return isConnect;
    }
    /**
     * 服务器是否关闭，通过发送一个socket信息,一般用在心跳
     */
    public boolean canConnectToServer() {
        try {
            if (mChannel != null){
                mChannel.socket().sendUrgentData(0xff);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private synchronized boolean repareRead() {
        boolean bRes = false;
        if(mChannel!=null) {
            try {
                mSelector = Selector.open();//打开并注册选择器到信道
                mChannel.register(mSelector, SelectionKey.OP_READ);
                bRes = true;
            } catch (Exception e) {
                 if (BuildConfig.DEBUG) Log.e("Socket", "repareRead Exception:" + e.toString());
            }
        }
        return bRes;
    }
    public void close() {
        try {
            SOCKET_RECEIVE_TIME = 0;
            SOCKET_SEND_TIME = 0;
            if (mSelector != null)mSelector.close();
            if (mChannel != null)mChannel.close();
        } catch (Exception e) {
             if (BuildConfig.DEBUG) Log.e("Socket","close Exception:"+e.toString());
        }
    }
//=============================initOK==============================
    /**
     * 关闭socket 重新连接,试试能不能walklock
     */
    private static long preReConnTime = 0;
    public  void reConnect(final String fromInfo) {
        if(System.currentTimeMillis() - preReConnTime<500L)return;
        preReConnTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                 if (BuildConfig.DEBUG) Log.e("SocketReConn", "1-------重连启动:"+fromInfo);
                if (SocketUtil.getSocketPort(context) == 0) return;
                close();//isConnected = false;
                boolean done = false;
                // 打开监听信道并设置为非阻塞模式
                try {
                    mChannel = SocketChannel.open(new InetSocketAddress(SocketUtil.getSocketIp(context), SocketUtil.getSocketPort(context)));
//                     if (BuildConfig.DEBUG) Log.e("SocketReConn", "2-------通道开启成功");
                } catch (IOException e) {
                     if (BuildConfig.DEBUG) Log.e("SocketReConn", "2-------通道开启失败"+e.toString());//一般是网络切换，网络不良
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    reConnect("重连通道开启失败");
                    return;
                }
                try {
                    if (mChannel != null) {
                        mChannel.configureBlocking(false);//非阻塞
                        mChannel.socket().setTcpNoDelay(false);
                        mChannel.socket().setKeepAlive(true);
                        //设置超时时间
                        mChannel.socket().setSoTimeout(CONN_TIMEOUT);

                        // 打开并注册选择器到信道
//                         if (BuildConfig.DEBUG) Log.e("SocketReConn", "3-------打开选择器");
                        Selector selector = Selector.open();
                        if (selector != null) {
                            mChannel.register(selector, SelectionKey.OP_READ);
                            done = true;
//                             if (BuildConfig.DEBUG) Log.e("SocketReConn", "4-------重连完成，准备收发包 repareRead");
                            if (!repareRead()) {//reg mSelector
//                                 if (BuildConfig.DEBUG) Log.e("SocketReConn", "4-------重连完成，准备收发包 repareRead 失败");
                                close();
                                if (onSocketStateListener != null)onSocketStateListener.onConnFailed("reg mSelector Error");
                                return;
                            }
                            readThread = new ReceiveWatchThread(mSelector, System.currentTimeMillis());
                            readThread.start();
                            JsonObject objmsg = SocketUtil.getpHead(context);
                            sendMessage(1, objmsg.toString());
                             if (BuildConfig.DEBUG) Log.e("ServiceC","5-------重连成功，完成发包 cmd 1:"+objmsg.toString());
                            //180911新加心跳
                            SocketHeartThread.getInstance().startThread();
                        }else{
                             if (BuildConfig.DEBUG) Log.e("ServiceC", "3-------重连失败 selector null");
                        }
                    }
                }catch (Exception e) {
                     if (BuildConfig.DEBUG) Log.e("ServiceC", "3-------重连失败:"+e.toString());
                    if (onSocketStateListener != null)onSocketStateListener.onConnFailed(e.toString());
                } finally {
                    if (!done)close();
                }
            }
        }).start();
    }

//    private static boolean preStateWifi = true;
//    public static boolean isNetConnChanged(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (manager == null) return false;//获取wifi连接状态
//        NetworkInfo.State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//        if (state == NetworkInfo.State.CONNECTED){
//            if(!preStateWifi){preStateWifi = true;return true;}
//            preStateWifi = true;
//            return false;
//        }
//        NetworkInfo.State state1 = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
//        if (state1 == NetworkInfo.State.CONNECTED){
//            if(preStateWifi){preStateWifi = false;return true;}
//            preStateWifi = false;
//            return false;
//        }
//        return false;
//    }
    public void sendMessage(final int cmd, final String str) {
//        if(isNetConnChanged(context)){reConnect("change wifi");return;}
        new Thread(new Runnable() {
            @Override
            public void run() {
//                if(cmd == 4)cacheMessage = str;//发指令不要发二次
                SOCKET_SEND_TIME = System.currentTimeMillis();//防止检测不停发包
                if (!isConnected()) {
                    LogMeLinks.e("TsControl", "1.Socket发送指令需要重连 cmd:" + cmd);
//                    SOCKET_SEND_TIME = System.currentTimeMillis();//防止检测不停发包
                    reConnect("Socket发送指令需要重连");
                    return;
                }
                try {
                    LogMeLinks.e("TsControl","1.Socket发送指令启动 cmd:" + cmd);

                    ByteBuffer buffer = ByteBuffer.allocate(1024 * 64);
                    byte[]     byts;
//                    if (cmd == 1) {
//                        byts = RSA.RSAgenerator(str, RSA.publicKey);
//                    } else {
////                        byts = str.getBytes(SocketUtil.CODE_TYPE);
//                    }
                    byts = str.getBytes(SocketUtil.CODE_TYPE);
                    LogMeLinks.e("TsControl","2.Socket真实发指令准备:");
//                     if (BuildConfig.DEBUG) Log.e("LOCK", ">>>>>>>>test CONTROL: 7 cmd:"+cmd);
                    byte cmdb = (byte) cmd;
                    buffer.put(cmdb);
                    buffer.putInt(byts.length);
                    buffer.put(byts);
                    buffer.put("\r\n".getBytes("utf-8"));
//                     if (BuildConfig.DEBUG) Log.e("TsControl","        4. Socket真实发指令准备: 2");
                    buffer.flip();
                    if(mChannel!=null&&mChannel.isConnected()){
                        int nCount = mChannel.write(buffer);
                         if (BuildConfig.DEBUG) Log.e("------------","3.Socket发送指令成功 cmd:" + cmd+"str"+str);
                    }
                    if(mChannel!=null){
                        if( !mChannel.socket().isClosed()){
                            mChannel.socket().setKeepAlive(true);
                        }
                    }
                    /**服务端推送命令（cmd=3，返回cmd=103）*/
                    if (cmd != 103) SOCKET_SEND_TIME = System.currentTimeMillis();
//                    if(cmd == 4)cacheMessage = null;
//                     if (BuildConfig.DEBUG) Log.e("LOCK", ">>>>>>>>test CONTROL: 8 cmd:"+cmd);
                    LogMeLinks.e("TsControl","3.Socket发送指令成功 cmd:" + cmd);
                    if (onSocketStateListener != null) onSocketStateListener.onSendOK(cmd);

//                    TimerTask task = new TimerTask() {
//                        public void run() {
//                            if(cacheMessage!=null){
//                                cacheMessage = null;//发指令不要发二次,这是断了管道的情况
//                                reConnect("判断断线重连,2500发数据还没收到");
//                            }
//                        }
//                    };
//                    Timer timer = new Timer();
//                    timer.schedule(task, 2500L);
                } catch (SocketException e) {
                    if (onSocketStateListener != null)
                        onSocketStateListener.onSendFailed(cmd, "1str:" + str + "\n" + e.toString());
                    reConnect("Socket发送指令异常,需要重连1");
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    if (onSocketStateListener != null)
                        onSocketStateListener.onSendFailed(cmd, "2str:" + str + "\n" + e.toString());
                    reConnect("Socket发送指令异常,需要重连2");
                    e.printStackTrace();
                } catch (IOException e) {
                    if (onSocketStateListener != null)
                        onSocketStateListener.onSendFailed(cmd, "3str:" + str + "\n" + e.toString());
                    reConnect("Socket发送指令异常,需要重连3");
                    e.printStackTrace();
                } catch (Exception e) {
                    if (onSocketStateListener != null)
                        onSocketStateListener.onSendFailed(cmd, "4str:" + str + "\n" + e.toString());
                    reConnect("Socket发送指令异常,需要重连4");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // ================================read Thread===================================

    private static long ReceiveWatchThread_singleId;

    class ReceiveWatchThread extends Thread {
        private Selector mSelector;
        private long     thread_singleId_check;

        public ReceiveWatchThread(Selector selector, final long time) {
            mSelector = selector;
            ReceiveWatchThread_singleId = time;
            thread_singleId_check = time;
        }
        public void run() {
            try {
//                 if (BuildConfig.DEBUG) Log.e("SocketConn", "ReceiveWatchThread--->");
                while (thread_singleId_check == ReceiveWatchThread_singleId && isConnected() && mSelector.select() > 0) {
                    for (SelectionKey sk : mSelector.selectedKeys()) {
                        // 如果该SelectionKey对应的Channel中有可读的数据 && sk.isAcceptable()
                        if (sk.isReadable()) {
                            //打开唤醒
//                            if (KulalaServiceA.uiOpen == false) {
//                                PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                                mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "UMSE PowerTest");
//                                if (mWakeLock != null) {
//                                    mWakeLock.acquire();
//                                }
//                            }
                            // 使用NIO读取Channel中的数据
                            SocketChannel sc         = (SocketChannel) sk.channel();

                            int           lengthTemp = 0;
                            ByteBuffer    buffer     = ByteBuffer.allocate(1024 * 64);
                            if(sc.isConnected()){
                                while (-1 != (lengthTemp = sc.read(buffer))) { // read方法并不保证一次能读取1024*64个字节
                                    if (lengthTemp == 0) break;
                                    buffer.flip();
                                    byte[] enData = new byte[lengthTemp];
                                    buffer.get(enData, 0, lengthTemp);
                                    buffer.clear();//重置
                                    //读完一组
                                    SOCKET_RECEIVE_TIME = System.currentTimeMillis();
                                    new SocketDataGet(context).readData(enData);
                                }
                            }
                            // 为下一次读取作准备
                            sk.interestOps(SelectionKey.OP_READ);
//                            if (KulalaServiceA.uiOpen == false) {
//                                if (mWakeLock != null) {
//                                    mWakeLock.release();
//                                    mWakeLock = null;
//                                }
//                            }
                        }
                        // 删除正在处理的SelectionKey
                        mSelector.selectedKeys().remove(sk);
                    }
                }
            } catch (Exception e) {
//                 if (BuildConfig.DEBUG) Log.e("SocketConn receive", e.toString());
                reConnect("SocketConn receive exception"+e.toString());
            }
        }
    }

    // ======================================================
    /**
     * 连接装态改变
     */
    public void setOnConnStateChangeListener(OnSocketStateListener listener) {
        this.onSocketStateListener = listener;
    }
    // ===================================================================
}