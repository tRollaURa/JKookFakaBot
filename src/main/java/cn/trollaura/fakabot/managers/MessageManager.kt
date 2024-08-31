package cn.trollaura.fakabot.managers

import cn.trollaura.fakabot.Loader
import cn.trollaura.fakabot.module.Module
import cn.trollaura.fakabot.util.ConfigUtil
import org.json.JSONObject
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.ButtonElement
import snw.jkook.message.component.card.element.MarkdownElement


object MessageManager {

    var modules = mutableSetOf<Module>()
    val moneyname = ConfigUtil.get<String>("MoneyName")
    fun init() {
        modules = Loader(ConfigUtil.plugin).modules
    }

    fun ButtonMessage(name: String,value: String,theme: Theme): ButtonElement {
        return ButtonElement(theme, value,ButtonElement.EventType.RETURN_VAL,MarkdownElement(name))
    }


}