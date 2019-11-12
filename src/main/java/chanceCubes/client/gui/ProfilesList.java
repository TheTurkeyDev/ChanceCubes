package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
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
		for(IProfile s : GlobalProfileManager.getPlayerProfileManager(Minecraft.getMinecraft().player.getUniqueID().toString()).getAllProfiles())
			profiles.add(new ProfileListEntry(this, mcIn, s.getName()));
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

	protected int getScrollBarX()
	{
		return this.width / 2 + 160;
	}

	public int getListWidth()
	{
		return 250;
	}
}
