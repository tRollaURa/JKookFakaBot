package cn.trollaura.fakabot.module.commands.user

import cn.trollaura.fakabot.Loader
import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import snw.jkook.entity.User
import snw.jkook.message.Message
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/8/18
 */
@Command
class HelpCommand(val plugin: Plugin): Module("help", arrayOf(" <指令>")) {
    init {
        description = "查看指令列表(查看某指令详细信息)"
    }
    override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
        if(p1 == null || p0 == null || p2 == null) return

        when(p1.size) {
            0 -> {
                val commands = Loader(plugin).modules.filter { it.isCommand() && !it.justAdmin }
                val CB = CardBuilder().setSize(Size.LG).setTheme(Theme.PRIMARY)
                    .addModule(SectionModule(MarkdownElement("**可用指令: **")))
                    .addModule(SectionModule(MarkdownElement("**注意: `<>为必填` `()为选填`**")))
                commands.forEach {
                    CB
                        .addModule(SectionModule(MarkdownElement("`${ConfigUtil.get<String>("CommandPrefix")}${it.name}${it.usage[0]}` - **[${it.description}]**")))
                }
                CB.build()
                p2.reply(CB.build())
            }
            1 -> {
                if(p1[0].toString() == "admin") {
                    val commands = Loader(plugin).modules.filter { it.isCommand() && it.justAdmin }
                    val CB = CardBuilder().setSize(Size.LG).setTheme(Theme.SUCCESS)
                        .addModule(SectionModule(MarkdownElement("**管理员指令: **")))
                        .addModule(SectionModule(MarkdownElement("**注意: `<>为必填` `()为选填`**")))
                    commands.forEach {
                        CB
                            .addModule(SectionModule(MarkdownElement("`${ConfigUtil.get<String>("CommandPrefix")}${it.name}${it.usage[0]}` - **[${it.description}]**")))
                    }
                    CB.build()
                    p2.reply(CB.build())
                    return
                }
                val command = Loader(plugin).modules.filter { it.isCommand() }.find { it.name == p1[0].toString() }
                if(command == null) {
                    p2.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
                        .addModule(SectionModule(MarkdownElement("指令不存在!"))).build())
                    return
                }

                    val CB = CardBuilder().setSize(Size.LG).setTheme(Theme.SUCCESS)
                        .addModule(SectionModule(MarkdownElement("**${command.name} 用法 **")))
                        .addModule(SectionModule(MarkdownElement("**注意: `<>为必填` `()为选填`**")))
                command.usage.forEach {
                    CB.addModule(SectionModule(MarkdownElement("`${command.name}${it}`")))
                }
                p2.reply(CB.build())
            }
            else -> {
                return
            }
            }
        }
}