package chanceCubes.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import chanceCubes.profiles.BasicProfile;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.ITrigger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import scala.actors.threadpool.Arrays;

public class ProfileInfoGui extends GuiScreen
{
	private GuiScreen parentScreen;
	private ProfileInfoList profileList;
	private IProfile profile;

	private List<String> tabs = new ArrayList<>();

	public ProfileInfoGui(GuiScreen screen, IProfile profile)
	{
		parentScreen = screen;
		this.profile = profile;
	}

	@Override
	public void initGui()
	{
		tabs.clear();
		profileList = new ProfileInfoList(this.mc, this.width, this.height, 84, this.height - 32, 20);
		if(profile instanceof BasicProfile)
		{
			BasicProfile bProfile = (BasicProfile) profile;

			List<String> stringsList = new ArrayList<>();
			if(bProfile.getRewardsToEnable().isEmpty())
				stringsList.add("----- NONE -----");
			for(String toEnable : bProfile.getRewardsToEnable())
				stringsList.add(toEnable);
			this.addTab("To Enable", stringsList);

			stringsList = new ArrayList<>();
			if(bProfile.getRewardsToDisable().isEmpty())
				stringsList.add("----- NONE -----");
			for(String toDisable : bProfile.getRewardsToDisable())
				stringsList.add(toDisable);
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
			for(String chanceValueChange : bProfile.getChanceValueChanges())
				stringsList.add(chanceValueChange);
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
		this.addButton(new GuiButton(0, this.width / 2 - 36, this.height - 28, 72, 20, "Back"));
	}

	public void addTab(String name, List<String> strings)
	{
		this.buttonList.add(new GuiButton(tabs.size() + 1, (75 * tabs.size()), 64, 75, 20, name));
		tabs.add(name);
		profileList.addStrings(name, strings);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		profileList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, "Disclaimer: In developement! Does not work on servers!", this.width / 2, 6, 0xFF0000);
		this.drawCenteredString(this.fontRenderer, "Profile Info", this.width / 2, 20, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.enabled)
		{
			if(button.id == 0)
				this.mc.displayGuiScreen(this.parentScreen);
			else if(button.id <= tabs.size())
				this.profileList.setStringsTab(tabs.get(button.id - 1));
		}
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		this.profileList.handleMouseInput();
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.profileList.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Called when a mouse button is released.
	 */
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
		this.profileList.mouseReleased(mouseX, mouseY, state);
	}

	public IProfile getProfile()
	{
		return this.profile;
	}
}
