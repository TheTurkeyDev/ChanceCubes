package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;

public class ParticlePart extends BasePart
{
	private IntVar particleID = new IntVar(-1);
	private StringVar particleName = new StringVar("");

	public ParticlePart(int particle)
	{
		this(new IntVar(particle));
	}

	public ParticlePart(IntVar particle)
	{
		this.particleID = particle;
	}

	public ParticlePart(String particle)
	{
		this(new StringVar(particle));
	}

	public ParticlePart(StringVar particle)
	{
		this.particleName = particle;
	}

	public boolean useID()
	{
		return this.particleID.getIntValue() != -1;
	}

	public int getParticleID()
	{
		return particleID.getIntValue();
	}

	public String getParticleName()
	{
		return particleName.getValue();
	}
}