package entities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class Utilities {

	/**
	 * Busca un elemento basándose en el tipo de búsqueda especificado.
	 * <p>
	 * Tipos de búsqueda admitidos: "className" "cssSelector" "id" "linkText" "name"
	 * "partialLinkText" "tagName" "xpath"
	 * 
	 * @param driver      WebDriver a utilizar para buscar
	 * @param searchType  tipo de búsqueda deseado (Ver más arriba en la definición)
	 * @param searchValue valor de búsqueda
	 * @return retorna el elemento buscado
	 * @throws Exception Si el tipo especificado no está dentro de los permitidos
	 */
	protected static WebElement findElementByType(WebDriver driver, String searchType, String searchValue)
			throws Exception {
		try {
			switch (searchType.toUpperCase()) {
			case "CLASSNAME":
				return driver.findElement(By.className(searchValue));
			case "CSSSELECTOR":
				return driver.findElement(By.cssSelector(searchValue));
			case "ID":
				return driver.findElement(By.id(searchValue));
			case "LINKTEXT":
				return driver.findElement(By.linkText(searchValue));
			case "NAME":
				return driver.findElement(By.name(searchValue));
			case "PARTIALLINKTEXT":
				return driver.findElement(By.partialLinkText(searchValue));
			case "TAGNAME":
				return driver.findElement(By.tagName(searchValue));
			case "XPATH":
				return driver.findElement(By.xpath(searchValue));
			default:
				throw new Exception("El método de búsqueda especificado no corresponde a ningún método definido.");
			}
		} catch (Exception e) {
			throw new Exception("No se pudo hallar el elemento.");
		}
	}

	/**
	 * Busca todos los elementos que coincidan con en el valor de búsqueda,
	 * basándose en el tipo de búsqueda especificado.
	 * <p>
	 * Tipos de búsqueda admitidos: "className" "cssSelector" "id" "linkText" "name"
	 * "partialLinkText" "tagName" "xpath"
	 * 
	 * @param driver      WebDriver a utilizar para buscar
	 * @param searchType  tipo de búsqueda deseado (Ver más arriba en la definición)
	 * @param searchValue valor de búsqueda
	 * @return retorna el elemento buscado
	 * @throws Exception Si el tipo especificado no está dentro de los permitidos
	 */
	protected static List<WebElement> findElementsByType(WebDriver driver, String searchType, String searchValue)
			throws Exception {
		try {
			switch (searchType.toUpperCase()) {
			case "CLASSNAME":
				return driver.findElements(By.className(searchValue));
			case "CSSSELECTOR":
				return driver.findElements(By.cssSelector(searchValue));
			case "ID":
				return driver.findElements(By.id(searchValue));
			case "LINKTEXT":
				return driver.findElements(By.linkText(searchValue));
			case "NAME":
				return driver.findElements(By.name(searchValue));
			case "PARTIALLINKTEXT":
				return driver.findElements(By.partialLinkText(searchValue));
			case "TAGNAME":
				return driver.findElements(By.tagName(searchValue));
			case "XPATH":
				return driver.findElements(By.xpath(searchValue));
			default:
				throw new Exception("El método de búsqueda especificado no corresponde a ningún método definido.");
			}
		} catch (Exception e) {
			throw new Exception("No se pudo hallar el elemento.");
		}
	}

	/**
	 * 
	 * Toma una captura de pantalla y la guarda en /screenshot del proyecto
	 * 
	 *
	 * @param nombreOpcional Nombre opcional con el que se guardará la captura. Para
	 *                       usar el nombre por default, dejar en blanco
	 * 
	 * @param timestamp      Boolean utilizado para guardar capturas con nombre
	 *                       opcional. True si se desea dejar el formato de *nombre
	 *                       opcional* + "-" + *timestamp*, false para dejar solo el
	 *                       nombre opcional como nombre de archivo
	 * 
	 */
	public static void takeScreenshot(String nombreOpcional, Boolean timestamp) {

		String savePath = System.getProperty("user.dir") + "/screenshots";

		try {
			String timeStamp;
			File screenShotName;
			File scrFile = ((TakesScreenshot) Browser.getDriver()).getScreenshotAs(OutputType.FILE);
			timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

			if (nombreOpcional.isEmpty())
				screenShotName = new File(savePath + "/" + timeStamp + ".png");
			else if (timestamp)
				screenShotName = new File(savePath + "/" + nombreOpcional + "-" + timeStamp + ".png");
			else
				screenShotName = new File(savePath + "/" + nombreOpcional + ".png");

			FileUtils.copyFile(scrFile, screenShotName);
			
		} catch (IOException e) {
			Reporter.log("No se pudo tomar Screenshot de la operación en la ruta: " + savePath);
		}
	}

	/**
	 * Toma una captura de pantalla y lo guarda en /screenshots del proyecto
	 * 
	 */
	public static void takeScreenshot() {
		try {
			String timeStamp;
			File screenShotName;
			File scrFile = ((TakesScreenshot) Browser.getDriver()).getScreenshotAs(OutputType.FILE);
			timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			screenShotName = new File(System.getProperty("user.dir") + "/screenshots/" + timeStamp + ".png");
			FileUtils.copyFile(scrFile, screenShotName);
			String filePath = screenShotName.toString();
			String path = "<img src=\"file://" + filePath + "\" alt=\"\"/>";
			Reporter.log(path);
		} catch (IOException e) {
			Reporter.log("No se pudo tomar Screenshot de la operación");
		}
	}

	/**
	 * limpiarScreenshots: Limpia la carpeta donde se guardan las capturas de
	 * pantalla Parametros: No aplica
	 * 
	 */
	public static void cleanScreenshots() {
		try {
			File directory = new File(System.getProperty("user.dir") + "/screenshots/");
			FileUtils.cleanDirectory(directory);
		} catch (IOException e) {
			Reporter.log("No se pudo limpiar la carpeta de Screenshots");
		}
	}

	/**
	 * webElementsVisibles: A partir de una lista de elementos indica cuales se
	 * encuentran visibles en pantalla
	 * 
	 * @param elementos = "Lista de elementos"
	 * 
	 * 
	 * @return Lista que representa los elementos visibles en pantalla
	 * 
	 */
	public static List<WebElement> webElementsVisibles(List<WebElement> elementos) {
		List<WebElement> resultado = new ArrayList<WebElement>();
		for (WebElement webElement : elementos) {
			if (webElement.isDisplayed()) {
				resultado.add(webElement);
			}
		}
		return resultado;
	}

	/**
	 * pathRepositorios: Devuelve el path de los repositorios Parametros: No aplica
	 * 
	 * @return String que representa el path de los repositorios
	 * 
	 */
	public static String pathRepositorios() {
		return System.getProperty("user.dir") + "/src/test/resources/repositorios/";
	}


	/**
	 * limpiarInput: Vacia el input con BackSpaces Parametros: No aplica
	 * 
	 * @param input parametro de entrada
	 * 
	 */
	public static void cleanInput(WebElement input) {
		String texto = input.getAttribute("value");
		for (int i = 0; i < texto.length(); i++) {
			input.sendKeys(Keys.BACK_SPACE);
		}
	}

}
