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
import com.joysuch.sdk.locate.JSLocateManager;
import com.joysuch.sdk.locate.JSPosition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends Activity implements IndoorLocateListener, OkHttpUtil.NetCall, MediaPlayerHolder.PlaybackInfoListener {
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
    @BindView(R.id.layout_daolan_small)
    LinearLayout layoutDaolanSmall;
    @BindView(R.id.layout_daolan_dig)
    LinearLayout layoutDaolanDig;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.mine_weizhi)
    ImageView im_Weizhi;
    //播放器
    @BindView(R.id.bt_daolan_play)
    ImageView btDaolanPlay;
    @BindView(R.id.tv_daolan_seekbar)
    SeekBar tvDaolanSeekbar;
    @BindView(R.id.tv_daolan_playtime)
    TextView tvDaolanPlaytime;
    @BindView(R.id.tv_daolan_alltime)
    TextView tvDaolanAlltime;
    //语言分类
    private int LanguageType =1;
    //商户id
    private String ID;
    //产品id
    private String product_id;
    //产品详情
    private DaolanDetailsBean bean;
    //判断获取的数据是否为详细数据
    private boolean isDetails = false;
    //音频播放
    private MediaPlayerHolder mediaPlayerIngHolder;
    //当前所在将节点数据
    private DaolanDetailsBean.DataBean dataBean;
    private double Staticx=0;
    private double Staticy=0;
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
        bluetoothRadiusUtils.getInstance(MainActivity.this).getRadius(this);
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
        mediaPlayerIngHolder = new MediaPlayerHolder();
        mediaPlayerIngHolder.setmPlaybackInfoListener(this);//设置监听
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
        if (checkAddress(jsPosition.getxMeters(), jsPosition.getyMeters())){
            changeSite();
        }
    }

    /**
     * 更换降解点
     */
    private void changeSite() {
        if (mediaPlayerIngHolder.isPlaying()){
            mediaPlayerIngHolder.release();
        }
        switch (LanguageType){
            case 1: mediaPlayerIngHolder.loadMedia(dataBean.getChineseBoy());break;
            case 2:mediaPlayerIngHolder.loadMedia(dataBean.getChineseGirl());break;
            case 3:mediaPlayerIngHolder.loadMedia(dataBean.getEnglishBoy());break;
            case 4:mediaPlayerIngHolder.loadMedia(dataBean.getEnglishGirl());break;
            default:break;
        }
    }

    /**
     * 监听是否更换音频
     * @return
     */
    private Boolean checkAddress(double x, double y ) {
        //判断移动距离是否大于0.5m
        if (AddressUtils.distance(x,y,Staticx,Staticy)>0.5){
            //分别判断距离是否在范围内
            for (DaolanDetailsBean.DataBean bean:bean.getData()){
                Double childX=Double.parseDouble(bean.getChildLatitude());
                Double childY=Double.parseDouble(bean.getChildLongitude());
                if (AddressUtils.distance(x,y,childX,childY)>0.5){
                    //判断角度是否合适
                    int range=AddressUtils.disrange(x,y,Staticx,Staticy);
                    if (range>(int)bean.getStartAngle()&&range<(int)bean.getEndAngle()){
                        dataBean=bean;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更改定位显示
     * @param getxMeters
     * @param getyMeters
     */
    private void changeView(double getxMeters, double getyMeters) {
        int width = layoutDaolanFlame.getWidth();
        int height = layoutDaolanFlame.getHeight();
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) im_Weizhi.getLayoutParams();
        int xx=(int)(width/91*getxMeters);
        int yy=(int)(height/106*getyMeters);
        im_Weizhi.setTranslationX(xx);
        im_Weizhi.setTranslationY(yy);
    }

    @OnClick({R.id.bt_title_back, R.id.bt_daolan_showContent, R.id.bt_daolan_dismissContent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_title_back:
                this.finish();
                break;
            case R.id.bt_daolan_showContent:
                if (dataBean==null){
                    Toast.makeText(getApplicationContext(),"还未到讲解点哦！",Toast.LENGTH_SHORT).show();
                }else{
//                    imDaolanPic.setBackground();
                    tvDaolanStr.setText(dataBean.getChineseContent());
                    AnimationUtil.startAlphaAnima(layoutDaolanSmall, 1, 0);
                    AnimationUtil.startAlphaAnima(layoutDaolanDig, 0, 1);
                }
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

    @Override
    public void onDurationChanged(int duration) {

    }

    @Override
    public void onPositionChanged(int position) {

    }

    @Override
    public void onStateChanged(int state) {

    }

    @Override
    public void onPlaybackCompleted() {

    }
}
