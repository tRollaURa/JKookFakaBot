package cn.trollaura.fakabot

import cn.trollaura.fakabot.annotations.Command
import cn.trollaura.fakabot.annotations.Listener
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.module.commands.admin.*
import cn.trollaura.fakabot.module.commands.user.*
import cn.trollaura.fakabot.module.listeners.ClickButton
import cn.trollaura.fakabot.module.listeners.MessageReader
import cn.trollaura.fakabot.util.ConfigUtil
import snw.jkook.command.JKookCommand
import snw.jkook.command.UserCommandExecutor
import snw.jkook.plugin.Plugin


/**
@author tRollaURa_
@since 2024/8/18
 */

class Loader(val plugin: Plugin) {
    var modules = mutableSetOf<Module>()

    init {
        ConfigUtil.init(plugin)

        modules.add(AddKeyCommand(plugin))
        modules.add(CreateProductCommand(plugin))
        modules.add(AddPointCommand(plugin))
        modules.add(GetUserCommand(plugin))
        modules.add(MuteCommand(plugin))
        modules.add(MeCommand(plugin))
        modules.add(ReloadCommand(plugin))
        modules.add(ShopCommand(plugin))
        modules.add(RechargeCommand(plugin))
        modules.add(HelpCommand(plugin))
        modules.add(MessageReader(plugin))

        modules.add(ClickButton(plugin))

    }



    fun init() {


        modules.filter { it::class.java.annotations.contains(Command()) }.forEach { cmd ->
            JKookCommand(cmd.name, ConfigUtil.get<String>("CommandPrefix"))
                .executesUser(cmd as UserCommandExecutor)
                .register(plugin)
        }

        modules.filter { it::class.java.annotations.contains(Listener()) }.forEach { listener ->
            plugin.core.eventManager.registerHandlers(plugin, listener as snw.jkook.event.Listener)

        }
    }
}