package fr.thisismac.loginqueue.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import fr.thisismac.loginqueue.Main;
import fr.thisismac.loginqueue.ServerConnect;

public class DropCommand extends Command{

	public DropCommand() {
		super("dropcurrent");
	}

	@Override
	public void execute(CommandSender p, String[] arg1) {
		if(!p.hasPermission("bungeecord.command.reload")) {
			return;
		}
		
		if(arg1.length > 0 && Main.servers.containsKey(arg1[0])) {
			ServerConnect sc = Main.servers.get(arg1[0]);
			sc.queue.clear();
			p.sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Vous avez vidé la file d'attente du " + arg1[0]));
		}
	}

}
