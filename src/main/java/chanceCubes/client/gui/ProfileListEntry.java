package chanceCubes.client.gui;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public class ProfileListEntry implements IGuiListEntry
{
	private ProfilesList profilesList;
	private IProfile profile;
	private Minecraft mc;
	private GuiButton enableToggleBtn;
	private GuiButton editBtn;
	private boolean enabled;

	public ProfileListEntry(ProfilesList profilesList, Minecraft mcIn, String profileName)
	{
		//TODO: ClientSide
		this.profile = GlobalProfileManager.getProfilefromName(profileName);
		this.profilesList = profilesList;
		this.mc = mcIn;
		enabled = GlobalProfileManager.getPlayerProfileManager(Minecraft.getMinecraft().player.getUniqueID().toString()).isProfileEnabled(profile);
		this.enableToggleBtn = new GuiButton(0, 0, 0, 50, 16, enabled ? "Enabled" : "Disabled");
		this.editBtn = new GuiButton(1, 0, 0, 40, 16, "Info");
	}

	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks)
	{

	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
	{
		mc.fontRenderer.drawString(profile.getName(), x, y + 1, 16777215);
		this.enableToggleBtn.x = x + this.profilesList.getListWidth() - 90;
		this.enableToggleBtn.y = y;
		this.editBtn.x = x + this.profilesList.getListWidth() - 35;
		this.editBtn.y = y;
		//this.button.enabled = enabled() && !isDefault();
		this.enableToggleBtn.drawButton(this.mc, mouseX, mouseY, partialTicks);
		this.editBtn.drawButton(this.mc, mouseX, mouseY, partialTicks);
		if(isSelected && mouseX - x < this.profilesList.getListWidth() - 55)
		{
			profilesList.profGui.setHoverText(this.profile.getDescLong());
		}
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
	{
		if(this.enableToggleBtn.mousePressed(this.mc, mouseX, mouseY))
		{
			enableToggleBtn.playPressSound(mc.getSoundHandler());
			enabled = !enabled;
			//TODO: Needs packet to server
			String playerUUID = Minecraft.getMinecraft().player.getUniqueID().toString();
			if(enabled)
				GlobalProfileManager.getPlayerProfileManager(playerUUID).enableProfile(profile, playerUUID);
			else
				GlobalProfileManager.getPlayerProfileManager(playerUUID).disableProfile(profile, playerUUID);

			this.enableToggleBtn.displayString = enabled ? "Enabled" : "Disabled";
			return true;
		}
		else if(this.editBtn.mousePressed(this.mc, mouseX, mouseY))
		{
			editBtn.playPressSound(mc.getSoundHandler());
			this.mc.displayGuiScreen(new ProfileInfoGui(this.profilesList.profGui, profile));
		}
		return false;
	}

	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
	{

	}

}
