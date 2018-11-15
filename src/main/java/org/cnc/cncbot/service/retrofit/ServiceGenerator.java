package org.cnc.cncbot.service.retrofit;

import org.cnc.cncbot.dto.ResponseType;
import org.cnc.cncbot.utils.StringConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit service generator
 * @author SebHeuze
 *
 */
public final class ServiceGenerator {

   private static OkHttpClient.Builder httpClient
     = new OkHttpClient.Builder();
   
   /**
    * Change Value of Level to enable Http logs
    */
   private static HttpLoggingInterceptor logging
   = new HttpLoggingInterceptor()
     .setLevel(HttpLoggingInterceptor.Level.NONE);
   
   
   public static <S> S createService(Class<S> serviceClass, String baseUrl, ResponseType responseType) {
	   if (!httpClient.interceptors().contains(logging)) {
           httpClient.addInterceptor(logging);
	   }
       Builder retrofitBuilder = new Retrofit.Builder()
		   .client(httpClient.followRedirects(false).build())
	       .baseUrl(baseUrl);
       
       switch (responseType) {
       	case JSON :
       		retrofitBuilder = retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
       	case PLAIN_TEXT:
       	default:
       		retrofitBuilder = retrofitBuilder.addConverterFactory(new  StringConverterFactory());
       }
       
       return retrofitBuilder.build().create(serviceClass);
   }
}