package cn.trollaura.fakabot.module.commands.user

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.managers.MessageManager
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.DataUtil
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
class MeCommand(val plugin: Plugin): Module("me") {
 init {
     description = "查看自己的钱包"
 }

 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  if(p0 == null || p2 == null || p1 == null) return

  val jsonArray = getJSONArray()
  val json = DataUtil.getJSONObjectWithID(jsonArray,p0.id)
  if(json == null) {
   p2.reply(CardBuilder()
    .setSize(Size.LG)
    .setTheme(Theme.PRIMARY)
    .addModule(SectionModule(MarkdownElement("您目前的${MessageManager.moneyname}: 0.0")))
    .build())
   return
  }
  p2.reply(CardBuilder()
   .setSize(Size.LG)
   .setTheme(Theme.PRIMARY)
   .addModule(SectionModule(MarkdownElement("您目前的${MessageManager.moneyname}: ${json!!.getDouble("Amount")}")))
   .build())
 }
}