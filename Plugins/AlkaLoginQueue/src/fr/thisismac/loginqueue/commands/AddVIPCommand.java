package fr.thisismac.loginqueue.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import fr.thisismac.loginqueue.Main;
import fr.thisismac.loginqueue.utils.DataBase;

public class AddVIPCommand extends Command{

	public AddVIPCommand() {
		super("addvip");
	}

	@Override
	public void execute(CommandSender p, String[] arg1) {
		if(!p.hasPermission("bungeecord.command.reload")) {
			return;
		}
		
		if(arg1.length > 0) {
			DataBase.load();
			DataBase.addPermission(arg1[0]);
			DataBase.save();
			p.sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + arg1[0]+" ajouté"));
		} else {
			p.sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Vous devez entrer un pseudo"));
		}
	}

}
