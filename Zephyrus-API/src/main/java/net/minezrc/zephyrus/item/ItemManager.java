package net.minezrc.zephyrus.item;

import net.minezrc.zephyrus.Manager;

import org.bukkit.inventory.ItemStack;

/**
 * Zephyrus - ItemManager.java <br>
 * Represents the manager of items
 * 
 * @author minnymin3
 * 
 */

public interface ItemManager extends Manager {

	/**
	 * Gets an item by the specified name
	 * 
	 * @param name The name of the item to find
	 * @return A registered item or Null if no item was found
	 */
	public Item getItem(String name);

	/**
	 * Gets an item by the specified itemstack
	 * 
	 * @param item The itemstack to find
	 * @return A registered item or Null if no item was found
	 */
	public Item getItem(ItemStack item);

	/**
	 * Registers the specified item with Zephyrus and Bukkit
	 * 
	 * @param item The item to register
	 */
	public void registerItem(Item item);

}
