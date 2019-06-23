package entities;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class Function {

	/**
	 * Retorna la ruta correspondiente a la carpeta DataPools del proyecto
	 * 
	 * @return Ruta de la carpeta dataPools
	 */
	public static String getRutaDataPools() {
		return (System.getProperty("user.dir") + "\\src\\main\\resources\\objectsRepository\\");
	}
	
	/**
	 * verifica que se paso a la pagina deseada y saca screenshot
	 * @param URLtoCompare = ingresar la url de la pagina a comparar
	 */
	public static void validateURL(String URLtoCompare) {
		try {
			Browser.waitChargeOfPage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebDriver driver = Browser.getDriver();
		String currentURL = driver.getCurrentUrl();
		if (currentURL.contains(URLtoCompare)) {
			Utilities.takeScreenshot("Se valido que se paso a la pagina correcta", false);
		} else {
			Utilities.takeScreenshot("no se paso a la pagina correcta", false);
		}
	}
	
    public static Boolean waitVisibility(WebElement elemento) {    	  
  	  int contador = 0;
          while(true) {
              contador++;
              if(contador < 20) {
                  try {
                       if(elemento.isDisplayed()) {
                           return true;
                       }
                  }catch(NoSuchElementException | NullPointerException ce){
                	  wait(1);
                  }
              }else
              if(contador == 20){
              	System.out.println("No se ha encontrado el objeto" + elemento.toString());
              	Reporter.log("No se ha encontrado el objeto" + elemento.toString());       
                  return false;
              }    
          }
    }
          
     /**
  	 *  <h1>Descripci�n</h1>
  	 * Funcionalidad interna para esperar segun un tiempo dado
  	 * @param time: tiempo a esperar
  	 * 
  	 */
  	  private static void wait(int time)
  		{
  			time += time*1000;
  			try {
  				Thread.sleep(time);
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}
  		}
    
  	  public static void closeWindow() {
  		WebDriver driver = Browser.getDriver();
  		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
  	    driver.switchTo().window(tabs2.get(0));
  	    driver.close();
  	    driver.switchTo().window(tabs2.get(1));
  	  }
}