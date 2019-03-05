package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.rewards.profiles.ProfileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class ProfilesList extends GuiListExtended
{
	private List<IGuiListEntry> profiles = new ArrayList<>();
	public ProfileGui profGui;

	public ProfilesList(ProfileGui profGui, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.profGui = profGui;
		for(String s : ProfileManager.getAllProfileNames(true))
			profiles.add(new ProfileListEntry(this, mcIn, s));
	}

	@Override
	public IGuiListEntry getListEntry(int index)
	{
		return profiles.get(index);
	}

	@Override
	protected int getSize()
	{
		return profiles.size();
	}

}
