import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FtpUploadDemo {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        try {
			client.connect("fransguelinckx.be");
			client.login("fransguelinckx.be", "A0bRXGDp");
            String filename = "Touch.dat";
            fis = new FileInputStream(filename);
            client.storeFile(filename, fis);
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}