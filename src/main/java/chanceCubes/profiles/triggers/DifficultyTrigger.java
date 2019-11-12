package chanceCubes.profiles.triggers;

import chanceCubes.profiles.IProfile;
import net.minecraft.world.EnumDifficulty;

public class DifficultyTrigger implements ITrigger<EnumDifficulty>
{
	private IProfile prof;
	private EnumDifficulty diff;

	public DifficultyTrigger(IProfile prof, EnumDifficulty diff)
	{
		this.prof = prof;
		this.diff = diff;
	}

	@Override
	public void onTrigger(String playerUUID, EnumDifficulty[] args)
	{
		if(args.length == 2)
		{
			if(args[0].equals(diff))
				prof.setTriggerState(this, playerUUID, true);
			else if(args[1].equals(diff))
				prof.setTriggerState(this, playerUUID, false);
		}
	}

	@Override
	public String getTriggerDesc()
	{
		return "Trigger on game difficulty change";
	}
}
