package Servlet;

import Bean.AgencyNameBean;
import Bean.DistBean;
import Bean.TrainingBean;
import Util.DBConnection;
import Util.DropBoxTask;
import Util.DropBoxCon;
import Util.Paths;
import Util.PropertiesUtil;
import com.dropbox.core.DbxClient;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Gaurab Pradhan
 */
public class SplitServlet extends HttpServlet {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SplitServlet.class.getName());
    static Connection con = null;
    static File file = new File(Paths.getDbHeaderFile());
    String templateFile = null;
    String outputfilePath = null;
    String splitPath = null;
    String tempDownloadPath = Paths.getTempDownloadPath();
    ArrayList<String> outputLog = new ArrayList<String>();
    DbxClient client = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        FileUtils.cleanDirectory(new File(tempDownloadPath + "IMHelperSplit"));
        if (request.getParameter("refreshBtn") != null) {
            try {
                // TODO add your handling code here:
                if (file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                    log.info(file.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                    log.info("Delete operation is failed.");
                }
            } catch (Exception ex) {
                log.info(ex);
            }
            response.sendRedirect("split.jsp");
        } else {
            String splitBy = request.getParameter("SplitBy");
            if (splitBy != null) {
                split(splitBy, request, response);
                if (outputLog.size() > 0) {
                    response.sendRedirect("split.jsp");
                    HttpSession session = request.getSession();
                    session.setAttribute("log", outputLog);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static List<String> fillDropDown() throws SQLException, IOException {
        List<String> dropDownList = new ArrayList<String>();
        // if file doesnt exists, then create it
        if (!file.exists()) {
            con = DBConnection.getConnection();
            if (con != null) {
                Statement stmt = null;
                file.createNewFile();
                System.out.println("Database Connection Established");
                log.info("Database Connection Established");
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM distributions");
                ResultSetMetaData rsmd = rs.getMetaData();
                int rowCount = rsmd.getColumnCount();
                String data[] = new String[rowCount + 1];
                data[0] = "-Select-";
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for (int i = 0; i < rowCount; i++) {
                    if (i == 0) {
                        bw.write(data[i] + "\n");
                        dropDownList.add(data[i]);
                    } else {
                        data[i] = rsmd.getColumnName(i);
                        dropDownList.add(data[i]);
                        bw.write(data[i] + "\n");
                    }
                }
                bw.close();
                con.close();
            } else {
                System.out.println("Failed to make connection!");
                log.info("Failed to make connection!");
            }
        } else {
            int len = (int) file.length();
            System.out.println("File size is " + len + "KB");
            if (len < 1) {
                if (file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                    log.info(file.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                    log.info("Delete operation is failed.");
                }
            } else {
                String data[] = null;
                BufferedReader br = null;
                try {
                    String sCurrentLine;
                    br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                    while ((sCurrentLine = br.readLine()) != null) {
                        dropDownList.add(sCurrentLine);
                    }
                } catch (IOException e) {
                    log.info(e);
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (IOException ex) {
                        log.info(ex);
                        ex.printStackTrace();
                    }
                }
            }
        }
        return dropDownList;
    }

    private void split(String splitBy, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /* TODO output your page here. You may use following sample code. */
        List<AgencyNameBean> agenciesName = new ArrayList<AgencyNameBean>();
        con = DBConnection.getConnection();
        try {
            if (con != null) {
                log.info("Database Connection Established. I am in split method");
                agenciesName = getImpAgenciesName(con, splitBy);
                System.out.println("Size of List " + agenciesName.size());
                log.info("Size of List " + agenciesName.size());
                // get dropbox conncetion
                client = DropBoxCon.getConnection();
                if (client != null) {
                    log.info("Dropbox connected");
                    outputfilePath = PropertiesUtil.getDb_split_op_path();
                    templateFile = PropertiesUtil.getDb_repo_temp_path();
                    String filename = templateFile.substring(templateFile.lastIndexOf("/") + 1);
                    splitPath = DropBoxTask.createFolderinDB(client, outputfilePath + "IMHelperSplit");
//                    client.createFolder(outputfilePath + "IMHelperSplit");
                    DropBoxTask.downloadTemplate(client, templateFile, tempDownloadPath, filename);
                    String localTemplateFile = tempDownloadPath + filename;
                    if (agenciesName.size() > 0) {
                        for (int i = 0; i < agenciesName.size(); i++) {
                            String fname = "temp";
                            List<DistBean> distList = getDistData(con, agenciesName.get(i).toString(), splitBy);
                            List<TrainingBean> trainList = getTrainData(con, agenciesName.get(i).toString(), splitBy);
                            System.out.println("Processing : " + agenciesName.get(i).toString());
                            outputLog.add("Processing : " + agenciesName.get(i).toString());
                            log.info("Processing : " + agenciesName.get(i).toString());
                            fname = agenciesName.get(i).toString().replaceAll("/", "-");
                            if (distList.size() > 0) {
                                if (trainList.isEmpty()) {
                                    writeToExcle(distList, fname, localTemplateFile, outputfilePath);
                                } else {
                                    writeToExcle(distList, trainList, fname, localTemplateFile, outputfilePath);
                                }
                            } else {
                                System.out.println("Distribution Sheet is Empty.");
                                outputLog.add("Distribution Sheet is Empty.");
                                log.info("Distribution Sheet is Empty.");
//                            outputLog.append("Distribution Sheet is Empty.");
//                            outputLog.append("\n");
                            }
                            distList = null;
                            trainList = null;
                        }
                    } else {
                    outputLog.add("Some issue in DB");
                    log.info("Some issue in DB");
                    }
                    agenciesName = null;
                } else {
                    System.out.println("Failed to make connection!");
                    log.info("Failed to make connection!");
//                    JOptionPane.showMessageDialog(null, "Failed to connect to Database!!!", "SC IM Data Processing", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (Exception e) {
            log.info(e);
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.info(e);
                Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
//        response.sendRedirect("split.jsp");
    }

    private static List<AgencyNameBean> getImpAgenciesName(Connection con, String splitBy) throws SQLException {
        List<AgencyNameBean> agenciesName = new ArrayList<AgencyNameBean>();
        Statement stmt = null;
        if (con != null) {
            System.out.println("Database Connection Established");
            stmt = con.createStatement();
//            String query = "SELECT DISTINCT imp_agency FROM " + PropertiesUtil.getDbTable();
            String query = "SELECT DISTINCT " + splitBy + " FROM " + PropertiesUtil.getDbTable();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                AgencyNameBean bean = new AgencyNameBean();
                bean.setImp_agency(rs.getString(splitBy));
                agenciesName.add(bean);
            }
            rs.close();

        } else {
            System.out.println("Failed to make connection!");
        }
        return agenciesName;
    }

    private static List<DistBean> getDistData(Connection con, String agencyName, String splitBy) {
        List<DistBean> mList = new ArrayList<DistBean>();
        PreparedStatement pstmt = null;
        try {
            String query = "SELECT *  FROM " + PropertiesUtil.getDbTable() + " WHERE " + splitBy + " = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, agencyName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DistBean bean = new DistBean();
                //Who
                bean.setImplementingAgency(rs.getString(8));
                bean.setSourcingAgency(rs.getString(9));
                bean.setLocalPartnerAgency(rs.getString(10));
                bean.setContactName(rs.getString(11));
                bean.setContactEmail(rs.getString(12));
                bean.setContactPhoneNumber(rs.getString(13));
//
//                    //Where
                bean.setDistrict(rs.getString(14));
                bean.setVDCMunicipalities(rs.getString(15));
                bean.setMunicipalWard(rs.getString(16));
//
//                    //What
                bean.setActionType(rs.getString(17));
                bean.setActionDescription(rs.getString(18));
                bean.setTargeting(rs.getString(19));
                bean.setItems(rs.getString(20));
                bean.setTotalNumberHouseholds(rs.getString(21));
                bean.setAverageCostPerHouseholds(rs.getString(22));
                bean.setFemaleHeadedHouseholds(rs.getString(23));
                bean.setVulnerableCasteEthnicityHouseholds(rs.getString(24));

//                    //When
                bean.setActivityStatus(rs.getString(25));
                bean.setDDStart(rs.getString(26));
                bean.setMMStart(rs.getString(27));
                bean.setYYStart(rs.getString(28));
                bean.setDDComp(rs.getString(29));
                bean.setMMComp(rs.getString(30));
                bean.setYYComp(rs.getString(31));
//
                bean.setAdditionalComments(rs.getString(32));
                mList.add(bean);
            }
            rs.close();
        } catch (SQLException ex) {
            log.info(ex);
            Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    log.info(ex);
                    Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return mList;
    }

    private List<TrainingBean> getTrainData(Connection con, String agencyName, String splitBy) {
        List<TrainingBean> mList = new ArrayList<TrainingBean>();
        PreparedStatement pstmt = null;
        try {
            String query = "SELECT *  FROM " + PropertiesUtil.getDbTable1() + " WHERE " + splitBy + " = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, agencyName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TrainingBean bean = new TrainingBean();
//                //Who
                bean.setImplementingAgency(rs.getString(8));
                bean.setSourcingAgency(rs.getString(9));
                bean.setLocalPartnerAgency(rs.getString(10));
                bean.setContactName(rs.getString(11));
                bean.setContactEmail(rs.getString(12));
                bean.setContactPhoneNumber(rs.getString(13));
//                
//               //Where
                bean.setDistrict(rs.getString(14));
                bean.setVDCMunicipalities(rs.getString(15));
                bean.setMunicipalWard(rs.getString(16));
//
//                //What
                bean.setTrainingSub(rs.getString(17));
                bean.setAudience(rs.getString(18));
                bean.setTrainingTitle(rs.getString(19));
                bean.setDemonstrationConstructionIncluded(rs.getString(20));
                bean.setIECMaterialsDistributed(rs.getString(21));
                bean.setDurationofeachsession(rs.getString(22));//in hours	
                bean.setAmountPaidtoParticipants(rs.getString(23)); //NRP per participants	
                bean.setTotalCostPerTraining(rs.getString(24));
                bean.setTotalParticipants(rs.getString(25));
                bean.setMales(rs.getString(26));
                bean.setFemales(rs.getString(27));
                bean.setThirdGender(rs.getString(28));
                bean.setElderly(rs.getString(29));//60+
                bean.setChildren(rs.getString(30));//u18	
                bean.setPersonswithDisabilities(rs.getString(31));
                bean.setVulnerableCasteorEthnicity(rs.getString(32));
                bean.setFemaleHH(rs.getString(33));
//                //When
                bean.setActivityStatus(rs.getString(34));
                bean.setDDStart(rs.getString(35));
                bean.setMMStart(rs.getString(36));
                bean.setYYStart(rs.getString(37));
                bean.setDDComp(rs.getString(38));
                bean.setMMComp(rs.getString(39));
                bean.setYYComp(rs.getString(40));

                bean.setAdditionalComments(rs.getString(41));
                mList.add(bean);
            }
            rs.close();
        } catch (SQLException ex) {
            log.info(ex);
            Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    log.info(ex);
                    Logger.getLogger(SplitServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return mList;
    }

    private void writeToExcle(List<DistBean> mainList, String fname, String localTemplateFile, String outputfilePath) throws Exception {
        InputStream file = new FileInputStream(localTemplateFile);
        Workbook workbook = WorkbookFactory.create(file);
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(1);
        int rowIndex = 1;
        for (DistBean bean : mainList) {
            Row row = sheet.createRow(rowIndex);
            //who
            row.createCell(0).setCellValue(bean.getImplementingAgency());
            row.createCell(1).setCellValue(bean.getSourcingAgency());
            row.createCell(2).setCellValue(bean.getLocalPartnerAgency());
            row.createCell(3).setCellValue(bean.getContactName());
            row.createCell(4).setCellValue(bean.getContactEmail());
            row.createCell(5).setCellValue(bean.getContactPhoneNumber());
            //where
            row.createCell(6).setCellValue(bean.getDistrict());
            row.createCell(7).setCellValue(bean.getVDCMunicipalities());
            row.createCell(8).setCellValue(bean.getMunicipalWard());
            //What
            row.createCell(9).setCellValue(bean.getActionType());
            row.createCell(10).setCellValue(bean.getActionDescription());
            row.createCell(11).setCellValue(bean.getTargeting());
            row.createCell(12).setCellValue(bean.getItems());
            row.createCell(13).setCellValue(bean.getTotalNumberHouseholds());
            row.createCell(14).setCellValue(bean.getAverageCostPerHouseholds());
            row.createCell(15).setCellValue(bean.getFemaleHeadedHouseholds());
            row.createCell(16).setCellValue(bean.getVulnerableCasteEthnicityHouseholds());
//                    //When
            row.createCell(17).setCellValue(bean.getActivityStatus());
            row.createCell(18).setCellValue(bean.getDDStart());
            row.createCell(19).setCellValue(bean.getMMStart());
            row.createCell(20).setCellValue(bean.getYYStart());
            row.createCell(21).setCellValue(bean.getDDComp());
            row.createCell(22).setCellValue(bean.getMMComp());
            row.createCell(23).setCellValue(bean.getYYComp());
//            row.createCell(18).setCellValue(bean.getStartDate());
//            row.createCell(19).setCellValue(bean.getCompletionDate());
            row.createCell(24).setCellValue(bean.getAdditionalComments());
            rowIndex++;
        }

        //Writing into excel file
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        String todayDate = dateFormat.format(cal.getTime());

        String filePath = tempDownloadPath + "IMHelperSplit/";
        File folder = new File(filePath);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        FileOutputStream fos = new FileOutputStream(filePath + fname + " - " + todayDate + ".xlsx");
        workbook.write(fos);
        System.out.println(filePath + fname + " - " + todayDate + ".xlsx created");
        String finalFileName = filePath + fname + " - " + todayDate + ".xlsx";
        fos.close();
        DropBoxTask.uploadFile(client, finalFileName, splitPath);
        outputLog.add(splitPath +"/" + fname + " - " + todayDate + ".xlsx created");
        log.info(splitPath +"/" + fname + " - " + todayDate + ".xlsx created");
//        outputLog.append("\n");
    }

    private void writeToExcle(List<DistBean> distList, List<TrainingBean> trainList, String fname, String localTemplateFile, String outputfilePath) throws Exception {
        InputStream file = new FileInputStream(localTemplateFile);
        Workbook workbook = WorkbookFactory.create(file);
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(1);
        int rowIndex = 1;
        for (DistBean bean : distList) {
            Row row = sheet.createRow(rowIndex);
            //who
            row.createCell(0).setCellValue(bean.getImplementingAgency());
            row.createCell(1).setCellValue(bean.getSourcingAgency());
            row.createCell(2).setCellValue(bean.getLocalPartnerAgency());
            row.createCell(3).setCellValue(bean.getContactName());
            row.createCell(4).setCellValue(bean.getContactEmail());
            row.createCell(5).setCellValue(bean.getContactPhoneNumber());
            //where
            row.createCell(6).setCellValue(bean.getDistrict());
            row.createCell(7).setCellValue(bean.getVDCMunicipalities());
            row.createCell(8).setCellValue(bean.getMunicipalWard());
            //What
            row.createCell(9).setCellValue(bean.getActionType());
            row.createCell(10).setCellValue(bean.getActionDescription());
            row.createCell(11).setCellValue(bean.getTargeting());
            row.createCell(12).setCellValue(bean.getItems());
            row.createCell(13).setCellValue(bean.getTotalNumberHouseholds());
            row.createCell(14).setCellValue(bean.getAverageCostPerHouseholds());
            row.createCell(15).setCellValue(bean.getFemaleHeadedHouseholds());
            row.createCell(16).setCellValue(bean.getVulnerableCasteEthnicityHouseholds());
//                    //When
            row.createCell(17).setCellValue(bean.getActivityStatus());
//            row.createCell(18).setCellValue(bean.getStartDate());
//            row.createCell(19).setCellValue(bean.getCompletionDate());
//            row.createCell(20).setCellValue(bean.getAdditionalComments());
            row.createCell(18).setCellValue(bean.getDDStart());
            row.createCell(19).setCellValue(bean.getMMStart());
            row.createCell(20).setCellValue(bean.getYYStart());
            row.createCell(21).setCellValue(bean.getDDComp());
            row.createCell(22).setCellValue(bean.getMMComp());
            row.createCell(23).setCellValue(bean.getYYComp());
            row.createCell(24).setCellValue(bean.getAdditionalComments());
            rowIndex++;
        }
        //Trainings
        org.apache.poi.ss.usermodel.Sheet sheetT = workbook.getSheetAt(2);
        rowIndex = 1;
        for (TrainingBean bean : trainList) {
            Row row = sheetT.createRow(rowIndex);
////            //who
            row.createCell(0).setCellValue(bean.getImplementingAgency());
            row.createCell(1).setCellValue(bean.getSourcingAgency());
            row.createCell(2).setCellValue(bean.getLocalPartnerAgency());
            row.createCell(3).setCellValue(bean.getContactName());
            row.createCell(4).setCellValue(bean.getContactEmail());
            row.createCell(5).setCellValue(bean.getContactPhoneNumber());
////            //where
            row.createCell(6).setCellValue(bean.getDistrict());
            row.createCell(7).setCellValue(bean.getVDCMunicipalities());
            row.createCell(8).setCellValue(bean.getMunicipalWard());
////            //What
            row.createCell(9).setCellValue(bean.getTrainingSub());
            row.createCell(10).setCellValue(bean.getAudience());
            row.createCell(11).setCellValue(bean.getTrainingTitle());
            row.createCell(12).setCellValue(bean.getDemonstrationConstructionIncluded());
            row.createCell(13).setCellValue(bean.getIECMaterialsDistributed());
            row.createCell(14).setCellValue(bean.getDurationofeachsession());
            row.createCell(15).setCellValue(bean.getAmountPaidtoParticipants());
            row.createCell(16).setCellValue(bean.getTotalCostPerTraining());
            row.createCell(17).setCellValue(bean.getTotalParticipants());
            row.createCell(18).setCellValue(bean.getMales());
            row.createCell(19).setCellValue(bean.getFemales());
            row.createCell(20).setCellValue(bean.getThirdGender());
            row.createCell(21).setCellValue(bean.getElderly());
            row.createCell(22).setCellValue(bean.getChildren());
            row.createCell(23).setCellValue(bean.getPersonswithDisabilities());
            row.createCell(24).setCellValue(bean.getVulnerableCasteorEthnicity());
            row.createCell(25).setCellValue(bean.getFemaleHH());
////                    //When
            row.createCell(26).setCellValue(bean.getActivityStatus());
            row.createCell(27).setCellValue(bean.getDDStart());
            row.createCell(28).setCellValue(bean.getMMStart());
            row.createCell(29).setCellValue(bean.getYYStart());
            row.createCell(30).setCellValue(bean.getDDComp());
            row.createCell(31).setCellValue(bean.getMMComp());
            row.createCell(32).setCellValue(bean.getYYComp());
            row.createCell(33).setCellValue(bean.getAdditionalComments());
            rowIndex++;
        }

        //Writing into excel file
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        String todayDate = dateFormat.format(cal.getTime());

        String filePath = tempDownloadPath + "IMHelperSplit/";
        File folder = new File(filePath);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        FileOutputStream fos = new FileOutputStream(filePath + fname + " - " + todayDate + ".xlsx");
        workbook.write(fos);
        System.out.println(filePath + fname + " - " + todayDate + ".xlsx created");
        String finalFileName = filePath + fname + " - " + todayDate + ".xlsx";
        fos.close();
        DropBoxTask.uploadFile(client, finalFileName, splitPath);
        outputLog.add(splitPath +"/" + fname + " - " + todayDate + ".xlsx created");
        log.info(splitPath +"/" + fname + " - " + todayDate + ".xlsx created");
//        outputLog.append("\n");
    }
}