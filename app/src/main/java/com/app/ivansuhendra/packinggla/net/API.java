package com.app.ivansuhendra.packinggla.net;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.app.ivansuhendra.packinggla.model.User;
import com.app.ivansuhendra.packinggla.utils.DateDeserializer;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import br.com.kots.mob.complex.preferences.ComplexPreferences;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Converter<ResponseBody, BadRequest> ERROR_CONVERTER;
    private static boolean sessionError = false;

    // Update this method to include basic authentication
    public static APIService service() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(getBasicAuthInterceptor()) // Add basic authentication interceptor
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.5.236/packing-app/public" + "/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(APIService.class);
    }

    // Update this method to create and return basic authentication interceptor
    private static Interceptor getBasicAuthInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder newRequestBuilder = originalRequest.newBuilder()
                        .header("Authorization", getBasicAuthToken());

                Request newRequest = newRequestBuilder.build();
                return chain.proceed(newRequest);
            }
        };
    }

    // Update this method to return the basic authentication token
    private static String getBasicAuthToken() {
        return Credentials.basic("admin@ghimli.com", "ghimli@2024");
    }

    static Converter<ResponseBody, BadRequest> getErrorConverter() {
        if (ERROR_CONVERTER == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.5.236/packing-app/public" + "/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERROR_CONVERTER = retrofit.responseBodyConverter(
                    BadRequest.class, new Annotation[0]);
        }
        return ERROR_CONVERTER;
    }

    public static void setSessionError(boolean sessionError) {
        API.sessionError = sessionError;
    }

    public static void saveCredentials(Context context, User user) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, GlobalVars.PREF_USER, MODE_PRIVATE);
        complexPreferences.putObject(GlobalVars.PREF_USER_KEY, user);
        complexPreferences.commit();
    }

    public static User currentUser(Context context) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, GlobalVars.PREF_USER, MODE_PRIVATE);
        return complexPreferences.getObject(GlobalVars.PREF_USER_KEY, User.class);
    }
}