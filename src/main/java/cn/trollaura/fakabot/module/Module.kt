package cn.trollaura.fakabot.module

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.util.DataUtil
import org.json.JSONArray
import snw.jkook.command.UserCommandExecutor
import snw.jkook.entity.Guild
import snw.jkook.event.Listener
import snw.jkook.message.Message
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/7/23
 */
 abstract class Module(val name: String) : Listener,UserCommandExecutor {
     var justAdmin = false
     var description: String = "无建议"
    var usage: Array<String> = arrayOf("")

    fun getJSONArray(): JSONArray {
        return JSONArray(DataUtil.file.readText())
    }
    fun getItems(): JSONArray {
        return JSONArray(DataUtil.file2.readText())
    }


    constructor(name: String,usage: Array<String>) : this(name) {
        this.usage = usage
    }

    fun isCommand(): Boolean {
        return this::class.java.annotations.contains(Command())
    }
    fun isListener(): Boolean {
        return this::class.java.annotations.contains(cn.trollaura.fakabot.annotations.Listener())
    }



    fun getGuild(plugin: Plugin,message: Message): Guild {
        return plugin.core.httpAPI.getGuild(getGuildID(plugin,message))
    }

    fun getJsonArray(): JSONArray {
        return JSONArray(DataUtil.file.readText())
    }

    fun getGuildID(plugin: Plugin,message: Message): String {
        return plugin.core.httpAPI.getTextChannelMessage(message.id).channel.guild.id
    }




 }
