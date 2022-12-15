package mainPackage;

import org.openqa.selenium.By;

public class Locators 
{
	public static By userName = By.id("loginEmail");
	public static By password = By.name("password");
	public static By signMeIn = By.xpath("//*[@value='Sign Me In']");
	
	public static By searchbox = By.name("eqsSearchText");
	public static By dashboardsTab = By.linkText("Dashboards");
	public static By searchingLoader = By.xpath("//*[@id='eqsResult']/h1");
	public static By searchedLeaseCompanyHeadings = By.xpath("//*[@id='eqsResult']/div/div/h1");
	public static By selectSearchedLease = By.partialLinkText(RunnerClass.building);
	public static By targetRentChangeButton = By.name("ChangeTargetRent");
	public static By newTargetRent = By.xpath("//*[text()='New Target Rent']/following::input[1]");
	public static By newTargetRent2 = By.name("targetRentChange.newTargetRentAsString");
	public static By reasonForChange = By.xpath("//*[text()='Reason For Change']/following::textarea[1]");
	public static By targetRentSaveButton = By.id("saveTargetRentChange");
	public static By targetRentCancelButton = By.id("cancelTargetRentChange");
	public static By editButton = By.xpath("//*[@onclick='edit();']");
	public static By targetDeposit = By.xpath("//*[text()='Target Deposit']/following::input[1]");
	public static By saveButton = By.xpath("(//*[@value='Save'])[1]");
	public static By cancelButton = By.xpath("(//*[@value='Cancel'])[1]");
	public static By noItemsFound = By.xpath("//*[text()='No Items Found']");
}
