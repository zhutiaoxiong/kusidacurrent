package com.kulala.staticsfunc.static_assistant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
public class BlurBitmapHelper {
    /**
     * 图片缩放比例
     */
    private static float BITMAP_MAX_WIDTH = 800f;

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param imageSrc   需要模糊的图片
     * @param blurRadius (常用20) 最大25,最小1
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap imageSrc, float blurRadius) {
        Log.i("Blur","blurRadius:"+blurRadius);
        /**创建图片副本*/
        //1.在内存中创建一个与原图一模一样大小的bitmap对象，创建与原图大小一致的白纸
        Bitmap image = Bitmap.createBitmap(imageSrc.getWidth(), imageSrc.getHeight(), imageSrc.getConfig());
        //2.创建画笔对象
        Paint paint = new Paint();
        //3.创建画板对象，把白纸铺在画板上
        Canvas canvas = new Canvas(image);
        //4.开始作画，把原图的内容绘制在白纸上
        canvas.drawBitmap(imageSrc, new Matrix(), paint);
        /**开始缩放 计算图片缩小后的长宽*/
        float scale = (image.getWidth()>image.getHeight()) ? BITMAP_MAX_WIDTH/image.getWidth() : BITMAP_MAX_WIDTH/image.getHeight();
        int width = image.getWidth();
        int height = image.getHeight();
        if(image.getWidth() > 800 || image.getHeight()>800){//如果图片比800像素大，就缩小
            width = Math.round(image.getWidth() * scale);
            height = Math.round(image.getHeight() * scale);
        }
        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn  = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        if(blurRadius>25)blurRadius = 25;
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
    /**
     * 将彩色图转换为纯黑白二色
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return resizeBmp;
    }
}