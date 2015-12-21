package Util;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author Gaurab Pradhan downloads file from dropbox
 */
public class DropBoxTask {

    public static void downloadTemplate(DbxClient client, String templateFile, String tempDownloadPath, String filename) throws Exception {
        File file = new File(tempDownloadPath + filename);
        if (!file.exists()) {
            FileOutputStream outputStream = new FileOutputStream(tempDownloadPath + filename);
            try {
                DbxEntry.File downloadedFile = client.getFile(templateFile, null, outputStream);
                System.out.println("Metadata: " + downloadedFile.toString());
            } finally {
                outputStream.close();
            }
        }
    }

    public static String createFolderinDB(DbxClient client, String outputFilePath) throws DbxException {
        DbxEntry.WithChildren listing = client.getMetadataWithChildren(outputFilePath);
        if (listing == null) {
            client.createFolder(outputFilePath);
        }
        return outputFilePath;
    }

    public static void uploadFile(DbxClient client, String finalFileName, String path) throws Exception {
        File inputFile = new File(finalFileName);
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            String fname = finalFileName.substring(finalFileName.lastIndexOf("/") + 1);
            DbxEntry.File uploadedFile = client.uploadFile(path +"/" + fname ,DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } finally {
            inputStream.close();
        }
    }

}
