import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.FileOutputStream;

public class FtpDownloadDemo {
	public static void main(String[] args) {
		FTPClient client = new FTPClient();
		FileOutputStream fos = null;
		try {
			client.connect("fransguelinckx.be");
			client.login("fransguelinckx.be", "A0bRXGDp");
			String filename = "sitemap.xml";
			fos = new FileOutputStream(filename);client.retrieveFile("/" + filename, fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}