import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeekOfMonth {
    public static void main(String[] args) {
    	LocalDate date = LocalDate.now(); // get the current date
    	int month = date.getMonthValue();
    	WeekFields weekFields = WeekFields.of(Locale.getDefault());
    	List<List<LocalDate>> weeks = new ArrayList<>();

    	// loop over the weeks of the month
    	LocalDate firstDayOfMonth = date.withDayOfMonth(1);
    	LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
    	int firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
    	int lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());

    	// add days from previous month to first week
    	List<LocalDate> daysOfFirstWeek = new ArrayList<>();
    	LocalDate prevMonthLastDay = firstDayOfMonth.minusDays(1);
    	int prevMonthDaysToAdd = firstDayOfMonth.getDayOfWeek().getValue() % 7;
    	for (int i = prevMonthDaysToAdd - 1; i >= 0; i--) {
    	    daysOfFirstWeek.add(prevMonthLastDay.minusDays(i));
    	}

    	// add days of current month to first week
    	for (int i = 1; i <= 7 - prevMonthDaysToAdd; i++) {
    	    daysOfFirstWeek.add(firstDayOfMonth.plusDays(i - 1));
    	}
    	weeks.add(daysOfFirstWeek);

    	// loop over the remaining weeks of the month
    	for (int weekNumber = firstWeekNumber + 1; weekNumber <= lastWeekNumber; weekNumber++) {
    	    LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 1);
    	    LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 7);
    	    if (sundayOfWeek.getMonthValue() > month) {
    	        sundayOfWeek = lastDayOfMonth;
    	    }
    	    List<LocalDate> daysOfWeek = new ArrayList<>();
    	    for (LocalDate d = mondayOfWeek; !d.isAfter(sundayOfWeek); d = d.plusDays(1)) {
    	        daysOfWeek.add(d);
    	    }
    	    weeks.add(daysOfWeek);
    	}
    	
    	// add days from next month to the last week
    	List<LocalDate> daysOfLastWeek = weeks.get(weeks.size() - 1);
    	LocalDate nextMonthFirstDay = lastDayOfMonth.plusDays(1);
    	int nextMonthDaysToAdd = 7 - daysOfLastWeek.size();
    	for (int i = 1; i <= nextMonthDaysToAdd; i++) {
    	    daysOfLastWeek.add(nextMonthFirstDay.plusDays(i - 1));
    	}

    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
    	// print out the days of each week
    	for (int i = 0; i < weeks.size(); i++) {
    	    for(LocalDate day:weeks.get(i)) {
    	        System.out.print("Week" + (i + 1) + ": ");
    	        if (day.getMonthValue() != month) {
    	            System.out.println("[ " + day.format(formatter) + " ]");
    	        } else {
    	            System.out.println(day.format(formatter));
    	        }
    	    }
    	}

    }
}
