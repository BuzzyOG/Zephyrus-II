package com.minnymin.zephyrus.spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import com.minnymin.zephyrus.Configurable;
import com.minnymin.zephyrus.YmlConfigFile;
import com.minnymin.zephyrus.Zephyrus;
import com.minnymin.zephyrus.aspect.Aspect;
import com.minnymin.zephyrus.aspect.AspectList;
import com.minnymin.zephyrus.spell.SpellAttributes.CastResult;
import com.minnymin.zephyrus.spell.SpellAttributes.SpellElement;
import com.minnymin.zephyrus.spell.SpellAttributes.SpellType;
import com.minnymin.zephyrus.user.User;

/**
 * Zephyrus - Spell.java<br>
 * Represents a spell
 * 
 * @author minnymin3
 * 
 */

public abstract class Spell {

	private final String defaultName;

	private String description;
	private SpellElement element;
	private int manaCost;
	private String name;
	private AspectList recipe;
	private int requiredLevel;

	private SpellType type;
	private int xpReward;

	public Spell(String name, String description, int manaCost, int xpReward, AspectList recipe, int requiredLevel,
			SpellElement element, SpellType type) {
		this.defaultName = name.toLowerCase();
		YmlConfigFile yml = Zephyrus.getSpellConfig();
		yml.addDefaults(defaultName + ".Enabled", true);
		yml.addDefaults(defaultName + ".Name", name.toLowerCase());
		yml.addDefaults(defaultName + ".Description", description);
		yml.addDefaults(defaultName + ".ManaCost", manaCost);
		yml.addDefaults(defaultName + ".XpReward", xpReward);
		yml.addDefaults(defaultName + ".Recipe", toList(recipe));
		yml.addDefaults(defaultName + ".RequiredLevel", requiredLevel);
		yml.saveConfig();

		if (this instanceof Configurable) {
			Iterator<Entry<String, Object>> iter = ((Configurable) this).getDefaultConfiguration().entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, Object> value = iter.next();
				yml.addDefaults(defaultName + "." + value.getKey(), value.getValue());
			}
		}

		ConfigurationSection config = yml.getConfig().getConfigurationSection(defaultName);
		this.name = config.getString("Name");
		this.description = config.getString("Description");
		this.manaCost = config.getInt("ManaCost");
		this.xpReward = config.getInt("XpReward");
		this.recipe = fromList(config.getStringList("Recipe"));
		this.requiredLevel = config.getInt("RequiredLevel");

		this.element = element;
		this.type = type;
	}

	private AspectList fromList(List<String> list) {
		List<Aspect> aspectType = new ArrayList<Aspect>();
		List<Integer> aspectValue = new ArrayList<Integer>();
		for (String s : list) {
			String[] split = s.split("-");
			try {
				Aspect aspect = Aspect.valueOf(split[0]);
				int value = Integer.parseInt(split[1]);
				aspectType.add(aspect);
				aspectValue.add(value);
			} catch (Exception ex) {
				// Catch any syntax errors caused by the user
			}
		}
		return AspectList.newList().setAspectLists(aspectType, aspectValue);
	}

	/**
	 * Gets the default name as originally defined in the constructor
	 * 
	 * @return The name of the spell
	 */
	public String getDefaultName() {
		return this.defaultName;
	}

	/**
	 * Gets the description of the spell configured by the user
	 * 
	 * @return The description of the spell
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the element of the spell
	 * 
	 * @return The SpellElement of the spell
	 */
	public SpellElement getElement() {
		return this.element;
	}

	/**
	 * Gets the mana cost of the spell configured by the user
	 * 
	 * @return The mana cost of the spell
	 */
	public int getManaCost() {
		return this.manaCost;
	}

	/**
	 * Gets the name of the spell configured by the user
	 * 
	 * @return The name of the spell
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the recipe of the spell configured by the user
	 * 
	 * @return The recipe of the spell
	 */
	public AspectList getRecipe() {
		return this.recipe;
	}

	/**
	 * Gets the required level of the spell configured by the user
	 * 
	 * @return The required level of the spell
	 */
	public int getRequiredLevel() {
		return this.requiredLevel;
	}

	/**
	 * Gets the type of the spell
	 * 
	 * @return The SpellType of the spell
	 */
	public SpellType getType() {
		return this.type;
	}

	/**
	 * Gets the xp reward of the spell configured by the user
	 * 
	 * @return The xp reward of the spell
	 */
	public int getXpReward() {
		return this.xpReward;
	}

	/**
	 * Called when the player casts the spell
	 * 
	 * @param user The user who cast the spell
	 * @param power The power this spell was cast with
	 * @param args The arguments this spell was cast with
	 * @return SUCCESS to drain mana and call post cast tasks, FAILURE to not
	 *         drain mana
	 */
	public abstract CastResult onCast(User user, int power, String[] args);

	/**
	 * Called when Zephyrus is enabled
	 */
	public void onDisable() {
	}

	/**
	 * Called when Zephyrus is disabled
	 */
	public void onEnable() {
	}

	private List<String> toList(AspectList recipe) {
		Map<Aspect, Integer> aspects = recipe.getAspectMap();
		List<String> list = new ArrayList<String>();
		for (Aspect aspect : aspects.keySet()) {
			list.add(aspect + "-" + aspects.get(aspect));
		}
		return list;
	}

}
