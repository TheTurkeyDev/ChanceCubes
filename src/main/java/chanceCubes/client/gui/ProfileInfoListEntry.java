package chanceCubes.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;

public class ProfileInfoListEntry extends AbstractList.AbstractListEntry<ProfileInfoListEntry>
{
	private String entryString;
	private Minecraft mc;

	public ProfileInfoListEntry(Minecraft mcIn, String entryString)
	{
		this.entryString = entryString;
		this.mc = mcIn;
	}

	@Override
	public void render(MatrixStack stack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
	{
		mc.fontRenderer.drawStringWithShadow(stack, entryString, left, top, 16777215);
	}
}