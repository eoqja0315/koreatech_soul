package com.example.hdb.mobileprogramming;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

public class RegisterActivity extends AppCompatActivity {

    static final int SEX = 0;
    static final int AGE = 1;

    public static final String TAG = "RegisterActivity";
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    boolean check_ID = false, check_PW = false, check_PH = false;

    String messageToCertification = "000000";

    EditText IdEditText;
    EditText PasswordEditText;
    EditText PasswordCheckEditText;
    EditText PhonNumberEditText;
    EditText PhonNumberEditText2;
    EditText PhonNumberEditText3;
    EditText CertificateNumberEditText;

    TextView AgeTextView;
    TextView SexTextView;
    TextView PasswordChecking;
    TextView RedundancyMessage;

    BroadcastReceiver myReceiver;

    public class Broadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG , "onReceive");

            if(SMS_RECEIVED.equals(intent.getAction())){
                //메세지 파싱
                Bundle bundle = intent.getExtras();
                Object messages[] = (Object[])bundle.get("pdus");
                SmsMessage smsMessage[] = new SmsMessage[messages.length];

                for(int i = 0; i < messages.length; i++){
                    //PDU 포멧으로 되어 있는 메세지 복원
                    smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
                }

                messageToCertification = smsMessage[0].getMessageBody().toString();

                CertificateNumberEditText.setText(messageToCertification.toString());

                Log.d(TAG , "parsingMessage");

                abortBroadcast();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG , "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        IdEditText = (EditText)findViewById(R.id.IdEditText);
        PasswordEditText = (EditText)findViewById(R.id.PasswordEditText);
        PasswordCheckEditText = (EditText)findViewById(R.id.PasswordCheckEditText);
        PhonNumberEditText = (EditText)findViewById(R.id.PhoneNumberEditText);
        PhonNumberEditText2 = (EditText)findViewById(R.id.PhoneNumberEditText2);
        PhonNumberEditText3 = (EditText)findViewById(R.id.PhoneNumberEditText3);
        CertificateNumberEditText = (EditText)findViewById(R.id.CertificateNumberEditText);

        AgeTextView = (TextView)findViewById(R.id.AgeTextView2);
        SexTextView = (TextView)findViewById(R.id.Sex);
        PasswordChecking = (TextView)findViewById(R.id.PasswordChecking);
        RedundancyMessage =  (TextView)findViewById(R.id.RedundancyMessage);

        IdEditText.setPadding(12 , 0 , 0 , 0);
        PasswordCheckEditText.setPadding(12 , 0 , 0 , 0);
        PasswordEditText.setPadding(12, 0, 0, 0);
        PhonNumberEditText.setPadding(12, 0 , 0 ,0);
        PhonNumberEditText2.setPadding(12, 0 , 0 ,0);
        PhonNumberEditText3.setPadding(12, 0 , 0 ,0);
        CertificateNumberEditText.setPadding(12 , 0 , 0 ,0);

        myReceiver = new Broadcast();

        IntentFilter intentfilter = new IntentFilter();

        intentfilter.addAction(SMS_RECEIVED);

        registerReceiver(myReceiver, intentfilter);

        AgeTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(AGE);
            }
        });

        SexTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(SEX);
            }
        });
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG , "onDestroy()");

        super.onDestroy();

        unregisterReceiver(myReceiver);

    }
    public void sendSMS(String smsNumber, String smsText){

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumber, null, smsText, null, null);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case SEX:
                final CharSequence[] items = {"male", "female"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("성별");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        SexTextView.setText(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                return alert;
            case AGE:
                final CharSequence[] items2 = {"10th", "20th", "30th", "40th", "50th"};

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("연령");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        AgeTextView.setText(items2[item].toString());
                    }
                });
                AlertDialog alert2 = builder2.create();
                return alert2;
        }
        return null;
    }

    public void onClickButton(View view){
        switch (view.getId()){
            case R.id.ApproveButton:
                if (!PasswordEditText.getText().toString().equals("") && PasswordEditText.getText().toString().equals(PasswordCheckEditText.getText().toString())) {
                    PasswordChecking.setText("Correct");
                    PasswordChecking.setTextColor(0xFF0000ff);
                    check_PW = true;
                } else if (!PasswordEditText.getText().toString().equals("") && !PasswordEditText.getText().toString().equals(PasswordCheckEditText.getText().toString())) {
                    PasswordChecking.setText("Incorrect");
                    PasswordChecking.setTextColor(0xFFff0000);
                    check_PW = false;
                }

               if((check_PW && check_PH && check_ID) && !AgeTextView.getText().toString().equals("") && !SexTextView.getText().toString().equals("")) {
                    insert();
                    finish();
                }
                else if(check_ID == false) {
                    Toast.makeText(getApplicationContext(), "아이디를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if(check_PW == false) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if(check_PH == false) {
                    Toast.makeText(getApplicationContext(), "인증번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if(AgeTextView.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "연령을 선택해주세요", Toast.LENGTH_SHORT).show();
                } else if(SexTextView.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.CancelButton:
                finish();
                break;
        }
    }

    public void onClickTextView(View view){
        switch (view.getId()){
            case R.id.CertificationTextView:

                Log.d(TAG , "CertificationTextView");

                Random random = new Random();

                int randomNumber = 0;

                randomNumber = random.nextInt(Integer.MAX_VALUE)%1000000;

                String message = String.valueOf(randomNumber);

                String Num = null;

                Num = PhonNumberEditText.getText().toString();
                Num += PhonNumberEditText2.getText().toString();
                Num += PhonNumberEditText3.getText().toString();

                sendSMS(Num , message);

                break;
            case R.id.CertificationTextView2:

                Log.d(TAG, "CertificationTextView2");

                String msg = CertificateNumberEditText.getText().toString();

                //Toast.makeText(getApplicationContext(), messageToCertification , Toast.LENGTH_SHORT).show();

                if(msg.equals(messageToCertification)){
                    check_PH = true;
                    Toast.makeText(getApplicationContext() , "인증되었습니다." , Toast.LENGTH_SHORT).show();
                } else{
                    check_PH = false;
                    Toast.makeText(getApplicationContext(), "인증번호를 확인해주세요" , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.IdCheckTextView:

                Log.d(TAG, "IdCheckButton");

                phpDown task = new phpDown();

                task.execute("http://10.0.2.2:8080/test.php");

                break;
        }
    }

    public void insert() {

        Log.d(TAG , "insert()");

        String id = IdEditText.getText().toString();
        String pw = PasswordEditText.getText().toString();
        String sex = SexTextView.getText().toString();
        String age = AgeTextView.getText().toString();
        String phone = PhonNumberEditText.getText().toString() + PhonNumberEditText2.getText().toString() + PhonNumberEditText3.getText().toString();

        insertToDatabase(id, pw, sex, age, phone);
    }

    private void insertToDatabase(String id, String pw, String sex, String age, String phone) {
        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                Log.d(TAG , "insert_onPreExecute()");
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Please wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d(TAG , "insert_onPostExecute()");
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                Log.d(TAG , "insert_doInBackground()");

                try {
                    String id = (String)params[0];
                    String pw = (String)params[1];
                    String sex = (String)params[2];
                    String age = (String)params[3];
                    String phone = (String)params[4];

                    String link = "http://10.0.2.2:8080/insert.php";

                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("pw", "UTF-8") + "=" + URLEncoder.encode(pw, "UTF-8");
                    data += "&" + URLEncoder.encode("sex", "UTF-8") + "=" + URLEncoder.encode(sex, "UTF-8");
                    data += "&" + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");
                    data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

                    Log.d(TAG , data);

                    URL url = new URL(link);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setRequestMethod("POST");

                    conn.setDoInput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch(Exception e) {
                    return new String("Exception : " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(id, pw, sex, age, phone);
    }

    private class phpDown extends AsyncTask <String, Integer , String[]> {

        String[] IDList;

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

                            IDList = line.split("/");

                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return IDList;

        }

        protected void onPostExecute(String[] IDList){
            Log.d(TAG, "onPostExecute");

            int length = IDList[0].length();

            String temp = IDList[0].substring(1, length);

            IDList[0] = temp;

            String ID = IdEditText.getText().toString();

                for(int i = 0; i < IDList.length; i++){

                    if(ID.equals(IDList[i])) {

                        RedundancyMessage.setText("중복됩니다.");
                        check_ID = false;
                        break;

                    }else{

                        check_ID = true;

                        RedundancyMessage.setText("중복되지않습니다.");
                    }

                }

        }

    }
}