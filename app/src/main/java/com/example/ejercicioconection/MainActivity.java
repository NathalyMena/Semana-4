package com.example.ejercicioconection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private EditText Input1, Input2, Input3, Input4;
    private String ID1, ID2, ID3, ID4;
    private Button pingBtn, hostBtn;
    private TextView IPText;
    private String ipLocal = "";
    private boolean searchHostsOption = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Input1 = findViewById(R.id.Input1);
        Input2 = findViewById(R.id.Input2);
        Input3 = findViewById(R.id.Input3);
        Input4 = findViewById(R.id.Input4);
        pingBtn = findViewById(R.id.pingBtn);
        hostBtn = findViewById(R.id.hostBtn);
        IPText = findViewById(R.id.IPText);

        getHostIp();

        pingBtn.setOnClickListener(

                (v) -> {
                    loadPing();
                }
        );

        hostBtn.setOnClickListener(

                (v) -> {
                    loadHosts();
                }
        );


    }

    private void getHostIp() {

        new Thread(
                () -> {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress("google.com", 80));
                        ipLocal = socket.getLocalAddress().getHostAddress();

                        socket.close();

                        runOnUiThread(

                                () -> {
                                    IPText.setText(ipLocal);
                                }
                        );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        ).start();
    }

    private String getWrittenIp() {

        ID1 = Input1.getText().toString();
        ID2 = Input2.getText().toString();
        ID3 = Input3.getText().toString();
        ID4 = Input4.getText().toString();

        return ID1 + "." + ID2 + "." + ID3 + "." + ID4;

    }

    private void loadPing() {

        getWrittenIp();

        if (ID1.isEmpty() || ID2.isEmpty() || ID3.isEmpty() || ID4.isEmpty()) {
            Toast.makeText(this, "Ingresa todos los valores", Toast.LENGTH_SHORT).show();
        } else {
            searchHostsOption = false;
            Intent i = new Intent(this, Ping.class);
            i.putExtra("ipWritten", getWrittenIp());
            i.putExtra("searchHostsOption", searchHostsOption);
            startActivity(i);
        }
    }

    private void loadHosts() {

        searchHostsOption = true;
        Intent i = new Intent(this, Ping.class);

        String[] ipSplited = ipLocal.split("\\.");
        String ipHostIncompleted = ipSplited[0] + "." + ipSplited[1] + "." + ipSplited[2] + ".";

        i.putExtra("ipHost", ipHostIncompleted);
        i.putExtra("searchHostsOption", searchHostsOption);
        startActivity(i);

    }
}

