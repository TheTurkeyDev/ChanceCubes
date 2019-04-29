package chanceCubes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public class ProfileInfoListEntry implements IGuiListEntry
{
	private String entryString;
	private Minecraft mc;

	public ProfileInfoListEntry(Minecraft mcIn, String entryString)
	{
		this.entryString = entryString;
		this.mc = mcIn;
	}

	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks)
	{

	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
	{
		mc.fontRenderer.drawStringWithShadow(entryString, (x + listWidth / 2) - mc.fontRenderer.getStringWidth(entryString) / 2, y + (slotHeight / 2), 16777215);
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
	{

		return false;
	}

	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
	{

	}
}