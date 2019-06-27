package chanceCubes.profiles.triggerHooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chanceCubes.profiles.BasicProfile;
import chanceCubes.profiles.ProfileManager;

public class RadioGroupProfile extends BasicProfile
{
	private List<RadioGroupProfile> otherProfiles = new ArrayList<>();
	private RadioGroupProfile defaultProfile;

	public RadioGroupProfile(String id, String name, String desc)
	{
		super(id, name, desc);
	}

	public void assignOtherProfiles(RadioGroupProfile defaultProfile, RadioGroupProfile... profiles)
	{
		this.defaultProfile = defaultProfile;
		this.otherProfiles = Arrays.asList(profiles);
	}

	@Override
	public void onEnable()
	{
		for(RadioGroupProfile profile : otherProfiles)
			if(profile != this)
				ProfileManager.disableProfile(profile);
		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		ProfileManager.enableProfile(defaultProfile);
	}
}
