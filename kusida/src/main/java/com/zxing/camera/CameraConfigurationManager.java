/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain getProcessName copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

final class CameraConfigurationManager {

  private static final String TAG = CameraConfigurationManager.class.getSimpleName();

  private static final int TEN_DESIRED_ZOOM = 27;
  private static final int DESIRED_SHARPNESS = 30;

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;
  private int previewFormat;
  private String previewFormatString;
  static final int MIN_PREVIEW_PIXELS = 480 * 320;
  static final double MAX_ASPECT_DISTORTION = 0.15;

  CameraConfigurationManager(Context context) {
    this.context = context;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  @SuppressWarnings("deprecation")
void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    previewFormat = parameters.getPreviewFormat();
    previewFormatString = parameters.get("preview-format");
    Log.d(TAG, "Default preview format: " + previewFormat + '/' + previewFormatString);
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    screenResolution = new Point(display.getWidth(), display.getHeight());
    Point screenResolutionForCamera = new Point();
    screenResolutionForCamera.x = screenResolution.x;
    screenResolutionForCamera.y = screenResolution.y;
    // preview size is always something like 480*320, other 320*480
    if (screenResolution.x < screenResolution.y) {
      screenResolutionForCamera.x = screenResolution.y;
      screenResolutionForCamera.y = screenResolution.x;
    }
    Log.d(TAG, "Screen resolution: " + screenResolution);
    cameraResolution = findBestPreviewSizeValue(parameters, screenResolutionForCamera);
//    cameraResolution = getCameraResolution(parameters, screenResolutionForCamera);
//    cameraResolution = getCameraResolution(parameters, screenResolution);
    Log.d(TAG, "Camera resolution: " + screenResolution);
  }

//  @SuppressWarnings("deprecation")
//void initFromCameraParameters(Camera camera) {
//    Camera.Parameters parameters = camera.getParameters();
//    previewFormat = parameters.getPreviewFormat();
//    previewFormatString = parameters.get("preview-format");
//    Log.d(TAG, "Default preview format: " + previewFormat + '/' + previewFormatString);
//    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//    Display display = manager.getDefaultDisplay();
//    screenResolution = new Point(display.getWidth(), display.getHeight());
//    Point screenResolutionForCamera = new Point();
//    screenResolutionForCamera.x = screenResolution.x;
//    screenResolutionForCamera.y = screenResolution.y;
//    // preview size is always something like 480*320, other 320*480
//    if (screenResolution.x < screenResolution.y) {
//      screenResolutionForCamera.x = screenResolution.y;
//      screenResolutionForCamera.y = screenResolution.x;
//    }
//    Log.d(TAG, "Screen resolution: " + screenResolution);
//    cameraResolution = findBestPreviewSizeValue(previewFormatString, screenResolutionForCamera);
////    if (screenResolution.x < screenResolution.y) {
////      screenResolutionForCamera.x = screenResolution.y;
////      screenResolutionForCamera.y = screenResolution.x;
////    }
////    cameraResolution = getCameraResolution(parameters, screenResolution);
//    Log.d(TAG, "Camera resolution: " + screenResolution);
//  }

  /**
   * Sets the camera up to take preview images which are used for both preview and decoding.
   * We detect the preview format here so that buildLuminanceSource() can build an appropriate
   * LuminanceSource subclass. In the future we may want to force YUV420SP as it's the smallest,
   * and the planar Y can be used for barcode scanning without getProcessName copy in some cases.
   */
//  void setDesiredCameraParameters(Camera camera) {
//    Camera.Parameters parameters = camera.getParameters();
//    Log.d(TAG, "Setting preview size: " + cameraResolution);
//    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
//    setFlash(parameters);
//    setZoom(parameters);
//    //setSharpness(parameters);
//    //modify here
//    camera.setDisplayOrientation(90);
//    camera.setParameters(parameters);
//  }

//  void setDesiredCameraParameters(Camera camera) {
//    Camera.Parameters parameters = camera.getParameters();
//    List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
//    int position =0;
//    if(supportedPreviewSizes.size()>2){
//      position=supportedPreviewSizes.size()/2+1;//supportedPreviewSizes.get();
//    }else {
//      position=supportedPreviewSizes.size()/2;
//    }
//    int width = supportedPreviewSizes.get(position).width;
//    int height = supportedPreviewSizes.get(position).height;
//    Log.d(TAG, "Setting preview size: " + cameraResolution);
////    camera.setDisplayOrientation(getDisplayRotation((Activity) context));
//    camera.setDisplayOrientation(90);
//    cameraResolution.x=width;
//    cameraResolution.y=height;
//    parameters.setPreviewSize(width,height);
////    if(parameters.isZoomSupported()){
////      parameters.setZoom(parameters.getMaxZoom()/10);
////    }
//    setFlash(parameters);
//    setZoom(parameters);
//    //setSharpness(parameters);
//    camera.setParameters(parameters);
//  }

  void setDesiredCameraParameters(Camera camera) {
//    Camera.Parameters parameters = camera.getParameters();
//
//    if (parameters == null) {
//      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
//      return;
//    }
//
//    Log.i(TAG, "Initial camera parameters: " + parameters.flatten());
//
////    if (safeMode) {
////      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
////    }
//
//    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
//    camera.setParameters(parameters);
//
//    Camera.Parameters afterParameters = camera.getParameters();
//    Camera.Size afterSize = afterParameters.getPreviewSize();
//    if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
//      Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y + ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
//      cameraResolution.x = afterSize.width;
//      cameraResolution.y = afterSize.height;
//    }
////    setFlash(parameters);
//    /** 设置相机预览为竖屏 */
//    camera.setDisplayOrientation(90);
    Camera.Parameters parameters = camera.getParameters();
    List<Camera.Size> supportedPreviewSizes=parameters.getSupportedPreviewSizes();
    int position=0;
    if(supportedPreviewSizes.size()>2){
      position=supportedPreviewSizes.size()/2+1;
    }else{
      position=supportedPreviewSizes.size()/2;
    }
    int width=supportedPreviewSizes.get(position).width;
    int height=supportedPreviewSizes.get(position).height;
    camera.setDisplayOrientation(90);
    cameraResolution.x=width;
    cameraResolution.y=height;
    parameters.setPreviewSize(width,height);
    setFlash(parameters);
    setZoom(parameters);
    camera.setParameters(parameters);
  }


  public static int getDisplayRotation(Activity activity) {
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    switch (rotation) {
      case Surface.ROTATION_0:
        return 0;
      case Surface.ROTATION_90:
        return 90;
      case Surface.ROTATION_180:
        return 180;
      case Surface.ROTATION_270:
        return 270;
    }
    return 0;
  }

  Point getCameraResolution() {
    return cameraResolution;
  }

  Point getScreenResolution() {
    return screenResolution;
  }
//  private static Point findBestPreviewSizeValue(Camera.Parameters parameters,
//                                                Point screenResolution) {
//    Point point = null;
//
//    Rect frame = CameraManager.get().getFramingRect(CameraManager.aliginTop);
//    int frameSize = frame.right - frame.left;
//    int discountMax = Integer.MAX_VALUE;
//    int width = 0;
//    int height = 0;
//    List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
//    for (Camera.Size size : supportedPreviewSizes) {
//      int discount = size.height - frameSize;
//      if (discount > 0 && size.height * screenResolution.x == screenResolution.y * size.width) {
//        if (discount < discountMax) {
//          discountMax = discount;
//          width = size.width;
//          height = size.height;
//        }
//      }
//    }
//    if (width * height != 0) {
//      point = new Point(width, height);
//    }
//    if (point == null) {
//      String previewSizeValueString = parameters.get("preview-size-values");
//      // saw this on Xperia
//      if (previewSizeValueString == null) {
//        previewSizeValueString = parameters.get("preview-size-value");
//      }
//
//      if (previewSizeValueString != null) {
//        Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
//        return findBestPreviewSizeValue(previewSizeValueString, screenResolution);
//      } else {
//        return null;
//      }
//    } else {
//      return point;
//    }
//  }
//  private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {
//
//    Point cameraResolution = null;
//
//    cameraResolution = findBestPreviewSizeValue(parameters, screenResolution);
//
//    if (cameraResolution == null) {
//      // Ensure that the camera resolution is a multiple of 8, as the
//      // screen may not be.
//      cameraResolution = new Point((screenResolution.x >> 3) << 3,
//              (screenResolution.y >> 3) << 3);
//    }
//
//    return cameraResolution;
//  }



  int getPreviewFormat() {
    return previewFormat;
  }

  String getPreviewFormatString() {
    return previewFormatString;
  }

//  private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {
//
//    String previewSizeValueString = parameters.get("preview-size-values");
//    // saw this on Xperia
//    if (previewSizeValueString == null) {
//      previewSizeValueString = parameters.get("preview-size-value");
//    }
//
//    Point cameraResolution = null;
//
//    if (previewSizeValueString != null) {
//      Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
//      cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
//    }
//
//    if (cameraResolution == null) {
//      // Ensure that the camera resolution is getProcessName multiple of 8, as the screen may not be.
//      cameraResolution = new Point(
//          (screenResolution.x >> 3) << 3,
//          (screenResolution.y >> 3) << 3);
//    }
//
//    return cameraResolution;
//  }
    private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {

    String previewSizeValueString = parameters.get("preview-size-values");
    // saw this on Xperia
    if (previewSizeValueString == null) {
      previewSizeValueString = parameters.get("preview-size-value");
    }

    Point cameraResolution = null;

    if (previewSizeValueString != null) {
      Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
      cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
    }

    if (cameraResolution == null) {
      // Ensure that the camera resolution is getProcessName multiple of 8, as the screen may not be.
      cameraResolution = new Point(
          (screenResolution.x >> 3) << 3,
          (screenResolution.y >> 3) << 3);
    }

    return cameraResolution;
  }

  private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
    int bestX = 0;
    int bestY = 0;
    //修改代碼
//    int diff = Integer.MAX_VALUE;
    float diff = Integer.MAX_VALUE;
    for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

      previewSize = previewSize.trim();
      int dimPosition = previewSize.indexOf('x');
      if (dimPosition < 0) {
        Log.w(TAG, "Bad preview-size: " + previewSize);
        continue;
      }

      int newX;
      int newY;
      try {
        newX = Integer.parseInt(previewSize.substring(0, dimPosition));
        newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad preview-size: " + previewSize);
        continue;
      }
        float newDiff = Math.abs(screenResolution.x * 1.0f / newY - screenResolution.y * 1.0f / newX);

//      int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
      //這是原來的代碼
//      int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
      if (newDiff == 0) {
        bestX = newX;
        bestY = newY;
        break;
      } else if (newDiff < diff) {
        bestX = newX;
        bestY = newY;
        diff = newDiff;
      }

    }

    if (bestX > 0 && bestY > 0) {
      return new Point(bestX, bestY);
    }
    return null;
  }

  private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
    int tenBestValue = 0;
    for (String stringValue : COMMA_PATTERN.split(stringValues)) {
      stringValue = stringValue.trim();
      double value;
      try {
        value = Double.parseDouble(stringValue);
      } catch (NumberFormatException nfe) {
        return tenDesiredZoom;
      }
      int tenValue = (int) (10.0 * value);
      if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
        tenBestValue = tenValue;
      }
    }
    return tenBestValue;
  }

  /**
   * 从相机支持的分辨率中计算出最适合的预览界面尺寸
   *
   * @param parameters
   * @param screenResolution
   * @return
   */
  Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {
    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      Log.w(TAG, "Device returned no supported preview sizes; using default");
      Camera.Size defaultSize = parameters.getPreviewSize();
      return new Point(defaultSize.width, defaultSize.height);
    }

    // Sort by size, descending
    List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
      @Override
      public int compare(Camera.Size a, Camera.Size b) {
        int aPixels = a.height * a.width;
        int bPixels = b.height * b.width;
        if (bPixels < aPixels) {
          return -1;
        }
        if (bPixels > aPixels) {
          return 1;
        }
        return 0;
      }
    });

//    if (Log.isLoggable(TAG, Log.INFO)) {
//      StringBuilder previewSizesString = new StringBuilder();
//      for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
//        previewSizesString.append(supportedPreviewSize.width).append('x').append(supportedPreviewSize.height).append(' ');
//      }
//      Log.i(TAG, "Supported preview sizes: " + previewSizesString);
//    }

    double screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;

    // Remove sizes that are unsuitable
    Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
    while (it.hasNext()) {
      Camera.Size supportedPreviewSize = it.next();
      int realWidth = supportedPreviewSize.width;
      int realHeight = supportedPreviewSize.height;
      if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
        it.remove();
        continue;
      }

      boolean isCandidatePortrait = realWidth < realHeight;
      int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
      int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;

      double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
      double distortion = Math.abs(aspectRatio - screenAspectRatio);
      if (distortion > MAX_ASPECT_DISTORTION) {
        it.remove();
        continue;
      }

      if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
        Point exactPoint = new Point(realWidth, realHeight);
        Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
        return exactPoint;
      }
    }

    // If no exact match, use largest preview size. This was not a great
    // idea on older devices because
    // of the additional computation needed. We're likely to get here on
    // newer Android 4+ devices, where
    // the CPU is much more powerful.
    if (!supportedPreviewSizes.isEmpty()) {
      Camera.Size largestPreview = supportedPreviewSizes.get(0);
      Point largestSize = new Point(largestPreview.width, largestPreview.height);
      Log.i(TAG, "Using largest suitable preview size: " + largestSize);
      return largestSize;
    }

    // If there is nothing at shouquan_ic_all suitable, return current preview size
    Camera.Size defaultPreview = parameters.getPreviewSize();
    Point defaultSize = new Point(defaultPreview.width, defaultPreview.height);
    Log.i(TAG, "No suitable preview sizes, using default: " + defaultSize);

    return defaultSize;
  }

  private void setFlash(Camera.Parameters parameters) {
    // FI_XME: This is getProcessName hack to turn the flash off on the Samsung Galaxy.
    // And this is getProcessName hack-hack to work around getProcessName different value on the Behold II
    // Restrict Behold II check to Cupcake, per Samsung's advice
    //if (Build.MODEL.contains("Behold II") &&
    //    CameraManager.SDK_INT == Build.VERSION_CODES.CUPCAKE) {
    if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) { // 3 = Cupcake
      parameters.set("flash-value", 1);
    } else {
      parameters.set("flash-value", 2);
    }
    // This is the standard setting to turn the flash off that all devices should honor.
    parameters.set("flash-mode", "off");
  }

  private void setZoom(Camera.Parameters parameters) {

    String zoomSupportedString = parameters.get("zoom-supported");
    if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
      return;
    }

    int tenDesiredZoom = TEN_DESIRED_ZOOM;

    String maxZoomString = parameters.get("max-zoom");
    if (maxZoomString != null) {
      try {
        int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad max-zoom: " + maxZoomString);
      }
    }

    String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
    if (takingPictureZoomMaxString != null) {
      try {
        int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad taking-picture-zoom-max: " + takingPictureZoomMaxString);
      }
    }

    String motZoomValuesString = parameters.get("mot-zoom-values");
    if (motZoomValuesString != null) {
      tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
    }

    String motZoomStepString = parameters.get("mot-zoom-step");
    if (motZoomStepString != null) {
      try {
        double motZoomStep = Double.parseDouble(motZoomStepString.trim());
        int tenZoomStep = (int) (10.0 * motZoomStep);
        if (tenZoomStep > 1) {
          tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
        }
      } catch (NumberFormatException nfe) {
        // continue
      }
    }

    // Set zoom. This helps encourage the user to pull back.
    // Some devices like the Behold have getProcessName zoom parameter
    if (maxZoomString != null || motZoomValuesString != null) {
      parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
    }

    // Most devices, like the Hero, appear to expose this zoom parameter.
    // It takes on values like "27" which appears to mean 2.7x zoom
    if (takingPictureZoomMaxString != null) {
      parameters.set("taking-picture-zoom", tenDesiredZoom);
    }
  }

	public static int getDesiredSharpness() {
		return DESIRED_SHARPNESS;
	}

}
