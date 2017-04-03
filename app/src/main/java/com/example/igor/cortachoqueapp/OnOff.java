package com.example.igor.cortachoqueapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.PendingIntent.getActivity;

public class OnOff extends AppCompatActivity {

    private static final String FIXXED_URL = "http://150.165.15.10/";

    private static final int STOPSPLASH = 0;
    //time in milliseconds
    private static final long SPLASHTIME = 10000;

    private ImageView splash;

    SqliteController controller = new SqliteController(this);

    //handler for splash screen
    private Handler splashHandler = new Handler() {
        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    //remove SplashScreen from view
                    splash.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_off);
        splash = (ImageView) findViewById(R.id.splashscreen);
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
        ArrayList<HashMap<String, String>> legacySwitch = controller.getAllSwitchs();
        if (legacySwitch.size() != 0) {
            for (HashMap<String, String> switchMap : legacySwitch) {
                createSwitch(switchMap.get("SwitchName"), switchMap.get("SwitchAddress"));
            }
        }
        ImageButton button = (ImageButton) findViewById(R.id.add_switch);

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
                        if (mTomada.getText().toString().isEmpty()) {
                            Toast.makeText(OnOff.this, "Insira um nome para a tomada.", Toast.LENGTH_SHORT).show();
                        } else {
                            createSwitch(mTomada.getText().toString(), mEndereco.getText().toString());
                            saveSwitch(mTomada.getText().toString(), mEndereco.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void saveSwitch(String switchName, String switchAddress) {
        HashMap<String, String> switchMap = new HashMap<String, String>();
        switchMap.put("SwitchName", switchName);
        switchMap.put("SwitchAddres", switchAddress);
        controller.insertSwitch(switchMap);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createSwitch(final String nome, String endereco) {
        final ViewGroup linearLayout = (ViewGroup) findViewById(R.id.switch_layout);
        final LinearLayout relativeLayout = new LinearLayout(OnOff.this);
        final OnOffSwitch bt = new OnOffSwitch(OnOff.this, endereco);
        bt.setText(nome);
        bt.setTextSize(18);
        bt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 0.9f));
        Drawable drawing = ResourcesCompat.getDrawable(getResources(), R.drawable.swtich_bg, null);
        bt.setTrackDrawable(drawing);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Neou-Bold.ttf");
        bt.setBackgroundResource(R.color.backgroundColor);
        bt.setTypeface(tf);
        bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                sendMessage(isChecked);
                if (isChecked) {
                    try {
                        sendOnMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        sendOffMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        final Button remS = new Button(OnOff.this);
        remS.setText("X");
        remS.setTextSize(16);
        remS.setTextColor(Color.rgb(175, 31, 36));
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        relativeLayout.setOrientation(LinearLayout.HORIZONTAL);
        relativeLayout.setBackgroundResource(R.color.backgroundColor);
        relativeLayout.setWeightSum(1);
        LayoutParams params = new LayoutParams(40, LayoutParams.WRAP_CONTENT, 0.1f);
        remS.setLayoutParams(params);
        remS.setBackgroundResource(R.color.backgroundColor);
        remS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeView(relativeLayout);
                controller.deleteSwitch(nome);
            }

        });

        relativeLayout.addView(bt);
        relativeLayout.addView(remS);
        linearLayout.addView(relativeLayout);
    }

    public void sendOnMessage() throws IOException {
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

    public void sendOffMessage() throws IOException {
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

    public void sendMessage(boolean turnOn) {
        String server = FIXXED_URL;
        if (turnOn) {
            server += "on";
        }
        else {
            server += "off";
        }

        TaskEsp taskEsp = new TaskEsp(server);
        taskEsp.execute();
    }

    private class TaskEsp extends AsyncTask<Void, Void, String> {

        String server;

        TaskEsp(String server){
            this.server = server;
        }

        @Override
        protected String doInBackground(Void... params) {

            final String p = "http://"+server;

            String serverResponse = "";

            //Using java.net.HttpURLConnection
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(p).openConnection());

                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = null;
                    inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    serverResponse = bufferedReader.readLine();

                    inputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }
            //

            return serverResponse;
        }
    }
}