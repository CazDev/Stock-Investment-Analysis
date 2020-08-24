import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StockAnalyser {
	/**
	 * Read the stock data from a csv file, then return an ArrayList containing
	 * StockSnapshot objects generated from the data in the csv (each entry in the
	 * ArrayList corresponds to a line in the csv file)
	 * 
	 * @param infile the String representing the name of the file to be opened
	 * @throws IOException if file is not found
	 * @return an arraylist containing StockSnapshot objects 
	 */
	public static ArrayList<StockSnapshot> readStockData(String infile) throws IOException{
		ArrayList<StockSnapshot> stock = new ArrayList<>();
		BufferedReader in = new BufferedReader(new FileReader(infile));
		
		String s;
		s = in.readLine(); // skip the first line, i.e the CSV headers
		while ((s = in.readLine()) != null) {
			
			// extract the rest of the data (comma separated)
			String[] data = s.split(",",-1);
			
			// first column of dates
			String date = null;
			try {
				date = data[0];
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// second column of values
			Float value = 0f;
			try {
				value = Float.parseFloat(data[1]);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// create a new Movie
			stock.add(new StockSnapshot(date, value, "ASX200"));
		}
		return stock;
	}
	
	public static Float percentIncrease(ArrayList<StockSnapshot> stock, Float initialValue, Float finalValue) {
		Float percentIncrease = 100 * (finalValue - initialValue) / initialValue;
		return percentIncrease;
		
	}
	
	public static Float smartInvest(ArrayList<StockSnapshot> stock, Float amount, String date, Float futureInvestTotal, Float incrementalInvestment) {
		StockSnapshot s = findDate(stock, date);
		
		Float total = amount;
		
		Float prevValue = s.getValue();
		Float curValue = s.getValue();
		
		for (int i = stock.indexOf(s); i < stock.size(); i++) {
			
			// check futureInvestTotal has been used
			if (futureInvestTotal <= 0) {
				return total;
			}
			
			curValue = stock.get(i).getValue();
			
			// Calculate percentage increase then convert to multiplier (5 decimal places)
			Float increase = (float) (Math.round(((percentIncrease(stock, prevValue, curValue) / 100) + 1) * 100000.0) / 100000.0);
			
			total += incrementalInvestment;
			total *= increase;
			
			futureInvestTotal -= incrementalInvestment;
			
			//System.out.println(total + "  " + stock.get(i).getDate());
			
			prevValue = stock.get(i-1).getValue();
		}
		return total;
	}
	
	public static StockSnapshot findDate(ArrayList<StockSnapshot> stock, String date) {
		for (int i = 0; i < stock.size(); i++) {
			String[] dateParts = date.split("/");
			if (stock.get(i).getDate().contains(dateParts[1] + "/" + dateParts[2])) {
				return stock.get(i);
			}
		}
		return null;
	}
	
	public static StockSnapshot todayStock(ArrayList<StockSnapshot> stock) {
		return stock.get(stock.size()-1);
	}
	
	public static void main(String[] args) {
		
		ArrayList<StockSnapshot> stock = new ArrayList<>();
		
		try {	
			stock = readStockData("src\\ASX200-weekly.csv");
		}
		catch (IOException e) {
			System.out.println("Exception: " + e);
		}
		
		// check if the stock data was read correctly
		/*
		System.out.println(stock.size());
		System.out.println(stock.get(0).getStock());
		System.out.println(stock.get(0).getDate());
		System.out.println(stock.get(0).getValue());
		*/
		
		//Initial invest
		Float amount = 5000f;
		String date = "01/01/2005";
		String untilDate = "24/08/2020";
		
		//Smart invest
		Float initialAmount = 1000f;
		Float futureAmount = 4000f;
		Float incrementalInvest = 4f;
		
		// Percent Increase
		Float finalValue = findDate(stock, untilDate).getValue();
		Float initialValue = findDate(stock, date).getValue();
		
		Float increase = percentIncrease(stock, initialValue, finalValue);
		
		System.out.println("Invest: " + amount + " at " + date + " until " + untilDate);
		System.out.println("Total return: " + amount * ((increase / 100)+1));
		System.out.println("Percent increase: " + increase + "%");
		System.out.println("Smart Invest: " + initialAmount + " and then " + incrementalInvest + " a week from " + date + " until " + untilDate);
		System.out.println("Total return: " + smartInvest(stock, initialAmount, date, futureAmount, incrementalInvest));
	}
}
