package chanceCubes.client.gui;

import chanceCubes.rewards.profiles.IProfile;
import chanceCubes.rewards.profiles.ProfileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public class ProfileListEntry implements IGuiListEntry
{
	private ProfilesList profilesList;
	private IProfile profile;
	private Minecraft mc;
	private GuiButton button;
	private boolean enabled;

	public ProfileListEntry(ProfilesList profilesList, Minecraft mcIn, String profileName)
	{
		this.profile = ProfileManager.getProfilefromName(profileName);
		this.profilesList = profilesList;
		this.mc = mcIn;
		enabled = ProfileManager.isProfileEnabled(profile);
		this.button = new GuiButton(0, 0, 0, 50, 16, enabled ? "Enabled" : "Disabled");
	}

	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks)
	{

	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
	{
		mc.fontRenderer.drawString(profile.getName(), x, y + 1, 16777215);
		this.button.x = x + this.profilesList.getListWidth() - 50;
		this.button.y = y;
		//this.button.enabled = enabled() && !isDefault();
		this.button.drawButton(this.mc, mouseX, mouseY, partialTicks);
		if(isSelected && mouseX - x < this.profilesList.getListWidth() - 55)
		{
			profilesList.profGui.setHoverText(this.profile.getDescLong());
		}
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
	{
		if(this.button.mousePressed(this.mc, mouseX, mouseY))
		{
			button.playPressSound(mc.getSoundHandler());
			enabled = !enabled;
			if(enabled)
				ProfileManager.enableProfile(profile);
			else
				ProfileManager.disableProfile(profile);

			this.button.displayString = enabled ? "Enabled" : "Disabled";
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
	{

	}

}
