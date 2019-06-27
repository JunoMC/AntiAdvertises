package xyz.juno.antiadvertises.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.juno.antiadvertises.main.listener.AsyncPlayerChat;

public class AntiAdvertises extends JavaPlugin implements Listener {
	public static File logf;
	public static FileConfiguration log;
	public static AntiAdvertises aa;
	
	public static JavaPlugin AntiAdvertise() {
		return aa;
	}
	
	@Override
	public void onEnable() {
		aa = this;
		saveDefaultConfig();
		createLog();
		Bukkit.getPluginManager().registerEvents(new AsyncPlayerChat(), AntiAdvertise());
		
	}
	
	@Override
	public void onDisable() {}
	
	public void createLog() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		sdf.format(date);
		
		int year = date.getYear() + 1900;
		int month = date.getMonth() + 1;
		
		logf = new File(getDataFolder(), "logs" + File.separator + date.getDate() + "-" + month + "-" + year + ".txt");
		log = new YamlConfiguration();
		
		if (!logf.exists()) {
			logf.getParentFile().mkdirs();
			try {
				logf.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			log.load(logf);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		while(date.getHours() == 0
				&& date.getMinutes() == 0
				&& date.getSeconds() == 1
				&& (!logf.exists())) {
			
			logf.getParentFile().mkdirs();
			
			try {
				logf.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				log.load(logf);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String Color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public boolean onCommand(CommandSender sender, Command c, String label, String[] a) {
		
		ArgumentEnum HELP = ArgumentEnum.HELP;
		ArgumentEnum RELOAD = ArgumentEnum.RELOAD;
		ArgumentEnum INFO = ArgumentEnum.INFO;
		
		String prefix = Color(getConfig().getString("Prefix"));
		
		if (a.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player)sender;
				if (p.hasPermission(HELP.Permission())) {
					for (String msg : getConfig().getStringList("messages.help")) {
						p.sendMessage(Color(msg.replace("{lenh}", label)));
					}
				} else {
					p.sendMessage(Color(prefix + getConfig().getString("messages.no-permission")));
				}
			} else {
				for (String msg : getConfig().getStringList("messages.help")) {
					sender.sendMessage(Color(msg.replace("{lenh}", label)));
				}
			}
		}
		
		if (a.length == 1) {
			if (a[0].toLowerCase().matches(HELP.RegEx())) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					if (p.hasPermission(HELP.Permission())) {
						for (String msg : getConfig().getStringList("messages.help")) {
							p.sendMessage(Color(msg.replace("{lenh}", label)));
						}
					} else {
						p.sendMessage(Color(prefix + getConfig().getString("messages.no-permission")));
					}
				} else {
					for (String msg : getConfig().getStringList("messages.help")) {
						sender.sendMessage(Color(msg.replace("{lenh}", label)));
					}
				}
			}
			
			if (a[0].toLowerCase().matches(INFO.RegEx())) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					if (p.hasPermission(INFO.Permission())) {
						p.sendMessage(Color("&m                "));
						
						TextComponent s1 = new TextComponent(Color("&fAuthor: "));
						
						TextComponent s2 = new TextComponent(Color("&eJuno"));
						s2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.facebook.com/profile.php?id=100033827385372"));
			            s2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Color("&bGo to Juno's facebook")).create()));
						
						ArrayList<TextComponent> array = new ArrayList<TextComponent>();
						array.add(s1);
						array.add(s2);
						
						TextComponent message = new TextComponent("");
						
						for (TextComponent total : array) {
							message.addExtra(total);
						}
						
						p.spigot().sendMessage(message);
						p.sendMessage(Color("&fVersion: 0.1-beta"));
						
						p.sendMessage(Color("&m                "));
					} else {
						p.sendMessage(Color(prefix + getConfig().getString("messages.no-permission")));
					}
				} else {
					sender.sendMessage(Color(prefix + getConfig().getString("messages.must-be-player")));
				}
			}
			
			if (a[0].toLowerCase().matches(RELOAD.RegEx())) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					if (p.hasPermission(RELOAD.Permission())) {
						try {
							reloadConfig();
							log.load(logf);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							p.sendMessage(Color(prefix + getConfig().getString("messages.reload-error")));
						} catch (IOException e) {
							e.printStackTrace();
							p.sendMessage(Color(prefix + getConfig().getString("messages.reload-error")));
						} catch (InvalidConfigurationException e) {
							e.printStackTrace();
							p.sendMessage(Color(prefix + getConfig().getString("messages.reload-error")));
						} finally {
							p.sendMessage(Color(prefix + getConfig().getString("messages.reload-success")));
						}
					} else {
						p.sendMessage(Color(prefix + getConfig().getString("messages.no-permission")));
					}
				} else {
					try {
						reloadConfig();
						log.load(logf);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						sender.sendMessage(Color(prefix + getConfig().getString("messages.reload-error")));
					} catch (IOException e) {
						e.printStackTrace();
						sender.sendMessage(Color(prefix + getConfig().getString("messages.reload-error")));
					} catch (InvalidConfigurationException e) {
						e.printStackTrace();
						sender.sendMessage(Color(prefix + getConfig().getString("messages.reload-error")));
					} finally {
						sender.sendMessage(Color(prefix + getConfig().getString("messages.reload-success")));
					}
				}
			}
		}
		
		return false;
	}
	
}