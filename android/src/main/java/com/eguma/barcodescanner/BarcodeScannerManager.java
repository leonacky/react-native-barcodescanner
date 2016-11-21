package com.eguma.barcodescanner;

import android.view.View;

import javax.annotation.Nullable;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.bridge.ReadableArray;

public class BarcodeScannerManager extends ViewGroupManager<BarcodeScannerView> implements LifecycleEventListener {
    private static final String REACT_CLASS = "RNBarcodeScannerView";

    private static final String DEFAULT_TORCH_MODE = "off";
    private static final String DEFAULT_CAMERA_TYPE = "back";
    private static final int COMMAND_STOP_CAMERA = 0x1;
	private static final int COMMAND_START_CAMERA = 0x2;

    private BarcodeScannerView mScannerView;
    private boolean mScannerViewVisible;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "cameraType")
    public void setCameraType(BarcodeScannerView view, @Nullable String cameraType) {
      if (cameraType != null) {
          view.setCameraType(cameraType);
      }
    }

    @ReactProp(name = "torchMode")
    public void setTorchMode(BarcodeScannerView view, @Nullable String torchMode) {
        if (torchMode != null) {
            view.setFlash(torchMode.equals("on"));
        }
    }

    @Override
    public BarcodeScannerView createViewInstance(ThemedReactContext context) {
        context.addLifecycleEventListener(this);
        mScannerView = new BarcodeScannerView(context);
        mScannerView.setCameraType(DEFAULT_CAMERA_TYPE);
        mScannerView.setFlash(DEFAULT_TORCH_MODE.equals("on"));
        mScannerViewVisible = true;
        return mScannerView;
    }

    @Override
    public void onHostResume() {
        mScannerView.onResume();
    }

    @Override
    public void onHostPause() {
        mScannerView.onPause();
    }

    @Override
    public void onHostDestroy() {
        mScannerView.stopCamera();
    }

    @Override
    public void addView(BarcodeScannerView parent, View child, int index) {
        parent.addView(child, index + 1);   // index 0 for camera preview reserved
    }
    
    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
        	"stopCamera", COMMAND_STOP_CAMERA,
        	"startCamera", COMMAND_START_CAMERA
        );
    }

    @Override
    public void receiveCommand(BarcodeScannerView root, int commandId, @Nullable ReadableArray args) {
        if (commandId == COMMAND_STOP_CAMERA) {
            root.stopCamera();
        }
        if (commandId == COMMAND_START_CAMERA) {
            root.startCamera();
        }
    }
}
