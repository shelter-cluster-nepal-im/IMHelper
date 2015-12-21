package Util;

import com.dropbox.core.*;
import java.util.Locale;

/**
 *
 * @author Gaurab Pradhan
 */
public class DropBoxCon {
     public static DbxClient getConnection() throws DbxException {
        DbxClient client = null;
        String token = PropertiesUtil.getDb_Token();
        DbxRequestConfig config = new DbxRequestConfig("Shelter Cluster/1.0", Locale.getDefault().toString());
        client = new DbxClient(config, token);
        System.out.println("Linked account: " + client.getAccountInfo().displayName);
        return client;
     }
}
