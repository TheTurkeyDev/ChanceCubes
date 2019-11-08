package chanceCubes.profiles.triggers;

import chanceCubes.profiles.IProfile;

public class DimensionChangeTrigger implements ITrigger<Integer>
{
	private IProfile prof;
	private Integer dimID;

	public DimensionChangeTrigger(IProfile prof, Integer dimID)
	{
		this.prof = prof;
		this.dimID = dimID;
	}

	@Override
	public void onTrigger(Integer[] args)
	{
		if(args.length == 2)
		{
			if(args[0].equals(dimID))
				prof.setTriggerState(this, true);
			else if(args[1].equals(dimID))
				prof.setTriggerState(this, false);
		}
	}

	@Override
	public String getTriggerDesc()
	{
		return "Trigger on dimension change";
	}
}
