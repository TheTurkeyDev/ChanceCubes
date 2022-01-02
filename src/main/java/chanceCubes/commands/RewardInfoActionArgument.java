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
		return switch(reader.getString().toLowerCase())
				{
					case "default" -> InfoAction.DEFAULT;
					case "default_all" -> InfoAction.DEFAULT_ALL;
					case "default_disabled" -> InfoAction.DEFAULT_DISABLED;
					case "giant" -> InfoAction.GIANT;
					case "giant_all" -> InfoAction.GIANT_ALL;
					case "giant_disabled" -> InfoAction.GIANT_DISABLED;
					default -> InfoAction.NONE;
				};
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