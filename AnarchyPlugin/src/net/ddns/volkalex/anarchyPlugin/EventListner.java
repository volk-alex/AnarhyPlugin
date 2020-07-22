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
					"Твое время еще не пришло. Приходи через " + timeLeftFormat(playerName));
		} else {
			event.allow();
		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();

		DataStorHandler.setPlayerReturnDate(player.getName(), System.currentTimeMillis() + 604800000L);
		player.kickPlayer("Ты умер. Жаль. Приходи в другой раз!");

	}

	private String timeLeftFormat(String playerName) {

		long timeLeft = DataStorHandler.getEndDate(playerName) - System.currentTimeMillis();

		long daysLeft = Math.round(Math.ceil(timeLeft / 86400000.0d));

		if (daysLeft > 4)
			return String.valueOf(daysLeft) + " дней.";
		if (daysLeft > 1)
			return String.valueOf(daysLeft) + " дня.";

		long hoursLeft = Math.round(Math.ceil(timeLeft / 3600000.0d));

		if (hoursLeft > 21)
			return String.valueOf(hoursLeft) + " часа.";
		if (hoursLeft > 20)
			return String.valueOf(hoursLeft) + " час.";
		if (hoursLeft > 4)
			return String.valueOf(hoursLeft) + " часов.";
		if (hoursLeft > 1)
			return String.valueOf(hoursLeft) + " часа.";

		long minutesLeft = Math.round(Math.ceil(timeLeft / 60000.0d));

		if (minutesLeft > 14) {
			long unitMinutesLeft = minutesLeft % 10;
			if (unitMinutesLeft > 4)
				return String.valueOf(minutesLeft) + " минут.";
			if (unitMinutesLeft > 1)
				return String.valueOf(minutesLeft) + " минуы.";
			if (unitMinutesLeft > 0)
				return String.valueOf(minutesLeft) + " минуту.";
			return String.valueOf(hoursLeft) + " минут.";
		}

		if (minutesLeft > 4)
			return String.valueOf(minutesLeft) + " минут.";
		if (minutesLeft > 1)
			return String.valueOf(minutesLeft) + " минуы.";
		if (minutesLeft == 1)
			return String.valueOf(minutesLeft) + " минуту.";

		return "0 минут. И обратись к админу. Тут явно что-то не так.";

	}

}
