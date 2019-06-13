package chanceCubes.client.gui;

import chanceCubes.rewards.profiles.IProfile;
import chanceCubes.rewards.profiles.ProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public class ProfileListEntry extends IGuiListEntry<ProfileListEntry>
{
	private ProfilesList profilesList;
	private IProfile profile;
	private Minecraft mc;
	private GuiButton enableToggleBtn;
	private GuiButton editBtn;
	private boolean enabled;

	public ProfileListEntry(ProfilesList profilesList, Minecraft mcIn, String profileName)
	{
		this.profile = ProfileManager.getProfilefromName(profileName);
		this.profilesList = profilesList;
		this.mc = mcIn;
		enabled = ProfileManager.isProfileEnabled(profile);
			
		this.enableToggleBtn = new GuiButton(0, 0, 0, 50, 16, enabled ? "Enabled" : "Disabled") {
			public void onClick(double mouseX, double mouseY) {
				enableToggleBtn.playPressSound(mc.getSoundHandler());
				enabled = !enabled;
				if(enabled)
					ProfileManager.enableProfile(profile);
				else
					ProfileManager.disableProfile(profile);
	
				enableToggleBtn.displayString = enabled ? "Enabled" : "Disabled";
			}
		};

		this.editBtn = new GuiButton(1, 0, 0, 40, 16, "Info"){
			public void onClick(double mouseX, double mouseY) {
				editBtn.playPressSound(mc.getSoundHandler());
				mc.displayGuiScreen(new ProfileInfoGui(profilesList.profGui, profile));
			}
		};	
	}

	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
	{
		mc.fontRenderer.drawString(profile.getName(), this.getX(), this.getY() + 1, 16777215);
		this.button.x = this.getX() + this.profilesList.getListWidth() - 50;
		this.button.y = this.getY();
		//this.button.enabled = enabled() && !isDefault();
		this.button.render(mouseX, mouseY, partialTicks);
		if(isSelected && mouseX - this.getX() < this.profilesList.getListWidth() - 55)
		{
			profilesList.profGui.setHoverText(this.profile.getDescLong());
		}
	}
}
