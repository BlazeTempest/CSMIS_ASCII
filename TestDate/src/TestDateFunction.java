import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TestDateFunction {

	public static void main(String[] args) {
		List<LocalDate> disabledDates = new ArrayList<>();

        // Add dates from previous weeks of the month
        LocalDate currentDate = LocalDate.of(2023, 4, 10);
        int currentWeekOfMonth = currentDate.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth());
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth()).with(DayOfWeek.MONDAY);
        for (int i = 1; i < currentWeekOfMonth; i++) {
            LocalDate weekStart = firstDayOfMonth.plusDays((i - 1) * 7);
            LocalDate weekEnd = weekStart.plusDays(6);
            for (LocalDate date = weekStart; !date.isAfter(weekEnd); date = date.plusDays(1)) {
                disabledDates.add(date);
            }
        }

        // Add current week dates
        for (int i = 0; i < 7; i++) {
            disabledDates.add(currentDate.with(DayOfWeek.MONDAY).plusDays(i));
        }

        // Add next week dates if it is Friday
        LocalTime currentTime = LocalTime.now();
        if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY && currentTime.isAfter(LocalTime.of(13, 0))) {
            for (int i = 0; i < 7; i++) {
                disabledDates.add(currentDate.with(DayOfWeek.MONDAY).plusDays(7 + i));
            }
        }
        
        List<LocalDate> toRemove = new ArrayList<>();
        for(LocalDate temp:disabledDates) {
        	if(temp.getDayOfWeek()==DayOfWeek.SUNDAY || temp.getDayOfWeek()==DayOfWeek.SATURDAY) {
        		toRemove.add(temp);
        	}
        }
        disabledDates.removeAll(toRemove);
		
		for(LocalDate temp : disabledDates) {
		    System.out.println(temp);
		}
	}
}
