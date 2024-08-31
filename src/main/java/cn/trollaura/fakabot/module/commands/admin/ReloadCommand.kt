package cn.trollaura.fakabot.module.commands.admin

import cn.trollaura.fakabot.Main
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
class ReloadCommand(val plugin: Plugin): Module("reload") {
 init {
  justAdmin = true
     description = "重载插件"
 }
 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  if (p1 == null || p0 == null || p2 == null) return

  try {
   if (ConfigUtil.plugin.config.getStringList("Admin").contains(p0.id)) {
    plugin.reloadConfig()
    Main.categories = plugin.config.getStringList("Categories")
    p2.reply(
     CardBuilder().setSize(Size.LG).setTheme(Theme.SUCCESS)
      .addModule(SectionModule(MarkdownElement("重载成功!"))).build()
    )
   } else {
    p2.reply(
     CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
      .addModule(SectionModule(MarkdownElement("您没有权限执行此命令!"))).build()
    )
   }
  }catch (e: Exception) {
   p2.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
    .addModule(SectionModule(MarkdownElement("重载时出现问题,请尝试检查下面的日志!\n```java\n${e.message}\n${e.cause}\n```"))).build())
  }
 }

}