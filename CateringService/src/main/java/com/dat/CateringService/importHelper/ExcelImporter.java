package com.dat.CateringService.importHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.dat.CateringService.entity.Staff;


public class ExcelImporter {
	
	public static List<Staff> readCSV(InputStream inputStream) throws IOException, NumberFormatException, CsvException {
	    List<Staff> objects = new ArrayList<>();
	    Reader reader = new InputStreamReader(inputStream);
	    CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(3).build(); // assuming header rows
	    List<String[]> rows = csvReader.readAll();
	    for(String[] row : rows) {
	    	Staff staff = new Staff();
	    	staff.setDivision(row[1]);
	    	staff.setStaffID(row[2]);
	    	staff.setName(row[3]);
	    	staff.setDoorLogNo(Integer.parseInt(row[4]));
	    	staff.setDept(row[5]);
	    	staff.setTeam(row[6]);
	    	staff.setEmail(row[7]);
	    	objects.add(staff);
	    }
//	    while ((row = csvReader.readNext()) != null) {
//	        Staff object = new Staff();
//	        object.setDivision(row[1]);
//	        object.setStaffID(row[2]);
//	        object.setName(row[3]);
//	        object.setDoorLogNo(Integer.parseInt(row[4]));
//	        object.setDept(row[5]);
//	        object.setTeam(row[6]);
//	        object.setEmail(row[7]);
//	        objects.add(object);
//	    }
	    csvReader.close();
	    return objects;
	}

    public static List<Staff> readExcel(InputStream inputStream) throws IOException {
        List<Staff> objects = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // assuming only one sheet in the workbook
        DataFormatter formatter = new DataFormatter();
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (row.getRowNum() == 0 || row.getRowNum() == 1 || row.getRowNum() == 2 || row.getRowNum() == 3) { // skip the header row
                continue;
            }
            
            Staff object = new Staff();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
	                case 0:
	                	break;
                	case 1:
                		object.setDivision(formatter.formatCellValue(cell));
                		break;
                    case 2: 
                    	object.setStaffID(formatter.formatCellValue(cell));
                        break;
                    case 3:
                    	object.setName(formatter.formatCellValue(cell));
                        break;
                    case 4:
                    	object.setDoorLogNo((int) cell.getNumericCellValue());
                        break;
                    case 5:
                    	object.setDept(formatter.formatCellValue(cell));
                    	break;
                    case 6:
                    	object.setTeam(formatter.formatCellValue(cell));
                    	break;
                    case 7:
                    	object.setEmail(cell.getStringCellValue());
                    	break;
                    case 8:
                    	break;
                    default:
                        break;
                }
            }
            objects.add(object);
        }
        workbook.close();
        return objects;
    }
}
