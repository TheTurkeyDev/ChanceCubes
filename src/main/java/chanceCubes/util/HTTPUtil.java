package chanceCubes.util;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class HTTPUtil
{
	private static JsonParser json = new JsonParser();

	public static JsonElement getWebFile(String link) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(link).openConnection();
		con.setDoOutput(false);
		con.setReadTimeout(20000);
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
		((HttpURLConnection) con).setRequestMethod("GET");
		con.setConnectTimeout(5000);
		
		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
		int responseCode = con.getResponseCode();
		
		if(responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
			CCubesCore.logger.log(Level.WARN, "Update request returned response code: " + responseCode + " " + con.getResponseMessage());
		else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM)
			throw new Exception();

		StringBuffer buffer = new StringBuffer();
		int chars_read;
		while((chars_read = in.read()) != -1)
			buffer.append((char) chars_read);
		
		String page = buffer.toString();

		return json.parse(page);
	}
}
