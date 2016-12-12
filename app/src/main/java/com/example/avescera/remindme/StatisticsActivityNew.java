package com.example.avescera.remindme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.avescera.remindme.Classes.CustomYAxisValueFormatter;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.MonthConverter;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        MonthConverter monthConverter = new MonthConverter(this);
        //HorizontalBarChart barChart= (HorizontalBarChart) findViewById(R.id.chart);

        List<List<Float>> amountByMonth = dbHandlers.getDbMoneyHandler().getLastSixMonthsMoney();

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> loan = new ArrayList<>();
        ArrayList<BarEntry> borrow = new ArrayList<>();

        List<Float> temp = amountByMonth.get(0);
        int month = Math.round(temp.get(0));
        int monthN, result;
        int position = 0;
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int j = 5;
        List<Integer> emptyMonths = new ArrayList<>();

        for (int i=0; i<amountByMonth.size(); i++) {
            while (currentMonth - j != Math.round(temp.get(i * 3))){
                j -= 1;
                labels.add(monthConverter.convert(i));
                loan.add(new BarEntry(0f, position));
                borrow.add(new BarEntry(0f, position));
                position++;
            }
            labels.add(monthConverter.convert(Math.round(temp.get(i * 3))));
            loan.add(new BarEntry(Math.round(temp.get(i * 3 + 1)), position));
            borrow.add(new BarEntry(Math.round(temp.get(i * 3 + 2)), position));
            position++;
            j-=1;
        }
        /*
        for (int i=0; i<amountByMonth.size(); i++) {
            labels.add(monthConverter.convert(Math.round(temp.get(i*3))));
            loan.add(new BarEntry(temp.get(i*3+1),i));
            borrow.add(new BarEntry(temp.get(i*3+2),i));
        }

        for (int i=0; i<amountByMonth.size(); i++) {
            monthN = Math.round(temp.get(i*3));
            result = monthN - month;
            position = 12 - Math.abs(result) - 1;
            if(1 < result && result < 6 || -11 < result && result < -6) {
                for (int j = i+1; j <= position; j++) {
                    emptyMonths.add(j);
                }
            }
            labels.add(monthConverter.convert(Math.round(temp.get(i * 3))));
            loan.add(new BarEntry(Math.round(temp.get(i * 3 + 1)), position));
            borrow.add(new BarEntry(Math.round(temp.get(i * 3 + 2)), position));
            month = monthN;
        }

        for (int i = emptyMonths.get(0); i < emptyMonths.size(); i++) {
            labels.add(monthConverter.convert(i));
            loan.add(new BarEntry(0f, emptyMonths.get(i)));
            borrow.add(new BarEntry(0f, emptyMonths.get(i)));
        }
*/
        BarDataSet barDataSet1 = new BarDataSet(loan, getResources().getString(R.string.type_loan));
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(borrow, getResources().getString(R.string.type_borrow));
        barDataSet2.setColor(Color.rgb(155, 0, 0));
        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);

        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.animateY(2000);

        barChart.setDescription(getResources().getString(R.string.money_bar_chart));
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setValueFormatter(new CustomYAxisValueFormatter());
    }

    private void objectBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        MonthConverter monthConverter = new MonthConverter(this);
        //HorizontalBarChart barChart= (HorizontalBarChart) findViewById(R.id.chart);

        List<List<Float>> objectByMonth = dbHandlers.getDbObjectHandler().getLastSixMonthsObject();

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> loan = new ArrayList<>();
        ArrayList<BarEntry> borrow = new ArrayList<>();

        List<Float> temp = objectByMonth.get(0);
        int month = Math.round(temp.get(0));
        int monthN, result;
        int position = 0;
        List<Integer> emptyMonths = new ArrayList<>();
        /*
        for (int i=0; i<objectByMonth.size(); i++) {
            monthN = Math.round(temp.get(i*3));
            result = monthN - month;
            if(1 < result && result < 6 || -11 < result && result < -6) {
                result = 12 - Math.abs(result) - 1;
                for (int j = month + 1; j<= month + result; j++) {
                    if (j<12) {
                        labels.add(monthConverter.convert(j));
                        loan.add(new BarEntry(0f, position));
                        borrow.add(new BarEntry(Math.round(temp.get(i * 3 + 2)), position));
                    } else {
                        labels.add(monthConverter.convert(Math.round(temp.get(i * 3))));
                        loan.add(new BarEntry(Math.round(temp.get(i * 3 + 1)), position));
                        borrow.add(new BarEntry(Math.round(temp.get(i * 3 + 2)), position));
                    }
                    monthN = month;
                }
            } else {
                labels.add(monthConverter.convert(Math.round(temp.get(i * 3))));
                loan.add(new BarEntry(Math.round(temp.get(i * 3 + 1)), position));
                borrow.add(new BarEntry(Math.round(temp.get(i * 3 + 2)), position));

                position +=1;
            }
        }
*/
        for (int i=0; i<objectByMonth.size(); i++) {
            monthN = Math.round(temp.get(i*3));
            result = monthN - month;
            position = 12 - Math.abs(result) - 1;
            if(1 < result && result < 6 || -11 < result && result < -6) {
                for (int j = i+1; j <= position; j++) {
                    emptyMonths.add(j);
                }
            }
            labels.add(monthConverter.convert(Math.round(temp.get(i * 3))));
            loan.add(new BarEntry(Math.round(temp.get(i * 3 + 1)), position));
            borrow.add(new BarEntry(Math.round(temp.get(i * 3 + 2)), position));
        }

        for (int i = emptyMonths.get(0); i < emptyMonths.size(); i++) {
            labels.add(monthConverter.convert(i));
            loan.add(new BarEntry(0f, emptyMonths.get(i)));
            borrow.add(new BarEntry(0f, emptyMonths.get(i)));
        }

        BarDataSet barDataSet1 = new BarDataSet(loan, getResources().getString(R.string.type_loan));
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(borrow, getResources().getString(R.string.type_borrow));
        barDataSet2.setColor(Color.rgb(155, 0, 0));
        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);

        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.animateY(2000);

        barChart.setDescription(getResources().getString(R.string.object_bar_chart));
    }

}
