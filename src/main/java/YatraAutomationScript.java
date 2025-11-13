import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Month;
import java.util.Date;
import java.util.List;

public class YatraAutomationScript {
    
    public static void main(String[] args) throws InterruptedException {
       
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
       WebDriver driver = new ChromeDriver(options);
       WebDriverWait waits = new WebDriverWait(driver, Duration.ofSeconds(20));
       driver.get("https://www.yatra.com/");
       driver.manage().window().maximize();
        
        PopupHandler(waits);
        
        ClickDepartureElement(waits);
        
        WebElement CurrentMonth = GetMonthCalendarWebElement(waits,0);
       WebElement NextMonth = GetMonthCalendarWebElement(waits,1);
       
       Thread.sleep(2000);
        System.out.println(GetLowestPrice(CurrentMonth));
        System.out.println(GetLowestPrice(NextMonth));
        
        String resultCurrentMonth = GetLowestPrice(CurrentMonth);
        String resultNextMonth = GetLowestPrice(NextMonth);
        
        
        ComparePrices(resultCurrentMonth,resultNextMonth);
        
    }
    
    public static void PopupHandler(WebDriverWait waits) {
        By LoginPopupLocator = By.xpath("//div[contains(@class,'style_popup')][1]");
        
        try {
            
            WebElement LoginPopupElement = waits.until(ExpectedConditions.visibilityOfElementLocated(LoginPopupLocator));
            WebElement CrossButton = LoginPopupElement.findElement(By.xpath(".//img[@alt=\"cross\"]"));
            CrossButton.click();
        }
        catch(Exception e){
            System.out.println("Popup is not located");
        }
    }
    
    public static void ClickDepartureElement(WebDriverWait waits) {
        By DepartureDateLocator = By.xpath("//div[@aria-label = 'Departure Date inputbox' and @role ='button']");
        
        WebElement DepartureDateButton = waits.until(ExpectedConditions.elementToBeClickable(DepartureDateLocator));
        
        DepartureDateButton.click();
    }
    
    public static String GetLowestPrice(WebElement monthselection) {
        By datesLocator = By.xpath(".//span[contains(@class,\"custom-day-content \")]");
        List<WebElement> CurrentMonthDateList = monthselection.findElements(datesLocator);
        int lowestprice = Integer.MAX_VALUE;
        WebElement PriceElementLocator = null;
        for (WebElement PriceString :CurrentMonthDateList){
            String Pricelist = PriceString.getText();
            
            if(Pricelist.length() > 0) {
                Pricelist = Pricelist.replace("₹", "").replace(",", "");
                int Price = Integer.parseInt(Pricelist);
                
                if (Price < lowestprice) {
                    
                    lowestprice = Price;
                    PriceElementLocator = PriceString;
                    
                }
            }
        }
        
        WebElement DateLocator = PriceElementLocator.findElement(By.xpath(".//../.."));
        
        String getLowestPremiumDate = DateLocator.getAttribute("aria-label")+" and the lowest price is Rs. "+lowestprice;
        
        return getLowestPremiumDate;
    }
    
   
   
   
    public static WebElement GetMonthCalendarWebElement(WebDriverWait waits, int Index){
        
        By MonthCalendarLocator = By.xpath("//div[@class=\"react-datepicker__month-container\"]");
        List<WebElement> Calendars = waits.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(MonthCalendarLocator));
        WebElement CurrentMonthCalendar = Calendars.get(Index);
        
        return CurrentMonthCalendar;
    }
    
    public static void ComparePrices(String CurrentMonth, String NextMonth){
        
        int indexCurrentMonthRs = CurrentMonth.indexOf("Rs");
        int indexNextMonthRs = NextMonth.indexOf("Rs");
        
        String currentMonthPriceString =CurrentMonth.substring(indexCurrentMonthRs+4);
        String nextMonthPriceString = NextMonth.substring(indexNextMonthRs+4);
        
        
        int Currentmonthpriceint = Integer.parseInt(currentMonthPriceString);
        int NextMonthpriceint = Integer.parseInt(nextMonthPriceString);
        
        if(Currentmonthpriceint < NextMonthpriceint){
            
            System.out.println("Current Month Price is Lowest");
        } else if (Currentmonthpriceint == NextMonthpriceint) {
            
            System.out.println("Both the Month Prices are equal It's your preference now !");
        }
        else {
            
            System.out.println("Next Month Price is lowest !");
        }
        
        
    }
}
