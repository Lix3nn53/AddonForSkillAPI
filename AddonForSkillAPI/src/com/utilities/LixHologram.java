package com.utilities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class LixHologram {
	
	public static ArmorStand createTextLine(Location loc, String title){
		ArmorStand as = (ArmorStand) EntityCreator.create(EntityType.ARMOR_STAND, loc);
		as.setVisible(false);
		as.setGravity(false);
		as.setMarker(true);
		as.setInvulnerable(true);
	    as.setCustomNameVisible(true);
	    as.setCustomName(title);
	    
	    ((CraftWorld)loc.getWorld()).getHandle().addEntity(((CraftEntity) as).getHandle());
	    return as;
	}
	
	public static ArmorStand createItemLine(Location loc, ItemStack title){
		ArmorStand as = (ArmorStand) EntityCreator.create(EntityType.ARMOR_STAND, loc);
		as.setVisible(false);
		as.setGravity(false);
		as.setMarker(true);
		as.setInvulnerable(true);
		Item a = loc.getWorld().dropItem(loc, title);
		as.addPassenger(a);
		
		((CraftWorld)loc.getWorld()).getHandle().addEntity(((CraftEntity) as).getHandle());
		return as;
	}
	
	public static ArmorStand createInvis(Location loc, String title){
		ArmorStand as = (ArmorStand) EntityCreator.create(EntityType.ARMOR_STAND, loc);
		as.setVisible(false);
		as.setGravity(false);
		as.setMarker(true);
		as.setInvulnerable(true);
	    as.setCustomName(title);
	    as.setCustomNameVisible(false);
	    
	    ((CraftWorld)loc.getWorld()).getHandle().addEntity(((CraftEntity) as).getHandle());
	    return as;
	}
}
