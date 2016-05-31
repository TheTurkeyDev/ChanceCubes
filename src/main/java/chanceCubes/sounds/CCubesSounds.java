package chanceCubes.sounds;

import chanceCubes.CCubesCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public enum CCubesSounds
{
	D20_BREAK(SoundCategory.BLOCKS, "d20_Break"), GIANT_CUBE_SPAWN(SoundCategory.BLOCKS, "giant_Cube_Spawn");

	private ResourceLocation resourceLocation;
	private SoundCategory soundCategory;
	private SoundEvent soundEvent = null;

	private CCubesSounds(SoundCategory soundCategory, String name)
	{
		this.soundCategory = soundCategory;
		this.resourceLocation = new ResourceLocation(CCubesCore.MODID, name);
	}

	public static void loadSounds()
	{
		for(CCubesSounds sound : values())
			sound.soundEvent = new SoundEvent(sound.resourceLocation);
	}
	
	public static SoundEvent registerSound(String name)
	{
		return new SoundEvent(new ResourceLocation(CCubesCore.MODID, name));
	}

	public SoundEvent getSoundEvent()
	{
		return soundEvent;
	}

	public SoundCategory getSoundCategory()
	{
		return soundCategory;
	}

}