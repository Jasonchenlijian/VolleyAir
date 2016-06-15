package com.chenlijian.volleyair;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.volley.air.HttpRequest;
import com.volley.air.Receiver;
import com.volley.air.TaskHandle;
import com.volley.air.VolleyAir;

public class MainActivity extends AppCompatActivity implements Receiver<String>{


    private TextView txt_1, txt_2;

    private VolleyAir volleyAir = new VolleyAir(MyApplication.getInstance());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.begin_volley);
        txt_1 = (TextView) findViewById(R.id.result_one);
        txt_2 = (TextView) findViewById(R.id.result_two);

        assert button != null;
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

        HttpRequest request = new HttpRequest("http://op.juhe.cn/onebox/news/query?key=&q=%E6%99%AE%E4%BA%AC%E5%A4%B1%E8%B8%AA");  // api地址
        request.addParameter("dtype", "json");      // 参数1
        request.addParameter("key", "test");        // 参数2
        request.setRequestTag("news");              // 分配请求tag，必要时可用于取消该请求

        TaskHandle handle_0 = volleyAir.arrange(request);
        handle_0.setId(0);              // 分配Id，便于回调时候区分结果
        handle_0.setReceiver(this);     // 设置回调监听者
        handle_0.pullTrigger();         // 执行请求

    }


    /**
     * 网络请求成功，处理结果
     * @param handle
     * @param result
     */
    @Override
    public void onSuccess(TaskHandle handle, String result) {
        switch (handle.id()){
            case 0:
                txt_1.setText(result);
                break;

            case 1:
                txt_2.setText(result);
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
