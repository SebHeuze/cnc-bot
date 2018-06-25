package org.cnc.cncbot.map.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServiceGenerator {

   private static OkHttpClient.Builder httpClient
     = new OkHttpClient.Builder();
   
   private static HttpLoggingInterceptor logging
   = new HttpLoggingInterceptor()
     .setLevel(HttpLoggingInterceptor.Level.BASIC);
   
   
   public static <S> S createService(Class<S> serviceClass, String baseUrl) {
	   if (!httpClient.interceptors().contains(logging)) {
           httpClient.addInterceptor(logging);
	   }
       Retrofit retrofit = new Retrofit.Builder()
		   .client(httpClient.followRedirects(false).build())
	       .baseUrl(baseUrl)
	       .addConverterFactory(GsonConverterFactory.create()).build();
       
       return retrofit.create(serviceClass);
   }
}