package com.pianominecraft.jjeobjjeob1ji

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.SplashPotion
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EventManager : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.joinMessage = ""
        if (wl && e.player.name !in plugin.survivors) {
            e.player.kickPlayer("멈춰!")
            return
        } // survivor whitelist

        if (e.player.name !in plugin.survivors && e.player.name !in plugin.zombies) {
            plugin.zombies.add(e.player.name)
            plugin.normalZombies.add(e.player.name)
        } // init

        if (e.player.name in plugin.survivors) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("a")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("a")
            e.player.setPlayerListName(e.player.name)
            e.player.customName = e.player.name
        } // survivor
        if (e.player.name in plugin.normalZombies) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("g")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("g")
            e.player.setPlayerListName(text("%red%${e.player.name}"))
            e.player.customName = text("%red%${e.player.name}")
        } // normal zombie
        if (e.player.name in plugin.superZombies) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("b")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("b")
            e.player.setPlayerListName(text("%dark_red%${e.player.name} %gold%[S]"))
            e.player.customName = text("%dark_red%${e.player.name} %gold%[S]")
        } // super zombie
        if (e.player.name in plugin.giantZombies) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("d")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("d")
            e.player.setPlayerListName(text("%red%${e.player.name} %dark_green%[G]"))
            e.player.customName = text("%red%${e.player.name} %dark_green%[G]")
        } // giant zombie
        if (e.player.name in plugin.speedZombies) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("f")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("f")
            e.player.setPlayerListName(text("%red%${e.player.name} %aqua%[S]"))
            e.player.customName = text("%red%${e.player.name} %aqua%[S]")
        } // speed zombie
        if (e.player.name in plugin.intelligentZombies) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("c")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("c")
            e.player.setPlayerListName(text("%red%${e.player.name} %dark_aqua%[I]"))
            e.player.customName = text("%red%${e.player.name} %dark_aqua%[I]")
        } // intelligent zombie
        if (e.player.name in plugin.explosionZombies) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam("e")?.addEntry(e.player.name)
                ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("e")
            e.player.setPlayerListName(text("%red%${e.player.name} %yellow%[E]"))
            e.player.customName = text("%red%${e.player.name} %yellow%[E]")
        } // explosion zombie

        if (!e.player.hasPlayedBefore()) {
            val x = Math.random() * 2000
            val z = Math.random() * 2000
            var y = 256.0
            while (Location(e.player.world, x, y - 1, z).block.type == Material.AIR) y--
            e.player.teleport(Location(e.player.world, x, y, z))
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        e.quitMessage = ""
    }

    @EventHandler
    fun onDamage(e: EntityDamageByEntityEvent) {
        if (e.entity is Player && e.damager is Player) {
            if (e.entity.name in plugin.zombies && e.damager.name in plugin.zombies) {
                e.isCancelled = true
            }
            if (e.entity.name in plugin.survivors) {
                when (e.damager.name) {
                    in plugin.survivors -> {
                        e.isCancelled = true
                    }
                    in plugin.normalZombies -> {
                        e.damage = e.damage * 0.5
                        (e.entity as Player).addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 0, false, false))
                    }
                    in plugin.intelligentZombies -> {
                        e.damage = 0.0
                        (e.entity as Player).addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 0, false, false))
                    }
                    in plugin.superZombies -> {
                        e.damage = e.damage * 0.7
                    }
                    in plugin.giantZombies -> {
                        if (e.damage > 1) {
                            e.damage = 2.0
                        } else {
                            e.damage = e.damage * 2
                        }
                    }
                    in plugin.speedZombies -> {
                        e.damage = e.damage * 0.3
                    }
                    in plugin.explosionZombies -> {
                        e.damage = e.damage * 0.5
                        (e.damager as Player).damage(e.finalDamage)
                        Bukkit.getOnlinePlayers().forEach {
                            it.playSound(e.entity.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                            it.spawnParticle(Particle.EXPLOSION_LARGE, e.entity.location, 10, 1.0, 1.0, 1.0, 0.0)
                        }
                        e.entity.getNearbyEntities(3.0, 3.0, 3.0).filter {
                            it.name in plugin.survivors
                        }.forEach {
                            (it as Player).damage(e.finalDamage / 4)
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onProjectileLaunch(e: ProjectileLaunchEvent) {
        if (e.entity.shooter is Player) {
            with ((e.entity.shooter as Player).name) {
                if (this !in plugin.survivors && this !in plugin.intelligentZombies && this !in plugin.superZombies) {
                    e.isCancelled = true
                    return
                }
                if (e.entityType == EntityType.SPLASH_POTION) {
                    if (this !in plugin.survivors) {
                        e.isCancelled = true
                        return
                    }
                    if ((e.entity as ThrownPotion).item.itemMeta.displayName == text("%aqua%백신")) {
                        e.entity.velocity = (e.entity.shooter!! as Player).location.direction
                    }
                }
                else if (this in plugin.survivors) {
                }
            }
        }
    }

    @EventHandler
    fun onPotionSplash(e: LingeringPotionSplashEvent) {
        if (e.entity.item == Items.VACCINE.item) {
            Bukkit.getOnlinePlayers().forEach { p ->
                p.spawnParticle(Particle.END_ROD, e.entity.location, 100, 0.0, 0.0, 0.0, 0.2)
                p.spawnParticle(Particle.TOTEM, e.entity.location, 100, 0.0, 0.0, 0.0, 0.5)
                p.playSound(e.entity.location, Sound.ITEM_TOTEM_USE, 1f, 1f)
            }
            e.entity.getNearbyEntities(3.0, 3.0, 3.0).filterIsInstance<Player>().filter {
                it.name in plugin.zombies
            }.forEach { p ->
                plugin.remove(p.name)
                plugin.survivors.add(p.name)
                Bukkit.broadcastMessage(text("%aqua%${p.name}님이 치료되었습니다!"))
                p.sendTitle(text("%green%회복!"), text("%green%이제 당신은 %white%생존자%green%입니다"), 0, 80, 20)
            }
            e.areaEffectCloud.remove()
        }
    }

    @EventHandler
    fun onEat(e: PlayerItemConsumeEvent) {
        if (e.item.type == Material.COOKIE) {
            if (e.player.name in plugin.survivors) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 100, 2, false, false))
                e.player.playSound(e.player.location, Sound.ENTITY_GENERIC_EAT, 1f, 1f)
                e.player.sendMessage(text("%aqua%쿠키를 먹으니 힘이 솟아오른다!"))
            }
        }
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        if (e.entity.name in plugin.zombies) {
            if ((Math.random() * 20).toInt() < 1) {
                e.entity.world.dropItemNaturally(e.entity.location, Items.ZOMBIE_HEAD.item)
            }
        }
        else {
            if (e.entity.name in plugin.superSurvivors) {
                plugin.remove(e.entity.name)
                plugin.zombies.add(e.entity.name)
                plugin.superZombies.add(e.entity.name)
                Bukkit.getOnlinePlayers().forEach { p ->
                    p.sendTitle(
                        text("%dark_red%${e.entity.name} 사망!"),
                        text("%dark_red%${e.entity.name}님이 슈퍼 좀비가 되었습니다"),
                        20,
                        60,
                        20
                    )
                }
                Bukkit.getScoreboardManager().mainScoreboard.getTeam("b")?.addEntry(e.entity.name)
                    ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("b")
                e.entity.setPlayerListName(text("%dark_red%${e.entity.name} %gold%[S]"))
                e.entity.customName = text("%dark_red%${e.entity.name} %gold%[S]")
            } else {
                plugin.remove(e.entity.name)
                plugin.zombies.add(e.entity.name)
                plugin.normalZombies.add(e.entity.name)
                Bukkit.getOnlinePlayers().forEach { p ->
                    p.sendTitle(
                        text("%red%${e.entity.name} 사망!"),
                        text("%red%${e.entity.name}님이 좀비가 되었습니다"),
                        20,
                        60,
                        20
                    )
                }
                Bukkit.getScoreboardManager().mainScoreboard.getTeam("g")?.addEntry(e.entity.name)
                    ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("g")
                e.entity.setPlayerListName(text("%red%${e.entity.name}"))
                e.entity.customName = text("%red%${e.entity.name}")
            }
        }
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e.cause == EntityDamageEvent.DamageCause.FALL) {
            if (e.entity is Player) {
                if ((e.entity as Player).inventory.boots != null) {
                    val i = (e.entity as Player).inventory.boots!!
                    i.itemMeta = i.itemMeta.apply {
                        if (this is Damageable) {
                            damage += e.damage.toInt()
                        }
                    }
                    if ((i.itemMeta as Damageable).damage >= i.type.maxDurability) {
                        i.amount = 0
                        Bukkit.getOnlinePlayers().forEach { p ->
                            p.playSound(e.entity.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                        }
                    }
                    when (i.type) {
                        Material.GOLDEN_BOOTS -> {
                            e.damage = e.damage * 0.8
                        }
                        Material.CHAINMAIL_BOOTS -> {
                            e.damage = e.damage * 0.6
                        }
                        Material.IRON_BOOTS -> {
                            e.damage = e.damage * 0.4
                        }
                        Material.DIAMOND_BOOTS -> {
                            e.damage = e.damage * 0.2
                        }
                        Material.NETHERITE_BOOTS -> {
                            e.damage = 0.0
                        }
                    }
                }
            }
        }
        else if (e.cause in listOf(
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
        )) {
            if (e.entity is Player) {
                if ((e.entity as Player).inventory.leggings != null) {
                    val i = (e.entity as Player).inventory.leggings!!
                    i.itemMeta = i.itemMeta.apply {
                        if (this is Damageable) {
                            damage += e.damage.toInt()
                        }
                    }
                    if ((i.itemMeta as Damageable).damage >= i.type.maxDurability) {
                        i.amount = 0
                        Bukkit.getOnlinePlayers().forEach { p ->
                            p.playSound(e.entity.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                        }
                    }
                    when (i.type) {
                        Material.GOLDEN_LEGGINGS -> {
                            e.damage = e.damage * 0.8
                        }
                        Material.CHAINMAIL_LEGGINGS -> {
                            e.damage = e.damage * 0.6
                        }
                        Material.IRON_LEGGINGS -> {
                            e.damage = e.damage * 0.4
                        }
                        Material.DIAMOND_LEGGINGS -> {
                            e.damage = e.damage * 0.2
                        }
                        Material.NETHERITE_LEGGINGS -> {
                            e.damage = 0.0
                        }
                    }
                }
            }
        }
        else if (e.cause in listOf(
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.FIRE
        )) {
            if (e.entity is Player) {
                if ((e.entity as Player).inventory.chestplate != null) {
                    val i = (e.entity as Player).inventory.chestplate!!
                    i.itemMeta = i.itemMeta.apply {
                        if (this is Damageable) {
                            damage += e.damage.toInt()
                        }
                    }
                    if ((i.itemMeta as Damageable).damage >= i.type.maxDurability) {
                        i.amount = 0
                        Bukkit.getOnlinePlayers().forEach { p ->
                            p.playSound(e.entity.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                        }
                    }
                    when (i.type) {
                        Material.GOLDEN_CHESTPLATE -> {
                            e.damage = e.damage * 0.8
                        }
                        Material.CHAINMAIL_CHESTPLATE -> {
                            e.damage = e.damage * 0.6
                        }
                        Material.IRON_CHESTPLATE -> {
                            e.damage = e.damage * 0.4
                        }
                        Material.DIAMOND_CHESTPLATE -> {
                            e.damage = e.damage * 0.2
                        }
                        Material.NETHERITE_CHESTPLATE -> {
                            e.damage = 0.0
                        }
                    }
                }
            }
        }
        else if (e.cause in listOf(
                EntityDamageEvent.DamageCause.DROWNING,
                EntityDamageEvent.DamageCause.SUFFOCATION
        )) {
            if (e.entity is Player) {
                if ((e.entity as Player).inventory.helmet != null) {
                    val i = (e.entity as Player).inventory.helmet!!
                    i.itemMeta = i.itemMeta.apply {
                        if (this is Damageable) {
                            damage += e.damage.toInt()
                        }
                    }
                    if ((i.itemMeta as Damageable).damage >= i.type.maxDurability) {
                        i.amount = 0
                        Bukkit.getOnlinePlayers().forEach { p ->
                            p.playSound(e.entity.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                        }
                    }
                    when (i.type) {
                        Material.GOLDEN_HELMET -> {
                            e.damage = e.damage * 0.8
                        }
                        Material.CHAINMAIL_HELMET -> {
                            e.damage = e.damage * 0.6
                        }
                        Material.IRON_HELMET -> {
                            e.damage = e.damage * 0.4
                        }
                        Material.DIAMOND_HELMET -> {
                            e.damage = e.damage * 0.2
                        }
                        Material.NETHERITE_HELMET -> {
                            e.damage = 0.0
                        }
                    }
                }
            }
        }
        if (time > 0) e.isCancelled = true
    }

    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {
        e.isCancelled = true
        if (e.player.name in plugin.superZombies) {
            plugin.zombies.forEach {
                Bukkit.getPlayer(it)?.sendMessage(text("%yellow%[ %red%Super Zombie %yellow%] %red%${e.player.name} %white%: ${e.message}"))
            }
        }
    }

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isOp) e.isCancelled = true
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.action in listOf(
                Action.RIGHT_CLICK_BLOCK,
                Action.RIGHT_CLICK_AIR
        )) {
            if (e.player.itemInHand.type == Material.COMPASS) {
                if (e.player.name in plugin.superZombies) {
                    Bukkit.getPlayer(plugin.survivors.random())?.let {
                        target[e.player] = it
                    }
                }
            }
        }
    }

    @EventHandler
    fun onCraft(e: CraftItemEvent) {
        if (e.whoClicked.name in plugin.survivors) return
        val item = e.currentItem ?: return
        if ("백신" in item.itemMeta.displayName) {
            e.isCancelled = true
            return
        }
        if ("DIAMOND" in item.type.name || item.type == Material.SHIELD) {
            if (e.whoClicked.name in plugin.intelligentZombies) {
                if (intelligentCool[e.whoClicked.uniqueId] == 0) {
                    intelligentCool[e.whoClicked.uniqueId] = 600
                } else {
                    if (intelligentCool[e.whoClicked.uniqueId]!! / 20 > 9) {
                        e.whoClicked.sendMessage(text("%red%다음 %gold%지능적인 조합%red%까지 남은 시간 ${intelligentCool[e.whoClicked.uniqueId]!! % 1200 / 20}초"))
                    } else {
                        e.whoClicked.sendMessage(text("%red%다음 %gold%지능적인 조합%red%까지 남은 시간 0${intelligentCool[e.whoClicked.uniqueId]!! % 1200 / 20}초"))
                    }
                    e.isCancelled = true
                }
            } else {
                e.isCancelled = true
                e.whoClicked.sendMessage(text("%red%당신은 이 아이템을 조합할 수 없습니다!"))
            }
        }
    }

    @EventHandler
    fun onPortal(e: PlayerPortalEvent) {
        if (e.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) e.isCancelled = true
    }

}