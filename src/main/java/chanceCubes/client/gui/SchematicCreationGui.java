package chanceCubes.client.gui;

import java.io.IOException;

import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SchematicCreationGui extends GuiScreen
{
	private GuiTextField nameField;

	private EntityPlayer player;

	private String[] buttonText = { "x--", "x++", "y--", "y++", "z--", "z++" };

	public SchematicCreationGui(EntityPlayer player)
	{
		this.player = player;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		this.buttonList.clear();
		int i = this.width / 2;
		this.nameField = new GuiTextField(0, this.fontRenderer, i - 70, 10, 140, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(true);
		this.nameField.setMaxStringLength(100);
		this.nameField.setText("Schematic Name");

		this.buttonList.add(new GuiButton(9, i - 50, this.height - 70, 100, 20, "Back"));
		this.buttonList.add(new GuiButton(10, i - 50, this.height - 40, 100, 20, "Create"));

		for(int j = 0; j < 2; j++)
			for(int k = 0; k < 6; k++)
				this.buttonList.add(new GuiButton(11 + ((j * 6) + k), (i - 90) + (k * 30), (50 * (j + 1)), 25, 20, buttonText[k]));
	}

	public void onGuiClosed()
	{
		super.onGuiClosed();
	}

	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
		{
			if(button.id == 9)
			{
				FMLCommonHandler.instance().showGuiScreen(null);
			}
			else if(button.id == 10)
			{
				String fileName = nameField.getText();
				fileName = fileName.endsWith(".ccs") ? fileName : fileName + ".ccs";
				SchematicUtil.createCustomSchematic(player.world, SchematicUtil.selectionPoints[0], SchematicUtil.selectionPoints[1], fileName);
				player.sendMessage(new TextComponentString("Schematic file named " + fileName + " created!"));
				RenderEvent.setCreatingSchematic(false);
				SchematicUtil.selectionPoints[0] = null;
				SchematicUtil.selectionPoints[1] = null;
				FMLCommonHandler.instance().showGuiScreen(null);
			}
			else if(button.id > 10 && button.id < 24)
			{
				int id = (button.id - 11) % 6;
				int point = (button.id - 11) / 6;

				if(id == 0)
					SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(-1, 0, 0);
				else if(id == 1)
					SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(1, 0, 0);
				else if(id == 2)
					SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, -1, 0);
				else if(id == 3)
					SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, 1, 0);
				else if(id == 4)
					SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, 0, -1);
				else if(id == 5)
					SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, 0, 1);
			}
		}
	}

	protected void keyTyped(char p_73869_1_, int p_73869_2_) throws IOException
	{
		if(!this.nameField.textboxKeyTyped(p_73869_1_, p_73869_2_))
			super.keyTyped(p_73869_1_, p_73869_2_);
	}

	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
	{
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		this.nameField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	public void drawScreen(int mouseX, int mouseY, float p_73863_3_)
	{
		this.drawGradientRect(0, 0, this.width, this.height, 0xBB000000, 0xBB000000);
		if(this.nameField != null)
			this.nameField.drawTextBox();
		int i = this.width / 2;
		this.drawCenteredString(this.fontRenderer, "Point 1 " + SchematicUtil.selectionPoints[0], i, 40, 0xFFFFFF);
		this.drawCenteredString(this.fontRenderer, "Point 2 " + SchematicUtil.selectionPoints[1], i, 90, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, p_73863_3_);
	}
}