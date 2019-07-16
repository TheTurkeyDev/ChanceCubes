package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.profiles.BasicProfile;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.ITrigger;
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
		this.addButton(new Button(this.width / 2 - 36, this.height - 28, 72, 20, "Back", new Button.IPressable()
		{
			@Override
			public void onPress(Button button)
			{
				minecraft.displayGuiScreen(parentScreen);
			}
		}));
	}

	public void addTab(String name, List<String> strings)
	{
		int pos = tabs.size() + 1;
		this.addButton(new Button((75 * tabs.size()), 64, 75, 20, name, new Button.IPressable()
		{
			@Override
			public void onPress(Button button)
			{
				profileList.setStringsTab(tabs.get(pos - 1));
			}
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

	public IProfile getProfile()
	{
		return this.profile;
	}
}
