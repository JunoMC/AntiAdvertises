package xyz.juno.antiadvertises.main;

public enum ArgumentEnum {
	HELP("(help|\\?)", "aa.help", true),
	RELOAD("(reload|rl)", "aa.reload", true),
	INFO("(info)", "aa.info", false);
	
	private String regex;
	private String permission;
	private boolean UseOnCmd;
	
	private ArgumentEnum(String regex, String permission, boolean UseOnCmd) {
		this.regex = regex;
		this.permission = permission;
		this.UseOnCmd = UseOnCmd;
	}
	
	public boolean UseOnCmd() {
		return UseOnCmd ? true : false;
	}
	
	public String RegEx() {
		return regex;
	}
	
	public String Permission() {
		return permission;
	}
}