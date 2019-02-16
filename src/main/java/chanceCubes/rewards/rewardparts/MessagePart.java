package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;

public class MessagePart extends BasePart
{
	private StringVar message;

	private BoolVar serverWide = new BoolVar(false);
	private IntVar range = new IntVar(32);

	public MessagePart(String message)
	{
		this(message, 0);
	}

	public MessagePart(String message, int delay)
	{
		this(new StringVar(message), new IntVar(delay));
	}

	public MessagePart(StringVar message)
	{
		this(message, new IntVar(0));
	}

	public MessagePart(StringVar message, IntVar delay)
	{
		this.message = message;
		this.setDelay(delay);
	}

	public String getMessage()
	{
		return message.getValue();
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