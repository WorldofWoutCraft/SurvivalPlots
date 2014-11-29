package com.woutwoot.svp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * Configuration class that uses .properties file format to store various settings and data.
 * 
 * @author woutwoot
 */
public class WootConfig {

	private File configFile;
	private Properties props = new Properties();

	/**
	 * @param configFile
	 */
	public WootConfig(File configFile) {
		this.setConfigFile(configFile);
	}

	/**
	 * Loads the actual config file in memory.
	 * 
	 * @throws java.io.IOException
	 */
	public void loadFile() throws IOException {
		if (configFile.exists()) {
			FileReader reader = new FileReader(configFile);
			props.load(reader);
			reader.close();
		}
	}

	/**
	 * Saves the settings currently in memory to the disk, in the specified file.
	 * 
	 * @throws java.io.IOException
	 * @param String
	 *            comment, what you want the config file to say on the first line.
	 */
	public void saveFile(String comment) throws IOException {
		FileWriter writer = new FileWriter(configFile);
		props.store(writer, comment);
		writer.close();
	}

	/**
	 * @return the configFile
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * @param configFile
	 *            the configFile to set
	 */
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param bool
	 * @return
	 */
	public boolean setBoolean(String path, boolean bool) {
		if (bool) {
			props.setProperty(path, "true");
		} else {
			props.setProperty(path, "false");
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public boolean getBoolean(String path) {
		if (props.getProperty(path) != null && props.getProperty(path).equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param l
	 * @return
	 */
	public boolean setLocation(String path, Location l) {
		if(l == null){
			return false;
		}
		props.setProperty(path, l.getX() + ";" + l.getY() + ";" + l.getZ() + ";" + l.getWorld().getName());
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public Location getLocation(String path) {
		String s = props.getProperty(path);
		if (s != null) {
			Location loc = new Location(Bukkit.getServer().getWorld(s.split(";")[3]), Double.parseDouble(s.split(";")[0]), Double.parseDouble(s.split(";")[1]), Double.parseDouble(s.split(";")[2]));
			return loc;
		}
		return null;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param string
	 * @return
	 */
	public boolean setString(String path, String string) {
		if(string == null){
			return false;
		}
		props.setProperty(path, string);
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public String getString(String path) {
		return props.getProperty(path);
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param integer
	 * @return
	 */
	public boolean setInteger(String path, int integer) {
		props.setProperty(path, integer + "");
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public int getInteger(String path) {
		if (props.getProperty(path) == null) {
			return 0;
		}
		return Integer.parseInt(props.getProperty(path));
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param doub
	 * @return
	 */
	public boolean setDouble(String path, double doub) {
		props.setProperty(path, doub + "");
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public double getDouble(String path) {
		if (props.getProperty(path) == null) {
			return Double.MIN_VALUE;
		}
		return Double.parseDouble(props.getProperty(path));
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param strings
	 * @return
	 */
	public boolean setStringList(String path, List<String> strings) {
		for (int i = 0; i < strings.size(); i++) {
			this.setString(path + i, strings.get(i));
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public List<String> getStringList(String path) {
		List<String> strings = new ArrayList<String>();
		boolean more = true;
		for (int i = 0; more; i++) {
			if (props.containsKey(path + i)) {
				strings.add(this.getString(path + i));
			} else {
				more = false;
			}
		}
		return strings;
	}

	/**
	 * @param map
	 *            The map to be saved to the config. Warning: Entries that already exist will be overridden!
	 * @return
	 */
	public boolean setStringMap(Map<String, String> map) {
		props.putAll(map);
		return true;
	}

	/**
	 * @return HashMap containing all the keys and values currently stored in the config.
	 */
	public Map<String, String> getStringMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (Object key : props.keySet().toArray()) {
			map.put((String) key, (String) props.get(key));
		}
		return map;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param strings
	 * @return
	 */
	public boolean setStringArray(String path, String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			this.setString(path + i, strings[i]);
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public String[] getStringArray(String path) {
		List<String> strings = new ArrayList<String>();
		boolean more = true;
		for (int i = 0; more; i++) {
			if (props.containsKey(path + i)) {
				strings.add(props.getProperty(path + i));
			} else {
				more = false;
			}
		}
		String[] stringarr = new String[strings.size()];
		for (int j = 0; j < strings.size(); j++) {
			stringarr[j] = strings.get(j);
		}
		return stringarr;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param locations
	 * @return
	 */
	public boolean setLocationList(String path, List<Location> locations) {
		for (int i = 0; i < locations.size(); i++) {
			this.setLocation(path + i, locations.get(i));
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return
	 */
	public List<Location> getLocationList(String path) {
		List<Location> locs = new ArrayList<Location>();
		boolean more = true;
		for (int i = 0; more; i++) {
			if (props.containsKey(path + i)) {
				locs.add(this.getLocation(path + i));
			} else {
				more = false;
			}
		}
		return locs;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param items
	 * @return
	 */
	public boolean setItemStackArray(String path, ItemStack[] items) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(items.length);
			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}
			dataOutput.close();
			props.setProperty(path, Base64Coder.encodeLines(outputStream.toByteArray()));
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param items
	 * @return
	 */
	public boolean setItemStackList(String path, List<ItemStack> items) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(items.size());
			for (int i = 0; i < items.size(); i++) {
				dataOutput.writeObject(items.get(i));
			}
			dataOutput.close();
			props.setProperty(path, Base64Coder.encodeLines(outputStream.toByteArray()));
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return List<ItemStack>
	 * @throws java.io.IOException
	 */
	public List<ItemStack> getItemStackList(String path) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(props.getProperty(path)));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			List<ItemStack> items = new ArrayList<ItemStack>();
			boolean more = true;
			for (int i = 0; more; i++) {
				if (props.containsKey(path + i)) {
					items.add((ItemStack) dataInput.readObject());
				} else {
					more = false;
				}
			}
			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return ItemStack[]
	 * @throws java.io.IOException
	 */
	public ItemStack[] getItemStackArray(String path) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(props.getProperty(path)));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			List<ItemStack> items = new ArrayList<ItemStack>();
			boolean more = true;
			for (int i = 0; more; i++) {
				if (props.containsKey(path + i)) {
					items.add((ItemStack) dataInput.readObject());
				} else {
					more = false;
				}
			}
			dataInput.close();
			ItemStack[] isarr = new ItemStack[items.size()];
			for (int j = 0; j < items.size(); j++) {
				isarr[j] = items.get(j);
			}
			return isarr;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @param inventory
	 * @return boolean
	 * @throws IllegalStateException
	 */
	public boolean setInventory(String path, Inventory inventory) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(inventory.getSize());
			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}
			dataOutput.close();
			props.setProperty(path, Base64Coder.encodeLines(outputStream.toByteArray()));
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
		return true;
	}

	/**
	 * @param path
	 *            The path in the configuration file where the value can be found/saved.
	 * @return Inventory
	 * @throws java.io.IOException
	 */
	public Inventory getInventory(String path) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(props.getProperty(path)));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}
			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public String toString() {
		return this.props.toString();
	}

}
