package com.lsyy.qabwgpro;


import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.joysuch.sdk.IndoorLocateListener;
import com.joysuch.sdk.locate.JSLocateManager;
import com.joysuch.sdk.locate.JSPosition;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextActivity extends AppCompatActivity {
    public static String TAG = "SMALLPIG";
    @BindView(R.id.msg)
    TextView msg;
    private StringBuilder localStringBuilder;
    private StringBuilder paramAnonymousPosition;
    private StringBuilder localObject2;
    IndoorLocateListener indoorLocateListener = new IndoorLocateListener() {
        public void onReceivePosition(JSPosition paramAnonymousJSPosition) {
            Object localObject = new StringBuilder();
            ((StringBuilder) localObject).append("User ID:");
            ((StringBuilder) localObject).append(paramAnonymousJSPosition.getUserID());
            ((StringBuilder) localObject).append("\r\n");
            localObject = ((StringBuilder) localObject).toString();
            if (paramAnonymousJSPosition.getErrorCode() == 1000) {
                localStringBuilder = new StringBuilder();
                localStringBuilder.append((String) localObject);
                localStringBuilder.append("Error code:");
                localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getErrorCode()));
                localStringBuilder.append("定位失败\r\n");
                localObject = localStringBuilder.toString();
            } else if (paramAnonymousJSPosition.getErrorCode() == 2010) {
                localStringBuilder = new StringBuilder();
                localStringBuilder.append((String) localObject);
                localStringBuilder.append("Error code:");
                localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getErrorCode()));
                localStringBuilder.append("无效的包名\r\n");
                localObject = localStringBuilder.toString();
            } else if (paramAnonymousJSPosition.getErrorCode() == 4012) {
                localStringBuilder = new StringBuilder();
                localStringBuilder.append((String) localObject);
                localStringBuilder.append("Error code:");
                localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getErrorCode()));
                localStringBuilder.append("参与定位的有效数据为空\r\n");
                localObject = localStringBuilder.toString();
            } else {
                localStringBuilder = new StringBuilder();
                localStringBuilder.append((String) localObject);
                localStringBuilder.append("Error code:");
                localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getErrorCode()));
                localStringBuilder.append("未知错误\r\n");
                localObject = localStringBuilder.toString();
            }
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append((String) localObject);
            localStringBuilder.append("Build ID:");
            localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getBuildID()));
            localStringBuilder.append("\r\n");
            localObject = localStringBuilder.toString();
            localStringBuilder = new StringBuilder();
            localStringBuilder.append((String) localObject);
            localStringBuilder.append("Floor ID:");
            localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getFloorID()));
            localStringBuilder.append("\r\n");
            localObject = localStringBuilder.toString();
            localStringBuilder = new StringBuilder();
            localStringBuilder.append((String) localObject);
            localStringBuilder.append("X:");
            localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getxMeters()));
            localStringBuilder.append("\r\n");
            localObject = localStringBuilder.toString();
            localStringBuilder = new StringBuilder();
            localStringBuilder.append((String) localObject);
            localStringBuilder.append("Y:");
            localStringBuilder.append(String.valueOf(paramAnonymousJSPosition.getyMeters()));
            localStringBuilder.append("\r\n");
            localObject = localStringBuilder.toString();
            if (paramAnonymousJSPosition.getErrorCode() == 0) {
                TextActivity.this.locationgroupid = String.valueOf(paramAnonymousJSPosition.getFloorID());
                TextActivity.this.locationx = String.valueOf(paramAnonymousJSPosition.getxMeters());
                TextActivity.this.locationy = String.valueOf(paramAnonymousJSPosition.getyMeters());
                TextActivity.this.userid = paramAnonymousJSPosition.getUserID();
                paramAnonymousPosition = new StringBuilder();
                paramAnonymousPosition.append((String) localObject);
                paramAnonymousPosition.append("定位成功\r\n");
//                paramAnonymousJSPosition = paramAnonymousPosition.toString();
            } else {
                TextActivity.this.locationgroupid = "0";
                TextActivity.this.locationx = "0";
                TextActivity.this.locationy = "0";
                TextActivity.this.userid = "0";
                paramAnonymousPosition = new StringBuilder();
                paramAnonymousPosition.append((String) localObject);
                paramAnonymousPosition.append("定位失败\r\n");
//                paramAnonymousJSPosition = paramAnonymousPosition.toString();
            }
            TextActivity.this.msg.setText(paramAnonymousPosition.toString());
        }
    };
    public String locationgroupid = "0";
    public String locationx = "0";
    public String locationy = "0";
    public String userid = "0";


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.aaa);
        ButterKnife.bind(this);
        JSLocateManager.getInstance().init(this);
        JSLocateManager.getInstance().setOnIndoorLocateListener(this.indoorLocateListener);
        JSLocateManager.getInstance().start();
    }
}
