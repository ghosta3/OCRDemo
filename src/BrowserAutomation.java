import fantasy.util.ImageUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BrowserAutomation {
    public static void main(String[] args) throws IOException, TesseractException {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        System.setProperty("webdriver.chrome.driver","D:/Work/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("http://192.168.0.51:47064/OpentrueAccount");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.id("VerifyCode"));
        Point location = element.getLocation();
        Dimension dimen = element.getSize();
        byte[] screenData = ((ChromeDriver)driver).getScreenshotAs(OutputType.BYTES);

        InputStream in = new ByteArrayInputStream(screenData);
        BufferedImage screen = ImageIO.read(in);
        BufferedImage verCodeImg = screen.getSubimage(location.x, location.y, dimen.width, dimen.height);
        BufferedImage image = ImageUtil.convertToGray(verCodeImg);

        ITesseract instance = new Tesseract();
        instance.setLanguage("eng");
        String result = instance.doOCR(image).trim();
        System.out.println(result);


        WebElement codeInput = driver.findElement(By.id("code"));
        codeInput.sendKeys(result);

        // Enter something to search for
//        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
//        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
//        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver d) {
//                return d.getTitle().toLowerCase().startsWith("cheese!");
//            }
//        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());

        //Close the browser
//        driver.quit();
    }
}
