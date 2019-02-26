package chanceCubes.client.gui;

import chanceCubes.CCubesCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class CCubesGuiHandler
{
	public enum CCGUIList
	{
		CREATIVE_PENDANT(new ResourceLocation(CCubesCore.MODID, "creative_pendent"));

		public final ResourceLocation rl;

		CCGUIList(ResourceLocation rl)
		{
			this.rl = rl;
		}
	}

	public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer)
	{
		if(openContainer.getId().equals(CCGUIList.CREATIVE_PENDANT.rl))
			return new CreativePendantGui(Minecraft.getInstance().player, Minecraft.getInstance().world);
		return null;
	}

}
