package com.booking.paymentservice.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.booking.paymentservice.dto.request.OrderRequest;
import com.booking.paymentservice.dto.response.OrderResponse;
import com.booking.paymentservice.utils.app.TokenUtil;
import com.booking.paymentservice.utils.zalopay.HMACUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    TokenUtil tokenUtil;

    @NonFinal
    @Value("${app.zalopay.appid}")
    private String appId;

    @NonFinal
    @Value("${app.zalopay.key1}")
    private String key1;

    @NonFinal
    @Value("${app.zalopay.key2}")
    private String key2;

    @NonFinal
    @Value("${app.zalopay.endpoint}")
    private String endpoint;

    public OrderResponse createOrder(OrderRequest request) {
        // String userId = tokenUtil.getUserIdFromToken();
        String userId = request.getUserId();

        OrderResponse orderResponse = new OrderResponse();
        
        final Map embed_data = new HashMap(){{}};
        final Map[] item = {
            new HashMap(){{}}
        };

        Random rand = new Random();
        int random_id = rand.nextInt(1000000);

        
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", appId);
        order.put("app_trans_id", getCurrentTimeString("yyMMdd") + "_" + random_id);
        order.put("app_time", System.currentTimeMillis());
        order.put("app_user", userId);
        order.put("amount", request.getAmount());
        order.put("bank_code", "zalopayapp");
        order.put("embed_data", new JSONObject(embed_data).toString());
        order.put("item", new JSONObject(item).toString());
        

        String data = order.get("app_id") +"|"+ order.get("app_trans_id") +"|"+ order.get("app_user") +"|"+ order.get("amount")
                +"|"+ order.get("app_time") +"|"+ order.get("embed_data") +"|"+ order.get("item");

        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key1, data));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(endpoint);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> e : order.entrySet()) {
            params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }
            JSONObject result = new JSONObject(resultJsonStr.toString());

            for (String key : result.keySet()) {
                orderResponse.put(key, result.get(key));
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orderResponse;
    }

    private String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
}
