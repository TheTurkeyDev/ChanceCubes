package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;

public class MessagePart
{
	private StringVar message;

	private IntVar delay = new IntVar(0);

	private BoolVar serverWide = new BoolVar(false);
	private IntVar range = new IntVar(32);

	public MessagePart(String message)
	{
		this(new StringVar(message));
	}

	public MessagePart(StringVar message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message.getValue();
	}

	public int getDelay()
	{
		return delay.getIntValue();
	}

	public MessagePart setDelay(int delay)
	{
		return this.setDelay(new IntVar(delay));
	}

	public MessagePart setDelay(IntVar delay)
	{
		this.delay = delay;
		return this;
	}

	public boolean isServerWide()
	{
		return serverWide.getBoolValue();
	}

	public MessagePart setServerWide(boolean serverWide)
	{
		return this.setServerWide(new BoolVar(serverWide));
	}

	public MessagePart setServerWide(BoolVar serverWide)
	{
		this.serverWide = serverWide;
		return this;
	}

	public int getRange()
	{
		return range.getIntValue();
	}

	public MessagePart setRange(int range)
	{
		return this.setRange(new IntVar(range));
	}

	public MessagePart setRange(IntVar range)
	{
		this.range = range;
		return this;
	}
}