package com.utilities;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.SkillPlugin;
import com.sucy.skill.api.event.PlayerCastSkillEvent;
import com.sucy.skill.api.event.PlayerClassChangeEvent;
import com.sucy.skill.api.event.SkillDamageEvent;
import com.sucy.skill.api.event.SkillHealEvent;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.skills.Skill;
import com.sucy.skill.dynamic.DynamicSkill;

import net.md_5.bungee.api.ChatColor;

public class AddonForSkillAPI extends JavaPlugin implements SkillPlugin, Listener {
	
	public static HashMap<UUID , Integer> justJoined = new HashMap<UUID , Integer>();
	
	public static HashMap<Integer , String> blockedRegions = new HashMap<Integer , String>();
	
	public static HashMap<String , Boolean> configurations = new HashMap<String , Boolean>();
	public static HashMap<String , Double> asLoc = new HashMap<String , Double>();
	public static HashMap<String , String> iconss = new HashMap<String , String>();
	public static HashMap<String , Integer> digits = new HashMap<String , Integer>();
	
	public static String chatPrefix = "";
	public static String chatSuffix = "";
	
	public static HashMap<Integer , Material> blockType = new HashMap<Integer , Material>();
	public static HashMap<Integer , List<String>> blockSkill = new HashMap<Integer , List<String>>();
	public static String regionMessage = ChatColor.RED + "You cant use skills in this region."; 
	
	public static String skillFailSound = ""; 
	
	List<Integer> nonSkillSlots = new ArrayList<Integer>();
	
	public static List<String> friendlyIgnoreMaps = new ArrayList<String>();
	
	public static String potionSplit = ""; 
	
    @Override
    public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		createYml("config");
    }
    
    @Override
    public void onLoad() {
    	
    }
    
    @Override
    public void onDisable() {
    	
    }
    
    public static boolean isParsable(String input){
        boolean parsable = true;
        try{
            Integer.parseInt(input);
        }catch(NumberFormatException e){
            parsable = false;
        }
        return parsable;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String [] args) {
		if (cmd.getName().equalsIgnoreCase("afs") && sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("afs.admin")) {
				if(args.length < 1) {
	    			p.sendMessage(ChatColor.AQUA + "/afs reload");
	    			p.sendMessage(ChatColor.AQUA + "/afs changeclass <player> <class to change> <new class>");
	    			p.sendMessage(ChatColor.AQUA + "/afs setlevel <player> <class> <level>");
				}else if(args[0].equals("reload")){
					createYml("config", p);
					for (Player pp : Bukkit.getOnlinePlayers()) {
						changeTabName(pp);
					}
				}else if(args[0].equals("changeclass")){
					if(args.length >= 4){
		    			OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
		    			if(op != null){
		    				if(op.isOnline()){
		    					PlayerData sdata = SkillAPI.getPlayerData(op);
		    					Collection<PlayerClass> classs = sdata.getClasses();
		    					for(PlayerClass clas : classs) {
		    						if(clas.getData().getName().equals(args[2])) {
		    							clas.setClassData(SkillAPI.getClass(args[3]));
		    							break;
		    						}
		    					}
		    				}
		    			}
	    			}
				}else if(args[0].equals("setlevel")){
					if(args.length >= 4){
		    			OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
		    			if(op != null){
		    				if(op.isOnline()){
		    					PlayerData sdata = SkillAPI.getPlayerData(op);
		    					Collection<PlayerClass> classs = sdata.getClasses();
		    					for(PlayerClass clas : classs) {
		    						if(clas.getData().getName().equals(args[2])) {
		    							if(isParsable(args[3])){
		    								int level = Integer.parseInt(args[3]);
		    								clas.setLevel(level);
		    								break;
		    							}
		    						}
		    					}
		    				}
		    			}
	    			}
				}else if(args[0].equals("test")){
					ItemStack pot = new ItemStack(Material.POTION);
					ItemMeta im = pot.getItemMeta();
					im.setDisplayName(ChatColor.LIGHT_PURPLE + "Mor Kanatlar" + ChatColor.GOLD +potionSplit + "1");
					pot.setItemMeta(im);
					p.getInventory().addItem(pot);
				}
			}
		}else if (cmd.getName().equalsIgnoreCase("afs") && sender instanceof ConsoleCommandSender){
			if(args.length < 1) {
				getLogger().info("afs reload");
			}else if(args[0].equals("reload")){
				createYml("config");
				for (Player pp : Bukkit.getOnlinePlayers()) {
					changeTabName(pp);
				}
			}
		}
		return false;
    }
    
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
    	boolean notFriendlyRace = true;
    	if(configurations.get("friendlyraces")) {
    		boolean canPass = true;
    		if(!friendlyIgnoreMaps.isEmpty()) {
    			if(friendlyIgnoreMaps.contains(e.getEntity().getWorld().getName())){
    				canPass = false;
    			}
    		}
    		if(canPass) {
	    		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
	    			Player p1 = (Player) e.getDamager();
	    			Player p2 = (Player) e.getEntity();
	    			
	    			PlayerData sdata1 = SkillAPI.getPlayerData(p1);
	    			String grup1 = sdata1.getMainClass().getData().getGroup();
	    			
	    			PlayerData sdata2 = SkillAPI.getPlayerData(p2);
	    			String grup2 = sdata2.getMainClass().getData().getGroup();
	    			
	    			if(grup1.equals(grup2)) {
	    				e.setCancelled(true);
	    				notFriendlyRace = false;
	    			}
	    		}
    		}
    	}
    	if(configurations.get("damage")) {
	    	if(e.getEntity() instanceof LivingEntity){
	    		if(e.getCause().equals(DamageCause.CUSTOM)){
	        		return;
	        	}
	    		if(notFriendlyRace) {
		    		LivingEntity p = (LivingEntity) e.getEntity();
		    		if(!p.getType().equals(EntityType.ARMOR_STAND)){
				    	if(e.getDamager() instanceof Player){
				    		String text = "error";
				    		if(digits.get("dmg") < 1) {
				    			int hpgain = (int) (e.getDamage() + 0.5);
				    			text = ChatColor.RED.toString() + hpgain + " " + iconss.get("icondmg");
				    		}else {
				    			BigDecimal hpgain = new BigDecimal(e.getDamage()).setScale(digits.get("dmg"), RoundingMode.CEILING);
				    			text = ChatColor.RED.toString() + hpgain + " " + iconss.get("icondmg");
				    		}
				    		double x = (new Random().nextDouble() * (asLoc.get("xmax") - asLoc.get("xmin"))) + asLoc.get("xmin");
				    		double y = (new Random().nextDouble() * (asLoc.get("ymax") - asLoc.get("ymin"))) + asLoc.get("ymin");
				    		double z = (new Random().nextDouble() * (asLoc.get("zmax") - asLoc.get("zmin"))) + asLoc.get("zmin");
				    		double chance1 = new Random().nextDouble();
				    		double chance2 = new Random().nextDouble();
				    		if(chance1 < 0.5) {
				    			x = -x;
				    		}
				    		if(chance2 < 0.5) {
				    			z = -z;
				    		}
				    		Location loc = p.getLocation().add(x, y, z);
				    		ArmorStand as = LixHologram.createTextLine(loc, text);
				    		new BukkitRunnable() {
				
					    	    @Override
					    	    public void run() {
					    	    	as.remove();
					    	    	cancel();
					    	    }
				    		}.runTaskTimerAsynchronously(this, 18L, 18L);
				    	}else if (e.getDamager() instanceof Arrow){
				    		String text = "error";
				    		if(digits.get("dmg") < 1) {
				    			int hpgain = (int) (e.getDamage() + 0.5);
				    			text = ChatColor.RED.toString() + hpgain + " " + iconss.get("icondmg");
				    		}else {
				    			BigDecimal hpgain = new BigDecimal(e.getDamage()).setScale(digits.get("dmg"), RoundingMode.CEILING);
				    			text = ChatColor.RED.toString() + hpgain + " " + iconss.get("icondmg");
				    		}
				    		double x = (new Random().nextDouble() * (asLoc.get("xmax") - asLoc.get("xmin"))) + asLoc.get("xmin");
				    		double y = (new Random().nextDouble() * (asLoc.get("ymax") - asLoc.get("ymin"))) + asLoc.get("ymin");
				    		double z = (new Random().nextDouble() * (asLoc.get("zmax") - asLoc.get("zmin"))) + asLoc.get("zmin");
				    		double chance1 = new Random().nextDouble();
				    		double chance2 = new Random().nextDouble();
				    		if(chance1 < 0.5) {
				    			x = -x;
				    		}
				    		if(chance2 < 0.5) {
				    			z = -z;
				    		}
				    		Location loc = p.getLocation().add(x, y, z);
				    		ArmorStand as = LixHologram.createTextLine(loc, text);
				    		new BukkitRunnable() {
				
					    	    @Override
					    	    public void run() {
					    	    	as.remove();
					    	    	cancel();
					    	    }
				    		}.runTaskTimerAsynchronously(this, 18L, 18L);
				    	}
		    		}
		    	}
	    	}
    	}
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSkillHeal(SkillHealEvent e) {
    	if(configurations.get("heal")) {
	    	if(e.getTarget() instanceof LivingEntity){
	    		if(!e.getTarget().getType().equals(EntityType.ARMOR_STAND)){
			    	if(e.getHealer() instanceof Player){
			    		LivingEntity p = (LivingEntity) e.getTarget();
			    		String text = "error";
			    		if(digits.get("heal") < 1) {
			    			int hpgain = (int) (e.getAmount() + 0.5);
			    			text = ChatColor.GREEN.toString() + hpgain + " " + iconss.get("iconheal");
			    		}else {
			    			BigDecimal hpgain = new BigDecimal(e.getAmount()).setScale(digits.get("heal"), RoundingMode.CEILING);
			    			text = ChatColor.GREEN.toString() + hpgain + " " + iconss.get("iconheal");
			    		}
			    		double x = (new Random().nextDouble() * (asLoc.get("xmax") - asLoc.get("xmin"))) + asLoc.get("xmin");
			    		double y = (new Random().nextDouble() * (asLoc.get("ymax") - asLoc.get("ymin"))) + asLoc.get("ymin");
			    		double z = (new Random().nextDouble() * (asLoc.get("zmax") - asLoc.get("zmin"))) + asLoc.get("zmin");
			    		double chance1 = new Random().nextDouble();
			    		double chance2 = new Random().nextDouble();
			    		if(chance1 < 0.5) {
			    			x = -x;
			    		}
			    		if(chance2 < 0.5) {
			    			z = -z;
			    		}
			    		Location loc = p.getLocation().add(x, y, z);
			    		ArmorStand as = LixHologram.createTextLine(loc, text);
			    		new BukkitRunnable() {
			
				    	    @Override
				    	    public void run() {
				    	    	as.remove();
				    	    	cancel();
				    	    }
			    		}.runTaskTimerAsynchronously(this, 12L, 12L);
			    	}
	    		}
	    	}
    	}
    }
    
	@Override
	public void registerClasses(SkillAPI arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerSkills(SkillAPI arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent e)
    {
    	Player p = e.getPlayer();
    	UUID uuid = p.getUniqueId();
    	justJoined.put(uuid, 1);
    	new BukkitRunnable() {

    	    @Override
    	    public void run() {
    	    	cancel();
    	    	if(justJoined.containsKey(uuid)){
    	    		justJoined.remove(uuid);
    	    	}
    	    }
    	}.runTaskTimer(this, 30L, 30L);
    }
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
		Player p = e.getPlayer();
    	UUID uuid = p.getUniqueId();
    	justJoined.put(uuid, 1);
    	changeTabName(p);
    	new BukkitRunnable() {

    	    @Override
    	    public void run() {
    	    	cancel();
    	    	if(justJoined.containsKey(uuid)){
    	    		justJoined.remove(uuid);
    	    	}
    	    }
    	}.runTaskTimer(this, 40L, 40L);
    }
	
	@EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent e)
    {
		Player p = e.getPlayer();
		changeTabName(p);
    }
	
	@EventHandler
    public void onPlayerClassChange(PlayerClassChangeEvent e)
    {
		Player p = e.getPlayerData().getPlayer();
		changeTabName(p);
    }
	
    @EventHandler
    public void onSkillUse(PlayerCastSkillEvent e)
    {
    	if(justJoined.containsKey(e.getPlayer().getUniqueId())){
    		e.setCancelled(true);
    	}
    	boolean InArena = false;
    	Player p = e.getPlayer();
		for(ProtectedRegion rg : WGBukkit.getRegionManager(p.getLocation().getWorld()).getApplicableRegions(p.getLocation())) {
			for(Integer regno : blockedRegions.keySet()) {
				String regstring = blockedRegions.get(regno);
				if(rg.getId().equalsIgnoreCase(regstring)){
					InArena = true;
					break;
				}
			}
			if(InArena) {
				break;
			}
		}
		if(InArena) {
			e.setCancelled(true);
			p.sendMessage(regionMessage);
		}
		if(e.isCancelled()) {
			if(skillFailSound != null) {
				if(!skillFailSound.equals("")) {
					p.playSound(p.getLocation(), skillFailSound, 0.5F, 0.9F);
				}
			}
		}
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageArmorStand(EntityDamageByEntityEvent e) {
    	if(configurations.get("invulnerable")) {
	    	if(e.getEntityType().equals(EntityType.ARMOR_STAND)) {
	    		e.setCancelled(true);
	    	}
    	}
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageArmorStandWSkill(SkillDamageEvent e) {
    	LivingEntity entity = e.getTarget();
    	if(configurations.get("invulnerable")) {
	    	if(entity.getType().equals(EntityType.ARMOR_STAND)) {
	    		e.setCancelled(true);
	    	}
    	}
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
    	if(!blockType.isEmpty()) {
    		Player p = e.getPlayer();
    		for(int i = 1; i <= blockType.size(); i++) {
    			Material mat = blockType.get(i);
				if(e.getBlock().getType().equals(mat)) {
					List<String> skills = blockSkill.get(i);
					for(String skill : skills) {
						final DynamicSkill dynskill = (DynamicSkill) SkillAPI.getSkill(skill);
						dynskill.cast(p, 1);
					}
				}
    		}
    	}
    }
    
    private void createYml(String filename) {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), filename+".yml");
            if (!file.exists()) {
                getLogger().info(filename + ".yml not found, creating!");
                saveDefaultConfig();
                loadDefaultOptions();
            } else {
                getLogger().info(filename + ".yml found, loading!");
                loadCongif(file);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    private void createYml(String filename, Player p) {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), filename+".yml");
            if (!file.exists()) {
            	p.sendMessage(filename + ".yml not found, creating!");
                saveDefaultConfig();
                loadDefaultOptions();
            } else {
                p.sendMessage(filename + ".yml found, loading!");
                loadCongif(file);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    private void loadCongif(File file) {
    	YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        boolean dmg = yaml.getBoolean("DamageInducators");
        boolean heal = yaml.getBoolean("HealInducators");
        boolean invulnerable = yaml.getBoolean("AllArmorStandsInvulnerable");
        double xmax = yaml.getDouble("ArmorStandSpawnLocation.xmax");
        double xmin = yaml.getDouble("ArmorStandSpawnLocation.xmin");
        double ymax = yaml.getDouble("ArmorStandSpawnLocation.ymax");
        double ymin = yaml.getDouble("ArmorStandSpawnLocation.ymin");
        double zmax = yaml.getDouble("ArmorStandSpawnLocation.zmax");
        double zmin = yaml.getDouble("ArmorStandSpawnLocation.zmin");
        String iconheal = yaml.getString("Icons.heal");
        String icondmg = yaml.getString("Icons.damage");
        int digitheal = yaml.getInt("Digits.heal");
        int digitdmg = yaml.getInt("Digits.damage");
        
        potionSplit = yaml.getString("PotionSkill.split");
        
        chatPrefix = ChatColor.translateAlternateColorCodes('&', yaml.getString("ChatPrefix.prefix"));
        chatSuffix = ChatColor.translateAlternateColorCodes('&', yaml.getString("ChatPrefix.suffix"));
        
        boolean chatactive = yaml.getBoolean("ChatPrefix.active");
        boolean tabName = yaml.getBoolean("ChatPrefix.tablist");
        
        int blocknum = yaml.getInt("BlockBreakEvent.howmanyblocks");
        if(blocknum > 0) {
            List<String> blockmats = yaml.getStringList("BlockBreakEvent.blocks");
            
            int y = 1;
            for(String mats : blockmats) {
            	Material mat = Material.getMaterial(mats);
            	blockType.put(y, mat);
            	y++;
            }
            
            for(int i = 1; i <= blocknum; i++){
            	List<String> blockskills = yaml.getStringList("BlockBreakEvent.skills." + i);
            	blockSkill.put(i, blockskills);
            }
        }
        
        List<String> regions = yaml.getStringList("ProtectedRegions.regions");
        for(int i = 0; i < regions.size(); i ++) {
        	blockedRegions.put(i, regions.get(i));
        }
        
        regionMessage = yaml.getString("ProtectedRegions.blockSkillMessage");
        regionMessage = ChatColor.translateAlternateColorCodes('&', regionMessage);
        
        skillFailSound = yaml.getString("FailSound.name");
        
        boolean slotact = yaml.getBoolean("SkillSlots.active");
        List<Integer> slots = yaml.getIntegerList("SkillSlots.nonSkillSlots");
        
        
        getLogger().info("Skill slots = " + slotact);
        getLogger().info("Heal inducators = " + heal);
        getLogger().info("All ArmorStands Invulnerable = " + invulnerable);
        
        boolean friendlyraces = yaml.getBoolean("FriendlyRaces.active");
        if(friendlyraces) {
        	friendlyIgnoreMaps = yaml.getStringList("ProtectedRegions.regions");
        }
        
        
        configurations.put("friendlyraces", friendlyraces);
        nonSkillSlots = slots;
        configurations.put("skillSlotsActive", slotact);
        configurations.put("damage", dmg);
        configurations.put("heal", heal);
        configurations.put("invulnerable", invulnerable);
        configurations.put("chatactive", chatactive);
        asLoc.put("xmax", xmax);
        asLoc.put("xmin", xmin);
        asLoc.put("ymax", ymax);
        asLoc.put("ymin", ymin);
        asLoc.put("zmax", zmax);
        asLoc.put("zmin", zmin);
        iconss.put("iconheal", iconheal);
        iconss.put("icondmg", icondmg);
        digits.put("dmg", digitdmg);
        digits.put("heal", digitheal);
        configurations.put("tablistName", tabName);
    }
    
    private void loadDefaultOptions() {
    	configurations.put("friendlyraces", false);
    	configurations.put("skillSlotsActive", false);
        configurations.put("damage", true);
        configurations.put("heal", true);
        configurations.put("invulnerable", true);
        configurations.put("chatactive", true);
        asLoc.put("xmax", 0.5);
        asLoc.put("xmin", 0.0);
        asLoc.put("ymax", 1.0);
        asLoc.put("ymin", 0.0);
        asLoc.put("zmax", 0.5);
        asLoc.put("zmin", 0.0);
        iconss.put("iconheal", "❤");
        iconss.put("icondmg", "➹");
        digits.put("dmg", 0);
        digits.put("heal", 0);
        chatPrefix = ChatColor.translateAlternateColorCodes('&', "%color%[%class%%color%] &6%level% &f");
        configurations.put("skillSlotsActive", false);
        configurations.put("tablistName", true);
        potionSplit = " level ";
        chatSuffix = ChatColor.GRAY + " > " + ChatColor.WHITE;
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
     
	    String eventMessage = e.getMessage();
	    Player eventPlayer = e.getPlayer();
	     
	    String format = "<group-prefix><player><group-suffix><message>";
	    //replacing your values
	    format = format.replace("<player>", "%s"); //the player name will be automatically replaced by player.getDisplayName() you could write "%s" too but if you do it like that, you can place the message before the player's name
	    format = format.replace("<group-prefix>", getChatPrefix(eventPlayer)); //something like that
	    format = format.replace("<group-suffix>", chatSuffix); //something like that
	    format = format.replace("<message>", "%s");
	    e.setFormat(format);
	    
    }
    
    protected String getChatPrefix(Player p) {
    	String prefix = "";
    	if(configurations.get("chatactive")) {
	    	PlayerData sdata = SkillAPI.getPlayerData(p);
			if(sdata.hasClass()){
				prefix = chatPrefix;
				if(prefix.contains("%class%")) {
					String classprefix = sdata.getMainClass().getData().getPrefix();
					prefix = prefix.replaceAll("%class%", classprefix);
				}
				if(prefix.contains("%level%")) {
					int level = p.getLevel();
					prefix = prefix.replaceAll("%level%", "" + level);
				}
				if(prefix.contains("%color%")) {
					org.bukkit.ChatColor color = sdata.getMainClass().getData().getPrefixColor();
					prefix = prefix.replaceAll("%color%", "" + color);
				}
			}
    	}
    	return prefix;
    }
    
    protected void changeTabName(Player p)
    {
    	if(configurations.get("tablistName")) {
    		PlayerData sdata = SkillAPI.getPlayerData(p);
    		if(sdata.hasClass()){
    			String prefix = chatPrefix;
    			if(prefix.contains("%class%")) {
    				String classprefix = sdata.getMainClass().getData().getPrefix();
    				prefix = prefix.replaceAll("%class%", classprefix);
    			}
    			if(prefix.contains("%level%")) {
    				int level = p.getLevel();
    				prefix = prefix.replaceAll("%level%", "" + level);
    			}
    			if(prefix.contains("%color%")) {
    				org.bukkit.ChatColor color = sdata.getMainClass().getData().getPrefixColor();
    				prefix = prefix.replaceAll("%color%", "" + color);
    			}
    			p.setPlayerListName(prefix + p.getName());
    		}
    	}
    }
    
    @EventHandler
    public void clearExtraSkillSlots(PlayerJoinEvent e) {
    	if(configurations.get("skillSlotsActive")) {
	    	Player p = e.getPlayer();
	    	PlayerData sdata = SkillAPI.getPlayerData(p);
	    	boolean error = false;
	    	if(!sdata.getSkillBar().isEnabled()) {
	    		sdata.getSkillBar().toggleEnabled();
	    		error = true;
	    	}
	    	for(int i : nonSkillSlots) {
	    		if(!sdata.getSkillBar().isWeaponSlot(i)) {
	    			sdata.getSkillBar().toggleSlot(i);
	        		error = true;
	    		}
	    	}
	    	if(error) {
	    		p.getInventory().setHeldItemSlot(9);
	    	}
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void clearExtraSkillSlots4(InventoryClickEvent e) {
    	if(configurations.get("skillSlotsActive")) {
	    	HumanEntity ph = e.getWhoClicked();
	    	if(ph instanceof Player) {
	    		if(e.getClick().equals(ClickType.NUMBER_KEY)) {
	    			int slot = e.getSlot();
	    			if(!nonSkillSlots.contains(slot)) {
	    				e.setCancelled(true);
	    			}
	    		}
	    	}
    	}
    }
    
    @EventHandler
    public void onPotionSkill(PlayerItemConsumeEvent e)
    {
    	ItemStack pot = e.getItem();
    	if(pot.getType().equals(Material.POTION)) {
    		if(pot.hasItemMeta()) {
    			if(pot.getItemMeta().hasDisplayName()) {
    				String name = pot.getItemMeta().getDisplayName();
    				name = ChatColor.stripColor(name);
    				String[] skillString = name.split(potionSplit);
    				
    				final Skill skill = SkillAPI.getSkill(skillString[0]);
    				
    				if(skill != null) {
	    				if(isParsable(skillString[1])) {
		    				int castlvl = Integer.parseInt(skillString[1]);
		    				((DynamicSkill) skill).cast(e.getPlayer(), castlvl);
		    				e.setCancelled(true);
		    				removeFromInventoryWithName(e.getPlayer().getInventory(), pot.getItemMeta().getDisplayName(), 1);
	    				}
    				}
    			}
    		}
    	}
    }
    
    public void removeFromInventoryWithName(Inventory inventory, String name, int amount) {
    	ItemStack[] items = inventory.getContents();
        for (int i = 0; i < items.length; i++) {
        	if(items[i] != null) {
	        	if(items[i].hasItemMeta()){
	        		if(items[i].getItemMeta().hasDisplayName()){
			            if (items[i] != null && items[i].getItemMeta().getDisplayName().equals(name)) {
			                if (items[i].getAmount() > amount) {
			                    items[i].setAmount(items[i].getAmount() - amount);
			                    break;
			                } else if (items[i].getAmount() == amount) {
			                    items[i] = null;
			                    break;
			                } else {
			                	amount -= items[i].getAmount();
			                    items[i] = null;
			                }
			            }
	        		}
	        	}
        	}
        }
        inventory.setContents(items);
    }
}
