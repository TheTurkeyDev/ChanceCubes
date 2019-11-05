package chanceCubes.profiles.triggers;

import chanceCubes.profiles.IProfile;

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
			prof.setTriggerState(this, true);
	}

	@Override
	public String getTriggerDesc()
	{
		return "Trigger on player advancement complete";
	}
}
