package chanceCubes.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;

public class RewardArgument implements ArgumentType<String>
{
	//TODO: Make "smarter" and aid the player in auto completing the reward names
	@Override
	public String parse(StringReader reader)
	{
		return reader.getString();
	}

	public static String func_212592_a(CommandContext<CommandSource> p_212592_0_, String p_212592_1_)
	{
		return p_212592_0_.getArgument(p_212592_1_, String.class);
	}
}
