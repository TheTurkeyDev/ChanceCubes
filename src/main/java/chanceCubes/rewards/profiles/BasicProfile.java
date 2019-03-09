package chanceCubes.rewards.profiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.profiles.triggers.ITrigger;

public class BasicProfile implements IProfile
{
	private String id;
	private String name;
	private String desc;
	private StringBuilder descFull = new StringBuilder();
	private List<ITrigger<?>> triggers = new ArrayList<>();
	private List<String> rewardsToEnable = new ArrayList<>();
	private List<String> rewardsToDisable = new ArrayList<>();
	private List<IProfile> subProfiles = new ArrayList<>();

	public BasicProfile(String id, String name, String desc)
	{
		this.id = id;
		this.name = name;
		this.desc = desc;
	}

	public BasicProfile addEnabledRewards(String... rewards)
	{
		this.rewardsToEnable.addAll(Arrays.asList(rewards));
		return this;
	}

	public BasicProfile addDisabledRewards(String... rewards)
	{
		this.rewardsToDisable.addAll(Arrays.asList(rewards));
		return this;
	}

	public BasicProfile addTriggers(ITrigger<?>... triggers)
	{
		this.triggers.addAll(Arrays.asList(triggers));
		return this;
	}

	public BasicProfile addSubProfile(IProfile... profiles)
	{
		this.subProfiles.addAll(Arrays.asList(profiles));
		return this;
	}

	@Override
	public void onEnable()
	{
		for(String s : this.rewardsToDisable)
			if(!ChanceCubeRegistry.INSTANCE.disableReward(s))
				CCubesCore.logger.log(Level.ERROR, name + " failed to disable reward " + s);
		for(String s : this.rewardsToEnable)
			if(!ChanceCubeRegistry.INSTANCE.enableReward(s))
				CCubesCore.logger.log(Level.ERROR, name + " failed to enable reward " + s);
		for(IProfile prof : this.subProfiles)
			prof.onEnable();
	}

	@Override
	public void onDisable()
	{
		for(String s : this.rewardsToDisable)
			if(!ChanceCubeRegistry.INSTANCE.enableReward(s))
				CCubesCore.logger.log(Level.ERROR, name + " failed to enable reward " + s);
		for(String s : this.rewardsToEnable)
			if(!ChanceCubeRegistry.INSTANCE.disableReward(s))
				CCubesCore.logger.log(Level.ERROR, name + " failed to disable reward " + s);
		for(IProfile prof : this.subProfiles)
			prof.onDisable();
	}

	@Override
	public String getID()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getDesc()
	{
		return desc;
	}

	@Override
	public String getDescLong()
	{
		if(descFull.length() == 0 && !desc.isEmpty())
		{
			descFull.append(desc);
			descFull.append("\n");
			descFull.append("=== Rewards Enabled ===");
			descFull.append("\n");
			if(this.rewardsToEnable.size() == 0)
				descFull.append("None\n");
			for(String s : this.rewardsToEnable)
			{
				descFull.append(s);
				descFull.append("\n");
			}
			descFull.append("=== Rewards Disabled ===");
			descFull.append("\n");
			if(this.rewardsToDisable.size() == 0)
				descFull.append("None\n");
			for(String s : this.rewardsToDisable)
			{
				descFull.append(s);
				descFull.append("\n");
			}
			descFull.append("=== Triggers ===");
			descFull.append("\n");
			if(this.triggers.size() == 0)
				descFull.append("None\n");
			for(ITrigger<?> t : this.triggers)
			{
				descFull.append(t.getClass().getSimpleName());
				descFull.append("\n");
			}
		}
		return descFull.toString();
	}

	@Override
	public List<ITrigger<?>> getTriggers()
	{
		return triggers;
	}
}
