package com.dat.CateringService.importHelper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.dat.CateringService.entity.Holidays;

public class HolidayExcelImport {
	
	public static List<Holidays> readHolidayExcel(InputStream inputStream) throws IOException {
		List<Holidays> holidayObjects = new ArrayList<>();
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> iterator = sheet.iterator();
		while(iterator.hasNext()) {
			Row row = iterator.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			
			Holidays holidayObject = new Holidays();
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				int columnIndex = cell.getColumnIndex();
				
				switch(columnIndex) {
				case 0 :
					holidayObject.setHoliday_ID((int)cell.getNumericCellValue());
					break;
				case 1 :
					LocalDateTime date = cell.getLocalDateTimeCellValue();
					holidayObject.setHolidayDate(date.toLocalDate());
					break;
				case 2 :
					holidayObject.setHolidayName(cell.getStringCellValue());
				default :
					break;
				}
			}
			holidayObjects.add(holidayObject);
		}
		workbook.close();
		return holidayObjects;
	}

}
