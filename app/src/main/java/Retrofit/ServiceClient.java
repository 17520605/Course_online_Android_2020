package Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceClient {
    private static Retrofit instance;
    public static Retrofit getInstance() {
        if(instance==null) {
            instance = new Retrofit.Builder()
                    .baseUrl("http://149.28.24.98:9000/") // API base url
                    .addConverterFactory(GsonConverterFactory.create()) // Factory phụ thuộc vào format trả về
                    .build();
        }
        return instance;
    }
}
