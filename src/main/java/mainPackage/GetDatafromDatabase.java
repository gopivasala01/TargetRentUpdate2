package mainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetDatafromDatabase 
{

	public static void updateTable(String query)
	 {
		    try (Connection conn = DriverManager.getConnection(AppConfig.connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(query);
		      System.out.println("Record Updated");
		      stmt.close();
	            conn.close();
		    } catch (SQLException e) 
		    {
		      e.printStackTrace();
		    }
	 }
	
	
	public static boolean getBuildingsList(String query)
	{
		try
		{
		        Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		            con = DriverManager.getConnection(AppConfig.connectionUrl);
		            String SQL = query;
		            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		           // stmt = con.createStatement();
		            rs = stmt.executeQuery(SQL);
		            int rows =0;
		            if (rs.last()) {
		            	rows = rs.getRow();
		            	// Move to beginning
		            	rs.beforeFirst();
		            }
		            System.out.println("No of Rows = "+rows);
		            RunnerClass.pendingBuildingList = new String[rows][4];
		           int  i=0;
		            while(rs.next())
		            {
		            	
		            	String 	company =  (String) rs.getObject(1);
		                String  buildingAbbreviation = (String) rs.getObject(2);
		                String  targetRent = (String) rs.getObject(3);
		                String  targetDeposit = (String) rs.getObject(4);
		                System.out.println(company +" ----  "+buildingAbbreviation+" ---- "+targetRent+" ---- "+targetDeposit);
		    				//Company
		    				RunnerClass.pendingBuildingList[i][0] = company;
		    				//Port folio
		    				RunnerClass.pendingBuildingList[i][1] = buildingAbbreviation;
		    				//Lease Name
		    				RunnerClass.pendingBuildingList[i][2] = targetRent;
		    				//Target Deposit
		    				RunnerClass.pendingBuildingList[i][3] = targetDeposit;
		    				i++;
		            }	
		            System.out.println("Total Pending Buildings  = " +RunnerClass.pendingBuildingList.length);
		            //for(int j=0;j<RunnerClass.pendingBuildingList.length;j++)
		            //{
		            //	System.out.println(RunnerClass.pendingBuildingList[j][j]);
		           // }
		            rs.close();
		            stmt.close();
		            con.close();
		 return true;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		 return false;
		}
	}
	public static boolean getBuildingEntityID(String company, String buildingAbbrivation)
	{
	try
	{
	        Connection con = null;
	        Statement stmt = null;
	        ResultSet rs = null;
	            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	            con = DriverManager.getConnection(AppConfig.connectionUrl);
	            String SQL = "Select BuildingEntityID from Buildings_Dashboard where Company='" + company +"' and BuildingAbbreviation like '%"+buildingAbbrivation+"%'";
	            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	           // stmt = con.createStatement();
	            rs = stmt.executeQuery(SQL);
	            int rows =0;
	            if (rs.last()) 
	            {
	            	rows = rs.getRow();
	            	// Move to beginning
	            	rs.beforeFirst();
	            }
	            if(rs.getFetchSize()>1) {
	            	return false;
	            	
	            }
	            System.out.println("No of Rows = "+rows);
	            RunnerClass.BuildingEntityIDFromBuildingDashboard = new String[rows][1];
	           int  i=0;
	            while(rs.next())
	            {
	  
	            	String 	buildingEntityID = rs.getObject(1).toString();
	            	
	              //stateCode
	                try 
	                {
	                	if(buildingEntityID==null)
	                		RunnerClass.BuildingEntityIDFromBuildingDashboard[i][0] = "";
	                	else
	                	{
	    				RunnerClass.BuildingEntityIDFromBuildingDashboard[i][0] = buildingEntityID;
	    				RunnerClass.buildingEntityID = RunnerClass.BuildingEntityIDFromBuildingDashboard[i][0];
	                	}
	                }
	                catch(Exception e)
	                {
	                	RunnerClass.BuildingEntityIDFromBuildingDashboard[i][0] = "";
	                }
	            }
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return true;
}
	public static boolean getCompletedBuildingsList()
	{
		try
		{
		        Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		            con = DriverManager.getConnection(AppConfig.connectionUrl);
		            String SQL = AppConfig.getBuildingsWithStatusforCurrentDay;
		            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		           // stmt = con.createStatement();
		            rs = stmt.executeQuery(SQL);
		            int rows =0;
		            if (rs.last()) {
		            	rows = rs.getRow();
		            	// Move to beginning
		            	rs.beforeFirst();
		            }
		            System.out.println("No of buildings with status = "+rows);
		            RunnerClass.completedBuildingList = new String[rows][8];
		           int  i=0;
		            while(rs.next())
		            {
		            	
		            	String 	company =  (String) rs.getObject(1);
		                String  buildingAbbreviation = (String) rs.getObject(2);
		                String  thirdPartyUnitID = (String) rs.getObject(3);
		                String  targetRent = (String) rs.getObject(4);
		                String  targetDeposit = (String) rs.getObject(5);
		                String  listingAgent = (String) rs.getObject(6);
		                String  status = (String) rs.getObject(7);
		                String  notes = (String) rs.getObject(8);
		                System.out.println(company +" | "+buildingAbbreviation+" | "+targetRent+" | "+targetDeposit+" | "+listingAgent+" | "+status+" | "+notes);
		    				//Company
		    				RunnerClass.completedBuildingList[i][0] = company;
		    				//Port folio
		    				RunnerClass.completedBuildingList[i][1] = buildingAbbreviation;
		    				//Third Party Unit ID
		    				RunnerClass.completedBuildingList[i][2] = thirdPartyUnitID;
		    				//Lease Name
		    				RunnerClass.completedBuildingList[i][3] = targetRent;
		    				//Target Deposit
		    				RunnerClass.completedBuildingList[i][4] = targetDeposit;
		    				//Listing Agent
		    				RunnerClass.completedBuildingList[i][5] = listingAgent;
		    				//Status
		    				RunnerClass.completedBuildingList[i][6] = status;
		    				//Notes
		    				RunnerClass.completedBuildingList[i][7] = notes;
		    				i++;
		            }	
		           // System.out.println("Total Pending Buildings  = " +RunnerClass.pendingBuildingList.length);
		            //for(int j=0;j<RunnerClass.pendingBuildingList.length;j++)
		            //{
		            //	System.out.println(RunnerClass.pendingBuildingList[j][j]);
		           // }
		            rs.close();
		            stmt.close();
		            con.close();
		 return true;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		 return false;
		}
	}
	
	public static boolean getStatusFromFactTables()
	{
		try
		{
		        Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		            con = DriverManager.getConnection(AppConfig.connectionUrl);
		            String SQL = AppConfig.statusListFromFactTables;
		            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		           // stmt = con.createStatement();
		            rs = stmt.executeQuery(SQL);
		            int rows =0;
		            if (rs.last()) {
		            	rows = rs.getRow();
		            	// Move to beginning
		            	rs.beforeFirst();
		            }
		            System.out.println("No of Statuses with N = "+rows);
		            RunnerClass.statusList = new String[rows];
		           int  i=0;
		            while(rs.next())
		            {
		            	
		            	String 	status =  (String) rs.getObject(1);
		    				//Status
		    				RunnerClass.statusList[i] = status;
		    				i++;
		            }	
		            rs.close();
		            stmt.close();
		            con.close();
		 return true;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		 return false;
		}
	}
	public static void getBuildingStatus(String query,String table)
	{
		String status="";
		try
		{
		        Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		            con = DriverManager.getConnection(AppConfig.connectionUrl);
		            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		           // stmt = con.createStatement();
		            rs = stmt.executeQuery(query);
		            int rows =0;
		            if (rs.last()) {
		            	rows = rs.getRow();
		            	// Move to beginning
		            	rs.beforeFirst();
		            }
		            System.out.println("No of Statuses with N = "+rows);
		            if(table=="Lease")
		            RunnerClass.leaseStatuses = new String[rows][2];
		            else RunnerClass.UWStatuses = new String[rows][2];
		            int i=0;
		            while(rs.next())
		            {
		            	if(table=="Lease")
		            	{
		            	String ID = rs.getObject(1).toString();
		            	String Status = (String) rs.getObject(2);
		            	//LeaseID
	    				RunnerClass.leaseStatuses[i][0] = ID;
	    				//Lease Status
	    				RunnerClass.leaseStatuses[i][1] = Status;
	    				
		            	}
		            	if(table=="UW")
		            	{
		            		String ID =  rs.getObject(1).toString();
			            	String Status = (String) rs.getObject(2);
			            	//UW ID
		    				RunnerClass.UWStatuses[i][0] = ID;
		    				//UW Status
		    				RunnerClass.UWStatuses[i][1] = Status;
		            	
		            	}
		            	i++;
		            }
		           // else status="";
		            rs.close();
		            stmt.close();
		            con.close();
		            /*
		            if(table=="Lease")
		            return RunnerClass.leaseStatuses;
		            else return RunnerClass.UWStatuses;
		            */
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		// return null;
		}
	}
	
	public static int getDateDifference(String query)
	{
		int status=0;
		try
		{
		        Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		            con = DriverManager.getConnection(AppConfig.connectionUrl);
		            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		           // stmt = con.createStatement();
		            rs = stmt.executeQuery(query);
		            int rows =0;
		            if (rs.last()) {
		            	rows = rs.getRow();
		            	// Move to beginning
		            	rs.beforeFirst();
		            }
		            System.out.println("No of Statuses with N = "+rows);
		            while(rs.next())
		            {
		            	
		            	status =  (Integer) rs.getObject(1);
		            }
		           // else status="";
		            rs.close();
		            stmt.close();
		            con.close();
		            return status;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		 return 0;
		}
	}
}
