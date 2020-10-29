package com.example.xie.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addAccounting extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist;
    ListView listView_addaccounting;
    Button btn_addaccounting;
    SQLiteDatabase db;
    ImageButton img_back;
    ImageButton imgbtn_refresh;
    TextView tv_todaydetail;
    int currentposition = 0;
    double todaytotal_in = 0;
    double todaytotal_out = 0;
    public void initial() {
        listView_addaccounting = (ListView) findViewById(R.id.lv_addaccounting);
        datalist = new ArrayList<Map<String, Object>>();
        btn_addaccounting = (Button) findViewById(R.id.btn_addaccounting);
        tv_todaydetail = (TextView)findViewById(R.id.tv_todaydetail);
        img_back = (ImageButton)findViewById(R.id.img_back);
        imgbtn_refresh = (ImageButton)findViewById(R.id.imgbtn_refresh);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accounting);
        initial();
        int[] item_id = {R.id.tv_mainlistviewitem_out, R.id.tv_mainlistviewitem_in, R.id.img_center};
        String[] item_name = {"out", "in", "img", "line_up", "line_down"};
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.addaccountinglistviewitem, item_name, item_id);
        listView_addaccounting.setAdapter(sim_adapter);
        listView_addaccounting.setOnItemClickListener(this);
        btn_addaccounting.setOnClickListener(this);
        img_back.setOnClickListener(this);
        imgbtn_refresh.setOnClickListener(this);
        //长按弹出菜单
        listView_addaccounting.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener()
        {
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
            {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                currentposition = info.position;
                menu.add(0, 0, 0, "删 除");
            }
        });

    }
    public boolean onContextItemSelected(MenuItem item) {
        deleteItem();
        return true;
    }

    private List<Map<String, Object>> getData() {
        todaytotal_in = 0;
        todaytotal_out = 0;
        datalist = new ArrayList<Map<String, Object>>();

        db = openOrCreateDatabase("record.db", MODE_PRIVATE, null);
        String sqlCreate = "create table if not exists accountingrecord" +
                "(_id integer primary key autoincrement, consumdate text," +
                "consumtype text,subconsum text, money double, payway text, inOrout integer, remark text)";
        //消费日期，消费类型，消费金额，支付方式，支出0或收入1, 备注
        db.execSQL(sqlCreate);

        String sqlInsert = "select * from accountingrecord where consumdate = date('now')";
        Cursor c = db.rawQuery(sqlInsert, null); //提取所有今天支出项
        if (c != null)
        {
            c.moveToFirst();
        }
        if (c.moveToFirst())
        {
            do
            {
                Map<String, Object> map = new HashMap<String, Object>();
                int inOrout = c.getInt(c.getColumnIndex("inOrout"));
                int id = c.getInt(c.getColumnIndex("_id"));
                String consumdate = c.getString(c.getColumnIndex("consumdate"));
                String consumtype = c.getString(c.getColumnIndex("consumtype"));
                String subconsum = c.getString(c.getColumnIndex("subconsum"));
                String payway = c.getString(c.getColumnIndex("payway"));
                double money = c.getDouble(c.getColumnIndex("money"));
                map.put("id", id);
                map.put("consumdate", consumdate);
                map.put("consumtype", consumtype);
                map.put("subconsum",subconsum);
                map.put("money", money);
                map.put("payway", payway);
                if (inOrout == 0) //支出
                {
                    map.put("out", consumtype+"->"+subconsum+"  -"+money+"￥");
                    map.put("in", "");
                    todaytotal_out = todaytotal_out+money;
                    map.put("img", R.drawable.round);
                }
                else if (inOrout == 1) //收入
                {
                    map.put("in", consumtype+"->"+subconsum+"   "+money+"￥");
                    map.put("out", "");
                    todaytotal_in = todaytotal_in+money;
                    map.put("img", R.drawable.round1);
                }
                map.put("line_up", R.id.img_line_up);
                map.put("line_down", R.id.img_line_down);

                tv_todaydetail.setText("支出"+todaytotal_out+"元  收入"+todaytotal_in+"元");
                datalist.add(map);
            }while (c.moveToNext());
        }
        return datalist;
    }

    //listView.invalidateViews(); 刷新listview
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //列表项点击事件
        currentposition = position;
    }

    private void deleteItem() {
        int id = (int) datalist.get(currentposition).get("id");
        String sqlDelete = "delete from accountingrecord where _id="+id;
        db = openOrCreateDatabase("record.db", MODE_PRIVATE, null);
        db.execSQL(sqlDelete);
        datalist.remove(currentposition);
        sim_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_addaccounting:
            {
                Intent intent = new Intent(addAccounting.this, addNewOne.class);
                startActivityForResult(intent,0);
                break;
            }
            case R.id.img_back:finish();break;
            case R.id.imgbtn_refresh:
            {
                int[] item_id = {R.id.tv_mainlistviewitem_out, R.id.tv_mainlistviewitem_in, R.id.img_center};
                String[] item_name = {"out", "in", "img", "line_up", "line_down"};
                sim_adapter = new SimpleAdapter(this, getData(), R.layout.addaccountinglistviewitem, item_name, item_id);
                listView_addaccounting.setAdapter(sim_adapter);
                Toast.makeText(addAccounting.this, "账目已更新", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        imgbtn_refresh.performClick();
    }
}