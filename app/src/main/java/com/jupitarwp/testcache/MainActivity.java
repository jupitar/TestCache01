package com.jupitarwp.testcache;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.text1)
   TextView textView1;
    @InjectView(R.id.text2)
    TextView textView2;
    @InjectView(R.id.text3)
    TextView textView3;
    @InjectView(R.id.text4)
     TextView textView4;
   private OkHttpClient okHttpClient;
    private String path= Environment.getExternalStorageDirectory().getPath()+File.separator+"cache_xx";
    private File file=new File(path);
    private long cacheSize=1024*1024;//缓存1m
    private String TEXT_URL="http://192.168.1.101:8080/testCache.txt";
    //缓存60秒
    private int cache_maxAge=60;
    int []ids={R.id.text1,R.id.text2,R.id.text3,R.id.text4};
    String responseBody=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
       initData();
    }


    //获取网络数据
    private void initData() {
        //获取OkHttpClient实例
        okHttpClient = OkHttpUtils.getInstance(file, cacheSize);
       getData();

    }

    private void getData() {

        try {
            final Request request=new  Request.Builder().cacheControl(new CacheControl.Builder().maxAge(cache_maxAge, TimeUnit.SECONDS).build())
                    .url(TEXT_URL).build();

            new Thread(new Runnable(){
                @Override
                public void run() {

                    try {
                        final Response response=okHttpClient.newCall(request).execute();
                        if(!response.isSuccessful()){
                            updateUI(3,response);
                        }
                        responseBody=response.body().string();
                        updateUI(0,response);
                        updateUI(1,response);
                        updateUI(2,response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新UI
    private void updateUI(final int id,final Response response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
             switch(id){
                 case 0:
                     textView1.setText("数据为："+responseBody);
                     break;
                 case 1:
                     textView2.setText("缓存数据为："+response.cacheResponse().toString());

                 case 2:
                     textView3.setText("网络获取："+response.networkResponse());
                     break;
                 case 3:
                     textView4.setText("请求结果为："+response);
                     break;




             }

            }
        });
    }


}
