package chanceCubes.client.renderType;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class LineRenderType extends RenderType
{
	public LineRenderType(String nameIn, VertexFormat formatIn, Mode drawMode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
	{
		super(nameIn, formatIn, drawMode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType lineRenderType()
	{
		return create("chancecubes:lines_no_depth",
				DefaultVertexFormat.POSITION_COLOR, Mode.LINES, 256, false, false,
				CompositeState.builder()
						.setShaderState(RENDERTYPE_LINES_SHADER)
						.setLineState(new LineStateShard(OptionalDouble.of(2F)))
						.setLayeringState(VIEW_OFFSET_Z_LAYERING)
						.setTransparencyState(NO_TRANSPARENCY)
						.setOutputState(ITEM_ENTITY_TARGET)
						.setWriteMaskState(COLOR_DEPTH_WRITE)
						.setCullState(NO_CULL)
						.setDepthTestState(NO_DEPTH_TEST)
						.createCompositeState(false));
	}
}