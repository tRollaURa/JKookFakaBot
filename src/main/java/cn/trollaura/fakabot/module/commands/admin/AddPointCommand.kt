package cn.trollaura.fakabot.module.commands.admin

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import cn.trollaura.fakabot.util.DataUtil
import snw.jkook.entity.User
import snw.jkook.message.Message
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.PlainTextElement
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/8/19
 */
@Command
class AddPointCommand(val plugin: Plugin): Module("add", arrayOf(" <@某人> <金额>")) {
 init {
  justAdmin = true
  description = "给某人添加金额"
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
  if(p1.size != 2) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("请输入Kook用户ID和金额")))
    .build()
   p2?.reply(card)
   return
  }
  val jsonA = getJSONArray()
  val p10 = p1[0].toString().replace("(met)","")
  val jsonUser = DataUtil.getJSONObjectWithID(jsonA,p10)
  if(jsonUser == null) {
   DataUtil.initUser(jsonA,p1[1].toString().toDouble(),plugin.core.httpAPI.getUser(p10))
   val card = CardBuilder()
    .setTheme(Theme.SUCCESS)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("添加成功")))
    .build()
   p2?.reply(card)
   return
  }
  DataUtil.putJson(jsonA,jsonUser,jsonUser.put("Amount",jsonUser.getDouble("Amount") + p1[1].toString().toDouble()))
  if(p1[1].toString().toDouble() < 0) {
   val card = CardBuilder()
    .setTheme(Theme.SUCCESS)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("扣除成功")))
    .build()
   p2?.reply(card)
  }

 }
}