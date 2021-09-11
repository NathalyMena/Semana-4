package com.example.ejercicioconection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Ping extends AppCompatActivity{

    private TextView statusTextView;
    private Button backBtn;
    private String ip, ipLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        backBtn = findViewById(R.id.backBtn);
        statusTextView = findViewById(R.id.statusTextView);

        ip = getIntent().getStringExtra("ipWritten");
        ipLocal = getIntent().getStringExtra("ipHost");
        boolean searchHostsOption = getIntent().getBooleanExtra("searchHostsOption", false);

        if ((ip != null || ip != "") && searchHostsOption == false) {
            pingIp();
        } else {
            searchHosts();
        }

        statusTextView.setText("");

        backBtn.setOnClickListener(

                (v) -> {

                    finish();

                }
        );

    }

    private void pingIp() {

        new Thread(
                () -> {
                    try {

                        InetAddress inet = InetAddress.getByName(ip);

                        for (int i = 0; i < 5; i++) {
                            boolean connected = inet.isReachable(1000);

                            runOnUiThread(

                                    () -> {
                                        if (connected) {
                                            statusTextView.append("Connected \n");
                                        } else {
                                            statusTextView.append("Not connected \n");
                                        }
                                    }
                            );
                        }

                        Thread.sleep(1000);

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private void searchHosts() {

        new Thread(
                () -> {
                    for (int i = 0; i < 254; i++) {

                        String nextIp = ipLocal + i;

                        try {

                            InetAddress inet = InetAddress.getByName(nextIp);
                            Log.e("currentIP", inet.toString());
                            boolean connected = inet.isReachable(1000);

                            runOnUiThread(

                                    () -> {
                                        if (connected) {
                                            statusTextView.append(inet + "\n");
                                        }
                                    }
                            );

                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).start();
    }
}

