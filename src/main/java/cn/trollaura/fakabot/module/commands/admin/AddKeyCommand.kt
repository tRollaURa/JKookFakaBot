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
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.element.PlainTextElement
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.plugin.Plugin
import javax.xml.crypto.Data

/**
@author tRollaURa_
@since 2024/8/19
 */
@Command
class AddKeyCommand(val plugin: Plugin): Module("addkey", arrayOf(" <商品ID> <卡密>")) {
 init {
  justAdmin = true
  description = "添加某个商品的卡密"
 }
 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  val items = getItems()
  if (p1 == null || p0 == null || p2 == null) return
  if (!ConfigUtil.plugin.config.getStringList("Admin").contains(p0.id)) {
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
    .addModule(SectionModule(PlainTextElement("看清楚使用方法谢谢!")))
    .build()
   p2.reply(card)
   return
  }
  val item = DataUtil.getJSONObjectWithID(items,p1[0].toString())
  if(item == null) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("此商品并不存在!")))
    .build()
   p2.reply(card)
   return
  }
  val msgs = p1[1].toString().split("\n")
  msgs.forEach {
   if(item.getJSONArray("keys").contains(it)) {
    val card = CardBuilder()
     .setTheme(Theme.WARNING)
     .setSize(Size.LG)
     .addModule(SectionModule(MarkdownElement("`${it}` 此卡密已经存在!")))
     .build()
    p2.reply(card)
   }else {
    DataUtil.putItem(items,item,item.put("keys",item.getJSONArray("keys").put(it)))
    val card = CardBuilder()
     .setTheme(Theme.SUCCESS)
     .setSize(Size.LG)
     .addModule(SectionModule(MarkdownElement("`${it}` 已经被添加!")))
     .build()
    p2.reply(card)

   }

  }


 }

}