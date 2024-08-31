package cn.trollaura.fakabot.module.listeners

import cn.trollaura.fakabot.Loader
import cn.trollaura.fakabot.annotations.Listener
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import snw.jkook.entity.User
import snw.jkook.event.EventHandler
import snw.jkook.event.channel.ChannelMessageEvent
import snw.jkook.event.pm.PrivateMessageEvent
import snw.jkook.event.pm.PrivateMessageReceivedEvent
import snw.jkook.message.Message
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/8/18
 */
@Listener
class MessageReader(val plugin: Plugin): Module("MessageReader") {
    @EventHandler
    fun onRead(evt: ChannelMessageEvent) {
        if(evt.message.component.toString() == "/plugins") {
            evt.message.reply("已安装并正在运行的插件 (2): Afterglow.CFG Bot ,Afterglow NFA Bot")
        }
        if(evt.message.component.toString().startsWith(ConfigUtil.get<String>("CommandPrefix"))) {
            Loader(plugin).modules.filter { it.isCommand() }
                .forEach {
                    if(evt.message.component.toString().removePrefix(ConfigUtil.get<String>("CommandPrefix")) == it.name) {
                        return
                    }
                }
            evt.message.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.PRIMARY).addModule(SectionModule("此指令并不存在!")).build())

        }
    }

    @EventHandler
    fun onReadPri(evt: PrivateMessageReceivedEvent) {
        if(evt.message.component.toString() == "/plugins") {
            evt.message.reply("已安装并正在运行的插件 (-1): AfterglowCFG.Bot ")
        }
        if(evt.message.component.toString().startsWith(ConfigUtil.get<String>("CommandPrefix"))) {
            Loader(plugin).modules.filter { it.isCommand() }
                .forEach {
                    if(evt.message.component.toString().removePrefix(ConfigUtil.get<String>("CommandPrefix")) == it.name) {
                        return
                    }
                }
            evt.message.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.PRIMARY).addModule(SectionModule("此指令并不存在!")).build())

        }
    }

    override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
        TODO("Not yet implemented")
    }
}