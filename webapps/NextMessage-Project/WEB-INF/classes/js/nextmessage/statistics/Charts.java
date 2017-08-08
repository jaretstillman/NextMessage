package js.nextmessage.statistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import js.nextmessage.exceptions.InvalidFundingException;
import js.nextmessage.exceptions.InvalidFundingRetrievalException;
import js.nextmessage.constants.Constants;
import js.nextmessage.structs.CompanyFundingData;

/*
 * DESCRIPTION: This class generates charts, opens them in a new window and also saves them as JPEGs
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Charts
{
	//This class alternates bar color in the "Total Funding" graph
	static class AlternateBarRenderer extends BarRenderer
	{
		private static final long serialVersionUID = 1L;
		private boolean isDivided;
		private int i;

		public AlternateBarRenderer(boolean isDivided)
		{
			super();
			this.isDivided = isDivided;
			i = 0;
		}

		public Paint getItemPaint(int x_row, int x_col)
		{
			if (isDivided)
			{
				return this.getSeriesPaint(x_row);
			}

			i++;

			if (i % 2 == 0)
			{
				return Color.decode("#0b2040");
			}
			return Color.decode("#5BC4BF");
		}
	}
	
	
	public static void generateFundingByCompanyChart(String fileName, boolean display) throws InvalidFundingException, IOException
	{	
		generateChart(getFundingByCompanyDataset(), "Total Funding By Company", fileName, false, display);
	}
	
	public static void generateFundingByCompanyDividedChart(String fileName, boolean display) throws InvalidFundingException, InvalidFundingRetrievalException, IOException
	{
		generateChart(getFundingByCompanyDividedDataset(), "Funding by Company and Company Type", fileName, true, display);
	}

	private static CategoryDataset getFundingByCompanyDataset() throws InvalidFundingException
	{
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		ArrayList<CompanyFundingData> cfd = Statistics.fundingByCompany(Constants.NUMBERMAP, Constants.INVESTMENTS);
		for(CompanyFundingData c : cfd)
		{
			dataSet.addValue(c.getTotalFunding(), "", c.getName());
		}
		
		return dataSet;
	}
	
	private static CategoryDataset getFundingByCompanyDividedDataset() throws InvalidFundingException, InvalidFundingRetrievalException
	{
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		ArrayList<CompanyFundingData> cfd = Statistics.fundingByCompanyDivided(Constants.NUMBERMAP, Constants.INVESTMENTS);
		for(CompanyFundingData c : cfd)
		{
			dataSet.addValue(c.getEnterpriseFunding(), "Enterprise", c.getName());
			dataSet.addValue(c.getMediumSizedBusinessFunding(), "Medium-Sized Business", c.getName());
			dataSet.addValue(c.getSmallBusinessFunding(), "Small Business", c.getName());
			dataSet.addValue(c.getStaffingAgencyFunding(), "Staffing Agency", c.getName());
			dataSet.addValue(c.getInvestorFunding(), "Investor", c.getName());
		}
		
		return dataSet;
	}
	
	/*
	 * EFFECTS: Prints chart to new window, publishes to png file
	 */
	private static void generateChart(CategoryDataset dataset, String title, String fileName, boolean isDivided, boolean display) throws IOException
	{
		
		JFreeChart chart = ChartFactory.createBarChart(
				title,  //title
				"Company", //x-axis
				"Funding ($)", //y-axis
				dataset, //data
				PlotOrientation.VERTICAL, //orientation
				isDivided, //include legend?
				false, //tooltips?
				false //urls?
				);
		
		chart.setBackgroundPaint(Color.WHITE);
		
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);
		
		
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
				
		final CategoryAxis axis = plot.getDomainAxis();
		axis.setLowerMargin(0.01); //between beginning and first bar
		axis.setUpperMargin(0.01); //between end and last bar
		axis.setCategoryMargin(0.1); //between categories
		
		//How to rotate labels
		//axis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 3.0);
		
		Charts.AlternateBarRenderer renderer = new Charts.AlternateBarRenderer(isDivided);
		renderer.setDrawBarOutline(true);
		renderer.setItemMargin(0.0); //between series bars
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setSeriesPaint(0, Color.decode("#0b2040"));
		renderer.setShadowVisible(false);
        plot.setRenderer(renderer);
        
        if(title.contains("Type")) //bar rendering
        {
        	renderer.setSeriesPaint(1, Color.decode("#FFC720"));
        	renderer.setSeriesPaint(2, Color.decode("#5BC4BF"));
        	renderer.setSeriesPaint(3, Color.decode("#FFA500"));
        	renderer.setSeriesPaint(4, Color.decode("#228B22"));
        }
        
		if (display)
		{
			JFrame frame = new JFrame();
			ChartPanel cp = new ChartPanel(chart, false);
			cp.setPreferredSize(new Dimension(1500, 900));
			frame.add(cp, "Center");
			frame.pack();
			frame.setVisible(true);
		}
        
        File file = new File(fileName);
        ChartUtilities.saveChartAsPNG(file, chart, 1500, 900);
	}
	
}
