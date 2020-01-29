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
				ProfileManager.enableProfile(prof);
			else if(args[1].equals(dimID))
				ProfileManager.disableProfile(prof);
		}
	}

	@Override
	public String getTriggerDesc()
	{
		return "Trigger on dimension change";
	}
}
