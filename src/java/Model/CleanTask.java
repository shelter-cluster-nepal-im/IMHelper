package Model;

import Util.PropertiesUtil;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gaurab Pradhan
 */
public class CleanTask extends Thread {

    String filePath;

    public void clean(String path) throws IOException, InterruptedException {
        PropertiesUtil.loadPropertiesFile();
        filePath = path;
        String cleanPara = PropertiesUtil.getCleanParam();
        String pythonScriptPath = cleanPara + " \"" + filePath + "\"";
        System.out.println(pythonScriptPath);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(pythonScriptPath);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = bfr.readLine()) != null) {
            // display each output line form python script
            System.out.println(line);
//            cleanLogs.append(line);
//            cleanLogs.append("\n");
            Thread.sleep(1000);
        }
    }

}
