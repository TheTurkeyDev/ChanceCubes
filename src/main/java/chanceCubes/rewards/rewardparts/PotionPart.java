package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionPart extends BasePart
{
	private StringVar id = new StringVar("0");
	private IntVar duration = new IntVar(0);
	private IntVar amplifier = new IntVar(0);

	public PotionPart(Potion pot, int duration, int amplifier)
	{
		this(new StringVar(String.valueOf(Potion.getIdFromPotion(pot))), new IntVar(duration), new IntVar(amplifier));
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