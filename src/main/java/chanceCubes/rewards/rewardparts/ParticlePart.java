package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.StringVar;

public class ParticlePart extends BasePart
{
	private StringVar particleName = new StringVar("");

	public ParticlePart(String particle)
	{
		this(new StringVar(particle));
	}

	public ParticlePart(StringVar particle)
	{
		this.particleName = particle;
	}

	public String getParticleName()
	{
		return particleName.getValue();
	}
}