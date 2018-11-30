package code.name.monkey.retromusic.interfaces

import com.afollestad.materialcab.MaterialCab


interface CabHolder {

    fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab
}
