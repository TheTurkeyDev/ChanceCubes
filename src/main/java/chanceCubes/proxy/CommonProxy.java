package chanceCubes.proxy;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.rewards.profiles.triggerHooks.GameStageTriggerHooks;
import chanceCubes.rewards.profiles.triggerHooks.VanillaTriggerHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

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
		MinecraftForge.EVENT_BUS.register(new VanillaTriggerHooks());
		if(Loader.isModLoaded("gamestages"))
		{
			MinecraftForge.EVENT_BUS.register(new GameStageTriggerHooks());
			CCubesCore.logger.log(Level.INFO, "Loaded GameStages support!");
		}
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}
}
