package chanceCubes.profiles.triggerHooks;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.AdvancementTrigger;
import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class VanillaTriggerHooks
{
	//TODO: Crafting trigger

	@SubscribeEvent
	public void onDifficultyChange(DifficultyChangeEvent event)
	{
		if(EffectiveSide.get() == LogicalSide.CLIENT)
			return;

		for(PlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
		{
			for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player.getUniqueID().toString()).getAllProfiles())
			{
				for(ITrigger<?> module : prof.getTriggers())
				{
					if(module instanceof DifficultyTrigger)
					{
						DifficultyTrigger trigger = (DifficultyTrigger) module;
						trigger.onTrigger(player.getUniqueID().toString(), new Difficulty[]{event.getDifficulty(), event.getOldDifficulty()});
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event)
	{

		if(EffectiveSide.get() == LogicalSide.CLIENT)
			return;
		PlayerEntity player = event.getPlayer();
		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof DimensionChangeTrigger)
				{
					DimensionChangeTrigger trigger = (DimensionChangeTrigger) module;
					trigger.onTrigger(player.getUniqueID().toString(), new Integer[]{event.getTo().getId(), event.getFrom().getId()});
				}
			}
		}
	}

	@SubscribeEvent
	public void onAdvancementComplete(AdvancementEvent event)
	{
		if(EffectiveSide.get() == LogicalSide.CLIENT)
			return;
		PlayerEntity player = event.getPlayer();
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
