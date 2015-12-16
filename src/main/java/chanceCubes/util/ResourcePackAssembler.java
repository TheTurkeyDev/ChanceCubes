package chanceCubes.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.commons.io.FileUtils;

import chanceCubes.CCubesCore;

public class ResourcePackAssembler
{
	/**
	 * Code Referenced from the EnderCore api See here for more https://github.com/SleepyTrousers/EnderCore/blob/master/src/main/java/com/enderio/core/common/util/ResourcePackAssembler.java
	 *
	 */
	private class CustomFile
	{
		private String ext;
		private File file;

		private CustomFile(String ext, File file)
		{
			this.ext = ext;
			this.file = file;
		}
	}

	private List<CustomFile> customs = new ArrayList<CustomFile>();

	private static List<IResourcePack> defaultResourcePacks;

	private static final String MC_META_BASE = "{\"pack\":{\"pack_format\":1,\"description\":\"%s\"}}";

	private File dir;
	private File zip;
	private String name;
	private String mcmeta;
	private String modid;

	public ResourcePackAssembler(File directory, String packName, String modid)
	{
		this.dir = directory;
		this.zip = new File(dir.getAbsolutePath() + ".zip");
		this.name = packName;
		this.modid = modid.toLowerCase(Locale.US);
		this.mcmeta = String.format(MC_META_BASE, this.name);
	}

	public void addCustomFile(String path, File file)
	{
		customs.add(new CustomFile(path, file));
	}

	public void addCustomFile(File file)
	{
		addCustomFile(null, file);
	}

	public ResourcePackAssembler assemble()
	{
		FileUtil.safeDeleteDirectory(dir);
		dir.mkdirs();

		String pathToDir = dir.getAbsolutePath();
		File metaFile = new File(pathToDir + "/pack.mcmeta");

		try
		{
			writeNewFile(metaFile, mcmeta);

			for(CustomFile custom : customs)
			{
				File directory = new File(pathToDir + (custom.ext != null ? "/" + custom.ext : ""));
				directory.mkdirs();
				FileUtils.copyFile(custom.file, new File(directory.getAbsolutePath() + "/" + custom.file.getName()));
			}

			FileUtil.zipFolderContents(dir, zip);
			FileUtil.safeDeleteDirectory(dir);
		} catch(IOException e)
		{
			throw new RuntimeException(e);
		}

		return this;
	}

	public void inject()
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			try
			{
				if(defaultResourcePacks == null)
				{
					defaultResourcePacks = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao", "ap");
				}

				File dest = new File(dir.getParent() + "/resourcepack/" + zip.getName());
				FileUtil.safeDelete(dest);
				FileUtils.copyFile(zip, dest);
				FileUtil.safeDelete(zip);
				writeNewFile(new File(dest.getParent() + "/readme.txt"), this.localize("resourcepack.readme") + "\n\n" + this.localize("resourcepack.readme2"));
				defaultResourcePacks.add(new FileResourcePack(dest));
			} catch(Exception e)
			{
				CCubesCore.logger.error("Failed to inject resource pack for mod {}", modid, e);
			}
		}
		else
		{
			CCubesCore.logger.info("Skipping resource pack, we are on a dedicated server.");
		}
	}

	private void writeNewFile(File file, String defaultText) throws IOException
	{
		FileUtil.safeDelete(file);
		file.delete();
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileWriter fw = new FileWriter(file);
		fw.write(defaultText);
		fw.flush();
		fw.close();
	}

	public String localize(String unloc)
	{
		return localizeExact(addPrefix(unloc));
	}

	public String addPrefix(String suffix)
	{
		return CCubesCore.MODID.concat(".").concat(suffix);
	}

	public String localizeExact(String unloc)
	{
		return StatCollector.translateToLocal(unloc);
	}
}
