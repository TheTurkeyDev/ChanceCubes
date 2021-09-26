package chanceCubes.rewards.biodomeGen;

public abstract class BaseBiome implements IBioDomeBiome
{
	private final String biomeName;

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
