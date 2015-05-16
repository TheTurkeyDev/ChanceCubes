package chanceCubes.renderer;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import chanceCubes.tileentities.TileChanceD20;

public class TileChanceD20Renderer extends TileEntitySpecialRenderer
{
	private IModelCustom model;
	private ResourceLocation texture;
	
	private float baseSpinSpd = 4F;
	private float baseColorSpd = 50F;
	private float hvrSpd = 12F;
	
	public TileChanceD20Renderer()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("chancecubes", "models/blocks/d20.obj"));
		texture = new ResourceLocation("chancecubes", "textures/models/d20.png");
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTick)
	{
		int stage = ((TileChanceD20)tileEntity).getStage();
		
		float spinSpd = baseSpinSpd + (stage/4f);
		float colorSpd = baseColorSpd - (stage/5f);
		
		float wave = stage == 0 ? MathHelper.sin((tileEntity.getWorldObj().getTotalWorldTime()%(hvrSpd * 1000F) + partialTick)/(hvrSpd * 1000F) * 360F) : (stage/10f);
		float spin = (tileEntity.getWorldObj().getTotalWorldTime()*spinSpd)%360 + partialTick;
		float color = (tileEntity.getWorldObj().getTotalWorldTime()%colorSpd + partialTick)/colorSpd;
		
		GL11.glPushMatrix();
		
        GL11.glTranslated(posX + 0.5F, posY + 0.5F + wave*0.1F, posZ + 0.5F);
        GL11.glRotatef(spin, 0F, 1F, 0F);
        Color tmpClr = new Color(Color.HSBtoRGB(color, 1F, 1F));
        GL11.glColor3f(tmpClr.getRed()/255F, tmpClr.getGreen()/255F, tmpClr.getBlue()/255F);
        
    	Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.renderAll();
        
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        
        GL11.glTranslated(posX + 0.5F, posY + 1.5F + wave*0.1F, posZ + 2.5F);
        Tessellator tessellator = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();
        float f1 = ((float)tileEntity.getWorldObj().getTotalWorldTime()%750 + partialTick) / 750.0F;
        float f2 = 0F;

        Random random = new Random(432L);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -1.0F, -2.0F);

        for (int i = 0; i < (16 + (stage/10)); ++i)
        {
            GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
            tessellator.startDrawing(6);
            float f3 = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
            
            f3 *= 0.1F;
            f4 *= 0.1F;
            
            tessellator.setColorRGBA_I(tmpClr.getRGB(), (int)(255.0F * (1.0F - f2)));
            tessellator.addVertex(0.0D, 0.0D, 0.0D);
            tessellator.setColorRGBA_I(tmpClr.getRGB(), 0);
            tessellator.addVertex(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
            tessellator.addVertex(0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
            tessellator.addVertex(0.0D, (double)f3, (double)(1.0F * f4));
            tessellator.addVertex(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
            tessellator.draw();
        }

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.enableStandardItemLighting();
        
        GL11.glPopMatrix();
	}
}
