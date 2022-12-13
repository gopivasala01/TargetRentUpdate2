package mainPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		WebDriverManager.chromedriver().setup();
		RunnerClass.driver= new ChromeDriver(RunnerClass.options);
		RunnerClass.actions = new Actions(RunnerClass.driver);
		RunnerClass.js = (JavascriptExecutor)RunnerClass.driver;
		RunnerClass.driver.manage().window().maximize();
		RunnerClass.driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
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
	
	public static boolean enterDetailsToBuilding(String company,String building,String targetRent, String targetDeposit) throws Exception
	{
		RunnerClass.driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
		try
		{
		RunnerClass.driver.findElement(Locators.searchbox).clear();
		RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
		try
		{
		RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
		}
		catch(Exception e)
		{}
		Thread.sleep(5000);
		System.out.println(building);
		try
		{
		if(RunnerClass.driver.findElement(Locators.noItemsFound).isDisplayed())
		{
			System.out.println("Building Not Found");
			RunnerClass.failedReaonsList.put(building, "Building Not Found");
		    RunnerClass.failedReason = "Building Not Found";
			RunnerClass.updateStatus=1;
			return false;
		}
		}
		catch(Exception e)
		{
			
		}
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
								RunnerClass.driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
								leaseSelected = true;
								break;
									
							}
						}
						
					}
					if(leaseSelected==true) break;
				}
		if(CommonMethods.enterTargetsInBuilding(targetRent, targetDeposit)==false)
		{
			return false;
		}
		//RunnerClass.driver.findElement(Locators.selectSearchedLease).click();
		Thread.sleep(5000); 
		if(leaseSelected==true)
		return true;
		else 
			{
		    System.out.println("Couldn't find Building");
		    RunnerClass.failedReaonsList.put(building, "Building Not Found");
		    RunnerClass.failedReason = "Building Not Found";
			RunnerClass.updateStatus=1;
			return false;
			}
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
		boolean targetRentUpdateCheck = false;
		boolean targetDepositUpdateCheck = false;
		try
		{
		RunnerClass.driver.findElement(Locators.targetRentChangeButton).click();
		Thread.sleep(2000);
		RunnerClass.driver.findElement(Locators.newTargetRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		//RunnerClass.actions.click(RunnerClass.driver.findElement(Locators.newTargetRent)).sendKeys(Keys.END).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
		RunnerClass.driver.findElement(Locators.newTargetRent).sendKeys(targetRent);
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
		Thread.sleep(2000);
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
		header.createCell(2).setCellValue("Target Rent");
		header.createCell(3).setCellValue("Target Deposit");
		header.createCell(4).setCellValue("Status");
		//int totalCurrentDayBuildings = RunnerClass.successBuildings.size()+RunnerClass.failedBuildings.size();
		//sheet1.createRow(sheet1.getLastRowNum()+totalCurrentDayBuildings);
		boolean getBuildings =  GetDatafromDatabase.getCompletedBuildingsList();
		if(getBuildings==true&&RunnerClass.completedBuildingList!=null)
		{
			for(int i=0;i<RunnerClass.completedBuildingList.length;i++)
			{
				String company = RunnerClass.completedBuildingList[i][0];
				String building = RunnerClass.completedBuildingList[i][1].trim();
				String targetRent = RunnerClass.completedBuildingList[i][2];
				String targetDeposit = RunnerClass.completedBuildingList[i][3];
				String status = RunnerClass.completedBuildingList[i][4];
				Row row = sheet1.createRow(1+i);
				row.createCell(0).setCellValue(company);
				row.createCell(1).setCellValue(building);
				row.createCell(2).setCellValue(targetRent);
				row.createCell(3).setCellValue(targetDeposit);
				row.createCell(4).setCellValue(status);
				
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

	         // Set To: header field of the header.
	        message.setRecipients(Message.RecipientType.TO,
	           InternetAddress.parse(AppConfig.toEmail));

	         // Set CC: header field of the header.
	         message.setRecipients(Message.RecipientType.CC,
	            InternetAddress.parse(AppConfig.CCEmail));
	         
	         // Set CC: header field of the header.
	         message.setRecipients(Message.RecipientType.BCC,
	            InternetAddress.parse("sujana.t@beetlerim.com"));
	         
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
	

}
