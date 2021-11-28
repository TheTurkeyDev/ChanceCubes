package chanceCubes.mcwrapper;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

public class JsonWrapper
{
	public static CompoundTag getNBTFromJson(JsonElement json)
	{
		return getNBTFromJson(json.getAsString());
	}

	public static CompoundTag getNBTFromJson(String json)
	{
		try
		{
			return TagParser.parseTag(json);
		} catch(CommandSyntaxException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
