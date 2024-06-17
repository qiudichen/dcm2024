package org.fasterjson.poi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ScheduleChart {
	private final static String Red = "#ff0000";
	private final static String Beige = "#F5F5DC";
	private final static String Black = "#000000";
	private final static String Brown = "#A52A2A";
	private final static String Chocolate = "#D2691E";
	private final static String Cyan = "#00FFFF";
	private final static String DarkRed = "#8B0000";
	private final static String Grey = "#808080";
	private final static String[] Data_Colors = {Brown, DarkRed, Beige, Black, Chocolate, Cyan, Grey, Red};
	private final static String Orange = "#FFA500";
	
	private final static String charTitle = "Demand and Coverage Chart";
	private final static String xAxisTitle = "Date/Time";
	private final static String yAxisTitle = "Agt No.";
	private final static String coverageTitle = "Coverage ";	
	private final static String DemandTitle = "Demand";	
	private final static String coverageDataTableTitle = "Demand and Coverage Data Table";	
	
	private final static DateTimeFormatter sDayFormatter = DateTimeFormatter.ofPattern("dd HH:mm");
	private final static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	
	public static void main(String[] args) throws IOException {
		int timeInterval = 15;

		int size = 4 * 24 * 7;

		
		double[] forecasts = new double[size];
		double[] coverages1 = new double[size]; 
		double[] coverages2 = new double[size];

		int min = 5;
		int max = 20;
		
		for(int i = 0; i < size; i++) {
			forecasts[i] = ThreadLocalRandom.current().nextInt(min, max + 1)/100.0;
			coverages1[i] = ThreadLocalRandom.current().nextInt(min, max + 1)/100.0;
			coverages2[i] = ThreadLocalRandom.current().nextInt(min, max + 1)/100.0;
		}
		ScheduleChart chart = new ScheduleChart();
		
		LocalDateTime startDate = LocalDateTime.parse("2022-02-07T00:00:00");
		
		chart.chartTwoSheet("Demand & Coverage", "schedule", startDate, timeInterval, forecasts, coverages1, coverages2);
	}

	public ScheduleChart() {
		
	}

	public void chartTwoSheet(String sheetName, String fileName, LocalDateTime startDate, int timeInterval,
			double[] forecasts, double[]... coverages) throws FileNotFoundException, IOException {
			String[] headers = createHeader(startDate, timeInterval, forecasts.length);
			chartTwoSheet(sheetName, fileName, headers, forecasts, coverages);
	}
	
	public void chartTwoSheet(String sheetName, String fileName, 
			String headers[], 
			double[] forecasts, double[]... coverages) throws FileNotFoundException, IOException {
		try (XSSFWorkbook wb = new XSSFWorkbook()) {
			//Create a sheet
			XSSFSheet chartSheet = wb.createSheet(sheetName);
			drawTableChart(chartSheet, headers, forecasts, coverages);
			
			XSSFSheet statSheet = wb.createSheet("statistic");
			fillStatSheet(statSheet);
			
			// Write output to an excel file
			String filename = fileName + ".xlsx";
			try (FileOutputStream fileOut = new FileOutputStream(filename)) {
				wb.write(fileOut);
			}			
		}		
	}

	private void fillStatSheet(XSSFSheet sheet) {
		//TODO:
	}
	
	private final static int CHART_WIDTH_ONE_DAY = 30;
	private final static int DATA_SIZE_ONE_DAY = 4 * 24;
	
	private void drawTableChart(XSSFSheet sheet, String headers[], 
			double[] forecasts, double[]... coverages) {

		int numOfDay = (int) Math.ceil((double)headers.length/DATA_SIZE_ONE_DAY);
		
		//draw chart area
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		
		int col1 = 0;
		int row1 = 5;
		int col2 = numOfDay * CHART_WIDTH_ONE_DAY;
		int row2 = 30;
		
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, col2, row2);
		
		XSSFChart chart = drawing.createChart(anchor);
		chart.setTitleText(charTitle);
		chart.setTitleOverlay(false);
		
		//dray x-y Axis
		XDDFChartLegend legend = chart.getOrAddLegend();
		//legend.setPosition(LegendPosition.TOP_RIGHT);
		legend.setPosition(LegendPosition.TOP);
		
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		bottomAxis.setTitle(xAxisTitle);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		leftAxis.setTitle(yAxisTitle);			
		
		//create Table title
		Row row = sheet.createRow(row2 + 2);
		Cell cell = row.createCell(5, CellType.STRING);
		cell.setCellValue(coverageDataTableTitle);
		cell.setCellStyle(null);
		sheet.addMergedRegion(new CellRangeAddress(row2 + 2, row2 + 2, 5, 10));
		
		Font newFont = cell.getSheet().getWorkbook().createFont();
	    newFont.setBold(true);
	    newFont.setColor((short)20);
	    newFont.setFontHeightInPoints((short)100);
	     
		//create Table header
		int firstRow = row2 + 4;
		int rowCount = firstRow;
		int solutionSize = coverages.length;
		
		int firstCol = 1;
		int totalColumns = forecasts.length;
		
		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellValue("date/time");
		for(int i = 0; i < totalColumns; i++) {
			cell = row.createCell(i + firstCol);
			cell.setCellValue(headers[i]);
		}
		
		//Forecast data
		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellValue(DemandTitle);
		for(int i = 0; i < totalColumns; i++) {
			cell = row.createCell(i + firstCol);
			cell.setCellValue(forecasts[i]);
		}			
		
		//coverage data
		for(int rowIndex = 0; rowIndex < coverages.length; rowIndex++) {
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellValue(coverageTitle + (rowIndex + 1));
			for(int colIndex = 0; colIndex < totalColumns; colIndex++) {
				cell = row.createCell(colIndex + firstCol);
				cell.setCellValue(coverages[rowIndex][colIndex]);
			}			
		}
		
		//create data source
		rowCount = firstRow;
		XDDFDataSource<String> timeLine = XDDFDataSourcesFactory.fromStringCellRange(sheet,
				new CellRangeAddress(rowCount, rowCount, firstCol, firstCol + totalColumns - 1));
		rowCount++;
		
		//forecastLine
		XDDFNumericalDataSource<Double> forecastLine = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
				new CellRangeAddress(rowCount, rowCount, firstCol, firstCol + totalColumns - 1));
		rowCount++;
		
		XDDFNumericalDataSource<Double>[] datas = new XDDFNumericalDataSource[solutionSize];
		for(int i = 0; i < solutionSize; i++) {
			datas[i] = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
					new CellRangeAddress(rowCount, rowCount, firstCol, firstCol + totalColumns - 1));
			rowCount++;
		}

		XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
		
		XDDFLineChartData.Series demand = (XDDFLineChartData.Series) data.addSeries(timeLine, forecastLine);
		demand.setTitle(DemandTitle, null);
		demand.setSmooth(false);
		demand.setMarkerSize((short) 6);
		demand.setMarkerStyle(MarkerStyle.STAR);
		lineSeriesColor(demand, XDDFColor.from(hex2Rgb(Orange)));
		
		for(int i = 0; i < solutionSize; i++) {
			XDDFLineChartData.Series coverage = (XDDFLineChartData.Series) data.addSeries(timeLine, datas[i]);
			
			coverage.setTitle(coverageTitle + (i+1), null);
			coverage.setSmooth(false);
			coverage.setMarkerSize((short) 6);
			coverage.setMarkerStyle(MarkerStyle.SQUARE);
			lineSeriesColor(coverage, XDDFColor.from(hex2Rgb(Data_Colors[i])));
		}			
		chart.plot(data);		
	}
	
	private void lineSeriesColor(XDDFChartData.Series series, XDDFColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(color);
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        } 
        
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }
	
	private byte[] hex2Rgb(String colorStr) {
        int r = Integer.valueOf(colorStr.substring(1, 3), 16);
        int g = Integer.valueOf(colorStr.substring(3, 5), 16);
        int b = Integer.valueOf(colorStr.substring(5, 7), 16);      
        return new byte[]{(byte) r, (byte) g, (byte) b};
    }	
	
	private String[] createHeader(LocalDateTime startDate, int timeInterval, int size) {
		String headers[] = new String[size];
		int day = 0;
		for(int i = 0; i < size; i++) {
			int current = startDate.getDayOfYear();
			if(day != current) {
				headers[i] = startDate.format(sDayFormatter);
			} else {
				headers[i] = startDate.format(timeFormatter);				
			}
			day = current;
			startDate = startDate.plusMinutes(timeInterval);
		}
		
		return headers;
	}
		
}
