package com.lsyy.qabwgpro;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.joysuch.sdk.IndoorLocateListener;
import com.joysuch.sdk.locate.JSLocateManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 蓝牙定位封装
 */
public class bluetoothRadiusUtils {
    public final static String TAG="bluetooth";
    private static bluetoothRadiusUtils bluetoothRadiusUtils;
    private  JSLocateManager jsLocateManager;

    /**
     * 初始化
     */
    private bluetoothRadiusUtils(Context context) {
        JSLocateManager.getInstance().init(context);
    }
    /**
     * 单例封装
     * @return
     */
    public static bluetoothRadiusUtils getInstance(Context context) {
        if (null == bluetoothRadiusUtils) {
            synchronized (bluetoothRadiusUtils.class) {
                if(null == bluetoothRadiusUtils) {
                    bluetoothRadiusUtils = new bluetoothRadiusUtils(context);
                }
            }
        }
        return bluetoothRadiusUtils;
    }
    public void getRadius(IndoorLocateListener indoorLocateListener){
        JSLocateManager.getInstance().setOnIndoorLocateListener(indoorLocateListener);
        JSLocateManager.getInstance().start();
    }
}
