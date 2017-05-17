package chanceCubes.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CCubesCommandSender implements ICommandSender
{
	private EntityPlayer harvester;
	private BlockPos blockLoc;

	public CCubesCommandSender(EntityPlayer player, BlockPos pos)
	{
		blockLoc = pos;
		harvester = player;
	}

	@Override
	public void addChatMessage(ITextComponent p_145747_1_)
	{

	}

	@Override
	public World getEntityWorld()
	{
		return harvester != null ? harvester.worldObj : null;
	}

	@SideOnly(Side.CLIENT)
	public int func_145751_f()
	{
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public void func_145757_a(ByteBuf p_145757_1_)
	{
	}

	@Override
	public BlockPos getPosition()
	{
		return blockLoc;
	}

	@Override
	public Vec3d getPositionVector()
	{
		return new Vec3d(blockLoc.getX(), blockLoc.getY(), blockLoc.getZ());
	}

	@Override
	public Entity getCommandSenderEntity()
	{
		return harvester;
	}

	@Override
	public MinecraftServer getServer()
	{
		return harvester.getEntityWorld().getMinecraftServer();
	}

	@Override
	public String getName()
	{
		return "Chance Cubes";
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(this.getName());
	}

	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName)
	{
		return true;
	}

	@Override
	public boolean sendCommandFeedback()
	{
		return false;
	}

	@Override
	public void setCommandStat(Type type, int amount)
	{

	}
}