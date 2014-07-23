package com.example.httpanduidemo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class MyThread implements Runnable
{
    private String imageURL =
        "http://172.17.182.49:8888/001.jpg";
    
    
    @Override
    public void run()
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(imageURL);
        HttpResponse httpResponse = null;
        
        try
        {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());
                
                Message message = Message.obtain();
                message.what = 1;
                message.obj = data;
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
