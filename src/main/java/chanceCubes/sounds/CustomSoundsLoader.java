package chanceCubes.sounds;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import chanceCubes.util.FileUtil;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.resource.IResourceType;

/**
 * Code Referenced and sourced from the EnderCore and CustomThings mods. All referenced sources and code belong to their original authors and is used with their permission. View the
 */

public class CustomSoundsLoader
{
	private File folder;
	private File dir;
	private File zip;
	private String name;
	private String mcmeta;
	private List<CustomFile> customsSounds = new ArrayList<CustomFile>();
	private static List<IResourceType> defaultResourcePacks;

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public CustomSoundsLoader(File folder, File directory, String packName)
	{
		this.folder = folder;
		this.dir = directory;
		this.zip = new File(dir.getAbsolutePath() + ".zip");
		this.name = packName;
		this.mcmeta = String.format("{\"pack\":{\"pack_format\":1,\"description\":\"%s\"}}", this.name);
	}

	public void addCustomSounds()
	{
		JsonObject root = new JsonObject();
		for(File f : new File(folder.getAbsolutePath() + "/Sounds").listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".ogg")))
		{
			String simpleName = f.getName().substring(0, f.getName().indexOf('.'));
			customsSounds.add(new CustomFile("assets/minecraft/sounds", f));// add record .ogg
			JsonObject event = new JsonObject();
			event.addProperty("category", "block"); // put under the "record" category for sound options
			JsonArray sounds = new JsonArray(); // array of sounds (will only ever be one)
			JsonObject soundsObj = new JsonObject();
			soundsObj.addProperty("name", simpleName);
			soundsObj.addProperty("stream", false);// sound object (instead of primitive to use 'stream' flag)
			sounds.add(soundsObj);
			event.add("sounds", sounds);
			root.add(simpleName, event); // event name (same as name sent to ItemCustomRecord)
		}
		customsSounds.add(new CustomFile("assets/minecraft", FileUtil.writeToFile(folder.getAbsolutePath() + "/Sounds/sounds.json", gson.toJson(root))));// add record .ogg
	}

	public void assemble()
	{

		FileUtil.safeDeleteDirectory(dir);
		dir.mkdirs();

		String pathToDir = dir.getAbsolutePath();
		File metaFile = new File(pathToDir + "/pack.mcmeta");

		try
		{
			FileUtil.writeNewFile(metaFile, mcmeta);

			for(CustomFile custom : customsSounds)
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

		ForgeHooksClient.refreshResources();
		if(ForgeHooksClient.getSide().isClient())
		{
//			try
//			{
//				if(defaultResourcePacks == null)
//					defaultResourcePacks = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao", "ap");
//
//				File dest = new File(dir.getParent() + "/Resourcepack/" + zip.getName());
//				FileUtil.safeDelete(dest);
//				FileUtils.copyFile(zip, dest);
//				FileUtil.safeDelete(zip);
//				FileUtil.writeNewFile(new File(dest.getParent() + "/readme.txt"), "This is the resource pack for loading in custom sounds to chance cubes. Feel free to ignore this file and folder.");
//				defaultResourcePacks.add(new FileResourcePack(dest));
//				
//				Minecraft.getMinecraft().refreshResources();
//			} catch(Exception e)
//			{
//				CCubesCore.logger.error("Failed to inject the resource pack for the custom sounds in the Chance Cubes rewards: ", e);
//			}
		}
	}

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
}