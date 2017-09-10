/*
 * Created on 17.02.2009
 */
package estim.gui.graph;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;

public abstract  class AbstractGraph implements Runnable{
    
    protected JFreeChart chart; 
    protected String filename = null; 
    
    protected TimeSeries seriesOut = null;
    protected TimeSeries seriesIn = null;
    protected double maxDataRange = 0.0;

    private static final Logger logger = Logger.getLogger(AbstractGraph.class);
    
    protected void createChart() {
            
        final TimeSeriesCollection datasetOut = new TimeSeriesCollection();
        final TimeSeriesCollection datasetIn = new TimeSeriesCollection();
            
        if(seriesOut != null) {
            datasetOut.addSeries(seriesOut);            
        }
    
        if(seriesIn != null) {
            datasetIn.addSeries(seriesIn);
        }
    
        chart = ChartFactory.createTimeSeriesChart( 
                getGraphTitle(), getGraphXAxisLegend(), getGraphYAxisLegend(), datasetOut, true, true, false); 
    
        formartChart(); 
    
        final XYItemRenderer area_renderer = getAreaRenderer();
        final StandardXYItemRenderer line_renderer = getLineRenderer();
    
        final XYPlot plot = (XYPlot) chart.getPlot();
    
        plot.setRenderer(area_renderer);
        plot.setDataset(1, datasetIn);
        plot.setRenderer(1, line_renderer);

        formatAxis();
        formatPlot();
    }
    
    protected void exportImage(final JFreeChart chart, final String filename) {
        try {
            logger.info("Writing Image to file: " + filename);
            
            ChartUtilities.saveChartAsJPEG(new File(filename), chart, 
                    getExportWidth(), getExportHeight());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public JFreeChart getChart() {
		run();    	
    	return chart;
    }

    protected int getExportHeight() {
        return 300;
    }

    protected int getExportWidth() {
        return 600;
    }

    protected void formartChart() {
        chart.setBackgroundPaint(Color.white);
    }

    protected XYItemRenderer getAreaRenderer() {
        final XYItemRenderer area_renderer = new XYAreaRenderer();
        final Color color = getAreaRendererDefaultColor();
        area_renderer.setSeriesPaint(0, color);
        area_renderer.setBaseSeriesVisibleInLegend(getAreaVisibleInLegend());
        return area_renderer;
    }

    protected boolean getAreaVisibleInLegend() {
        return true;
    }

    protected StandardXYItemRenderer getLineRenderer() {
        final StandardXYItemRenderer line_renderer   
            = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        line_renderer.setSeriesPaint(0, Color.BLUE);
        line_renderer.setBaseSeriesVisibleInLegend(getLineVisibleInLegend());
        return line_renderer;
    }

    protected Color getAreaRendererDefaultColor() {
        return new Color(00, 0xCF, 00);
    }

    protected boolean getLineVisibleInLegend() {
        return true;
    }

    protected void formatAxis() {
        final XYPlot plot = (XYPlot) chart.getPlot();
        
        final DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setAutoRange(true);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setTickLabelsVisible(true);
        
        domainAxis.setAxisLinePaint(Color.BLACK);
        domainAxis.setLabelPaint(Color.BLACK);
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setTickMarkPaint(Color.BLACK);
        domainAxis.setPositiveArrowVisible(true);
        
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAxisLinePaint(Color.BLACK);
        rangeAxis.setLabelPaint(Color.BLACK);
        rangeAxis.setTickLabelPaint(Color.BLACK);
        rangeAxis.setTickMarkPaint(Color.BLACK);
        rangeAxis.setPositiveArrowVisible(true);
        rangeAxis.setAutoRange(false);
        rangeAxis.setLowerBound(0);
        rangeAxis.setUpperBound(maxDataRange * 1.1);
    }

    protected void formatPlot() {
        final XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        
        plot.setDomainGridlinesVisible(true);  
        plot.setRangeGridlinesVisible(true);  
        plot.setRangeGridlinePaint(Color.BLACK);  
        plot.setDomainGridlinePaint(Color.BLACK);   
        plot.setForegroundAlpha(getAreaAlpha());
        
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        final LegendTitle legend = chart.getLegend();
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setMargin(new RectangleInsets(0, 0, 5, 10));
        legend.setBorder(0, 0, 0, 0);
    }

    protected float getAreaAlpha() {
        return 0.95f;
    }

    //===========================================
    // Public
    //===========================================
    public void run() {
        calculateDataSeries();
        createChart();      
        
        if(filename != null) {
            exportImage(chart, filename);
        }        
    }

    public void clearCache() {
        chart = null;
    }
    
    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }
    
    protected void handleNewTimeValue(final double value) {
        maxDataRange = Math.max(value, maxDataRange);
    }
    
    //=====================================================
    // Abstract
    //=====================================================
    protected abstract String getGraphYAxisLegend();
    protected abstract String getGraphXAxisLegend();
    protected abstract String getGraphTitle();
    protected abstract void calculateDataSeries();
}
