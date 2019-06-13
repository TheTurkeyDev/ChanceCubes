package chanceCubes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public class ProfileInfoListEntry extends IGuiListEntry<ProfileInfoListEntry>
{
	private String entryString;
	private Minecraft mc;

	public ProfileInfoListEntry(Minecraft mcIn, String entryString)
	{
		this.entryString = entryString;
		this.mc = mcIn;
	}

	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
	{
		mc.fontRenderer.drawStringWithShadow(entryString, this.getX(), this.getY(), 16777215);
	}
}