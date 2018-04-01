import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class parseExcelFile {
    //public static final String SAMPLE_XLS_FILE_PATH = "./sample-xls-file.xls";
    //private String XLSX_FILE_PATH = "./1.xlsx";
    private Map<String, String[]> usersShiftsMap = new LinkedHashMap<>();


    public parseExcelFile(String filePath, int sheetInd, int month ) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

//        System.out.println("Sheets list:");
//        for(Sheet sheet: workbook) {
//            System.out.println("=> " + sheet.getSheetName());
//        }

        Sheet sheet = workbook.getSheetAt(sheetInd);
        sheetParse(sheet);
        showSheetContent();
        convertToDate(month);
        workbook.close();
    };

    private void sheetParse(Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();

        System.out.println("Parse selected sheet: "+ sheet.getSheetName());
        for (Row row: sheet) {
            String[] rowArray = new String[32];
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
            String key = entry.getKey();
            System.out.println();
            System.out.println(key);

            for (int t=0; t<entry.getValue().length; t++) {
                String val = entry.getValue()[t] ;
                System.out.println(t +": "+ val);
            }
        }
    }

    private void convertToDate(int month){
        for (Map.Entry<String, String[]> entry: usersShiftsMap.entrySet()) {
            String key = entry.getKey();
            System.out.println();
            System.out.println(key);

            for (int t=0; t<entry.getValue().length; t++) {
                String val = entry.getValue()[t] ;

                System.out.println(t +": "+ val);
            }
        }

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        parseExcelFile parseExcelFile = new parseExcelFile("./1.xlsx", 0, 4 );
    }


}