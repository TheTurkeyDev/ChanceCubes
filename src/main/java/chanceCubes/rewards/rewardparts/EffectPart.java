package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectPart extends BasePart
{
	private IntVar radius = new IntVar(1);

	private StringVar id = new StringVar("0");
	private IntVar duration = new IntVar(0);
	private IntVar amplifier = new IntVar(0);

	public EffectPart(Potion pot, int duration, int amplifier)
	{
		this(new StringVar(String.valueOf(Potion.getIdFromPotion(pot))), new IntVar(duration), new IntVar(amplifier));
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

	public PotionEffect getEffect()
	{
		Potion pot;

		String val = id.getValue();
		if(IntVar.isInteger(val))
			pot = Potion.getPotionById(Integer.parseInt(val));
		else
			pot = ForgeRegistries.POTIONS.getValue(new ResourceLocation(val));
		return new PotionEffect(pot, duration.getIntValue() * 20, amplifier.getIntValue());
	}
}
