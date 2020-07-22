package net.ddns.volkalex.anarchyPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.Bukkit;

public final class DataStorHandler {

	private final static String SystemSeparator = File.separator;
	private final static String WorldFolderPATH = Bukkit.getWorlds().get(0).getWorldFolder().getAbsolutePath();
	private final static String FileName = "AnarhyPluginPlayersList";

	private static HashMap<String, Long> PLAYERS = new HashMap<String, Long>();

	public static boolean isPlayerKnown(String playerName) {
		return PLAYERS.containsKey(playerName);
	}

	public static void addPlayerToKnown(String playerName) {
		PLAYERS.put(playerName, 1L);

	}

	public static boolean isDead(String playerName) {
		return (PLAYERS.get(playerName) > System.currentTimeMillis());
	}

	public static long getEndDate(String playerName) {
		return PLAYERS.get(playerName);
	}

	public static void setPlayerReturnDate(String name, long l) {
		PLAYERS.replace(name, l);
		saveToFile();
	}

	public static void loadFromFile() {
		Object obj = importObjectFromFile(new File(WorldFolderPATH + SystemSeparator + FileName));
		if (obj instanceof HashMap) {
			HashMap<Object, Object> hashMap = (HashMap<Object, Object>) obj;
			if ((hashMap.keySet().iterator().next() instanceof String)
					&& (hashMap.get(hashMap.keySet().iterator().next()) instanceof Long)) {
				PLAYERS = (HashMap<String, Long>) obj;
			}
		}
	}

	public static void saveToFile() {
		writeObjectToFile(WorldFolderPATH, FileName, PLAYERS, false);
	}

// ---------------------------------------- ���� ����� ������� ������/������ ������ --------------------------------------- //

	/**
	 * ������� ���������� ������ � ����, �� �� ���� �� ���� �� ����� (�����: ����
	 * ���� ��� �����, � ���� �������, �� ���������� � �������� �������).
	 * 
	 * @param dir            ���� � ����������, � ������� ����� ��������� ���� �
	 *                       ��������
	 * @param fileName       ��� �����, ���� ����� ������� ������
	 * @param obj            ������, ������� ����� ���������
	 * @param successMessage ���� <b>true</b>, �� � ������ ������ ������� � �������
	 *                       ��������� �� �������� ������ ������� � ����
	 * @return ������ <b>true</b>, ���� ������ <b>obj</b> ��� ������� ������� � ����
	 *         <b>fileName</b> � ���������� <b>dir</b>. ���� ���������� ������
	 *         ����������� <b>�����</b> � �� ����������� ���������� �������
	 *         {@link ObjectOutputStream#writeObject(Object) writeObject(obj)}, �
	 *         ������ �� ����� ����, ������ ��� ������ {@link IOException} ��� ���.
	 */

	private static boolean writeObjectToFile(String dir, String fileName, Object obj, boolean successMessage) {

		try {

			// ���� ����� �� ����������, �� ������� ��
			File file = new File(dir);
			if (!file.exists())
				file.mkdirs();

			// ���� ���� �� ����������, �� ������� ���
			file = new File(dir + SystemSeparator + fileName);
			if (!file.exists())
				if (!file.createNewFile()) {
					Bukkit.getServer().getConsoleSender()
							.sendMessage("File " + fileName + " does not exist and cant be created");
					return false;
				} else
					Bukkit.getServer().getConsoleSender().sendMessage("File " + fileName + " successfully created");
			if (!file.canWrite()) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage("File " + fileName + " does no have permission to write");
				return false;
			}

			// ������� ����� ������ �������
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(obj);
			objectOut.flush();
			objectOut.close();
			fileOut.close();

		} catch (IOException ex) {
			Bukkit.getServer().getConsoleSender().sendMessage("File " + fileName + " not found");
			ex.printStackTrace();
			return false;
		}

		if (successMessage)
			Bukkit.getConsoleSender().sendMessage("File " + fileName + " successfully saved");
		return true;
	}

	/**
	 * �������� ������� �� ���������� ����� ������, ����, ��� ����, ����� ������ ���
	 * ������ <b>catch (</b>{@link Exception}<b> e)</b>.<br>
	 * ���� ���� �� ����������, ������� ��������� �� ������ � ������� � ������ null
	 * � ������ ������ ������ {@link Exception#printStackTrace()
	 * e.printStackTrace()} � ������ <b>null</b>.<br>
	 * <br>
	 * �/� � ����, ��� ������������ ���������� ���������� �� �� �������, �� ��� ����
	 * 
	 * @param file ������ ���� {@link File}, ������� �������� � ���� ���� � ����� �
	 *             ������������� ��������.
	 * @return ���� ������ ����� ������ ������� ���������� {@link Object} ����������
	 *         �� ����� ��� ������ {@link ObjectInputStream#readObject()
	 *         readObject()}, ���� <b>null</b> � ������ ��������� ������
	 */

	private static Object importObjectFromFile(File file) {

		Object obj = null;

		if (!file.exists()) {
			Bukkit.getServer().getConsoleSender().sendMessage("System cant finde file " + file);
			return null;
		}

		try {

			// ������� ����� ������� ������� �� ����� � ������ ������ �� �����
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			obj = objectIn.readObject();
			objectIn.close();
			fileIn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return obj;
	}

}
