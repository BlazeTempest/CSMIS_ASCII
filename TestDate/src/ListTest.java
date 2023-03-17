import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTest {
	public static void main(String[] args) {
		String avoidMeatList = "";
		List<String> ids = new ArrayList<>();
		ids.add("1");
		ids.add("2");
		for(String id:ids) {
			System.out.println(id);
			if(avoidMeatList=="" || avoidMeatList.isEmpty()) {
				avoidMeatList = id;
			}else {
				avoidMeatList = avoidMeatList + "," + id;
			}
		}
		List<String> idList = Arrays.asList(avoidMeatList.split(","));
		System.out.println(avoidMeatList);
		System.out.println(idList.get(0) + idList.get(1));
	}
}
