import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TreeDataReader {
	
	private TreeDataReader() {
		
	}

	public static List<Long> readFromFile(String operation, String dataSet, String file) throws IOException {
		List<Long> itemList = new ArrayList<>();
		String filePath = "/Users/lakindu/MSC/Semester 3/CS5701 - Advanced Algorithms/Assignment 01/data/".concat(operation).concat("/").concat(dataSet).concat("/").concat(file)
				.concat(".txt");
		String content = new String(Files.readAllBytes(Paths.get(filePath)));

		
		for(String data:content.trim().split(",")) {
			itemList.add(Long.valueOf(data.trim()));
		}
		
		return itemList;
	}
}
