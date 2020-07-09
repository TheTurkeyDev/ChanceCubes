package chanceCubes.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.net.URI;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.HoverEvent;
import static net.minecraft.util.text.event.HoverEvent.Action.*;

public class ProfileGui extends Screen implements IGuiEventListener
{
	private ProfilesList profileList;

	private String hoverText;

	private String rewardsInfoUrlText = "Click here to see all the rewards and info about them";

	public ProfileGui()
	{
		super(new StringTextComponent("Profiles"));
	}

	@Override
	public void init()
	{
		this.profileList = new ProfilesList(this, super.minecraft, this.width, this.height, 64, this.height - 32, 20);
		this.addButton(new Button(this.width / 2 - 36, this.height - 28, 72, 20, new StringTextComponent("Save"), (button) ->
		{

		}));
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
	{
		hoverText = "";
		this.profileList.render(stack, mouseX, mouseY, partialTicks);
		this.drawCenteredString(stack, this.font, "Disclaimer: In developement! Does not work on servers!", this.width / 2, 6, 0xFF0000);
		this.drawCenteredString(stack, this.font, "Profiles", this.width / 2, 20, 16777215);
		this.drawCenteredString(stack, this.font, "Hover over the profile name to see a description", this.width / 2, 34, 16777215);
		this.drawCenteredString(stack, this.font, rewardsInfoUrlText, this.width / 2, 48, 0x00FF00);
		super.render(stack, mouseX, mouseY, partialTicks);
		if(!hoverText.isEmpty())
			this.renderComponentHoverEffect(stack, Style.EMPTY.setHoverEvent(new HoverEvent(SHOW_TEXT, new StringTextComponent(hoverText))), mouseX, mouseY);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		int textWidth = this.font.getStringWidth(rewardsInfoUrlText);
		int x = (this.width / 2) - textWidth / 2;
		if(mouseButton == 0 && mouseX > x && mouseX < x + textWidth && mouseY > 48 && mouseY < 60)
		{
			try
			{
				Class<?> oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop").invoke(null);
				oclass.getMethod("browse", URI.class).invoke(object, new URI("https://github.com/Turkey2349/ChanceCubes/wiki/Chance-Cubes-Rewards-Evolution"));
				return true;
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return this.profileList.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Called when a mouse button is released.
	 *
	 * @return something...
	 */
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state)
	{
		this.profileList.mouseReleased(mouseX, mouseY, state);
		return super.mouseReleased(mouseX, mouseY, state);
	}

	public void setHoverText(String text)
	{
		this.hoverText = text;
	}
}