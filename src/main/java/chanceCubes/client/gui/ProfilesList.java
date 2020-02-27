package chanceCubes.client.gui;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.PlayerProfileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;

import java.util.ArrayList;
import java.util.List;

public class ProfilesList extends ExtendedList<ProfileListEntry>
{
	private List<AbstractList.AbstractListEntry<ProfileListEntry>> profiles = new ArrayList<>();
	public ProfileGui profGui;

	public ProfilesList(ProfileGui profGui, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.profGui = profGui;
		if(Minecraft.getInstance().player != null)
		{
			PlayerProfileManager ppm = GlobalProfileManager.getPlayerProfileManager(Minecraft.getInstance().player.getUniqueID().toString());
			if(ppm != null)
				for(IProfile s : ppm.getAllProfiles())
					profiles.add(new ProfileListEntry(this, mcIn, s.getName(), profGui));
		}
	}

	public int getListWidth()
	{
		return 250;
	}
}