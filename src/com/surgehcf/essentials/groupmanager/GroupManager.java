package com.surgehcf.essentials.groupmanager;

import java.util.ArrayList;

import com.surgehcf.essentials.configuration.ConfigurationService;

public class GroupManager {

	ArrayList<String> vanished = new ArrayList<String>();
	ArrayList<String> staffchat = new ArrayList<String>();
	ArrayList<String> flying = new ArrayList<String>();
	
	public ArrayList<String> getFlying(){
		return this.flying;
	}
	
	public ArrayList<String> getVanished(){
		return this.vanished;
	}
	
	public ArrayList<String> getStaffChatMembers(){
		
		return this.staffchat;
	}
	
}
