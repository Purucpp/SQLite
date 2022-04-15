package com.yesandroid.sqlite;

import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import androidx.appcompat.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewReplyFromServer;
    private EditText mEditTextSendMessage;
    private String ipAddress;
    protected double[] results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        results = new double[]{1, 2, 3, 4, 5, 6, 7, 8};
        Button buttonSend = (Button) findViewById(R.id.btn_send);

        mEditTextSendMessage = (EditText) findViewById(R.id.edt_send_message);
        mTextViewReplyFromServer = (TextView) findViewById(R.id.tv_reply_from_server);


        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo dhcpInfo=wifiMgr.getDhcpInfo();
       // Log.d("test",Formatter.formatIpAddress(dhcpInfo.gateway));
        ipAddress=Formatter.formatIpAddress(dhcpInfo.gateway);



//        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
//        String ipAddress = Formatter.formatIpAddress(ip);

     //   Log.d("address",ipAddress);

        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_send:
                sendMessage(mEditTextSendMessage.getText().toString());
                break;
        }
    }

    private void sendMessage(final String message) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            String stringData;

            @Override
            public void run() {

                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                    // IP Address below is the IP address of that Device where server socket is opened.
                    InetAddress serverAddr = InetAddress.getByName(ipAddress);
                    DatagramPacket dp;

                    byte[] dataTosend = new byte[6021];
                    dataTosend[0] = 54;
                    dataTosend[1] = (byte) 255;
                    dataTosend[2] = (byte) results[0]; //fhr
                    dataTosend[3] = (byte) results[1]; // Mhr
                    dataTosend[4] = (byte) results[2]; // uc plot
                  /*  for(int i=5;i<6021;i++)
                    {
                        dataTosend[i]=(byte) 0;
                    } */

                    dp = new DatagramPacket(dataTosend, dataTosend.length, serverAddr, 1234);
                    ds.send(dp);

                    byte[] lMsg = new byte[1000];
                    dp = new DatagramPacket(lMsg, lMsg.length);
                    ds.receive(dp);
                    stringData = new String(lMsg, 0, dp.getLength());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String s = mTextViewReplyFromServer.getText().toString();
                        if (stringData.trim().length() != 0)
                            mTextViewReplyFromServer.setText(s + "\nFrom Server : " + stringData);

                    }
                });
            }
        });

        thread.start();
    }


}