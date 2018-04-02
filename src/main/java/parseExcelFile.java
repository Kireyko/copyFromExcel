import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class parseExcelFile {
    //public static final String SAMPLE_XLS_FILE_PATH = "./sample-xls-file.xls";
    //private String XLSX_FILE_PATH = "./1.xlsx";
    private static int month;
    private Map<String, String[]> usersShiftsMap = new LinkedHashMap<>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");


    public parseExcelFile(String filePath, int sheetInd, int month ) throws IOException, InvalidFormatException {
        this.month=month;
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

//        System.out.println("Sheets list:");
//        for(Sheet sheet: workbook) {
//            System.out.println("=> " + sheet.getSheetName());
//        }

        Sheet sheet = workbook.getSheetAt(sheetInd);
        sheetParse(sheet);
        //showSheetContent();
        processParsedArray();
        workbook.close();
    };

    private void sheetParse(Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();

        System.out.println("Parse selected sheet: "+ sheet.getSheetName());
        for (Row row: sheet) {
            String[] rowArray = new String[31];
            for(Cell cell: row) {
                int cellInd = cell.getColumnIndex();
                if (cellInd==0) {
                    usersShiftsMap.put(dataFormatter.formatCellValue(cell), rowArray);
                }else{
                    rowArray[cellInd-1] = dataFormatter.formatCellValue(cell);
                }                
            }
        }
    }

    private void showSheetContent(){
        //Show/save to database
        for (Map.Entry<String, String[]> entry: usersShiftsMap.entrySet()) {
            String key = entry.getKey().trim();
            System.out.println();
            System.out.println(key);

            for (int t=0; t<entry.getValue().length; t++) {
                String val = entry.getValue()[t] ;
                System.out.println(t +": "+ val);
            }
        }
    }

    private void processParsedArray(){
        for (Map.Entry<String, String[]> entry: usersShiftsMap.entrySet()) {
            String key = entry.getKey().trim();
            System.out.println();
            System.out.println(key);

            for (int t=0; t<entry.getValue().length; t++) {
                String val = entry.getValue()[t];

                if (val != null && !val.isEmpty()){
                    System.out.println(t +": "+ val);
                    parseCell(val.trim(), t);
                    System.out.println();
                }
            }
        }

    }

    private void parseCell(String value, int position){

        String[] shifts = value.split("\\/");
        for (String shift:shifts ) {
            //parse shift
            String[] shiftHours = shift.split("\\-");
            for (String strHour:shiftHours ) {
                //System.out.println(strHour.trim());
                convertToTime(strHour, position);
            }
        }
    }

    private void convertToTime(String strVal, int position){
        String day = "2018." +String.format("%02d",month)+"." + String.format("%02d", position+1)+" ";

        String strDateTime;
        if (strVal.indexOf(":")>0) strDateTime= day + strVal;
        else strDateTime= day + strVal + ":00";

        Date date  = null;
        try {
            date = simpleDateFormat.parse(strDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(simpleDateFormat.format(date));

    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        parseExcelFile parseExcelFile = new parseExcelFile("./1.xlsx", 0, 4 );
    }


}