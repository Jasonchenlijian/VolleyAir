# VolleyAir

##简介
VolleyAir是在著名的谷歌开源的网络框架Volley的基础上进行的二次封装，使之能更有效的在复杂的数据处理逻辑层进行网络请求，使View层的代码更加清爽简洁。
之所以选择Volley进行封装，是因为Volley是一款极为高效的网络请求框架，并且开发自谷歌的Android团队。在其基础上封装适配过后，将更为有利于我们的应用开发。

##使用方法
1.让View层（Activity、Fragment等）实现网络数据接收器接口

	public class MainActivity extends AppCompatActivity implements Receiver<String>



2.在View层（Activity、Fragment等）中进行请求，及结果处理

    	/**
         * 可以并发多个网络请求，通过每个请求Task的Id在view层的回调接口中处理请求结果
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



##感谢
* Volley： [https://android.googlesource.com/platform/frameworks/volley](https://android.googlesource.com/platform/frameworks/volley)
* VolleyPlus： [https://github.com/DWorkS/VolleyPlus](https://github.com/DWorkS/VolleyPlus)

##关于作者
* GitHub项目地址：[https://github.com/Jasonchenlijian](https://github.com/Jasonchenlijian/VolleyAir)
* Email： 1033526540@qq.com , jasonchenlijian@outlook.com
* 欢迎给我提意见： [http://www.jianshu.com/users/03be02d3e424/latest_articles](http://www.jianshu.com/users/03be02d3e424/latest_articles)
