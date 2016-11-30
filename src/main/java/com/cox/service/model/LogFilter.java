package com.cox.service.model;

public class LogFilter {

	private int count;
	private String[] levels;
	private String matchesText;
	private Long beforeTimestamp;
	private Long afterTimestamp;

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String[] getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		String[] levelsArr = null;
		if(levels.equalsIgnoreCase("All")){
			levelsArr = new String[4];
			levelsArr[0] = "DEBUG";
			levelsArr[1] = "INFO";
			levelsArr[2] = "WARN";
			levelsArr[3] = "ERROR";
			this.levels = levelsArr;
		}else if(levels.equalsIgnoreCase("DEBUG")){
			levelsArr = new String[4];
			levelsArr[0] = "DEBUG";
			levelsArr[1] = "INFO";
			levelsArr[2] = "WARN";
			levelsArr[3] = "ERROR";
			this.levels = levelsArr;
		}else if(levels.equalsIgnoreCase("INFO")){
			levelsArr = new String[3];
			levelsArr[0] = "INFO";
			levelsArr[1] = "WARN";
			levelsArr[2] = "ERROR";
			this.levels = levelsArr;
		}else if(levels.equalsIgnoreCase("WARN")){
			levelsArr = new String[2];
			levelsArr[0] = "WARN";
			levelsArr[1] = "ERROR";
			this.levels = levelsArr;
		}else if(levels.equalsIgnoreCase("ERROR")){
			levelsArr = new String[1];
			levelsArr[0] = "ERROR";
			this.levels = levelsArr;
		}else{
			this.levels = null;
		}
	}
	public String getMatchesText() {
		return matchesText;
	}
	public void setMatchesText(String matchesText) {
		this.matchesText = matchesText;
	}
	public Long getBeforeTimestamp() {
		return beforeTimestamp;
	}
	public void setBeforeTimestamp(Long beforeTimestamp) {
		this.beforeTimestamp = beforeTimestamp;
	}
	public Long getAfterTimestamp() {
		return afterTimestamp;
	}
	public void setAfterTimestamp(Long afterTimestamp) {
		this.afterTimestamp = afterTimestamp;
	}
	
}
