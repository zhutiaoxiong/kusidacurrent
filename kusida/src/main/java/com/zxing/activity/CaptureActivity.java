package com.zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.client.proj.kusida.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.decoding.RGBLuminanceSource;
import com.zxing.view.ViewfinderView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

import common.BitmapUtil;
import view.ActivityKulalaMain;
import view.view4me.set.ClipTitleMeSet;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler	handler;
	private ViewfinderView			viewfinderView;
//	private TextView				txt_title;
	private boolean					hasSurface;
	private Vector<BarcodeFormat>	decodeFormats;
	private String					characterSet;
	private InactivityTimer			inactivityTimer;
	private MediaPlayer				mediaPlayer;
	private boolean					playBeep;
	private static final float		BEEP_VOLUME	= 0.50f;
	private boolean					vibrate;
//	private Button					cancelScanButton;
    private ClipTitleMeSet title_head;
	private final int REQUEST_CODE_SCAN_GALLERY=11;
	private Bitmap scanBitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_capture);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
//		txt_title = (TextView) findViewById(R.id.txt_title);
//		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		initEvent();

		//???????????????????????????????????????
//		Bundle bundle = this.getIntent().getExtras();
//		String scantype = bundle.getString("scantype");
//		if (scantype.equals("qrcode")) {
////			txt_title.setText(getResources().getString(R.string.scan_the_qr_code));
//		} else if (scantype.equals("oned")) {
////			txt_title.setText(getResources().getString(R.string.scan_the_type_code));
//			decodeFormats = new Vector<BarcodeFormat>();
//			decodeFormats.add(BarcodeFormat.UPC_A);
//			decodeFormats.add(BarcodeFormat.UPC_E);
//			decodeFormats.add(BarcodeFormat.EAN_13);
//			decodeFormats.add(BarcodeFormat.EAN_8);
//			decodeFormats.add(BarcodeFormat.RSS14);
//			decodeFormats.add(BarcodeFormat.CODE_39);
//			decodeFormats.add(BarcodeFormat.CODE_93);
//			decodeFormats.add(BarcodeFormat.CODE_128);
//			decodeFormats.add(BarcodeFormat.ITF);
//		}
	}
	private void initEvent(){
		title_head.txt_right.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
					//????????????
					if ( permissionRead != PackageManager.PERMISSION_GRANTED) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
					} else {
//????????????????????????
						Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
						innerIntent.setType("image/*");
						startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
					}
				}else{
//????????????????????????
					Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					innerIntent.setType("image/*");
					startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
				}
			}
		});
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ActivityKulalaMain.GestureNeed = false;
				CaptureActivity.this.finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode==RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_SCAN_GALLERY:
					final Uri uri = data.getData();
					Result result = scanningImage(uri);
					if(result==null){
						Toast.makeText(CaptureActivity.this, "????????????", Toast.LENGTH_SHORT).show();
					}else{
						String resultString = result.getText();
						// FIX_ME
						if (resultString.equals("")) {
							Toast.makeText(CaptureActivity.this, "????????????", Toast.LENGTH_SHORT).show();
						} else {
//							Toast.makeText(CaptureActivity.this, resultString, Toast.LENGTH_SHORT).show();
							ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, resultString);
							ActivityKulalaMain.GestureNeed = false;
							CaptureActivity.this.finish();
						}
					}
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ??????????????????????????????
	 * @param uri
	 * @return
	 */
	public Result scanningImage(Uri uri) {
		if (uri == null) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //??????????????????????????????

		scanBitmap = BitmapUtil.decodeUri(this, uri, 500, 500);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		characterSet = null;
//		characterSet = "ISO-8859-1";

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		// quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListenerMy(){
//			@Override
//			public void onClickNoFast(View v) {
//				ActivityKulalaMain.GestureNeed = false;
//				CaptureActivity.this.finish();
//			}
//		});
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ActivityKulalaMain.GestureNeed = false;
				CaptureActivity.this.finish();
			}
		});
	}
	public static boolean isChineseCharacter(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			//?????????Unicode??????,??????"???"????????????.???????????????????????????
			if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')||((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}

	public static boolean isSpecialCharacter(String str){
		//???"???"?????????????????????????????????
		if(str.contains("??????")){
			return true;
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();

		// FIX_ME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
//
//			try {
//				String targetStr=new String(resultString.getBytes("ISO-8859-1"),"UTF-8");
//				ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, targetStr);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}

			// System.out.println("Result:"+resultString);
			// Intent resultIntent = new Intent();
			// Bundle bundle = new Bundle();
			// bundle.putString("result", resultString);
//		String resultsssss=	getDecodeResult(resultString);

//			ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, resultsssss);
			ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, resultString);


			// resultIntent.putExtras(bundle);
			// this.setResult(RESULT_OK, resultIntent);-
		}
		ActivityKulalaMain.GestureNeed = false;
		CaptureActivity.this.finish();
	}
	private String getDecodeResult(String result){
		String UTF_Str="";
		String GB_Str="";
		boolean is_cN=false;
		String resultStr="";
		try {
			UTF_Str=new String(result.getBytes("ISO-8859-1"),"UTF-8");
			is_cN=isChineseCharacter(UTF_Str);
			//??????????????????????????????????????????????????????????????????
			boolean b=isSpecialCharacter(result);
			if(b){
				is_cN=true;
				resultStr=UTF_Str;
			}
			System.out.println("??????:"+is_cN);
			if(!is_cN){
				GB_Str=new String(result.getBytes("ISO-8859-1"),"GB2312");
				resultStr=GB_Str;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultStr;
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long	VIBRATE_DURATION	= 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener	beepListener	= new OnCompletionListener() {
															public void onCompletion(MediaPlayer mediaPlayer) {
																mediaPlayer.seekTo(0);
															}
														};

}