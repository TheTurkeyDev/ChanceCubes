package chanceCubes.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

import com.google.common.collect.Lists;

public class CustomExtendedList extends GuiListExtended
{
	public List<CustomListEntry> elements = Lists.newArrayList();

	private Minecraft mc;
	private ConfigGui parentScreen;

	public CustomExtendedList(ConfigGui parentScreen, Minecraft mc, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_)
	{
		super(mc, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
		this.parentScreen = parentScreen;
		this.mc = mc;
	}

	@Override
	public IGuiListEntry getListEntry(int index)
	{
		return elements.get(index);
	}

	@Override
	protected int getSize()
	{
		return elements.size();
	}

	public void addElement(String e)
	{
		elements.add(new CustomListEntry(e, parentScreen, mc));
	}
	
	public void clearElements()
	{
		elements.clear();
	}



	public class CustomListEntry implements IGuiListEntry
	{
		private String name;
		private GuiButton button;
		private ConfigGui parentScreen;
		private Minecraft mc;

		public CustomListEntry(String name, ConfigGui parentScreen, Minecraft mc)
		{
			this.name = name;
			this.parentScreen = parentScreen;
			button = new GuiButton(0, 0, 0, 200, 20, name);
			this.mc = mc;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
		{
			this.button.displayString = name;
            this.button.xPosition = x;
            this.button.yPosition = y;
            //this.button.enabled = enabled();
            this.button.drawButton(mc, mouseX, mouseY);
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{
			if(this.button.mousePressed(mc, x, y))
				parentScreen.nextEditStage(button.displayString);
			return false;
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{
			
		}
	}
}