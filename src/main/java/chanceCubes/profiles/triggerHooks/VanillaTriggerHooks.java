package chanceCubes.profiles.triggerHooks;

import chanceCubes.CCubesCore;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.AdvancementTrigger;
import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

public class VanillaTriggerHooks
{
	//TODO: Crafting trigger

	@SubscribeEvent
	public void onDifficultyChange(DifficultyChangeEvent event)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			return;
		for(EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
		{
			for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player.getUniqueID().toString()).getAllProfiles())
			{
				for(ITrigger<?> module : prof.getTriggers())
				{
					if(module instanceof DifficultyTrigger)
					{
						DifficultyTrigger trigger = (DifficultyTrigger) module;
						trigger.onTrigger(player.getUniqueID().toString(), new EnumDifficulty[]{event.getDifficulty(), event.getOldDifficulty()});
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent event)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			return;
		EntityPlayer player = event.player;
		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof DimensionChangeTrigger)
				{
					DimensionChangeTrigger trigger = (DimensionChangeTrigger) module;
					trigger.onTrigger(player.getUniqueID().toString(), new Integer[]{event.toDim, event.fromDim});
				}
			}
		}
	}

	@SubscribeEvent
	public void onAdvancementComplete(AdvancementEvent event)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			return;
		EntityPlayer player = event.getEntityPlayer();
		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof AdvancementTrigger)
				{
					AdvancementTrigger trigger = (AdvancementTrigger) module;
					trigger.onTrigger(player.getUniqueID().toString(), new String[]{event.getAdvancement().getId().toString()});
				}
			}
		}
	}
}
