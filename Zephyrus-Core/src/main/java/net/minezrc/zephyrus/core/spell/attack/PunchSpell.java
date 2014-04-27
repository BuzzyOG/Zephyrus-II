package net.minezrc.zephyrus.core.spell.attack;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import net.minezrc.zephyrus.aspect.Aspect;
import net.minezrc.zephyrus.aspect.AspectList;
import net.minezrc.zephyrus.core.util.DataStructureUtils;
import net.minezrc.zephyrus.spell.ConfigurableSpell;
import net.minezrc.zephyrus.spell.Spell;
import net.minezrc.zephyrus.spell.SpellAttributes.CastResult;
import net.minezrc.zephyrus.spell.SpellAttributes.SpellElement;
import net.minezrc.zephyrus.spell.SpellAttributes.SpellType;
import net.minezrc.zephyrus.spell.SpellAttributes.TargetType;
import net.minezrc.zephyrus.spell.annotation.Bindable;
import net.minezrc.zephyrus.user.Targeted;
import net.minezrc.zephyrus.user.User;

/**
 * Zephyrus - Punch.java
 * 
 * @author minnymin3
 * 
 */

@Bindable
@Targeted(type = TargetType.ENTITY)
public class PunchSpell extends Spell implements ConfigurableSpell {

	private int damage;
	
	public PunchSpell() {
		super("punch", "Punches your target with a superpunch", 25, 2, AspectList.newList(Aspect.WEAPON, 30, Aspect.FLESH, 15, Aspect.MYSTICAL, 15), 1, SpellElement.NEUTREAL, SpellType.ATTACK);
	}

	@Override
	public CastResult onCast(User user, int power, String[] args) {
		user.getTarget(this.getDefaultName()).getEntity().damage(damage * power, user.getPlayer());
		return CastResult.SUCCESS;
	}

	@Override
	public Map<String, Object> getDefaultConfiguration() {
		Map<String, Object> map = DataStructureUtils.createConfigurationMap();
		map.put("Damage", 2);
		return map;
	}

	@Override
	public void loadConfiguration(ConfigurationSection config) {
		this.damage = config.getInt("Damage");
	}

}