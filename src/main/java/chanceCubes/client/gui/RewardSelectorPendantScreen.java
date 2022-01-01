package chanceCubes.client.gui;

import chanceCubes.CCubesCore;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketRewardSelector;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RewardSelectorPendantScreen extends Screen
{
	private static final ResourceLocation guiTextures = new ResourceLocation(CCubesCore.MODID, "textures/gui/container/gui_reward_selector_pendant.png");
	private EditBox rewardField;
	private String rewardName = "";
	private final Player player;
	private final int imageWidth = 176;
	private final int imageHeight = 54;
	private final ItemStack stack;

	public RewardSelectorPendantScreen(Player player, ItemStack stack)
	{
		super(new TextComponent(""));
		this.stack = stack;
		this.player = player;
		if(stack.getTag() != null && stack.getTag().contains("Reward"))
			this.rewardName = stack.getTag().getString("Reward");
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void init()
	{
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.rewardField = new EditBox(this.font, i + 17, j + 10, 143, 12, new TextComponent("Test"));
		this.rewardField.setTextColor(-1);
		//this.rewardField.setDisabledTextColour(-1);
		//this.rewardField.setEnableBackgroundDrawing(true);
		this.rewardField.setMaxLength(100);
		this.rewardField.setValue(this.rewardName);
		this.addRenderableWidget(this.rewardField);
		this.addRenderableWidget(new Button(i + 57, j + 27, 70, 20, new TextComponent("Set Reward"), p_onPress_1_ ->
		{
			CompoundTag nbt = stack.getTag();
			if(nbt == null)
				nbt = new CompoundTag();
			nbt.putString("Reward", rewardName);
			stack.setTag(nbt);
			CCubesPacketHandler.CHANNEL.sendToServer(new PacketRewardSelector(rewardField.getValue()));
			rewardName = rewardField.getValue();
			player.closeContainer();
		}));
	}

	@Override
	public void onClose()
	{
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, guiTextures);
		this.blit(matrixStack, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.rewardField.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}