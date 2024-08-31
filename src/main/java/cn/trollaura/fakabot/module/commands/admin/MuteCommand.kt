package cn.trollaura.fakabot.module.commands.admin

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import snw.jkook.command.UserCommandExecutor
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
class MuteCommand(val plugin: Plugin): Module("mute", arrayOf(" <@某人>")) {
 init {
  justAdmin = true
     description = "为了防止有人狗叫,我要把这个Mute备在身上"
 }
 val muteList = mutableMapOf<User,Collection<Int>>()
 override fun onCommand(p0: User?, p1: Array<out Any>?, p2: Message?) {

  if (!ConfigUtil.plugin.config.getStringList("Admin").contains(p0!!.id)) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("您没有权限!")))
    .build()
   p2?.reply(card)
    return
  }


  if (p1!!.isEmpty()) {
   val card = CardBuilder()
    .setTheme(Theme.WARNING)
    .setSize(Size.LG)
    .addModule(SectionModule(PlainTextElement("请输入Kook用户ID")))
    .build()
   p2?.reply(card)
    return
  }
  val guild = plugin.core.httpAPI.getTextChannelMessage(p2!!.id).channel.guild
  val roles = guild.roles
  while(roles.hasNext()) {
   val role = roles.next()
   val p10 = p1[0].toString().replace("(met)","")
   for (role1 in role) {
    if(role1.name == "Mute") {
     val user = plugin.core.httpAPI.getUser(p10)


     val userRoles = user.getRoles(guild)
     if(muteList.containsKey(user)) {
      userRoles.forEach {
       if(it == role1.id) {


        user.revokeRole(role1)
        for (role2 in muteList[user]!!) {
         user.grantRole(guild,role2)
        }
        muteList.remove(user)
        p2.reply("已经解禁这个东西")
         return
       }
      }
      return
     }


     muteList[user] = userRoles


     userRoles.forEach {
      user.revokeRole(guild,it)
     }
     user.grantRole(role1)
     p2.reply("已经禁言这个东西")
      return
    }
   }
  }
 }

}