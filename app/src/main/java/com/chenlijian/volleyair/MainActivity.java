package com.chenlijian.volleyair;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.volley.air.Receiver;
import com.volley.air.TaskHandle;

public class MainActivity extends AppCompatActivity implements Receiver<DataModule>{

    private NetworkModule networkModule;

    private Button button;
    private TextView txt_1, txt_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkModule = ((ApplicationController)getApplication()).getNetworkModule();
        button = (Button) findViewById(R.id.begin_volley);
        txt_1 = (TextView) findViewById(R.id.result_one);
        txt_2 = (TextView) findViewById(R.id.result_two);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginVolley();
            }
        });
    }




    /**
     * 并发多个网络请求，给每个请求setId，通过Id在view层的回调接口中处理请求结果
     */
    private void beginVolley(){

        TaskHandle handle_0 = networkModule.arrangeGetNewsList("arrangeGetNewsList", null, null, 1, 10, null);
        handle_0.setId(0);
        handle_0.setReceiver(this);
        handle_0.pullTrigger();

        TaskHandle handle_1 = networkModule.arrangeUploadImg("arrangeUploadImg", "path");
        handle_1.setId(1);
        handle_1.setReceiver(this);
        handle_1.pullTrigger();
    }


    /**
     * 网络请求成功，处理结果
     * @param handle
     * @param result
     */
    @Override
    public void onSucess(TaskHandle handle, DataModule result) {
        switch (handle.id()){
            case 0:
                if(result.code() == DataModule.CodeSucess){

                }
                txt_1.setText(result.toString());
                break;

            case 1:
                if(result.code() == DataModule.CodeSucess){

                }
                txt_2.setText(result.toString());
                break;
        }
    }

    /**
     * 网络请求异常，处理结果
     * @param handle
     * @param error
     */
    @Override
    public void onError(TaskHandle handle, Throwable error) {
        switch (handle.id()){
            case 0:
                break;

            case 1:
                break;
        }
    }


}
