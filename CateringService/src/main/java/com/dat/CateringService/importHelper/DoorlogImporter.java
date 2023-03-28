package com.dat.CateringService.importHelper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.dat.CateringService.entity.DailyDoorLog;
import com.dat.CateringService.entity.Staff;

public class DoorlogImporter {
	
	public static List<DailyDoorLog> readExcel(InputStream inputStream) throws IOException {
        List<DailyDoorLog> objects = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // assuming only one sheet in the workbook
        Iterator<Row> iterator = sheet.iterator();
        int doorId = 0;
        System.out.println("Start: " + doorId);
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (row.getRowNum() == 0) { // skip the header row
                continue;
            }
            
            DailyDoorLog object = new DailyDoorLog();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
	                case 0:
	                	break;
                	case 1:
                		break;
                    case 2:
                    	int doorlog = Integer.parseInt(cell.getStringCellValue());
                    	object.setDoorLogNo(doorlog);
                        break;
                    case 3:
                    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy H:mm:ss");
                    	LocalDate date = LocalDate.parse(cell.getStringCellValue(), formatter);
                    	object.setDineDate(date);
                    	break;
                    default:
                        break;
                }
            }
            
            if(doorId==0) objects.add(object); 
            if(doorId==0) doorId = object.getDoorLogNo();
            System.out.println("Previous Id: " + doorId);
            if(object.getDoorLogNo()!=doorId) {
            	objects.add(object);
            }
            doorId = object.getDoorLogNo();
            System.out.println("Next Id: " + doorId);
        }
        workbook.close();
        return objects;
    }
}
