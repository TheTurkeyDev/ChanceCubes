package chanceCubes.sounds;

import chanceCubes.CCubesCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

//@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, CCubesCore.MODID);

	public static DeferredHolder<SoundEvent, SoundEvent> D20_BREAK = SOUNDS.register("d20_break", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CCubesCore.MODID, "d20_break")));
	public static DeferredHolder<SoundEvent, SoundEvent> GIANT_CUBE_SPAWN = SOUNDS.register("giant_cube_spawn", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CCubesCore.MODID, "giant_cube_spawn")));

	public static final Map<String, SoundEvent> customSounds = new HashMap<>();

//	private static boolean registered = true;
//
//	public static SoundEvent registerSound(String soundID)
//	{
//		SoundEvent sound;
//		if(!customSounds.containsKey(soundID))
//		{
//			// TODO: I guess we should do something here, but idk what yet
//			if(registered)
//				CCubesCore.logger.log(Level.WARN, "A new sound was added after the sounds were registered and therefore the new sound could not be added!");
//			ResourceLocation res = new ResourceLocation("minecraft", soundID);
//			sound = new SoundEvent(res).setRegistryName(res);
//			//ForgeRegistries.SOUND_EVENTS.register(sound);
//			customSounds.put(soundID, sound);
//		}
//		else
//		{
//			sound = customSounds.get(soundID);
//		}
//
//		return sound;
//	}
}
