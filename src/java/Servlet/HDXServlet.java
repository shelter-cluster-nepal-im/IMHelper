package Servlet;

import Bean.*;
import Util.*;
import com.dropbox.core.DbxClient;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Gaurab Pradhan
 */
public class HDXServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String templateFile = null;
    String outputfilePath = null;
    String hdxPath = null;
    DbxClient client = null;
    ArrayList<String> outputLog = new ArrayList<String>();
//    String tempDownloadPath = "D:/Gaurab/JavaProj/IMHelper/download/";
//    String tempDownloadPath = "/var/lib/tomcat8/webapps/download/";
    String tempDownloadPath = Paths.getTempDownloadPath();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet HDXServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet HDXServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {
//            out.close();
//        }
        FileUtils.cleanDirectory(new File(tempDownloadPath + "HDX"));
        getDataForHDX();
        response.sendRedirect("hdx.jsp");
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
            Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    private void getDataForHDX() throws Exception {
        Connection con = DBConnection.getConnection();
        try {
            if (con != null) {
                List<DistBean> distList = getDistData(con);
                List<TrainingBean> trainList = getTrainData(con);
                System.out.println("Processing to generate HDX DB");
                outputLog.add("Database Connection Established");
//                    outputLog.append("Processing to generate HDX DB \n");
//              local path
                String filePathTemplate = tempDownloadPath;
                String filePathOutput = tempDownloadPath + "HDX/";
                // dropbox path
                outputfilePath = PropertiesUtil.getDb_hdx_op_path();
                templateFile = PropertiesUtil.getDb_repo_temp_hdx();
                client = DropBoxCon.getConnection();
                String filename = templateFile.substring(templateFile.lastIndexOf("/") + 1);
                if (client != null) {
                    if (distList.size() > 0) {
//                        hdxPath = DropBoxTask.createFolderinDB(client, outputfilePath);
//                    client.createFolder(outputfilePath + "IMHelperSplit");
                        DropBoxTask.downloadTemplate(client, templateFile, filePathTemplate, filename);
                        filePathTemplate = tempDownloadPath + filename;
                        writeToHDXExcle(distList, trainList, filePathTemplate, filePathOutput);
                    } else {
                        System.out.println("Distribution Sheet is Empty.");
//                        outputLog.append("Distribution Sheet is Empty.\n");
                    }
                    distList = null;
                    trainList = null;
                }
            } else {
                System.out.println("Failed to make connection!");
//                    JOptionPane.showMessageDialog(null, "Failed to connect to Database!!!", "SC IM Data Processing", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private List<DistBean> getDistData(Connection con) {
        List<DistBean> mList = new ArrayList<DistBean>();
        Statement stmt = null;
        try {
            String query = "SELECT *  FROM " + PropertiesUtil.getDbTable() + " ORDER BY imp_agency ASC";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                DistBean bean = new DistBean();
                //first
                String priority = rs.getString(1);
                if (priority.toUpperCase().equals("TRUE")) {
                    bean.setPriority("Priority");
                } else {
                    bean.setPriority("");
                }
                bean.setAccessMethods(rs.getString(2));
                bean.setHub(rs.getString(3));
                bean.setLastUpdate(rs.getString(4));
                bean.setDistrictHLCITCode(rs.getString(5));
                bean.setVDCHLCITCode(rs.getString(6));
//                bean.setPriority(rs.getString(7));
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
            Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return mList;
    }

    private List<TrainingBean> getTrainData(Connection con) {
        List<TrainingBean> mList = new ArrayList<TrainingBean>();
        Statement stmt = null;
        try {
            String query = "SELECT *  FROM " + PropertiesUtil.getDbTable1() + " ORDER BY imp_agency ASC";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                TrainingBean bean = new TrainingBean();
                //first
                String priority = rs.getString(1);
                if (priority.toUpperCase().equals("TRUE")) {
                    bean.setPriority("Priority");
                } else {
                    bean.setPriority("");
                }
                bean.setAccessMethods(rs.getString(2));
                bean.setHub(rs.getString(3));
                bean.setLastUpdate(rs.getString(4));
                bean.setDistrictHLCITCode(rs.getString(5));
                bean.setVDCHLCITCode(rs.getString(6));
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
            Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(HDXServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return mList;
    }

    private void writeToHDXExcle(List<DistBean> distList, List<TrainingBean> trainList, String filePathTemplate, String filePathOutput) throws Exception {
        InputStream file = new FileInputStream(filePathTemplate);
        Workbook workbook = WorkbookFactory.create(file);
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 1;
        for (DistBean bean : distList) {
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(bean.getPriority());
            row.createCell(1).setCellValue(bean.getAccessMethods());
            row.createCell(2).setCellValue(bean.getHub());
            row.createCell(3).setCellValue(bean.getLastUpdate());
            row.createCell(4).setCellValue(bean.getDistrictHLCITCode());
            row.createCell(5).setCellValue(bean.getVDCHLCITCode());
            //who
            row.createCell(6).setCellValue(bean.getImplementingAgency());
            row.createCell(7).setCellValue(bean.getSourcingAgency());
            row.createCell(8).setCellValue(bean.getLocalPartnerAgency());
//            row.createCell(3).setCellValue(bean.getContactName());
//            row.createCell(4).setCellValue(bean.getContactEmail());
//            row.createCell(5).setCellValue(bean.getContactPhoneNumber());
            //where
            row.createCell(9).setCellValue(bean.getDistrict());
            row.createCell(10).setCellValue(bean.getVDCMunicipalities());
            row.createCell(11).setCellValue(bean.getMunicipalWard());
            //What
            row.createCell(12).setCellValue(bean.getActionType());
            row.createCell(13).setCellValue(bean.getActionDescription());
            row.createCell(14).setCellValue(bean.getTargeting());
            row.createCell(15).setCellValue(bean.getItems());
            row.createCell(16).setCellValue(bean.getTotalNumberHouseholds());
            row.createCell(17).setCellValue(bean.getAverageCostPerHouseholds());
            row.createCell(18).setCellValue(bean.getFemaleHeadedHouseholds());
            row.createCell(19).setCellValue(bean.getVulnerableCasteEthnicityHouseholds());
//                    //When
            row.createCell(20).setCellValue(bean.getActivityStatus());
//            row.createCell(18).setCellValue(bean.getStartDate());
//            row.createCell(19).setCellValue(bean.getCompletionDate());
//            row.createCell(20).setCellValue(bean.getAdditionalComments());
            if (bean.getDDStart() == null) {
                row.createCell(21).setCellValue("");
            } else {
                row.createCell(21).setCellValue(bean.getDDStart() + "/" + bean.getMMStart() + "/" + bean.getYYStart());
            }
            if (bean.getDDComp() == null) {
                row.createCell(22).setCellValue("");
            } else {
                row.createCell(22).setCellValue(bean.getDDComp() + "/" + bean.getMMComp() + "/" + bean.getYYComp());
            }
            row.createCell(23).setCellValue(bean.getAdditionalComments());
            rowIndex++;
        }
        //Trainings
        org.apache.poi.ss.usermodel.Sheet sheetT = workbook.getSheetAt(1);
        rowIndex = 1;
        for (TrainingBean bean : trainList) {
            Row row = sheetT.createRow(rowIndex);
            row.createCell(0).setCellValue(bean.getPriority());
            row.createCell(1).setCellValue(bean.getAccessMethods());
            row.createCell(2).setCellValue(bean.getHub());
            row.createCell(3).setCellValue(bean.getLastUpdate());
            row.createCell(4).setCellValue(bean.getDistrictHLCITCode());
            row.createCell(5).setCellValue(bean.getVDCHLCITCode());
////            //who
            row.createCell(6).setCellValue(bean.getImplementingAgency());
            row.createCell(7).setCellValue(bean.getSourcingAgency());
            row.createCell(8).setCellValue(bean.getLocalPartnerAgency());
//            row.createCell(3).setCellValue(bean.getContactName());
//            row.createCell(4).setCellValue(bean.getContactEmail());
//            row.createCell(5).setCellValue(bean.getContactPhoneNumber());
////            //where
            row.createCell(9).setCellValue(bean.getDistrict());
            row.createCell(10).setCellValue(bean.getVDCMunicipalities());
            row.createCell(11).setCellValue(bean.getMunicipalWard());
////            //What
            row.createCell(12).setCellValue(bean.getTrainingSub());
            row.createCell(13).setCellValue(bean.getAudience());
            row.createCell(14).setCellValue(bean.getTrainingTitle());
            row.createCell(15).setCellValue(bean.getDemonstrationConstructionIncluded());
            row.createCell(16).setCellValue(bean.getIECMaterialsDistributed());
            row.createCell(17).setCellValue(bean.getDurationofeachsession());
            row.createCell(18).setCellValue(bean.getAmountPaidtoParticipants());
            row.createCell(19).setCellValue(bean.getTotalCostPerTraining());
            row.createCell(20).setCellValue(bean.getTotalParticipants());
            row.createCell(21).setCellValue(bean.getMales());
            row.createCell(22).setCellValue(bean.getFemales());
            row.createCell(23).setCellValue(bean.getThirdGender());
            row.createCell(24).setCellValue(bean.getElderly());
            row.createCell(25).setCellValue(bean.getChildren());
            row.createCell(26).setCellValue(bean.getPersonswithDisabilities());
            row.createCell(27).setCellValue(bean.getVulnerableCasteorEthnicity());
            row.createCell(28).setCellValue(bean.getFemaleHH());
////                    //When
            row.createCell(29).setCellValue(bean.getActivityStatus());
            if (bean.getDDStart() == null) {
                row.createCell(30).setCellValue("");
            } else {
                row.createCell(30).setCellValue(bean.getDDStart() + "/" + bean.getMMStart() + "/" + bean.getYYStart());
            }
            if (bean.getDDComp() == null) {
                row.createCell(31).setCellValue("");
            } else {
                row.createCell(31).setCellValue(bean.getDDComp() + "/" + bean.getMMComp() + "/" + bean.getYYComp());
            }
//                row.createCell(30).setCellValue(bean.getDDStart() + "/" + bean.getMMStart() + "/" + bean.getYYStart());
//                row.createCell(31).setCellValue(bean.getDDComp() + "/" + bean.getMMComp() + "/" + bean.getYYComp());
            row.createCell(32).setCellValue(bean.getAdditionalComments());
            rowIndex++;
        }

        //Writing into excel file
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        Calendar cal = Calendar.getInstance();
        String todayDate = dateFormat.format(cal.getTime());

        String filePath = filePathOutput;
        File folder = new File(filePath);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        FileOutputStream fos = new FileOutputStream(filePath + "DatabaseV5.0_" + todayDate + "for HDX.xlsx");
        workbook.write(fos);
        System.out.println(filePath + "DatabaseV5.0_" + todayDate + "for HDX.xlsx");
        fos.close();
//        outputLog.append("\n");
        String finalFileName = filePath + "DatabaseV5.0_" + todayDate + "for HDX.xlsx";
        outputLog.add(outputfilePath + "/" + "DatabaseV5.0_" + todayDate + "for HDX.xlsx");
        DropBoxTask.uploadFile(client, finalFileName, outputfilePath);
    }
}
