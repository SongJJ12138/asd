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
        //离线使用
        copyAssetsToDst(context,"LocateDatas","Joysuch/LocateDatas");
        JSLocateManager.getInstance().setOfflineMode();//定位数据内置APP情况
        JSLocateManager.getInstance().setRootFolderName("Joysuch");//自定义数据存储文件夹
        jsLocateManager=JSLocateManager.getInstance();
        jsLocateManager.init(context);
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
        jsLocateManager.setOnIndoorLocateListener(indoorLocateListener);
        jsLocateManager.setLocateTimesSecond(2);
        jsLocateManager.start();
    }

    /**
     * 保存信息
     * @param context
     * @param srcPath
     * @param dstPath
     * @return
     */
    public static boolean copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG,e.getMessage());
            return false;
        }
    }
}
