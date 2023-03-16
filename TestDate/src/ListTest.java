import java.util.ArrayList;
import java.util.List;

public class ListTest {
	public static void main(String[] args) {
		List<Boolean> dates = new ArrayList<>();
		dates.add(null);
		dates.add(null);
		dates.add(null);
		dates.add(null);
		dates.add(true);
		dates.add(true);
		dates.add(false);
		for(Boolean b : dates) {
			System.out.println(b);
		}
	}
}
