package esprims.gi2.ma_pharmacie.data.local.type_converters

import androidx.room.TypeConverter
import java.sql.Timestamp

class TimesConverter {
    @TypeConverter
    fun FromArrayListOfTimeStamp(timestampes:ArrayList<Timestamp>):String
    {
        var timestampesInString=""
        for (timestam in timestampes )
        {
            timestampesInString+= " $timestam"
        }
        return  timestampesInString
    }


}