import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeekOfMonth {
    public static void main(String[] args) {
    	LocalDate date = LocalDate.now(); // get the current date
        int year = date.getYear();
        int month = date.getMonthValue();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        List<List<LocalDate>> weeks = new ArrayList<>();

        // loop over the weeks of the month
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        int firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
        int lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());
        for (int weekNumber = firstWeekNumber; weekNumber <= lastWeekNumber; weekNumber++) {
            LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 1);
            LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 7);
            if (sundayOfWeek.getMonthValue() > month) {
                sundayOfWeek = lastDayOfMonth;
            }
            List<LocalDate> daysOfWeek = new ArrayList<>();
            for (LocalDate d = mondayOfWeek; !d.isAfter(sundayOfWeek); d = d.plusDays(1)) {
                if(d.getMonth() == date.getMonth()) {
                	daysOfWeek.add(d);
                }else {
                	daysOfWeek.add(null);
                }
            }
            weeks.add(daysOfWeek);
        }

        // print out the days of each week
        for (int i = 0; i < weeks.size(); i++) {
            System.out.println("Week " + (i+firstWeekNumber) + ": " + weeks.get(i));
        }
    }
}
