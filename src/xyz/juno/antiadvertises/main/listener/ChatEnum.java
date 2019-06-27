package xyz.juno.antiadvertises.main.listener;

import java.util.List;

import xyz.juno.antiadvertises.main.AntiAdvertises;

public enum ChatEnum {
	CHARACTERS("Advertise-Characters-RegEx"),
	DOMAIN("Advertise-Domain-List"),
	REGEX("Advertise-RegEx"),
	WHITELIST("Advertise-Domain-WhiteList");
	
	private String path;
	
	private ChatEnum(String path) {
		this.path = path;
	}
	
	public List<String> getPath() {
		return AntiAdvertises.AntiAdvertise().getConfig().getStringList(path);
	}
}