package net.ddns.volkalex.anarchyPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class EventListner implements Listener {

	@EventHandler
	public void onPlayerLoginEvent(AsyncPlayerPreLoginEvent event) {
		String playerName = event.getName();

		if (!DataStorHandler.isPlayerKnown(playerName)) {
			DataStorHandler.addPlayerToKnown(playerName);
		}

		if (DataStorHandler.isDead(playerName)) {
			event.disallow(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					"���� ����� ��� �� ������. ������� ����� " + timeLeftFormat(playerName));
		} else {
			event.allow();
		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();

		DataStorHandler.setPlayerReturnDate(player.getName(), System.currentTimeMillis() + 604800000L);
		player.kickPlayer("�� ����. ����. ������� � ������ ���!");

	}

	private String timeLeftFormat(String playerName) {

		long timeLeft = DataStorHandler.getEndDate(playerName) - System.currentTimeMillis();

		long daysLeft = Math.round(Math.ceil(timeLeft / 86400000.0d));

		if (daysLeft > 4)
			return String.valueOf(daysLeft) + " ����.";
		if (daysLeft > 1)
			return String.valueOf(daysLeft) + " ���.";

		long hoursLeft = Math.round(Math.ceil(timeLeft / 3600000.0d));

		if (hoursLeft > 21)
			return String.valueOf(hoursLeft) + " ����.";
		if (hoursLeft > 20)
			return String.valueOf(hoursLeft) + " ���.";
		if (hoursLeft > 4)
			return String.valueOf(hoursLeft) + " �����.";
		if (hoursLeft > 1)
			return String.valueOf(hoursLeft) + " ����.";

		long minutesLeft = Math.round(Math.ceil(timeLeft / 60000.0d));

		if (minutesLeft > 14) {
			long unitMinutesLeft = minutesLeft % 10;
			if (unitMinutesLeft > 4)
				return String.valueOf(minutesLeft) + " �����.";
			if (unitMinutesLeft > 1)
				return String.valueOf(minutesLeft) + " �����.";
			if (unitMinutesLeft > 0)
				return String.valueOf(minutesLeft) + " ������.";
			return String.valueOf(hoursLeft) + " �����.";
		}

		if (minutesLeft > 4)
			return String.valueOf(minutesLeft) + " �����.";
		if (minutesLeft > 1)
			return String.valueOf(minutesLeft) + " �����.";
		if (minutesLeft == 1)
			return String.valueOf(minutesLeft) + " ������.";

		return "0 �����. � �������� � ������. ��� ���� ���-�� �� ���.";

	}

}
