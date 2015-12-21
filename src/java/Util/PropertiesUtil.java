package Util;

import java.io.*;
import java.util.*;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Gaurab Pradhan
 */
public class PropertiesUtil {

    static String APPLICATION_PROPERTY_FILE;
    static String cleanPath;
    static String ConsDBPath;
    static String cleanParam;
    static String ConsParam;
    static String DatabaseFilePath;
    static String LogPath;
    static String username;
    static String password;
    static String dbName;
    static String dbUrl;
    static String dbTable; // distributions
    static String dbTable1; // trainings
    static String db_Token;
    static String db_repo_temp_path;
    static String db_split_op_path;
    static String db_repo_temp_hdx;
    static String db_hdx_op_path;
    static String InsertDistribution;
    static String InsertTraining;

    public static String getConsDBPath() {
        return ConsDBPath;
    }

    public static String getCleanPath() {
        return cleanPath;
    }

    public static String getCleanParam() {
        return cleanParam;
    }

    public static String getConsParam() {
        return ConsParam;
    }

    public static String getDatabaseFilePath() {
        return DatabaseFilePath;
    }

    public static String getLogPath() {
        return LogPath;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getDbTable() {
        return dbTable;
    }

    public static String getDbTable1() {
        return dbTable1;
    }

    public static String getDb_Token() {
        return db_Token;
    }

    public static String getDb_split_op_path() {
        return db_split_op_path;
    }

    public static String getDb_repo_temp_path() {
        return db_repo_temp_path;
    }

    public static String getDb_repo_temp_hdx() {
        return db_repo_temp_hdx;
    }

    public static String getDb_hdx_op_path() {
        return db_hdx_op_path;
    }

    public static String getInsertDistribution() {
        return InsertDistribution;
    }

    public static String getInsertTraining() {
        return InsertTraining;
    }

    public static void loadPropertiesFile() {
        try {
            Paths p = new Paths();
            APPLICATION_PROPERTY_FILE = p.getPropertiesFile();
            Properties prop = new Properties();
            prop.load(new FileInputStream(APPLICATION_PROPERTY_FILE));
            cleanPath = prop.getProperty("CleanPath");
            ConsDBPath = prop.getProperty("ConsDBPath");
            cleanParam = prop.getProperty("CleanParam");
            ConsParam = prop.getProperty("ConsParam");
            DatabaseFilePath = prop.getProperty("DatabaseFilePath");
            LogPath = prop.getProperty("LogPath");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            dbName = prop.getProperty("dbName");
            dbUrl = prop.getProperty("dbUrl");
            dbTable = prop.getProperty("dbTable");
            dbTable1 = prop.getProperty("dbTable1");
            db_Token = prop.getProperty("db_Token");
            db_repo_temp_path = prop.getProperty("db_repo_temp_path");
            db_split_op_path = prop.getProperty("db_split_op_path");
            db_repo_temp_hdx = prop.getProperty("db_repo_temp_hdx");
            db_hdx_op_path = prop.getProperty("db_hdx_op_path");
            InsertDistribution = prop.getProperty("InsertDistribution");
            InsertTraining = prop.getProperty("InsertTraining");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLog4j() {
        System.out.println(Paths.getLogPath());
        PropertyConfigurator.configure(Paths.getLogPath());
    }
}