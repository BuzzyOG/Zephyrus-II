package net.minezrc.zephyrus.event;

import net.minezrc.zephyrus.user.User;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Zephyrus - UserTargetEntityEvent.java
 * 
 * @author minnymin3
 * 
 */

public class UserTargetBlockEvent extends UserEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private Block target;
	private boolean cancelled = false;
	
	public UserTargetBlockEvent(Player player, Block target) {
		super(player);
		this.target = target;
	}
	
	public UserTargetBlockEvent(User user, Block target) {
		super(user);
		this.target = target;
	}

	/**
	 * Gets the block the user is trying to target
	 * 
	 * @return A Block target
	 */
	public Block getTarget() {
		return target;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}