package fr.thisismac.koth;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdExecutor implements CommandExecutor{
	
	 private Core core;

	    public CmdExecutor(final Core c) {
	    	this.core = c;
	    }

	    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmde, final String[] args) {
	    	
	    	if(sender.isOp()) {
	    		if(args.length == 0) {
	    			core.startGame();
		    		sender.sendMessage(core.prefix + ChatColor.GREEN + "Le KOTH a bien démarré.");
		    		return true;
	    		}
	    		else if(args.length == 1 && args[0].equalsIgnoreCase("enable")) {
	    			core.enabled = true;
	    		}
	    		else if(args.length == 1 && args[0].equalsIgnoreCase("disable")) {
	    			core.enabled = false;
	    		}
	    	}
	    	return true;
	    }
	
}
