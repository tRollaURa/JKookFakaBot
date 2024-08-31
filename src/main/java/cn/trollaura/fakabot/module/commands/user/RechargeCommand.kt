package cn.trollaura.fakabot.module.commands.user

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.managers.MessageManager
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import org.json.JSONObject
import snw.jkook.entity.User
import snw.jkook.message.Message
import snw.jkook.message.PrivateMessage
import snw.jkook.message.component.card.CardBuilder
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.InteractElement
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.module.ActionGroupModule
import snw.jkook.message.component.card.module.SectionModule
import snw.jkook.plugin.Plugin

/**
@author tRollaURa_
@since 2024/8/18
 */
@Command
class RechargeCommand(val plugin: Plugin): Module("recharge", arrayOf(" <金额>")) {
 val lowNumberPaid = mutableMapOf<User,Int>()
 init {
     description = "充值金额(仅支持威信之父)"
 }

 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {
  if(p1 == null || p0 == null || p2 == null) return
  if(p2 !is PrivateMessage) {
   p2.reply(
    CardBuilder()
     .setSize(Size.LG)
     .setTheme(Theme.WARNING)
     .addModule(SectionModule(MarkdownElement("请在私聊发送此消息!")))
     .build()
   )
   return
  }
  when(p1.size) {
   0 -> {
    p2.reply(
     CardBuilder()
      .setSize(Size.LG)
      .setTheme(Theme.WARNING)
      .addModule(SectionModule(MarkdownElement("您要充值的金额呢?")))
      .build()
    )
   }

   1 -> {
    try {
     if (p1[0].toString().toDouble() > 114514) {
      p2.reply(
       CardBuilder()
        .setSize(Size.LG)
        .setTheme(Theme.WARNING)
        .addModule(SectionModule(MarkdownElement("您要充值的数字太大了吧?")))
        .addModule(SectionModule(MarkdownElement("你真的支付得起吗(小声)..")))
        .build()
      )
      return
     }
     if (p1[0].toString().toDouble() < 1.00) {
      if (lowNumberPaid.containsKey(p0)) {
       if (lowNumberPaid[p0]!! >= 1) {
        p2.reply(
         CardBuilder()
          .setSize(Size.LG)
          .setTheme(Theme.WARNING)
          .addModule(SectionModule(MarkdownElement("您今天已经生成了3次小额订单,请明天再试")))
          .addModule(SectionModule(MarkdownElement("现在您至少生成大于等于1.00${MessageManager.moneyname}")))
          .build()
        )
        return
       } else {
        lowNumberPaid[p0] = lowNumberPaid[p0]!! + 1
       }
      } else {
       lowNumberPaid[p0] = 1
      }
     }

     p2.reply(
      CardBuilder()
       .setSize(Size.LG)
       .setTheme(Theme.WARNING)
       .addModule(SectionModule(MarkdownElement("您确定要支付 ${p1[0]} 元吗? (如误点,请不要继续支付)")))
       .addModule(
        ActionGroupModule(arrayListOf<InteractElement>(MessageManager.ButtonMessage("确认支付",
         JSONObject().apply {
          put("Type", "Recharge")
          put("Amount", p1[0].toString().toDouble())
          put("Sender", p0.id)
         }.toString(), Theme.PRIMARY
        )
        )
        )
       )
       .build()
     )
    }catch (e: NumberFormatException) {
     p2.reply(
      CardBuilder()
       .setSize(Size.LG)
       .setTheme(Theme.WARNING)
       .addModule(SectionModule(MarkdownElement("您得输入一个数字啊.. ")))
       .build()
     )
    }
   }
  }
 }


}