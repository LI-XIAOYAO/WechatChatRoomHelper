package com.zdy.project.wechat_chatroom_helper.plugins.addition.hook

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Handler
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import com.zdy.project.wechat_chatroom_helper.LogUtils
import com.zdy.project.wechat_chatroom_helper.plugins.addition.DataModel
import com.zdy.project.wechat_chatroom_helper.plugins.addition.MyListAdapter
import com.zdy.project.wechat_chatroom_helper.plugins.addition.SpecialPluginEntry
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import org.xml.sax.InputSource
import java.io.StringReader
import java.util.ArrayList

object AutoSendSnsRequestHook {
    fun hook(classLoader: ClassLoader) {


        val dataClass = XposedHelpers.findClass("com.tencent.mm.pluginsdk.model.m", classLoader)
        val requestDataClass = XposedHelpers.findClass("com.tencent.mm.ai.m", classLoader)
        val requestHandlerClass = XposedHelpers.findClass("com.tencent.mm.model.aw", classLoader)


        val ContactInfoUI = XposedHelpers.findClass("com.tencent.mm.plugin.profile.ui.ContactInfoUI", classLoader)

        XposedHelpers.findAndHookMethod(ContactInfoUI, "initView", object : XC_MethodHook() {


            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)

                val thisObject = param.thisObject as Activity

                XposedHelpers.callMethod(thisObject, "addTextOptionMenu", 1, "加好友", object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {

                        val constructor = XposedHelpers.findConstructorExact(dataClass,
                                Int::class.java, java.util.List::class.java, java.util.List::class.java, java.util.List::class.java,
                                String::class.java, String::class.java, java.util.Map::class.java,
                                String::class.java, String::class.java)
                        constructor.isAccessible = true

                        val m = constructor.newInstance(2, ArrayList<String>().apply { add("helper_2018") }, ArrayList<Int>().apply { add(30) }, null,
                                "测试发送好友请求", "", HashMap<String, Int>().apply { put("helper_2018", 0) },
                                null, ""
                        )

                        val auDF = XposedHelpers.callStaticMethod(requestHandlerClass, "Rg")
                        XposedHelpers.callMethod(auDF, "a", m, 0)

                        return true
                    }
                })


            }
        })


        XposedHelpers.findAndHookConstructor(dataClass,
                Int::class.java, java.util.List::class.java, java.util.List::class.java, java.util.List::class.java,
                String::class.java, String::class.java, java.util.Map::class.java,
                String::class.java, String::class.java,
                object : XC_MethodHook() {

                    override fun afterHookedMethod(param: MethodHookParam) {


                        val args = param.args


                        XposedBridge.log("AutoSendSnsRequestHook, args = ${args.joinToString { if (it == null) "null" else it.toString() }}")


                    }
                })


    }

}
