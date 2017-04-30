package fr.thisismac.loginqueue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import fr.thisismac.loginqueue.utils.DataBase;

public class Events implements Listener{

	@EventHandler
    public void onServerJoin(ServerConnectEvent e) {
		if(e.getTarget().getName().equals("hub")) {
			return;
		}
		
		ServerConnect sc = Main.servers.get(e.getTarget().getName());
		if(sc.tpok.contains(e.getPlayer().getName())) {
			sc.tpok.remove(e.getPlayer().getName());
			return;
		}
		
		if(DataBase.hasPermission(e.getPlayer().getName())) {
			System.out.println(e.getPlayer().getName()+" a bypass la file d'attente");
			e.getPlayer().sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Bypass de la file d'attente. Connexion en cours ..."));
			return;
		}
		
		if(!sc.queue.contains(e.getPlayer().getName())) {
			if(sc.current < (sc.max - Main.vip - 10)) {
				e.getPlayer().sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Vous allez être téléporté vers le serveur "+ e.getTarget().getName() + ChatColor.GOLD + " dans quelques instants."));
				return;
			}
			else {
				sc.queue.add(e.getPlayer().getName());
				e.getPlayer().sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Vous rentrez n°" + (sc.queue.indexOf(e.getPlayer().getName())+1) + " en file d'attente pour entrer sur le serveur " + ChatColor.GOLD + e.getTarget().getName()));
			}
		}
		
		if(sc.queue.contains(e.getPlayer().getName())) {
			e.getPlayer().sendMessage(new TextComponent(Main.tag + ChatColor.GOLD + "Vous êtes déjà dans la file d'attente du serveur " +e.getTarget().getName() + " , cela ne sert à rien de forcer le passage."));
		}
		
		e.setCancelled(true);
    }
	
}
