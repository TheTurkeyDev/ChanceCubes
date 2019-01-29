package chanceCubes.util;

import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class CCubesCommandSender implements ICommandSource
{
	private EntityPlayer harvester;
	private BlockPos blockLoc;

	public CCubesCommandSender(EntityPlayer player, BlockPos pos)
	{
		blockLoc = pos;
		harvester = player;
	}

	@Override
	public void sendMessage(ITextComponent p_145747_1_)
	{

	}

	@Override
	public boolean shouldReceiveFeedback()
	{
		return false;
	}

	@Override
	public boolean shouldReceiveErrors()
	{
		return false;
	}

	@Override
	public boolean allowLogging()
	{
		return false;
	}
}