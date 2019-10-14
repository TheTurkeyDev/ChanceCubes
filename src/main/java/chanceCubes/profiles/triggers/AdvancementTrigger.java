package chanceCubes.profiles.triggers;

import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;

public class AdvancementTrigger implements ITrigger<String>
{
	private IProfile prof;
	private String advancement;

	public AdvancementTrigger(IProfile prof, String advancement)
	{
		this.prof = prof;
		this.advancement = advancement;
	}

	@Override
	public void onTrigger(String[] args)
	{
		if(args.length == 1 && args[0].equals(advancement))
			ProfileManager.enableProfile(prof);
	}

	@Override
	public String getTriggerDesc()
	{
		return "Trigger on player advancement complete";
	}
}
