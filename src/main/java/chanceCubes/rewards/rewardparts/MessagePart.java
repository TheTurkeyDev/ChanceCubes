package chanceCubes.rewards.rewardparts;

public class MessagePart
{
	public static String[] elements = new String[]{"message", "delay", "serverWide", "range"};
	
	private String message;
	
	private int delay = 0;
	
	private boolean serverWide = false;
	private int range = 32;
	
	public MessagePart(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public int getDelay()
	{
		return delay;
	}

	public MessagePart setDelay(int delay)
	{
		this.delay = delay;
		return this;
	}

	public boolean isServerWide()
	{
		return serverWide;
	}

	public MessagePart setServerWide(boolean serverWide)
	{
		this.serverWide = serverWide;
		return this;
	}

	public int getRange()
	{
		return range;
	}

	public MessagePart setRange(int range)
	{
		this.range = range;
		return this;
	}
}