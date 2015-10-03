package com.hacku.kuse.hacku2015;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by denjo on 15/10/02.
 */
public class Graph extends AppCompatActivity {
    private View mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        getSupportActionBar().hide();


        openChart();

        ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Graph.this, MainActivity.class));
            }
        });

        ImageButton btnGraph = (ImageButton) findViewById(R.id.btnGraph);
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Graph.this, Graph.class));
            }
        });

    }

    private void openChart(){

        //Creating the data
        int[] time = { 0,1,2,3,4,5,6,7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
        int[] intensity = { 112, 210, 150, 110, 50, 20, 30, 70, 80, 176,
                245, 255, 67, 87, 187, 123, 176, 214, 245, 210 };

        //Creating an XYSeries
        XYSeries intensitySeries = new XYSeries("");
        // Adding data to the Series
        for(int i=0;i<time.length;i++){
            intensitySeries.add(time[i],intensity[i]);
        }

        // Creating a dataset to hold the series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(intensitySeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer intensityRenderer = new XYSeriesRenderer();
        intensityRenderer.setColor(Color.RED); //color of the graph set to cyan
        intensityRenderer.setFillPoints(true);
        intensityRenderer.setLineWidth(2);
        intensityRenderer.setDisplayChartValues(true);
        intensityRenderer.setDisplayChartValuesDistance(10); //setting chart value distance

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);
        //multiRenderer.setXLabels(0);
        //multiRenderer.setYLabels(0);
        //multiRenderer.setChartTitle("Intensity of shaking legs");
        //multiRenderer.setXTitle("Time[ago]");
        //multiRenderer.setYTitle("Intensity");
        multiRenderer.addSeriesRenderer(intensityRenderer);
        multiRenderer.setBarSpacing(0.5);
        multiRenderer.setMargins(new int[]{0, 0, 0, 0});
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        multiRenderer.setBackgroundColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0,Color.BLACK);

        //setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(30);
        multiRenderer.setShowLegend(false);
        multiRenderer.setShowGrid(true);
        multiRenderer.setShowAxes(true);
        multiRenderer.setInScroll(false);
        multiRenderer.setAxesColor(Color.BLACK);

        //setting text size of the graph lable
        multiRenderer.setLabelsTextSize(24);

        //this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
        //remove any views before u paint the chart
        chartContainer.removeAllViews();
        //drawing bar chart
        mChart = ChartFactory.getBarChartView(Graph.this, dataset, multiRenderer, BarChart.Type.DEFAULT);
        //adding the view to the linearlayout
        chartContainer.addView(mChart);
    }
}
