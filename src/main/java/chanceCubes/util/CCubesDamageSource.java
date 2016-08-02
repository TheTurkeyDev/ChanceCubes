package chanceCubes.util;

import net.minecraft.util.DamageSource;

public class CCubesDamageSource extends DamageSource
{
	public static CCubesDamageSource mathfail = new CCubesDamageSource("mathdeath");
	public static CCubesDamageSource mazefail = new CCubesDamageSource("mazedeath");
	public static CCubesDamageSource questionfail = new CCubesDamageSource("questiondeath");
	
	public CCubesDamageSource(String id)
	{
		super(id);
		super.setDamageBypassesArmor();
	}

}