package  at.jojokobi.blockykingdom.commands;
import at.jojokobi.mcutil.JojokobiUtilPlugin;
import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.PlayerActionHandler;
import at.jojokobi.mcutil.JojokobiUtilPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import  at.jojokobi.blockykingdom.gui.SelectSpeciesGUI;
import  at.jojokobi.blockykingdom.players.StatHandler;
import  at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.gui.PlayerGUI;
import at.jojokobi.blockykingdom.gui.SelectSpeciesGUI;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.ChatInputHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class OpenGuiCommand implements CommandExecutor {

    public static final String COMMAND_NAME = "opengui";
    private InventoryGUIHandler guiHandler;

    public OpenGuiCommand(InventoryGUIHandler guiHandler) {
        super();
        this.guiHandler = guiHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
        if (text.equalsIgnoreCase(COMMAND_NAME)) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ChatInputHandler inputHandler = null;
                PlayerGUI gui = new PlayerGUI(player, StatHandler.getInstance().getStats(player), null, new KingdomPoint(player.getLocation()), null);
                guiHandler.addGUI(gui);
                gui.show();
                return true;
            }
        }
        return false;
    }

}
