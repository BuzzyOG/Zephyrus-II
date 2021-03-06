package com.minnymin.zephyrus.core.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.minnymin.zephyrus.Zephyrus;
import com.minnymin.zephyrus.aspect.Aspect;
import com.minnymin.zephyrus.aspect.AspectList;
import com.minnymin.zephyrus.core.chat.Message;
import com.minnymin.zephyrus.core.chat.MessageComponent;
import com.minnymin.zephyrus.core.chat.MessageEvent.MessageHoverEvent;
import com.minnymin.zephyrus.core.chat.MessageForm.MessageColor;
import com.minnymin.zephyrus.core.chat.MessageForm.MessageFormatting;
import com.minnymin.zephyrus.core.config.ConfigOptions;
import com.minnymin.zephyrus.core.item.MagicBooks;
import com.minnymin.zephyrus.core.item.SpellTome;
import com.minnymin.zephyrus.core.util.Language;
import com.minnymin.zephyrus.core.util.command.Command;
import com.minnymin.zephyrus.core.util.command.CommandArgs;
import com.minnymin.zephyrus.core.util.command.Completer;
import com.minnymin.zephyrus.event.UserBindSpellEvent;
import com.minnymin.zephyrus.item.Item;
import com.minnymin.zephyrus.item.Wand;
import com.minnymin.zephyrus.spell.Spell;
import com.minnymin.zephyrus.spell.annotation.Bindable;
import com.minnymin.zephyrus.user.User;

/**
 * Zephyrus - ItemCommand.java
 * 
 * @author minnymin3
 * 
 */

public class ItemCommand {

	@Command(name = "spelltome",
			aliases = { "tome" },
			permission = "zephyrus.command.spelltome",
			description = "Gives a spelltome to the designated player",
			usage = "/spelltome <spell> [player]")
	public void onTomeCommand(CommandArgs args) {
		if (args.getArgs().length == 0) {
			Language.sendMessage("command.spell", args.getSender());
		} else if (args.getArgs().length == 1) {
			if (!args.isPlayer()) {
				Language.sendError("command.player", args.getSender());
				return;
			}
			Spell spell = Zephyrus.getSpell(args.getArgs()[0]);
			if (spell == null) {
				Language.sendError("command.spelltome.badspell", args.getSender());
				return;
			}
			args.getPlayer().getInventory().addItem(SpellTome.createSpellTome(spell));
			Language.sendMessage("command.spelltome.complete.self", args.getSender(), "[SPELL]", ChatColor.GOLD
					+ WordUtils.capitalize(spell.getName()));
		} else {
			Spell spell = Zephyrus.getSpell(args.getArgs()[0]);
			if (spell == null) {
				Language.sendError("command.spelltome.badspell", args.getSender());
				return;
			}
			Player player = Bukkit.getPlayer(args.getArgs()[1]);
			if (player == null) {
				Language.sendError("command.offline", args.getSender());
				return;
			}
			player.getInventory().addItem(SpellTome.createSpellTome(spell));
			Language.sendMessage("command.spelltome.complete", args.getSender(), "[PLAYER]", player.getName(),
					"[SPELL]", ChatColor.GOLD + WordUtils.capitalize(spell.getName()));
		}
	}

	@Command(name = "aspects",
			permission = "zephyrus.command.aspects",
			description = "Checks the aspects on an itemstack",
			usage = "/aspects")
	public void onAspects(CommandArgs args) {
		if (args.getArgs().length == 0) {
			if (!args.isPlayer()) {
				Language.sendError("command.ingame", args.getSender());
				return;
			}
			if (args.getPlayer().getItemInHand() == null) {
				Language.sendError("command.aspects.noitem", args.getSender());
				return;
			}
			AspectList list = Zephyrus.getAspectManager().getAspects(args.getPlayer().getItemInHand());
			Language.sendMessage("command.aspects.aspecttitle", args.getSender());
			if (list == null) {
				Language.sendError("command.aspects.none", args.getSender());
				return;
			}
			for (Entry<Aspect, Integer> entry : list.getAspectMap().entrySet()) {
				Language.sendMessage(
						"command.aspects.aspects",
						args.getSender(),
						"[NAME]",
						entry.getKey().getColor()
								+ Language.get("aspect." + entry.getKey().name().toLowerCase() + ".name", entry
										.getKey().getDefaultName()) + ChatColor.WHITE, "[DESC]", Language.get("aspect."
								+ entry.getKey().name() + ".desc", entry.getKey().getDefaultDescription()), "[AMOUNT]",
						entry.getValue() + "");
			}
		} else {
			Material mat = Material.getMaterial(args.getArgs()[0].toUpperCase());
			if (mat != null) {
				AspectList list = Zephyrus.getAspectManager().getAspects(new ItemStack(mat));
				Language.sendMessage("command.aspects.aspecttitle", args.getSender());
				if (list == null) {
					Language.sendError("command.aspects.none", args.getSender());
					return;
				}
				for (Entry<Aspect, Integer> entry : list.getAspectMap().entrySet()) {
					Language.sendMessage("command.aspects.aspects", args.getSender(), "[NAME]", Language.get("aspect."
							+ entry.getKey().name().toLowerCase() + ".name", entry.getKey().getDefaultName()),
							"[DESC]", Language.get("aspect." + entry.getKey().name() + ".desc", entry.getKey()
									.getDefaultDescription()), "[AMOUNT]", entry.getValue() + "");
				}
			}
		}
	}

	@Command(name = "aspects.list",
			permission = "zephyrus.command.aspects",
			description = "List all aspects",
			usage = "/aspects list")
	public void onAspectList(CommandArgs args) {
		if (args.isPlayer()) {
			Message message = new Message("command.aspects.aspecttitle", MessageColor.RED,
					MessageFormatting.BOLD);
			for (Aspect aspect : Aspect.values()) {
				message.addComponent(new MessageComponent(Language.get("aspect." + aspect.name().toLowerCase()
						+ ".name", aspect.getDefaultName()), MessageColor.valueOf(aspect.getColor().name()))
						.setHoverEvent(
								MessageHoverEvent.TEXT,
								Language.get("aspect." + aspect.name().toLowerCase() + ".desc",
										aspect.getDefaultDescription())));
				message.addComponent(new MessageComponent(" - ", MessageColor.BLACK, MessageFormatting.BOLD));
			}
			message.sendMessage(args.getPlayer());
		}
	}

	@Command(name = "book",
			permission = "zephyrus.command.book",
			description = "Allows each player to get one Zephyronomicon and a Mystic Recipe Book",
			usage = "/book <recipe|info>")
	public void onBookCommand(CommandArgs args) {
		if (!args.isPlayer()) {
			Language.sendError("command.ingame", args.getSender());
			return;
		}
		User user = Zephyrus.getUser(args.getPlayer());
		if (args.getArgs().length == 0) {
			String s = user.getData("book.info");
			if (Integer.valueOf(s == null ? 0 + "" : s) >= ConfigOptions.MAX_BOOKS) {
				Language.sendError("command.book.max", args.getSender(), "[BOOK]", "info");
				return;
			}
			args.getPlayer().getInventory().addItem(MagicBooks.createZephyronomicon());
			Language.sendMessage("command.book.info", args.getSender());
			user.setData("book.info", String.valueOf(Integer.valueOf(s == null ? 0 + "" : s) + 1));
		} else if (args.getArgs()[0].equalsIgnoreCase("info")) {
			String s = user.getData("book.info");
			if (Integer.valueOf(s == null ? 0 + "" : s) >= ConfigOptions.MAX_BOOKS) {
				Language.sendError("command.book.max", args.getSender(), "[BOOK]", "info");
				return;
			}
			args.getPlayer().getInventory().addItem(MagicBooks.createZephyronomicon());
			Language.sendMessage("command.book.info", args.getSender());
			user.setData("book.info", String.valueOf(Integer.valueOf(Integer.valueOf(s == null ? 0 + "" : s) + 1) + 1));
		} else if (args.getArgs()[0].equalsIgnoreCase("recipe")) {
			if (args.getArgs().length < 2) {
				String s = user.getData("book.recipe1");
				if (Integer.valueOf(s == null ? 0 + "" : s) >= ConfigOptions.MAX_BOOKS) {
					Language.sendError("command.book.max", args.getSender(), "[BOOK]", "recipe");
					return;
				}
				args.getPlayer().getInventory().addItem(MagicBooks.createZephyricRecipeBook(1, 5));
				Language.sendMessage("command.book.recipe", args.getSender(), "[START-LEVEL]", String.valueOf(1),
						"[END-LEVEL]", String.valueOf(5));
				user.setData("book.recipe1", String.valueOf(Integer.valueOf(s == null ? 0 + "" : s) + 1));
			} else {
				int teir = 1;
				try {
					teir = Integer.parseInt(args.getArgs()[1]);
				} catch (Exception ex) {
				}
				int level = (teir - 1) * 5 + 1;
				if (user.getLevel() < level) {
					Language.sendError("command.book.recipe.reqlevel", args.getSender(), "[LEVEL]",
							String.valueOf(level), "[TEIR]", String.valueOf(teir));
					return;
				}
				String s = user.getData("book.recipe" + teir);
				if (Integer.valueOf(s == null ? 0 + "" : s) >= ConfigOptions.MAX_BOOKS) {
					Language.sendError("command.book.max", args.getSender(), "[BOOK]", "recipe teir " + teir);
					return;
				}
				args.getPlayer().getInventory().addItem(MagicBooks.createZephyricRecipeBook(level, level + 4));
				Language.sendMessage("command.book.recipe", args.getSender(), "[START-LEVEL]", String.valueOf(level),
						"[END-LEVEL]", String.valueOf(level + 4));
				user.setData("book.recipe" + teir, String.valueOf(Integer.valueOf(s == null ? 0 + "" : s) + 1));
			}
		} else {
			Language.sendError("command.book.unknown", args.getSender());
		}
	}

	@Command(name = "bind",
			permission = "zephyrus.command.bind",
			description = "Binds a spell to the wand held in your hand",
			usage = "/bind <spell>")
	public void onBind(CommandArgs args) {
		if (!args.isPlayer()) {
			Language.sendError("command.ingame", args.getSender());
			return;
		}
		if (args.getArgs().length == 0) {
			Language.sendMessage("command.spell", args.getSender());
			return;
		}
		User user = Zephyrus.getUser(args.getPlayer().getName());
		Spell spell = Zephyrus.getSpell(args.getArgs()[0]);
		Item item = Zephyrus.getItem(args.getPlayer().getItemInHand());
		if (item == null || !(item instanceof Wand)) {
			Language.sendError("command.bind.nowand", args.getSender());
			return;
		}
		Wand wand = (Wand) item;
		if (spell == null || !user.isSpellLearned(spell)) {
			Language.sendError("command.bind.learn", args.getSender(), "[SPELL]", args.getArgs()[0]);
			return;
		}
		if (!spell.getClass().isAnnotationPresent(Bindable.class)) {
			Language.sendError("command.bind.unable", args.getSender());
			return;
		}
		if (wand.getBindingAbilityLevel() < spell.getRequiredLevel()) {
			Language.sendError("commabd.bind.level", args.getSender());
			return;
		}
		UserBindSpellEvent event = new UserBindSpellEvent(args.getPlayer(), spell, wand);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			ItemStack stack = args.getPlayer().getItemInHand();
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName(wand.getBoundName(spell));
			meta.setLore(wand.getBoundLore(spell));
			stack.setItemMeta(meta);
			args.getPlayer().setItemInHand(stack);
			Language.sendMessage("command.bind.complete", args.getSender(), "[SPELL]", ChatColor.GOLD + spell.getName()
					+ ChatColor.WHITE);
		}
	}

	@Command(name = "unbind",
			aliases = { "bind.none", "bind.remove" },
			permission = "zephyrus.command.bind",
			description = "Removes the bound spell from the wand",
			usage = "/unbind")
	public void onBindNone(CommandArgs args) {
		if (!args.isPlayer()) {
			Language.sendError("command.ingame", args.getSender());
			return;
		}
		Item item = Zephyrus.getItem(args.getPlayer().getItemInHand());
		if (item == null || !(item instanceof Wand)) {
			Language.sendError("command.unbind.nowand", args.getSender());
			return;
		}
		Wand wand = (Wand) item;
		ItemStack stack = args.getPlayer().getItemInHand();
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(wand.getName());
		meta.setLore(wand.getLore());
		stack.setItemMeta(meta);
		args.getPlayer().setItemInHand(stack);
		Language.sendMessage("command.unbind.complete", args.getSender());
	}

	@Completer(name = "spelltome", aliases = { "tome", "learn", "teach" })
	public List<String> onTomeComplete(CommandArgs cmd) {
		List<String> list = new ArrayList<String>();
		for (String s : Zephyrus.getSpellNameSet()) {
			list.add(s);
		}
		if (cmd.getArgs().length == 0) {
			return list;
		}
		String spell = cmd.getArgs()[0];
		List<String> newList = new ArrayList<String>();
		for (String s : list) {
			if (s.startsWith(spell.toLowerCase())) {
				newList.add(s);
			}
		}
		return newList;
	}

}
