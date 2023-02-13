package com.kulala.staticsview.image.gifview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kulala.staticsfunc.BuildConfig;

import java.io.InputStream;

public class GifView extends View
        implements GifAction {
    private GifDecoder gifDecoder = null;

    private Bitmap currentImage = null;

    private boolean isRun = true;

    private boolean pause = false;

    private int  showWidth  = -1;
    private int  showHeight = -1;

    private DrawThread drawThread = null;

    private GifImageType animationType = GifImageType.SYNC_DECODER;

    private Handler redrawHandler = new Handler() {
        public void handleMessage(Message msg) {
            GifView.this.invalidate();
        }
    };
    public void exit() {
        isRun = false;
        pause = true;
        currentImage = null;
        gifDecoder = null;
    }

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void setGifDecoderImage(byte[] gif) {
        if (this.gifDecoder != null) {
            this.gifDecoder.free();
            this.gifDecoder = null;
        }
        this.gifDecoder = new GifDecoder(gif, this);
        this.gifDecoder.start();
    }

    private void setGifDecoderImage(InputStream is) {
        if (this.gifDecoder != null) {
            this.gifDecoder.free();
            this.gifDecoder = null;
        }
        this.gifDecoder = new GifDecoder(is, this);
        this.gifDecoder.start();
    }

    public void setGifImage(byte[] gif) {
        setGifDecoderImage(gif);
    }

    public void setGifImage(InputStream is) {
        setGifDecoderImage(is);
    }

    public void setGifImage(int resId) {
        Resources   r  = getResources();
        InputStream is = r.openRawResource(resId);
        setGifDecoderImage(is);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.gifDecoder == null) return;
        if (this.currentImage == null) this.currentImage = this.gifDecoder.getImage();
        if (this.currentImage == null) return;
        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        if (this.showWidth == -1)
            canvas.drawBitmap(this.currentImage, 0.0F, 0.0F, null);
        else {
//            canvas.drawBitmap(this.currentImage, this.rectSrc, this.rectDest, null);
//            canvas.drawBitmap(this.currentImage, null, this.rectDest, null);
            Matrix matrix = new Matrix();
            float  scaleW = (float) this.showWidth / (float) this.currentImage.getWidth();
            float  scaleH = (float) this.showHeight / (float) this.currentImage.getHeight();
//            matrix.preTranslate(skinPos.x,skinPos.y);//在setScale前,平移
            matrix.postScale(scaleW, scaleH);
            canvas.drawBitmap(this.currentImage, matrix, null);
            matrix = null;
        }
        canvas.restoreToCount(saveCount);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int pleft   = getPaddingLeft();
        int pright  = getPaddingRight();
        int ptop    = getPaddingTop();
        int pbottom = getPaddingBottom();
        int h, w;
        if (this.gifDecoder == null) {
            w = 1;
            h = 1;
        } else {
            if (this.showWidth != -1) {
                w = this.showWidth;
                h = this.showHeight;
            } else {
                w = this.gifDecoder.width;
                h = this.gifDecoder.height;
            }
        }

        w += pleft + pright;
        h += ptop + pbottom;

        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());

        int widthSize  = resolveSize(w, widthMeasureSpec);
        int heightSize = resolveSize(h, heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    public void showCover() {
        if (this.gifDecoder == null)
            return;
        this.pause = true;
        this.currentImage = this.gifDecoder.getImage();
        invalidate();
    }

    public void showAnimation() {
        if (this.pause)
            this.pause = false;
    }

    public void setGifImageType(GifImageType type) {
        if (this.gifDecoder == null)
            this.animationType = type;
    }

    public void setShowDimension(int w, int h) {
        if ((w > 0) && (h > 0)) {
            this.showWidth = w;
            this.showHeight = h;
        }
    }

    public void parseOk(boolean parseStatus, int frameIndex) {
        if (parseStatus)
            if (this.gifDecoder != null)
//                int resulta = GifImageType()[this.animationType.ordinal()];
//                $SWITCH_TABLE$com$ant$liao$GifView$GifImageType()[this.animationType.ordinal()]
                switch (this.animationType.ordinal()) {
                    case 1:
                        if (frameIndex != -1) break;
                        if (this.gifDecoder.getFrameCount() > 1) {
                            DrawThread dt = new DrawThread();
                            dt.start();
                        } else {
                            reDraw();
                        }

                        break;
                    case 3:
                        if (frameIndex == 1) {
                            this.currentImage = this.gifDecoder.getImage();
                            reDraw();
                        } else if (frameIndex == -1) {
                            if (this.gifDecoder.getFrameCount() > 1) {
                                if (this.drawThread == null) {
                                    this.drawThread = new DrawThread();
                                    this.drawThread.start();
                                }
                            } else reDraw();
                        }

                        break;
                    case 2:
                        if (frameIndex == 1) {
                            this.currentImage = this.gifDecoder.getImage();
                            reDraw();
                        } else if (frameIndex == -1) {
                            reDraw();
                        } else if (this.drawThread == null) {
                            this.drawThread = new DrawThread();
                            this.drawThread.start();
                        }
                        break;
                    default:
                        break;
                }
            else
                if(BuildConfig.DEBUG) Log.e("gif", "parse error");
    }

    private void reDraw() {
        if (this.redrawHandler != null) {
            Message msg = this.redrawHandler.obtainMessage();
            this.redrawHandler.sendMessage(msg);
        }
    }

    private class DrawThread extends Thread {
        private DrawThread() {
        }

        public void run() {
            try {
                if (GifView.this.gifDecoder == null) {
                    return;
                }
                while (GifView.this.isRun)
                    if (!GifView.this.pause) {
                        GifFrame frame = GifView.this.gifDecoder.next();
                        if(frame==null)return;
                        GifView.this.currentImage = frame.image;
                        long sp = frame.delay;
                        if (GifView.this.redrawHandler == null) break;
                        Message msg = GifView.this.redrawHandler.obtainMessage();
                        GifView.this.redrawHandler.sendMessage(msg);
                        SystemClock.sleep(sp);
                    } else {
                        SystemClock.sleep(10L);
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static enum GifImageType {
        WAIT_FINISH(0),

        SYNC_DECODER(1),

        COVER(2);

        final int nativeInt;

        private GifImageType(int i) { this.nativeInt = i; }

    }
}
