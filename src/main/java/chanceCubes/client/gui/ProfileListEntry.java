package chanceCubes.client.gui;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.util.text.StringTextComponent;

public class ProfileListEntry extends AbstractList.AbstractListEntry<ProfileListEntry>
{
	private ProfilesList profilesList;
	private IProfile profile;
	private Minecraft mc;
	private Button enableToggleBtn;
	private Button editBtn;
	private boolean enabled;

	public ProfileListEntry(ProfilesList profilesList, Minecraft mcIn, String profileName, Screen parentScreen)
	{
		this.profile = GlobalProfileManager.getProfilefromName(profileName);
		this.profilesList = profilesList;
		this.mc = mcIn;
		enabled = GlobalProfileManager.getPlayerProfileManager(Minecraft.getInstance().player.getUniqueID().toString()).isProfileEnabled(profile);

		this.enableToggleBtn = new Button(0, 0, 50, 16, enabled ? "Enabled" : "Disabled", (button) ->
		{
			enableToggleBtn.playDownSound(mc.getSoundHandler());
			enabled = !enabled;
			String playerUUID = Minecraft.getInstance().player.getUniqueID().toString();
			if(enabled)
				GlobalProfileManager.getPlayerProfileManager(playerUUID).enableProfile(profile);
			else
				GlobalProfileManager.getPlayerProfileManager(playerUUID).disableProfile(profile);

			enableToggleBtn.setMessage(enabled ? "Enabled" : "Disabled");
		});

		this.editBtn = new Button(0, 0, 40, 16, "Info", (button) ->
		{
			editBtn.playDownSound(mc.getSoundHandler());
			mc.displayGuiScreen(new ProfileInfoGui(new StringTextComponent(""), profile, parentScreen));
		});
	}

	@Override
	public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
	{
		mc.fontRenderer.drawString(profile.getName(), left, top + 1, 16777215);
		this.enableToggleBtn.x = left + this.profilesList.getListWidth() - 50;
		this.enableToggleBtn.y = top;
		if(isSelected && mouseX - left < this.profilesList.getListWidth() - 55)
			profilesList.profGui.setHoverText(this.profile.getDescLong());
	}
}
