package com.example.igor.cortachoqueapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static android.app.PendingIntent.getActivity;

public class OnOff extends AppCompatActivity {

    private static final String FIXXED_URL = "150.165.15.10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_off);

        Button button =  (Button) findViewById(R.id.add_switch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OnOff.this);
                View mView = getLayoutInflater().inflate(R.layout.add_switch_layout, null);
                final EditText mTomada = (EditText) mView.findViewById(R.id.etTomada);
                final EditText mEndereco = (EditText) mView.findViewById(R.id.etEndereco);
                Button mCriarTomada = (Button) mView.findViewById(R.id.btnCriarTomada);


//                mBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mCriarTomada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createSwitch(mTomada.getText().toString(), mEndereco.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
            });
            }

            public void createSwitch(String nome, String endereco) {
                final ViewGroup linearLayout = (ViewGroup) findViewById(R.id.switch_layout);
                final OnOffSwitch bt = new OnOffSwitch(OnOff.this, endereco);
                bt.setText(nome);
                bt.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
                linearLayout.addView(bt);
            }

            public void sendMessage() throws IOException {
                URL url = new URL(FIXXED_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    writeStream(out);

                } finally {
                    urlConnection.disconnect();

                }
            }

            public void writeStream(OutputStream out) throws IOException {
                String message = "new message";
                out.write(message.getBytes());
            }

        }