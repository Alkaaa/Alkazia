package fr.thisismac.loginqueue.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import fr.thisismac.loginqueue.Main;
import fr.thisismac.loginqueue.ServerConnect;

public class LeaveCommand extends Command{

	public LeaveCommand() {
		super("leave");
	}

	@Override
	public void execute(CommandSender p, String[] arg1) {
		for(final String s : Main.servers.keySet()) {
			ServerConnect sc = Main.servers.get(s);
			if(sc.queue.contains(p.getName()) || sc.tp.contains(p.getName())) {
				if(sc.queue.contains(p.getName())) {
					sc.queue.remove(p.getName());
				} else {
					sc.tp.remove(p.getName());
				}
				p.sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Vous avez quitté la file d'attente du "+s));
			}
		}
	}

}
