package cn.trollaura.fakabot.util

import cn.trollaura.fakabot.annotations.Command
import org.json.JSONArray
import org.json.JSONObject
import snw.jkook.entity.User
import java.io.File

object DataUtil {
    val filePath = "${ConfigUtil.plugin.file.parent}/${ConfigUtil.plugin.description.name}/data/data.json"
    val filePath2 = "${ConfigUtil.plugin.file.parent}/${ConfigUtil.plugin.description.name}/data/items.json"
    val file = File(filePath)
    val file2 = File(filePath2)


    fun hasID(user: User?,jsonArray: JSONArray): Boolean {
        for (i in 0 until jsonArray.length()) {
            val jo = jsonArray.getJSONObject(i)
            if (jo.getString("ID") == user!!.id) {
                return true
            }
        }
        return false
    }

    fun initUser(jsonArray: JSONArray,amount: Double,user: User?) {
        file.writeText(jsonArray.put(JSONObject().apply {
            put("ID",user!!.id)
            put("Amount",amount)
            put("Name",user.name)
            put("Items", JSONArray())

        }).toString(4))
    }
    fun initItem(jsonArray: JSONArray,infor: String,manually: Boolean,amount: Double,id: String,name: String,keys: MutableList<String>,category: String) {
        file2.writeText(jsonArray.put(JSONObject().apply {
            put("ID",id)
            put("amount",amount)
            put("name",name)
            put("infor",infor)
            put("manually",manually)
            put("keys", JSONArray().apply {
                keys.forEach {
                    put(it)
                }
            })
            put("category",category)

        }).toString(4))
    }
    fun getJSONObjectsWithCategory(jsonArray: JSONArray,cateogry: String): List<Any> {
        return jsonArray.filter {  (it as JSONObject).getString("category") == cateogry }

    }

    fun getCategoryByID(jsonArray: JSONArray,id: String): String? {
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            if(json.getString("ID") == id) {
                return json.getString("category") ?: null
            }

        }
        return null
    }


    fun getJSONObjectWithID(jsonArray: JSONArray,userID: String): JSONObject? {
        return jsonArray.find { (it as JSONObject).getString("ID") == userID } as JSONObject?

    }


    fun putJson(jsonArray: JSONArray, key: JSONObject, newJSONObject: JSONObject): Boolean {
        val index = getIndexWithJSONObject(jsonArray, key)
        return if (index != -1) {
            jsonArray.put(index, newJSONObject)
            file.writeText(jsonArray.toString(4))
            true
        } else {
            false
        }
    }
    fun putItem(jsonArray: JSONArray, key: JSONObject, newJSONObject: JSONObject): Boolean {
        val index = getIndexWithJSONObject(jsonArray, key)
        return if (index != -1) {
            jsonArray.put(index, newJSONObject)
            file2.writeText(jsonArray.toString(4))
            true
        } else {
            false
        }
    }

    fun getIndexWithJSONObject(jsonArray: JSONArray, key: JSONObject): Int {
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.similar(key)) {
                return i
            }
        }
        return -1
    }


}