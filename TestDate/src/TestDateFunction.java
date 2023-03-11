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
		
		
		LocalDate date = firstDayOfMonth;
		while (date.getMonth() == currentDate.getMonth()) {
		    DayOfWeek dayOfWeek = date.getDayOfWeek();
		    if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && !holidays.contains(date)) {
		        // Add the date to a list or process it in some other way
		    	System.out.println(date);
		    }
		    date = date.plusDays(1);
		}

	}

}
