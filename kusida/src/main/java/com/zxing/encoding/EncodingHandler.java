package com.zxing.encoding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
/**
 * @author Ryan Tang
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;

    public static byte[] createQRCodeByteArr(String str,Bitmap logoBm) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 100, 100);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }else{//不要else就透明
                    pixels[y * width + x] = WHITE;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        if (logoBm != null) {
            bitmap = addLogo(bitmap, logoBm);//加了logo不识别
        }
//		return bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);//jpeg不识别
        byte[] bitmapDataForSave = baos.toByteArray();
        matrix = null;
        bitmap = null;
        return bitmapDataForSave;
    }
	public static Bitmap createQRCode(String str,Bitmap logoBm) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}else{//不要else就透明
                    pixels[y * width + x] = WHITE;
                }
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		if (logoBm != null) {
            bitmap = addLogo(bitmap, logoBm);//加了logo不识别
        }
//		return bitmap;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);//jpeg不识别
		byte[] bitmapDataForSave = baos.toByteArray();
		Bitmap bitmapResult = BitmapFactory.decodeByteArray(bitmapDataForSave, 0, bitmapDataForSave.length);
        matrix = null;
        bitmap = null;
		return bitmapResult;
	}

//    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.car_body_normal);//not this
//    //			sendWxShareImage(thumb);
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    thumb.compress(Bitmap.CompressFormat.JPEG, 80, baos);//jpeg不识别
//    byte[] bitmapDataForSave = baos.toByteArray();
	/** 
     * 在二维码中间添加Logo图案 
     */  
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {  
        if (src == null) {  
            return null;  
        }  
  
        if (logo == null) {  
            return src;  
        }  
  
        //获取图片的宽高  
        int srcWidth = src.getWidth();  
        int srcHeight = src.getHeight();  
        int logoWidth = logo.getWidth();  
        int logoHeight = logo.getHeight();  
  
        if (srcWidth == 0 || srcHeight == 0) {  
            return null;  
        }  
  
        if (logoWidth == 0 || logoHeight == 0) {  
            return src;  
        }  
  
        //logo大小为二维码整体大小的1/6
        float scaleFactor = srcWidth * 1.0f / 6 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);  
        try {  
            Canvas canvas = new Canvas(bitmap);  
            canvas.drawBitmap(src, 0, 0, null);  
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);  
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);  
  
            canvas.save();
            canvas.restore();  
        } catch (Exception e) {  
            bitmap = null;  
            e.getStackTrace();  
        }  
  
        return bitmap;  
    }  
}
