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
		if(!itemStack.getItemMeta().getLore().get(0).contains("��ʯ����-")) return;
		List<String> newLores = new ArrayList<>();
		for (String string : itemStack.getItemMeta().getLore()) {
			if(!string.contains("#")&&!string.contains("��ʯ����-")) newLores.add(string);
		}
		String type = itemStack.getItemMeta().getLore().get(0).replaceAll("��ʯ����-","");
		ItemStack target = e.getPlayer().getInventory().getItemInOffHand();
		
		switch (type) {
		case "�޸�":
			if(target==null||target.getType()==Material.AIR) {
				e.getPlayer().sendMessage("���ڸ���������Ҫ�޸�����Ʒ");
			}
			if(target.getType().getMaxDurability()!=0) {
				e.getPlayer().sendMessage("����Ʒ�޷��޸�");
				return;
			}
			if(antiRepair(target))
				e.getPlayer().sendMessage("����Ʒ������ʹ���޸�ʯ�޸�");
			e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
			break;
		case "ӡ��":
			if(target==null||target.getType()==Material.AIR) {
				e.getPlayer().sendMessage("���ڸ���������Ҫӡ������Ʒ");
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
			e.getPlayer().sendMessage("�ɹ����ӡ��");
			e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
			break;
		case "��Ƭ":
			if(target==null||target.getType()==Material.AIR) {
				e.getPlayer().sendMessage("���ڸ���������Ҫ�γ�ӡ������Ʒ");
			}
			ItemMeta iMeta2 = e.getPlayer().getInventory().getItemInOffHand().getItemMeta();
			iMeta2.setLore(new ArrayList<>());
			e.getPlayer().getInventory().getItemInOffHand().setItemMeta(iMeta2);
			e.getPlayer().sendMessage("�ɹ��γ�����ӡ��");
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
			if(string.contains("����ʹ���޸�ʯ"))
				noRepair = true;
				break;
		}
		return noRepair;
	}
	    
}
