package com.lazerycode.selenium.tests;
import java.util.concurrent.TimeUnit;

import java.util.*;
import java.io.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;

import com.lazerycode.selenium.DriverBase;
import com.lazerycode.selenium.page_objects.GoogleHomePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class GoogleExampleIT extends DriverBase {
/*
    private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
        return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
    }
*/
    @Test
    public void googleCheeseExample() throws Exception {
        // Create a new WebDriver instance
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = getDriver();
driver.get("http://www.google.com");
driver.manage().window().maximize();
WebElement element = driver.findElement(By.name("q"));
element.sendKeys("Rent apartments near me ");
Thread.sleep(1000);
WebElement matchingResult= driver.findElement(By.xpath(".//div[@class='aajZCb']/ul"));
List<WebElement> listResult= matchingResult.findElements(By.xpath("//li/div/div[@class='sbtc']"));
System.out.println(listResult.size());
//if you want to print matching results
     for(WebElement results: listResult)
       {
         String value= results.getText();
         System.out.println(value);
       }     

    }

    @Test
    public void googleMilkExample() throws Exception {
	
	    WebDriver driver = getDriver(); 



 driver.manage().window().maximize();

      
    driver.get("http://35.245.8.239:4200/");
    driver.findElement(By.linkText("List Users")).click();
    driver.findElement(By.linkText("Add User")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("automationtest");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("automationtest@automationtest.com");
    driver.findElement(By.cssSelector(".btn:nth-child(4)")).click();
	
}}
