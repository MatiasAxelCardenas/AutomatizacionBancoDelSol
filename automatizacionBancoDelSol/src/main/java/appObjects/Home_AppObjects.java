package appObjects;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebElement;

import entities.Browser;
import entities.Function;

public class Home_AppObjects {
	
	static Properties propiedades = new Properties();

	// Carga el archivo de propiedades para el appObject
	static {
		FileInputStream archivoPropiedades;

		try {
			archivoPropiedades = new FileInputStream(Function.getRutaDataPools() + "\\PaginaHome.properties");
			propiedades.load(archivoPropiedades);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static WebElement getInputSearchArticles() {
		return Browser.searchElement(propiedades.getProperty("methodInputSearchArticles"),
				propiedades.getProperty("inputSearchArticles"));
	}
	
	public static WebElement getBtnSearch() {
		return Browser.searchElement(propiedades.getProperty("methodBtnSearch"),
				propiedades.getProperty("btnSearch"));
	}
	
	public static WebElement getDivQuatityOfResults() {
		return Browser.searchElement(propiedades.getProperty("methodDivQuatityOfResults"),
				propiedades.getProperty("divQuatityOfResults"));
	}
}
