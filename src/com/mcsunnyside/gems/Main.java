package com.mcsunnyside.gems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	List<String> uuid = new ArrayList<>();
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uuid.clear();
			}
		}.runTaskTimer(this, 5, 5);
	}
	@Override
	public void onDisable() {

	}

	
	@EventHandler
	 public void onRightClick(PlayerInteractEvent e)
	 {
		if(uuid.contains(e.getPlayer().getUniqueId().toString())) return;
		if(e.getClass()!=PlayerInteractEvent.class) return;
		if(e.getAction() != Action.RIGHT_CLICK_AIR) return;
		if(e.getPlayer().getInventory().getItemInMainHand()==null) return;
		if(!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) return;
		if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) return;
		ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();
		if(!itemStack.getItemMeta().getLore().get(0).contains("宝石类型-")) return;
		List<String> newLores = new ArrayList<>();
		for (String string : itemStack.getItemMeta().getLore()) {
			if(!string.contains("#")&&!string.contains("宝石类型-")) newLores.add(string);
		}
		String type = itemStack.getItemMeta().getLore().get(0).replaceAll("宝石类型-","");
		ItemStack target = e.getPlayer().getInventory().getItemInOffHand();
		
		switch (type) {
		case "修复":
			if(target==null||target.getType()==Material.AIR) {
				e.getPlayer().sendMessage("请在副手拿着需要修复的物品");
			}
			if(target.getType().getMaxDurability()!=0) {
				e.getPlayer().sendMessage("此物品无法修复");
				return;
			}
			if(antiRepair(target))
				e.getPlayer().sendMessage("此物品不允许使用修复石修复");
			e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
			break;
		case "印花":
			if(target==null||target.getType()==Material.AIR) {
				e.getPlayer().sendMessage("请在副手拿着需要印花的物品");
			}
			List<String> finalLore = new ArrayList<String>();
			if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()&&e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore()!=null) {
				finalLore = e.getPlayer().getInventory().getItemInOffHand().getItemMeta().getLore();
			}
			for (String string : newLores) {
				getLogger().info(string);
				try {
					finalLore.add(string);
				}catch (NullPointerException ex) {
					finalLore=new ArrayList<String>();
					finalLore.add(string);
				}
			}
			ItemMeta iMeta = e.getPlayer().getInventory().getItemInOffHand().getItemMeta();
			iMeta.setLore(finalLore);
			e.getPlayer().getInventory().getItemInOffHand().setItemMeta(iMeta);
			e.getPlayer().sendMessage("成功添加印花");
			e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
			break;
		case "刀片":
			if(target==null||target.getType()==Material.AIR) {
				e.getPlayer().sendMessage("请在副手拿着需要刮除印花的物品");
			}
			ItemMeta iMeta2 = e.getPlayer().getInventory().getItemInOffHand().getItemMeta();
			iMeta2.setLore(new ArrayList<>());
			e.getPlayer().getInventory().getItemInOffHand().setItemMeta(iMeta2);
			e.getPlayer().sendMessage("成功刮除所有印花");
			e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
			break;
		default:
			break;
		}
		uuid.add(e.getPlayer().getUniqueId().toString());
	  }
	boolean antiRepair(ItemStack is) {
		if(!is.hasItemMeta()||!is.getItemMeta().hasLore()) return false;
		List<String> lores = is.getItemMeta().getLore();
		boolean noRepair = false;
		for (String string : lores) {
			if(string.contains("不可使用修复石"))
				noRepair = true;
				break;
		}
		return noRepair;
	}
	    
}
