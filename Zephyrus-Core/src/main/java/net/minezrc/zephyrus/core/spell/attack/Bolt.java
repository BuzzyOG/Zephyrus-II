package net.minezrc.zephyrus.core.spell.attack;

import net.minezrc.zephyrus.aspect.Aspect;
import net.minezrc.zephyrus.aspect.AspectList;
import net.minezrc.zephyrus.core.projectile.DamagingBeamProjectile;
import net.minezrc.zephyrus.core.util.ParticleEffects.Particle;
import net.minezrc.zephyrus.spell.Spell;
import net.minezrc.zephyrus.spell.SpellAttributes.CastResult;
import net.minezrc.zephyrus.spell.SpellAttributes.SpellElement;
import net.minezrc.zephyrus.spell.SpellAttributes.SpellType;
import net.minezrc.zephyrus.spell.annotation.Bindable;
import net.minezrc.zephyrus.user.User;

/**
 * Zephyrus - Bolt.java
 * 
 * @author minnymin3
 * 
 */

@Bindable
public class Bolt extends Spell {

	public Bolt() {
		super("bolt", "Launches a ball of electricity", 20, 1, AspectList.newList()
				.setAspectTypes(Aspect.WIND, Aspect.POWER).setAspectValues(8, 4), 1, SpellElement.AIR, SpellType.ATTACK);
	}

	@Override
	public CastResult onCast(User user, int power, String[] args) {
		DamagingBeamProjectile projectile = new DamagingBeamProjectile(Particle.MAGIC_CRITICAL_HIT, 4);
		projectile.launchProjectile(user.getPlayer());
		return CastResult.SUCCESS;
	}

}