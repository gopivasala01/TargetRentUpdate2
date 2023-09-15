package mainPackage;

import org.openqa.selenium.By;

public class Locators 
{
	public static By userName = By.name("email");
	public static By password = By.name("password");
	public static By signMeIn = By.xpath("//*[@value='Sign Me In']");
	public static By loginError = By.xpath("//*[@class='toast toast-error']");
	
	public static By searchbox = By.name("eqsSearchText");
	public static By dashboardsTab = By.linkText("Dashboards");
	public static By searchingLoader = By.xpath("//*[@id='eqsResult']/h1");
	public static By searchedLeaseCompanyHeadings = By.xpath("//*[@id='eqsResult']/div/div/h1");
	//public static By selectSearchedLease = By.partialLinkText(RunnerClass.building);
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
	public static By portfolioType =By.xpath("//*[text()='Attached Portfolio']/following::td[1]/a");
	public static By checkPortfolioType = By.xpath("//*[@title='Click to jump to portfolio']");
	
	 public static By popUpAfterClickingLeaseName = By.xpath("//*[@id='viewStickyNoteForm']");
	 public static By scheduledMaintanancePopUp = By.xpath("//*[text()='Scheduled Maintenance Notification']");
	 public static By scheduledMaintanancePopUpOkButton = By.id("alertDoNotShow");
	 public static By popupClose = By.xpath("//*[@id='editStickyBtnDiv']/input[2]");
	
	public static By marketDropdown = By.id("switchAccountSelect");
	
	public static By published = By.xpath("//*[contains(text(),'Published Rental')]/following::td[1]");
    public static By listingAgent = By.xpath("//*[contains(text(),'Listing Agent [Name/Phone/Email]')]/following::td[1]");
    
    
   
    public static By noItemsFoundMessagewhenLeaseNotFound = By.xpath("//*[text()='No Items Found']");
}
