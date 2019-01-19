package chanceCubes.rewards.variableParts;

public class StringPart implements IPart
{

	private String val;

	public StringPart(String val)
	{
		this.val = val;
	}
	
	public StringPart(boolean val)
	{
		this.val = String.valueOf(val);
	}
	
	public StringPart(float val)
	{
		this.val = String.valueOf(val);
	}
	
	public StringPart(int val)
	{
		this.val = String.valueOf(val);
	}

	@Override
	public String getValue()
	{
		return val;
	}

}
