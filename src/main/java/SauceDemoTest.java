import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;

public class SauceDemoTest {
    public static void main(String[] args) {

        String userProfilePath = System.getProperty("java.io.tmpdir") + File.separator + "chromeProfile";
        new File(userProfilePath).mkdirs();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-data-dir=" + userProfilePath);
        options.addArguments("--incognito");

        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try {
            driver.get("https://www.saucedemo.com/");
            System.out.println("🔗 Opened SauceDemo");

            // Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            System.out.println("Logged in successfully.");

            // Click on the jacket item
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Sauce Labs Fleece Jacket']"))).click();
            System.out.println("Navigated to jacket details.");

            // Scroll and click Add to Cart
            System.out.println("Waiting for Add to Cart button to be clickable...");
            WebElement addToCartBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
            wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
            System.out.println("Item added to cart.");

            // Go to cart
            driver.findElement(By.className("shopping_cart_link")).click();

            // Checkout
            wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();

            // Enter info
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("Vidhi");
            driver.findElement(By.id("last-name")).sendKeys("Arora");
            driver.findElement(By.id("postal-code")).sendKeys("125055");
            driver.findElement(By.id("continue")).click();

            // Print payment summary
            String summary = driver.findElement(By.className("summary_info")).getText();
            String total = driver.findElement(By.className("summary_total_label")).getText();
            System.out.println("===== Checkout Summary =====");
            System.out.println(summary);
            System.out.println("Total: " + total);

            // Finish
            driver.findElement(By.id("finish")).click();
            System.out.println("Order completed successfully!");

        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
