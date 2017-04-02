package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vidyanand on 15/8/15.
 */
public class RequestTask {

	/**
	 * Making HTTP Connection and reading the places
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public String readPlaces(String httpUrl) throws IOException {

		String httpData = "";
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;

		try {
			URL url = new URL(httpUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.connect();

			inputStream = httpURLConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer stringBuffer = new StringBuffer();
			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}

			httpData = stringBuffer.toString();
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inputStream.close();
			httpURLConnection.disconnect();
		}
		return httpData;
	}
}
