import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class copyFromExcel {
    //public static final String SAMPLE_XLS_FILE_PATH = "./sample-xls-file.xls";
    public static final String SAMPLE_XLSX_FILE_PATH = "./1.xlsx";

    private static void sheetProceed(Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();

        Map<String, String[]> userMap = new HashMap<>();
        System.out.println("Parse sheet");
        for (Row row: sheet) {
            
            String[] rowArray = new String[32];
            for(Cell cell: row) {
                int cellInd = cell.getColumnIndex();
                if (cellInd==0) {
                    userMap.put(dataFormatter.formatCellValue(cell), rowArray);

                }else{
                    rowArray[cellInd-1] = dataFormatter.formatCellValue(cell);
                }                
                //System.out.print(cellValue + "\t");
            }
            //System.out.println();
        }

        for (Map.Entry<String, String[]> entry: userMap.entrySet()) {
            String key = entry.getKey();
            System.out.println();
            System.out.println(key);

//            for (String rowArray :entry.getValue() ) {
//                String val = rowArray;
//                System.out.println(val);
//            }
            for (int t=0; t<entry.getValue().length; t++) {
                String val = entry.getValue()[t] ;
                System.out.println(t +": "+ val);
            }


        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {

        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        System.out.println("Sheets list:");
        for(Sheet sheet: workbook) {
            System.out.println("=> " + sheet.getSheetName());
        }

        Sheet sheet = workbook.getSheetAt(0);



        sheetProceed(sheet);

        workbook.close();
    }


}