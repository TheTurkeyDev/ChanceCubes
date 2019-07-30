package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.util.RewardsUtil;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionPart extends BasePart
{
	private StringVar id;
	private IntVar duration;
	private IntVar amplifier;

	public PotionPart(Effect effect, int duration, int amplifier)
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

	public EffectInstance getEffect()
	{
		return new EffectInstance(RewardsUtil.getPotionSafe(new ResourceLocation(id.getValue())), duration.getIntValue() * 20, amplifier.getIntValue());
	}
}