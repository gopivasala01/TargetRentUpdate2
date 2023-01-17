package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RunnerClass 
{
	public static	ChromeDriver driver;
	public static ChromeOptions options;
	public static Actions actions;
	public static JavascriptExecutor js;
	public static WebDriverWait wait;
	public static String[][] pendingBuildingList;
	public static String company;
	public static String building;
	public static String targetRent;
	public static String targetDeposit;
	public static boolean saveButtonOnAndOff;
	public static int updateStatus;
	public static String failedReason ="";
	public static ArrayList<String> successBuildings = new ArrayList<String>();
	public static ArrayList<String> failedBuildings = new ArrayList<String>();
	public static String[][] completedBuildingList;
	public static String [] statusList;
	public static String currentDate = "";
	public static HashMap<String,String> failedReaonsList= new HashMap<String,String>();
	public static String leaseStatuses[][];
	public static String UWStatuses[][];
	public static boolean published;
	public static boolean listingAgent;
	public static void main(String[] args) throws Exception
	{
		//Get Pending Buildings from DataBase
		int w=0;
		String pendingList = AppConfig.quertyToFetchPendingBuildingsListFromETLSource;
		boolean getBuildings =  GetDatafromDatabase.getBuildingsList(pendingList);
		while(w<2)
		{
		GetDatafromDatabase.getStatusFromFactTables();
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        currentDate = dateObj.format(formatter);
		if(getBuildings==true)
		{
			saveButtonOnAndOff = false;
			try
			{
			for(int i=0;i<pendingBuildingList.length;i++)
			{
				updateStatus=0;
				company = pendingBuildingList[i][0];
				building = pendingBuildingList[i][1].trim();
				targetRent = pendingBuildingList[i][2];
				targetDeposit = pendingBuildingList[i][3];
				System.out.println(company+"   |  "+building);
				if(CommonMethods.checkForBuildingStatusInFactTables(company, building)==true)
			    //continue;
				RunnerClass.runAutomation(company,building,targetRent,targetDeposit);
				else
				{
					updateStatus=1;
				}
			    if(updateStatus==0)
			    {
			    	successBuildings.add("'"+building+"'");
			    	if(failedBuildings.contains(building))
			    	{
			    		failedBuildings.remove(building);
			    	}
			    }
			    else 
			    {
			    	failedBuildings.add("'"+building+"'");
			    }
                try {
			    driver.quit();}catch(Exception e) {}
                System.out.println("Record = "+i);
			 }
			}
			catch(Exception e) {}
		}
		String failedList = AppConfig.failedBuildingsList;
		getBuildings =  GetDatafromDatabase.getBuildingsList(failedList);
		w++;
		}
			String success = String.join(",",successBuildings);
			String failed = String.join(",",failedBuildings);
			try
			{
				if(successBuildings.size()>0)
				{
				String updateSuccessStatus = "update automation.TargetRent Set Status ='Completed',StatusID=4, completedOn = getdate(), Notes=null where [Building/Unit Abbreviation] in ("+success+")";
		    	GetDatafromDatabase.updateTable(updateSuccessStatus);
				}
				if(failedBuildings.size()>0)
				{
				String failedReasons = String.join(",",failedReaonsList.values());
				String failedBuildings = String.join(",",failedReaonsList.keySet());
				String failedBuildingsUpdateQuery = "";
				for(int i=0;i<failedReaonsList.size();i++)
				{
					String buildingAbbr = failedBuildings.split(",")[i].trim();
					String failedReason = failedReasons.split(",")[i].trim();
					failedBuildingsUpdateQuery =failedBuildingsUpdateQuery+"\nupdate automation.TargetRent Set Status ='Failed',StatusID=3, completedOn = getdate(),Notes='"+failedReason+"' where [Building/Unit Abbreviation] ='"+buildingAbbr+"'";
					
				}
		    	//String updateFailedStatus = "update automation.TargetRent Set Status ='Failed', completedOn = getdate(),Notes='"+failedReason+"' where [Building/Unit Abbreviation] in ("+failed+")";
		    	GetDatafromDatabase.updateTable(failedBuildingsUpdateQuery);
				}
			}
			catch(Exception e) {}
			
			//
			
			//Send Email with status attachment
			if(pendingBuildingList.length>0)
			CommonMethods.createExcelFileWithProcessedData();
			
	}

	public static boolean runAutomation(String company,String building,String targetRent, String targetDeposit) throws Exception
	{
		//Open Browser
		if(CommonMethods.openBrowser()==false)
		return false;
		//Login to PropertyWare
		if(CommonMethods.loginToPropertyWare()==false)
		return false;
		//Perform Automation
		if(CommonMethods.enterDetailsToBuilding(company, building, targetRent, targetDeposit)==false)
			return false;
		
		return true;
	}
	
	public static long dateDifferenceInDays(String date1, String date2)
	{
		SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");

		try {
		    Date date_1 = myFormat.parse(date1);
		    Date date_2 = myFormat.parse(date2);
		    long diff = date_2.getTime() - date_1.getTime();
		    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		    return days;
		} 
		catch (ParseException e) 
		{
		    e.printStackTrace();
		}
		return 0;
	}
	
}
