package com.lsyy.qabwgpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.joysuch.sdk.IndoorLocateListener;
import com.joysuch.sdk.locate.JSPosition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends Activity implements IndoorLocateListener, OkHttpUtil.NetCall {
    public static String TAG = "SMALLPIG";
    @BindView(R.id.bt_title_back)
    LinearLayout btTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.bt_daolan_showContent)
    ImageView btDaolanShowContent;
    @BindView(R.id.bt_daolan_dismissContent)
    ImageView btDaolanDismissContent;
    @BindView(R.id.im_daolan_pic)
    ImageView imDaolanPic;
    @BindView(R.id.tv_daolan_str)
    TextView tvDaolanStr;
    @BindView(R.id.layout_daolan_flame)
    FrameLayout layoutDaolanFlame;
    //    @BindView(R.id.bt_daolan_play)
//    ImageView btDaolanPlay;
//    @BindView(R.id.tv_daolan_seekbar)
//    SeekBar tvDaolanSeekbar;
//    @BindView(R.id.tv_daolan_playtime)
//    TextView tvDaolanPlaytime;
//    @BindView(R.id.tv_daolan_alltime)
    TextView tvDaolanAlltime;
    @BindView(R.id.layout_daolan_small)
    LinearLayout layoutDaolanSmall;
    @BindView(R.id.layout_daolan_dig)
    LinearLayout layoutDaolanDig;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.mine_weizhi)
    ImageView im_Weizhi;
    @BindView(R.id.tv_daolan_seekbar)
    SeekBar tvDaolanSeekbar;
    @BindView(R.id.tv_daolan_playtime)
    TextView tvDaolanPlaytime;
//    @BindView(R.id.tv_daolan_alltime)
//    TextView tvDaolanAlltime;
    //商户id
    private String ID;
    //产品id
    private String product_id;
    //产品详情
    private DaolanDetailsBean bean;
    //判断获取的数据是否为详细数据
    private boolean isDetails = false;
    private double x=50;
    private double y=50;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 200:
                    String response = (String) msg.obj;
                    try {
                        if (isDetails) {
                            isDetails = false;
                            Gson gson = new Gson();
                            bean = gson.fromJson(response, DaolanDetailsBean.class);
                        } else {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null && jsonObject.getString("status").equals("200")) {
                                product_id = 1 + "";
                                isDetails = true;
                                getData(ID + "/" + product_id);
                            } else {
                                Toast.makeText(getApplicationContext(), "数据请求失败，请稍后重试", Toast.LENGTH_LONG).show();
                                MainActivity.this.finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 404:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bluetoothRadiusUtils.getInstance(getApplicationContext()).getRadius(this);
//        Intent intent=getIntent();
//        String id=intent.getStringExtra("ID");
        AnimationDrawable animationDrawable = (AnimationDrawable) image.getDrawable();
        animationDrawable.start();
        ID = 2452 + "";
        getData(ID);
        initMedia();
    }

    /**
     * 初始化音频播放器
     */
    private void initMedia() {
    }

    /**
     * 获取导览数据
     *
     * @param id
     */
    private void getData(String id) {
        if (!isDetails) {
            OkHttpUtil.getInstance().getDataAsyn(Contents.HOST + Contents.GET_DALAN + id, this);
        } else {
            OkHttpUtil.getInstance().getDataAsyn(Contents.HOST + Contents.GET_DALAN_DETAILS + id, this);
        }
    }

    @Override
    public void onReceivePosition(JSPosition jsPosition) {
        String result = "User ID:" + jsPosition.getUserID() + "\r\n";
        result += "Error code:" + String.valueOf(jsPosition.getErrorCode()) + "\r\n";
        result += "Build ID:" + String.valueOf(jsPosition.getBuildID()) + "\r\n";
        result += "Floor ID:" + String.valueOf(jsPosition.getFloorID()) + "\r\n";
        result += "X:" + String.valueOf(jsPosition.getxMeters()) + "\r\n";
        result += "Y:" + String.valueOf(jsPosition.getyMeters()) + "\r\n";
        changeView(jsPosition.getxMeters(), jsPosition.getyMeters());

    }

    private void changeView(double getxMeters, double getyMeters) {
        int width = layoutDaolanFlame.getWidth();
        int height = layoutDaolanFlame.getHeight();
        FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) im_Weizhi.getLayoutParams();
        layoutParams.leftMargin= (int) (width/91*x);
        layoutParams.topMargin= (int) (height/106*y);
        x+=10;
        y+=10;
        im_Weizhi.requestLayout();
    }

    @OnClick({R.id.bt_title_back, R.id.bt_daolan_showContent, R.id.bt_daolan_dismissContent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_title_back:
                this.finish();
                break;
            case R.id.bt_daolan_showContent:
                AnimationUtil.startAlphaAnima(layoutDaolanSmall, 1, 0);
                AnimationUtil.startAlphaAnima(layoutDaolanDig, 0, 1);
                break;
            case R.id.bt_daolan_dismissContent:
                AnimationUtil.startAlphaAnima(layoutDaolanSmall, 0, 1);
                AnimationUtil.startAlphaAnima(layoutDaolanDig, 1, 0);
                break;
//            case R.id.bt_daolan_play:
//                break;
        }
    }


    @Override
    public void success(Call call, String response) throws IOException {
        Message msg = new Message();
        msg.what = 200;
        msg.obj = response;
        handler.sendMessage(msg);
    }

    @Override
    public void failed(Call call, IOException e) {
        handler.sendEmptyMessage(404);
    }

}
