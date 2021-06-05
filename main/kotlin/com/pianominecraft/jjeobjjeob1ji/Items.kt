package com.pianominecraft.jjeobjjeob1ji

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

enum class Items(val item: ItemStack) {
    ZOMBIE_HEAD(ItemStack(Material.ZOMBIE_HEAD).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%aqua%좀비 머리"))
        }
    }),
    DIFFUSER(ItemStack(Material.DRAGON_BREATH).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%aqua%확산제"))
        }
    }),
    VACCINE(ItemStack(Material.LINGERING_POTION).apply {
        itemMeta = itemMeta.apply {
            (this as org.bukkit.inventory.meta.PotionMeta).basePotionData = org.bukkit.potion.PotionData(org.bukkit.potion.PotionType.SPEED)
            this.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
            setDisplayName(text("%aqua%백신"))
        }
    }),
    COMPACTED_NETHER_BRICK(ItemStack(Material.NETHER_BRICK).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%aqua%압축된 네더 벽돌"))
            addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
            addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, false)
        }
    }),
    COMPACTED_OBSIDIAN(ItemStack(Material.OBSIDIAN).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%aqua%압축된 흑요석"))
            addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
            addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, false)
        }
    }),
    COMPACTED_BLAZE_ROD(ItemStack(Material.BLAZE_ROD).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%aqua%압축된 블레이즈 막대"))
            addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
            addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, false)
        }
    }),
    COMPACTED_MAGMA_CREAM(ItemStack(Material.MAGMA_CREAM).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%aqua%압축된 마그마 크림"))
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
            addEnchant(Enchantment.MENDING, 1, false)
        }
    }),
    HYPER_VACCINE(ItemStack(Material.FIREWORK_ROCKET).apply {
        itemMeta = itemMeta.apply {
            setDisplayName(text("%dark_red%하이퍼 백신"))
        }
    }),
}