package com.example.xie.moneymanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class addNewOne<pubilc> extends Activity implements View.OnClickListener{
    private AlertDialog alertDialog2; //单选框
    final String[] items1 = {"早餐", "晚餐", "午餐", "宵夜","其他"};
    final String[] items2 = {"食物", "衣服", "文娱", "人情礼品","其他"};
    final String[] items3 = {"家庭", "其他"};
    final String[] items4 = {"公交车", "出租", "飞机票", "火车票","其他"};
    final String[] items5 = {"年终奖","工资","其他"};
    final String[] items6 = {"股票","期货","其他"};
    final String[] items7 = {"婚庆","节日","其他"};
    final String[] items8 = {"房租","银行存款","其他"};
    TabHost th;
    TextView tv_choosedate; //选择日期
    Button btn_submit; //提交到数据库
    //支出
    EditText et_remark; //备注
    EditText et_consummoney; //消费金额
    ImageButton btn_eatting; //吃饭
    ImageButton btn_shopping; //购物
    ImageButton btn_waterandelectricity; //水电费
    ImageButton btn_transport; //交通费
    ImageButton btn_elseconsumtype; //其它消费类型
    ImageButton btn_cash; //现金支付
    ImageButton btn_zfb; //支付宝支付
    ImageButton btn_weixin; //微信
    ImageButton btn_card; //银行卡
    ImageButton btn_elsepayway; //其它支付方式
    String str_remark; //备注
    String consumtype = "";
    String payway = "";
    //收入
    EditText et_in_remark; //备注
    EditText et_in_earnmoney;; //收入金额
    ImageButton btn_in_salary; //工资
    ImageButton btn_in_remark; //奖金
    ImageButton btn_in_lottery; //彩票
    ImageButton btn_in_parttimejob; //兼职
    ImageButton btn_in_elsetype; //其它收入类型
    ImageButton btn_in_cash; //现金
    ImageButton btn_in_zfb; //支付宝
    ImageButton btn_in_weixin; //微信
    ImageButton btn_in_card; //银行卡
    ImageButton btn_in_elseway; //其它收入方式
    String in_str_remark; //备注
    String in_consumtype = "";
    String in_payway = "";
    double in_d_money; //消费金额
    ImageButton imgbtn_back, btn_refresh;
    View v_current;
    View v_last;
    private String sec_text;
    int flag_isthefirstclick = 0; //如果为0表示从未点击过按钮，否则表示已经点击过
    String datestr; //日历中获取到的日期
    double d_money; //消费金额
    int year; //年
    int month;
    int day; //月日
    int month_r;
    int inOrout_tabfocus; //获取上一个tab页面指向收入还是支出 0代表收入，1代表支出
    Calendar cal;
    SQLiteDatabase db;
    void initial() {
        th = (TabHost) findViewById(R.id.tabhost);
        tv_choosedate = (TextView)findViewById(R.id.tv_choosedate);
        btn_submit = (Button)findViewById(R.id.btn_addnewone_submit);
        et_remark = (EditText) findViewById(R.id.et_remarks);
        et_consummoney = (EditText)findViewById(R.id.et_consummoney);
        et_in_earnmoney = (EditText)findViewById(R.id.et_in_earnmoney);
        et_in_remark = (EditText)findViewById(R.id.et_in_remarks);
        btn_refresh = findViewById(R.id.imgbtn_refresh);
        ////////////////支出/////////////////
        //消费类型

        btn_eatting = (ImageButton)findViewById(R.id.btn_eating);
        btn_shopping = (ImageButton)findViewById(R.id.btn_shoping);
        btn_waterandelectricity = (ImageButton)findViewById(R.id.btn_waterandelectricity);
        btn_transport = (ImageButton)findViewById(R.id.btn_transport);
        btn_elseconsumtype = (ImageButton)findViewById(R.id.btn_elseconsumtype);
        //支付方式
        btn_cash = (ImageButton)findViewById(R.id.btn_cash);
        btn_zfb = (ImageButton)findViewById(R.id.btn_zfb);
        btn_weixin = (ImageButton)findViewById(R.id.btn_weixin);
        btn_card = (ImageButton)findViewById(R.id.btn_card);
        btn_elsepayway = (ImageButton)findViewById(R.id.btn_elsepayway);
        ///////////////收入//////////////////
        //收入类型
        btn_in_salary = (ImageButton)findViewById(R.id.btn_in_salary);
        btn_in_lottery = (ImageButton)findViewById(R.id.btn_in_lottery);
        btn_in_remark = (ImageButton)findViewById(R.id.btn_in_remark);
        btn_in_parttimejob = (ImageButton)findViewById(R.id.btn_in_parttimejob);
        btn_in_elsetype = (ImageButton)findViewById(R.id.btn_in_elsetype);
        //收入方式
        btn_in_cash = (ImageButton)findViewById(R.id.btn_in_cash);
        btn_in_zfb = (ImageButton)findViewById(R.id.btn_in_zfb);
        btn_in_weixin = (ImageButton)findViewById(R.id.btn_in_weixin);
        btn_in_card = (ImageButton)findViewById(R.id.btn_in_card);
        btn_in_elseway = (ImageButton)findViewById(R.id.btn_in_elseway);
        //tab
        th.setup(); //初始化TabHost容器
        th.addTab(th.newTabSpec("tab1").setIndicator("支出", null).setContent(R.id.lv_tab1));
        th.addTab(th.newTabSpec("tab2").setIndicator("收入", null).setContent(R.id.lv_tab2));

        imgbtn_back = (ImageButton)findViewById(R.id.img_back);
    }
    void setlistener() {
        btn_eatting.setOnClickListener(this);
        btn_shopping.setOnClickListener(this);
        btn_waterandelectricity.setOnClickListener(this);
        btn_transport.setOnClickListener(this);
        btn_elseconsumtype.setOnClickListener(this);

        btn_cash.setOnClickListener(this);
        btn_zfb.setOnClickListener(this);
        btn_weixin.setOnClickListener(this);
        btn_card.setOnClickListener(this);
        btn_elsepayway.setOnClickListener(this);

        btn_in_elseway.setOnClickListener(this);
        btn_in_cash.setOnClickListener(this);
        btn_in_card.setOnClickListener(this);
        btn_in_weixin.setOnClickListener(this);
        btn_in_zfb.setOnClickListener(this);

        btn_in_remark.setOnClickListener(this);
        btn_in_salary.setOnClickListener(this);
        btn_in_parttimejob.setOnClickListener(this);
        btn_in_lottery.setOnClickListener(this);
        btn_in_elsetype.setOnClickListener(this);

        imgbtn_back.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_one);
        initial();
        getDate();
        setlistener();
        tv_choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        month_r = month+1;
                        datetype(year, month_r, dayOfMonth);
                        tv_choosedate.setText("<  "+datestr+"  >");
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(addNewOne.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (submitcheck() != 0)
                {
                    insertrecordintodb();
                    Toast.makeText(addNewOne.this, "账目已成功提交哦！", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }
    void insertrecordintodb() {
        db = openOrCreateDatabase("record.db", MODE_PRIVATE, null);
        String sqlCreate = "create table if not exists accountingrecord" +
                "(_id integer primary key autoincrement, consumdate text," +
                "consumtype text, sec_text text,money double,payway text, inOrout integer, remark text)";
        //消费日期，消费类型，消费金额，支付方式，支出0或收入1, 备注
        db.execSQL(sqlCreate);
        String sqlInsert = new String();
        if (inOrout_tabfocus == 0)
        {
            sqlInsert = "insert into accountingrecord (consumdate, consumtype,subconsum, money, payway, inOrout, remark) values " +
                    "('"+datestr+"', "+"'"+consumtype+"', '"+sec_text+"', "+d_money+", '"+payway+"', "+inOrout_tabfocus+", '"+str_remark+"'"+")";
        }
        else if (inOrout_tabfocus == 1)
        {
            sqlInsert = "insert into accountingrecord (consumdate, consumtype, subconsum,money, payway, inOrout, remark) values " +
                    "('"+datestr+"', "+"'"+in_consumtype+"', '"+sec_text+"', "+in_d_money+", '"+in_payway+"', "+inOrout_tabfocus+", '"+in_str_remark+"'"+")";
        }
        db.execSQL(sqlInsert);
        db.close();
    }
    public void onClick(View v) {
        if (flag_isthefirstclick == 0)
        {
            v_current = v;
            v_last = v;
        }
        else
        {
            v_last = v_current;
            v_current = v;
        }
        setbuttonstyle();
        switch (v.getId())
        {
            //支出
            case R.id.btn_eating:consumtype="吃饭";
                showSingleAlertDialog(items1);
            break;
            case R.id.btn_shoping:consumtype="购物";
                showSingleAlertDialog(items2);
                break;
            case R.id.btn_waterandelectricity:consumtype="水电费";
                showSingleAlertDialog(items3);
                break;
            case R.id.btn_transport:consumtype="交通";
                showSingleAlertDialog(items4);
                break;
            case R.id.btn_elseconsumtype:selfinput(0);
                break;

            case R.id.btn_cash:payway="现金";break;
            case R.id.btn_zfb:payway="支付宝";break;
            case R.id.btn_weixin:payway="微信";break;
            case R.id.btn_card:payway="银行卡";break;
            case R.id.btn_elsepayway:selfinput(1);break;

            //收入
            case R.id.btn_in_salary:in_consumtype="职业收入";
                showSingleAlertDialog(items5);
                break;
            case R.id.btn_in_parttimejob:in_consumtype="利息收入";
                showSingleAlertDialog(items8);
                break;
            case R.id.btn_in_lottery:in_consumtype="礼金收入";
                showSingleAlertDialog(items7);
                break;
            case R.id.btn_in_remark:in_consumtype="投资收入";
                showSingleAlertDialog(items6);
                break;
            case R.id.btn_in_elsetype:selfinput(0);
            break;

            case R.id.btn_in_card:in_payway = "银行卡";break;
            case R.id.btn_in_cash:in_payway = "现金";break;
            case R.id.btn_in_weixin:in_payway="微信";break;
            case R.id.btn_in_zfb:in_payway="支付宝";break;
            case R.id.btn_in_elseway:selfinput(1);break;

            case R.id.img_back:finish();
        }
    }


    public void selfinput(final int flag) {//flag:0表示消费类型，flag:1表示支付方式
        inOrout_tabfocus = th.getCurrentTab();
        final EditText inputServer = new EditText(addNewOne.this);
        String str = new String();
        if (inOrout_tabfocus == 0)
        {
            if(flag == 0)
            {
                str = "请输入其它消费内容";
                inputServer.setText(consumtype);
            }
            else if (flag == 1)
            {
                str = "请输入其它支付方式";
                inputServer.setText(payway);
            }
        }
        else if (inOrout_tabfocus == 1)
        {
            if(flag == 0)
            {
                str = "请输入其它收入内容";
                inputServer.setText(in_consumtype);
            }
            else if (flag == 1)
            {
                str = "请输入其它收入方式";
                inputServer.setText(in_payway);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(addNewOne.this);
        builder.setTitle(str).setIcon(R.drawable.r).setView(inputServer).setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                String checkstrInput = inputServer.getText().toString();
                if (checkstrInput.length()>4)
                {
                    Toast.makeText(addNewOne.this, "不超过四个字哦~", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (checkstrInput.isEmpty())
                {
                    Toast.makeText(addNewOne.this, "输入为空哦~", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inOrout_tabfocus == 0)
                {
                    if (flag == 0)
                    {
                        consumtype=checkstrInput;
                    }
                    else if (flag == 1)
                    {
                        payway=checkstrInput;
                    }
                }
                else if (inOrout_tabfocus == 1)
                {
                    if (flag == 0)
                    {
                        in_consumtype=checkstrInput;
                    }
                    else if (flag == 1)
                    {
                        in_payway=checkstrInput;
                    }
                }
            }
        });
        builder.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
        month_r = month+1;
        if (month_r < 10)
        {
            if (day < 10)
            {
                datestr=year+"-0"+month_r+"-0"+day;
            }
            else
            {
                datestr=year+"-0"+month_r+"-"+day;
            }
        }
        else
        {
            if (day < 10)
            {

                datestr=year+"-"+month_r+"-0"+day;
            }
            else
            {
                datestr=year+"-"+month_r+"-"+day;
            }
        }
        tv_choosedate.setText("<  "+year+" - "+month_r+" - "+day+"  >");
    }
    void datetype(int year, int month_r, int dayOfMonth) {
        if (month_r < 10)
        {
            if (dayOfMonth < 10)
            {
                datestr=year+"-0"+month_r+"-0"+dayOfMonth;
            }
            else
            {
                datestr=year+"-0"+month_r+"-"+dayOfMonth;
            }
        }
        else
        {
            if (dayOfMonth < 10)
            {
                datestr=year+"-"+month_r+"-0"+dayOfMonth;
            }
            else
            {
                datestr=year+"-"+month_r+"-"+dayOfMonth;
            }
        }
    }
    int submitcheck() {
        inOrout_tabfocus = th.getCurrentTab();
        if (inOrout_tabfocus == 0)
        {
            String s = et_consummoney.getText().toString();
            if (!s.isEmpty())
            {
                d_money = Double.parseDouble(et_consummoney.getText().toString());
            }
            str_remark = ""; //初始化为空
            str_remark = et_remark.getText().toString(); //获取备注

            if (d_money == 0)
            {
                Toast.makeText(addNewOne.this, "请填写消费金额哦！", Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (consumtype.isEmpty())
            {
                Toast.makeText(addNewOne.this, "请选择一个消费类型哦！", Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (payway.isEmpty())
            {
                Toast.makeText(addNewOne.this, "请选择一个支付方式哦！", Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (str_remark.length()>15)
            {
                Toast.makeText(addNewOne.this, "不超过15个字哦~", Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (str_remark.isEmpty())
            {
                str_remark = "无";
            }
        }
        else if (inOrout_tabfocus == 1)
        {
            String s = et_in_earnmoney.getText().toString();
            if (!s.isEmpty())
            {
                in_d_money = Double.parseDouble(et_in_earnmoney.getText().toString());
            }
            in_str_remark = ""; //初始化为空
            in_str_remark = et_in_remark.getText().toString(); //获取备注
            if (in_d_money == 0)
            {
                Toast.makeText(addNewOne.this, "请填写收入金额哦！",Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (in_consumtype.isEmpty())
            {
                Toast.makeText(addNewOne.this, "请选择一个收入类型哦！", Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (in_payway.isEmpty())
            {
                Toast.makeText(addNewOne.this, "请选择一个收入方式哦！", Toast.LENGTH_SHORT).show();
                return 0;
            }
            if (in_str_remark.isEmpty())
            {
                in_str_remark = "无";
            }
            if (in_str_remark.length()>15)
            {
                Toast.makeText(addNewOne.this, "不超过15个字哦~", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        return 1;
    }
    void setbuttonstyle()
    {
        if(flag_isthefirstclick == 0)
        {
            v_current.setSelected(true);
            flag_isthefirstclick = 1;
        }
        else
        {
            v_last.setSelected(false);
            v_current.setSelected(true);
        }

    }



    public void showSingleAlertDialog(final String[] items){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("二级分类");
        alertBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("run_tag", "onClick: "+i);
                sec_text = items[i];
                if(sec_text=="其他"){
                    final EditText inputServer = new EditText(addNewOne.this);
                    String str = new String();
                    str = "请输入其它自定义";
                    inputServer.setText(sec_text);
                }
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(sec_text.equals("其他"))
                    self();
                Log.d("run_tag", "showSingleAlertDialog: "+sec_text);
                Toast.makeText(addNewOne.this, sec_text,Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }

    public void self() {
        final EditText inputServer = new EditText(addNewOne.this);
        String str2 = new String();
        str2 = "请输入其它消费内容";
        inputServer.setText(sec_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(addNewOne.this);
        builder.setTitle(str2).setIcon(R.drawable.r).setView(inputServer).setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkstrInput = inputServer.getText().toString();
                sec_text = checkstrInput;
            }
        });
        builder.show();
    }
}
