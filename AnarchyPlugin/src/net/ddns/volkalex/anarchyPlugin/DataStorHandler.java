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

// ---------------------------------------- БЛОК ОБЩИХ ФУНКЦИЙ ЧТЕНИЯ/ЗАПИСИ ФАЙЛОВ --------------------------------------- //

	/**
	 * Функция записывает объект в файл, да же если он того не хочет (читай: если
	 * файл или папки, к нему ведущие, не существуют в файловой системе).
	 * 
	 * @param dir            Путь к директории, в которой будет храниться файл с
	 *                       объектом
	 * @param fileName       Имя файла, куда будет записан объект
	 * @param obj            Объект, который хотим сохранить
	 * @param successMessage Если <b>true</b>, то в случае успеха выведет в консоль
	 *                       сообщение об успешной записи объекта в файл
	 * @return Вернет <b>true</b>, если объект <b>obj</b> был успешно записан в файл
	 *         <b>fileName</b> в директории <b>dir</b>. Сама успешность записи
	 *         проверяется <b>никак</b> и по результатом выполнения функции
	 *         {@link ObjectOutputStream#writeObject(Object) writeObject(obj)}, а
	 *         точнее по факту того, выдала она ошибку {@link IOException} или нет.
	 */

	private static boolean writeObjectToFile(String dir, String fileName, Object obj, boolean successMessage) {

		try {

			// Если папка не существует, то создать ее
			File file = new File(dir);
			if (!file.exists())
				file.mkdirs();

			// Если файл не существует, то создать его
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

			// Создаем поток вывода объекта
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
	 * Пытается извлечь из указанного файла объект, ловя, при этом, любые ошибки при
	 * помощи <b>catch (</b>{@link Exception}<b> e)</b>.<br>
	 * Если файл не существует, выведет сообщение об ошибке в консоль и вернет null
	 * В случае ошибки дернет {@link Exception#printStackTrace()
	 * e.printStackTrace()} и вернет <b>null</b>.<br>
	 * <br>
	 * З/Ы Я знаю, что обрабатывать исключения желательно бы по другому, но мне лень
	 * 
	 * @param file Объект типа {@link File}, который содержит в себе путь к файлу с
	 *             импортируемым объектом.
	 * @return Если чтение файла прошло успешно возвращает {@link Object} вытащенный
	 *         из файла при помощи {@link ObjectInputStream#readObject()
	 *         readObject()}, либо <b>null</b> в случае появления ошибки
	 */

	private static Object importObjectFromFile(File file) {

		Object obj = null;

		if (!file.exists()) {
			Bukkit.getServer().getConsoleSender().sendMessage("System cant finde file " + file);
			return null;
		}

		try {

			// Создаем поток импорта объекта из файла и читаем объект из файла
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
