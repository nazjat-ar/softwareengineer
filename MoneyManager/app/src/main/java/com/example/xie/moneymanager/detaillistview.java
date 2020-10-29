package com.example.xie.moneymanager;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detaillistview extends Activity {
    SimpleAdapter sim_adapter;
    ListView lv;
    SQLiteDatabase db;
    List<Map<String, Object>> datalistForcurrentdata;
    ArrayList<Integer> al_id = new ArrayList<Integer>();
    ArrayList<String> al_consumdate = new ArrayList<String>();
    ArrayList<String> al_consumtype = new ArrayList<String>();
    ArrayList<String> al_subconsum = new ArrayList<String>();
    ArrayList<String> al_payway = new ArrayList<String>();
    ArrayList<Integer> al_inOrout = new ArrayList<Integer>();
    ArrayList<String> al_remark = new ArrayList<String>();
    double []dl_money = new double[300];
    int length;
    int flag_inOrout = 0;
    String flag_consumtype = new String();
    String flag_payway = new String();
    int flag_flag = 0;
    int currentposition;
    int lastposition;
    int flag_isclickforthefirsttime = 0; //0：从来没有点击过，1表示已经点击过
    void getvalue()
    {
        Bundle bundle = this.getIntent().getExtras();
        al_inOrout = bundle.getIntegerArrayList("inOrout");
        al_id = bundle.getIntegerArrayList("_id");
        al_consumdate = bundle.getStringArrayList("consumdate");
        al_payway = bundle.getStringArrayList("payway");
        al_consumtype = bundle.getStringArrayList("consumtype");
        al_subconsum  = bundle.getStringArrayList("subconsum");
        dl_money = bundle.getDoubleArray("money");
        al_remark = bundle.getStringArrayList("remark");
        flag_inOrout = bundle.getInt("flag_inOrout");
        flag_consumtype = bundle.getString("flag_consumtype");
        flag_payway = bundle.getString("flag_payway");
        flag_flag = bundle.getInt("flag_flag");
    }
    void  initial()
    {
        length = al_consumdate.size();
        lv = (ListView)findViewById(R.id.lv_detail);
        datalistForcurrentdata = new ArrayList<Map<String, Object>>();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaillistview);

        getvalue();
        initial();
        int[]item_id = {R.id.tv_zhanwei, R.id.tv_li, R.id.tv_li_more};
        String[]item_name={"item1", "item2", "item3"};
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.normallistviewitem, item_name, item_id);
        lv.setAdapter(sim_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) datalistForcurrentdata.get(position).get("index");
                if (flag_isclickforthefirsttime == 0)
                {
                    lastposition = position;
                    currentposition = position;
                    datalistForcurrentdata.get(currentposition).put("item3",  "日期："+
                            al_consumdate.get(index)+"\n"+"方式："+al_payway.get(index)+"\n"+
                            "备注："+al_remark.get(index));
                    flag_isclickforthefirsttime = 1;
                }
                else
                {
                    lastposition = currentposition;
                    currentposition = position;
                    datalistForcurrentdata.get(lastposition).put("item3", "");
                    datalistForcurrentdata.get(currentposition).put("item3",  "日期："+
                            al_consumdate.get(index)+"\n"+"方式："+al_payway.get(index)+"\n"+
                            "备注："+al_remark.get(index));

                }
                sim_adapter.notifyDataSetChanged();
            }
        });
        lv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener()
        {
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
            {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                currentposition = info.position;
                menu.add(0, 0, 0, "删 除");
            }
        });
    }
    List<Map<String, Object>> getData() {
        datalistForcurrentdata = new ArrayList<Map<String, Object>>();

        if (flag_inOrout != -1)
        {
            if (flag_inOrout == 0) //支出
            {
                for (int i = 0; i < length; i++)
                {
                    if (al_inOrout.get(i) == 0)
                    {
                        Map<String, Object>map = new HashMap<String, Object>();
                        map.put("item2", al_consumtype.get(i)+al_subconsum.get(i)  +"  ￥"+dl_money[i]);
                        map.put("id", al_id.get(i));
                        map.put("index", i);
                        datalistForcurrentdata.add(map);
                    }
                }
            }
            else if (flag_inOrout == 1)
            {
                for (int i = 0; i < length; i++)
                {
                    if (al_inOrout.get(i) == 1)
                    {
                        Map<String, Object>map = new HashMap<String, Object>();
                        map.put("item2", al_consumtype.get(i)+al_subconsum.get(i) +"  ￥"+dl_money[i]);
                        map.put("id", al_id.get(i));
                        map.put("index", i);
                        datalistForcurrentdata.add(map);
                    }
                }
            }
        }
        else if (flag_consumtype.compareTo("无") != 0)
        {
            for (int i = 0; i < length; i++)
            {
                if (al_inOrout.get(i) == flag_flag)
                {
                    if (flag_flag == 0&&flag_consumtype.compareTo("其它")==0&&al_consumtype.get(i).compareTo("吃饭")!=0 &&
                            al_consumtype.get(i).compareTo("购物")!=0&& al_consumtype.get(i).compareTo("水电费")!=0&&
                            al_consumtype.get(i).compareTo("交通")!=0||flag_flag == 1&&flag_consumtype.compareTo("其它")==0&&
                            al_consumtype.get(i).compareTo("职业收入")!=0 &&
                            al_consumtype.get(i).compareTo("投资收入")!=0&& al_consumtype.get(i).compareTo("礼金收入")!=0&&
                            al_consumtype.get(i).compareTo("利息收入")!=0)
                    {
                        Map<String, Object>map = new HashMap<String, Object>();
                        map.put("item2", al_consumtype.get(i)+al_subconsum.get(i) +"  ￥"+dl_money[i]);
                        map.put("id", al_id.get(i));
                        map.put("index", i);
                        datalistForcurrentdata.add(map);
                    }
                    else if(al_consumtype.get(i).compareTo(flag_consumtype) == 0)
                    {
                        Map<String, Object>map = new HashMap<String, Object>();
                        map.put("item2", al_consumtype.get(i)+al_subconsum.get(i) +"  ￥"+dl_money[i]);
                        map.put("id", al_id.get(i));
                        map.put("index", i);
                        datalistForcurrentdata.add(map);
                    }
                }
            }
        }
        else if (flag_payway.compareTo("无") != 0)
        {
            for (int i = 0; i < length; i++)
            {
                if(al_inOrout.get(i) == flag_flag)
                {
                    if (flag_payway.compareTo("其它")==0&&al_payway.get(i).compareTo("现金")!=0 &&
                            al_payway.get(i).compareTo("微信")!=0&& al_payway.get(i).compareTo("支付宝")!=0&&
                            al_payway.get(i).compareTo("银行卡")!=0)
                    {
                        Map<String, Object>map = new HashMap<String, Object>();
                        map.put("item2", al_consumtype.get(i) +"  ￥"+dl_money[i]);
                        map.put("id", al_id.get(i));
                        map.put("index", i);
                        datalistForcurrentdata.add(map);
                    }
                    else if (al_payway.get(i).compareTo(flag_payway) == 0)
                    {
                        Map<String, Object>map = new HashMap<String, Object>();
                        map.put("item2", al_consumtype.get(i) +"  ￥"+dl_money[i]);
                        map.put("id", al_id.get(i));
                        map.put("index", i);
                        datalistForcurrentdata.add(map);
                    }
                }
            }
        }
        return datalistForcurrentdata;
    }
    public boolean onContextItemSelected(MenuItem item) {
        deleteItem();
        return true;
    }
    private void deleteItem() {
        int id = (int) datalistForcurrentdata.get(currentposition).get("id");
        String sqlDelete = "delete from accountingrecord where _id="+id;
        db = openOrCreateDatabase("record.db", MODE_PRIVATE, null);
        String sqlCreate = "create table if not exists accountingrecord" +
                "(_id integer primary key autoincrement, consumdate text," +
                "consumtype text, sec_text text,money double, payway text, inOrout integer, remark text)";
        //消费日期，消费类型，消费金额，支付方式，支出0或收入1, 备注
        db.execSQL(sqlCreate);
        db.execSQL(sqlDelete);
        datalistForcurrentdata.remove(currentposition);
        sim_adapter.notifyDataSetChanged();
    }
}
