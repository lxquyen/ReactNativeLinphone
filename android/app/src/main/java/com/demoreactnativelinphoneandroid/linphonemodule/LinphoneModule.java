package com.demoreactnativelinphoneandroid.linphonemodule;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;

public class LinphoneModule extends ReactContextBaseJavaModule {
    public static final String TAG = "LinphoneModule";

    public static final String SIP_DOMAIN = "192.168.17.74:5060";

    public static final String ON_SERVICE_READY = "ON_SERVICE_READY";
    public static final String ON_CALL_STATE_CHANGED = "ON_CALL_STATE_CHANGED";
    public static final String ON_REGISTRATION_STATE_CHANGED = "ON_REGISTRATION_STATE_CHANGED";

    public static final String KEY_STATE = "state";
    public static final String KEY_MESSAGE = "message";

    private static ReactContext mReactContext;

    private Handler mHandler;


    public LinphoneModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);

        mReactContext = reactContext;

        mHandler = new Handler(Looper.getMainLooper());

        if (LinphoneService.isReady()) {
            onServiceReady();
        } else {
            reactContext.startService(new Intent().setClass(reactContext, LinphoneService.class));
            new ServiceWaitThread().start();
        }
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void connect(String account, String password) {
        AccountCreator accountCreator = LinphoneService.getCore().createAccountCreator(null);
        accountCreator.setUsername(account);
        accountCreator.setDomain(SIP_DOMAIN);
        accountCreator.setPassword(password);
        accountCreator.setTransport(TransportType.Tcp);

        ProxyConfig config = accountCreator.createProxyConfig();
        LinphoneService.getCore().setDefaultProxyConfig(config);
    }

    @ReactMethod
    public void call(String account) {
        Core core = LinphoneService.getCore();
        Address addressToCall = core.interpretUrl(account);
        CallParams params = core.createCallParams(null);
        params.enableVideo(true);


        if (addressToCall != null) {
            core.inviteAddressWithParams(addressToCall, params);
        }
    }

    @ReactMethod
    public void answer() {
        Call currentCall = getCurrentCall();
        if (currentCall != null) {
            currentCall.accept();
        }
    }

    @ReactMethod
    public void decline() {
        Call currentCall = getCurrentCall();
        if (currentCall != null) {
            currentCall.terminate();
        }
    }

    private void onServiceReady() {
        LinphoneService.getCore().addListener(new MyCoreListenerStub());
    }

    private Call getCurrentCall() {
        Core core = LinphoneService.getCore();
        for (Call call : core.getCalls()) {
            if (call.getState() == Call.State.IncomingReceived || call.getState() == Call.State.IncomingEarlyMedia) {
                return call;
            }
        }
        return null;
    }

    private class ServiceWaitThread extends Thread {
        @Override
        public void run() {
            while (!LinphoneService.isReady()) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }
            mHandler.post(LinphoneModule.this::onServiceReady);
        }
    }


    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (mReactContext == null) return;
        mReactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    /**
     * Custom CoreListenerStub
     */
    private class MyCoreListenerStub extends CoreListenerStub {
        @Override
        public void onCallStateChanged(Core lc, Call call, Call.State cstate, String message) {
            Log.i(TAG, "---------> " + cstate.toString());
            WritableMap params = Arguments.createMap();

            params.putString(KEY_STATE, cstate.toString());
            params.putString(KEY_MESSAGE, message);

            sendEvent(ON_CALL_STATE_CHANGED, params);
        }

        @Override
        public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message) {
            WritableMap params = Arguments.createMap();

            params.putString(KEY_STATE, cstate.toString());

            sendEvent(ON_REGISTRATION_STATE_CHANGED, params);
        }
    }
}
