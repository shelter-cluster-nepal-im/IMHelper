package Util;

/**
 *
 * @author Gaurab Pradhan
 * THis file contains path for server and local
 */
public class Paths {
//    local Path
    static String propertiesFile = "C:/IMHelper/config/application.properties";
    static String userInfoFile = "C:/IMHelper/config/userinfo.ini";
    static String dbHeaderFile = "C:/IMHelper/config/dbColumnHeader.ini";
    static String tempDownloadPath = "C:/IMHelper/download/";
    static String logPath = "C:/IMHelper/config/Log4j.properties";
    // Server path
//    static String propertiesFile = "/var/lib/tomcat8/webapps/IMHelperFiles/config/application.properties";
//    static String userInfoFile = "/var/lib/tomcat8/webapps/IMHelperFiles/config/userinfo.ini";
//    static String dbHeaderFile = "/var/lib/tomcat8/webapps/IMHelperFiles/config/dbColumnHeader.ini";
//    static String tempDownloadPath = "/var/lib/tomcat8/webapps/IMHelperFiles/download/";
//    static String logPath = "/var/lib/tomcat8/webapps/IMHelperFiles/config/Log4j.properties";

    public static String getPropertiesFile() {
        return propertiesFile;
    }

    public static String getUserInfoFile() {
        return userInfoFile;
    }

    public static String getDbHeaderFile() {
        return dbHeaderFile;
    }

    public static String getTempDownloadPath() {
        return tempDownloadPath;
    }

    public static String getLogPath() {
        return logPath;
    }
}