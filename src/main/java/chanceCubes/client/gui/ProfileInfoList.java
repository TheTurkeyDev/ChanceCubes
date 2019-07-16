package chanceCubes.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;

public class ProfileInfoList extends ExtendedList<ProfileInfoListEntry>
{
	private Map<String, List<AbstractList.AbstractListEntry<ProfileInfoListEntry>>> strings = new HashMap<>();
	private String currentTab = "";
	private Minecraft mc;

	public ProfileInfoList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.mc = mcIn;
	}

	public void addStrings(String tabName, List<String> stringsIn)
	{
		if(strings.size() == 0)
			currentTab = tabName;
		List<AbstractList.AbstractListEntry<ProfileInfoListEntry>> entries = new ArrayList<>();
		for(String s : stringsIn)
			entries.add(new ProfileInfoListEntry(mc, s));
		this.strings.put(tabName, entries);
	}

	public void setStringsTab(String tab)
	{
		this.currentTab = tab;
	}
}
