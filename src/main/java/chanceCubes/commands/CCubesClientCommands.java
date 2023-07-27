package chanceCubes.commands;

import chanceCubes.client.ClientHelper;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class CCubesClientCommands
{
	public CCubesClientCommands(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		// @formatter:off
		dispatcher.register(Commands.literal("chancecubes")
				.requires(cs -> cs.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(
						Commands.literal("schematic").then(
								Commands.literal("create").executes(this::executeSchematicCreate)
						).then(
								Commands.literal("cancel").executes(this::executeSchematicCancel)
						)
				)
		);
		// @formatter:on
	}

	public int executeSchematicCreate(CommandContext<CommandSourceStack> ctx)
	{
		if(RenderEvent.isCreatingSchematic())
		{
			//Possibly make own packet
			if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
			{
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
				{
					ClientHelper.openSchematicCreatorGUI(Minecraft.getInstance().player);
				});
			}
			else
			{
				RewardsUtil.sendMessageToPlayer(Minecraft.getInstance().player, "Please set both points before moving on!");
			}
		}
		else
		{
			RenderEvent.setCreatingSchematic(true);
		}
		return 0;
	}

	public int executeSchematicCancel(CommandContext<CommandSourceStack> ctx)
	{
		RenderEvent.setCreatingSchematic(false);
		SchematicUtil.selectionPoints[0] = null;
		SchematicUtil.selectionPoints[1] = null;
		return 0;
	}
}