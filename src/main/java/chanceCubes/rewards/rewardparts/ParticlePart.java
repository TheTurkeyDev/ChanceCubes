package chanceCubes.rewards.rewardparts;

public class ParticlePart
{
	public static String[] elements = new String[] { "particle:I", "x:I", "y:I", "z:I", "delay:I" };

	private int particleID = -1;
	private String particleName = "";

	private int delay = 0;

	public ParticlePart(int particle)
	{
		this.particleID = particle;
	}

	public ParticlePart(String particle)
	{
		this.particleName = particle;
	}

	public boolean useID()
	{
		return this.particleID != -1;
	}

	public int getParticleID()
	{
		return particleID;
	}

	public String getParticleName()
	{
		return particleName;
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}
}