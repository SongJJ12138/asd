package com.lsyy.qabwgpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.joysuch.sdk.IndoorLocateListener;
import com.joysuch.sdk.locate.JSPosition;
import com.shehuan.niv.NiceImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends Activity implements IndoorLocateListener, OkHttpUtil.NetCall, MediaPlayerHolder.PlaybackInfoListener, SeekBar.OnSeekBarChangeListener {
    public static String TAG = "SMALLPIG";
    @BindView(R.id.img_background)
    ImageView imgBackground;
    @BindView(R.id.layout_daolan_flame)
    FrameLayout layoutDaolanFlame;
    @BindView(R.id.bt_daolan_showContent)
    TextView btDaolanShowContent;
    @BindView(R.id.bt_play)
    NiceImageView btPlay;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.tv_playtime)
    TextView tvPlaytime;
    @BindView(R.id.tv_alltime)
    TextView tvAlltime;
    //语言分类
    private int LanguageType = 1;
    //商户id
    private String ID;
    //产品id
    private Long product_id;
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
                                product_id = 1L;
                                isDetails = true;
                                getData("2452/" + product_id);
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
                    int position = (int) msg.obj;
                    if (position == 0) {
                        tvPlaytime.setText("00 : 00");
                    } else {
                        String ttt = getTime(position);
                        tvPlaytime.setText(ttt);
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
            imageView.setBackground(getResources().getDrawable(R.mipmap.icon_jjd));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.width = 30;
            params.height = 30;
            int width = imgBackground.getWidth();
            int height = imgBackground.getHeight();
            float xx = (float) ((width / 56) * (Double.parseDouble(!data.getChildLatitude().equals("") ? data.getChildLongitude() : 0 + "") - 22.9386));
            float yy = (float) ((height / 66.03) * (Double.parseDouble(!data.getChildLatitude().equals("") ? data.getChildLatitude() : 0 + "") - 10.9398));
            if (xx == 0 || yy == 0) {
                break;
            }
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
        ViewTreeObserver vto = imgBackground.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams para;
                para = imgBackground.getLayoutParams();
                int width = imgBackground.getWidth();
                para.width = width;
                int Height = (int) (1.18 * width);
                para.height = Height;
                imgBackground.setLayoutParams(para);
            }
        });
//        getData(2452 + "");
        bluetoothRadiusUtils.getInstance(MainActivity.this).getRadius(this);
//        initMedia();
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
     * 初始化音频播放器
     */
    private void initMedia() {
        mediaPlayerIngHolder = new MediaPlayerHolder();
        mediaPlayerIngHolder.setmPlaybackInfoListener(this);//设置监听
    }

    /**
     * 总时长改变（变音频）
     *
     * @param duration
     */
    @Override
    public void onDurationChanged(int duration) {

    }

    /**
     * 当前播放时长改变（拖动）
     *
     * @param position
     */
    @Override
    public void onPositionChanged(int position) {
        Message msg = new Message();
        msg.what = 202;
        msg.obj = position;
        handler.sendMessage(msg);

    }

    /**
     * 播放状态改变
     *
     * @param state
     */
    @Override
    public void onStateChanged(int state) {
        switch (state) {

        }
    }

    /**
     * 播放完成回调
     */
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


    /**
     * 接受蓝牙定位回调
     *
     * @param jsPosition
     */
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
                String url = "http://www.guolaiwan.net/file" + dataBean.getChineseBoy();
                mediaPlayerIngHolder.loadMedia(url);
                break;
            case 2:
                mediaPlayerIngHolder.loadMedia("http://www.guolaiwan.net/file" + dataBean.getChineseGirl());
                break;
            case 3:
                mediaPlayerIngHolder.loadMedia("http://www.guolaiwan.net/file" + dataBean.getEnglishBoy());
                break;
            case 4:
                mediaPlayerIngHolder.loadMedia("http://www.guolaiwan.net/file" + dataBean.getEnglishGirl());
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
        ViewGroup group = findViewById(R.id.layout_daolan_flame);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setBackground(getResources().getDrawable(R.mipmap.asd));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.width = 30;
        params.height = 30;
        int width = imgBackground.getWidth();
        int height = imgBackground.getHeight();
        float xx = (float) ((width / 56) * getxMeters- 22.9386);
        float yy = (float) ((height / 66.03) * getyMeters- 10.9398);
        imageView.setTranslationX(xx);
        imageView.setTranslationY(yy);
        imageView.setLayoutParams(params);
        group.addView(imageView);
    }

    @OnClick({R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                this.finish();
                break;
        }
    }


    /**
     * 时间转换
     *
     * @param duration
     * @return
     */
    private String getTime(int duration) {
        int time = duration / 1000;
        int min = time / 60;
        int second = time % 60;
        if (min > 10) {
            if (second >= 10) {
                return min + " : " + second;
            } else {
                return min + " : 0" + second;
            }
        } else if (min > 1 && min < 10) {
            if (second >= 10) {
                return "0" + min + " : " + second;
            } else {
                return "0" + min + " : 0" + second;
            }
        } else {
            if (second >= 10) {
                return "00" + " : " + second;
            } else {
                return "00" + " : 0" + second;
            }
        }
    }

}