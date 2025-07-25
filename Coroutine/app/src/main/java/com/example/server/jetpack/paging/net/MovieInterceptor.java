package com.example.server.jetpack.paging.net;

import androidx.annotation.NonNull;

import com.example.server.realPro.model.CommonConstant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MovieInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(Chain chain) {
        try {
            Thread.sleep(2000); // 模拟网络延迟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Request request = chain.request();
        String sinceStr = request.url().queryParameter("since");
        String pageSizeStr = request.url().queryParameter("pageSize");

        int since = 0;
        int pageSize = 8;

        try {
            if (sinceStr != null) {
                since = Integer.parseInt(sinceStr);
            }
            if (pageSizeStr != null) {
                pageSize = Integer.parseInt(pageSizeStr);
            }
        } catch (NumberFormatException e) {
            // 使用默认值
        }

        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String item = String.format(
                    "{\"movieName\": \"Movie %d\", \"movieUrl\": \"%s\"}",
                    i, i, CommonConstant.IMAGE_URL
            );
            items.add(item);
        }

        int toIndex = Math.min(since + pageSize, items.size());
        List<String> pageItems = items.subList(since, toIndex);

        String json = "[" + String.join(",\n", pageItems) + "]";

        Response ok = new Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(json, MediaType.parse("application/json")))
                .addHeader("content-type", "application/json")
                .build();
        return ok;
    }
}
