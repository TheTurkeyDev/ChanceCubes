package chanceCubes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;

public class ProfileInfoListEntry extends AbstractList.AbstractListEntry<ProfileInfoListEntry>
{
	private String entryString;
	private Minecraft mc;

	public ProfileInfoListEntry(Minecraft mcIn, String entryString)
	{
		this.entryString = entryString;
		this.mc = mcIn;
	}

	@Override
	public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
	{
		mc.fontRenderer.drawStringWithShadow(entryString, left, top, 16777215);
	}
}