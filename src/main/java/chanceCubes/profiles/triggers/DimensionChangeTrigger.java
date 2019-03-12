package chanceCubes.profiles.triggers;

import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;

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
			if(args[0] == dimID)
				ProfileManager.enableProfile(prof);
			else if(args[1] == dimID)
				ProfileManager.disableProfile(prof);
		}
	}
}
