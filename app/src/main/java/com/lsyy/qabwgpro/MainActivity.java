package com.lsyy.qabwgpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;

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
import okhttp3.Response;

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
    @BindView(R.id.bt_daolan_play)
    ImageView btDaolanPlay;
    @BindView(R.id.tv_daolan_seekbar)
    SeekBar tvDaolanSeekbar;
    @BindView(R.id.tv_daolan_playtime)
    TextView tvDaolanPlaytime;
    @BindView(R.id.tv_daolan_alltime)
    TextView tvDaolanAlltime;
    @BindView(R.id.layout_daolan_small)
    LinearLayout layoutDaolanSmall;
    @BindView(R.id.layout_daolan_dig)
    LinearLayout layoutDaolanDig;
    //商户id
    private String ID;
    //产品id
    private String product_id;
    //产品详情
    private DaolanDetailsBean bean;
    //判断获取的数据是否为详细数据
    private boolean isDetails=false;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 200:
                    Response response= (Response) msg.obj;
                    try {
                        if (isDetails){
                            isDetails=false;
                            Gson gson=new Gson();
                            bean=gson.fromJson(response.body().string(),DaolanDetailsBean.class);
                        }else{
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            if (jsonObject!=null&&jsonObject.getString("status").equals("200")){
                                product_id=1+"";
                                isDetails=true;
                                getData(ID+"/"+product_id);
                            }else{
                                Toast.makeText(getApplicationContext(),"数据请求失败，请稍后重试",Toast.LENGTH_LONG).show();
                                MainActivity.this.finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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
        ID=2452+"";
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
     * @param id
     */
    private void getData(String id) {
        if (!isDetails){
            OkHttpUtil.getInstance().getDataAsyn(Contents.HOST+Contents.GET_DALAN+id,this);
        }else{
            OkHttpUtil.getInstance().getDataAsyn(Contents.HOST+Contents.GET_DALAN_DETAILS+id,this);
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
        result += "Timestamp:" + String.valueOf(jsPosition.getTimeStampMillisecond()) + "\r\n";
    }

    @OnClick({R.id.bt_title_back, R.id.bt_daolan_showContent, R.id.bt_daolan_dismissContent, R.id.bt_daolan_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_title_back:
                this.finish();
                break;
            case R.id.bt_daolan_showContent:
                AnimationUtil.startAlphaAnima(layoutDaolanSmall,1,0);
                AnimationUtil.startAlphaAnima(layoutDaolanDig,0,1);
                break;
            case R.id.bt_daolan_dismissContent:
                AnimationUtil.startAlphaAnima(layoutDaolanSmall,0,1);
                AnimationUtil.startAlphaAnima(layoutDaolanDig,1,0);
                break;
            case R.id.bt_daolan_play:
                break;
        }
    }

    @Override
    public void success(Call call, Response response) throws IOException {
            Message msg=new Message();
            msg.what=200;
            msg.obj=response;
            handler.sendMessage(msg);
    }

    @Override
    public void failed(Call call, IOException e) {
        handler.sendEmptyMessage(404);
    }
}
