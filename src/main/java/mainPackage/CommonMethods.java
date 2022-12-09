package mainPackage;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
		Thread.sleep(10000);
		System.out.println(building);
		try
		{
		if(RunnerClass.driver.findElement(Locators.noItemsFound).isDisplayed())
		{
			System.out.println("Couldn't find Building");
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
							if(lease.contains(building))
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
		    RunnerClass.failedReason = "Building Not Found";
			RunnerClass.updateStatus=1;
			return false;
			}
		}
		catch(Exception e)
		{
			System.out.println("Issue in selecting Building");
			RunnerClass.failedReason = "Issue in finding Building";
			RunnerClass.updateStatus=1;
			return false;
		}
	}
	public static boolean enterTargetsInBuilding(String targetRent,String targetDeposit) throws Exception
	{
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
		
		return true;
		}
		catch(Exception e)
		{
			RunnerClass.failedReason = "Unable to update values";
			RunnerClass.updateStatus=1;
			return false;
		}
	}
	
	

}
