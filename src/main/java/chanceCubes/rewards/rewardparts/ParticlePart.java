package chanceCubes.rewards.rewardparts;

public class ParticlePart
{
	public static String[] elements = new String[] { "particle:I", "x:I", "y:I", "z:I", "delay:I" };

	private int particle;

	private int delay = 0;

	public ParticlePart(int particle)
	{
		this.particle = particle;
	}

	public int getParticle()
	{
		return particle;
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