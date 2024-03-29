package chanceCubes.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class RewardArgument implements ArgumentType<String>
{
	public static RewardArgument rewardArgument() {
		return new RewardArgument();
	}

	@Override
	public String parse(StringReader reader)
	{
		int i = reader.getCursor();

		while(reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}

		return reader.getString().substring(i, reader.getCursor());
	}

	public static String getReward(CommandContext<CommandSourceStack> context, String name) {
		return context.getArgument(name, String.class);
	}
}