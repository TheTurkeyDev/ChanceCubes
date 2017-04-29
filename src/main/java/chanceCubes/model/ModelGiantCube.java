package chanceCubes.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelGiantCube extends ModelBase
{
	public ModelRenderer block;

	public ModelGiantCube()
	{
		block = new ModelRenderer(this, 0, 0);
		block.addBox(-8F, -8F, -8F, 16, 16, 16);
		block.setRotationPoint(0F, 0F, 0F);
		block.setTextureSize(256, 128);
		block.mirror = true;
	}

	public void renderAll()
	{
		this.block.render(0.0625F);
	}
}