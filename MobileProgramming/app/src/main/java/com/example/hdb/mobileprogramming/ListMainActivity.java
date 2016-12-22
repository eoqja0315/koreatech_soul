package com.example.hdb.mobileprogramming;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

class ListViewAdapter extends BaseAdapter {
    private ArrayList<Listview> listViewItemList = new ArrayList<Listview>() ;

    TextView list_Dt, list_Cate, list_bD, list_Money, list_Method;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        list_Dt = (TextView) convertView.findViewById(R.id.list_dt) ;
        list_Cate = (TextView) convertView.findViewById(R.id.list_category) ;
        list_bD = (TextView) convertView.findViewById(R.id.list_breakdown) ;
        list_Money = (TextView) convertView.findViewById(R.id.list_money) ;
        list_Method = (TextView) convertView.findViewById(R.id.list_method) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Listview listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        list_Dt.setText(listViewItem.getDate());
        list_Cate.setText(listViewItem.getCate());
        list_bD.setText(listViewItem.getBD());
        list_Money.setText(listViewItem.getMoney());
        list_Method.setText(listViewItem.getMethod());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String date, String category, String breakdown, String money, String method) {
        Listview item = new Listview();

        item.setDate(date);
        item.setCate(category);
        item.setBD(breakdown);
        item.setMoney(money);
        item.setMethod(method);

        listViewItemList.add(item);
        sort();
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem2(String date, String breakdown, String money, String method) {
        Listview item = new Listview();

        item.setDate(date);
        item.setBD(breakdown);
        item.setMoney(money);
        item.setMethod(method);

        listViewItemList.add(item);
        sort();
    }

    public void sort() {
        Collections.sort(listViewItemList, Listview.ALPHA_COMPARATOR);
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public class ListMainActivity extends AppCompatActivity {
    static final int METHOD_DIALOG_ID = 0;
    static final int CATEGORY_DIALOG_ID = 1;

    private DBHelper helper;
    private SQLiteDatabase db;

    Calendar mCal = Calendar.getInstance();
    Calendar tCal = Calendar.getInstance();
    SimpleDateFormat sdf;

    boolean bSpend, bImport, bBudget;

    String sID, sDate, sCategory, sBreakdown, sMoney, sMethod;
    TextView spend_menu, import_menu, budget_menu, stat_menu;

    Button current_amount;

    Button before_month, next_month, year_month;
    Button pay_method, pay_category;

    ListView listview, listview2, listview3, listview4, listview5, listview6, listview7, listview8;
    ListViewAdapter adapter, adapter2, adapter3, adapter4, adapter5, adapter6, adapter7, adapter8;
    //5 = 결제수단별, 6 = 카테고리별, 7 = 예산관리, 8 = 자료통계

    public int spend_amount = 0, spend_method_amount = 0, spend_category_amount = 0;
    public int import_amount = 0, import_method_amount = 0;

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        Intent intent = getIntent();
        sID = intent.getStringExtra("ID");

        helper = new DBHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        adapter = new ListViewAdapter();
        adapter2 = new ListViewAdapter();
        adapter3 = new ListViewAdapter();
        adapter4 = new ListViewAdapter();
        adapter5 = new ListViewAdapter();
        adapter6 = new ListViewAdapter();
        adapter7 = new ListViewAdapter();
        adapter8 = new ListViewAdapter();

        listview = (ListView)findViewById(R.id.history_List);
        listview2 = (ListView)findViewById(R.id.history_List);
        listview3 = (ListView)findViewById(R.id.history_List);
        listview4 = (ListView)findViewById(R.id.history_List);
        listview5 = (ListView)findViewById(R.id.history_List);
        listview6 = (ListView)findViewById(R.id.history_List);
        listview7 = (ListView)findViewById(R.id.history_List);
        listview8 = (ListView)findViewById(R.id.history_List);


        listview.setAdapter(adapter);

        spend_menu = (TextView)findViewById(R.id.spend_Menu);
        import_menu = (TextView)findViewById(R.id.import_Menu);
        budget_menu = (TextView)findViewById(R.id.budget_Menu);
        stat_menu= (TextView)findViewById(R.id.stat_Menu);

        before_month = (Button)findViewById(R.id.before_Month);
        next_month = (Button)findViewById(R.id.next_Month);
        year_month = (Button)findViewById(R.id.Year_Month);

        current_amount = (Button)findViewById(R.id.current_Amount);

        pay_method = (Button)findViewById(R.id.pay_Method);
        pay_category = (Button)findViewById(R.id.pay_Category);

        String []days = {"일","월","화","수","목","금","토"};

        sdf = new SimpleDateFormat();
        String ym_date = sdf.format(mCal.getTime()).substring(0, 7);
        String ymd_date = sdf.format(mCal.getTime()).substring(0, 11);

        year_month.setText("20" + ym_date);
        bSpend = true;

        Cursor c;
        c = db.rawQuery("SELECT * FROM userinfo WHERE NOT _category IS NULL AND _id = '" + sID + "';", null);

        if(c.getColumnCount() != 0) {
            while(c.moveToNext()) {
                sDate = c.getString(1);
                sCategory = c.getString(2);
                sBreakdown = c.getString(3);
                sMoney = c.getString(4);
                sMethod = c.getString(5);

                adapter.addItem(sDate, sCategory, sBreakdown, sMoney, sMethod);
                spend_amount += Integer.parseInt(sMoney);
            }
            adapter.notifyDataSetChanged();
            current_amount.setText("￦" + String.valueOf(spend_amount).toString());
        }

        c = db.rawQuery("SELECT * FROM userinfo WHERE _category IS NULL AND _id = '" + sID + "';", null);

        if(c.getColumnCount() != 0) {
            while(c.moveToNext()) {
                sDate = c.getString(1);
                sBreakdown = c.getString(3);
                sMoney = c.getString(4);
                sMethod = c.getString(5);

                adapter2.addItem(sDate, null, sBreakdown, sMoney, sMethod);
                import_amount += Integer.parseInt(sMoney);
            }
            adapter2.notifyDataSetChanged();
        }

        pay_method.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(METHOD_DIALOG_ID);
            }
        });

        pay_category.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bImport == false) {
                    showDialog(CATEGORY_DIALOG_ID);
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case METHOD_DIALOG_ID:
                final CharSequence[] items = {"카드", "현금"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("선택해주세요");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Cursor c;
                        adapter5 = new ListViewAdapter();
                        spend_method_amount = 0;
                        listview5.setAdapter(adapter5);

                        if(bSpend == true) {
                            c = db.rawQuery("SELECT * FROM userinfo WHERE _method = '" + items[item] + "'AND _category IS NOT NULL AND _id = '" + sID + "';", null);
                            if(c.getColumnCount() != 0) {
                                while(c.moveToNext()) {
                                    sDate = c.getString(1);
                                    sCategory = c.getString(2);
                                    sBreakdown = c.getString(3);
                                    sMoney = c.getString(4);
                                    sMethod = c.getString(5);

                                    adapter5.addItem(sDate, sCategory, sBreakdown, sMoney, sMethod);
                                    spend_method_amount += Integer.parseInt(sMoney);
                                }
                                adapter5.notifyDataSetChanged();
                                current_amount.setText("￦" + String.valueOf(spend_method_amount).toString());
                            }
                        } else if(bImport == true){
                            c = db.rawQuery("SELECT * FROM userinfo WHERE _method = '" + items[item] + "'AND _category IS NULL AND _id = '" + sID + "';", null);
                            if(c.getColumnCount() != 0) {
                                while(c.moveToNext()) {
                                    sDate = c.getString(1);
                                    sBreakdown = c.getString(3);
                                    sMoney = c.getString(4);
                                    sMethod = c.getString(5);

                                    adapter5.addItem(sDate, null, sBreakdown, sMoney, sMethod);
                                    spend_method_amount += Integer.parseInt(sMoney);
                                }
                                adapter5.notifyDataSetChanged();
                                current_amount.setText("￦" + String.valueOf(spend_method_amount).toString());
                            }
                        }
                        adapter5.notifyDataSetChanged();
                    }
                });
                AlertDialog alert = builder.create();
                return alert;
            case CATEGORY_DIALOG_ID:
                final CharSequence[] items2 = {"식비", "학비", "의류", "기타생활비"};
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("카테고리를 선택해주세요");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Cursor c;
                        adapter6 = new ListViewAdapter();
                        spend_category_amount = 0;
                        listview6.setAdapter(adapter6);

                        if(bSpend == true) {
                            c = db.rawQuery("SELECT * FROM userinfo WHERE _category = '" + items2[item] + "'AND _category IS NOT NULL AND _id = '" + sID + "';", null);
                            if (c.getColumnCount() != 0) {
                                while (c.moveToNext()) {
                                    sDate = c.getString(1);
                                    sCategory = c.getString(2);
                                    sBreakdown = c.getString(3);
                                    sMoney = c.getString(4);
                                    sMethod = c.getString(5);

                                    adapter6.addItem(sDate, sCategory, sBreakdown, sMoney, sMethod);
                                    spend_category_amount += Integer.parseInt(sMoney);
                                }
                                adapter6.notifyDataSetChanged();
                                current_amount.setText("￦" + String.valueOf(spend_category_amount).toString());
                            }
                        } else if(bImport == true){
                            c = db.rawQuery("SELECT * FROM userinfo WHERE _category = '" + items2[item] + "'AND _category IS NULL AND _id = '" + sID + "';", null);
                            if (c.getColumnCount() != 0) {
                                while (c.moveToNext()) {
                                    sDate = c.getString(1);
                                    sBreakdown = c.getString(3);
                                    sMoney = c.getString(4);
                                    sMethod = c.getString(5);

                                    adapter6.addItem(sDate, null, sBreakdown, sMoney, sMethod);
                                    spend_category_amount += Integer.parseInt(sMoney);
                                }
                                adapter6.notifyDataSetChanged();
                                current_amount.setText("￦" + String.valueOf(spend_category_amount).toString());
                            }
                        }
                        adapter6.notifyDataSetChanged();
                    }
                });
                AlertDialog alert2 = builder2.create();
                return alert2;
        }
        return null;
    }

    public void onClick_before_Month(View view) {
        tCal.add(Calendar.MONTH, -1);
        String t_ym_date = sdf.format(tCal.getTime()).substring(0, 7);
        year_month.setText("20" + t_ym_date);
    }

    public void onClick_next_Month(View view) {
        tCal.add(Calendar.MONTH, 1);
        String t_ym_date = sdf.format(tCal.getTime()).substring(0, 7);
        year_month.setText("20" + t_ym_date);
    }

    public void onClick_Menu(View view) {
        switch(view.getId()) {
            case R.id.spend_Menu:
                spend_menu.setTextColor(ColorStateList.valueOf(0xFF000000));
                import_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                budget_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                stat_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                pay_category.setText("카테고리별");
                pay_method.setText("결제수단별");
                listview.setAdapter(adapter);
                current_amount.setText("￦" + String.valueOf(spend_amount));

                bSpend = true;
                bImport = false;
                bBudget = false;

                break;
            case R.id.import_Menu:
                spend_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                import_menu.setTextColor(ColorStateList.valueOf(0xFF000000));
                budget_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                stat_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                pay_category.setText("");
                pay_method.setText("수입수단별");
                listview2.setAdapter(adapter2);
                current_amount.setText("￦" + String.valueOf(import_amount));

                bSpend = false;
                bImport = true;
                bBudget = false;

                break;
            case R.id.budget_Menu:
                spend_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                import_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                budget_menu.setTextColor(ColorStateList.valueOf(0xFF000000));
                stat_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));

                bSpend = false;
                bImport = false;
                bBudget = true;

                break;
            case R.id.stat_Menu:
                spend_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                import_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                budget_menu.setTextColor(ColorStateList.valueOf(0xFFaaaaaa));
                stat_menu.setTextColor(ColorStateList.valueOf(0xFF000000));

                break;
            default: break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent in_s = new Intent(this, SpendList.class);
        Intent in_i = new Intent(this, ImportList.class);
        switch(item.getItemId()) {
            case R.id.add:
                if(bSpend == true)
                    startActivityForResult(in_s, 1);
                else if (bImport == true)
                    startActivityForResult(in_i, 2);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                sDate = data.getStringExtra("DATE");
                sBreakdown = data.getStringExtra("BREAKDOWN");
                sMoney = data.getStringExtra("MONEY");
                sMethod = data.getStringExtra("METHOD");
                sCategory = data.getStringExtra("CATEGORY");
                adapter.addItem(sDate, sCategory, sBreakdown, sMoney, sMethod);

                spend_amount += Integer.parseInt(sMoney);

                db.execSQL("INSERT INTO userinfo VALUES ('" + sID + "', '" + sDate + "' , '" + sCategory + "', '" + sBreakdown + "', '" + sMoney + "', '" + sMethod + "');");

                current_amount.setText("￦" + String.valueOf(spend_amount).toString());

                adapter.notifyDataSetChanged();
            }
            else if (requestCode == 2) {
                sDate = data.getStringExtra("DATE");
                sBreakdown = data.getStringExtra("BREAKDOWN");
                sMoney = data.getStringExtra("MONEY");
                sMethod = data.getStringExtra("METHOD");
                adapter2.addItem2(sDate, sBreakdown, sMoney, sMethod);

                import_amount += Integer.parseInt(sMoney);

                db.execSQL("INSERT INTO userinfo VALUES ('" + sID + "', '" + sDate + "' , null , '" + sBreakdown + "', '" + sMoney + "', '" + sMethod + "');");

                current_amount.setText("￦" + String.valueOf(import_amount).toString());

                adapter2.notifyDataSetChanged();
            }
        }
    }
}