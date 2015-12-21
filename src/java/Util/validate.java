/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.*;
/**
 *
 * @author Gaurab Pradhan
 */
public class validate {

    /**
     *
     * @param username
     * @param password
     * @return
     */
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(validate.class.getName());
    public boolean isLogIn(String username, String password) {
        boolean flag = false;
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Paths p = new Paths();
        File file = new File(p.getUserInfoFile());
        log.info(p.getUserInfoFile());
            BufferedReader br = null;
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(file));
                while ((sCurrentLine = br.readLine()) != null) {
                    if (!sCurrentLine.isEmpty()) {
                        String uname = sCurrentLine.substring(0, sCurrentLine.indexOf('=')).trim();
                        String pass = sCurrentLine.substring((sCurrentLine.indexOf('=') + 1)).trim();
                        if (username.equals(uname) && password.equals(pass)) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        return flag;
    }
}