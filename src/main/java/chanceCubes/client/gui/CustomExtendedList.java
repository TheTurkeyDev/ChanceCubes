package chanceCubes.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;

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
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
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

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
		{
			
		}
	}
	
	public class CustomTextEntry implements IGuiListEntry
	{
		private GuiTextField text;
		private ConfigGui parentScreen;
		private Minecraft mc;

		public CustomTextEntry(ConfigGui parentScreen, Minecraft mc)
		{
			this.parentScreen = parentScreen;
			text = new GuiTextField(bottom, mc.fontRendererObj, 0, 0, 200, 20);
			this.mc = mc;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
		{
            this.text.xPosition = x;
            this.text.yPosition = y;
            //this.button.enabled = enabled();
            this.text.drawTextBox();
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{
			return false;
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{
			
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
		{
			
		}
	}
}