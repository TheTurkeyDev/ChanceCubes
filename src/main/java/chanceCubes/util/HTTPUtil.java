package chanceCubes.util;

import chanceCubes.CCubesCore;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

		int responseCode = con.getResponseCode();

		if(responseCode != HttpURLConnection.HTTP_OK)
		{
			CCubesCore.logger.log(Level.ERROR, "Update request returned response code: " + responseCode + " " + con.getResponseMessage());
			return JsonNull.INSTANCE;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder buffer = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null)
			buffer.append(line);

		con.disconnect();

		String page = buffer.toString();

		return json.parse(page);
	}
}