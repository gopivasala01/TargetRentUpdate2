package mainPackage;

import java.time.Duration;
import java.util.ArrayList;
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
	public static void main(String[] args) throws Exception
	{
		//Get Pending Buildings from DataBase
		boolean getBuildings =  GetDatafromDatabase.getBuildingsList();
		if(getBuildings==true)
		{
			saveButtonOnAndOff = false;
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
			String success = String.join(",",successBuildings);
			String failed = String.join(",",failedBuildings);
			try
			{
				if(successBuildings.size()>0)
				{
				String updateSuccessStatus = "update automation.TargetRent Set Status ='Completed', completedOn = getdate() where [Building/Unit Abbreviation] in ("+success+")";
		    	GetDatafromDatabase.updateTable(updateSuccessStatus);
				}
				if(failedBuildings.size()>0)
				{
		    	String updateFailedStatus = "update automation.TargetRent Set Status ='Failed', completedOn = getdate(),Notes='"+failedReason+"' where [Building/Unit Abbreviation] =("+failed+")";
		    	GetDatafromDatabase.updateTable(updateFailedStatus);
				}
			}
			catch(Exception e) {}
		}
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
}
