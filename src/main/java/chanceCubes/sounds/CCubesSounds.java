package chanceCubes.sounds;

import chanceCubes.CCubesCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CCubesCore.MODID);

	public static RegistryObject<SoundEvent> D20_BREAK = SOUNDS.register("d20_break", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CCubesCore.MODID, "d20_break")));
	public static RegistryObject<SoundEvent> GIANT_CUBE_SPAWN = SOUNDS.register("giant_cube_spawn", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CCubesCore.MODID, "giant_cube_spawn")));

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
