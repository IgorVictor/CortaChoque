package com.example.igor.cortachoqueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class OnOff extends AppCompatActivity {

    private static final String FIXXED_URL = "150.165.15.10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_off);

        final Button button =  (Button) findViewById(R.id.add_switch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //when addYear is clicked show the new button
                Button b = new Button(v.getContext());  // create Button
                b.setText("New button");      //  Set the text of the Button
                // set params and add button to layout
                
            }
        });
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
