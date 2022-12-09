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
}
