package at.fhooe.tellustrations

import android.content.Context

class sharedPreferences(context: Context) {
    val preferencePlayerAmount_Name = "PlayerAmountName"
    val preferencePlayerAmount = "PlayerAmount"

    val preferenceA = context.getSharedPreferences(preferencePlayerAmount_Name, Context.MODE_PRIVATE)

    fun getPlayerAmount() : Int{
        return preferenceA.getInt(preferencePlayerAmount, 0)
    }
    fun setPlayerAmount(count: Int){
        val editor = preferenceA.edit()
        editor.putInt(preferencePlayerAmount, count)
        editor.apply()
    }


    val preferencePlayerNr_Name = "PlayerNr"
    val preferencePlayerNr = "PlayerNr"

    val preferenceNr = context.getSharedPreferences(preferencePlayerNr_Name, Context.MODE_PRIVATE)

    fun getPlayerNr() : Int{
        return preferenceNr.getInt(preferencePlayerNr, 2)
    }
    fun setPlayerNr(count: Int){
        val editor = preferenceNr.edit()
        editor.putInt(preferencePlayerNr, count)
        editor.apply()
    }
    fun resetPlayerNr(){
        val editor = preferenceNr.edit()
        editor.clear().apply()
    }


    val preferenceBufferStr_Name = "String_Name"
    val preferenceBufferStr = "String_Buffer"

    val preferenceStr = context.getSharedPreferences(preferenceBufferStr_Name, Context.MODE_PRIVATE)

    fun getStrBuffer() : String{
        return preferenceStr.getString(preferenceBufferStr, "Error 404: No Sentence Found. Sorry")!!
    }
    fun setStrBuffer(msg: String){
        val editor = preferenceStr.edit();
        editor.putString(preferenceBufferStr, msg)
        editor.apply()
    }
}