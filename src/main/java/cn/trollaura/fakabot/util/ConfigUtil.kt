package cn.trollaura.fakabot.util


import snw.jkook.plugin.Plugin

object ConfigUtil {
    lateinit var plugin: Plugin


    fun init(plugin: Plugin) {
        this.plugin = plugin

    }
    inline fun <reified T: Any>  get(path: String): T {
        return plugin.config.get(path) as T
    }
    fun set(path: String, value: Any) {
        plugin.config.set(path, value)
        plugin.saveConfig()
        plugin.reloadConfig()
        init(plugin)
    }
}