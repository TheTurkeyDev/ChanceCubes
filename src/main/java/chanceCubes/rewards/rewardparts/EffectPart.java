package chanceCubes.rewards.rewardparts;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectPart extends BasePart
{
	private IntVar radius = new IntVar(1);

	private StringVar id = new StringVar("0");
	private IntVar duration = new IntVar(0);
	private IntVar amplifier = new IntVar(0);

	public EffectPart(Effect effect, int duration, int amplifier)
	{
		this(effect, new IntVar(duration), new IntVar(amplifier));
	}

	public EffectPart(Effect effect, IntVar duration, IntVar amplifier)
	{
		this(new StringVar(String.valueOf(ForgeRegistries.POTIONS.getKey(effect))), duration, amplifier);
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

	public EffectInstance getEffect()
	{
		Effect pot;

		String val = id.getValue();
		if(IntVar.isInteger(val))
			pot = Effect.get(Integer.parseInt(val));
		else
			pot = ForgeRegistries.POTIONS.getValue(new ResourceLocation(val));

		if(pot == null)
		{
			pot = Effects.BLINDNESS;
			CCubesCore.logger.log(Level.ERROR, "The Potion Effect with the id of " + val + " does not exist! Falling back to default to avoid crash!");
		}

		return new EffectInstance(pot, duration.getIntValue() * 20, amplifier.getIntValue());
	}
}
