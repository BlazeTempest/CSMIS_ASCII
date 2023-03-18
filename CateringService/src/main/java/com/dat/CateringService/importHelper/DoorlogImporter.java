package com.dat.CateringService.importHelper;

import java.io.IOException;
import java.io.InputStream;
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
        DataFormatter formatter = new DataFormatter();
        Iterator<Row> iterator = sheet.iterator();
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
	                	object.setStaffID(formatter.formatCellValue(cell));
	                	break;
                	case 1:
                		break;
                    case 2:
                    	object.setDoorLogNo((int) cell.getNumericCellValue());
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
