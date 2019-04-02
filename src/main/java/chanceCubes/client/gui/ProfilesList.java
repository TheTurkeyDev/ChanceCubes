package chanceCubes.client.gui;

import chanceCubes.rewards.profiles.ProfileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class ProfilesList extends GuiListExtended<ProfileListEntry>
{
	public ProfileGui profGui;

	public ProfilesList(ProfileGui profGui, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.profGui = profGui;
		for(String s : ProfileManager.getAllProfileNames(true))
			this.addEntry(new ProfileListEntry(this, mcIn, s));
	}
}