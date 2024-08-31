package cn.trollaura.fakabot.module.commands.admin

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import cn.trollaura.fakabot.util.DataUtil
import org.json.JSONArray
import org.json.JSONObject
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
class CreateProductCommand(val plugin: Plugin): Module("create", arrayOf(" <名称> <ID> <金额> <手动发货吗> <介绍> <卡密> <种类>"," <名称> <ID> <金额> <手动发货吗> <介绍> <种类>"," <名称> <ID> <金额> <手动发货吗> <种类>"," <名称> <ID> <金额> <种类>")) {
 init {
  justAdmin = true
  description = "创建一个新的商品"
 }
 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  val items = getItems()
  if (p1 == null || p0 == null || p2 == null) return
  if (!ConfigUtil.plugin.config.getStringList("Admin").contains(p0!!.id)) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("您没有权限!")))
    .build()
   p2?.reply(card)
   return
  }
  when(p1.size) {
   4 -> {
    val item = DataUtil.getJSONObjectWithID(items,p1[1].toString())
    if(item != null) {
     val card = CardBuilder()
      .setTheme(Theme.WARNING)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("此商品ID已经存在,请换个ID")))
      .build()
     p2.reply(card)
     return
    }
    try {
     DataUtil.initItem(items,"本商品没有介绍呜呜呜",true,p1[2].toString().toDouble(),p1[1].toString(),p1[0].toString(),
      mutableListOf(),p1[3].toString())
     val card = CardBuilder()
      .setTheme(Theme.SUCCESS)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("已经添加了此商品,输入.shop查看!!")))
      .build()
     p2.reply(card)
     return
    }catch (e: Exception) {
     p2.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
      .addModule(SectionModule(MarkdownElement("添加时出现问题,请尝试检查下面的日志!\n```java\n${e.message}\n${e.cause}\n```"))).build())
    }
   }
   5 -> {
    val item = DataUtil.getJSONObjectWithID(items,p1[1].toString())
    if(item != null) {
     val card = CardBuilder()
      .setTheme(Theme.WARNING)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("此商品ID已经存在,请换个ID")))
      .build()
     p2.reply(card)
     return
    }
    try {
     DataUtil.initItem(items,"本商品没有介绍呜呜呜",p1[3].toString().toBoolean(),p1[2].toString().toDouble(),p1[1].toString(),p1[0].toString(),
      mutableListOf(),p1[4].toString())
     val card = CardBuilder()
      .setTheme(Theme.SUCCESS)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("已经添加了此商品,输入.shop查看!!")))
      .build()
     p2.reply(card)
     return
    }catch (e: Exception) {
     p2.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
      .addModule(SectionModule(MarkdownElement("添加时出现问题,请尝试检查下面的日志!\n```java\n${e.message}\n${e.cause}\n```"))).build())
    }
   }
   6 -> {
    val item = DataUtil.getJSONObjectWithID(items,p1[1].toString())
    if(item != null) {
     val card = CardBuilder()
      .setTheme(Theme.WARNING)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("此商品ID已经存在,请换个ID")))
      .build()
     p2.reply(card)
     return
    }
    try {
     DataUtil.initItem(items,p1[4].toString(),p1[3].toString().toBoolean(),p1[2].toString().toDouble(),p1[1].toString(),p1[0].toString(),
      mutableListOf(),p1[5].toString())
     val card = CardBuilder()
      .setTheme(Theme.SUCCESS)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("已经添加了此商品,输入.shop查看!!")))
      .build()
     p2.reply(card)
     return
    }catch (e: Exception) {
     p2.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
      .addModule(SectionModule(MarkdownElement("添加时出现问题,请尝试检查下面的日志!\n```java\n${e.message}\n${e.cause}\n```"))).build())
    }
   }
   7 -> {
    val item = DataUtil.getJSONObjectWithID(items,p1[1].toString())
    if(item != null) {
     val card = CardBuilder()
      .setTheme(Theme.WARNING)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("此商品ID已经存在,请换个ID")))
      .build()
     p2.reply(card)
     return
    }
    try {
     val msgs = p1[5].toString().split("\n")
     val keys = mutableListOf<String>()
     msgs.forEach {
      keys.add(it)
     }
     DataUtil.initItem(items,p1[4].toString(),p1[3].toString().toBoolean(),p1[2].toString().toDouble(),p1[1].toString(),p1[0].toString(),keys,p1[6].toString())
     val card = CardBuilder()
      .setTheme(Theme.SUCCESS)
      .setSize(Size.LG)
      .addModule(SectionModule(PlainTextElement("已经添加了此商品,输入.shop查看!!")))
      .build()
     p2.reply(card)
     return
    }catch (e: Exception) {
     p2.reply(CardBuilder().setSize(Size.LG).setTheme(Theme.DANGER)
      .addModule(SectionModule(MarkdownElement("添加时出现问题,请尝试检查下面的日志!\n```java\n${e.message}\n${e.cause}\n```"))).build())
    }
   }
   else -> {
    val card = CardBuilder()
     .setTheme(Theme.WARNING)
     .setSize(Size.LG)
     .addModule(SectionModule(PlainTextElement("看清楚使用方法谢谢!")))
     .build()
    p2.reply(card)

    return
   }
  }


 }
}