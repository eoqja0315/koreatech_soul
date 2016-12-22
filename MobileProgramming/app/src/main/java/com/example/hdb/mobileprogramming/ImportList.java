package com.example.hdb.mobileprogramming;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ImportList extends Activity {
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    static final int DIALOG_METHOD = 3;

    Calendar cal = Calendar.getInstance();

    public int year, month, day, hour, min;
    private int mYear, mMonth, mDay, mHour, mMin;

    public String date, time;
    public String []days = {"일","월","화","수","목","금","토"};

    TextView dp;
    TextView tp;
    TextView method;
    EditText money, breakdown;

    public ImportList() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_list);

        money = (EditText)findViewById(R.id.et_sum);
        breakdown = (EditText)findViewById(R.id.et_breakdown);

        dp = (TextView)findViewById(R.id.dp_date);
        tp = (TextView)findViewById(R.id.tp_time);
        method = (TextView)findViewById(R.id.et_method);

        dp.setText(mYear + ". " + (mMonth + 1) + ". " + mDay);

        if(mHour < 10 && mMin < 10) {
            time = "0" + mHour + " : 0" + mMin;
        } else if (mHour < 10 && mMin > 9) {
            time = "0" + mHour + " : " + mMin;
        } else if (mHour > 9 && mMin < 10) {
            time = mHour + " : 0" + mMin;
        } else if (mHour > 9 && mMin > 9) {
            time = mHour + " : " + mMin;
        }
        tp.setText(time);

        dp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        tp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        method.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_METHOD);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
            mYear = yearSelected;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            dp.setText(mYear + ". " + (mMonth+1) + ". " + mDay);
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourSelected, int minSelected) {
            mHour = hourSelected;
            mMin = minSelected;
            if(mHour < 10 && mMin < 10) {
                time = "0" + mHour + " : 0" + mMin;
            } else if (mHour < 10 && mMin > 9) {
                time = "0" + mHour + " : " + mMin;
            } else if (mHour > 9 && mMin < 10) {
                time = mHour + " : 0" + mMin;
            } else if (mHour > 9 && mMin > 9) {
                time = mHour + " : " + mMin;
            }

            tp.setText(time);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMin, false);
            case DIALOG_METHOD:
                final CharSequence[] items2 = {"카드", "현금"};

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("수입");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        method.setText(items2[item].toString());
                    }
                });
                AlertDialog alert2 = builder2.create();
                return alert2;
        }
        return null;
    }

    public void onClick_CANCEL(View v) {
        finish();
    }
    public void onClick_OK(View v) {
        date = (mYear + ". " + (mMonth+1) + ". " + mDay + " (" + days[cal.get(Calendar.DAY_OF_WEEK) - 1] + ") " + time).substring(2);

        Intent intent = new Intent(this, ListMainActivity.class);

        if(!money.getText().toString().equals("") && !breakdown.getText().toString().equals("")) {
            intent.putExtra("DATE", date);
            intent.putExtra("MONEY", money.getText().toString());
            intent.putExtra("BREAKDOWN", breakdown.getText().toString());
            intent.putExtra("METHOD", method.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "입력을 확인하세요", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_CANCELED, intent);
    }
}