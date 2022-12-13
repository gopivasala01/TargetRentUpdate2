package mainPackage;

public class AppConfig 
{
   public static String propertyWareURL ="https://app.propertyware.com/pw/login.jsp";
   public static String username ="mds0418@gmail.com";
   public static String password ="HomeRiver1#";
   
   public static String quertyToFetchPendingBuildingsListFromETLSource = "Select Company,[Building/Unit Abbreviation],TargetRent,TargetDeposit from automation.TargetRent where Status ='Pending' and Source ='ETL'";
   public static String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T";

   public static String reasonForChange = "HRG - Automation";
   
   public static String updateSuccessStatus = "update automation.TargetRent Set Status ='Completed', ";
   public static String excelFileLocation = "E:\\Automation\\Target Rent Data";
   public static String getBuildingsWithStatusforCurrentDay = "Select Company,[Building/Unit Abbreviation],TargetRent,TargetDeposit,Status from automation.TargetRent where Source ='ETL' and FoRMAT(completedOn,'MM-dd-yyyy')=FoRMAT(getdate(),'MM-dd-yyyy')";
   
   //Mail credentials
   public static String fromEmail = "bireports@beetlerim.com";
   public static String fromEmailPassword = "Welcome@123";
   
   public static String toEmail = "gopi.v@beetlerim.com";
   public static String CCEmail = "gopi.v@beetlerim.com";
   
   public static String mailSubject = "Target Rent Update for  ";
   
}
