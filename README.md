# VolleyAir

##简介
VolleyAir是在著名的谷歌开源的网络框架Volley的基础上进行的二次封装，并吸取了VolleyPlus的一些封装经验，使之能更有效的在复杂的数据处理逻辑层进行网络请求，使逻辑层的代码更加清爽简洁。
之所以选择Volley进行封装，是因为Volley是一款极为高效的网络请求框架，并且开发自谷歌的Android团队。在其基础上封装适配过后，将更为有利于我们的应用开发。

##使用方法

1.根据自己的业务需求，，在NetworkMoudle类中自定义请求地址以及参数


    public TaskHandle arrangeGetNewsList(String requestTag, String cty, String category, int page, int row, String title) {
        HttpRequest request = new HttpRequest(API_URL + "news/getNews");
        request.addParameter("cty", cty);
        request.addParameter("category", category);
        request.addParameter("page", Integer.toString(page));
        request.addParameter("row", Integer.toString(row));
		request.addParameter("title", title);
        request.setRequestTag(requestTag);
        return center.arrange(request, volleyPostString);
    }

2.根据自己的业务需求，在DataMoudle类中自定义如何解析接收到的网络数据
	
	public IDData parseNewsList() throws HttpProcessException {
        try {
            JSONObject json = tryExtra(JSONObject.class);
            IDData data = new IDData(json.optInt("count", -1), null);
            JSONArray array = json.optJSONArray("data");

            ArrayList<NewsListItem> list = new ArrayList<NewsListItem>(array == null ? 0 : array.length());
            data.data = list;
            if (null != array) {
                NewsListItem item;
                for (int i = 0; i < array.length(); ++i) {
                    json = array.getJSONObject(i);
                    item = new NewsListItem();
                    item.id = json.optString("id");
                    item.title = json.optString("title");
                    item.create_time = json.optString("create_time");
                    item.img = json.optString("img");
                    item.category_name = json.optString("category_name");
                    item.city_name = json.optString("city_name");
                    item.description = json.optString("description");
                    list.add(item);
                }
            }
            extra = data;
            return data;
        } catch (Exception e) {
            throw badResponseException(e);
        }
    }

3.让View层（Activity、Fragment等）实现网络数据接收器接口

	public class MainActivity extends AppCompatActivity implements Receiver<DataModule>



4.在View层（Activity、Fragment等）中进行请求，及结果处理


    	/**
         * 可以并发多个网络请求，通过每个请求Task的Id在view层的回调接口中处理请求结果
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



##感谢
* Volley： [https://android.googlesource.com/platform/frameworks/volley](https://android.googlesource.com/platform/frameworks/volley)
* VolleyPlus： [https://github.com/DWorkS/VolleyPlus](https://github.com/DWorkS/VolleyPlus)

##关于作者
* GitHub项目地址：[https://github.com/Jasonchenlijian/VolleyAir](https://github.com/Jasonchenlijian/VolleyAir)
* Email： 1033526540@qq.com , jasonchenlijian@outlook.com
* 欢迎关注我的博客给我的写的文字提提意见： [http://www.jianshu.com/users/03be02d3e424/latest_articles](http://www.jianshu.com/users/03be02d3e424/latest_articles)
