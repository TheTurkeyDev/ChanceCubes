package chanceCubes.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
	public static JsonElement getWebFile(String type, String link, CustomEntry<String, String>... extras) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(link).openConnection();
		con.setDoInput(true);
		con.setReadTimeout(5000);
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
		con.setRequestMethod(type);

		if(!type.equals("GET"))
		{
			con.setDoOutput(true);
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
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		int responseCode = con.getResponseCode();

		if(responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
			CCubesCore.logger.log(Level.WARN, "Update request returned response code: " + responseCode + " " + con.getResponseMessage());
		else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM)
			throw new Exception();

		StringBuilder buffer = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null)
			buffer.append(line);

		String page = buffer.toString();

		return json.parse(page);
	}
}