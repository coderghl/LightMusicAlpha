package com.ghl.lightmusicalpha.tools;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpHelper {
  // 发送get请求
  public static void requestGet(String url, Callback callback) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .get()
      .url(url)
      .build();
    client.newCall(request).enqueue(callback);
  }
}
