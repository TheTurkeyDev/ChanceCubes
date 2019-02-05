package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionPart extends BasePart
{
	private IntVar id = new IntVar(0);
	private IntVar duration = new IntVar(0);
	private IntVar amplifier = new IntVar(0);

	public PotionPart(Potion pot, int duration, int amplifier)
	{
		this(new IntVar(Potion.getIdFromPotion(pot)), new IntVar(duration), new IntVar(amplifier));
	}

	public PotionPart(int id, int duration, int amplifier)
	{
		this(new IntVar(id), new IntVar(duration), new IntVar(amplifier));
	}

	public PotionPart(IntVar id, IntVar duration, IntVar amplifier)
	{
		this.id = id;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public PotionEffect getEffect()
	{
		return new PotionEffect(Potion.getPotionById(id.getIntValue()), duration.getIntValue() * 20, amplifier.getIntValue());
	}
}