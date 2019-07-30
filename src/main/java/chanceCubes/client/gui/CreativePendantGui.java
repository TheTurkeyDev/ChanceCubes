package chanceCubes.client.gui;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketCreativePendant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreativePendantGui extends ContainerScreen<CreativePendantContainer>
{
	private static final ResourceLocation guiTextures = new ResourceLocation(CCubesCore.MODID + ":textures/gui/container/gui_creative_pendant.png");
	private int chanceValue = 0;
	private static CreativePendantContainer container;
	private PlayerEntity player;

	public CreativePendantGui(PlayerEntity player, World world)
	{
		super(container = new CreativePendantContainer(0, player.inventory, world), player.inventory, new StringTextComponent("Creative Pendant"));
		this.xSize = 176;
		this.ySize = 167;
		this.player = player;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void init()
	{
		super.init();
		this.buttons.clear();
		this.addButton(new CustomGuiButton(this.width / 2 - 40, (this.height / 2) - 63, 20, 20, I18n.format("-1"), 0));
		this.addButton(new CustomGuiButton(this.width / 2 + 15, (this.height / 2) - 63, 20, 20, I18n.format("+1"), 0));
		this.addButton(new CustomGuiButton(this.width / 2 - 60, (this.height / 2) - 63, 20, 20, I18n.format("-5"), 0));
		this.addButton(new CustomGuiButton(this.width / 2 + 35, (this.height / 2) - 63, 20, 20, I18n.format("+5"), 0));
		this.addButton(new CustomGuiButton(this.width / 2 - 80, (this.height / 2) - 63, 20, 20, I18n.format("-10"), 0));
		this.addButton(new CustomGuiButton(this.width / 2 + 55, (this.height / 2) - 63, 20, 20, I18n.format("+10"), 0));
		this.addButton(new CustomGuiButton(this.width / 2 + 12, (this.height / 2) - 35, 70, 20, I18n.format("Set Chance"), 0));
	}

	public class CustomGuiButton extends Button
	{
		public CustomGuiButton(int x, int y, int widthIn, int heightIn, String buttonText, int inc)
		{
			super(x, y, widthIn, heightIn, buttonText, (p_onPress_1_) ->
			{
				if(inc == 0)
				{
					if(container.getChanceCubesInPendant() != null)
						CCubesPacketHandler.CHANNEL.sendToServer(new PacketCreativePendant(player.getName().toString(), chanceValue));
				}
				else
				{
					chanceValue += inc;
				}

				if(chanceValue > 100)
					chanceValue = 100;
				if(chanceValue < -100)
					chanceValue = -100;
			});
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		this.font.drawString("Chance Value", 50, 5, 0);
		String cValue = "" + this.chanceValue;
		this.font.drawString(cValue, (88 - (cValue.length() * 3)), 27, 0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		Minecraft.getInstance().getTextureManager().bindTexture(guiTextures);
		this.blit((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, xSize, ySize);
	}
}