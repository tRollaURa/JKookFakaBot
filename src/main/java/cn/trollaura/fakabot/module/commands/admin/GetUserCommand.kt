package cn.trollaura.fakabot.module.commands.admin

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.managers.MessageManager
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import cn.trollaura.fakabot.util.DataUtil
import snw.jkook.entity.User
import snw.jkook.message.Message
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.element.PlainTextElement
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/8/19
 */
@Command
class GetUserCommand(val plugin: Plugin): Module("getuser", arrayOf(" <@某人>")) {
 init {
  justAdmin = true
  description = "康康某人"
 }
 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  if(p1 == null || p0 == null || p2 == null) return

  if (!ConfigUtil.plugin.config.getStringList("Admin").contains(p0!!.id)) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("您没有权限!")))
    .build()
   p2?.reply(card)
   return
  }
  if(p1.size != 1) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("请输入Kook用户ID")))
    .build()
   p2?.reply(card)
   return
  }

  val p10 = p1[0].toString().replace("(met)","")
  val user = plugin.core.httpAPI.getUser(p10)

  val jsonArray = getJSONArray()
  val json = DataUtil.getJSONObjectWithID(jsonArray,p10)
  if(json == null) {
   p2.reply(CardBuilder()
    .setSize(Size.LG)
    .setTheme(Theme.DANGER)
    .addModule(SectionModule(MarkdownElement("这人目前没有账户")))
    .build())
   return
  }
  p2.reply(CardBuilder()
   .setSize(Size.LG)
   .setTheme(Theme.PRIMARY)
   .addModule(SectionModule(MarkdownElement("他目前的${MessageManager.moneyname}: ${json!!.getDouble("Amount")}")))
   .build())
 }

}