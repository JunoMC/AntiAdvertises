package xyz.juno.antiadvertises.main.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import xyz.juno.antiadvertises.main.AntiAdvertises;

public class AsyncPlayerChat implements Listener {
	
	@EventHandler
	public void onAsyncPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		
		ChatEnum REGEX = ChatEnum.REGEX;
		ChatEnum CHARACTERS = ChatEnum.CHARACTERS;
		ChatEnum DOMAIN = ChatEnum.DOMAIN;
		ChatEnum WHITELIST = ChatEnum.WHITELIST;
		
		for (String w : WHITELIST.getPath()) {
			if (msg.contains(w)) {
				return;
			}
		}
		
		for (String RegEx : REGEX.getPath()) {
			String s1 = msg.replaceAll("[a-zA-Z]", "");
			String s2 = s1.replaceAll("(^ | $)", "");
			
			FileConfiguration config = AntiAdvertises.AntiAdvertise().getConfig();
			
			boolean cancelled = config.getBoolean("Cancelled");
			
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
			sdf.format(date);
			
			if (Checked(s2, RegEx)) {
				e.setCancelled(cancelled);
				
				for (String _string : config.getStringList("Commands-Punish")) {
					
					String _datum = _string.substring(0, _string.indexOf(":"));
    				String _command = _string.substring(_string.indexOf(":")).replaceFirst("(: |\\:)", "");
					
    				switch(_datum.toUpperCase()) {
					case "CONSOLE":
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AntiAdvertises.Color(_command)
								.replaceAll("(\\{player})", p.getName())
								.replaceAll("(\\{advertise})", s2));
						break;
					case "MESSAGE":
						p.sendMessage(AntiAdvertises.Color(_command)
								.replaceAll("(\\{player})", p.getName())
    							.replaceAll("(\\{advertise})", s2));
						break;
					default:
						break;
					}
				}
				
				AntiAdvertises.log.set("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]", s2);
				
				try {
					AntiAdvertises.log.save(AntiAdvertises.logf);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			} else {
				String _regex = String.join("|", CHARACTERS.getPath());
				String _matches = "(" + _regex + ")";
				
				String s3 = s2.replaceAll(_matches, ".");
				
				if (Checked(s3, RegEx)) {
					e.setCancelled(cancelled);
					
					for (String _string : config.getStringList("Commands-Punish")) {
						
						String _datum = _string.substring(0, _string.indexOf(":"));
	    				String _command = _string.substring(_string.indexOf(":")).replaceFirst("(: |\\:)", "");
						
	    				switch(_datum.toUpperCase()) {
						case "CONSOLE":
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AntiAdvertises.Color(_command)
									.replaceAll("(\\{player})", p.getName())
									.replaceAll("(\\{advertise})", s3));
							break;
						case "MESSAGE":
							p.sendMessage(AntiAdvertises.Color(_command)
									.replaceAll("(\\{player})", p.getName())
	    							.replaceAll("(\\{advertise})", s3));
							break;
						default:
							break;
						}
					}
					
					AntiAdvertises.log.set("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]", s3);
					
					try {
						AntiAdvertises.log.save(AntiAdvertises.logf);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					
					String matches_2 = "(" + _regex + ")";
					String s4 = msg.replaceAll(matches_2, ".");
					
					for (String suffix : DOMAIN.getPath()) {
						if (s4.contains(suffix)) {
							e.setCancelled(cancelled);
							
							for (String _string : config.getStringList("Commands-Punish")) {
								
								String _datum = _string.substring(0, _string.indexOf(":"));
			    				String _command = _string.substring(_string.indexOf(":")).replaceFirst("(: |\\:)", "");
								
			    				switch(_datum.toUpperCase()) {
	    						case "CONSOLE":
	    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AntiAdvertises.Color(_command)
	    									.replaceAll("(\\{player})", p.getName())
	    									.replaceAll("(\\{advertise})", s4));
	    							break;
	    						case "MESSAGE":
	    							p.sendMessage(AntiAdvertises.Color(_command)
	    									.replaceAll("(\\{player})", p.getName())
	    	    							.replaceAll("(\\{advertise})", s4));
	    							break;
	    						default:
	    							break;
	    						}
							}
							
							AntiAdvertises.log.set("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]", s4);
							
							try {
								AntiAdvertises.log.save(AntiAdvertises.logf);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public boolean Checked(String ip, String matches) {
		return ip.matches(matches)? true : false;
	}
}
