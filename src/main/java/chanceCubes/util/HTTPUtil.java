package chanceCubes.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import chanceCubes.CCubesCore;

public class HTTPUtil
{
	private static JsonParser json = new JsonParser();

	@SafeVarargs
	public static JsonElement getWebFile(String link, CustomEntry<String, String>... extras) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(link).openConnection();
		con.setDoOutput(true);
		con.setReadTimeout(5000);
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
		((HttpURLConnection) con).setRequestMethod("POST");
		con.setConnectTimeout(5000);

		StringBuilder builder = new StringBuilder();

		for(CustomEntry<String, String> property : extras)
		{
			builder.append(property.getKey());
			builder.append("=");
			builder.append(property.getValue());
			builder.append("&");
		}

		if(builder.length() > 0)
			builder.deleteCharAt(builder.length() - 1);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(builder.toString());
		wr.flush();
		wr.close();

		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
		int responseCode = con.getResponseCode();

		if(responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
			CCubesCore.logger.log(Level.WARN, "Update request returned response code: " + responseCode + " " + con.getResponseMessage());
		else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM)
			throw new Exception();

		StringBuilder buffer = new StringBuilder();
		int chars_read;
		while((chars_read = in.read()) != -1)
			buffer.append((char) chars_read);

		String page = buffer.toString();

		return json.parse(page);
	}
}