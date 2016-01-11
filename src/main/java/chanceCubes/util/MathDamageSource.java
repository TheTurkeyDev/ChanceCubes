package chanceCubes.util;

import net.minecraft.util.DamageSource;

public class MathDamageSource extends DamageSource
{
	public static MathDamageSource mathfail = new MathDamageSource();
	public MathDamageSource()
	{
		super("mathdeath");
		super.setDamageBypassesArmor();
	}

}
