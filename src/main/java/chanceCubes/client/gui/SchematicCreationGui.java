package chanceCubes.client.gui;

import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SchematicCreationGui extends Screen
{
	private TextFieldWidget nameField;

	private PlayerEntity player;

	private String[] buttonText = { "x--", "x++", "y--", "y++", "z--", "z++" };

	public SchematicCreationGui(PlayerEntity player)
	{
		super(new StringTextComponent("Test"));
		this.player = player;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		this.buttons.clear();
		int i = this.width / 2;
		this.nameField = new TextFieldWidget(this.font, i - 70, 10, 140, 12, "TEST");
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(true);
		this.nameField.setMaxStringLength(100);
		this.nameField.setText("Schematic Name");

		this.buttons.add(new Button(i - 50, this.height - 70, 100, 20, "Back", new Button.IPressable()
		{
			@Override
			public void onPress(Button button)
			{
				SchematicCreationGui.this.onClose();
			}
		}));

		this.buttons.add(new Button(i - 50, this.height - 40, 100, 20, "Create", new Button.IPressable()
		{
			@Override
			public void onPress(Button p_onPress_1_)
			{
				String fileName = nameField.getText();
				fileName = fileName.endsWith(".ccs") ? fileName : fileName + ".ccs";
				SchematicUtil.createCustomSchematic(player.world, SchematicUtil.selectionPoints[0], SchematicUtil.selectionPoints[1], fileName);
				player.sendMessage(new StringTextComponent("Schematic file named " + fileName + " created!"));
				RenderEvent.setCreatingSchematic(false);
				SchematicUtil.selectionPoints[0] = null;
				SchematicUtil.selectionPoints[1] = null;
				SchematicCreationGui.this.onClose();
			}
		}));

		for(int j = 0; j < 2; j++)
		{
			for(int k = 0; k < 6; k++)
			{
				int id = 11 + ((j * 6) + k);
				this.buttons.add(new Button((i - 90) + (k * 30), (50 * (j + 1)), 25, 20, buttonText[k], new Button.IPressable()
				{
					@Override
					public void onPress(Button p_onPress_1_)
					{
						int id2 = (id - 11) % 6;
						int point = (id2 - 11) / 6;

						if(id2 == 0)
							SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(-1, 0, 0);
						else if(id2 == 1)
							SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(1, 0, 0);
						else if(id2 == 2)
							SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, -1, 0);
						else if(id2 == 3)
							SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, 1, 0);
						else if(id2 == 4)
							SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, 0, -1);
						else if(id2 == 5)
							SchematicUtil.selectionPoints[point] = SchematicUtil.selectionPoints[point].add(0, 0, 1);
					}
				}));
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
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.blit(0, 0, this.width, this.height, 0xBB000000, 0xBB000000);
		if(this.nameField != null)
			this.nameField.render(mouseX, mouseY, partialTicks);
		int i = this.width / 2;
		this.drawCenteredString(this.font, "Point 1 " + SchematicUtil.selectionPoints[0], i, 40, 0xFFFFFF);
		this.drawCenteredString(this.font, "Point 2 " + SchematicUtil.selectionPoints[1], i, 90, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
	}
}