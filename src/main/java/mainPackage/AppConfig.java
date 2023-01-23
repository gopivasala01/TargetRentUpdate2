package mainPackage;

public class AppConfig 
{
   public static String propertyWareURL ="https://app.propertyware.com/pw/login.jsp";
   public static String username ="mds0418@gmail.com";
   public static String password ="HomeRiver1#";
   
   public static String quertyToFetchPendingBuildingsListFromETLSource = "Select Company,[Building/Unit Abbreviation],TargetRent,TargetDeposit from automation.TargetRent where Sno>1566 and Notes ='Unable to update details' or Status ='Pending'";
   public static String failedBuildingsList = "Select Company,[Building/Unit Abbreviation],TargetRent,TargetDeposit from automation.TargetRent where Notes in ('Target Deposit is not updated','Unable to update details','Error logging into PW','Error opening browser','Building Not Found','Issue in selecting Building','Target Rent is not updated','')";
   public static String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T";

   public static String reasonForChange = "HRG - Automation";
   
   public static String updateSuccessStatus = "update automation.TargetRent Set Status ='Completed', ";
   public static String excelFileLocation = "E:\\Automation\\Target Rent Data";
   public static String getBuildingsWithStatusforCurrentDay = "Select Company,[Building/Unit Abbreviation],TargetRent,TargetDeposit,ListingAgent,Status,notes from automation.TargetRent where Source ='ETL' and FoRMAT(completedOn,'MM-dd-yyyy')=FoRMAT(getdate(),'MM-dd-yyyy')";
   
   //Mail credentials
   public static String fromEmail = "bireports@beetlerim.com";
   public static String fromEmailPassword = "Welcome@123";
   
   public static String toEmail ="gopi.v@beetlerim.com";// "msanfilippo@homeriver.com,amesa@homeriver.com";
   public static String CCEmail = "gopi.v@beetlerim.com";
   
   public static String mailSubject = "Target Rent Update for  ";
   
   public static String statusListFromFactTables = "Select Status from TargetRentStatusConsideration Where [Y/N]='N'";
   
  
}


//String failedList = AppConfig.failedBuildingsList;
		//getBuildings =  GetDatafromDatabase.getBuildingsList(failedList);
		//w++;
		//}
		
		/*
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
			
*/