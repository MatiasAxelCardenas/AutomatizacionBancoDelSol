package entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class Browser {

	private static WebDriver driver;
	private static String URL = null;
	@SuppressWarnings("unused")
	private static WebDriverWait wait;
	private static int speedOfExecution = 3, cantidadIteraciones = 10;

	/**
	 * Devuelve la velocidad de ejecuci�n (por default 3)
	 * 
	 * @return velocidad de ejecuci�n
	 */
	public static int getSpeedOfExecution() {
		return speedOfExecution;
	}

	/**
	 * Tiempo de espera en milisengundos para la b�squeda de un control, utilizado
	 * para simular distintas velocidades de ejecuci�n (por default 3)
	 * 
	 * @param executionSpeed Milisengundos de tiempo de espera
	 */
	public static void setEjecutionVelocity(int executionSpeed) {
		Browser.speedOfExecution = executionSpeed;
	}

	/**
	 * Cantidad de iteraciones que desea realizar. Utilizado principalmente en el
	 * hilo de {@link Browser#waitVisibilityElement(String, String)} para definir
	 * la cantidad de iteraciones que se realizar�n para esperar hasta que el
	 * elemento aparezca
	 * 
	 * @return la cantidad de iteraciones, por default es 10. (10 iteraciones, 1
	 *         segundo entre cada iteracion)
	 */
	public static int getAmountIteractions() {
		return cantidadIteraciones;
	}

	/**
	 * Cantidad de iteraciones que desea realizar. Utilizado principalmente en el
	 * hilo de {@link Browser#waitVisibilityElement(String, String)} para definir
	 * la cantidad de iteraciones que se realizar�n para esperar hasta que el
	 * elemento aparezca
	 * 
	 * @param cantidadIteraciones la cantidad de iteraciones, por default es 10. (10
	 *                            iteraciones, 1 segundo entre cada iteracion)
	 */
	public static void setCantidadIteraciones(int cantidadIteraciones) {
		Browser.cantidadIteraciones = cantidadIteraciones;
	}

	/**
	 * Devuelve el web driver
	 * 
	 * @return web driver
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/**
	 * devuelve la url del sitio
	 * 
	 * @return la url del sitio
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Setea la url del sitio a navegar
	 * 
	 * @param uRL url del sitio
	 */
	public static void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * Cierra el driver utilizado y termina con cualquier instancia que pudiera
	 * haber quedado ejecutandose
	 * 
	 */
	public static void finish() {
		if (Browser.getDriver() != null) {
			driver.quit();
			killDriverProcesses();
		}
	}

	/**
	 * Obtiene una lista completa con todos los procesos que se estan ejecutando
	 * actualmente y termina con todos los que correspondan al driver de chrome
	 * 
	 */
	private static void killDriverProcesses() {
		String processName = "chromedriver";
		if (!processName.isEmpty() && !processName.equals(null)) {
			try {
				String line;
				Process process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = input.readLine()) != null) {
					if (line.equals(processName))
						process.destroy();
				}
				input.close();
			} catch (Exception err) {
				Reporter.log("Fallo al buscar elementos: " + err.getMessage());
			}
		}
	}

	/**
	 * Genera una espera implicita de sec segundos
	 * 
	 * @param sec segundos que esperar� el driver
	 * 
	 */
	public static void implicityWait(int sec) {
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
	}

	/**
	 * Instancia el Web Driver
	 * 
	 * @param navegador Navegador a utilizar, puede ser: "chrome", "ie", "zafari"
	 * 
	 */
	public static void init(String navegador) {

		File file;

		switch (navegador.toLowerCase()) {
		case "chrome":
			file = new File(pathDrivers("chrome"));
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver = (WebDriver) new ChromeDriver();
			driver.manage().window().maximize();
			generateWait(driver, Long.valueOf(Browser.getSpeedOfExecution()));
			break;

		case "ie":
			file = new File(pathDrivers("ie"));
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			generateWait(driver, Long.valueOf(Browser.getSpeedOfExecution()));
			break;

		default:
			Reporter.log("No existe el navegador especificado. Admitidos: 'chrome' - 'ie'");
			break;
		}
	}

	/**
	 * Espera a que el elemento pasado por parametro se encuentre visible,
	 * utilizando wait until y expected conditions
	 * 
	 * @param metodoBusqueda Metodo de busqueda deseado para hallar el elemento
	 * @param valorBusqueda  Valor del elemento a buscar
	 * 
	 */
	public static void waitVisibilityElement(String metodoBusqueda, String valorBusqueda) {

		List<WebElement> elementos;
		int iteraciones = Browser.getAmountIteractions();
		try {
			elementos = new ArrayList<WebElement>();
			while ((elementos.isEmpty() || elementos == null) && iteraciones-- >= 0) {
				Browser.implicityWait(1);
				elementos = Browser.waitElement(metodoBusqueda, valorBusqueda);
			}
		} catch (Exception e) {
			Reporter.log("Fallo al esperar que el elemento sea visible: " + e.getMessage() + ": " + valorBusqueda);
		}
	}


	/**
	 * Funci�n interna para facilitar la utilizacion de WebDriverWait
	 * 
	 * @param driver  driver
	 * @param seconds segundos a esperar
	 * 
	 */
	private static void generateWait(WebDriver driver, long seconds) {
		wait = new WebDriverWait(driver, seconds);
	}

	/**
	 * Retorna el manejador de la ventana/pestaña actual
	 * 
	 * @return una string con el manejador de la ventana
	 * 
	 */
	public static String currentWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * Retorna una lista con todos los manejadores de todas las ventanas/pestañas
	 * 
	 * @return Lista con todos los manejadores
	 * 
	 */
	public static Set<String> currentWindowsHandlers() {
		return driver.getWindowHandles();
	}

	/**
	 * Setea el atributo Visibility de un elemento en 'visible'
	 * 
	 * @param searchMethod M�todo de busqueda @see
	 *                     {@link Utilities#findElementByType(WebDriver, String, String)}
	 * @param value        Valor de b�squeda
	 * 
	 */
	public static void makeVisibilityElement(String searchMethod, String value) {
		WebElement element;
		try {
			element = Utilities.findElementByType(driver, searchMethod, value);
			String javascript = "argument[0.style.height='auto'; arguments[0].style.visibility='visible';";
			((JavascriptExecutor) Browser.driver).executeScript(javascript, element);
		} catch (Exception e) {
			Reporter.log("Fallo al hacer visible el elemento: " + e.getMessage() + ": " + value);
		}
	}

	/**
	 * Busca un elemento con espera
	 * 
	 * @param searchMethod M�todo de b�squeda @see
	 *                     {@link Utilities#findElementByType(WebDriver, String, String)}
	 * @param value        Valor de busqueda
	 * @return Retorna el elemento
	 * 
	 */
	public static WebElement searchElement(String searchMethod, String value) {

		try {
			driver.manage().timeouts().implicitlyWait(getSpeedOfExecution(), TimeUnit.SECONDS);
			waitChargeOfPage();
			return Utilities.findElementByType(driver, searchMethod, value);
		} catch (Exception e) {
			Reporter.log("Fallo al buscar elementos: " + e.getMessage() + ": " + value);
			return null;
		}
	}

	/**
	 * Busca un elemento sin espera
	 * 
	 * @param searchMethod M�todo de busqueda @see
	 *                     {@link Utilities#findElementByType(WebDriver, String, String)}
	 * @param value        Valor de busqueda
	 * @return Retorna el elemento
	 * 
	 */
	public static WebElement searchElementWithoutWait(String searchMethod, String value) {

		try {
			return Utilities.findElementByType(driver, searchMethod, value);
		} catch (Exception e) {
			Reporter.log("Fallo al buscar elementos: " + e.getMessage() + ": " + value);
			return null;
		}
	}

	/**
	 * Busca todos los elementos coincidentes, con espera
	 * 
	 * @param searchMethod M�todo de busqueda @see
	 *                     {@link Utilities#findElementByType(WebDriver, String, String)}
	 * @param value        Valor de busqueda
	 * @return Retorna una lista con los elementos
	 * 
	 */
	public static List<WebElement> waitElement(String searchMethod, String value) {

		try {
			driver.manage().timeouts().implicitlyWait(getSpeedOfExecution(), TimeUnit.SECONDS);
			waitChargeOfPage();
			return Utilities.findElementsByType(driver, searchMethod, value);
		} catch (Exception e) {
			Reporter.log("Fallo al buscar elementos: " + e.getMessage() + ": " + value);
			return null;
		}
	}

	/**
	 * Busca todos los elementos coincidentes, sin espera
	 * 
	 * @param searchMethod Metodo de busqueda @see
	 *                     {@link Utilities#findElementByType(WebDriver, String, String)}
	 * @param value        Valor de busqueda
	 * @return Retorna una lista con los elementos
	 * 
	 */
	public static List<WebElement> seachElementWithoutWait(String searchMethod, String value) {

		try {
			return Utilities.findElementsByType(driver, searchMethod, value);
		} catch (Exception e) {
			Reporter.log("Fallo al buscar elementos: " + e.getMessage() + ": " + value);
			return null;
		}
	}

	/**
	 * realiza un doble click sobre el elemento enviado
	 * 
	 * @param element Elemento sobre el cual se hara doble click
	 * @throws InstantiationException Al fallar la instanciacion de la clase
	 *                                Keyboard
	 * @throws IllegalAccessException Al no poder acceder a la instancia de Keyboard
	 * 
	 */
	public static void doubleClick(WebElement element) throws InstantiationException, IllegalAccessException {
		Actions DoubleClick = new Actions(driver);
		DoubleClick.doubleClick(element).perform();
	}

	/**
	 * Realiza un click derecho (o click de contexto)sobre el elemento deseado
	 * 
	 * @param element Elemento en el que se realizar� el click derecho
	 * @throws InstantiationException Al fallar la instanciaci�n de la clase
	 *                                Keyboard
	 * @throws IllegalAccessException Al no poder acceder a la instancia de Keyboard
	 * 
	 */
	public static void clickRight(WebElement element) throws InstantiationException, IllegalAccessException {
		Actions contextClick = new Actions(driver);
		contextClick.contextClick(element);
	}

	/**
	 * Setea el atributo a un elemento por javaScript
	 * 
	 * @param searchType M�todo de b�squeda @see
	 *                   {@link Utilities#findElementByType(WebDriver, String, String)}
	 * @param value      Valor de b�squeda
	 * @throws Exception En caso de no encontrar el elemento
	 * 
	 */
	public static void setAtributsToJS(String searchType, String value) throws Exception {
		WebElement element = Browser.searchElement(searchType, value);
		((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
	}

	/**
	 * Mueve el puntero al elemento asignado
	 * 
	 * @param element Elemento al cual mover� el puntero
	 * 
	 */
	public static void moveTowardsElement(WebElement element) {
		Actions moveToElement = new Actions(driver);
		moveToElement.moveToElement(element).perform();

	}

	/**
	 * Simula la accion del mouse over sobre un elemento
	 * 
	 * @param element Elemento sobre el cual se simular� el mouse over
	 * 
	 */
	public static void mouseOver(WebElement element) {
		Actions MouseOver = new Actions(driver);
		MouseOver.moveToElement(element).perform();
	}

	/**
	 * Clickea un elemento en las coordenadas indicadas
	 * 
	 * @param coordX Coordenada x del elemento
	 * @param coordY Coordenada y del elemento
	 * 
	 */
	public void clickearElementoPorCoordenadas(int coordX, int coordY) {
		Actions move = new Actions(driver);
		move.moveByOffset(coordX, coordY).click().perform();
	}

	/**
	 * Navega hacia la URL especificada
	 * 
	 * @param url Direccion del sitio web al cual se desea navegar
	 */
	public static void travelTo(String url) {
		try {
			Browser.driver.navigate().to(url);
		} catch (Exception e) {
			Reporter.log("Fallo al redireccionar: " + e.getMessage() + ". URL: " + url);
		}
	}

	/**
	 * Navega a la URL seteada en el campo url del driver.
	 * 
	 * 
	 */
	public static void travelTo() {
		try {
			Browser.driver.navigate().to(Browser.URL);
		} catch (Exception e) {
			Reporter.log("Fallo al redireccionar: " + e.getMessage() + ". URL: " + driver.getCurrentUrl());
		}
	}

	/**
	 * Ejecuta un script para comprobar si se termin� de cargar la pagina. En caso
	 * de falla, espera y vuelve a ejecutarlo luego de 1 segundo
	 * 
	 * @throws Exception En caso de falla en la ejecuci�n del script
	 * 
	 */
	public static void waitChargeOfPage() throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// This loop will rotate for 100 times to check If page Is ready after every 1
		// second.
		// You can replace your if you wants to Increase or decrease wait time.

		int waittime;
		waittime = 60;
		for (int i = 0; i < waittime; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Reporter.log("Fall� al ejecutar script de espera: " + e.getMessage());
			}
			// To check page ready state.
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				
				break;
			}
		}
	}

	/**
	 * pathRepositorios: Devuelve el path de los drivers
	 * 
	 * @param browser Navegador a utilizar, "chrome" o "ie"
	 * @return String que representa el path de los drivers
	 * 
	 */
	private static String pathDrivers(String browser) {
		return DriverHandler.getDriver(browser);
	}
	
	public static Select getElementsDropdown(WebElement dropdown) {
        Select dropdownSelect = new Select(dropdown);
        return dropdownSelect;
    }

}
