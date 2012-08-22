

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpListDemo {
	public static void main(String[] args) {
		FTPClient client = new FTPClient();
		try {
			client.connect("fransguelinckx.be");
			client.login("fransguelinckx.be", "A0bRXGDp");
			String[] names = client.listNames();
			for (String name : names) {
				System.out.println("Name = " + name);
			}

			FTPFile[] ftpFiles = client.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getType() == FTPFile.FILE_TYPE) {
					System.out.println("FTPFile: "
							+ ftpFile.getName()
							+ "; ");
				}
			}
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}