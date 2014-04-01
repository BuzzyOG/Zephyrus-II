package net.minezrc.zephyrus.core.spell.attack;

import net.minezrc.zephyrus.aspect.Aspect;
import net.minezrc.zephyrus.aspect.AspectList;
import net.minezrc.zephyrus.spell.Bindable;
import net.minezrc.zephyrus.spell.Spell;
import net.minezrc.zephyrus.spell.SpellAttributes.CastPriority;
import net.minezrc.zephyrus.spell.SpellAttributes.CastResult;
import net.minezrc.zephyrus.spell.SpellAttributes.SpellElement;
import net.minezrc.zephyrus.spell.SpellAttributes.SpellType;
import net.minezrc.zephyrus.user.User;

/**
 * Zephyrus - Arrow.java
 * 
 * @author minnymin3
 * 
 */

@Bindable
public class Arrow implements Spell {

	@Override
	public String getName() {
		return "arrow";
	}

	@Override
	public String getDescription() {
		return "Shoots an arrow";
	}

	@Override
	public int getRequiredLevel() {
		return 1;
	}

	@Override
	public int getManaCost() {
		return 30;
	}

	@Override
	public int getXpReward() {
		return 5;
	}

	@Override
	public AspectList getRecipe() {
		return AspectList.newList(Aspect.ATTACK, 10);
	}

	@Override
	public SpellElement getElement() {
		return SpellElement.AIR;
	}

	@Override
	public SpellType getType() {
		return SpellType.ATTACK;
	}

	@Override
	public CastPriority getPriority() {
		return CastPriority.LOW;
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public CastResult onCast(User user, int power, Spell combo, String[] args) {
		user.getPlayer().launchProjectile(org.bukkit.entity.Arrow.class);
		return CastResult.NORMAL_SUCCESS;
	}

}
