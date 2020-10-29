package com.example.xie.moneymanager;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class LineChartManager {

    private static String lineName = "支出";
    private static String lineName1 = "收入";
    public void initDoubleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                    ArrayList<Entry> yValue, ArrayList<Entry> yValue1) {

        initDataStyle(context,mLineChart);

        LineDataSet dataSet = new LineDataSet(yValue, lineName);
        dataSet.setCircleColor(Color.parseColor("#ff5e5e"));
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(3);
        dataSet.setColor(Color.parseColor("#ff5e5e"));
        dataSet.setCircleSize(10f);// 显示的圆形大小
        dataSet.setFillColor(Color.parseColor("#f9bebe"));//折线图下方填充颜色设置
        dataSet.setDrawFilled(true);
        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        dataSet1.setCircleColor(Color.parseColor("#61a3c9"));
        dataSet1.setDrawValues(false);
        dataSet1.setLineWidth(3);
        dataSet1.setColor(Color.parseColor("#61a3c9"));
        dataSet1.setFillColor(Color.rgb(132, 155, 179));//折线图下方填充颜色设置
        dataSet1.setDrawFilled(true);
        dataSet1.setCircleSize(10f);// 显示的圆形大小

        dataSet.setDrawVerticalHighlightIndicator(true);


        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet);
        dataSets.add(dataSet1);
        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入


        mLineChart.setExtraRightOffset(20f); //设置距离屏幕右边距离
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(1000, Easing.EasingOption.Linear);
        mLineChart.animateX(1000, Easing.EasingOption.Linear);
    }

    private static void initDataStyle(Context context, LineChart mLineChart) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(false);
        mLineChart.setDescription("");
        MyMarkerView mv = new MyMarkerView(context, R.layout.markviewtext);
        mLineChart.setMarkerView(mv);
        Legend title = mLineChart.getLegend();
        title.setForm(Legend.LegendForm.LINE);
        title.setTextSize(15);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.parseColor("#66CDAA"));
        xAxis.setAxisLineWidth(5);
        xAxis.setDrawGridLines(true);

        //设置是否显示x轴
        xAxis.setEnabled(true);
        xAxis.setTextSize(15);
        //设置左边y轴的样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineColor(Color.parseColor("#66CDAA"));
        yAxisLeft.setAxisLineWidth(5);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setTextSize(15);
        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

    }
}
