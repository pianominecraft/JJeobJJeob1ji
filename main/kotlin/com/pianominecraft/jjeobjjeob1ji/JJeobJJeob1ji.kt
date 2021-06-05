package com.pianominecraft.jjeobjjeob1ji

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.*
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

lateinit var plugin: JJeobJJeob1ji

@Suppress("DEPRECATION")
class JJeobJJeob1ji : JavaPlugin() {

    var zombies = ArrayList<String>()
    var normalZombies = ArrayList<String>()
    var superZombies = ArrayList<String>()
    var giantZombies = ArrayList<String>()
    var speedZombies = ArrayList<String>()
    var intelligentZombies = ArrayList<String>()
    var explosionZombies = ArrayList<String>()
    var survivors = ArrayList<String>()
    private var whitelist = listOf("")
    var superSurvivors = listOf("")

    fun remove(name: String) {
        normalZombies.remove(name)
        superZombies.remove(name)
        giantZombies.remove(name)
        speedZombies.remove(name)
        intelligentZombies.remove(name)
        explosionZombies.remove(name)
        zombies.remove(name)
        survivors.remove(name)
    }

    private val normalFile = File(dataFolder, "normal.txt")
    private val superFile = File(dataFolder, "super.txt")
    private val giantFile = File(dataFolder, "giant.txt")
    private val speedFile = File(dataFolder, "speed.txt")
    private val intelligentFile = File(dataFolder, "intelligent.txt")
    private val explosionFile = File(dataFolder, "explosion.txt")
    private val survivorFile = File(dataFolder, "survivor.txt")
    private val whitelistFile = File(dataFolder, "whitelist.txt")
    private val superSurvivorFile = File(dataFolder, "superSurvivor.txt")

    @Suppress("DEPRECATION")
    override fun onEnable() {
        plugin = this

        repeat {
            Cooltime.keys().forEach { k ->
                Cooltime.getMap(k)!!.forEach { (t, u) ->
                    if (u > 0) {
                        Cooltime.subtract(k, t, 1)
                    }
                }
            }
        } //cooltime
        repeat (200) {
            Bukkit.getOnlinePlayers().forEach { p ->
                if (p.world.name == "world") {
                    if ((Math.random() * 20).toInt() == 0) {
                        val x = (Math.random() * 100).toInt() - 50.0
                        var y = 256
                        val z = (Math.random() * 100).toInt() - 50.0
                        while (Location(p.world, x, (y - 1).toDouble(), z).block.type == Material.AIR) {y--}
                        println(y)
                        p.world.spawnEntity(Location(p.world, x, y.toDouble(), z), EntityType.CREEPER)
                    }
                }
            }
        }

        setupCooltime()
        setupPlayers()
        Bukkit.getPluginManager().registerEvents(EventManager(), this)
        Bukkit.getPluginManager().registerEvents(SkillHandler(), this)
        setupRecipe()
        setupSkill()
        Bukkit.getLogger().info("[쩝쩝일지] has been enabled!")
    }
    private fun setupCooltime() {
        Cooltime.addKey("super_jump")
        Cooltime.addKey("summon")
        Cooltime.addKey("intelligent_craft")
    }
    private fun setupPlayers() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        if (!normalFile.exists()) normalFile.createNewFile()
        if (!superFile.exists()) superFile.createNewFile()
        if (!giantFile.exists()) giantFile.createNewFile()
        if (!speedFile.exists()) speedFile.createNewFile()
        if (!intelligentFile.exists()) intelligentFile.createNewFile()
        if (!explosionFile.exists()) explosionFile.createNewFile()
        if (!survivorFile.exists()) survivorFile.createNewFile()
        if (!whitelistFile.exists()) whitelistFile.createNewFile()
        if (!superSurvivorFile.exists()) superSurvivorFile.createNewFile()
        normalZombies.addAll(BufferedReader(FileReader(normalFile)).use {
            it.readLines()
        })
        superZombies.addAll(BufferedReader(FileReader(superFile)).use {
            it.readLines()
        })
        giantZombies.addAll(BufferedReader(FileReader(giantFile)).use {
            it.readLines()
        })
        speedZombies.addAll(BufferedReader(FileReader(speedFile)).use {
            it.readLines()
        })
        intelligentZombies.addAll(BufferedReader(FileReader(intelligentFile)).use {
            it.readLines()
        })
        explosionZombies.addAll(BufferedReader(FileReader(explosionFile)).use {
            it.readLines()
        })
        survivors.addAll(BufferedReader(FileReader(survivorFile)).use {
            it.readLines()
        })
        whitelist = BufferedReader(FileReader(whitelistFile)).use {
            it.readLines()
        }
        superSurvivors = BufferedReader(FileReader(superSurvivorFile)).use {
            it.readLines()
        }
        zombies.addAll(normalZombies)
        zombies.addAll(superZombies)
        zombies.addAll(giantZombies)
        zombies.addAll(intelligentZombies)
        zombies.addAll(speedZombies)
        zombies.addAll(explosionZombies)

        Bukkit.getOnlinePlayers().forEach {
            if (it.name !in survivors && it.name !in zombies) {
                plugin.zombies.add(it.name)
                plugin.normalZombies.add(it.name)
            }
        } // init
        repeat {
            if (time > 0) {
                time--
                bossbar.isVisible = false
                if (time / 1200 > 9) {
                    bossbar = if (time % 1200 / 20 > 9) {
                        Bukkit.createBossBar(
                            text("%dark_red%%bold%좀비 해방 %gray%%bold%[ ${time / 1200} : ${time % 1200 / 20} ]"),
                            BarColor.RED,
                            BarStyle.SOLID
                        )
                    } else {
                        Bukkit.createBossBar(
                            text("%dark_red%%bold%좀비 해방 %gray%%bold%[ ${time / 1200} : 0${time % 1200 / 20} ]"),
                            BarColor.RED,
                            BarStyle.SOLID
                        )
                    }
                } else {
                    bossbar = if (time % 1200 / 20 > 9) {
                        Bukkit.createBossBar(
                            text("%dark_red%%bold%좀비 해방 %gray%%bold%[ 0${time / 1200} : ${time % 1200 / 20} ]"),
                            BarColor.RED,
                            BarStyle.SOLID
                        )
                    } else {
                        Bukkit.createBossBar(
                            text("%dark_red%%bold%좀비 해방 %gray%%bold%[ 0${time / 1200} : 0${time % 1200 / 20} ]"),
                            BarColor.RED,
                            BarStyle.SOLID
                        )
                    }
                }
                bossbar.progress = time / 36000.0
                Bukkit.getOnlinePlayers().forEach {
                    bossbar.addPlayer(it)
                }
                bossbar.isVisible = true
            } else if (time == 0) {
                time--
                bossbar.isVisible = false
                wl = false
                Bukkit.getWorld("world")?.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                Bukkit.getWorld("world_nether")?.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                Bukkit.getOnlinePlayers().forEach {
                    it.sendTitle(text("%dark_red%으악! 좀비다!"), text("%dark_red%좀비가 등장하기 시작합니다!"))
                    it.playSound(it.location, Sound.ENTITY_ZOMBIE_AMBIENT, 1f, 1f)
                }
            }
        } // zombies appear

        repeat {
            normalZombies.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.maxHealth = 20.0
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
                    }
                } catch (e: Exception) {
                }
            }
        } // normal
        repeat {
            superZombies.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.maxHealth = 20.0
                        try {
                            p.inventory.forEach { item ->
                                if (item.type == Material.COMPASS) {
                                    val meta = item.itemMeta as CompassMeta
                                    meta.lodestone = target[p]!!.location
                                    meta.isLodestoneTracked = false
                                    item.itemMeta = meta
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(text("%red%%bold%${target[p]!!.name}을(를) 추적중입니다")))
                                    target[p]!!.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(text("%red%%bold%슈퍼 좀비가 당신을 추적중입니다")))
                                }
                            }
                        } catch (e: Exception) {
                        }
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
                    }
                } catch (e: Exception) {
                }
            }
        } // super
        repeat {
            giantZombies.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        var health = 20.0
                        var slow = false
                        if (p.inventory.helmet != null) {
                            health += 5.0
                            slow = true
                        }
                        if (p.inventory.chestplate != null) {
                            health += 5.0
                            slow = true
                        }
                        if (p.inventory.leggings != null) {
                            health += 5.0
                            slow = true
                        }
                        if (p.inventory.boots != null) {
                            health += 5.0
                            slow = true
                        }
                        p.maxHealth = health
                        if (slow) p.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 2, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
                    }
                } catch (e: Exception) {
                }
            }
        } // giant
        repeat {
            speedZombies.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.maxHealth = 20.0
                        p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 2, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 2, 1, false, false))
                    }
                } catch (e: Exception) {
                }
            }
        } // speed
        repeat {
            intelligentZombies.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.maxHealth = 14.0
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
                    }
                } catch (e: Exception) {
                }
            }
        } // intelligent
        repeat {
            explosionZombies.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.maxHealth = 20.0
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
                    }
                } catch (e: Exception) {
                }
            }
        } // explosion
        repeat {
            survivors.forEach {
                try {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.maxHealth = 20.0
                        p.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 2, 0, false, false))
                        p.setPlayerListName(p.name)
                    }
                    Bukkit.getScoreboardManager().mainScoreboard.getTeam("a")?.addEntry(it)
                        ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("a")
                } catch (e: Exception) {
                }
            }
        } // survivor
    }
    private fun setupRecipe() {
        with (server) {
            addRecipe(
                ShapelessRecipe(
                    NamespacedKey.minecraft("jj_sugar_cane"),
                    ItemStack(Material.SUGAR_CANE)
                ).apply {
                    addIngredient(Material.KELP)
                    addIngredient(Material.KELP)
                    addIngredient(Material.KELP)
                }
            )
            addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("diffuser"),
                    Items.DIFFUSER.item
                ).apply {
                    shape(
                        " H ",
                        " B ",
                        " G "
                    )
                    setIngredient('H', RecipeChoice.ExactChoice(Items.ZOMBIE_HEAD.item))
                    setIngredient('B', Material.GLASS_BOTTLE)
                    setIngredient('G', Material.GUNPOWDER)
                }
            )
            addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("vaccine"),
                    Items.VACCINE.item
                ).apply {
                    shape(
                        " G ",
                        "HBD",
                        "   "
                    )
                    setIngredient('G', Material.GOLDEN_APPLE)
                    setIngredient('B', Material.GLASS_BOTTLE)
                    setIngredient('H', Material.HONEY_BOTTLE)
                    setIngredient('D', RecipeChoice.ExactChoice(Items.DIFFUSER.item))
                }
            )
            addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("honey"),
                    ItemStack(Material.HONEY_BOTTLE)
                ).apply {
                    shape(
                        "SSS",
                        "SBS",
                        "SSS"
                    )
                    setIngredient('S', Material.SUGAR)
                    setIngredient('B', Material.GLASS_BOTTLE)
                }
            )
            addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("hyper_vaccine"),
                    Items.HYPER_VACCINE.item
                ).apply {
                    shape(
                        " V ",
                        "VVV",
                        " V "
                    )
                    setIngredient('V', RecipeChoice.ExactChoice(Items.VACCINE.item))
                }
            )
        } // Vaccine
        with (server) {
            this.addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("jj_blaze_powder"),
                    ItemStack(Material.BLAZE_POWDER)
                ).apply {
                    shape(
                        "QGQ",
                        "QGQ",
                        "QGQ"
                    )
                    setIngredient('G', Material.GOLD_INGOT)
                    setIngredient('Q', Material.QUARTZ)
                }
            )
            this.addRecipe(
                ShapelessRecipe(
                    NamespacedKey.minecraft("jj_blaze_rod"),
                    ItemStack(Material.BLAZE_ROD)
                ).apply {
                    addIngredient(Material.BLAZE_POWDER)
                    addIngredient(Material.BLAZE_POWDER)
                }
            )
            this.addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("compacted_nether_brick"),
                    Items.COMPACTED_NETHER_BRICK.item
                ).apply {
                    shape(
                        "NNN",
                        "NNN",
                        "NNN"
                    )
                    setIngredient('N', Material.NETHER_BRICK)
                }
            )
            this.addRecipe(
                ShapelessRecipe(
                    NamespacedKey.minecraft("compacted_obsidian"),
                    Items.COMPACTED_OBSIDIAN.item
                ).apply {
                    addIngredient(Material.OBSIDIAN)
                    addIngredient(Material.OBSIDIAN)
                    addIngredient(Material.OBSIDIAN)
                    addIngredient(Material.OBSIDIAN)
                }
            )
            this.addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("compacted_blaze_rod"),
                    Items.COMPACTED_BLAZE_ROD.item
                ).apply {
                    shape(
                        " B ",
                        " B ",
                        " B "
                    )
                    setIngredient('B', Material.BLAZE_ROD)
                }
            )
            this.addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("teambattle_netherite"),
                    ItemStack(Material.NETHERITE_INGOT)
                ).apply {
                    shape(
                        "ONO",
                        "NBN",
                        "ONO"
                    )
                    setIngredient('O', RecipeChoice.ExactChoice(Items.COMPACTED_OBSIDIAN.item))
                    setIngredient('N', RecipeChoice.ExactChoice(Items.COMPACTED_NETHER_BRICK.item))
                    setIngredient('B', RecipeChoice.ExactChoice(Items.COMPACTED_BLAZE_ROD.item))
                }
            )
        } // Netherite
        with (server) {
            this.addRecipe(
                ShapedRecipe(
                    NamespacedKey.minecraft("compacted_magma_cream"),
                    Items.COMPACTED_MAGMA_CREAM.item
                ).apply {
                    shape(
                        " M ",
                        "MMM",
                        " M "
                    )
                    setIngredient('M', Material.MAGMA_CREAM)
                }
            )
        } // Magma
    }
    @Suppress("UNCHECKED_CAST")
    private fun setupSkill() {
        skills.add(Skill(
            { e ->
                e.player.sendTitle(text("%red%3"), text("%gray%다리에 힘을 모으는 중..."), 0, 21, 0)
                zombies.forEach {
                    Bukkit.getPlayer(it)?.let { p ->
                        p.playSound(e.player.location, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f)
                        p.playSound(e.player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    }
                }
                delay (20) {
                    e.player.sendTitle(text("%red%2"), text("%gray%다리에 힘을 모으는 중..."), 0, 21, 0)
                    zombies.forEach {
                        Bukkit.getPlayer(it)?.playSound(e.player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    }
                }
                delay (40) {
                    e.player.sendTitle(text("%red%1"), text("%gray%다리에 힘을 모으는 중..."), 0, 21, 0)
                    zombies.forEach {
                        Bukkit.getPlayer(it)?.playSound(e.player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    }
                }
                delay (60) {
                    var x = e.player.location.x
                    if (x < 0) x--
                    var y = 256
                    var z = e.player.location.z
                    if (z < 0) z--
                    while (e.player.world.getBlockAt(x.toInt(), y - 1, z.toInt()).type == Material.AIR) y--
                    e.player.teleport(e.player.location.apply {
                        this.y = y.toDouble()
                    })
                    zombies.forEach {
                        Bukkit.getPlayer(it)?.let { p ->
                            p.spawnParticle(Particle.CLOUD, e.player.location, 100, 0.0, 0.0, 0.0, 0.25)
                            p.playSound(e.player.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                        }
                    }
                }
            },
            { e ->
                e.player.name in superZombies && e.player.world.name == "world"
            },
            "super_jump",
            3600,
            Material.GOLD_INGOT
        ))
        skills.add(Skill(
            { e ->
                val z = Bukkit.getOnlinePlayers().filter {
                    it.name !in superZombies && it.name !in survivors
                }.shuffled().take(5)
                for (i in 0..4) {
                    delay ((i * 20).toLong()) {
                        e.player.sendTitle(text("%red%$i"), text("%gray%좀비들을 불러 모으는 중..."), 0, 21, 0)
                        zombies.forEach {
                            Bukkit.getPlayer(it)?.let { p ->
                                p.playSound(e.player.location, Sound.ENTITY_ZOMBIE_AMBIENT, 1f, 1f)
                                p.playSound(e.player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                            }
                        }
                        z.forEach { p ->
                            p.sendTitle(text("%red%$i"), text("%gray%소환되는 중..."), 0, 21, 0)
                            p.playSound(p.location, Sound.ENTITY_ZOMBIE_AMBIENT, 1f, 1f)
                            p.playSound(p.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                        }
                    }
                }
                delay (100) {
                    e.player.sendTitle(text("%dark_red%좀비 소환"), text("%gray%5마리의 좀비를 불러모았습니다"), 0, 20, 20)
                    for (i in 0..4) {
                        delay ((i * 4).toLong()) {
                            z[i].teleport(e.player)
                            zombies.forEach {
                                Bukkit.getPlayer(it)?.let { p ->
                                    p.playSound(e.player.location, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f)
                                    p.spawnParticle(Particle.TOTEM, e.player.location, 100, 0.0, 0.0, 0.0, 0.5)
                                    p.sendTitle(text("%dark_red%소환"),text("%gray%슈퍼 좀비에게 아이템을 바치세요"), 0, 80, 20)
                                }
                            }
                        }
                    }
                }
            },
            { e ->
                e.player.name in superZombies
            },
            "summon",
            6000,
            Material.DIAMOND
        ))
    }

    override fun onDisable() {
        PrintWriter(normalFile).use { pw ->
            normalZombies.forEach { s ->
                pw.println(s)
            }
        }
        PrintWriter(superFile).use { pw ->
            superZombies.forEach { s ->
                pw.println(s)
            }
        }
        PrintWriter(giantFile).use { pw ->
            giantZombies.forEach { s ->
                pw.println(s)
            }
        }
        PrintWriter(speedFile).use { pw ->
            speedZombies.forEach { s ->
                pw.println(s)
            }
        }
        PrintWriter(intelligentFile).use { pw ->
            intelligentZombies.forEach { s ->
                pw.println(s)
            }
        }
        PrintWriter(explosionFile).use { pw ->
            explosionZombies.forEach { s ->
                pw.println(s)
            }
        }
        PrintWriter(survivorFile).use { pw ->
            survivors.forEach { s ->
                pw.println(s)
            }
        }
        bossbar.isVisible = false
        Bukkit.getLogger().info("[쩝쩝일지] has been disabled!")
    }

    override fun onCommand(s: CommandSender, c: Command, l: String, a: Array<out String>): Boolean {

        if (c.name.equals("change", ignoreCase = true)) {
            if (a.size > 1) {
                when (a[0]) {
                    "normal" -> {
                        remove(a[1])
                        normalZombies.add(a[1])
                        zombies.add(a[1])
                        s.sendMessage(text("%red%${a[1]} %white%의 타입을 %gold%일반 좀비%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("g")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("g")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(text("%red%${a[1]}"))
                        Bukkit.getPlayer(a[1])?.customName = text("%red%${a[1]}")
                    }
                    "super" -> {
                        remove(a[1])
                        superZombies.add(a[1])
                        zombies.add(a[1])
                        s.sendMessage(text("%red%${a[1]} %white%의 타입을 %gold%슈퍼 좀비%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("b")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("b")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(text("%dark_red%${a[1]} %gold%[S]"))
                        Bukkit.getPlayer(a[1])?.customName = text("%dark_red%${a[1]} %gold%[S]")
                    }
                    "giant" -> {
                        remove(a[1])
                        giantZombies.add(a[1])
                        zombies.add(a[1])
                        s.sendMessage(text("%red%${a[1]} %white%의 타입을 %gold%자이언트 좀비%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("d")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("d")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(text("%red%${a[1]} %dark_green%[G]"))
                        Bukkit.getPlayer(a[1])?.customName = text("%red%${a[1]} %dark_green%[G]")
                    }
                    "speed" -> {
                        remove(a[1])
                        speedZombies.add(a[1])
                        zombies.add(a[1])
                        s.sendMessage(text("%red%${a[1]} %white%의 타입을 %gold%스피드 좀비%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("f")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("f")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(text("%red%${a[1]} %aqua%[S]"))
                        Bukkit.getPlayer(a[1])?.customName = text("%red%${a[1]} %aqua%[S]")
                    }
                    "intelligent" -> {
                        remove(a[1])
                        intelligentZombies.add(a[1])
                        zombies.add(a[1])
                        s.sendMessage(text("%red%${a[1]} %white%의 타입을 %gold%지능 좀비%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("c")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("c")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(text("%red%${a[1]} %dark_aqua%[I]"))
                        Bukkit.getPlayer(a[1])?.customName = text("%red%${a[1]} %dark_aqua%[I]")
                    }
                    "explosion" -> {
                        remove(a[1])
                        explosionZombies.add(a[1])
                        zombies.add(a[1])
                        s.sendMessage(text("%red%${a[1]} %white%의 타입을 %gold%폭팔 좀비%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("e")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("e")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(text("%red%${a[1]} %yellow%[E]"))
                        Bukkit.getPlayer(a[1])?.customName = text("%red%${a[1]} %yellow%[E]")
                    }
                    "survivor" -> {
                        remove(a[1])
                        survivors.add(a[1])
                        s.sendMessage(text("${a[1]} %white%의 타입을 %gold%생존자%white%로 바꿨습니다"))
                        Bukkit.getScoreboardManager().mainScoreboard.getTeam("a")?.addEntry(a[1])
                            ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("a")
                        Bukkit.getPlayer(a[1])?.setPlayerListName(a[1])
                        Bukkit.getPlayer(a[1])?.customName = a[1]
                    }
                }
            }
        }
        else if (c.name.equals("item", ignoreCase = true)) {
            try {
                (s as Player).inventory.addItem(Items.valueOf(a[0]).item)
            } catch (e: Exception) {
            }
        }
        else if (c.name.equals("start", ignoreCase = true)) {
            time = 36000
            Bukkit.getOnlinePlayers().forEach {
                it.sendTitle(text("%red%파밍 시작!"), text("%red%좀비로부터 살아남기 위해 파밍하세요"), 20, 60, 20)
                it.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 36000, 127, false, false))
            }
            val world = Bukkit.getWorld("world")!!
            val nether = Bukkit.getWorld("world_nether")!!
            listOf(world, nether).forEach { w ->
                w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false)
                w.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false)
                w.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false)
                w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
                w.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false)
                w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
                w.setGameRule(GameRule.REDUCED_DEBUG_INFO, true)
                w.worldBorder.setCenter(0.0, 0.0)
                w.worldBorder.size = 2000.0
            }
        }
        else if (c.name.equals("debugWithChat", ignoreCase = true)) {
            if (s.isOp) {
                debug = !debug
                s.sendMessage(text("%green%Now, debug with chat mode is $debug"))
            }
        }
        else if (c.name.equals("debugVariables", ignoreCase = true)) {
            if (s.isOp) {
                s.sendMessage("survivors : $survivors")
                s.sendMessage("super : $superZombies")
                s.sendMessage("intelligent : $intelligentZombies")
                s.sendMessage("giant : $giantZombies")
                s.sendMessage("explosion : $explosionZombies")
                s.sendMessage("speed : $speedZombies")
                s.sendMessage("normal : $normalZombies")
                s.sendMessage("zombies : $zombies")
            }
        }
        else if (c.name.equals("hyperVaccine", ignoreCase = true)) {
            if (s.isOp && s is Player) {
                damageCancel = true
                Bukkit.getOnlinePlayers().forEach { p ->
                    p.sendTitle(text("%aqua%하이퍼 백신 발사"), text("%aqua%전세계에 백신을 발사합니다"), 20, 60, 20)
                    p.playSound(p.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
                }
                s.world.strikeLightning(s.location)
                val task = HyperVaccineTask(s.location.apply {
                    yaw = 0f
                    pitch = 0f
                    y += 20
                })
                task.task = plugin.server.scheduler.runTaskTimer(plugin, task, 100L, 1L)
            }
        }

        return false
    }

    private fun repeat(delay: Long = 1, task: () -> Unit) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, task, 0, delay)
    }
    fun delay(delay: Long = 1, task: () -> Unit) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, task, delay)
    }

}

fun text(string: String) : String {
    var s = string
    val rgb = Pattern.compile("#[0-9a-f]{6}").matcher(string)
    while (rgb.find()) {
        try {
            s = s.replaceFirst(rgb.group(), net.md_5.bungee.api.ChatColor.of(rgb.group()).toString())
        } catch (e: Exception) {
        }
    }
    val color = Pattern.compile("%[a-zA-Z_]*%").matcher(string)
    while (color.find()) {
        try {
            s = s.replaceFirst(
                color.group(),
                net.md_5.bungee.api.ChatColor.of(color.group().replace("%", "")).toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return s
}
var wl = true
var time = 0
var bossbar: BossBar = Bukkit.createBossBar(text("%dark_red%%bold%좀비 해방 %gray%%bold%[ 30 : 00 ]"), BarColor.RED, BarStyle.SOLID)
val target = HashMap<Player, Player>()
var damageCancel = false
var debug = false