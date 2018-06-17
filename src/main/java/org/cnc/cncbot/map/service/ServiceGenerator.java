package org.cnc.cncbot.map.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServiceGenerator {

   private static OkHttpClient.Builder httpClient
     = new OkHttpClient.Builder();

   public static <S> S createService(Class<S> serviceClass, String baseUrl) {
	   Retrofit retrofit = new Retrofit.Builder()
	   .client(httpClient.build())
       .baseUrl(baseUrl)
       .addConverterFactory(GsonConverterFactory.create()).build();
	   
       return retrofit.create(serviceClass);
   }
}