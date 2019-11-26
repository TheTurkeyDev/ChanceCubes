package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.PlayerProfileManager;
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
		if(Minecraft.getMinecraft().player != null)
		{
			PlayerProfileManager ppm = GlobalProfileManager.getPlayerProfileManager(Minecraft.getMinecraft().player.getUniqueID().toString());
			if(ppm != null)
				for(IProfile s : ppm.getAllProfiles())
					profiles.add(new ProfileListEntry(this, mcIn, s.getName()));
		}
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
