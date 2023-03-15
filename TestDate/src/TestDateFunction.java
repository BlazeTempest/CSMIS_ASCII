import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

public class TestDateFunction {

	public static void main(String[] args) {
//		List<LocalDate> holidays = new ArrayList<>();
//		holidays.add(LocalDate.now());
		List<LocalDate> mondays = new ArrayList<>();
		List<LocalDate> tuesdays = new ArrayList<>();
		List<LocalDate> wednesdays = new ArrayList<>();
		List<LocalDate> thursdays = new ArrayList<>();
		List<LocalDate> fridays = new ArrayList<>();
		List<LocalDate> saturdays = new ArrayList<>();
		List<LocalDate> sundays = new ArrayList<>();
		List<List<LocalDate>> month = new ArrayList<>();
		
		LocalDate currentDate = LocalDate.now();
		 
		LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

		LocalDate date = firstDayOfMonth;
		
		List<LocalDate> week = new ArrayList<>();
		DayOfWeek firstDay = firstDayOfMonth.getDayOfWeek();
		if(firstDay == DayOfWeek.SUNDAY) {
	    	week.add(null);
	    	System.out.println("Sun ADDED null");
	    }
	    if(firstDay == DayOfWeek.MONDAY) {
	    	for(int i=0; i<2; i++) {
	    		week.add(null);
	    		System.out.println("Mon ADDED null");
	    	}
	    }
	    if(firstDay == DayOfWeek.TUESDAY) {
	    	for(int i=0; i<3; i++) {
	    		week.add(null);
	    		System.out.println("Tue ADDED null");
	    	}
	    }
	    if(firstDay == DayOfWeek.WEDNESDAY) {
	    	for(int i=0; i<4; i++) {
	    		week.add(null);
	    		System.out.println("Wed ADDED null");
	    	}
	    }
	    if(firstDay == DayOfWeek.THURSDAY) {
	    	for(int i=0; i<5; i++) {
	    		week.add(null);
	    		System.out.println("Thur ADDED null");
	    	}
	    }
	    if(firstDay == DayOfWeek.FRIDAY) {
	    	for(int i=0; i<6; i++) {
	    		week.add(null);
	    		System.out.println("Fri ADDED null");
	    	}
	    }
	    if(firstDay == DayOfWeek.SATURDAY) {
	    	for(int i=0; i<7; i++) {
	    		week.add(null);
	    		System.out.println("Sat ADDED null");
	    	}
	    }
		while (date.getDayOfMonth() == date.lengthOfMonth()) {
			
		    DayOfWeek dayOfWeek = date.getDayOfWeek();
		    if(week.size()==7) {
		    	month.add(week);
		    	for(int i=0; i<week.size(); i++) System.out.println("Week" + i + ": " + week.get(i));
		    	week = new ArrayList<>();
		    }
		    
		    week.add(date);
		    date = date.plusDays(1);
		    
//		    if(dayOfWeek == DayOfWeek.MONDAY) mondays.add(date);
//		    if(dayOfWeek == DayOfWeek.TUESDAY) tuesdays.add(date);
//		    if(dayOfWeek == DayOfWeek.WEDNESDAY) wednesdays.add(date);
//		    if(dayOfWeek == DayOfWeek.THURSDAY) thursdays.add(date);
//		    if(dayOfWeek == DayOfWeek.FRIDAY) fridays.add(date);
//		    if(dayOfWeek == DayOfWeek.SATURDAY) saturdays.add(date);
//		    if(dayOfWeek == DayOfWeek.SUNDAY) sundays.add(date);
		    
		    
		    
		}
//		for(LocalDate d : mondays) {
//			System.out.println("Monday: "+d);
//		}
//		for(LocalDate d : tuesdays) {
//			System.out.println("Tuesday: "+d);
//		}
//		for(LocalDate d : wednesdays) {
//			System.out.println("Wednesday: "+d);
//		}
//		for(LocalDate d : thursdays) {
//			System.out.println("Thursday: "+d);
//		}
//		for(LocalDate d : fridays) {
//			System.out.println("Friday: "+d);
//		}
//		for(LocalDate d : saturdays) {
//			System.out.println("Saturday: "+d);
//		}
//		for(LocalDate d : sundays) {
//			System.out.println("Sunday: "+d);
//		}
//		month.add(mondays);
//		month.add(tuesdays);
//		month.add(wednesdays);
//		month.add(thursdays);
//		month.add(fridays);
//		month.add(saturdays);
//		month.add(sundays);
		for(List<LocalDate> tempWeek : month) {
			for(LocalDate day : tempWeek) {
				System.out.println(": " + day);
			}
		}
	}

}
