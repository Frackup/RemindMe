package com.example.avescera.remindme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.CustomYAxisValueFormatter;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.MonthAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;

    private ImageView imgStatMoney;
    private ImageView imgStatObject;
    private ImageView imgStatContact;
    private ImageView imgStatCategory;
    private TextView txtVEmptyStats;

    private Calendar calendar;

    //TODO : ajouter des icones avec couleurs inversées pour mettre en relief le rapport sur lequel nous sommes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
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

        imgStatContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactBarChart();
            }
        });

        imgStatCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryBarChart();
            }
        });
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);
        calendar = Calendar.getInstance();
        txtVEmptyStats.setText(getResources().getString(R.string.empty_stats));
    }

    private void attachViewItems(){
        imgStatMoney = (ImageView) findViewById(R.id.imgStatMoney);
        imgStatObject = (ImageView) findViewById(R.id.imgStatObject);
        imgStatContact = (ImageView) findViewById(R.id.imgStatContact);
        imgStatCategory = (ImageView) findViewById(R.id.imgStatCategory);
        txtVEmptyStats = (TextView) findViewById(R.id.txtViewEmptyStats);
    }

    private void moneyBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        barChart.getDescription().setText(getResources().getString(R.string.money_bar_chart));
        //HorizontalBarChart barChart= (HorizontalBarChart) findViewById(R.id.chart);

        List<List<Float>> amountByMonth = dbHandlers.getDbMoneyHandler().getLastSixMonthsMoney();

        if(amountByMonth.size() > 0) {
            //As there's something to show, we don't show the empty textview, but show the barchart.
            txtVEmptyStats.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);

            ArrayList<BarEntry> loan = new ArrayList<>();
            ArrayList<BarEntry> borrow = new ArrayList<>();

            List<Float> temp = amountByMonth.get(0);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int displayedMonth = 6;
            int maxMonth = 12, month, doubleMonths;
            float xBoarder = 1f; //To add space on left and right part of the chart.
            float startMonth;

            if (currentMonth - (displayedMonth - 1) <= 0) {
                barChart.getXAxis().setAxisMinimum(maxMonth + currentMonth - (displayedMonth - 1));
                barChart.getXAxis().setAxisMaximum(maxMonth + currentMonth + xBoarder);
                startMonth = maxMonth + currentMonth - (displayedMonth - 1);
            } else {
                barChart.getXAxis().setAxisMinimum(currentMonth - (displayedMonth - 1));
                barChart.getXAxis().setAxisMaximum(currentMonth + xBoarder);
                startMonth = currentMonth - (displayedMonth - 1);
            }


            for (int i = 0; i < amountByMonth.size(); i++) {
                if (currentMonth - (displayedMonth - 1) <= 0) {
                    month = maxMonth - (displayedMonth - 1 - currentMonth);
                    doubleMonths = maxMonth + currentMonth - displayedMonth - 1;
                } else {
                    month = currentMonth - (displayedMonth - 1);
                    doubleMonths = currentMonth - (displayedMonth - 1);
                }

                while (month < Math.round(temp.get(i * 3))) {
                    loan.add(new BarEntry(doubleMonths - 1, 0f));
                    borrow.add(new BarEntry(doubleMonths - 1, 0f));
                    displayedMonth -= 1;
                    if (currentMonth - (displayedMonth - 1) <= 0) {
                        month = maxMonth - (displayedMonth - 1 - currentMonth);
                        doubleMonths = maxMonth + currentMonth - (displayedMonth - 1);
                    } else {
                        month = currentMonth - (displayedMonth - 1);
                        doubleMonths = currentMonth - (displayedMonth - 1);
                    }
                }
                if(month ==  Math.round(temp.get(i * 3))) {
                    loan.add(new BarEntry(Math.round(temp.get(i * 3) - 1), temp.get(i * 3 + 1)));
                    borrow.add(new BarEntry(Math.round(temp.get(i * 3) - 1), temp.get(i * 3 + 2)));
                } else if (month >  Math.round(temp.get(i * 3))){
                    loan.add(new BarEntry(Math.round(temp.get(i * 3) + maxMonth - 1), temp.get(i * 3 + 1)));
                    borrow.add(new BarEntry(Math.round(temp.get(i * 3) + maxMonth - 1), temp.get(i * 3 + 2)));
                }
                displayedMonth -= 1;
            }

            BarDataSet barDataSet1 = new BarDataSet(loan, getResources().getString(R.string.type_loan));
            barDataSet1.setColor(Color.rgb(0, 155, 0));
            //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

            BarDataSet barDataSet2 = new BarDataSet(borrow, getResources().getString(R.string.type_borrow));
            barDataSet2.setColor(Color.rgb(155, 0, 0));
            //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setLabelCount(12);

            float groupSpace = 0.06f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.45f; // x2 dataset
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

            BarData data = new BarData(barDataSet1, barDataSet2);
            data.setBarWidth(barWidth);
            barChart.setData(data);
            barChart.groupBars(startMonth, groupSpace, barSpace);
            barChart.getXAxis().setValueFormatter(new MonthAxisValueFormatter(this));
            data.setValueFormatter(new CustomYAxisValueFormatter());
            barChart.getXAxis().setCenterAxisLabels(true);
            barChart.animateY(2000);
            barChart.invalidate();

        } else {
            txtVEmptyStats.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.INVISIBLE);
        }
    }

    private void objectBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        barChart.getDescription().setText(getResources().getString(R.string.object_bar_chart));

        List<List<Float>> quantityByMonth = dbHandlers.getDbObjectHandler().getLastSixMonthsObject();

        if(quantityByMonth.size() > 0) {
            //As there's something to show, we don't show the empty textview, but show the barchart.
            txtVEmptyStats.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);

            ArrayList<BarEntry> loan = new ArrayList<>();
            ArrayList<BarEntry> borrow = new ArrayList<>();

            List<Float> temp = quantityByMonth.get(0);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int displayedMonth = 6;
            int maxMonth = 12, month, doubleMonths;
            float xBoarder = 1f; //To add space on left and right part of the chart.
            float startMonth;

            if (currentMonth - (displayedMonth - 1) <= 0) {
                barChart.getXAxis().setAxisMinimum((float) maxMonth + currentMonth - (displayedMonth - 1));
                barChart.getXAxis().setAxisMaximum((float) maxMonth + currentMonth + xBoarder);
                startMonth = maxMonth + currentMonth - (displayedMonth - 1);
            } else {
                barChart.getXAxis().setAxisMinimum((float) currentMonth - (displayedMonth - 1));
                barChart.getXAxis().setAxisMaximum((float) currentMonth + xBoarder);
                startMonth = currentMonth - (displayedMonth - 1);
            }

            for (int i = 0; i < quantityByMonth.size(); i++) {
                if (currentMonth - (displayedMonth - 1) <= 0) {
                    month = maxMonth - (displayedMonth - 1 - currentMonth);
                    doubleMonths = maxMonth + currentMonth - (displayedMonth - 1);
                } else {
                    month = currentMonth - (displayedMonth - 1);
                    doubleMonths = currentMonth - (displayedMonth - 1);
                }
                while (month != Math.round(temp.get(i * 3))) {
                    loan.add(new BarEntry(doubleMonths - 1, 0f));
                    borrow.add(new BarEntry(doubleMonths - 1, 0f));
                    displayedMonth -= 1;
                    if (currentMonth - (displayedMonth - 1) <= 0) {
                        month = maxMonth - (displayedMonth - 1 - currentMonth);
                        doubleMonths = maxMonth + currentMonth - (displayedMonth - 1);
                    } else {
                        month = currentMonth - (displayedMonth - 1);
                        doubleMonths = currentMonth - (displayedMonth - 1);
                    }
                }
                if(month ==  Math.round(temp.get(i * 3))) {
                    loan.add(new BarEntry(Math.round(temp.get(i * 3) - 1), Math.round(temp.get(i * 3 + 1))));
                    borrow.add(new BarEntry(Math.round(temp.get(i * 3) - 1), Math.round(temp.get(i * 3 + 2))));
                } else if (month >  Math.round(temp.get(i * 3))){
                    loan.add(new BarEntry(Math.round(temp.get(i * 3) + maxMonth - 1), Math.round(temp.get(i * 3 + 1))));
                    borrow.add(new BarEntry(Math.round(temp.get(i * 3) + maxMonth - 1), Math.round(temp.get(i * 3 + 2))));
                }
                displayedMonth -= 1;
            }

            BarDataSet barDataSet1 = new BarDataSet(loan, getResources().getString(R.string.type_loan));
            barDataSet1.setColor(Color.rgb(0, 155, 0));

            BarDataSet barDataSet2 = new BarDataSet(borrow, getResources().getString(R.string.type_borrow));
            barDataSet2.setColor(Color.rgb(155, 0, 0));

            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setLabelCount(12);

            float groupSpace = 0.06f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.45f; // x2 dataset
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

            BarData data = new BarData(barDataSet1, barDataSet2);
            data.setBarWidth(barWidth);
            barChart.setData(data);
            barChart.groupBars(startMonth, groupSpace, barSpace);
            barChart.getXAxis().setValueFormatter(new MonthAxisValueFormatter(this));
            //data.setValueFormatter(new CustomYAxisValueFormatter());
            barChart.getXAxis().setCenterAxisLabels(true);
            barChart.animateY(2000);
            barChart.invalidate();

        } else {
            txtVEmptyStats.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.INVISIBLE);
        }
    }

    private void contactBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        txtVEmptyStats.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.INVISIBLE);
    }

    private void categoryBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        txtVEmptyStats.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.INVISIBLE);
    }
}
