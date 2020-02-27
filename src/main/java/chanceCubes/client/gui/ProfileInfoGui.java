package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import chanceCubes.profiles.BasicProfile;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.ITrigger;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class ProfileInfoGui extends Screen
{
	private Screen parentScreen;
	private ProfileInfoList profileList;
	private IProfile profile;

	private List<String> tabs = new ArrayList<>();

	public ProfileInfoGui(ITextComponent titleIn, IProfile profile, Screen parentScreen)
	{
		super(titleIn);
		this.profile = profile;
		this.parentScreen = parentScreen;
	}

	@Override
	public void init()
	{
		tabs.clear();
		profileList = new ProfileInfoList(this.minecraft, this.width, this.height, 84, this.height - 32, 20);
		if(profile instanceof BasicProfile)
		{
			BasicProfile bProfile = (BasicProfile) profile;

			List<String> stringsList = new ArrayList<>();
			if(bProfile.getRewardsToEnable().isEmpty())
				stringsList.add("----- NONE -----");
			stringsList.addAll(bProfile.getRewardsToEnable());
			this.addTab("To Enable", stringsList);

			stringsList = new ArrayList<>();
			if(bProfile.getRewardsToDisable().isEmpty())
				stringsList.add("----- NONE -----");
			stringsList.addAll(bProfile.getRewardsToDisable());
			this.addTab("To Disable", stringsList);

			stringsList = new ArrayList<>();
			if(bProfile.getTriggers().isEmpty())
				stringsList.add("----- NONE -----");
			for(ITrigger<?> trigger : bProfile.getTriggers())
				stringsList.add(trigger.getTriggerDesc());
			this.addTab("Triggers", stringsList);

			stringsList = new ArrayList<>();
			if(bProfile.getChanceValueChanges().isEmpty())
				stringsList.add("----- NONE -----");
			stringsList.addAll(bProfile.getChanceValueChanges());
			this.addTab("New Chances", stringsList);

			stringsList = new ArrayList<>();
			if(bProfile.getSubProfiles().isEmpty())
				stringsList.add("----- NONE -----");
			for(IProfile profile : bProfile.getSubProfiles())
				stringsList.add(profile.getName() + " (" + profile.getID() + ")");
			this.addTab("Sub Profs", stringsList);

			stringsList = new ArrayList<>();
			if(bProfile.getRewardSettings().isEmpty())
				stringsList.add("----- NONE -----");
			for(String reward : bProfile.getRewardSettings().keySet())
			{
				stringsList.add("----------------------");
				Map<String, Object> rewardSettings = bProfile.getRewardSettings().get(reward);
				stringsList.add(reward);
				stringsList.add("----------------------");
				for(String setting : rewardSettings.keySet())
				{
					Object valueObj = rewardSettings.get(setting);
					if(valueObj instanceof String[])
						stringsList.add(setting + " - " + Arrays.toString((String[]) valueObj));
					else if(valueObj instanceof JsonObject[])
						stringsList.add(setting + " - " + Arrays.toString((JsonObject[]) valueObj));
					else
						stringsList.add(setting + " - " + valueObj.toString());
				}
			}
			this.addTab("Reward Props", stringsList);
		}
		else
		{
			List<String> triggerStrings = new ArrayList<>();
			for(ITrigger<?> trigger : profile.getTriggers())
				triggerStrings.add(trigger.getTriggerDesc());

			profileList.addStrings("Triggers", triggerStrings);
		}
		this.addButton(new Button(this.width / 2 - 36, this.height - 28, 72, 20, "Back", (button) ->
		{
			if(minecraft != null)
				minecraft.displayGuiScreen(parentScreen);
		}));
	}

	public void addTab(String name, List<String> strings)
	{
		int pos = tabs.size() + 1;
		this.addButton(new Button((75 * tabs.size()), 64, 75, 20, name, (button) ->
		{
			profileList.setStringsTab(tabs.get(pos - 1));
		}));
		tabs.add(name);
		profileList.addStrings(name, strings);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		profileList.render(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.font, "Disclaimer: In developement! Does not work on servers!", this.width / 2, 6, 0xFF0000);
		this.drawCenteredString(this.font, "Profile Info", this.width / 2, 20, 16777215);
		super.render(mouseX, mouseY, partialTicks);
	}
}