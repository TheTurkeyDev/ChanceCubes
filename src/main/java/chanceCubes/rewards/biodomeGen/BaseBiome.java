package chanceCubes.rewards.biodomeGen;

public abstract class BaseBiome implements IBioDomeBiome
{
	private String biomeName = "undefined";

	public BaseBiome(String name)
	{
		this.biomeName = name;
	}

	@Override
	public String getBiomeName()
	{
		return this.biomeName;
	}
}
