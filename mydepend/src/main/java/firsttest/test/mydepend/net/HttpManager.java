package firsttest.test.mydepend.net;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import firsttest.test.mydepend.api.MyApi;
import firsttest.test.mydepend.utils.SpUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static HttpManager instance;

    public  static HttpManager getInstance(){
        if(instance == null){
            synchronized(HttpManager.class){
                if(instance == null){
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    private MyApi serviceApi;


    private Retrofit getRetrofit(String url){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    private OkHttpClient getOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new HeaderInterceptor())
                .build();
        return okHttpClient;
    }

    static class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            ResponseBody responseBody = response.peekBody(Integer.MAX_VALUE);
            Log.i("responseBody",responseBody.string());
            return response;
        }
    }

    /**
     * 拦截的头处理
     */
    static class HeaderInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization","APPCODE 964e16aa1ae944e9828e87b8b9fbd30a")
                    .addHeader("X-Nideshop-Token", SpUtils.getInstance().getString("token"))
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * ServiceApi
     * @return
     */
    public MyApi getService(){
        if(serviceApi == null){
            serviceApi = getRetrofit(MyApi.BASE_URL).create(MyApi.class);
        }
        return serviceApi;
    }

}
