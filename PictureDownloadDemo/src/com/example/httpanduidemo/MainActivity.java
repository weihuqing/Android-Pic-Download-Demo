package com.example.httpanduidemo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity
{
    private Button button;
    
    private ImageView imageView;
    
    private ProgressDialog dialog;
    
    private String imageURL = "http://172.17.182.49:8888/001.jpg";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button = (Button) findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.image);
        
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
        
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new MyThread()).start();
                dialog.show();
            }
        });
    }
    
    private Handler handler = new Handler()
    {
        // 在Handler中获取消息，重写handleMessage()方法
        @Override
        public void handleMessage(Message msg)
        {
            // 判断消息码是否为1
            switch (msg.what)
            {
                case 1:
                    byte[] data = (byte[]) msg.obj;
                    Bitmap bmp =
                        BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView.setImageBitmap(bmp);
                    dialog.dismiss();
                    break;
                
                default:
                    break;
            }
        }
    };
    
    public class MyThread implements Runnable
    {
        
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
                    byte[] data =
                        EntityUtils.toByteArray(httpResponse.getEntity());
                    // 获取一个Message对象，设置what为1
                    Message msg = Message.obtain();
                    msg.obj = data;
                    msg.what = 1;
                    // 发送这个消息到消息队列中
                    handler.sendMessage(msg);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
