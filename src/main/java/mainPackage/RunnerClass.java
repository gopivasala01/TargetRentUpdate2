package mainPackage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
	public static ArrayList<String> failedBuildings = new ArrayList<String>();;
	public static String[][] completedBuildingList;
	public static String currentDate = "";
	public static HashMap<String,String> failedReaonsList= new HashMap<String,String>();
	public static void main(String[] args) throws Exception
	{
		//Get Pending Buildings from DataBase
		boolean getBuildings =  GetDatafromDatabase.getBuildingsList();
		if(getBuildings==true)
		{
			saveButtonOnAndOff = true;
			try
			{
			for(int i=0;i<pendingBuildingList.length;i++)
			{
				updateStatus=0;
				company = pendingBuildingList[i][0];
				building = pendingBuildingList[i][1].trim();
				targetRent = pendingBuildingList[i][2];
				targetDeposit = pendingBuildingList[i][3];
			    RunnerClass.runAutomation(company,building,targetRent,targetDeposit);
			    if(updateStatus==0)
			    {
			    	successBuildings.add("'"+building+"'");
			    }
			    else 
			    {
			    	failedBuildings.add("'"+building+"'");
			    }
			    driver.close();
			 }
			}
			catch(Exception e) {}
			String success = String.join(",",successBuildings);
			String failed = String.join(",",failedBuildings);
			try
			{
				if(successBuildings.size()>0)
				{
				String updateSuccessStatus = "update automation.TargetRent Set Status ='Completed',StatusID=4, completedOn = getdate() where [Building/Unit Abbreviation] in ("+success+")";
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
			
			//Send Email with status attachment
			if(pendingBuildingList.length>0)
			CommonMethods.createExcelFileWithProcessedData();
			
		}
	}

	public static boolean runAutomation(String company,String building,String targetRent, String targetDeposit) throws Exception
	{
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        currentDate = dateObj.format(formatter);
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
}
