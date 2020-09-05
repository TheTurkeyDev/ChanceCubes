package chanceCubes.client.gui;

import chanceCubes.CCubesCore;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketRewardSelector;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RewardSelectorPendantGui extends Screen
{
	private static final ResourceLocation guiTextures = new ResourceLocation(CCubesCore.MODID + ":textures/gui/container/gui_reward_selector_pendant.png");
	private TextFieldWidget rewardField;
	private String rewardName = "";
	private PlayerEntity player;
	private int imageWidth = 176;
	private int imageHeight = 54;
	private ItemStack stack;

	public RewardSelectorPendantGui(PlayerEntity player, ItemStack stack)
	{
		super(new StringTextComponent(""));
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
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.rewardField = new TextFieldWidget(this.font, i + 17, j + 10, 143, 12, new StringTextComponent("Test"));
		this.rewardField.setTextColor(-1);
		this.rewardField.setDisabledTextColour(-1);
		this.rewardField.setEnableBackgroundDrawing(true);
		this.rewardField.setMaxStringLength(100);
		this.rewardField.setText(this.rewardName);
		this.children.add(this.rewardField);
		this.addButton(new Button(i + 57, j + 27, 70, 20, new StringTextComponent("Set Reward"), p_onPress_1_ ->
		{
			CompoundNBT nbt = stack.getTag();
			if(nbt == null)
				nbt = new CompoundNBT();
			nbt.putString("Reward", rewardName);
			stack.setTag(nbt);
			CCubesPacketHandler.CHANNEL.sendToServer(new PacketRewardSelector(rewardField.getText()));
			rewardName = rewardField.getText();
			player.closeScreen();

		}));
	}

	@Override
	public void onClose()
	{
		super.onClose();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.minecraft.getTextureManager().bindTexture(guiTextures);
		this.blit(matrixStack, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.rewardField.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}