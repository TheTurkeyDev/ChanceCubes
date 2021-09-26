package chanceCubes.rewards.rewardparts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectPart extends BasePart
{
	private IntVar radius = new IntVar(1);

	private final StringVar id;
	private final IntVar duration;
	private final IntVar amplifier;

	public EffectPart(MobEffect effect, int duration, int amplifier)
	{
		this(effect, new IntVar(duration), new IntVar(amplifier));
	}

	public EffectPart(MobEffect effect, IntVar duration, IntVar amplifier)
	{
		this(new StringVar(String.valueOf(ForgeRegistries.MOB_EFFECTS.getKey(effect))), duration, amplifier);
	}

	public EffectPart(String id, int duration, int amplifier)
	{
		this(new StringVar(id), new IntVar(duration), new IntVar(amplifier));
	}

	public EffectPart(StringVar id, IntVar duration, IntVar amplifier)
	{
		this.id = id;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public IntVar getRadius()
	{
		return radius;
	}

	public EffectPart setRadius(int radius)
	{
		return this.setRadius(new IntVar(radius));
	}

	public EffectPart setRadius(IntVar radius)
	{
		this.radius = radius;
		return this;
	}

	public MobEffectInstance getEffect()
	{
		MobEffect pot;

		String val = id.getValue();
		if(IntVar.isInteger(val))
			pot = MobEffect.byId(Integer.parseInt(val));
		else
			pot = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(val));

		if(pot == null)
		{
			pot = MobEffects.BLINDNESS;
			CCubesCore.logger.log(Level.ERROR, "The Potion Effect with the id of " + val + " does not exist! Falling back to default to avoid crash!");
		}

		return new MobEffectInstance(pot, duration.getIntValue() * 20, amplifier.getIntValue());
	}
}