package com.yesandroid.sqlite;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;




import android.os.Bundle;
        import android.os.Handler;

        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import java.io.IOException;
        import java.net.DatagramPacket;
        import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   public String parentFolder;

    final Handler handler = new Handler();

    private Button buttonStartReceiving;
    private Button buttonStopReceiving;
    private TextView textViewDataFromClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartReceiving = (Button) findViewById(R.id.btn_start_receiving);
        buttonStopReceiving = (Button) findViewById(R.id.btn_stop_receiving);
        textViewDataFromClient = (TextView) findViewById(R.id.tv_data_from_client);

        buttonStartReceiving.setOnClickListener(this);
        buttonStopReceiving.setOnClickListener(this);

    }

    private void startServerSocket() {

        Thread thread = new Thread(new Runnable() {

            private String stringData = null;

            @Override
            public void run() {

              //  Log.d("thread","therad");
                byte[] msg = new byte[6021];
                DatagramPacket dp = new DatagramPacket(msg, msg.length);
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket(1234);
                    //ds.setSoTimeout(50000);
                    ds.receive(dp);

                    Logging.appendLog(byteArrayToHex(msg),parentFolder);

                    Log.d("msg", Arrays.toString(msg));

                    Log.d("msg", (msg[1] & 0xff)+"");
                    stringData = new String(msg, 0, dp.getLength());
                    updateUI(stringData);

                    String msgToSender = "Bye Bye ";
                    dp = new DatagramPacket(msgToSender.getBytes(), msgToSender.length(), dp.getAddress(), dp.getPort());
                    ds.send(dp);
                    startServerSocket();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
            }

        });
        thread.start();
    }

    private void updateUI(final String stringData) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                String s = textViewDataFromClient.getText().toString();
                if (stringData.trim().length() != 0)
                    textViewDataFromClient.setText(s + "\n" + "From Client : " + stringData);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_start_receiving:

                parentFolder=java.text.DateFormat.getDateTimeInstance().format(new Date());
                startServerSocket();

                buttonStartReceiving.setEnabled(false);
                buttonStopReceiving.setEnabled(true);
                break;

            case R.id.btn_stop_receiving:

                //Add logic to stop server socket yourself

                buttonStartReceiving.setEnabled(true);
                buttonStopReceiving.setEnabled(false);
                break;
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);

        for(byte b: a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}