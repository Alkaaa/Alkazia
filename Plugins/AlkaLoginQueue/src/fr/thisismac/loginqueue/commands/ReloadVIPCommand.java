package fr.thisismac.loginqueue.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import fr.thisismac.loginqueue.Main;
import fr.thisismac.loginqueue.utils.DataBase;

public class ReloadVIPCommand extends Command{

	public ReloadVIPCommand() {
		super("reloadvip");
	}

	@Override
	public void execute(CommandSender p, String[] arg1) {
		if(!p.hasPermission("bungeecord.command.reload")) {
			return;
		}
		
		DataBase.load();
		p.sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Reload effectué"));
	}

}
