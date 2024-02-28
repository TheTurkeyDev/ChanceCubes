package chanceCubes.client.listeners;

import chanceCubes.util.SchematicUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.bus.api.SubscribeEvent;

public class RenderEvent
{
	private static boolean islookingAt = false;
	private static boolean creatingSchematic = false;
	private static int chance = -201;
	private static int chanceIncrease = 0;

	@SubscribeEvent()
	@OnlyIn(Dist.CLIENT)
	public void onGuiRender(RenderGuiOverlayEvent.Post event)
	{
		if (event.getOverlay().id() != VanillaGuiOverlay.TITLE_TEXT.id())
			return;


		int k = event.getWindow().getGuiScaledWidth();
		int l = event.getWindow().getGuiScaledHeight();

		Font fontRenderer = Minecraft.getInstance().font;

		GuiGraphics guiGraphics = event.getGuiGraphics();
		PoseStack matrixStack = guiGraphics.pose();

		if(islookingAt)
		{
			matrixStack.pushPose();
			//TODO
			//GlStateManager.disableLighting();
			//GlStateManager.color4f(1F, 1F, 1F, 1F);
			if(chance == -201)
			{
				guiGraphics.drawString(fontRenderer, "The chance of this cube is: Destruction... Probably", (k / 2f) - 80, (l / 2f) - 30, 16777215, false);
			}
			else
			{
				guiGraphics.drawString(fontRenderer, "The chance of this cube is: " + chance, (k / 2f) - 80, (l / 2f) - 30, 16777215, false);
				if(chanceIncrease != 0)
				{
					int c = chance + chanceIncrease;
					guiGraphics.drawString(fontRenderer, "Chance with pendants is: " + Math.min(100, Math.max(c, -100)), (k / 2f) - 80, (l / 2f) - 15, 16777215, false);
				}
			}
			//TODO
			//GlStateManager.enableLighting();
			matrixStack.popPose();
		}
		if(creatingSchematic)
		{
			matrixStack.pushPose();
			//TODO
			//GlStateManager.disableLighting();
			//GlStateManager.color4f(1F, 1F, 1F, 1F);

			String text1 = "--- Creating A Chance Cube Schematic ---";
			String text2 = "Right or left click a block or air to set positions.";
			String text3 = "/ChanceCubes schematic create to continue";
			String text4 = "/ChanceCubes schematic cancel to cancel";
			String text5 = "Point 1";
			String text6 = "Point 2";

			guiGraphics.drawString(fontRenderer, text1, (k / 2f) - (fontRenderer.width(text1) / 2f), 10, 0xFFFFFF, false);
			guiGraphics.drawString(fontRenderer, text2, (k / 2f) - (fontRenderer.width(text2) / 2f), 20, 0xFFFFFF, false);
			guiGraphics.drawString(fontRenderer, text3, (k / 2f) - (fontRenderer.width(text3) / 2f), 30, 0xFFFFFF, false);
			guiGraphics.drawString(fontRenderer, text4, (k / 2f) - (fontRenderer.width(text4) / 2f), 40, 0xFFFFFF, false);
			guiGraphics.drawString(fontRenderer, text5, (k / 2f) - (fontRenderer.width(text5) / 2f), 60, SchematicUtil.selectionPoints[0] == null ? 0xFF0000 : 0x00FF00, false);
			guiGraphics.drawString(fontRenderer, text6, (k / 2f) - (fontRenderer.width(text6) / 2f), 70, SchematicUtil.selectionPoints[1] == null ? 0xFF0000 : 0x00FF00, false);

			//TODO
			//GlStateManager.enableLighting();
			matrixStack.popPose();
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