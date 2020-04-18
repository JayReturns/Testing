package eu.mcuniverse.testing.Dimension;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;

import eu.mcuniverse.testing.main.Main;
import net.md_5.bungee.api.ChatColor;

public class DimensionChanger implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Not a player");
			return true;
		}
		
		Player p = (Player) sender;
		
		try {
			Integer i = Integer.valueOf(args[0]);
			PacketContainer packet = Main.protocolManager.createPacket(PacketType.Play.Server.RESPAWN);
			
			
//			packet.getIntegers().write(0, i); // Set Dimension
			packet.getDimensions().write(0, i);
			packet.getDifficulties().write(0, EnumWrappers.Difficulty.valueOf(p.getWorld().getDifficulty().toString())); // Set Difficulty
			packet.getWorldTypeModifier().write(0, p.getWorld().getWorldType()); // Set World type
			packet.getGameModes().write(0, NativeGameMode.fromBukkit(p.getGameMode()));

//			PacketType.Play.Server.GAME_STATE_CHANGE;
			
			try {
				Main.protocolManager.sendServerPacket(p, packet);
			} catch (InvocationTargetException exe) {
				throw new RuntimeException("Cannot send packet " + packet, exe);
			}
			
			p.sendMessage("Changed Level to " + i);
			
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "Error: " + e.getMessage());
		}
		
		return true;
	}

}
