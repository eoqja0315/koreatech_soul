package com.example.hdb.mobileprogramming;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int DIALOG_YES_NO_MESSAGE = 1;

    public final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 1;

    EditText IdEditText;
    EditText PasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdEditText = (EditText)findViewById(R.id.IDEditText);
        PasswordEditText = (EditText)findViewById(R.id.PasswordEditText);

        IdEditText.setPadding(12 , 0 , 0 , 0);
        PasswordEditText.setPadding(12, 0 , 0 , 0);

        if (ContextCompat.checkSelfPermission(MainActivity.this , android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.SEND_SMS)) {
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }

        } else {
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this , android.Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.RECEIVE_SMS)){

            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS} ,
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        } else {
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.INTERNET)){

            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET} ,
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        } else {
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onDestroy()");

    }
    @Override
    protected void onDestroy() {

        Log.d(TAG , "onDestroy()");
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
            d.setTitle("종료");
            d.setMessage("정말 종료 하시겠습니꺄?");

            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    MainActivity.this.finish();
                }
            });

            d.setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            d.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.LoginButton:

                phpDown2 task = new phpDown2();

                task.execute("http://10.0.2.2:8080/password.php");

                break;
            case R.id.RegisterButton:
                Intent intent = new Intent(this , RegisterActivity.class);

                startActivity(intent);

                break;
        }
    }

    private class phpDown2 extends AsyncTask<String, Integer , String[]> {

        String[] IDPWList;

        @Override
        protected String[] doInBackground(String... urls){

            Log.d(TAG, "doInBackground()");

            try{

                URL url = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if(conn != null){

                    Log.d(TAG, "conn != null");

                    conn.setConnectTimeout(5000);
                    conn.setUseCaches(false);

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                        Log.d(TAG, "conn.getResponseCode() == HttpURLConnection.HTTP_OK");

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        for(;;){
                            String line = br.readLine();

                            if(line == null)break;

                            IDPWList = line.split("/");

                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return IDPWList;

        }

        protected void onPostExecute(String[] IDPWList){
            Log.d(TAG, "onPostExecute");

            boolean CorrectFlag = false;

            int length = IDPWList[0].length();

            String temp = IDPWList[0].substring(0, length);

            IDPWList[0] = temp;

            String ID = IdEditText.getText().toString();
            String PW = PasswordEditText.getText().toString();

            for(int i = 0; i < IDPWList.length; i += 2){

                Log.d(TAG , IDPWList[i]);
                Log.d(TAG , String.valueOf(IDPWList[i].length()));

                if(ID.equals(IDPWList[i])) {

                    if(PW.equals(IDPWList[i+1])) {

                        CorrectFlag = true;

                        IdEditText.setText("");
                        PasswordEditText.setText("");

                        Intent intent_toListMain = new Intent(getApplicationContext(), ListMainActivity.class);
                        intent_toListMain.putExtra("ID", ID);
                        startActivity(intent_toListMain);

                        break;
                    }

                }

            }

            if(CorrectFlag == false)
                 Toast.makeText(getApplicationContext() , "아이디 혹은 비밀번호를 확인해주세요" , Toast.LENGTH_SHORT).show();

        }

    }

}

