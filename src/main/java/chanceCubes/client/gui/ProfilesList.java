package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.profiles.ProfileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;

public class ProfilesList extends ExtendedList<ProfileListEntry>
{
	private List<AbstractList.AbstractListEntry<ProfileListEntry>> profiles = new ArrayList<>();
	public ProfileGui profGui;

	public ProfilesList(ProfileGui profGui, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.profGui = profGui;
		for(String s : ProfileManager.getAllProfileNames(true))
			profiles.add(new ProfileListEntry(this, mcIn, s, profGui));
	}

	public int getListWidth()
	{
		return 250;
	}
}
