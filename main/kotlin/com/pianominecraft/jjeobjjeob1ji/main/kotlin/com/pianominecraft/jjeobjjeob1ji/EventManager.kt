package com.pianominecraft.jjeobjjeob1ji

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.lang.Exception
import kotlin.math.sqrt

@Suppress("DEPRECATION", "NON_EXHAUSTIVE_WHEN")
class EventManager : Listener {

    @EventHandler fun onJoin(e: PlayerJoinEvent) {
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
    @EventHandler fun onQuit(e: PlayerQuitEvent) {
        e.quitMessage = ""
    }
    @EventHandler fun onDamage(e: EntityDamageByEntityEvent) {
        if (e.entity is Player && e.damager is Player) {
            if (e.entity.name in plugin.zombies && e.damager.name in plugin.zombies) {
                e.isCancelled = true
                return
            }
            else if (e.entity.name in plugin.survivors) {
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
                        (e.entity as Player).addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 0, false, false))
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
            else if (e.entity.name in plugin.giantZombies) {
                e.isCancelled = true
                (e.entity as Player).damage(e.damage)
            }
        }
    }
    @EventHandler fun onProjectileLaunch(e: ProjectileLaunchEvent) {
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
                    if ((e.entity as ThrownPotion).item.isSimilar(Items.VACCINE.item) || (e.entity as ThrownPotion).item.isSimilar(Items.COMPACTED_VACCINE.item)) {
                        e.entity.velocity = (e.entity.shooter!! as Player).location.direction
                    }
                }
            }
        }
    }
    @EventHandler fun onPotionSplash(e: LingeringPotionSplashEvent) {
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
                p.playEffect(EntityEffect.TOTEM_RESURRECT)
                Bukkit.broadcastMessage(text("%aqua%${p.name}님이 치료되었습니다!"))
                p.sendTitle(text("%green%회복!"), text("%green%이제 당신은 %white%생존자%green%입니다"), 0, 80, 20)
            }
            e.areaEffectCloud.remove()
        } else if (e.entity.item == Items.COMPACTED_VACCINE.item) {
            Bukkit.getOnlinePlayers().forEach { p ->
                p.spawnParticle(Particle.END_ROD, e.entity.location, 800, 0.0, 0.0, 0.0, 0.4)
                p.spawnParticle(Particle.TOTEM, e.entity.location, 800, 0.0, 0.0, 0.0, 1.0)
                p.playSound(e.entity.location, Sound.ITEM_TOTEM_USE, 1f, 1f)
            }
            e.entity.getNearbyEntities(6.0, 6.0, 6.0).filterIsInstance<Player>().filter {
                it.name in plugin.zombies
            }.forEach { p ->
                plugin.remove(p.name)
                plugin.survivors.add(p.name)
                p.playEffect(EntityEffect.TOTEM_RESURRECT)
                Bukkit.broadcastMessage(text("%aqua%${p.name}님이 치료되었습니다!"))
                p.sendTitle(text("%green%회복!"), text("%green%이제 당신은 %white%생존자%green%입니다"), 0, 80, 20)
            }
            e.areaEffectCloud.remove()
        }
    }
    @EventHandler fun onEat(e: PlayerItemConsumeEvent) {
        if (e.item.type == Material.COOKIE) {
            if (e.player.name in plugin.survivors) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 100, 2, false, false))
                e.player.playSound(e.player.location, Sound.ENTITY_GENERIC_EAT, 1f, 1f)
                e.player.sendMessage(text("%aqua%쿠키를 먹으니 힘이 솟아오른다!"))
            }
        }
        else if (e.item.isSimilar(Items.GOLDEN_APPLE.item)) e.isCancelled = true
    }
    @EventHandler fun onDeath(e: PlayerDeathEvent) {
        if (e.entity.name in plugin.zombies) {
            if ((Math.random() * 20).toInt() < 1) {
                e.entity.world.dropItemNaturally(e.entity.location, Items.ZOMBIE_HEAD.item)
            }
            if (e.entity.name in plugin.explosionZombies) {
                val l = e.entity.location
                plugin.delay {
                    l.world.createExplosion(l, 10f)
                }
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
    @EventHandler fun onDamage(e: EntityDamageEvent) {
        if (damageCancel) {
            e.isCancelled = true
            return
        }
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
    @EventHandler fun onChat(e: AsyncPlayerChatEvent) {
        if (debug) return
        e.isCancelled = true
        if (e.player.name in plugin.superZombies) {
            plugin.zombies.forEach {
                Bukkit.getPlayer(it)?.sendMessage(text("%yellow%[ %red%Super Zombie %yellow%] %red%${e.player.name} %white%: ${e.message}"))
            }
        }
    }
    @EventHandler fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isOp) e.isCancelled = true
    }
    @EventHandler fun onInteract(e: PlayerInteractEvent) {
        if (e.action in listOf(
                Action.RIGHT_CLICK_BLOCK,
                Action.RIGHT_CLICK_AIR
        )) {
            if (e.player.name in plugin.superZombies) {
                if (e.player.itemInHand.type == Material.COMPASS) {
                    Bukkit.getPlayer(plugin.survivors.random())?.let {
                        target[e.player] = it
                        it.sendMessage(text("%red%슈퍼 좀비가 당신을 추적하기 시작합니다"))
                        it.playSound(it.location, Sound.AMBIENT_CAVE, 1f, 1f)
                    }
                }
            }
            else if (e.player.name in plugin.survivors) {
                if (e.player.itemInHand.isSimilar(Items.HYPER_VACCINE.item)) {
                    e.player.itemInHand.amount--
                    damageCancel = true
                    Bukkit.getOnlinePlayers().forEach { p ->
                        p.sendTitle(text("%aqua%하이퍼 백신 발사"), text("%aqua%전세계에 백신을 발사합니다"), 20, 60, 20)
                        p.playSound(p.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
                    }
                    e.player.world.strikeLightning(e.player.location)
                    val task = HyperVaccineTask(e.player.location.apply {
                        yaw = 0f
                        pitch = 0f
                        y += 20
                    })
                    task.task = plugin.server.scheduler.runTaskTimer(plugin, task, 100L, 1L)
                }
            }
        }
    }
    @EventHandler fun onCraft(e: CraftItemEvent) {
        if (e.whoClicked.name in plugin.survivors) return
        val item = e.currentItem ?: return
        if ("백신" in item.itemMeta.displayName) {
            e.isCancelled = true
            return
        }
        if ("DIAMOND" in item.type.name || item.type == Material.SHIELD) {
            if (e.whoClicked.name in plugin.intelligentZombies) {
                if (Cooltime.getOf("intelligent_craft", e.whoClicked.uniqueId) <= 0) {
                    Cooltime.setOf("intelligent_craft", e.whoClicked.uniqueId, 20)
                }
                else {
                    if (Cooltime.getOf("intelligent_craft", e.whoClicked.uniqueId) % 1200 / 20 > 9) {
                        (e.whoClicked as Player).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(text("%red%%bold%남은 시간 ${Cooltime.getOf("intelligent_craft", e.whoClicked.uniqueId) / 1200} %white%%bold%: %red%%bold%${Cooltime.getOf("intelligent_craft", e.whoClicked.uniqueId) % 1200 / 20}")))
                    }
                    else {
                        (e.whoClicked as Player).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(text("%red%%bold%남은 시간 ${Cooltime.getOf("intelligent_craft", e.whoClicked.uniqueId) / 1200} %white%%bold%: %red%%bold%0${Cooltime.getOf("intelligent_craft", e.whoClicked.uniqueId) % 1200 / 20}")))
                    }
                    e.isCancelled = true
                }
            }
            else {
                e.isCancelled = true
                e.whoClicked.sendMessage(text("%red%당신은 이 아이템을 조합할 수 없습니다!"))
            }
        }
    }
    @EventHandler fun onPortal(e: PlayerPortalEvent) {
        if (e.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) e.isCancelled = true
    }
    @EventHandler fun onAnvil(e: PrepareAnvilEvent) {
        if (e.inventory.getItem(1)?.type == Material.MAGMA_CREAM &&
            e.inventory.getItem(1)?.itemMeta?.displayName == Items.COMPACTED_MAGMA_CREAM.item.itemMeta.displayName) {
            when (e.inventory.getItem(0)?.type) {
                in listOf(
                    Material.LEATHER_BOOTS,
                    Material.LEATHER_LEGGINGS,
                    Material.LEATHER_CHESTPLATE,
                    Material.LEATHER_HELMET,
                    Material.IRON_BOOTS,
                    Material.IRON_LEGGINGS,
                    Material.IRON_CHESTPLATE,
                    Material.IRON_HELMET,
                    Material.DIAMOND_BOOTS,
                    Material.DIAMOND_LEGGINGS,
                    Material.DIAMOND_CHESTPLATE,
                    Material.DIAMOND_HELMET,
                    Material.GOLDEN_BOOTS,
                    Material.GOLDEN_LEGGINGS,
                    Material.GOLDEN_CHESTPLATE,
                    Material.GOLDEN_HELMET,
                    Material.NETHERITE_BOOTS,
                    Material.NETHERITE_LEGGINGS,
                    Material.NETHERITE_CHESTPLATE,
                    Material.NETHERITE_HELMET,
                    Material.CHAINMAIL_BOOTS,
                    Material.CHAINMAIL_LEGGINGS,
                    Material.CHAINMAIL_CHESTPLATE,
                    Material.CHAINMAIL_HELMET
                ) -> { // armor
                    e.result = e.inventory.getItem(0)?.clone()?.apply {
                        itemMeta = itemMeta.apply {
                            addEnchant(Enchantment.PROTECTION_FIRE, 4, false)
                        }
                    }
                }
                in listOf(
                    Material.WOODEN_SWORD,
                    Material.STONE_SWORD,
                    Material.IRON_SWORD,
                    Material.GOLDEN_SWORD,
                    Material.DIAMOND_SWORD,
                    Material.NETHERITE_SWORD
                ) -> { // sword
                    e.result = e.inventory.getItem(0)?.clone()?.apply {
                        itemMeta = itemMeta.apply {
                            addEnchant(Enchantment.FIRE_ASPECT, 1, false)
                        }
                    }
                }
                Material.BOW -> { // bow
                    e.result = e.inventory.getItem(0)?.clone()?.apply {
                        itemMeta = itemMeta.apply {
                            addEnchant(Enchantment.ARROW_FIRE, 1, false)
                        }
                    }
                }
            }
        }
    }
    @EventHandler fun onAnvil(e: InventoryClickEvent) {
        if (e.whoClicked.inventory.firstEmpty() != -1) {
            if (e.inventory is AnvilInventory) {
                if (e.slotType == InventoryType.SlotType.RESULT) {
                    e.whoClicked.inventory.addItem(e.inventory.getItem(2))
                    e.inventory.setItem(2, null)
                    e.inventory.setItem(0, null)
                    e.inventory.getItem(1)?.apply {
                        amount--
                    }
                }
            }
        }
    }

}

class HyperVaccineTask(private val loc: Location) : Runnable {

    lateinit var task: BukkitTask

    private var tick = 0
    private val animationTime = 100 + Bukkit.getOnlinePlayers().count { it.name in plugin.zombies } * 4
    private lateinit var stand: Entity
    private lateinit var blocks: List<FallingBlock>
    private lateinit var fireworkCenter: Location
    private val fireworkLocations = arrayListOf<Location>()
    private val colors = listOf(
        FireworkEffect.builder()
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(15597823))
            .withColor(Color.fromRGB(16711833))
            .withColor(Color.fromRGB(7799039))
            .withColor(Color.fromRGB(16752347))
            .withFade(Color.fromRGB(16765430))
            .build(),
        FireworkEffect.builder()
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(16772608))
            .withColor(Color.fromRGB(16753152))
            .withColor(Color.fromRGB(16766208))
            .withColor(Color.fromRGB(15793956))
            .withFade(Color.fromRGB(16773544))
            .build(),
        FireworkEffect.builder()
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(58879))
            .withColor(Color.fromRGB(43775))
            .withColor(Color.fromRGB(65475))
            .withColor(Color.fromRGB(2211071))
            .withFade(Color.fromRGB(10942975))
            .build(),
        FireworkEffect.builder()
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(65408))
            .withColor(Color.fromRGB(65306))
            .withColor(Color.fromRGB(9633539))
            .withColor(Color.fromRGB(295424))
            .withFade(Color.fromRGB(14811079))
            .build(),
        FireworkEffect.builder()
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(16711680))
            .withColor(Color.fromRGB(16729088))
            .withColor(Color.fromRGB(16728198))
            .withColor(Color.fromRGB(16747702))
            .withFade(Color.fromRGB(16758968))
            .build(),
        FireworkEffect.builder()
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(16711680))
            .withColor(Color.fromRGB(16776960))
            .withColor(Color.fromRGB(65280))
            .withColor(Color.fromRGB(48127))
            .withFade(Color.fromRGB(16777215))
            .build(),
    )

    @Suppress("DEPRECATION")
    override fun run() {
        if (tick < animationTime) {
            if (tick == 0) {
                stand = loc.world.spawnEntity(loc.apply {
                    z -= 5
                }, EntityType.ARMOR_STAND).apply {
                    val armorStand = this as ArmorStand
                    armorStand.isVisible = false
                    armorStand.setGravity(false)
                }
                fun blockR(loc: Location) : FallingBlock {
                    return loc.world.spawnFallingBlock(loc, Material.RED_CONCRETE, 0).apply { setGravity(false) }
                }
                fun blockW(loc: Location) : FallingBlock {
                    return loc.world.spawnFallingBlock(loc, Material.WHITE_CONCRETE, 0).apply { setGravity(false) }
                }
                blocks = listOf(
                    blockR(loc.clone().apply { y += 0.5; z += 10 }),

                    blockW(loc.clone().apply { y += 1; z += 9 }),
                    blockW(loc.clone().apply { x += 1; y += 1; z += 10 }),
                    blockR(loc.clone().apply { x -= 1; y += 1; z += 10 }),

                    blockW(loc.clone().apply { y += 2; z += 9 }),
                    blockR(loc.clone().apply { x += 1; y += 2; z += 10 }),
                    blockW(loc.clone().apply { x -= 1; y += 2; z += 10 }),

                    blockR(loc.clone().apply { y += 3; z += 9 }),
                    blockR(loc.clone().apply { x += 1; y += 3; z += 10 }),
                    blockW(loc.clone().apply { x -= 1; y += 3; z += 10 }),

                    blockR(loc.clone().apply { y += 4; z += 9 }),
                    blockW(loc.clone().apply { x += 1; y += 4; z += 10 }),
                    blockR(loc.clone().apply { x -= 1; y += 4; z += 10 }),

                    blockW(loc.clone().apply { y += 5; z += 9 }),
                    blockW(loc.clone().apply { x += 1; y += 5; z += 10 }),
                    blockR(loc.clone().apply { x -= 1; y += 5; z += 10 }),

                    blockW(loc.clone().apply { y += 6; z += 9 }),
                    blockR(loc.clone().apply { x += 1; y += 6; z += 10 }),
                    blockW(loc.clone().apply { x -= 1; y += 6; z += 10 }),

                    blockR(loc.clone().apply { y += 7; z += 9 }),
                    blockR(loc.clone().apply { x += 1; y += 7; z += 10 }),
                    blockW(loc.clone().apply { x -= 1; y += 7; z += 10 }),

                    blockR(loc.clone().apply { y += 8; z += 9 }),
                    blockW(loc.clone().apply { x += 1; y += 8; z += 10 }),
                    blockR(loc.clone().apply { x -= 1; y += 8; z += 10 }),

                    blockR(loc.clone().apply { y += 9; z += 8 }),
                    blockR(loc.clone().apply { x -= 1; y += 9; z += 9 }),
                    blockR(loc.clone().apply { x -= 2; y += 9; z += 10 }),
                    blockR(loc.clone().apply { x += 1; y += 9; z += 9 }),
                    blockR(loc.clone().apply { x += 2; y += 9; z += 10 }),

                    blockR(loc.clone().apply { y += 10; z += 8 }),
                    blockR(loc.clone().apply { x -= 1; y += 10; z += 9 }),
                    blockR(loc.clone().apply { x -= 2; y += 10; z += 10 }),
                    blockR(loc.clone().apply { x += 1; y += 10; z += 9 }),
                    blockR(loc.clone().apply { x += 2; y += 10; z += 10 }),

                    blockR(loc.clone().apply { y += 11; z += 9 }),
                    blockR(loc.clone().apply { x += 1; y += 11; z += 10 }),
                    blockR(loc.clone().apply { x -= 1; y += 11; z += 10 }),

                    blockR(loc.clone().apply { y += 12; z += 9 }),
                    blockR(loc.clone().apply { x += 1; y += 12; z += 10 }),
                    blockR(loc.clone().apply { x -= 1; y += 12; z += 10 }),

                    blockR(loc.clone().apply { y += 13; z += 10 }),

                    blockR(loc.clone().apply { y += 14; z += 10 }),
                )
                Bukkit.getOnlinePlayers().forEach {
                    it.playSound(it.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                    it.spawnParticle(Particle.EXPLOSION_LARGE, blocks[0].location, 100, 5.0, 0.0, 5.0, 0.0)
                    it.spawnParticle(Particle.EXPLOSION_LARGE, blocks[0].location, 1000, 0.0, 0.0, 0.0, 2.0)
                }
            }
            else if (tick in 1..40) {
                stand.teleport(stand.location.apply {
                    pitch -= 2f
                })
                blocks.forEach {
                    for (x in -1..1) {
                        for (y in -1..1) {
                            for (z in -1..1) {
                                it.location.clone().apply {
                                    this.x += x
                                    this.y += y + 1
                                    this.z += z
                                }.block.type = Material.AIR
                            }
                        }
                    }
                    it.velocity = Vector(0.0, 0.5, 0.0)
                }
                Bukkit.getOnlinePlayers().forEach {
                    val l = blocks[0].location
                    it.spawnParticle(Particle.FLAME, l, 200, 0.5, 0.5, 0.5, 0.0)
                    it.spawnParticle(Particle.CLOUD, l, 50, 0.4, 0.4, 0.4, 0.0)
                }
            }
            else if (tick == 41) {
                fireworkCenter = blocks[blocks.size - 1].location.clone()
                Bukkit.getOnlinePlayers().forEach {
                    it.spawnParticle(Particle.FIREWORKS_SPARK, fireworkCenter, 1000, 0.0, 0.0, 0.0, 1.0)
                    it.spawnParticle(Particle.EXPLOSION_LARGE, fireworkCenter, 100, 0.0, 0.0, 0.0, 10.0)
                    it.playSound(it.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                }
                blocks.forEach {
                    it.remove()
                }
                for (x in -5..5) {
                    for (y in -5..5) {
                        for (z in -5..5) {
                            if (sqrt((x * x + y * y + z * z).toDouble()) in 4.8..5.2) {
                                fireworkLocations.add(fireworkCenter.clone().apply {
                                    this.x += x * 5
                                    this.y += y * 5
                                    this.z += z * 5
                                })
                            }
                        }
                    }
                }
                println(fireworkLocations.size)
                fireworkLocations.forEach {
                    Bukkit.getOnlinePlayers().forEach { p ->
                        p.spawnParticle(Particle.FLASH, it, 1, 0.0, 0.0, 0.0, 0.0)
                    }
                }
            }
            else if (tick in 62..95) {
                try {
                    repeat(5) {
                        val l = fireworkLocations.random()
                        fireworkLocations.remove(l)
                        loc.world.spawn(l, Firework::class.java).apply {
                            fireworkMeta = fireworkMeta.apply {
                                addEffect(colors.random())
                            }
                        }.detonate()
                    }
                } catch (e: Exception) {
                }
            }
            else if (tick >= 100 && tick % 4 == 0) {
                try {
                    Bukkit.getOnlinePlayers().forEach { p ->
                        p.spawnParticle(Particle.END_ROD, loc, 100, 0.0, 0.0, 0.0, 0.2)
                        p.spawnParticle(Particle.TOTEM, loc, 100, 0.0, 0.0, 0.0, 0.5)
                        p.playSound(loc, Sound.ITEM_TOTEM_USE, 1f, 1f)
                    }
                    with(Bukkit.getOnlinePlayers().filter {
                        it.name in plugin.zombies
                    }.random()) {
                        plugin.remove(this.name)
                        plugin.survivors.add(this.name)
                        playEffect(EntityEffect.TOTEM_RESURRECT)
                        Bukkit.broadcastMessage(text("%aqua%${this.name}님이 치료되었습니다!"))
                        sendTitle(text("%green%회복!"), text("%green%이제 당신은 %white%생존자%green%입니다"), 0, 80, 20)
                    }
                } catch (e: Exception) {
                }
            }
            Bukkit.getOnlinePlayers().forEach {
                it.gameMode = GameMode.SPECTATOR
                it.teleport(stand)
                it.spectatorTarget = stand
            }
            tick++
        }
        else {
            stand.remove()
            Bukkit.getOnlinePlayers().forEach {
                it.gameMode = GameMode.SURVIVAL
            }
            task.cancel()
        }
    }

}