package cn.trollaura.fakabot.module.listeners

import cn.trollaura.api.获取网站生数据
import cn.trollaura.fakabot.Main
import cn.trollaura.fakabot.annotations.Listener
import cn.trollaura.fakabot.managers.MessageManager
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.pay.PaymentUrlGenerator
import cn.trollaura.fakabot.util.DataUtil
import org.json.JSONArray
import org.json.JSONObject
import snw.jkook.entity.User
import snw.jkook.entity.channel.TextChannel
import snw.jkook.event.EventHandler
import snw.jkook.event.user.UserClickButtonEvent
import snw.jkook.message.Message
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.MultipleCardComponent
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.*
import snw.jkook.message.component.card.module.ActionGroupModule
import snw.jkook.message.component.card.module.ContextModule
import snw.jkook.message.component.card.module.ImageGroupModule
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.message.component.card.structure.Paragraph
import snw.jkook.plugin.Plugin
import java.time.LocalDate
import java.util.*
import javax.xml.soap.Text
import kotlin.random.Random

/**
@author tRollaURa_
@since 2024/8/1
 */
@Listener
class ClickButton(val plugin: Plugin): Module("ClickButton") {
 var rechargeMsg = mutableMapOf<User,String>()

 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  onCommand(p0,p1,p2)
 }

 @EventHandler
 fun onClick(evt: UserClickButtonEvent) {

  val jsonArray = getJSONArray()
  val items = getItems()
  val jsonUser = DataUtil.getJSONObjectWithID(jsonArray, evt.user.id)

  val jsonT = JSONObject(evt.value)


  when(jsonT.getString("Type").lowercase()) {
   "recharge" -> {
    val json = JSONObject(evt.value)
    plugin.core.httpAPI.getPrivateMessage(evt.user, evt.messageId).delete()
    val amount = json.getDouble("Amount")
    val url = PaymentUrlGenerator.generateURL(evt.user.id,amount)
    rechargeMsg[evt.user] = evt.user.sendPrivateMessage(CardBuilder()
     .setSize(Size.LG)
     .setTheme(Theme.PRIMARY)
     .addModule(SectionModule(MarkdownElement("订单已经为您生成，请点击下方按钮呼起浏览器!")))
     .addModule(SectionModule(MarkdownElement("支付完成后请点击查询订单!")))
     .addModule(
      ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("查询订单",
       JSONObject().apply {
        put("Type","Order")
        put("Amount",amount)
        put("OrderTime",PaymentUrlGenerator.time[evt.user.id])
        put("Sender",evt.user)
       }.toString()
       , Theme.PRIMARY),
       ButtonElement(Theme.PRIMARY,url,ButtonElement.EventType.LINK,MarkdownElement("支付"))))
     )
     .build())

   }
   "order" -> {
    val json = JSONObject(evt.value)
    val amount = json.getDouble("Amount")
    val get = 获取网站生数据("https://hlcode.huluwl.com/api.html?act=order&pid=6954&key=lcZE0J2c3Yc0GLE6D3LV6kqbZ0N6qBKK&type=wxpay&out_trade_no=${json.getString("OrderTime")}").joinToString()
    val msg = evt.user!!.sendPrivateMessage(
     CardBuilder()
      .setTheme(Theme.PRIMARY)
      .setSize(Size.LG)
      .addModule(SectionModule(MarkdownElement("⏰正在为您查询订单 请等待1-10秒!")))
      .build()
    )
    Thread.sleep(Random.nextLong(2000))
    if(get.contains("\"status\":1")) {
     plugin.core.httpAPI.getPrivateMessage(evt.user,msg).component = CardBuilder()
      .setSize(Size.LG)
      .setTheme(Theme.SUCCESS)
      .addModule(ImageGroupModule(arrayListOf(ImageElement("https://img.kookapp.cn/assets/2024-08/18/EMAzG6VfXS0zy0zy.jpg","yeah!",false))))
      .addModule( SectionModule("好耶! 已经充值 $amount ${MessageManager.moneyname}!"))
      .build()
     if(DataUtil.hasID(evt.user,jsonArray)) {
      DataUtil.putJson(jsonArray,jsonUser!!,jsonUser.put("Amount",jsonUser.getDouble("Amount")+ amount))
     }else {
      DataUtil.initUser(jsonArray,amount,evt.user)
     }
     if(rechargeMsg[evt.user] != null) {
      plugin.core.httpAPI.getPrivateMessage(evt.user,rechargeMsg[evt.user]!!).delete()
     }


    }else {
     plugin.core.httpAPI.getPrivateMessage(evt.user,msg).component =  CardBuilder().setTheme(Theme.SUCCESS).setSize(Size.LG)
      .addModule(SectionModule(MarkdownElement("**您未完成支付,如已支付,请寻找开发者解决!**"))).build()
    }
   }
   "buy" -> {
    val json = JSONObject(evt.value)
/*    if(evt.user != json.get("Sender") as User) {
     plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
      CardBuilder()
       .setTheme(Theme.WARNING)
       .setSize(Size.LG)
       .addModule(SectionModule(MarkdownElement("这不是您开启的商店,请用.shop重新开启一个")))
       .build(),
      null,
      evt.user
     )
    }*/
    val item = DataUtil.getJSONObjectWithID(items, json.getString("ID"))!!
    if(json == null) {
     plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
      CardBuilder()
       .setTheme(Theme.PRIMARY)
       .setSize(Size.LG)
       .addModule(SectionModule(MarkdownElement("您的${MessageManager.moneyname}不够!")))
       .build(),
      null,
      evt.user
     )
     return
    }

    if(jsonUser!!.getDouble("Amount") >= json.getDouble("Amount")) {
     if(item.getBoolean("manually")) {
      DataUtil.putJson(jsonArray, jsonUser,jsonUser.put("Amount",jsonUser.getDouble("Amount")- json.getDouble("Amount")))
      evt.user!!.sendPrivateMessage(
       CardBuilder()
        .setTheme(Theme.PRIMARY)
        .setSize(Size.LG)
        .addModule(SectionModule(MarkdownElement("您已经购买成功!")))
        .addModule(SectionModule(MarkdownElement("已自动添加到购买记录!")))
        .build()
      )

      val time = System.currentTimeMillis()
      DataUtil.putJson(jsonArray,jsonUser,jsonUser.put("Items",jsonUser.getJSONArray("Items").put("${json.getString("ID")}-${time}"),

       ))

      evt.user!!.sendPrivateMessage(
       CardBuilder()
        .setTheme(Theme.PRIMARY)
        .setSize(Size.LG)
        .addModule(SectionModule(MarkdownElement("请带着下方购买凭据寻找机器人主人!")))
        .addModule(SectionModule(MarkdownElement("`${item.getString("name")}-${item.getString("ID")}-${time}-${evt.user!!.id}`")))
        .build()
      )
      plugin.core.httpAPI.getTextChannelMessage(evt.messageId).component = shopMessage(items,DataUtil.getCategoryByID(items,json.getString("Category"))!!,evt.user)
      return
     }
     if(DataUtil.getJSONObjectWithID(items,json.getString("ID"))!!.getJSONArray("keys").isEmpty) {
      plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
       CardBuilder()
        .setTheme(Theme.PRIMARY)
        .setSize(Size.LG)
        .addModule(SectionModule(MarkdownElement("抱歉,目前此商品库存为空 请等待补货!!")))
        .build(),
       null,
       evt.user
      )
      return
     }
     //移除Amount
      DataUtil.putJson(jsonArray, jsonUser,jsonUser.put("Amount",jsonUser.getDouble("Amount")- json.getDouble("Amount")))
     evt.user!!.sendPrivateMessage(
      CardBuilder()
       .setTheme(Theme.PRIMARY)
       .setSize(Size.LG)
       .addModule(SectionModule(MarkdownElement("您已经购买成功!")))
       .addModule(SectionModule(MarkdownElement("已自动添加到购买记录!")))
       .build()
     )

     DataUtil.putJson(jsonArray,jsonUser,jsonUser.put("Items",jsonUser.getJSONArray("Items").put("${json.getString("ID")}-${System.currentTimeMillis()}"),

     ))

     evt.user!!.sendPrivateMessage(
      CardBuilder()
       .setTheme(Theme.PRIMARY)
       .setSize(Size.LG)
       .addModule(SectionModule(MarkdownElement("下方是您的卡密!")))
       .addModule(SectionModule(MarkdownElement(DataUtil.getJSONObjectWithID(items,json.getString("ID"))!!.getJSONArray("keys")[0].toString())))
       .build()
     )
     DataUtil.putItem(
      items,
      item,
      item
       .put("keys", item.getJSONArray("keys").filter { it.toString() !=  item.getJSONArray("keys")[0]})
     )
     plugin.core.httpAPI.getTextChannelMessage(evt.messageId).component = shopMessage(items,DataUtil.getCategoryByID(items,json.getString("Category"))!!,evt.user)
    }else {
     plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
      CardBuilder()
       .setTheme(Theme.PRIMARY)
       .setSize(Size.LG)
       .addModule(SectionModule(MarkdownElement("您的${MessageManager.moneyname}不够!")))
       .build()
      ,null,
      evt.user
     )
    }
   }
   "infor" -> {
    val json = JSONObject(evt.value)
    val item = DataUtil.getJSONObjectWithID(items, json.getString("ID"))!!
    val infor = item.getString("infor")

    if(infor.startsWith("https://") || infor.startsWith("http://")) {
     val CB =       CardBuilder()
      .setTheme(Theme.PRIMARY)
      .setSize(Size.LG)


     获取网站生数据(infor).forEach {
      CB.addModule(SectionModule(MarkdownElement(it)))
     }


     evt.user
     plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
      CB.build(),
      null,
      evt.user
     )
     return
    }
    plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
     CardBuilder()
      .setSize(Size.LG)
      .setTheme(Theme.PRIMARY)
      .addModule(SectionModule(MarkdownElement(infor)))
      .build(),
     null,
     evt.user
    )
   }
   "unfold" -> {
    val json = JSONObject(evt.value)
    val category = json.getString("Category")
    val itemss = DataUtil.getJSONObjectsWithCategory(items, category)
    if(itemss.isEmpty()) {
     plugin.core.httpAPI.getTextChannelMessage(evt.messageId).channel.sendComponent(
      CardBuilder()
       .setTheme(Theme.PRIMARY)
       .setSize(Size.LG)
       .addModule(SectionModule(MarkdownElement("抱歉,目前此分类没有商品")))
       .build(),
      null,
      evt.user
     )
     return
    }
    plugin.core.httpAPI.getTextChannelMessage(evt.messageId).component = shopMessage(items,category,evt.user)

   }
   "exit" -> {
    plugin.core.httpAPI.getTextChannelMessage(evt.messageId).component = shopCategoryMessage(evt.user)
   }
  }

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
 fun shopMessage(items: JSONArray,category: String,user: User): MultipleCardComponent {
  val CB = CardBuilder().setSize(Size.LG).setTheme(Theme.PRIMARY)
   .addModule(SectionModule(MarkdownElement("**可购入的商品: **")))
   .addModule(SectionModule(MarkdownElement("**注意: `商品名后为价格` **")))
  CB.addModule(ContextModule(arrayListOf<BaseElement>(ImageElement(user.getAvatarUrl(user.isVip),"avatar",true),MarkdownElement("此条消息由${user.name}唤起!"))))
  val itemss = DataUtil.getJSONObjectsWithCategory(items,category)
  itemss.forEach { ak ->
   val it = ak as JSONObject
   CB
    .addModule(SectionModule(MarkdownElement("`${it.getString("name")}` - **${it.getDouble("amount")} ${MessageManager.moneyname}**")))


   if(it.getBoolean("manually")) {

    CB.addModule(ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("购入 (手动发货)",
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
      , Theme.PRIMARY),
     MessageManager.ButtonMessage("返回",
      JSONObject().apply {
       put("Type","Exit")
       put("Sender",user)
      }.toString()
      , Theme.DANGER)))
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
      , Theme.PRIMARY),
     MessageManager.ButtonMessage("返回",
      JSONObject().apply {
       put("Type","Exit")
       put("Sender",user)
      }.toString()
      , Theme.DANGER)))
    )
   }


  }
   CB.addModule(ContextModule(arrayListOf<BaseElement>(MarkdownElement("此条消息展示的可能不是最新的内容,如有需要请输入.shop查看最新商品信息!"))))
  return CB.build()
 }

}