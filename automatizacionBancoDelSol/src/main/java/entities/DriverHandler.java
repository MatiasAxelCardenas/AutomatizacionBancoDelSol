package entities;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.testng.Reporter;

public class DriverHandler {

	/**
	 * Función creada para obtener los drivers que se encuentran dentro del jar
	 * exportado del framework
	 * 
	 * @param navegador Navegador a utilizar, puede ser "chrome", "ie" o
	 *                  "chromeMacOs"
	 * @return retorna un string con la ruta del driver temporal
	 */
	public static String getDriver(String navegador) {

		final String chromepath = "chromedriver.exe";
		final String iepath = "IEDriverServer.exe";
		final String chromeMacOsPath = "chromedriverMacOs";
		final URI uri;
		String path = null;
		String driverPath;
		URI exe = null;

		switch (navegador.toLowerCase()) {
		case "chrome":
			driverPath = chromepath;
			break;

		case "ie":
			driverPath = iepath;
			break;
		case "chromemacos":
			driverPath = chromeMacOsPath;
			break;
		default:
			driverPath = chromepath;
			break;
		}

		try {
			uri = getJarURI();
			exe = getFile(uri, driverPath);
			path = exe.toString().replaceFirst("file:/", "");
		} catch (IOException e) {
			Reporter.log("Fallo al instanciar el driver: " + e.getMessage());
		} catch (URISyntaxException e) {
			Reporter.log("Fallo en la sintaxis de la URI: " + e.getMessage());
		}

		return path;
	}

	private static URI getJarURI() throws URISyntaxException {
		final ProtectionDomain domain;
		final CodeSource source;
		final URL url;
		final URI uri;

		domain = DriverHandler.class.getProtectionDomain();
		source = domain.getCodeSource();
		url = source.getLocation();
		uri = url.toURI();

		return (uri);
	}

	private static URI getFile(final URI where, final String fileName) throws ZipException, IOException {
		final File location;
		final URI fileURI;

		location = new File(where);

		// not in a JAR, just return the path on disk
		if (location.isDirectory()) {
			fileURI = URI.create(where.toString() + fileName);

		} else {
			final ZipFile zipFile;
			zipFile = new ZipFile(location);
			try {
				fileURI = extract(zipFile, fileName);
			} finally {
				zipFile.close();
			}
		}

		return (fileURI);
	}

	private static URI extract(final ZipFile zipFile, final String fileName) throws IOException {
		final File tempFile;
		final ZipEntry entry;
		final InputStream zipStream;
		OutputStream fileStream;

		tempFile = File.createTempFile(fileName, Long.toString(System.currentTimeMillis()));
		tempFile.deleteOnExit();
		entry = zipFile.getEntry(fileName);

		if (entry == null) {
			throw new FileNotFoundException("cannot find file: " + fileName + " in archive: " + zipFile.getName());
		}

		zipStream = zipFile.getInputStream(entry);
		fileStream = null;

		try {
			final byte[] buf;
			int i;

			fileStream = new FileOutputStream(tempFile);
			buf = new byte[1024];
			i = 0;

			while ((i = zipStream.read(buf)) != -1) {
				fileStream.write(buf, 0, i);
			}
		} finally {
			close(zipStream);
			close(fileStream);
		}

		return (tempFile.toURI());
	}

	private static void close(final Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (final IOException ex) {
				Reporter.log("Fallo al finalizar: " + ex.getMessage());
			}
		}
	}
}
