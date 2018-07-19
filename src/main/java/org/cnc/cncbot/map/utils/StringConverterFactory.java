package org.cnc.cncbot.map.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * String converter used by Retrofit
 * @author sheuze
 *
 */
public final class StringConverterFactory extends Converter.Factory {
  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    return new Converter<ResponseBody, String>() {
      @Override public String convert(ResponseBody value) throws IOException {
        return value.string();
      }
    };
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    return new Converter<String, RequestBody>() {
      @Override public RequestBody convert(String value) throws IOException {
        return RequestBody.create(MediaType.parse("text/plain"), value);
      }
    };
  }
}