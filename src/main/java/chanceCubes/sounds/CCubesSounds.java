package chanceCubes.sounds;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CCubesSounds
{
	public static SoundEvent D20_BREAK;
	public static SoundEvent GIANT_CUBE_SPAWN;

	public static Map<String, SoundEvent> customSounds = new HashMap<>();

	private static boolean registered = false;

	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event)
	{
		ResourceLocation res = new ResourceLocation(CCubesCore.MODID, "d20_Break");
		D20_BREAK = new SoundEvent(res).setRegistryName(res);
		event.getRegistry().register(D20_BREAK);

		res = new ResourceLocation(CCubesCore.MODID, "giant_Cube_Spawn");
		GIANT_CUBE_SPAWN = new SoundEvent(res).setRegistryName(res);
		event.getRegistry().register(D20_BREAK);

		for(String soundID : customSounds.keySet())
			event.getRegistry().register(customSounds.get(soundID));

		registered = true;
	}

	public static SoundEvent registerSound(String soundID)
	{
		SoundEvent sound;
		if(!customSounds.containsKey(soundID))
		{
			// TODO: I guess we should do something here, but idk what yet
			if(registered)
				CCubesCore.logger.log(Level.WARN, "A new sound was added after the sounds were registered and therefore the new sound could not be added!");
			ResourceLocation res = new ResourceLocation(soundID);
			sound = new SoundEvent(res).setRegistryName(res);
			customSounds.put(soundID, sound);
		}
		else
		{
			sound = customSounds.get(soundID);
		}

		return sound;
	}

}
