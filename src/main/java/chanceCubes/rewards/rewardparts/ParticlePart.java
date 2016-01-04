package chanceCubes.rewards.rewardparts;


public class ParticlePart
{
	public static String[] elements = new String[]{"particle:S", "x:I", "y:I", "z:I", "delay:I"};
	
	private String particle;
	
	private int delay = 0;

	public ParticlePart(String particle)
	{
		this.particle = particle;
	}

	public String getParticle()
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