package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.util.RewardsUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class PotionPart extends BasePart
{
	private final StringVar id;
	private final IntVar duration;
	private final IntVar amplifier;

	public PotionPart(MobEffect effect, int duration, int amplifier)
	{
		this(new StringVar(effect.getRegistryName().toString()), new IntVar(duration), new IntVar(amplifier));
	}

	public PotionPart(String id, int duration, int amplifier)
	{
		this(new StringVar(id), new IntVar(duration), new IntVar(amplifier));
	}

	public PotionPart(StringVar id, IntVar duration, IntVar amplifier)
	{
		this.id = id;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public MobEffectInstance getEffect()
	{
		return new MobEffectInstance(RewardsUtil.getPotionSafe(new ResourceLocation(id.getValue())), duration.getIntValue() * 20, amplifier.getIntValue());
	}
}