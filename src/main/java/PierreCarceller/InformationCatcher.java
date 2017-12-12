package PierreCarceller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.*;

class InformationCatcher extends Observable {
    private final String SNP_MARKER_NAME = "SNP marker name";
    private final String CHROMOSOME = "chromosome";
    private final String SNP_POSITION ="snp position";
    private String filePath;
    private FileInputStream excelFile;// = new FileInputStream(new File(FILE_NAME));
    private Workbook workbook;// = new XSSFWorkbook(excelFile);
    private Observer obs;

    InformationCatcher(String filePath, Observer obs) {
        this.filePath=filePath;
        this.obs=obs;
        try {
            this.excelFile = new FileInputStream(new File(this.filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.obs.update(this,"File " + filePath + " not found");
        }
        try {
            this.workbook=new HSSFWorkbook(this.excelFile);
        } catch (IOException e) {
            e.printStackTrace();
            this.obs.update(this,"Unable to create the workbook, check the file format (Only xsl files are accepted)");
        }

    }

    private int getColumnIdByName(Sheet currentSheet, String columnName) {
        int count=0;
        Row firstRow = currentSheet.getRow(0);
        Iterator<Cell> cellIterator = firstRow.cellIterator();
        Cell currentCell = cellIterator.next();
        while (cellIterator.hasNext()){
            if (currentCell.getStringCellValue().equals(columnName)){
                return count;
            }else{
                count++;
                currentCell=cellIterator.next();
            }
        }
        this.obs.update(this,"The column " + columnName + " is not found");
        return -1;
    }

    TreeSet<MarkerPosition> getMarkersPosition(String sheetName){
        TreeSet<MarkerPosition> result = new TreeSet<MarkerPosition>();
        Sheet currentSheet = this.workbook.getSheet(sheetName);
        if (currentSheet==null){
            this.obs.update(this, "Sheet "+sheetName+" not found");
        }
        int columnsNumbers[] = {this.getColumnIdByName(currentSheet,SNP_MARKER_NAME),this.getColumnIdByName(currentSheet,CHROMOSOME),this.getColumnIdByName(currentSheet,SNP_POSITION)};
        Iterator<Row> rowIterator = currentSheet.rowIterator();
        Row currentRow = rowIterator.next();
        currentRow = rowIterator.next();
        while (rowIterator.hasNext()){
            Comparable markerPositionAttribute[]= new Comparable[3];
            for (int i=0;i<columnsNumbers.length;i++){
                if (currentRow.getCell(columnsNumbers[i]).getCellTypeEnum() == CellType.STRING) {
                    markerPositionAttribute[i]=currentRow.getCell(columnsNumbers[i]).getStringCellValue();
                } else if (currentRow.getCell(columnsNumbers[i]).getCellTypeEnum() == CellType.NUMERIC) {
                    Double temp = currentRow.getCell(columnsNumbers[i]).getNumericCellValue();
                    Integer tempInt = temp.intValue();
                    markerPositionAttribute[i]=tempInt;
                }else{
                    this.obs.update(this,"Cell data type is not support, use string or numeric value");
                }
            }
            if (markerPositionAttribute[1]!=null && markerPositionAttribute[2]!=null){
                result.add(new MarkerPosition((String) markerPositionAttribute[0],(String) markerPositionAttribute[1],(Integer) markerPositionAttribute[2]));
            }else{
                result.add(new MarkerPosition((String) markerPositionAttribute[0],"Unknown", new Integer(0)));
            }
            currentRow=rowIterator.next();
        }
        result.last().printMarkerPosition();
        return result;
    }

    ArrayList<String> getSamplesNames(String sheetName){
        ArrayList<String> result = new ArrayList<String>();
        Sheet currentSheet = this.workbook.getSheet(sheetName);
        Row firstRow = currentSheet.getRow(0);
        Iterator<Cell> cells = firstRow.cellIterator();
        Cell cell = cells.next();
        while (cells.hasNext()){
            cell = cells.next();
            result.add(cell.getStringCellValue());
        }
        return result;
    }

    HashMap getHmap(String sheetName){
        HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
        Sheet currentSheet = this.workbook.getSheet(sheetName);
        if (currentSheet==null){
            this.obs.update(this, "Sheet "+sheetName+" not found");
        }else{
            Iterator<Row> rowIterator = currentSheet.rowIterator();
            Row currentRow = rowIterator.next();
            while (rowIterator.hasNext()){
                currentRow = rowIterator.next();
                hashMap.put(currentRow.getCell(0).getStringCellValue(),currentRow.getRowNum()+1);
            }
        }
        return hashMap;
    }

    ArrayList getRowWithHmap(HashMap hashMap, String key, String sheetName){
        ArrayList<Comparable> result = new ArrayList<Comparable>();
        Integer requestedRowNumber = (Integer) hashMap.get(key);
        Sheet currentSheet = this.workbook.getSheet(sheetName);
        if (currentSheet==null){
            this.obs.update(this, "Sheet "+sheetName+" not found");
        }else{
            System.out.println(requestedRowNumber);
            Row requestedRow = currentSheet.getRow(requestedRowNumber-1);
            return this.fillComparableArray(requestedRow);
        }
        return null;
    }

    private ArrayList<Comparable> fillComparableArray(Row currentRow){
        ArrayList<Comparable> result = new ArrayList<Comparable>();
        Iterator<Cell> cellsOfCurrentRow = currentRow.cellIterator();
        Cell currentCell = cellsOfCurrentRow.next();
        while (cellsOfCurrentRow.hasNext()){
            if (currentCell.getCellTypeEnum() == CellType.STRING) {
                result.add(currentCell.getStringCellValue());
            }else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                Double temp = currentCell.getNumericCellValue();
                Integer temp2 = temp.intValue();
                result.add(temp2);
            }
            currentCell = cellsOfCurrentRow.next();
        }
        return result;
    }

}