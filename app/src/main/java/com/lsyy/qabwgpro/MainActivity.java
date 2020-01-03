package com.lsyy.qabwgpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
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

public class MainActivity extends Activity implements IndoorLocateListener, OkHttpUtil.NetCall, MediaPlayerHolder.PlaybackInfoListener, SeekBar.OnSeekBarChangeListener {
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
    @BindView(R.id.im_background)
    ImageView imBackground;
    //语言分类
    private int LanguageType = 1;
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
    private double Staticx = 0;
    private double Staticy = 0;
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
                            addSite(bean);
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
                case 202:
                    int position= (int) msg.obj;
                    tvDaolanSeekbar.setProgress(position);
                    if (position==0){
                        tvDaolanPlaytime.setText("00 : 00");
                    }else{
                        String ttt=getTime(position);
                        tvDaolanPlaytime.setText(ttt);
                    }
                    break;
                case 404:
                    break;
            }
        }
    };

    private void addSite(DaolanDetailsBean bean) {
        ViewGroup group = findViewById(R.id.layout_daolan_flame);
        for (DaolanDetailsBean.DataBean data : bean.getData()) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackground(getResources().getDrawable(R.mipmap.red));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.width =20;
            params.height =20;
            int width = layoutDaolanFlame.getWidth();
            int height = layoutDaolanFlame.getHeight();
//            float xx = (float) ((width / 91) * 91);
//            float yy = (float) ((height / 106) *106);
            float xx = (float) ((width / 91) * Double.parseDouble(!data.getChildLatitude().equals("") ? data.getChildLongitude() : 0 + ""));
            float yy = (float) ((height / 106) * Double.parseDouble(!data.getChildLatitude().equals("") ? data.getChildLatitude() : 0 + ""));
           if (xx==0||yy==0){break;}
            imageView.setTranslationX(xx);
            imageView.setTranslationY(yy);
            imageView.setLayoutParams(params);
            group.addView(imageView);
        }
    }

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
        tvDaolanSeekbar.setOnSeekBarChangeListener(this);
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
        if (checkAddress(jsPosition.getxMeters(), jsPosition.getyMeters())) {
            changeSite();
        }
    }

    /**
     * 更换降解点
     */
    private void changeSite() {
        if (mediaPlayerIngHolder.isPlaying()) {
            mediaPlayerIngHolder.release();
        }
        switch (LanguageType) {
            case 1:
                String url="http://www.guolaiwan.net/file"+dataBean.getChineseBoy();
                mediaPlayerIngHolder.loadMedia(url);
                break;
            case 2:
                mediaPlayerIngHolder.loadMedia("http://www.guolaiwan.net/file"+dataBean.getChineseGirl());
                break;
            case 3:
                mediaPlayerIngHolder.loadMedia("http://www.guolaiwan.net/file"+dataBean.getEnglishBoy());
                break;
            case 4:
                mediaPlayerIngHolder.loadMedia("http://www.guolaiwan.net/file"+dataBean.getEnglishGirl());
                break;
            default:
                break;
        }
    }

    /**
     * 监听是否更换音频
     *
     * @return
     */
    private Boolean checkAddress(double x, double y) {
        //判断移动距离是否大于0.5m
        if (AddressUtils.distance(x, y, Staticx, Staticy) > 0.5) {
            //分别判断距离是否在范围内
            for (DaolanDetailsBean.DataBean bean : bean.getData()) {
                Double childX = Double.parseDouble(bean.getChildLatitude());
                Double childY = Double.parseDouble(bean.getChildLongitude());
                if (AddressUtils.distance(x, y, childX, childY) > 0.5) {
                    //判断角度是否合适
                    int range = AddressUtils.disrange(x, y, Staticx, Staticy);
                    if (range > Integer.parseInt(bean.getStartAngle()) && range < Integer.parseInt(bean.getEndAngle())) {
                        dataBean = bean;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更改定位显示
     *
     * @param getxMeters
     * @param getyMeters
     */
    private void changeView(double getxMeters, double getyMeters) {
        int width = layoutDaolanFlame.getWidth();
        int height = layoutDaolanFlame.getHeight();
        int xx = (int) (width / 91 * getxMeters);
        int yy = (int) (height / 106 * getyMeters);
        im_Weizhi.setTranslationX(xx);
        im_Weizhi.setTranslationY(yy);
    }

    @OnClick({R.id.bt_title_back, R.id.bt_daolan_showContent,R.id.bt_daolan_play, R.id.bt_daolan_dismissContent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_title_back:
                this.finish();
                break;
            case R.id.bt_daolan_showContent:
                if (dataBean == null) {
                    Toast.makeText(getApplicationContext(), "还未到讲解点哦！", Toast.LENGTH_SHORT).show();
                } else {
                    Glide.with(getApplicationContext())
                            .load(dataBean.getChildPic())
                            .into(imDaolanPic);
                    tvDaolanStr.setText(dataBean.getChineseContent());
                    AnimationUtil.startAlphaAnima(layoutDaolanSmall, 1, 0);
                    AnimationUtil.startAlphaAnima(layoutDaolanDig, 0, 1);
                }
                break;
            case R.id.bt_daolan_dismissContent:
                AnimationUtil.startAlphaAnima(layoutDaolanSmall, 0, 1);
                AnimationUtil.startAlphaAnima(layoutDaolanDig, 1, 0);
                break;
            case R.id.bt_daolan_play:
                if (mediaPlayerIngHolder.isPlaying()){
                    mediaPlayerIngHolder.pause();
                }else{
                    if (dataBean==null){
                        Toast.makeText(getApplicationContext(), "还未到讲解点哦！", Toast.LENGTH_SHORT).show();
                    }else{
                        mediaPlayerIngHolder.play();
                    }

                }
                break;
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

    /**
     * 时间转换
     * @param duration
     * @return
     */
    private String getTime(int duration) {
        int time=duration/1000;
        int min=time/60;
        int second=time%60;
        if (min>10){
            if (second>=10){
                return min+" : "+second;
            }else{
                return min+" : 0"+second;
            }
        }else if(min>1&&min<10){
            if (second>=10){
                return "0"+min+" : "+second;
            }else{
                return "0"+min+" : 0"+second;
            }
        }else{
            if (second>=10){
                return "00"+" : "+second;
            }else{
                return "00"+" : 0"+second;
            }
        }
    }
    @Override
    public void onDurationChanged(int duration) {
        tvDaolanAlltime.setText(getTime(duration));
        tvDaolanPlaytime.setText("00 : 00");
        mediaPlayerIngHolder.play();
        btDaolanPlay.setBackground(getResources().getDrawable(R.mipmap.pause));
        tvDaolanSeekbar.setMax(duration);
    }



    @Override
    public void onPositionChanged(int position) {
        Message msg=new Message();
        msg.what=202;
        msg.obj=position;
        handler.sendMessage(msg);

    }
//    public static int PLAYSTATUS0=0;//正在播放
//    public static int PLAYSTATUS1=1;//暂停播放
//    public static int PLAYSTATUS2=2;//重置
//    public static int PLAYSTATUS3=3;//播放完成
//    public static int PLAYSTATUS4=4;//媒体流装载完成
//    public static int PLAYSTATUS5=5;//媒体流加载中
//    public static int PLAYSTATUSD1=-1;//错误
    @Override
    public void onStateChanged(int state) {
        switch (state){
            case 0: btDaolanPlay.setBackground(getResources().getDrawable(R.mipmap.pause)); break;
            case 1: btDaolanPlay.setBackground(getResources().getDrawable(R.mipmap.play));break;
            case 2: btDaolanPlay.setBackground(getResources().getDrawable(R.mipmap.play));break;
            case 3: btDaolanPlay.setBackground(getResources().getDrawable(R.mipmap.play));break;
        }
    }

    @Override
    public void onPlaybackCompleted() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayerIngHolder.seekTo(seekBar.getProgress());
    }
}
