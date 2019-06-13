package chanceCubes.client.gui;

import org.lwjgl.opengl.GL11;

import chanceCubes.CCubesCore;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketRewardSelector;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RewardSelectorPendantGui extends GuiScreen
{
	private static final ResourceLocation guiTextures = new ResourceLocation(CCubesCore.MODID + ":textures/gui/container/gui_reward_selector_pendant.png");
	private GuiTextField rewardField;
	private String rewardName = "";
	private EntityPlayer player;
	private int imageWidth = 176;
	private int imageHeight = 54;
	private ItemStack stack;

	public RewardSelectorPendantGui(EntityPlayer player, ItemStack stack)
	{
		this.stack = stack;
		this.player = player;
		if(stack.getTag() != null && stack.getTag().hasKey("Reward"))
			this.rewardName = stack.getTag().getString("Reward");
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.rewardField = new GuiTextField(0, this.fontRenderer, i + 17, j + 10, 143, 12);
		this.rewardField.setTextColor(-1);
		this.rewardField.setDisabledTextColour(-1);
		this.rewardField.setEnableBackgroundDrawing(true);
		this.rewardField.setMaxStringLength(100);
		this.rewardField.setText(this.rewardName);
		this.children.add(this.rewardField);
		this.addButton(new GuiButton(0, i + 57, j + 27, 70, 20, I18n.format("Set Reward", new Object[0]))
		{
			public void onClick(double mouseX, double mouseY)
			{
				if(ChanceCubeRegistry.INSTANCE.getRewardByName(rewardField.getText()) != null || GiantCubeRegistry.INSTANCE.getRewardByName(rewardField.getText()) != null)
				{
					NBTTagCompound nbt = stack.getTag();
					if(nbt == null)
						nbt = new NBTTagCompound();
					nbt.setString("Reward", rewardName);
					stack.setTag(nbt);

					CCubesPacketHandler.CHANNEL.sendToServer(new PacketRewardSelector(rewardField.getText()));
					rewardName = rewardField.getText();
					player.closeScreen();
				}
				else
				{
					rewardField.setText("Invalid Name!");
					rewardName = "";
				}
			}
		});
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.mc.getTextureManager().bindTexture(guiTextures);
		this.drawTexturedModalRect((this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		super.render(mouseX, mouseY, partialTicks);
		this.rewardField.drawTextField(mouseX, mouseY, partialTicks);
	}
}