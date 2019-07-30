package chanceCubes.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;

public class RewardData
{
	public static JsonElement getVillageSchematic()
	{
		return FileUtil.JSON_PARSER.parse(getSchematic("/assets/chancecubes/schematics/village.ccs"));
	}

	public static JsonElement getArrowTrapSchematic()
	{
		return FileUtil.JSON_PARSER.parse(getSchematic("/assets/chancecubes/schematics/arrow_trap.ccs"));
	}

	public static JsonElement getTrampolineSchematic()
	{
		return FileUtil.JSON_PARSER.parse(getSchematic("/assets/chancecubes/schematics/trampoline.ccs"));
	}

	public static JsonElement getWoodlandMansionSchematic()
	{
		return FileUtil.JSON_PARSER.parse(getSchematic("/assets/chancecubes/schematics/mansion.ccs"));
	}

	private static String getSchematic(String path)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(RewardData.class.getResourceAsStream(path)));
		StringBuilder builder = new StringBuilder();
		try
		{
			String line;
			while((line = in.readLine()) != null)
				builder.append(line);

			in.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return builder.toString();
	}
}