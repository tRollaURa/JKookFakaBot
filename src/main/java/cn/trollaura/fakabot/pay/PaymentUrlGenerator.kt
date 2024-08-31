package cn.trollaura.fakabot.pay

import cn.trollaura.fakabot.util.ConfigUtil
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
@author tRollaURa_
@since 2024/8/18
 */
object PaymentUrlGenerator {

    private val HL_CODE_ID = ConfigUtil.plugin.config.getString("CodeID")!!
    private val HL_CODE_KEY = ConfigUtil.plugin.config.getString("CodeKey")!!
    var time = mutableMapOf<String,String>()

    private fun getOrderTime(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS")
        return sdf.format(Date())
    }

    fun generateURL(whoBuy: String,money: Double): String {
        val time = getOrderTime()
        this.time.put(whoBuy,time)
        val parameters = mutableMapOf(
            "pid" to HL_CODE_ID,
            "type" to "wxpay",
            "notify_url" to ConfigUtil.get<String>("NotifyURL"),
            "return_url" to ConfigUtil.get<String>("ReturnURL"),
            "out_trade_no" to time,
            "name" to RandomName.entries[Random.nextInt(RandomName.entries.size)].names  ,
            "money" to money.toString(),
            "sitename" to "Mziac"
        )

        val sortedParams = parameters.toSortedMap()
        val signBuilder = StringBuilder()

        for ((key, value) in sortedParams) {
            if (value.isNotEmpty() && key != "sign" && key != "sign_type") {
                if (signBuilder.isNotEmpty()) {
                    signBuilder.append("&")
                }
                signBuilder.append("$key=$value")
            }
        }

        val generatedSign = md5(signBuilder.toString() + HL_CODE_KEY)

        val urlParams = StringBuilder()

        for ((key, value) in sortedParams) {
            if (urlParams.isNotEmpty()) {
                urlParams.append("&")
            }
            urlParams.append("$key=").append(URLEncoder.encode(value, "UTF-8"))
        }
        urlParams.append("&sign=").append(generatedSign)

        val paymentUrl = "https://hlcode.huluwl.com/submit?$urlParams"
        return paymentUrl
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { String.format("%02x", it) }
    }


    enum class RandomName(val names: String) {
        A("吴许春驴肉火锅店"),
        B("张晓阳师傅卤味店"),
        C("杨超风味面馆"),
        D("邓彦松小食"),
        E("程鋆达小面点"),
        F("胡春生菜馆"),
        G("海棠饭馆")
    }

}
