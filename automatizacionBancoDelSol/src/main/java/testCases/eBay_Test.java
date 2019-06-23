package testCases;

import org.testng.annotations.Test;

import entities.Browser;
import entities.Function;
import task.Home_Tasks;


public class eBay_Test {
	
	final boolean runTest = true;
	
	/**
	 *  1-Abrir Chrome
		2-Abrir la url http://www.ebay.com
		3-Ingresar en el campo Search la palabra "Pilas"
		4-Hacer tap sobre la lupa.
		5-Imprimir por consola el numero de items que devuelve la búsqueda.
	 */
	
	@Test(enabled = runTest, priority = 1)
	public void firstTest() {
	 Home_Tasks.openWithBrowser("chrome");
	 Home_Tasks.seachAnArticle("Pilas");
	 Browser.finish();
	}
	
	//F5 o Refresh en la carpeta Screenshot para poder visualizar las imagenes después de correr el test
	//En el caso de no poder visualizar la carpeta de Screenshot hacer refresh en el proyecto
}
	