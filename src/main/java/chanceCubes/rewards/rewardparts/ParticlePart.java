package chanceCubes.rewards.rewardparts;

public class ParticlePart
{
	public static String[] elements = new String[] { "particle:I", "delay:I" };

	private int particleID = -1;
	private String particleName = "";

	private int delay = 0;

	public ParticlePart(int particleID)
	{
		this.particleID = particleID;
	}

	public ParticlePart(String particleName)
	{
		this.particleName = particleName;
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