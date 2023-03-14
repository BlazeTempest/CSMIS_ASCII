import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDateFunction {

	public static void main(String[] args) {
		List<LocalDate> holidays = new ArrayList<>();
		holidays.add(LocalDate.now());
		LocalDate currentDate = LocalDate.now();
//		System.out.println(currentDate);
		 
		LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
//		System.out.println(firstDayOfMonth);
		
		LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
		System.out.println("Last day of month:" + lastDayOfMonth);
//		for(int i=0; i<3; i++) {
//			currentDate.plusWeeks(i);
//			System.out.println(currentDate);
//		}
		// Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
		LocalDate date = firstDayOfMonth;
		while (date.getMonth() == currentDate.getMonth()) {
		    DayOfWeek dayOfWeek = date.getDayOfWeek();
		    if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && !holidays.contains(date)) {
		        // Add the date to a list or process it in some other way
		    	System.out.println("Date: " + date);
		    	System.out.println(dayOfWeek);
		    }
		    date = date.plusDays(1);
		}

	}

}
