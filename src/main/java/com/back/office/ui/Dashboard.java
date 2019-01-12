package com.back.office.ui;

//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.model.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.vaadin.addon.JFreeChartWrapper;

public class Dashboard extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public Dashboard(){
        setMargin(true);
        createChart();
        //createCharts();
    }

    /*private void createCharts(){
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Total fruit consumption, grouped by gender");
        XAxis x = new XAxis();
        x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec");
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Rainfall (mm)");
        conf.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.x +': '+ this.y +' mm'");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        conf.addSeries(new ListSeries("Tokyo", 49.9, 71.5, 106.4, 129.2, 144.0,
                176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4));
        conf.addSeries(new ListSeries("New York", 83.6, 78.8, 98.5, 93.4,
                106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3));
        conf.addSeries(new ListSeries("London", 48.9, 38.8, 39.3, 41.4, 47.0,
                48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
        conf.addSeries(new ListSeries("Berlin", 42.4, 33.2, 34.5, 39.7, 52.6,
                75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));

        chart.drawChart(conf);
        addComponent(chart);
    }*/
    private void createChart(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        dataset.addValue( 15 , "schools" , "Jan" );
        dataset.addValue( 30 , "schools" , "Feb" );
        dataset.addValue( 60 , "schools" ,  "March" );
        dataset.addValue( 100 , "schools" , "April" );
        dataset.addValue( 90 , "schools" , "May" );
        dataset.addValue( 80 , "schools" , "June" );
        // Generate the graph

        JFreeChart chart = ChartFactory.createLineChart("Monthly Sales", // Title
                "Month", // x-axis Label
                "Gross Amount $", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                false, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        final String fiat = "FIAT";
        DefaultCategoryDataset dataset1 =
                new DefaultCategoryDataset( );

        dataset1.addValue( 300.0 , fiat , "DTF" );
        dataset1.addValue( 200.0 , fiat , "BOB" );
        dataset1.addValue( 25.0 , fiat , "VRT" );
        dataset1.addValue( 150.0 , fiat , "POD" );
        JFreeChart barChart = ChartFactory.createBarChart("Sales Summary", "Gross Amount $", "Sales Type",
                dataset1, PlotOrientation.VERTICAL, false, true, true);

        JFreeChartWrapper wrapper = new JFreeChartWrapper(chart){
            private static final long serialVersionUID = 1L;
            @Override
            public void attach() {
                super.attach();
                setResource("src", getSource());
            }
        };
        JFreeChartWrapper wrapper1 = new JFreeChartWrapper(barChart){
            private static final long serialVersionUID = 1L;
            @Override
            public void attach() {
                super.attach();
                setResource("src", getSource());
            }
        };
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        addComponent(layout);
        wrapper.setWidth("80%");
        wrapper.setHeight("70%");
        wrapper1.setWidth("80%");
        wrapper1.setHeight("70%");
        layout.addComponent(wrapper);
        layout.addComponent(wrapper1);
    }
}

