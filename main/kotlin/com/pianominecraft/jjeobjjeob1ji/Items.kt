package com.pianominecraft.jjeobjjeob1ji

import org.bukkit.Material
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
            this.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_POTION_EFFECTS)
            setDisplayName(text("%aqua%백신"))
        }
    })
}