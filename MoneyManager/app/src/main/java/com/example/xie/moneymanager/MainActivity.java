package com.example.xie.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    SimpleAdapter sim_adapter;//接受点击
    List<Map<String, Object>>datalist;//
    ListView listView_mainmenu;
    Animation animation;
    ImageView img_beginword;


    public void initial()
    {
        listView_mainmenu = (ListView)findViewById(R.id.lv_mainmenu);
        datalist = new ArrayList<Map<String, Object>>();
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_anim);
        img_beginword = (ImageView)findViewById(R.id.img_beginword);

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // startActivity(new Intent(MainActivity.this, LoginActivity.class));
        initial();
        int[]item_id = {R.id.tv_mainlistitem, R.id.img_mainlistitem};
        String[]item_name={"text", "img"};
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.main_listviewitem, item_name, item_id);
        listView_mainmenu.setAdapter(sim_adapter);
        listView_mainmenu.setOnItemClickListener(this);
        animation.setDuration(2500);
        img_beginword.startAnimation(animation);

    }

    private List<Map<String, Object>> getData()
    {

        Map<String, Object>map1 = new HashMap<String, Object>();
        map1.put("text", "记账");
        map1.put("img", R.drawable.ic_add_active);
        datalist.add(map1);
        Map<String, Object>map2 = new HashMap<String, Object>();
        map2.put("text", "算账");
        map2.put("img", R.drawable.ic_suanpan_default);
        datalist.add(map2);
        return datalist;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
            {
                datalist.get(0).clear();
                datalist.get(0).put("text", "记账");
                datalist.get(0).put("img", R.drawable.ic_add_active);
                datalist.get(1).clear();
                datalist.get(1).put("text", "算账");
                datalist.get(1).put("img", R.drawable.ic_suanpan_default);
                sim_adapter.notifyDataSetChanged();
                startActivity(new Intent(MainActivity.this, addAccounting.class));break;
            }
            case 1:
            {
                datalist.get(0).clear();
                datalist.get(0).put("text", "记账");
                datalist.get(0).put("img", R.drawable.ic_add_active);
                datalist.get(1).clear();
                datalist.get(1).put("text", "算账");
                datalist.get(1).put("img", R.drawable.ic_suanpan_default);
                sim_adapter.notifyDataSetChanged();
                startActivity(new Intent(MainActivity.this, checkaccounting.class));break;
            }
        }
    }
}
