import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Tophub {

	private String baseurl = "https://api.tophubdata.com";
	private String apikey = "";

	public Tophub(String apikey) {
		this.apikey = apikey;
	}

	public Map<String, Object> nodes(int p) {
		return call("/nodes", Map.of("p", p));
	}

	public Map<String, Object> node(String hashid) {
		return call("/nodes/" + hashid, new HashMap<>());
	}

	public Map<String, Object> nodeHistorys(String hashid, String date) {
		return call("/nodes/" + hashid + "/historys", Map.of("date", date));
	}

	public Map<String, Object> search(String q, int p, String hashid) {
		return call("/search", Map.of("q", q, "p", p, "hashid", hashid));
	}

	public Map<String, Object> call(String endpoint, Map<String, Object> params) {
		try {
			String url = baseurl + endpoint;
			if (!params.isEmpty()) {
				url += "?" + getQueryString(params);
			}

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", apikey);

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			reader.close();
			connection.disconnect();

			// Assuming the response is in JSON format
			// You may need to adjust this based on the actual response format
			// For simplicity, I'm returning a Map<String, Object>
			// You can create appropriate classes to represent the response structure
			return parseJson(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getQueryString(Map<String, Object> params) {
		StringBuilder queryString = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (queryString.length() > 0) {
				queryString.append("&");
			}
			queryString.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return queryString.toString();
	}

	// You may use a JSON parsing library for a more robust solution
	private Map<String, Object> parseJson(String json) {
		// Implement your JSON parsing logic here
		// For simplicity, I'm returning null
		return null;
	}

	public static void main(String[] args) {
		// Example usage
		String apikey = "";
		Tophub tophub = new Tophub(apikey);
		Map<String, Object> resultNodes = tophub.nodes(1);
		Map<String, Object> resultNode = tophub.node("mproPpoq6O");
		Map<String, Object> resultNodeHistorys = tophub.nodeHistorys("mproPpoq6O", "2023-01-01");
		Map<String, Object> resultSearch = tophub.search("苹果", 1, "mproPpoq6O");
	}
}
