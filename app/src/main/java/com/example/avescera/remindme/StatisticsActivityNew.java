package com.example.avescera.remindme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.avescera.remindme.Classes.CustomYAxisValueFormatter;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.MonthAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsActivityNew extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;

    private ImageView imgStatMoney;
    private ImageView imgStatObject;
    private ImageView imgStatContact;
    private ImageView imgStatCategory;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        moneyBarChart();

        imgStatMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyBarChart();
            }
        });

        imgStatObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectBarChart();
            }
        });
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);
        calendar = Calendar.getInstance();
    }

    private void attachViewItems(){
        imgStatMoney = (ImageView) findViewById(R.id.imgStatMoney);
        imgStatObject = (ImageView) findViewById(R.id.imgStatObject);
        imgStatContact = (ImageView) findViewById(R.id.imgStatContact);
        imgStatCategory = (ImageView) findViewById(R.id.imgStatCategory);
    }

    private void moneyBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        barChart.getDescription().setText(getResources().getString(R.string.money_bar_chart));
        //HorizontalBarChart barChart= (HorizontalBarChart) findViewById(R.id.chart);

        List<List<Float>> amountByMonth = dbHandlers.getDbMoneyHandler().getLastSixMonthsMoney();

        ArrayList<BarEntry> loan = new ArrayList<>();
        ArrayList<BarEntry> borrow = new ArrayList<>();

        List<Float> temp = amountByMonth.get(0);
        int currentMonth = calendar.get(Calendar.MONTH);
        int j = 5; //number of month before the current one to get the 6 last months.
        float xBoarder = 0.5f; //To add space on left and right part of the chart.

        barChart.getXAxis().setAxisMinimum((float) currentMonth - j - xBoarder);
        barChart.getXAxis().setAxisMaximum((float) currentMonth + xBoarder);

        for (int i=0; i<amountByMonth.size(); i++) {
            while (currentMonth - j != Math.round(temp.get(i * 3))-1){
                loan.add(new BarEntry(currentMonth - j, 0f));
                borrow.add(new BarEntry(currentMonth - j, 0f));
                j-=1;
            }
            loan.add(new BarEntry(Math.round(temp.get(i * 3)-1), Math.round(temp.get(i * 3 + 1))));
            borrow.add(new BarEntry(Math.round(temp.get(i * 3)-1), Math.round(temp.get(i * 3 + 2))));
            j-=1;
        }

        BarDataSet barDataSet1 = new BarDataSet(loan, getResources().getString(R.string.type_loan));
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(borrow, getResources().getString(R.string.type_borrow));
        barDataSet2.setColor(Color.rgb(155, 0, 0));
        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        /*ArrayList<IBarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);
*/
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setLabelCount(12);

        //BarData data = new BarData(dataset);
        BarData data = new BarData(barDataSet1, barDataSet2);
        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new MonthAxisValueFormatter(this));
        barChart.invalidate();
        barChart.animateY(2000);
        barChart.getBarData().setBarWidth(0.5f);
        //barChart.groupBars(0, 1f, 0.1f);

        data.setValueFormatter(new CustomYAxisValueFormatter());
    }

    private void objectBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        barChart.getDescription().setText(getResources().getString(R.string.object_bar_chart));

        List<List<Float>> quantityByMonth = dbHandlers.getDbObjectHandler().getLastSixMonthsObject();

        ArrayList<BarEntry> loan = new ArrayList<>();
        ArrayList<BarEntry> borrow = new ArrayList<>();

        List<Float> temp = quantityByMonth.get(0);
        int currentMonth = calendar.get(Calendar.MONTH);
        int j = 5;

        barChart.getXAxis().setAxisMinimum((float) currentMonth - j);
        barChart.getXAxis().setAxisMaximum((float) currentMonth);

        for (int i=0; i<quantityByMonth.size(); i++) {
            while (currentMonth - j != Math.round(temp.get(i * 3))-1){
                loan.add(new BarEntry(currentMonth - j, 0f));
                borrow.add(new BarEntry(currentMonth - j, 0f));
                j-=1;
            }
            loan.add(new BarEntry(Math.round(temp.get(i * 3)-1), Math.round(temp.get(i * 3 + 1))));
            borrow.add(new BarEntry(Math.round(temp.get(i * 3)-1), Math.round(temp.get(i * 3 + 2))));

            j-=1;
        }

        BarDataSet barDataSet1 = new BarDataSet(loan, getResources().getString(R.string.type_loan));
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        BarDataSet barDataSet2 = new BarDataSet(borrow, getResources().getString(R.string.type_borrow));
        barDataSet2.setColor(Color.rgb(155, 0, 0));

        ArrayList<IBarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);

        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setLabelCount(12);

        BarData data = new BarData(dataset);
        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new MonthAxisValueFormatter(this));
        barChart.animateY(2000);
        barChart.getBarData().setBarWidth(0.5f);
    }

}
