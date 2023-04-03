import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AvoidmeatTest {

	public static void main(String[] args) {
			String meats = "1,2,3";
			int removeId = 1;
			List<String> ids = new ArrayList<>(Arrays.asList(meats.split(",")));
			List<String> toRemove = new ArrayList<>();
			for(String id : ids) {
				if(id.equals(String.valueOf(removeId))) {
					System.out.println(removeId);
					toRemove.add(String.valueOf(removeId));
				}
			}
			ids.removeAll(toRemove);
			meats = ids.toString();
			System.out.println(meats);
		}
}
