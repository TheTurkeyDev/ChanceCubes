package chanceCubes.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import chanceCubes.CCubesCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CreativePendantGui extends GuiScreen
{
	private static final ResourceLocation guiTextures = new ResourceLocation(CCubesCore.MODID + ":textures/gui/container/guiCreativePendant.png");
	/** The player editing the book */
	private final EntityPlayer player;
	private final ItemStack pendant;
	private int changeValue = 0;
	private int imageWidth = 176;
	private int imageHeight = 167;
	
	public CreativePendantGui(EntityPlayer player, ItemStack stack)
	{
		this.player = player;
		this.pendant = stack;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 40, 25 + this.imageHeight, 20, 20, I18n.format("-1", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 15, 25 + this.imageHeight, 20, 20, I18n.format("+1", new Object[0])));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 60, 25 + this.imageHeight, 20, 20, I18n.format("-5", new Object[0])));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 35, 25 + this.imageHeight, 20, 20, I18n.format("+5", new Object[0])));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 80, 25 + this.imageHeight, 20, 20, I18n.format("-10", new Object[0])));
		this.buttonList.add(new GuiButton(5, this.width / 2 + 55, 25 + this.imageHeight, 20, 20, I18n.format("+10", new Object[0])));
	}
	
	private void sendBookToServer()
	{
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				this.changeValue-=1;
			}
			else if (button.id == 1)
			{
				this.changeValue+=1;
			}
			else if (button.id == 2)
			{
				this.changeValue-=5;
			}
			else if (button.id == 3)
			{
				this.changeValue+=5;
			}
			else if (button.id == 4)
			{
				this.changeValue-=10;
			}
			else if (button.id == 5)
			{
				this.changeValue+=10;
			}
		}
		if(this.changeValue > 100)
			this.changeValue = 100;
		if(this.changeValue < -100)
			this.changeValue = -100;
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiTextures);
		int k = (this.width - this.imageWidth) / 2;
		int l = (this.height - this.imageHeight) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.imageWidth, this.imageHeight);
		this.fontRendererObj.drawString("Chance Value", k + this.imageWidth - 120, l + 5, 0);
		String cValue = "" + this.changeValue;
		this.fontRendererObj.drawString(cValue, k + this.imageWidth - (87 + (cValue.length() * 3)), l + 27, 0);
		
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}