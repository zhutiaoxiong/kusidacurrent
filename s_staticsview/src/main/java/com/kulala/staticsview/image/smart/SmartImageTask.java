package com.kulala.staticsview.image.smart;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SmartImageTask implements Runnable {
	
    private static final int BITMAP_READY = 0;

    private boolean cancelled = false;
    private OnCompleteHandler onCompleteHandler;
    private SmartImage image;
    private Context context;

    public static class OnCompleteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            onComplete(bitmap);
        }

        public void onComplete(Bitmap bitmap){};
    }

    public interface OnCompleteListener {
    	void onSuccess(Bitmap bitmap);
    	void onFail();
    }

    public SmartImageTask(Context context, SmartImage image) {
        this.image = image;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            if(image != null) {
                complete(image.getBitmap(context));
                context = null;
            }
        }catch (Exception e){
            Log.w("SmartImage",e.toString()+e.getMessage());
        }
    }

    public void setOnCompleteHandler(OnCompleteHandler handler){
        this.onCompleteHandler = handler;
    }

    public void cancel() {
        cancelled = true;
    }

    public void complete(Bitmap bitmap){
        if(onCompleteHandler != null && !cancelled) {
            onCompleteHandler.sendMessage(onCompleteHandler.obtainMessage(BITMAP_READY, bitmap));
        }
    }
}