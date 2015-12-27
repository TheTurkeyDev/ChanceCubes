package chanceCubes.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;

import com.google.common.collect.Lists;

public class CustomExtendedList extends GuiListExtended
{
	public List<IGuiListEntry> elements = Lists.newArrayList();

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

	public void addButton(String e)
	{
		elements.add(new CustomListEntry(e, parentScreen, mc));
	}
	public void addTextEntry(String label, String textBoxText)
	{
		elements.add(new CustomTextEntry(parentScreen, mc, label, textBoxText));
	}
	
	public void clearElements()
	{
		elements.clear();
	}
	
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		for(IGuiListEntry entry : elements)
			if(entry instanceof CustomTextEntry)
				((CustomTextEntry)entry).keyTyped(p_73869_1_, p_73869_2_);
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
	
	public class CustomTextEntry implements IGuiListEntry
	{
		private GuiTextField text;
		@SuppressWarnings("unused")
		private ConfigGui parentScreen;
		private Minecraft mc;
		private String label;

		public CustomTextEntry(ConfigGui parentScreen, Minecraft mc, String label, String textBoxText)
		{
			this.parentScreen = parentScreen;
			text = new GuiTextField(mc.fontRenderer, 0, 0, 200, 20);
			text.setText(textBoxText);
			this.mc = mc;
			this.label = label + ":";
			this.label = this.label.substring(0, 1).toUpperCase() + this.label.substring(1);
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
		{
            this.text.xPosition = x + 30;
            this.text.yPosition = y;
            //this.button.enabled = enabled();
            this.text.drawTextBox();
            mc.fontRenderer.drawString(this.label, x - (int) (label.length() * 3), y + 7, 0xFFFFFF);
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{
			text.mouseClicked(x, y, mouseEvent);
			return false;
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{
			
		}
		
		public void keyTyped(char p_73869_1_, int p_73869_2_)
		{
			this.text.textboxKeyTyped(p_73869_1_, p_73869_2_);
		}
		
		public String getLabel()
		{
			return this.label;
		}
		
		public GuiTextField getTextBox()
		{
			return this.text;
		}
	}
}