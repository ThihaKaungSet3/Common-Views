package non.shahad.textview.exceptions

internal class FontNotFoundException: Throwable(){
    override val message: String?
        get() = "Font not found,Please specify all font"
}