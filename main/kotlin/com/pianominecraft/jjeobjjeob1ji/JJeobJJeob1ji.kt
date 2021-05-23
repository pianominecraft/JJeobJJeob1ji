package com.pianominecraft.jjeobjjeob1ji

import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

lateinit var plugin: JJeobJJeob1ji

class JJeobJJeob1ji : JavaPlugin() {

    var zombies = ArrayList<String>()
    var normalZombies = ArrayList<String>()
    var superZombies = ArrayList<String>()
    var giantZombies = ArrayList<String>()
    var speedZombies = ArrayList<String>()
    var intelligentZombies = ArrayList<String>()
    var explosionZombies = ArrayList<String>()
    var survivors = ArrayList<String>()
    var whitelist = listOf("")
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
        }
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
        }

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
                        var slow = 0.0
                        if (p.inventory.helmet != null) {
                            health += 5.0
                            slow += 0.5
                        }
                        if (p.inventory.chestplate != null) {
                            health += 5.0
                            slow += 0.5
                        }
                        if (p.inventory.leggings != null) {
                            health += 5.0
                            slow += 0.5
                        }
                        if (p.inventory.boots != null) {
                            health += 5.0
                            slow += 0.5
                        }
                        p.maxHealth = health
                        p.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 2, slow.toInt(), false, false))
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
                        p.maxHealth = 14.0
                        p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 2, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 205, 0, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 2, 127, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.POISON, 2, 0, false, false))
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
                        if (!intelligentCool.containsKey(p.uniqueId)) intelligentCool[p.uniqueId] = 0
                        if (intelligentCool[p.uniqueId]!! > 0) intelligentCool[p.uniqueId] = intelligentCool[p.uniqueId]!! - 1
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

        Bukkit.getPluginManager().registerEvents(EventManager(), this)
        setupRecipe()
        Bukkit.getLogger().info("[쩝쩝일지] has been enabled!")
    }
    private fun setupRecipe() {
        with (server) {
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
        }
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
                        zombies.add(a[1])
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
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false)
            world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false)
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false)
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
            world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false)
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
            world.worldBorder.setCenter(0.0, 0.0)
            world.worldBorder.size = 2000.0
            nether.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            nether.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false)
            nether.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false)
            nether.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false)
            nether.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
            nether.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false)
            nether.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
            nether.worldBorder.setCenter(0.0, 0.0)
            nether.worldBorder.size = 2000.0
        }

        return false
    }

    private fun repeat(delay: Long = 1, task: () -> Unit) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, task, 0, delay)
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
var time = -1
var bossbar: BossBar = Bukkit.createBossBar(text("%dark_red%%bold%좀비 해방 %gray%%bold%[ 30 : 00 ]"), BarColor.RED, BarStyle.SOLID)
val target = HashMap<Player, Player>()
val intelligentCool = HashMap<UUID, Int>()