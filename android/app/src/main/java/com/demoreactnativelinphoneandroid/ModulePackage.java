package com.demoreactnativelinphoneandroid;

import androidx.annotation.NonNull;

import com.demoreactnativelinphoneandroid.linphonemodule.LinphoneModule;
import com.demoreactnativelinphoneandroid.linphonemodule.ui.VideoViewManager;
import com.demoreactnativelinphoneandroid.toastmodule.ToastModule;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModulePackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new ToastModule(reactContext));
        modules.add(new LinphoneModule(reactContext));

        return modules;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        List<ViewManager> managers = new ArrayList<>();

        managers.add(new VideoViewManager());

        return managers;
    }
}
