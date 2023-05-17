package mainPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CommonMethods 
{
	public static boolean openBrowser()
	{
		try
		{
		RunnerClass.options = new ChromeOptions();
		//RunnerClass.options.setExperimentalOption("prefs", prefs);
		RunnerClass.options.addArguments("--remote-allow-origins=*");
		WebDriverManager.chromedriver().setup();
		RunnerClass.driver= new ChromeDriver(RunnerClass.options);
		RunnerClass.actions = new Actions(RunnerClass.driver);
		RunnerClass.js = (JavascriptExecutor)RunnerClass.driver;
		RunnerClass.driver.manage().window().maximize();
		RunnerClass.driver.manage().timeouts().implicitlyWait(50,TimeUnit.SECONDS);
		RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(50));
		return true;
		}
		catch(Exception e)
		{
			System.out.println("Error Opening Browser");
			RunnerClass.failedReason = "Error opening browser";
			RunnerClass.updateStatus=1;
			return false;
		}
	}
	public static boolean loginToPropertyWare() throws Exception
	{
		try
		{
		RunnerClass.driver.get(AppConfig.propertyWareURL);
		Thread.sleep(3000);
		RunnerClass.driver.findElement(Locators.userName).sendKeys(AppConfig.username);
		RunnerClass.driver.findElement(Locators.password).sendKeys(AppConfig.password);
		RunnerClass.driver.findElement(Locators.signMeIn).click();
		Thread.sleep(3000);
		return true;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			RunnerClass.failedReaonsList.put(RunnerClass.building, "Error logging into PW");
			RunnerClass.failedReason = "Error logging into PW";
			RunnerClass.updateStatus=1;
			return false;
		}
	}
	
	public static void popUpHandling()
	{
		RunnerClass.driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		try
		{
			if(RunnerClass.driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
			{
				RunnerClass.driver.findElement(Locators.popupClose).click();
			}
		}
		catch(Exception e) {};
		RunnerClass.driver.manage().timeouts().implicitlyWait(150,TimeUnit.SECONDS);
	}
	
	public static boolean enterDetailsToBuilding(String company,String building,String targetRent, String targetDeposit) throws Exception
	{
		RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(10));
		RunnerClass.driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		//Thread.sleep(3000);
		CommonMethods.popUpHandling();
		RunnerClass.js.executeScript("window.scrollBy(0, -document.body.scrollHeight)");
		RunnerClass.driver.navigate().refresh();
		try
		{
	    //RunnerClass.driver.findElement(Locators.dashboardsTab).click();
		RunnerClass.driver.findElement(Locators.searchbox).clear();
		RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
		RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(300));
			try
			{
			RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
			}
			catch(Exception e)
			{
				try
				{
				RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
				RunnerClass.driver.navigate().refresh();
				CommonMethods.popUpHandling();
				RunnerClass.driver.findElement(Locators.dashboardsTab).click();
				RunnerClass.driver.findElement(Locators.searchbox).clear();
				RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
				RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
				}
				catch(Exception e2) {}
			}
			try
			{
			RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
			if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
			{
				long count = building.chars().filter(ch -> ch == '.').count();
				if(building.chars().filter(ch -> ch == '.').count()>=2)
				{
					building = building.substring(building.lastIndexOf(".")+1);
					RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					RunnerClass.driver.navigate().refresh();
					CommonMethods.popUpHandling();
					RunnerClass.driver.findElement(Locators.dashboardsTab).click();
					RunnerClass.driver.findElement(Locators.searchbox).clear();
					RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
					RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
					try
					{
					RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
					{
						System.out.println("Building Not Found");
					    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
						return false;
					}
					}
					catch(Exception e3) {}
				}
				else
				{
				try
				 {
					if(building.contains("."))
						building = building.substring(building.indexOf(".")+1,building.length());
					else 
					if(building.contains("_"))
				  building = building.split("_")[1];
					else 
						building = RunnerClass.completeBuildingAbbreviation.substring(RunnerClass.completeBuildingAbbreviation.indexOf("(")+1,RunnerClass.completeBuildingAbbreviation.indexOf(")"));
					
				 if( CommonMethods.searchingBuildingWithDifferentText(building)==false)
				 {
					 System.out.println("Building Not Found");
			         RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
				     return false;
				 }
				 }
				 catch(Exception e)
				 {
			     System.out.println("Building Not Found");
		         RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
			     return false;
				 }
				}
			}
			}
			catch(Exception e2)
			{
			}
			RunnerClass.driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
			Thread.sleep(1000);
			System.out.println(building);
		// Select Lease from multiple leases
				List<WebElement> displayedCompanies = RunnerClass.driver.findElements(Locators.searchedLeaseCompanyHeadings);
				boolean leaseSelected = false;
				for(int i =0;i<displayedCompanies.size();i++)
				{
					String companyName = displayedCompanies.get(i).getText();
					if(companyName.contains(company)&&!companyName.contains("Legacy"))
					{
						//RunnerClass.driver.findElement(By.xpath("(//*[@class='searchCat4'])["+(i+1)+"]/a")).click();
						//break;
						//RunnerClass.driver.findElement(By.partialLinkText(leaseName)).click();
						
						List<WebElement> leaseList = RunnerClass.driver.findElements(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li/a"));
						System.out.println(leaseList.size());
						for(int j=0;j<leaseList.size();j++)
						{
							String lease = leaseList.get(j).getText();
							if(lease.toLowerCase().contains(building.toLowerCase()))
							{
								try
								{
								RunnerClass.driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
								leaseSelected = true;
								break;
								}
								catch(Exception e)
								{
									System.out.println("Building Not Found");
									RunnerClass.failedReaonsList.put(building, "Building Not Found");
								    RunnerClass.failedReason = "Building Not Found";
									RunnerClass.updateStatus=1;
									return false;
								}
							}
						}
						
					}
					if(leaseSelected==true) break;
				}
					if(leaseSelected==true)
					{
						Thread.sleep(5000); 
						//Check Listing Agent Type
						try
						{
						String listingAgent = RunnerClass.driver.findElement(Locators.listingAgent).getText();
						RunnerClass.listingAgentName = listingAgent;//.split("\\|")[0].trim();
						System.out.println("Listing Agent Name = "+RunnerClass.listingAgentName);
						if(listingAgent.trim().toLowerCase().contains("Sovereign".toLowerCase())&&listingAgent.trim().toLowerCase().contains("MCH".toLowerCase()))
						{
							RunnerClass.listingAgent =false;
							System.out.println("Unit marketed by Sovereign");
							//RunnerClass.failedReaonsList.put(building, "Unit marketed by Sovereign");
						    RunnerClass.failedReason = "Unit marketed by Sovereign";
							RunnerClass.updateStatus=1;
							return false;
						}
						else RunnerClass.listingAgent =true;
						}
						catch(Exception e) {}
						if(RunnerClass.listingAgent ==true&&RunnerClass.updateStatus==0)
						if(CommonMethods.enterTargetsInBuilding(targetRent, targetDeposit)==false)
						{
							return false;
						}
						return true;
					
					}
					else 
						{
					    System.out.println("Couldn't find Building");
					    RunnerClass.failedReaonsList.put(building, "Building Not Found");
					    RunnerClass.failedReason = "Building Not Found";
						RunnerClass.updateStatus=1;
						return false;
						}
				
		
		//RunnerClass.driver.findElement(Locators.selectSearchedLease).click();
		
		
		
		
		}
		catch(Exception e)
		{
			System.out.println("Issue in selecting Building");
			RunnerClass.failedReaonsList.put(building, "Issue in finding Building");
			RunnerClass.failedReason = "Issue in finding Building";
			RunnerClass.updateStatus=1;
			return false;
		}
	}
	public static boolean enterTargetsInBuilding(String targetRent,String targetDeposit) throws Exception
	{
		
		RunnerClass.published =true;
		RunnerClass.listingAgent = true;
		//Check if Unit is published or not
		try
		{
		String published = RunnerClass.driver.findElement(Locators.published).getText();
		if(!published.trim().toLowerCase().equals("yes"))
		{
			RunnerClass.published =false;
			System.out.println("Unit is not Published");
			//RunnerClass.failedReaonsList.put(building, "Not a Published Unit");
		    RunnerClass.failedReason = "Unit is not Published";
			RunnerClass.updateStatus=1;
			return false;
		}
		}
		catch(Exception e) {}
		Thread.sleep(5000);
		//Check if the Portfolio is MCH
		String portfolioType="";
		try
		{
			portfolioType = RunnerClass.driver.findElement(Locators.portfolioType).getText();
			System.out.println("Portfolio Type = "+portfolioType);
			if(!portfolioType.contains("MCH"))
			{
				System.out.println("Portfolio is not MCH");
				RunnerClass.failedReaonsList.put(RunnerClass.building, "Portfolio is not MCH");
				RunnerClass.failedReason = "Portfolio is not MCH";
				RunnerClass.updateStatus=1;
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		boolean targetRentUpdateCheck = false;
		boolean targetDepositUpdateCheck = false;
		try
		{
			RunnerClass.driver.findElement(Locators.targetRentChangeButton).click();
			Thread.sleep(2000);
			try
			{
			RunnerClass.driver.findElement(Locators.newTargetRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			RunnerClass.driver.findElement(Locators.newTargetRent).sendKeys(targetRent);
			}
			catch(ElementNotInteractableException e)
			{
				RunnerClass.driver.findElement(Locators.newTargetRent2).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				RunnerClass.driver.findElement(Locators.newTargetRent2).sendKeys(targetRent);
			}
			//RunnerClass.actions.click(RunnerClass.driver.findElement(Locators.newTargetRent)).sendKeys(Keys.END).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			RunnerClass.driver.findElement(Locators.reasonForChange).sendKeys(AppConfig.reasonForChange);
			if(RunnerClass.saveButtonOnAndOff==true)
				RunnerClass.driver.findElement(Locators.targetRentSaveButton).click();
			else 
				RunnerClass.driver.findElement(Locators.targetRentCancelButton).click();
			targetRentUpdateCheck = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RunnerClass.driver.navigate().refresh();
			targetRentUpdateCheck = false;
		}
		try
		{
		Thread.sleep(5000);
		RunnerClass.driver.findElement(Locators.editButton).click();
		Thread.sleep(2000);
		RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.targetDeposit));
		//RunnerClass.actions.click(RunnerClass.driver.findElement(Locators.targetDeposit)).sendKeys(Keys.END).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
		RunnerClass.driver.findElement(Locators.targetDeposit).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		RunnerClass.driver.findElement(Locators.targetDeposit).sendKeys(targetDeposit);
		if(RunnerClass.saveButtonOnAndOff==true)
			RunnerClass.driver.findElement(Locators.saveButton).click();
		else 
			RunnerClass.driver.findElement(Locators.cancelButton).click();
		targetDepositUpdateCheck = true;
		//return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			targetDepositUpdateCheck = false;
		}
		
		if(targetRentUpdateCheck==true&&targetDepositUpdateCheck==true)
			return true;
		else if(targetRentUpdateCheck==true&&targetDepositUpdateCheck==false)
		{
			RunnerClass.failedReaonsList.put(RunnerClass.building, "Target Deposit is not updated");
			RunnerClass.failedReason = "Target Deposit is not updated";
			RunnerClass.updateStatus=1;
			return false;
		}else if(targetRentUpdateCheck==false&&targetDepositUpdateCheck==true)
		{
			RunnerClass.failedReaonsList.put(RunnerClass.building, "Target Rent is not updated");
			RunnerClass.failedReason = "Target Rent is not updated";
			RunnerClass.updateStatus=1;
			return false;
		}else 
		{
			RunnerClass.failedReaonsList.put(RunnerClass.building, "Unable to update details");
			RunnerClass.failedReason = "Unable to update details";
			RunnerClass.updateStatus=1;
			return false;
		}
	}
	
	//Create Excel File with processed data
	public static void createExcelFileWithProcessedData()
	{
		//Get Today's date in MMddyyyy format
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String date = dateObj.format(formatter);
        System.out.println(date);
        String filename ;
		try   
		{  
		filename = AppConfig.excelFileLocation+"\\TargetRentUpdate_"+date+".xlsx";  
		File file = new File(filename);
		//if file exists, delete and re create it
		if(file.exists())
		{
			file.delete();
		}
		Workbook wb = new XSSFWorkbook();
		Sheet sheet1 = wb.createSheet("Sheet 1");
		Row header = sheet1.createRow(0);
		header.createCell(0).setCellValue("Company");
		header.createCell(1).setCellValue("Building Abbreviation");
		header.createCell(2).setCellValue("Third Party UnitID");
		header.createCell(3).setCellValue("Target Rent");
		header.createCell(4).setCellValue("Target Deposit");
		header.createCell(5).setCellValue("Listing Agent");
		header.createCell(6).setCellValue("Status");
		header.createCell(7).setCellValue("Failed Notes");
		//int totalCurrentDayBuildings = RunnerClass.successBuildings.size()+RunnerClass.failedBuildings.size();
		//sheet1.createRow(sheet1.getLastRowNum()+totalCurrentDayBuildings);
		boolean getBuildings =  GetDatafromDatabase.getCompletedBuildingsList();
		if(getBuildings==true&&RunnerClass.completedBuildingList!=null)
		{
			for(int i=0;i<RunnerClass.completedBuildingList.length;i++)
			{
				String company = RunnerClass.completedBuildingList[i][0];
				String building = RunnerClass.completedBuildingList[i][1].trim();
				String thirdPartyUnitID = RunnerClass.completedBuildingList[i][2].trim();
				String targetRent = RunnerClass.completedBuildingList[i][3];
				String targetDeposit = RunnerClass.completedBuildingList[i][4];
				String listingAgent = RunnerClass.completedBuildingList[i][5];
				String status = RunnerClass.completedBuildingList[i][6];
				String notes = RunnerClass.completedBuildingList[i][7];
				Row row = sheet1.createRow(1+i);
				row.createCell(0).setCellValue(company);
				row.createCell(1).setCellValue(building);
				row.createCell(2).setCellValue(thirdPartyUnitID);
				row.createCell(3).setCellValue(targetRent);
				row.createCell(4).setCellValue(targetDeposit);
				row.createCell(5).setCellValue(listingAgent);
				row.createCell(6).setCellValue(status);
				row.createCell(7).setCellValue(notes);
				
			}
		
		}
		
		System.out.println("Last row in the sheet = "+sheet1.getLastRowNum());
		FileOutputStream fileOut = new FileOutputStream(filename);  
		wb.write(fileOut);
		wb.close();
		fileOut.close();  
		System.out.println("Excel file has been generated successfully.");  
		CommonMethods.sendFileToMail(filename);
		}   
		catch (Exception e)   
		{  
		e.printStackTrace();  
		}  
		
		//Send Email the attachment
	}
	
	
	public static void sendFileToMail(String fileName) 
	   {
	     
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtpout.asia.secureserver.net";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      //props.put("mail.smtp.starttls.enable", "true");
	     props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "80");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(AppConfig.fromEmail, AppConfig.fromEmailPassword);
	            }
	         });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(AppConfig.fromEmail));

	         InternetAddress[] toAddresses = InternetAddress.parse(AppConfig.toEmail);
	         // Set To: header field of the header.
	        message.setRecipients(Message.RecipientType.TO,
	        		toAddresses);

	        
	        InternetAddress[] CCAddresses = InternetAddress.parse(AppConfig.CCEmail);
	         // Set CC: header field of the header.
	         message.setRecipients(Message.RecipientType.CC,
	        		 CCAddresses);
	         
	         /*
	         // Set CC: header field of the header.
	         message.setRecipients(Message.RecipientType.BCC,
	            InternetAddress.parse("sujana.t@beetlerim.com"));
	         */
	         // Set Subject: header field
	        String subject = AppConfig.mailSubject+RunnerClass.currentDate;
	        message.setSubject(subject);

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         String messageInBody = "Hi All,\n Please find the attachment.\n\n Regards,\n HomeRiver Group.";
	         messageBodyPart.setText(messageInBody);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	        // String filename = "C:\\PropertyWare\\externalFiles\\downloadFiles\\"+"Operations-Marketing.xlsx";
	         System.out.println("FileName sending in mail"+fileName);
	         messageBodyPart.setFileName(new File(fileName).getName());
	         DataSource source = new FileDataSource(fileName);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	        // messageBodyPart.setFileName(filename);
	         messageBodyPart.setFileName(new File(fileName).getName());
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	         //wait until file is downloaded
	         /*
	         File dir = new File("DownloadPath");
	         //String partialName = downloaded_report.split("_")[0].concat("_"); //get cancelled and add underscore
	        // FluentWait<WebDriver> wait = new FluentWait<WebDriver>(RunnerClass.driver);
	                 //wait.pollingEvery(1, TimeUnit.SECONDS);
	                 //wait.withTimeout(15, TimeUnit.SECONDS);
	                 RunnerClass.wait.until(x -> {
	                     File[] filesInDir = dir.listFiles();
	                     for (File fileInDir : filesInDir) {
	                         if (fileInDir.getName().startsWith("Marketing")) {
	                             return true;
	                         }
	                     }
	                     return false;
	                 });
	         */
	         //delete the current file
	         File file = new File(fileName);
	         file.delete();
	      } catch (MessagingException e) 
	      {
	    	  e.printStackTrace();
	         throw new RuntimeException(e);
	      }
	   }
	
	public static boolean checkForBuildingStatusInFactTables(String company, String buildingAbbreviation) throws Exception
	{
		String leaseFactQuery = "Select  Max(ID) as ID, Status from LeaseFact_Dashboard where BuildingAbbreviation ='"+buildingAbbreviation+"'  group by status";
				//"Select  ID,Status from LeaseFact_Dashboard where BuildingAbbreviation = '"+buildingAbbreviation+"'";// and Company ='%"+company+"%'
		String UWFactQuery = "Select  Max(ID) as ID,Status  from underwriting_fact_max where BuildingAbbreviation ='"+buildingAbbreviation+"'  group by status";
				//"Select ID, Status from Underwriting_Max_Table where BuildingAbbreviation = '"+buildingAbbreviation+"'"; // and CompanyName ='"+company+"'
		boolean checkLeaseStatus =false;
		boolean checkUWStatus =false;
		//List<String> buildingStatusFromLeaseFact = GetDatafromDatabase.getBuildingStatus(leaseFactQuery,"Lease");
		GetDatafromDatabase.getBuildingStatus(leaseFactQuery,"Lease");
		//List<String>  buildingStatusFromUWFact = GetDatafromDatabase.getBuildingStatus(UWFactQuery,"UW");
		GetDatafromDatabase.getBuildingStatus(UWFactQuery,"UW");
		 if(RunnerClass.leaseStatuses==null&&RunnerClass.UWStatuses==null)
		 {
			 System.out.println(buildingAbbreviation +" - Building is not available in both Lease and UW tables");
			 return true;
		 }
		 String leaseMatchedStatus ="";
		 String UWMatchedStatus ="";
		 try
		 {
		 for(int i=0;i<RunnerClass.leaseStatuses.length;i++)
		 {
			 String statusFromLeaseTable = RunnerClass.leaseStatuses[i][1];
			 String leaseID = RunnerClass.leaseStatuses[i][0];
			 for(int j=0;j<RunnerClass.statusList.length;j++)
			 {
				 String statusFromExcel = RunnerClass.statusList[j];
			 if(statusFromExcel.equalsIgnoreCase(statusFromLeaseTable))
			 {
				// String dateQuery = "Select top 1 Format(EndDate,'dd MM yyyy') from LeaseFact_Dashboard where BuildingAbbreviation like '%"+buildingAbbreviation+"%'"; // and Company ='%"+company+"%'
				 try
				 {
				 String daysDifference = "Select DATEDIFF(DAY,EndDate,Format(getdate(),'yyyy-MM-dd')) from LeaseFact_Dashboard where BuildingAbbreviation like '%"+buildingAbbreviation+"%' and ID ='"+leaseID+"'";
				 int diff = GetDatafromDatabase.getDateDifference(daysDifference);
				 if(diff>0)
					return true;
				 else
				 {
					 checkLeaseStatus = true;
					 leaseMatchedStatus = statusFromExcel;
					 break;
				 }
				 }
				 catch(Exception e) 
				 {
					 e.printStackTrace();
			     }
			 }
			 }
		 }
		 }
		 catch(Exception e) {checkLeaseStatus = false;}
		 try
		 {
		 for(int i=0;i<RunnerClass.UWStatuses.length;i++)
		 {
			 String statusFromUWTable = RunnerClass.UWStatuses[i][1];
			 String uwID = RunnerClass.UWStatuses[i][0];
			 for(int j=0;j<RunnerClass.statusList.length;j++)
			 {
				 String statusFromExcel = RunnerClass.statusList[j];
			 if(statusFromExcel.equalsIgnoreCase(statusFromUWTable))
			 {
				 try
				 {
				 String daysDifference = "Select  DATEDIFF(DAY,CreatedDate,Format(getdate(),'yyyy-MM-dd')) from Underwriting_fact_Max where BuildingAbbreviation like '%"+buildingAbbreviation+"%' and ID ='"+uwID+"'";
				 //checkStatus = true;
				 int diff = GetDatafromDatabase.getDateDifference(daysDifference);
				 if(diff>60)
					return true;
				 else 
				 {
					 checkUWStatus = true;
					 UWMatchedStatus = statusFromExcel;
					 break;
				 }
				 }
				 catch(Exception e) 
				 {
					 e.printStackTrace();
			     }
			 
			 }
			 }
		 }
		 }
		 catch(Exception e) {checkUWStatus = false; }
		 if(checkLeaseStatus == true && checkUWStatus ==true)
		 {
		 RunnerClass.failedReason = "Target Rent not Updated: Unit has Lease with Status of "+leaseMatchedStatus+" and Application with a status of "+UWMatchedStatus;
		// RunnerClass.failedReaonsList.put(buildingAbbreviation, "Target Rent not Updated: Unit has Lease with Status of "+leaseMatchedStatus+" and Application with a status of "+UWMatchedStatus);
		 return false;
		 }
		 if(checkLeaseStatus == true)
		 {
			 RunnerClass.failedReason = "Target Rent not Updated: Unit has Lease with Status of "+leaseMatchedStatus;
		// RunnerClass.failedReaonsList.put(buildingAbbreviation, "Target Rent not Updated: Unit has Lease with Status of "+leaseMatchedStatus);
		 return false;
		 }
		 if(checkUWStatus == true)
		 {
			 RunnerClass.failedReason ="Target Rent not Updated: Unit has Application with a status of "+UWMatchedStatus;
		 //RunnerClass.failedReaonsList.put(buildingAbbreviation, "Target Rent not Updated: Unit has Application with a status of "+UWMatchedStatus);
		 return false;
		 }
		 if(checkLeaseStatus == false && checkUWStatus ==false)
		 {
		// RunnerClass.failedReaonsList.put(buildingAbbreviation, "Target Rent not Updated: Could not fetch Lease and Application Statuses");
		 return true;
		 }
		 return true;
	 
	}
	
	public static boolean searchingBuildingWithDifferentText(String building)
	{
		try
		{
		RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
		 RunnerClass.driver.navigate().refresh();
		 CommonMethods.popUpHandling();
		 RunnerClass.driver.findElement(Locators.dashboardsTab).click();
		 RunnerClass.driver.findElement(Locators.searchbox).clear();
		 RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
		 RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
		 try
		 {
		 RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
		 if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
		 { 
			System.out.println("Building Not Found");
		    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
			return false;
	     }
		 }
		 catch(Exception e3) {}
		 }
		 catch(Exception e)
		 {
	     System.out.println("Building Not Found");
        RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
	     return false;
		 }
		return true;
	}
	

}
