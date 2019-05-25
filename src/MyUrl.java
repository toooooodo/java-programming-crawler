import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MyUrl {
	private String[] array;
	private String content;
	private final String baseUrl = "https://dxy.com/disease/";

	public MyUrl(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		InputStream inputStream = null;
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// System.out.println(EntityUtils.toString(entity, "utf-8"));
				content = EntityUtils.toString(entity, "utf-8");
				array = content.split("\"id\":");

			}
			EntityUtils.consume(entity);
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public String[] getUrl() {
		int i = 0;
		for (String sub : array) {
			int index = sub.indexOf(',');
			 //System.out.println(sub.substring(0, index));
			array[i++] = baseUrl + sub.substring(0, index);
		}
		return array;
	}
}
