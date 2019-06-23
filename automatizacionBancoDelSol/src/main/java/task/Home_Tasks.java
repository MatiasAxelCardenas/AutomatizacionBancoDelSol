package task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import appObjects.Home_AppObjects;
import entities.Browser;
import entities.Function;
import entities.Utilities;

public class Home_Tasks {

	private final static String url = "http://www.ebay.com";

	/**
	 * Funcion que nos permite instanciar el Driver de Selenium, setear los
	 * parametros necesarios y navegar al sitio de despegar.com
	 */

	public static void openWithBrowser(String navegador) {

		// Inicializamos el driver especificando el navegador que vamos a utilizar
		Browser.init(navegador);

		// Seteamos la velocidad de ejecucion deseada (por default: ver definici�n de
		// funci�n)
		Browser.setEjecutionVelocity(3);

		// Seteamos la url del sitio al cual deseamos ingresar
		Browser.setURL(url);

		// Le decimos al driver que ingrese al sitio especificado (Esto se puede
		// realizar en un solo paso, llamando a Browser.travellTo("url del sitio");
		Browser.travelTo();
	}
	
	/**
	 * Se ingresa y busca el articulo deseado dando tambien la cantidad de resultados sobre el articulo
	 * @param article = ingresar el nombre articulo a buscar
	 */
	public static void seachAnArticle(String article) {
		//Se ingresa y busca el articulo deseado
		Home_AppObjects.getInputSearchArticles().sendKeys(article);
		Home_AppObjects.getBtnSearch().click();
		//Se valido que se paso a la pagina de search del articulo deseado y saca screenshot
		Function.validateURL("https://www.ebay.com/sch/");
		//Se creo una lista para poder encontrar la posicion en la cual tiene el valor de la cantidad de resultados del articulo deseado
		List<WebElement> lstCount = Browser.waitElement("className", "srp-controls__count-heading");
		System.out.println("La cantidad de " + article + " encontradas es de: " + lstCount.get(0).getText());
	}
	
	

}
