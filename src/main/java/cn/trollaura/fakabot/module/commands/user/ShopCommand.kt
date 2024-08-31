package cn.trollaura.fakabot.module.commands.user

import cn.trollaura.fakabot.Loader
import cn.trollaura.fakabot.Main
import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.managers.MessageManager
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import org.json.JSONArray
import org.json.JSONObject
import snw.jkook.config.MemorySection
import snw.jkook.entity.User
import snw.jkook.message.Message
import snw.jkook.message.PrivateMessage
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.MultipleCardComponent
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.BaseElement
import snw.jkook.message.component.card.element.InteractElement
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.module.ActionGroupModule
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.message.component.card.structure.Paragraph
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/8/18
 */
@Command
class ShopCommand(val plugin: Plugin): Module("shop") {
 init {
  description = "查看商店"
 }

 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  if (p1 == null || p0 == null || p2 == null) return

  if (p2 is PrivateMessage) {
   p2.reply(
    CardBuilder()
     .setSize(Size.LG)
     .setTheme(Theme.WARNING)
     .addModule(SectionModule(MarkdownElement("因特殊原因此指令无法在私聊使用!")))
     .build()
   )
   return
  }

  val products = getItems()
  val CB = shopCategoryMessage(p0)
  p2.reply(CB)
 }

 fun shopCategoryMessage(sender: User): MultipleCardComponent {
  val CB = CardBuilder().setSize(Size.LG).setTheme(Theme.PRIMARY)
   .addModule(SectionModule(MarkdownElement("**可购入的商品类别: **")))
  Main.categories.forEach {
   CB
    .addModule(
     SectionModule(
      Paragraph(
       2, arrayListOf<BaseElement>(MarkdownElement("**${it}**")).toCollection(ArrayList())
      )
     )
    )
   CB.addModule(ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage(
    "展开",
    JSONObject().apply {
     put("Type", "Unfold")
     put("Category", it)
     put("Sender", sender)
    }.toString(), Theme.PRIMARY))))
  }

  return CB.build()
 }

 /*CB
    .addModule(SectionModule(MarkdownElement("`${it.getString("name")}` - **${it.getDouble("amount")} ${MessageManager.moneyname}**")))


   if(it.getBoolean("manually")) {
    CB.addModule(ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("购入 (手动发货)",
     JSONObject().apply {
      put("Type","Buy")
      put("Amount",it.getDouble("amount"))
      put("ID",it.getString("ID"))
     }.toString()
     , Theme.PRIMARY)
     ,
     MessageManager.ButtonMessage("详细信息",
      JSONObject().apply {
       put("Type","Infor")
       put("ID",it.getString("ID"))
      }.toString()
      , Theme.PRIMARY)
    ))
    )
   }else {
    CB.addModule(ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("购入 (库存: ${it.getJSONArray("keys").length()})",
     JSONObject().apply {
      put("Type","Buy")
      put("Amount",it.getDouble("amount"))
      put("ID",it.getString("ID"))
     }.toString()
     , Theme.PRIMARY),
     MessageManager.ButtonMessage("详细信息",
      JSONObject().apply {
       put("Type","Infor")
       put("ID",it.getString("ID"))
      }.toString()
      , Theme.PRIMARY)))
    )
   }


  }*/

 fun shopMessage(items: JSONArray): MultipleCardComponent {
  val CB = CardBuilder().setSize(Size.LG).setTheme(Theme.PRIMARY)
   .addModule(SectionModule(MarkdownElement("**可购入的商品: **")))
   .addModule(SectionModule(MarkdownElement("**注意: `商品名后为价格` **")))
  for (i in 0 until items.length()) {
   val it = items.getJSONObject(i) as JSONObject
   CB
    .addModule(SectionModule(MarkdownElement("`${it.getString("name")}` - **${it.getDouble("amount")} ${MessageManager.moneyname}**")))


    if(it.getBoolean("manually")) {
     CB.addModule(ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("购入 (手动发货)",
      JSONObject().apply {
       put("Type","Buy")
       put("Amount",it.getDouble("amount"))
       put("ID",it.getString("ID"))
      }.toString()
      , Theme.PRIMARY)
     ,
      MessageManager.ButtonMessage("详细信息",
       JSONObject().apply {
        put("Type","Infor")
        put("ID",it.getString("ID"))
       }.toString()
       , Theme.PRIMARY)
      ))
     )
    }else {
     CB.addModule(ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("购入 (库存: ${it.getJSONArray("keys").length()})",
      JSONObject().apply {
       put("Type","Buy")
       put("Amount",it.getDouble("amount"))
       put("ID",it.getString("ID"))
      }.toString()
      , Theme.PRIMARY),
      MessageManager.ButtonMessage("详细信息",
       JSONObject().apply {
        put("Type","Infor")
        put("ID",it.getString("ID"))
       }.toString()
       , Theme.PRIMARY)))
     )
    }


  }
 return CB.build()
 }

}