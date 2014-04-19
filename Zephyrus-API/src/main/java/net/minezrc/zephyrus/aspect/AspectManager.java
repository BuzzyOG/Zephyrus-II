package net.minezrc.zephyrus.aspect;

import net.minezrc.zephyrus.Manager;

import org.bukkit.inventory.ItemStack;

/**
 * Zephyrus - AspectManager.java <br>
 * Represents the manager of aspects and storage of aspects per item
 * 
 * @author minnymin3
 * 
 */

public interface AspectManager extends Manager {

	/**
	 * Gets the aspects that pertain to itemstack
	 * 
	 * @param item The itemstack to get the aspects of
	 * @return An aspect list
	 */
	public AspectList getAspects(ItemStack item);

}
