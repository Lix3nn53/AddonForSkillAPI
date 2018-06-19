package com.utilities;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

public class FakeHologram {
	
	public int ENTITY_ID;
	private WrappedDataWatcher metadata;
	private WrapperPlayServerSpawnEntityLiving armorStand;
	
	public FakeHologram(Location loc, String displayName)
    {
		this.armorStand = new WrapperPlayServerSpawnEntityLiving();
		
		this.armorStand.setType(EntityType.ARMOR_STAND);
		this.ENTITY_ID = AddonForSkillAPI.LASTEST_HOLOGRAM_ID;
		this.armorStand.setEntityID(ENTITY_ID);
		this.armorStand.setX(loc.getX());
		this.armorStand.setY(loc.getY());
		this.armorStand.setZ(loc.getZ());
		
		AddonForSkillAPI.LASTEST_HOLOGRAM_ID++;
		
		this.metadata = new WrappedDataWatcher();
		this.metadata.setObject(new WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20); //invis
		this.metadata.setObject(new WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.get(String.class)), displayName); //display name
		this.metadata.setObject(new WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true); //custom name visible
		this.metadata.setObject(new WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true); //no gravity
		this.metadata.setObject(new WrappedDataWatcherObject(11, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10)); //isSmall, noBasePlate, set Marker
        
        this.armorStand.setMetadata(metadata);
    }
	
	public boolean showToPlayer(Player p, ProtocolManager manager) {
		try {
			manager.sendServerPacket(p, armorStand.getHandle());
			return true;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(Player p, ProtocolManager manager) {
		WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        destroy.setEntityIds(new int[] {ENTITY_ID});
        try {
			manager.sendServerPacket(p, destroy.getHandle());
			return true;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	

}
