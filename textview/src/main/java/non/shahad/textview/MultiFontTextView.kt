package non.shahad.textview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import non.shahad.textview.exceptions.FontNotFoundException

class MultiFontTextView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleDef: Int = 0
): AppCompatTextView(context,attrs,styleDef) {

    companion object {
        private var mTypeFaces: HashMap<String, Typeface>? = null
        private const val MM = "mm"
        private const val EN = "en"
        // Next version
        private const val JA = "ja"
    }

    init {
        if(mTypeFaces == null) mTypeFaces = HashMap()

        val a = context.obtainStyledAttributes(attrs,R.styleable.MultiFontTextView)

        val fontForEnglish = a.getString(R.styleable.MultiFontTextView_fontForEnglish)
        val fontForBurmese = a.getString(R.styleable.MultiFontTextView_fontForBurmese)


        if (fontForBurmese == null || fontForEnglish == null){
            throw FontNotFoundException()
        }

        when(currentLocale()){
            MM -> setFontFamily(fontForBurmese)
            EN -> setFontFamily(fontForEnglish)
        }

        a.recycle()
    }

    private fun setFontFamily(fontRes: String){
        var lTypeFace: Typeface? = null

        if (mTypeFaces?.containsKey(fontRes)!!){
            lTypeFace = mTypeFaces?.get(fontRes)
        }else{
            val assets = context.assets
            lTypeFace = Typeface.createFromAsset(assets,fontRes)
            mTypeFaces!![fontRes] = lTypeFace!!
        }

        typeface = lTypeFace

    }

    private fun currentLocale() = context.resources.configuration.locale.toString()
}