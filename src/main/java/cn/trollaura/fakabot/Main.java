package cn.trollaura.fakabot;

import cn.trollaura.fakabot.managers.MessageManager;
import snw.jkook.plugin.BasePlugin;

import java.util.Date;
import java.util.List;


/**
 * @author tRollaURa_
 * @since 2024/8/18
 */
public class Main extends BasePlugin {
    public static List<String> categories;


    @Override
    public void onLoad() {

        saveDefaultConfig();
        reloadConfig();
    }

    @Override
    public void onEnable() {
        categories = getConfig().getStringList("Categories");
        saveResource("data/data.json",false,false);
        saveResource("data/items.json",false,false);
        new Loader(this).init();
        MessageManager.INSTANCE.init();
//        getCore().getHttpAPI().setListening("cloudmusic","tRollaURa_","输入 领养宝剑 获取您的宝剑吧!");
    }

}
