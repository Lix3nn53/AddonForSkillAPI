package com.utilities;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class LixHologram {
	
	public static ArmorStand createTextLine(Location loc, String title){
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setVisible(false);
		as.setGravity(false);
		as.setMarker(true);
		as.setInvulnerable(true);
	    as.setCustomNameVisible(true);
	    as.setCustomName(title);
	    return as;
	}
	
	public static ArmorStand createItemLine(Location loc, ItemStack title){
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setVisible(false);
		as.setGravity(false);
		as.setMarker(true);
		as.setInvulnerable(true);
		Item a = loc.getWorld().dropItem(loc, title);
		as.addPassenger(a);
		return as;
	}
}
