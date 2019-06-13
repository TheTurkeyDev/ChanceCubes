package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.profiles.BasicProfile;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.ITrigger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

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
		}
		else
		{
			List<String> triggerStrings = new ArrayList<>();
			for(ITrigger<?> trigger : profile.getTriggers())
				triggerStrings.add(trigger.getTriggerDesc());

			profileList.addStrings("Triggers", triggerStrings);
		}
		this.addButton(new GuiButton(0, this.width / 2 - 36, this.height - 28, 72, 20, "Back")
		{
			public void onClick(double mouseX, double mouseY)
			{
				mc.displayGuiScreen(parentScreen);
			}
		});
	}

	public void addTab(String name, List<String> strings)
	{
		this.addButton(new GuiButton(tabs.size() + 1, (75 * tabs.size()), 64, 75, 20, name)
		{
			public void onClick(double mouseX, double mouseY)
			{
				profileList.setStringsTab(tabs.get(this.id - 1));
			}
		});
		tabs.add(name);
		profileList.addStrings(name, strings);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		profileList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, "Disclaimer: In developement! Does not work on servers!", this.width / 2, 6, 0xFF0000);
		this.drawCenteredString(this.fontRenderer, "Profile Info", this.width / 2, 20, 16777215);
		super.render(mouseX, mouseY, partialTicks);
	}

	public IProfile getProfile()
	{
		return this.profile;
	}
}
