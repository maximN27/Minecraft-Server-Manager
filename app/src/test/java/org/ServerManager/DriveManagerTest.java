package org.ServerManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DriveManagerTest {
    @Test
    public void uploadFileTest() throws Exception{
        String fileID = null;
        DriveManager dm = new DriveManager();
        String FILE_PATH = "D:/Amalanathan/image.png";

        fileID = dm.uploadFile("image2.png", FILE_PATH, "image/png");
        System.out.println(fileID);
        assertNotNull(fileID);
    }
}
