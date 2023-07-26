package chanceCubes.client.gui;

import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SchematicCreationGui extends Screen
{
	private final String[] buttonText = {"x--", "x++", "y--", "y++", "z--", "z++"};

	private EditBox nameField;

	private final Player player;

	public SchematicCreationGui(Player player)
	{
		super(ComponentWrapper.string("Test"));
		this.player = player;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void init()
	{
		this.clearWidgets();
		int i = this.width / 2;
		this.nameField = new EditBox(this.font, i - 70, 10, 140, 12, ComponentWrapper.string("TEST"));
		this.nameField.setTextColor(-1);
		//this.nameField.setDisabledTextColour(-1);
		//this.nameField.setEnableBackgroundDrawing(true);
		this.nameField.setMaxLength(100);
		this.nameField.setValue("Schematic Name");

		this.addWidget(Button.builder(ComponentWrapper.string("Back"), (button) ->
		{
			SchematicCreationGui.this.onClose();
		}).bounds(i - 50, this.height - 70, 100, 20).build());

		this.addWidget(Button.builder(ComponentWrapper.string("Create"), (button) ->
		{
			String fileName = nameField.getValue();
			fileName = fileName.endsWith(".ccs") ? fileName : fileName + ".ccs";
			SchematicUtil.createCustomSchematic(player.level(), SchematicUtil.selectionPoints[0], SchematicUtil.selectionPoints[1], fileName);
			RewardsUtil.sendMessageToPlayer(player, "Schematic file named " + fileName + " created!");
			RenderEvent.setCreatingSchematic(false);
			SchematicUtil.selectionPoints[0] = null;
			SchematicUtil.selectionPoints[1] = null;
			SchematicCreationGui.this.onClose();
		}).bounds(i - 50, this.height - 40, 100, 20).build());

		for(int j = 0; j < 2; j++)
		{
			for(int k = 0; k < 6; k++)
			{
				int buttonID = (j * 2) + k;
				this.addWidget(Button.builder(ComponentWrapper.string(buttonText[k]), (button) ->
				{
					int idNormalized = buttonID % 6;
					int point = (buttonID) / 6;

					if(idNormalized == 0)
						SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].offset(-1, 0, 0);
					else if(idNormalized == 1)
						SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].offset(1, 0, 0);
					else if(idNormalized == 2)
						SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].offset(0, -1, 0);
					else if(idNormalized == 3)
						SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].offset(0, 1, 0);
					else if(idNormalized == 4)
						SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].offset(0, 0, -1);
					else if(idNormalized == 5)
						SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].offset(0, 0, 1);
				}).bounds((i - 90) + (k * 30), (50 * (j + 1)), 25, 20).build());
			}
		}
	}

	public boolean charTyped(char p_73869_1_, int p_73869_2_)
	{
		if(!this.nameField.charTyped(p_73869_1_, p_73869_2_))
			return super.charTyped(p_73869_1_, p_73869_2_);
		return false;
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseEvent)
	{
		super.mouseClicked(x, y, mouseEvent);
		return this.nameField.mouseClicked(x, y, mouseEvent);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(guiGraphics); //TODO: Test if this works or else feed the blit
//		poseStack.blit(INSERT_TEXTURE, 0, 0, this.width, this.height, 0xBB000000, 0xBB000000);
		if(this.nameField != null)
			this.nameField.render(guiGraphics, mouseX, mouseY, partialTicks);
		int i = this.width / 2;
		guiGraphics.drawCenteredString(this.font, "Point 1 " + SchematicUtil.selectionPoints[0], i, 40, 0xFFFFFF);
		guiGraphics.drawCenteredString(this.font, "Point 2 " + SchematicUtil.selectionPoints[1], i, 90, 0xFFFFFF);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
}