package chanceCubes.proxy;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.rewards.profiles.TriggerHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{

	public boolean isClient()
	{
		return false;
	}

	public void registerGuis()
	{

	}

	public void registerRenderings()
	{

	}

	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new PlayerConnectListener());
		MinecraftForge.EVENT_BUS.register(new TickListener());
		MinecraftForge.EVENT_BUS.register(new WorldGen());
		MinecraftForge.EVENT_BUS.register(new CCubesBlocks());
		MinecraftForge.EVENT_BUS.register(new CCubesItems());
		MinecraftForge.EVENT_BUS.register(new TriggerHooks());
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}
}
