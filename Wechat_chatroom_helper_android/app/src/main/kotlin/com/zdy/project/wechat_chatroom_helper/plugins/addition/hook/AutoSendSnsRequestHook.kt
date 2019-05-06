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
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

object AutoSendSnsRequestHook {
    fun hook(classLoader: ClassLoader) {


        var result = ""

        val dataClass = XposedHelpers.findClass("com.tencent.mm.pluginsdk.model.m", classLoader)
        val requestDataClass = XposedHelpers.findClass("com.tencent.mm.ai.m", classLoader)
        val requestHandlerClass = XposedHelpers.findClass("com.tencent.mm.model.aw", classLoader)
        val wechatRequestHandlerClass = XposedHelpers.findClass("com.tencent.mm.plugin.messenger.a.f", classLoader)


        val ContactInfoUI = XposedHelpers.findClass("com.tencent.mm.plugin.profile.ui.ContactInfoUI", classLoader)

        XposedHelpers.findAndHookMethod(ContactInfoUI, "initView", object : XC_MethodHook() {


            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)

                val thisObject = param.thisObject as Activity

                XposedHelpers.callMethod(thisObject, "addTextOptionMenu", 0, "加好友", object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {

                        val constructor = XposedHelpers.findConstructorExact(dataClass,
                                Int::class.java, java.util.List::class.java, java.util.List::class.java, java.util.List::class.java,
                                String::class.java, String::class.java, java.util.Map::class.java,
                                String::class.java, String::class.java)
                        constructor.isAccessible = true

                        val m = constructor.newInstance(2, ArrayList<String>().apply { add(result) }, ArrayList<Int>().apply { add(3) }, null,
                                "你好我是", "", HashMap<String, Int>().apply { put(result, 0) },
                                null, ""
                        )

                        val auDF = XposedHelpers.callStaticMethod(requestHandlerClass, "Rg")
                        XposedHelpers.callMethod(auDF, "a", m, 0)

                        return true
                    }
                })


                //回调获得ticket
                XposedHelpers.callMethod(thisObject, "addTextOptionMenu", 1, "加", object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {

                        val interface1 = XposedHelpers.findClass("com.tencent.mm.ai.f", classLoader)
                        val array = Array<Class<*>>(1, init = { interface1 })
                        val newObject = Proxy.newProxyInstance(classLoader, array, object : InvocationHandler {
                            override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
                                args?.let {
                                    XposedBridge.log("AutoSendSnsRequestHook, newProxyInstance , method = $method, args = ${args.joinToString { if (it == null) "null" else it.toString() }}")
                                }
                                if (method == null) {
                                    return null
                                } else return when {
                                    String::class.java == method.returnType -> ""
                                    Int::class.java == method.returnType -> Integer.valueOf(0)
                                    Int::class.javaPrimitiveType == method.returnType -> 0
                                    Boolean::class.java == method.returnType -> java.lang.Boolean.FALSE
                                    Boolean::class.javaPrimitiveType == method.returnType -> false
                                    else -> null
                                }
                            }
                        })

                        val nameText = "zhuyier"
                        val Rg = XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.tencent.mm.kernel.g", classLoader), "Rg")
                        XposedHelpers.callMethod(Rg, "a", 106, newObject)

                        val f = XposedHelpers.findClass("com.tencent.mm.plugin.messenger.a.f", classLoader)
                        val constructor = f.getConstructor(String::class.java, Int::class.java)
                        val fVar = constructor.newInstance(nameText, 3)

                        XposedHelpers.callMethod(Rg, "a", fVar, 0)

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


                        XposedBridge.log("AutoSendSnsRequestHook, com.tencent.mm.pluginsdk.model.m, Constructor args = ${args.joinToString { if (it == null) "null" else it.toString() }}")


                    }
                })


//        a(int i, int i2, int i3, java.lang.String str, com.tencent.mm.network.q qVar, byte[] bArr)

        val argClass = XposedHelpers.findClass("com.tencent.mm.network.q", classLoader)
        val aaClass = XposedHelpers.findClass("com.tencent.mm.platformtools.aa", classLoader)

        XposedHelpers.findAndHookMethod(wechatRequestHandlerClass, "a",
                Int::class.java, Int::class.java, Int::class.java,
                String::class.java, argClass, ByteArray::class.java,

                object : XC_MethodHook() {

                    override fun afterHookedMethod(param: MethodHookParam) {

                        val args = param.args

                        XposedBridge.log("AutoSendSnsRequestHook, args = ${args.joinToString { if (it == null) "null" else it.toString() }}")


                        val thisObject = param.thisObject

                        val bOj = XposedHelpers.callMethod(thisObject, "bOj")//com.tencent.mm.protocal.protobuf.buv

                        val wcB = XposedHelpers.getObjectField(bOj, "wcB")

                        /////com.tencent.mm.platformtools.aa.a(buv.wcB)

                        result = XposedHelpers.callStaticMethod(aaClass, "a", wcB) as String

                        XposedBridge.log("AutoSendSnsRequestHook, result = ${result}")


                        //添加好友
//                        val constructor = XposedHelpers.findConstructorExact(dataClass,
//                                Int::class.java, java.util.List::class.java, java.util.List::class.java, java.util.List::class.java,
//                                String::class.java, String::class.java, java.util.Map::class.java,
//                                String::class.java, String::class.java)
//                        constructor.isAccessible = true
//
//                        val m = constructor.newInstance(2, ArrayList<String>().apply { add(result) }, ArrayList<Int>().apply { add(3) }, null,
//                                "测试发送好友请求", "", HashMap<String, Int>().apply { put(result, 0) },
//                                null, ""
//                        )
//
//                        val auDF = XposedHelpers.callStaticMethod(requestHandlerClass, "Rg")
//                        XposedHelpers.callMethod(auDF, "a", m, 0)

                    }
                }
        )


        val fClass = XposedHelpers.findClass("com.tencent.mm.plugin.messenger.a.f", classLoader)

        XposedHelpers.findAndHookConstructor(fClass, String::class.java, Int::class.java, Int::class.java, Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {

                        val args = param.args

                        XposedBridge.log("AutoSendSnsRequestHook, com.tencent.mm.plugin.messenger.a.f, Constructor args = ${args.joinToString { if (it == null) "null" else it.toString() }}")

                    }
                })


        val sayHiWithSnsPermissionUI = XposedHelpers.findClass("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI", classLoader)

        XposedHelpers.findAndHookMethod(sayHiWithSnsPermissionUI, "initView", object : XC_MethodHook() {


            override fun afterHookedMethod(param: MethodHookParam) {
                val thisObject = param.thisObject as Activity

                val intent = thisObject.intent

                var result = ""

                for (s in intent.extras.keySet()) {

                    try {
                        val stringExtra = intent.getStringExtra(s)

                        if (stringExtra == null) {

                            try {
                                val intExtra = intent.getIntExtra(s, -1)
                                if (intExtra == -1) {

                                    val booleanExtra = intent.getBooleanExtra(s, false)

                                    result = result + ", (boolean)$s = $booleanExtra"
                                } else {

                                    result = result + ", (int)$s = $intExtra"
                                }
                            } catch (e: Throwable) {
                                try {
                                    val booleanExtra = intent.getBooleanExtra(s, false)

                                    result = result + ", (boolean)$s = $booleanExtra"
                                } catch (e: Throwable) {

                                    result = result + ", ()$s = unknownType"
                                }
                            }


                        } else {
                            result = result + ", (string)$s = $stringExtra"
                        }
                    } catch (e: Throwable) {

                        try {
                            val intExtra = intent.getIntExtra(s, -1)
                            if (intExtra == -1) {

                                val booleanExtra = intent.getBooleanExtra(s, false)

                                result = result + ", (boolean)$s = $booleanExtra"
                            } else {

                                result = result + ", (int)$s = $intExtra"
                            }
                        } catch (e: Throwable) {
                            try {
                                val booleanExtra = intent.getBooleanExtra(s, false)

                                result = result + ", (boolean)$s = $booleanExtra"
                            } catch (e: Throwable) {

                                result = result + ", ()$s = unknownType"
                            }
                        }
                    }
                }

                XposedBridge.log("AutoSendSnsRequestHook, intent = $result")
            }
        })

    }


}
