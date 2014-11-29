package com.woutwoot.svp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Plot {

	private Area area;
	private String owner;
	private List<String> friends = new ArrayList<String>();
	private String worldGuardName;
	private double priceFactor = 0.2;
	private Location signLocation;

	public Plot(String plotname) {
		this.setWorldGuardName(plotname);
	}

	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the friends
	 */
	public List<String> getFriends() {
		return friends;
	}

	/**
	 * @param friends
	 *            the friends to add
	 */
	public void addFriends(List<String> friends) {
		this.friends.addAll(friends);
	}

	/**
	 * @param friend
	 *            the friend to add
	 */
	public void addFriend(String friend) {
		this.friends.add(friend);
		ProtectedRegion rg = Main.pm.wg.getRegionManager(this.getArea().getC1().getWorld()).getRegion(this.getName());
		DefaultDomain domain = new DefaultDomain();
		domain.addPlayer(this.getOwner());
		for (String friend1 : this.getFriends()) {
			domain.addPlayer(friend1);
		}
		rg.setOwners(domain);
	}

	/**
	 * @return the worldGuardName
	 */
	public String getName() {
		return worldGuardName;
	}

	/**
	 * @param worldGuardName
	 *            the worldGuardName to set
	 */
	public void setWorldGuardName(String worldGuardName) {
		this.worldGuardName = worldGuardName;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return Math.round(this.priceFactor * this.getArea().getSize());
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.priceFactor = price;
	}

	/**
	 * @return the signLocation
	 */
	public Location getSignLocation() {
		return signLocation;
	}

	/**
	 * @param signLocation
	 *            the signLocation to set
	 */
	public void setSignLocation(Location signLocation) {
		this.signLocation = signLocation;
	}

	public boolean empty() {
		if (this.owner == null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Plot) {
			Plot p = (Plot) o;
			if (this.getName().equals(p.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		if(this.owner != null){
			return this.getName() + " - Owner: " + this.getOwner() + " - Price: " + this.getPrice();
		}else{
			return this.getName() + " - buyable - Price: " + this.getPrice();
		}
	}
}
