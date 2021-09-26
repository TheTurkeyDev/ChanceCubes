package chanceCubes.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class RewardInfoActionArgument implements ArgumentType<InfoAction>
{
	@Override
	public InfoAction parse(StringReader reader)
	{
		switch(reader.getString().toLowerCase())
		{
			case "default":
				return InfoAction.DEFAULT;
			case "default_all":
				return InfoAction.DEFAULT_ALL;
			case "default_disabled":
				return InfoAction.DEFAULT_DISABLED;
			case "giant":
				return InfoAction.GIANT;
			case "giant_all":
				return InfoAction.GIANT_ALL;
			case "giant_disabled":
				return InfoAction.GIANT_DISABLED;
		}
		return InfoAction.NONE;
	}

	public static InfoAction func_212592_a(CommandContext<CommandSourceStack> p_212592_0_, String p_212592_1_)
	{
		return p_212592_0_.getArgument(p_212592_1_, InfoAction.class);
	}
}

enum InfoAction
{
	NONE,
	DEFAULT,
	GIANT,
	DEFAULT_ALL,
	GIANT_ALL,
	DEFAULT_DISABLED,
	GIANT_DISABLED
}