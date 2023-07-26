package chanceCubes.client.gui;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketCreativePendant;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreativePendantScreen extends AbstractContainerScreen<CreativePendantContainer>
{
	private static final ResourceLocation GUI_BG = new ResourceLocation(CCubesCore.MODID, "textures/gui/container/gui_creative_pendant.png");

	private int chanceValue = 0;

	public CreativePendantScreen(CreativePendantContainer pendantContainer, Inventory inv, Component component)
	{
		super(pendantContainer, inv, component);
	}

	@Override
	protected void init()
	{
		super.init();
		int halfWidth = this.width / 2;
		int halfHeight = (this.height / 2);
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("-1"), p_onPress_1_ -> this.changeChanceValue(-1)).bounds(halfWidth - 40, halfHeight - 60, 20, 20).build());
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("+1"), p_onPress_1_ -> this.changeChanceValue(1)).bounds(halfWidth + 15, halfHeight - 60, 20, 20).build());
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("-5"), p_onPress_1_ -> this.changeChanceValue(-5)).bounds(halfWidth - 60, halfHeight - 60, 20, 20).build());
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("+5"), p_onPress_1_ -> this.changeChanceValue(5)).bounds(halfWidth + 35, halfHeight - 60, 20, 20).build());
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("-10"), p_onPress_1_ -> this.changeChanceValue(-10)).bounds(halfWidth - 80, halfHeight - 60, 20, 20).build());
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("+10"), p_onPress_1_ -> this.changeChanceValue(10)).bounds(halfWidth + 55, halfHeight - 60, 20, 20).build());
		this.addRenderableWidget(Button.builder(ComponentWrapper.string("Set Chance"), p_onPress_1_ -> CCubesPacketHandler.CHANNEL.sendToServer(new PacketCreativePendant(this.chanceValue))).bounds(halfWidth + 12, halfHeight - 35, 70, 20).build());
	}

	public void changeChanceValue(int amount)
	{
		this.chanceValue += amount;
		if(this.chanceValue > 100)
			this.chanceValue = 100;
		if(this.chanceValue < -100)
			this.chanceValue = -100;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float p_97788_, int p_97789_, int p_97790_)
	{
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(GUI_BG, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int p_97936_, int p_97937_)
	{
		guiGraphics.drawString(this.font, "Chance Value", 50, 5, 0, false);
		String cValue = String.valueOf(this.chanceValue);
		guiGraphics.drawString(this.font, cValue, (88 - (cValue.length() * 3)), 27, 0, false);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int p_97922_, int p_97923_, float p_97924_)
	{
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, p_97922_, p_97923_, p_97924_);
		this.renderTooltip(guiGraphics, p_97922_, p_97923_);
	}
}
