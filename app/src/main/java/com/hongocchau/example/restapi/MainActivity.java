package com.hongocchau.example.restapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hongocchau.example.restapi.model.WeatherModel;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    //http://api.openweathermap.org/data/2.5/weather?q=Singapore&appid=2d3414da8018911d61c03f2a9d0aaee5
    public final static String API_KEY = "2d3414da8018911d61c03f2a9d0aaee5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit openWeatherApi = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        OpenWeatherApiInterface openWeatherApiInterface = openWeatherApi.create(OpenWeatherApiInterface.class);
        Call<WeatherModel> request = openWeatherApiInterface.getWeather("Singapore",API_KEY);
        request.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                Log.d("TEMPERATURE",String.valueOf(response.body().getMain().getTemp()));
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {

            }
        });
    }


    public interface OpenWeatherApiInterface {
        @GET("weather")
        Call<WeatherModel> getWeather(@Query("q") String city, @Query("appid") String apiKey);
    }
}
