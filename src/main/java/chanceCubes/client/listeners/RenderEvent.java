package chanceCubes.client.listeners;

import chanceCubes.util.SchematicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderEvent
{
	private static boolean islookingAt = false;
	private static boolean creatingSchematic = false;
	private static int chance = -201;
	private static int chanceIncrease = 0;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() != ElementType.HELMET || event.isCancelable())
			return;

		Minecraft mc = Minecraft.getMinecraft();

		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();

		FontRenderer fontrenderer = mc.fontRendererObj;

		if(islookingAt)
		{
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.color(1F, 1F, 1F, 1F);
			if(chance == -201)
			{
				fontrenderer.drawString("The chance of this cube is: Destruction... Probably", (k / 2) - 80, (l / 2) - 30, 16777215);
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
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		if(creatingSchematic)
		{
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.color(1F, 1F, 1F, 1F);

			String text1 = "--- Creating A Chance Cube Schematic ---";
			String text2 = "Right or left click a block or air to set positions.";
			String text3 = "/ChanceCubes schematic create to continue";
			String text4 = "/ChanceCubes schematic cancel to cancel";
			String text5 = "Point 1";
			String text6 = "Point 2";

			fontrenderer.drawString(text1, (k / 2) - (fontrenderer.getStringWidth(text1) / 2), 10, 0xFFFFFF);
			fontrenderer.drawString(text2, (k / 2) - (fontrenderer.getStringWidth(text2) / 2), 20, 0xFFFFFF);
			fontrenderer.drawString(text3, (k / 2) - (fontrenderer.getStringWidth(text3) / 2), 30, 0xFFFFFF);
			fontrenderer.drawString(text4, (k / 2) - (fontrenderer.getStringWidth(text4) / 2), 40, 0xFFFFFF);
			fontrenderer.drawString(text5, (k / 2) - (fontrenderer.getStringWidth(text5) / 2), 60, SchematicUtil.selectionPoints[0] == null ? 0xFF0000 : 0x00FF00);
			fontrenderer.drawString(text6, (k / 2) - (fontrenderer.getStringWidth(text6) / 2), 70, SchematicUtil.selectionPoints[1] == null ? 0xFF0000 : 0x00FF00);

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
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

	public static void setCreatingSchematic(boolean isCreating)
	{
		creatingSchematic = isCreating;
	}

	public static boolean isCreatingSchematic()
	{
		return creatingSchematic;
	}
}