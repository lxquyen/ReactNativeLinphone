package com.demoreactnativelinphoneandroid.linphonemodule.ui;

import android.view.TextureView;

import androidx.annotation.NonNull;

import com.demoreactnativelinphoneandroid.linphonemodule.LinphoneService;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class VideoViewManager extends SimpleViewManager<TextureView> {
    public static final int VIDEO_TYPE_MY = 0;
    public static final int VIDEO_TYPE_OTHER = 1;

    public static final String TAG = "VideoView";

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @NonNull
    @Override
    protected TextureView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new TextureView(reactContext);
    }

    @ReactProp(name = "videoType")
    public void setVideoType(TextureView textureView, int videoType) {
        if (videoType == VIDEO_TYPE_MY) {
            LinphoneService.getCore().setNativePreviewWindowId(textureView);
        } else {
            LinphoneService.getCore().setNativeVideoWindowId(videoType);
        }
    }
}
