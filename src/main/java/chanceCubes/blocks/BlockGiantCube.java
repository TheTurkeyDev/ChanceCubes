package chanceCubes.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import chanceCubes.CCubesCore;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGiantCube extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private IIcon[] sideIcons;

	public BlockGiantCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
		this.setBlockName("Giant_Chance_Cube");
		this.setBlockTextureName("chancecubes:chanceCube");
		this.setLightLevel(2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileGiantCube();
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileGiantCube te = (TileGiantCube) world.getTileEntity(x, y, z);

			if(te != null)
			{
				player.addChatMessage(new ChatComponentText("The Giant Cube and rewards are currently In developement"));
				player.addChatMessage(new ChatComponentText("Please let me know what you think of the idea and leave sugestions!"));
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, te.getMasterX(), te.getMasterY(), te.getMasterZ(), player, 0);
				GiantCubeUtil.removeStructure(te.getMasterX(), te.getMasterY(), te.getMasterZ(), world);
			}
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		this.icons = new IIcon[16];
		this.icons[0] = register.registerIcon(CCubesCore.MODID + ":GCMMND");
		this.icons[1] = register.registerIcon(CCubesCore.MODID + ":GCULD");
		this.icons[2] = register.registerIcon(CCubesCore.MODID + ":GCULND");
		this.icons[3] = register.registerIcon(CCubesCore.MODID + ":GCUM");
		this.icons[4] = register.registerIcon(CCubesCore.MODID + ":GCURD");
		this.icons[5] = register.registerIcon(CCubesCore.MODID + ":GCURND");
		this.icons[6] = register.registerIcon(CCubesCore.MODID + ":GCMLD");
		this.icons[7] = register.registerIcon(CCubesCore.MODID + ":GCMLND");
		this.icons[8] = register.registerIcon(CCubesCore.MODID + ":GCMMD");
		this.icons[9] = register.registerIcon(CCubesCore.MODID + ":GCMRD");
		this.icons[10] = register.registerIcon(CCubesCore.MODID + ":GCMRND");
		this.icons[11] = register.registerIcon(CCubesCore.MODID + ":GCBLD");
		this.icons[12] = register.registerIcon(CCubesCore.MODID + ":GCBLND");
		this.icons[13] = register.registerIcon(CCubesCore.MODID + ":GCBM");
		this.icons[14] = register.registerIcon(CCubesCore.MODID + ":GCBRD");
		this.icons[15] = register.registerIcon(CCubesCore.MODID + ":GCBRND");

		this.sideIcons = new IIcon[6];
		for(int i = 0; i < 6; i++)
			this.sideIcons[i] = this.icons[0];
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.sideIcons[side];
	}

	public void setSideIcon(int side, int icon)
	{
		this.sideIcons[side] = this.icons[icon];
	}
}