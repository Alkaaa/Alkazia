package fr.thisismac.loginqueue;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import fr.thisismac.loginqueue.commands.AddVIPCommand;
import fr.thisismac.loginqueue.commands.DropCommand;
import fr.thisismac.loginqueue.commands.LeaveCommand;
import fr.thisismac.loginqueue.commands.ReloadVIPCommand;
import fr.thisismac.loginqueue.utils.DataBase;

public class Main extends Plugin{

	public static String tag = ChatColor.DARK_GREEN + "[Alkazia] " + ChatColor.RESET;
	public static HashMap<String, ServerConnect> servers = new HashMap<String, ServerConnect>();
	public static int vip = 15;

	@Override
	public void onEnable() {
		DataBase.load();
		startQueue();
		getProxy().getPluginManager().registerListener(this, new Events());
		getProxy().getPluginManager().registerCommand(this, new LeaveCommand());
		getProxy().getPluginManager().registerCommand(this, new AddVIPCommand());
		getProxy().getPluginManager().registerCommand(this, new ReloadVIPCommand());
		getProxy().getPluginManager().registerCommand(this, new DropCommand());

		for(String s : getProxy().getServers().keySet())
			servers.put(s, new ServerConnect());
	}

	private void startQueue() {
		getProxy().getScheduler().schedule(this,
				new Runnable() {
			public void run() {
				for(final String s : servers.keySet()) {
					getProxy().getServerInfo(s).ping(new Callback<ServerPing>() {
						@Override
						public void done(ServerPing arg0, Throwable arg1) {
							if(arg0 != null) {
								
								int current = arg0.getPlayers() == null ? 0 : arg0.getPlayers().getOnline();
								int max = arg0.getPlayers() == null ? 0 : arg0.getPlayers().getMax();
								
								servers.get(s).max = max;
								servers.get(s).current = current;
								
								if(current < max-vip-Main.servers.get(s).tp.size()) {
									teleportGuys(s, max-current-vip-Main.servers.get(s).tp.size());
								}
								
							}
						}
					});
				}
			}
		}, 5L, 5L, TimeUnit.SECONDS);

		getProxy().getScheduler().schedule(this,
				new Runnable() {
			public void run() {
				for(final String s : servers.keySet()) {
					ServerConnect sc = Main.servers.get(s);
					if(sc.tp.size() != 0) {
						ProxiedPlayer p = getProxy().getPlayer(sc.tp.get(0));
						sc.tp.remove(0);
						if(p != null) {
							sc.tpok.add(p.getName());
							p.sendMessage(new TextComponent(tag + ChatColor.GOLD + "Vous avez été téléporté au "+s));
							System.out.println("Téléportation de "+p.getName()+" au "+s);
							p.connect(getProxy().getServerInfo(s));
						}
						
						sendCurrentPositionToOther(s);
					}
				}
			}
		}, 2L, 2L, TimeUnit.SECONDS);
	}

	private void teleportGuys(String server, final int i) {
		ServerConnect sc = Main.servers.get(server);
		int x = 0;
		while(x < i) {
			if(sc.queue.size() <= 0) {
				x = i+1;
			} else {
				if(sc.queue.get(0) != null) {
					sc.tp.add(sc.queue.get(0));
					sc.queue.remove(0);
					sendCurrentPositionToOther(server);
					x++;
				} else {
					try {
						sc.queue.remove(0);
					} catch(Exception e) {}
				}
			}
		}
	}

	private void sendCurrentPositionToOther(String server) {
		ServerConnect sc = Main.servers.get(server);
		if(sc.queue.size() > 0) {
			for(String p : sc.queue) {
				if(p != null && getProxy().getPlayer(p) != null)
					getProxy().getPlayer(p).sendMessage(new TextComponent(tag + ChatColor.GOLD + "Vous êtes actuellement n°" + (sc.queue.indexOf(p)+1) + " dans la file d'attente pour le serveur "+server));
			}
		}
	}
}
