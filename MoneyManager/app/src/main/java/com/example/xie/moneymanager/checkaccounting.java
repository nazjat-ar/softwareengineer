package com.example.xie.moneymanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class checkaccounting extends Activity implements View.OnClickListener {
    ArrayList<Integer> al_id = new ArrayList<Integer>();
    ArrayList<String> al_consumdate = new ArrayList<String>();
    ArrayList<String> al_consumtype = new ArrayList<String>();
    ArrayList<String>  al_subconsum= new ArrayList<String>();
    ArrayList<String> al_payway = new ArrayList<String>();
    ArrayList<Integer> al_inOrout = new ArrayList<Integer>();
    ArrayList<String> al_remark = new ArrayList<String>();
    double []dl_money = new double[300];
    ImageButton img_back;
    ImageButton imgbtn_refresh;
    TextView tv_begindate;
    TextView tv_enddate;
    TabHost th;
    int year;
    int month;
    int day;
    int month_r;
    static String date_begin;
    static String date_end;
    SQLiteDatabase db;
    static Calendar cal;
    List<Map<String, Object>> datalistForcurrentdata = new ArrayList<Map<String, Object>>();
    void initial()
    {
        tv_begindate = (TextView)findViewById(R.id.tv_beginDate);
        tv_enddate = (TextView)findViewById(R.id.tv_endDate);
        th=(TabHost)findViewById(R.id.tabhost);
        th.setup(); //初始化TabHost容器
        th.addTab(th.newTabSpec("tab1").setIndicator("收支情况",null).setContent(R.id.lv_tab_inout));
        th.addTab(th.newTabSpec("tab2").setIndicator("收支详情",null).setContent(R.id.lv_tab_inoutdetail));
        th.addTab(th.newTabSpec("tab3").setIndicator("资金详情",null).setContent(R.id.lv_tab_way));
        img_back = (ImageButton)findViewById(R.id.img_back);
        imgbtn_refresh = (ImageButton)findViewById(R.id.imgbtn_refresh);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDate() {
        cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
        month_r = month+1;
    }
    void format(int year, int month, int day) {
        if (month_r < 10)
        {
            if (day < 10)
            {
                date_begin=year+"-0"+month+"-0"+day;
                date_end=year+"-0"+month+"-0"+day;
            }
            else
            {
                date_begin=year+"-0"+month+"-"+day;
                date_end=year+"-0"+month+"-"+day;
            }
        }
        else
        {
            if (day < 10)
            {
                date_begin=year+"-"+month+"-0"+day;
                date_end=year+"-"+month+"-0"+day;
            }
            else
            {
                date_begin=year+"-"+month+"-"+day;
                date_end =year+"-"+month+"-"+day;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkaccounting);
        initial();
        getDate();
        format(year, month_r, day);
        tv_begindate.setText(date_begin);
        tv_enddate.setText(date_end);
        getCurrentDatedbData();
        th.setCurrentTab(0);
        Drawgraph();
        tv_begindate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                getDate();
                new DatePickerDialog(checkaccounting.this ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        // 此处得到选择的时间，可以进行你想要的操作
                        int temp = monthOfYear+1;
                        if (temp < 10)
                        {
                            if (dayOfMonth < 10)
                            {
                                date_begin=year+"-0"+temp+"-0"+dayOfMonth;
                            }
                            else
                            {
                                date_begin=year+"-0"+temp+"-"+dayOfMonth;
                            }
                        }
                        else
                        {
                            if (dayOfMonth < 10)
                            {
                                date_begin=year+"-"+temp+"-0"+dayOfMonth;
                            }
                            else
                            {
                                date_begin=year+"-"+temp+"-"+dayOfMonth;
                            }
                        }
                        tv_begindate.setText(date_begin);

                        getCurrentDatedbData();
                        Drawgraph();
                    }
                }, year,month,day).show();
            }
        });
        tv_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
                new DatePickerDialog(checkaccounting.this ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        int temp = monthOfYear+1;
                        if (temp < 10)
                        {
                            if (dayOfMonth < 10)
                            {
                                date_end=year+"-0"+temp+"-0"+dayOfMonth;
                            }
                            else
                            {
                                date_end=year+"-0"+temp+"-"+dayOfMonth;
                            }
                        }
                        else
                        {
                            if (dayOfMonth < 10)
                            {
                                date_end=year+"-"+temp+"-0"+dayOfMonth;
                            }
                            else
                            {
                                date_end=year+"-"+temp+"-"+dayOfMonth;
                            }
                        }
                        tv_enddate.setText(date_end);
                        getCurrentDatedbData();
                        Drawgraph();
                    }
                }, year,month,day).show();
            }
        });

        th.setOnTabChangedListener(new TabHost.OnTabChangeListener()
           {
               public void onTabChanged(String tabId) {
                   Drawgraph();
               }
           });
        img_back.setOnClickListener(this);
        imgbtn_refresh.setOnClickListener(this);
    }
    void getCurrentDatedbData() //获取当前数据库数据
    {
        datalistForcurrentdata = new ArrayList<Map<String, Object>>();
        al_id = new ArrayList<Integer>();
        al_consumdate = new ArrayList<String>();
        al_subconsum = new ArrayList<String>();
        al_consumtype = new ArrayList<String>();
        al_payway = new ArrayList<String>();
        al_inOrout = new ArrayList<Integer>();
        al_remark = new ArrayList<String>();
        dl_money = new double[300];

        db = openOrCreateDatabase("record.db", MODE_PRIVATE, null);
        String sqlCreate = "create table if not exists accountingrecord" +
                "(_id integer primary key autoincrement, consumdate text," +
                "consumtype text, money double, payway text, inOrout integer, remark text)";
        //消费日期，消费类型，消费金额，支付方式，支出0或收入1, 备注
        db.execSQL(sqlCreate);

        String sqlInsert = "select * from accountingrecord where consumdate >= '"+date_begin+"' and consumdate <= '"+ date_end+"'";
        Cursor cout = db.rawQuery(sqlInsert, null);
        if (cout != null)
        {
            if (cout.moveToFirst())
            {
                do
                {
                    String d = cout.getString(cout.getColumnIndex("consumdate"));
                    Map<String, Object>map = new HashMap<String, Object>();
                    map.put("_id", cout.getInt(cout.getColumnIndex("_id")));
                    map.put("consumdate", cout.getString(cout.getColumnIndex("consumdate")));
                    map.put("subconsum",cout.getString(cout.getColumnIndex("subconsum")));
                    map.put("consumtype", cout.getString(cout.getColumnIndex("consumtype")));
                    map.put("money", cout.getDouble(cout.getColumnIndex("money")));
                    map.put("payway", cout.getString(cout.getColumnIndex("payway")));
                    map.put("inOrout", cout.getInt(cout.getColumnIndex("inOrout")));
                    map.put("remark", cout.getString(cout.getColumnIndex("remark")));
                    datalistForcurrentdata.add(map);
                }while (cout.moveToNext());
            }
        }
        db.close();
        cout.close();
        for (int k = 0; k < datalistForcurrentdata.size(); k++)
        {
            al_id.add((int) datalistForcurrentdata.get(k).get("_id"));
            al_consumdate.add((String) datalistForcurrentdata.get(k).get("consumdate"));
            al_consumtype.add((String)datalistForcurrentdata.get(k).get("consumtype"));
            al_subconsum.add((String)datalistForcurrentdata.get(k).get("subconsum"));
            al_payway.add((String) datalistForcurrentdata.get(k).get("payway"));
            dl_money[k] = Double.parseDouble(datalistForcurrentdata.get(k).get("money").toString());
            al_inOrout.add((int)datalistForcurrentdata.get(k).get("inOrout"));
            al_remark.add((String)datalistForcurrentdata.get(k).get("remark"));
        }
    }
    void Drawgraph()
    {
        PieChart piechart = (PieChart) findViewById(R.id.inout_piegraph);
        BarChart outbarChart=(BarChart)findViewById(R.id.out_detailbar);
        BarChart inbarChart=(BarChart)findViewById(R.id.in_detailbar);
        LineChart lineChart = (LineChart)findViewById(R.id.line_chart);
        PieData mPieData = getPieData();
        showChart(piechart, mPieData);
        showBarChart(outbarChart, getBarData_out(), 0);
        showBarChart(inbarChart, getBarData_in(), 1);
        showLinechart(lineChart);
    }
    //piechart
    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(45f);  //半径
        pieChart.setTransparentCircleRadius(55f); // 半透明圈
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setCenterText("收支百分比%");  //饼状图中间的文字
        pieChart.setCenterTextSize(20);
        pieChart.setDrawSliceText(false);
        pieChart.setDescription("");

        //设置数据
        pieChart.setData(pieData);
        pieChart.animateXY(1000, 1000);  //设置动画
        Legend legend = pieChart.getLegend();
        legend.setTextSize(20);
        pieChart.setUsePercentValues(true);  //显示成百分比

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                int n = highlight.getXIndex();
                if (n == 0) //支出
                {
                    Intent intent = new Intent(checkaccounting.this, detaillistview.class);
                    Bundle bundle = setBundle();

                    bundle.putInt("flag_inOrout", 0);
                    bundle.putString("flag_consumtype", "无");
                    bundle.putString("flag_payway", "无");
                    bundle.putInt("flag_flag", -1);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (n == 1) //收入
                {
                    Intent intent = new Intent(checkaccounting.this, detaillistview.class);
                    Bundle bundle = setBundle();

                    bundle.putInt("flag_inOrout", 1);
                    bundle.putString("flag_consumtype", "无");
                    bundle.putString("flag_payway", "无");
                    bundle.putInt("flag_flag", -1);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    private PieData getPieData() {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        xValues.add("支出");
        xValues.add("收入");
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        float money_in = 0;
        float money_out = 0;

        for (int i = 0; i < datalistForcurrentdata.size(); i++)
        {
            if (Integer.parseInt(datalistForcurrentdata.get(i).get("inOrout").toString()) == 0)
            {
                money_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (Integer.parseInt(datalistForcurrentdata.get(i).get("inOrout").toString()) == 1)
            {
                money_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
        }

        yValues.add(new Entry(money_out, 0));
        yValues.add(new Entry(money_in, 1));
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "收支情况"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(2f); //设置个饼状图之间的距离
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);
        pieData.setValueTextSize(20);
        pieData.setValueFormatter(new ValueFormatter() {//圆环内文字设置
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                String str = n + "%";
                if (n == 0) {
                    str = "";
                }
                return str;
            }
        });

        return pieData;
    }
    //barchart
    private void showBarChart(final BarChart barChart, BarData barData, final int inOrout) {

        barChart.setNoDataTextDescription("暂时还没有收支详情哦！");
        barChart.setData(barData); // 设置数据
        barChart.setDescription("");
        barChart.setDescriptionTextSize(15);
        barChart.setDrawBorders(false); //是否在折线图上添加边框
        barChart.setDescription("");
        barChart.setDescriptionColor(Color.RED);//数据的颜色
        barChart.setDescriptionTextSize(40);//数据字体大小
        barChart.setDrawBarShadow(false);//柱状图没有数据的部分是否显示阴影效果
        Legend legend = barChart.getLegend();
        legend.setTextSize(15);
        barChart.setTouchEnabled(true); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
        barChart.setDrawValueAboveBar(true);//柱状图上面的数值显示在柱子上面还是柱子里面
        barChart.getXAxis().setDrawGridLines(false);//是否显示竖直标尺线
        barChart.getXAxis().setLabelsToSkip(0);//设置横坐标显示的间隔
        barChart.getXAxis().setDrawLabels(true);//是否显示X轴数值
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置 默认在上方
        barChart.getAxisRight().setDrawLabels(false);//右侧是否显示Y轴数值
        barChart.getAxisRight().setEnabled(false);//是否显示最右侧竖线
        barChart.getAxisRight().setDrawAxisLine(true);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART);//设置比例图标的位置
        barChart.getLegend().setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);//设置比例图标和文字之间的位置方向
        barChart.getLegend().setTextColor(Color.RED);
        barChart.getAxisLeft().setTextSize(15);
        barChart.getXAxis().setTextSize(15);

        final String []str_out = {"吃饭", "购物", "水电费", "交通", "其它"};
        final String []str_in = {"职业收入", "投资收入", "礼金收入", "利息收入", "其它"};

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                int n = highlight.getXIndex();
                String consumtype = new String();
                int flag_flag = 0;
                if (inOrout == 0) //支出
                {
                    consumtype = str_out[n];
                    flag_flag = 0;
                }
                else if (inOrout == 1)//收入
                {
                    consumtype = str_in[n];
                    flag_flag = 1;
                }


                Intent intent = new Intent(checkaccounting.this, detaillistview.class);
                Bundle bundle = setBundle();
                /*
                *用于在detailistview中识别点击的哪个图表的哪一个位置
                 */
                bundle.putInt("flag_inOrout", -1);
                bundle.putString("flag_consumtype", consumtype);
                bundle.putString("flag_payway", "无");
                bundle.putInt("flag_flag", flag_flag);


                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        barChart.animateXY(1000,1500);
    }
    private BarData getBarData_out() {
        float money_eatting = 0;
        float money_shopping = 0;
        float money_sd = 0;
        float money_transport = 0;
        float money_else_out = 0;
        for (int i = 0; i < datalistForcurrentdata.size(); i++)
        {
            if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("吃饭") == 0)
            {
                money_eatting+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("购物") == 0)
            {
                money_shopping+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("水电费") == 0)
            {
                money_sd+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("交通") == 0)
            {
                money_transport+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (Integer.parseInt(datalistForcurrentdata.get(i).get("inOrout").toString()) == 0)
            {
                money_else_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
        }

        ArrayList<String> xValues = new ArrayList<String>();
        String []str_out_detail = {"吃饭", "购物", "水电费", "交通", "其它"};
        for (int i = 0; i <5; i++)
        {
            xValues.add(str_out_detail[i]);
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(money_eatting, 0));
        yValues.add(new BarEntry(money_shopping, 1));
        yValues.add(new BarEntry(money_sd, 2));
        yValues.add(new BarEntry(money_transport, 3));
        yValues.add(new BarEntry(money_else_out, 4));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");

        barDataSet.setBarSpacePercent(40);
        barDataSet.setVisible(true);//是否显示柱状图柱子

        ArrayList<Integer> colors_bar = new ArrayList<Integer>();
        colors_bar.add(Color.rgb(255, 94, 94));
        barDataSet.setColors(colors_bar);

        barDataSet.setDrawValues(true);//是否显示柱子上面的数值

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);
        barData.setValueTextSize(15);
        barData.setValueFormatter(new ValueFormatter() {//圆环内文字设置
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                String str = "-"+n+"元";
                if (n == 0) {
                    str = "";
                }
                return str;
            }
        });
        return barData;
    }
    private BarData getBarData_in() {
        float money_salary = 0;
        float money_reward = 0;
        float money_lottery = 0;
        float money_parttimejob = 0;
        float money_else_in = 0;
        for (int i = 0; i < datalistForcurrentdata.size(); i++)
        {
            if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("职业收入") == 0)
            {
                money_salary+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("投资收入") == 0)
            {
                money_reward+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("礼金收入") == 0)
            {
                money_lottery+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (datalistForcurrentdata.get(i).get("consumtype").toString().compareTo("利息收入") == 0)
            {
                money_parttimejob+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
            else if (Integer.parseInt(datalistForcurrentdata.get(i).get("inOrout").toString()) == 1)
            {
                money_else_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
            }
        }

        ArrayList<String> xValues = new ArrayList<String>();
        String []str_in_detail = {"职业收入", "投资收入", "礼金收入", "利息收入", "其它"};
        for (int i = 0; i <5; i++)
        {
            xValues.add(str_in_detail[i]);
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(money_salary, 0));
        yValues.add(new BarEntry(money_reward, 1));
        yValues.add(new BarEntry(money_lottery, 2));
        yValues.add(new BarEntry(money_parttimejob, 3));
        yValues.add(new BarEntry(money_else_in, 4));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");

        barDataSet.setBarSpacePercent(40);
        barDataSet.setVisible(true);//是否显示柱状图柱子
        ArrayList<Integer> colors_bar = new ArrayList<Integer>();
        colors_bar.add(Color.rgb(97, 163, 201));
        barDataSet.setColors(colors_bar);
        barDataSet.setDrawValues(true);//是否显示柱子上面的数值

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);
        barData.setValueTextSize(15);
        barData.setValueFormatter(new ValueFormatter() {//圆环内文字设置
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                String str = n+"元";
                if (n == 0) {
                    str = "";
                }
                return str;
            }
        });
        return barData;
    }
    //linechart
    void showLinechart(LineChart lineChart)
    {
        float cash_in = 0;
        float cash_out = 0;
        float card_in = 0;
        float card_out = 0;
        float weixin_in = 0;
        float weixin_out = 0;
        float zfb_in = 0;
        float zfb_out = 0;
        float else_in = 0;
        float else_out = 0;
        int i;
        for (i = 0; i < datalistForcurrentdata.size(); i++)
        {
            if (Integer.parseInt(datalistForcurrentdata.get(i).get("inOrout").toString()) == 0) //支出
            {
                if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("现金") == 0)
                {
                    cash_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("支付宝") == 0)
                {
                    zfb_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("微信") == 0)
                {
                    weixin_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("银行卡") == 0)
                {
                    card_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else
                {
                    else_out+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
            }
            else if (Integer.parseInt(datalistForcurrentdata.get(i).get("inOrout").toString()) == 1)
            {
                if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("现金") == 0)
                {
                    cash_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("支付宝") == 0)
                {
                    zfb_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("微信") == 0)
                {
                    weixin_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else if (datalistForcurrentdata.get(i).get("payway").toString().compareTo("银行卡") == 0)
                {
                    card_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
                else
                {
                    else_in+=Float.parseFloat(datalistForcurrentdata.get(i).get("money").toString());
                }
            }

        }

        //设置x轴数据
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add("现金");
        xValues.add("银行卡");
        xValues.add("支付宝");
        xValues.add("微信");
        xValues.add("其它");

        //设置y轴的数据
        ArrayList<Entry> yValue = new ArrayList<>();
        yValue.add(new Entry(cash_out, 0));
        yValue.add(new Entry(card_out, 1));
        yValue.add(new Entry(zfb_out, 2));
        yValue.add(new Entry(weixin_out, 3));
        yValue.add(new Entry(else_out, 4));
        //设置第二条折线y轴的数据
        ArrayList<Entry> yValue1 = new ArrayList<>();
        yValue1.add(new Entry(cash_in, 0));
        yValue1.add(new Entry(card_in, 1));
        yValue1.add(new Entry(zfb_in, 2));
        yValue1.add(new Entry(weixin_in, 3));
        yValue1.add(new Entry(else_in, 4));

        //创建两条折线的图表
        new LineChartManager().initDoubleLineChart(checkaccounting.this,lineChart,xValues,yValue,yValue1);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            String str[] = {"现金", "银行卡", "支付宝", "微信", "其它"};
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                int n = highlight.getXIndex();
                int inOrout = highlight.getDataSetIndex(); //支出0，收入1
                Intent intent = new Intent(checkaccounting.this, detaillistview.class);
                Bundle bundle = setBundle();
                bundle.putInt("flag_inOrout", -1);
                bundle.putString("flag_consumtype", "无");
                bundle.putString("flag_payway", str[n]);
                bundle.putInt("flag_flag", inOrout);

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    Bundle setBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("_id", al_id);
        bundle.putIntegerArrayList("inOrout", al_inOrout);
        bundle.putDoubleArray("money", dl_money);
        bundle.putStringArrayList("consumdate", al_consumdate);
        bundle.putStringArrayList("subconsum",al_subconsum);
        bundle.putStringArrayList("consumtype", al_consumtype);
        bundle.putStringArrayList("payway", al_payway);
        bundle.putStringArrayList("remark", al_remark);
        return bundle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_back:finish();break;
            case R.id.imgbtn_refresh:
                {
                    getCurrentDatedbData();
                    Drawgraph();
                    break;
                }
        }

    }
}




