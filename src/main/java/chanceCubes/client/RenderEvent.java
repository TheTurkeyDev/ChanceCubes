package chanceCubes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

public class RenderEvent
{
	private static boolean islookingAt = false;
	private static int chance = -201;
	private static int chanceIncrease = 0;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderGameOverlayEvent.Post event)
	{
		if(event.type != ElementType.HELMET || event.isCancelable())
		{
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();

		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();

		FontRenderer fontrenderer = mc.fontRendererObj;

		GL11.glPushMatrix();

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		if(islookingAt)
		{
			if(chance == -201)
			{
				fontrenderer.drawString("The chance of this cube is: Desctruction... Probably", (k / 2) - 80, (l / 2) - 30, 16777215);
			}
			else
			{
				fontrenderer.drawString("The chance of this cube is: " + chance, (k / 2) - 80, (l / 2) - 30, 16777215);
				if(chanceIncrease != 0)
				{
					int c = chance + chanceIncrease;
					fontrenderer.drawString("Chance with pendants is: " + (c > 100 ? 100 : c < -100 ? -100 : c), (k / 2) - 80, (l / 2) - 15, 16777215);
				}
			}
		}

		GL11.glPopMatrix();
	}

	public static void setLookingAtChance(int c)
	{
		chance = c;
	}

	public static void setLookingAt(boolean lookingAt)
	{
		islookingAt = lookingAt;
	}

	public static void setChanceIncrease(int increase)
	{
		chanceIncrease = increase;
	}
}