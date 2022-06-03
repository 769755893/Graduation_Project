package com.example.uitraning.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogMyToastBinding
import com.app.project.hotel.common.mwindow
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.RandomAccessFile
import java.lang.reflect.Field
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.max

fun Any?.log(e: Any ? = "") {
    var add = ""
    for (i in 0..99) add += "-"
    Utils.logd("$this----$e$add")
}

fun showToast(context: Context, text: String, window: Window, state: Boolean = true) {
    val t = Toast(context)
    val viewbinding = DataBindingUtil.inflate<DialogMyToastBinding>(
        LayoutInflater.from(context),
        R.layout.dialog_my_toast,
        window.decorView as ViewGroup,
        false
    )
    if (!state) {
        viewbinding.ivToastIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_baseline_error_24,
                context.resources.newTheme()
            )
        )
    }
    viewbinding.tvContent.text = text
    val lp = FrameLayout.LayoutParams(viewbinding.root.layoutParams)
    lp.gravity = Gravity.TOP
    t.view = viewbinding.root
    t.duration = Toast.LENGTH_SHORT
    t.show()
}


object Utils {

    fun getNowFormatDate(pattern: String): String {
        val timeZone = TimeZone.getTimeZone("Etc/GMT-8")
        TimeZone.setDefault(timeZone)
        val nowDate = Date()
        val format = SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE)
        return format.format(nowDate)
    }

    fun CalDateDiff(date1: Date, date2: Date): Int {
        val cal1 = Calendar.getInstance();
        cal1.time = date1;

        val cal2 = Calendar.getInstance();
        cal2.time = date2;
        val day1 = cal1.get(Calendar.DAY_OF_YEAR);
        val day2 = cal2.get(Calendar.DAY_OF_YEAR);

        val year1 = cal1.get(Calendar.YEAR);
        val year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            var timeDistance = 0;
            for (i in year1 until year2) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else {
            return day2 - day1;
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun calDateDiffDayLine(st: String, et: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val stt = sdf.parse(st)!!
        val ett = sdf.parse(et)!!
        val daycount =  CalDateDiff(stt,ett)
        daycount.log("日期相差")
        return daycount
    }

    fun isNumber(value: String): Boolean {
        var ans = true
        value.forEach {
            if (it < '0' || it > '9') {
                ans = false
            }
        }
        return ans
    }

    fun <F : Fragment> getFragment(supportFragmentManager: FragmentManager ,fragmentClass: Class<F>): F? {
        val navHostFragment = supportFragmentManager.fragments.first() as NavHostFragment

        navHostFragment.childFragmentManager.fragments.forEach {
            if (fragmentClass.isAssignableFrom(it.javaClass)) {
                return it as F
            }
        }
        return null
    }

    fun changedViewPagerSensitive(vp: ViewPager2) {
        try {
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(vp) as RecyclerView
            val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * 3)
        } catch (ignore: java.lang.Exception) {

        }
    }

    fun isAllNumber(str: String): Boolean {
        var ans = true
        for (i in str) {
            if (i < '0' || i > '9') {1
                ans = false
                break
            }
        }
        if (str.isEmpty()) ans = false
        return ans
    }

    @SuppressLint("SimpleDateFormat")
    fun checkTime(startTime: String, endTime: String): Boolean {
        val sdf = SimpleDateFormat("yyyy年MM月dd日")
        val st = sdf.parse(startTime)!!
        val et = sdf.parse(endTime)!!
        val daycount = CalDateDiff(st,et)
        return daycount > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowTime(): Triple<Int, Int, Int> {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val localDate = LocalDate.now()
        val now = dtf.format(localDate).split('/')
        now.log()
        val year = now[0].toInt()
        val moth = now[1].toInt()
        val day = now[2].toInt()
        return Triple(year, moth, day)
    }

    @SuppressLint("Range")
    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
    }

    fun checkPermission(context: Context, activity: Activity) {
        val Permissions = listOf(
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        val permissionList = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //>23
            for (permission in Permissions) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionList.add(permission)
                }
            }
        }

        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), 100)
        }
    }

    fun createNewFile(fileName: String): File {
        val file = File(fileName)

        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return file
    }

    fun readLineFromFile(filePath: String): List<String> {
        val file = File(filePath)
        return file.readLines()
    }

    fun readTextFromFile(filePath: String): String {
        val file = File(filePath)
        return file.readText()
    }

    fun writeTextToFile(file: File, text: String, isNeedChangeLine: Boolean) {
        var strContent = text
        if (isNeedChangeLine) {
            strContent += "\r\n"
        }
        var randomAccessFile = RandomAccessFile(file, "rwd")
        randomAccessFile.seek(file.length())
        randomAccessFile.write(strContent.toByteArray())
        randomAccessFile.close()
    }

    fun bitmap2String(bitmap: Bitmap?): String {
        var str = ""
        val byteStream = ByteArrayOutputStream()
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        }
        val bytes = byteStream.toByteArray()
        str = Base64.encodeToString(bytes, Base64.DEFAULT)
        return str
    }

    fun string2Bitmap(str: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val byteArray = Base64.decode(str, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun setPasswordVisibility(editText: EditText?, setVisible: Boolean) {
        val InputType_PSW_Hide =
            EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD

        val InputType_PSW_Visible =
            EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        if (null != editText) {
            val selectionEnd = editText.selectionEnd
            val length = editText.text.toString().length
            val inputType = editText.inputType
            if (inputType == InputType_PSW_Hide || inputType == InputType_PSW_Visible) {
                editText.inputType = if (setVisible) InputType_PSW_Visible else InputType_PSW_Hide
            } else {
                editText.transformationMethod =
                    if (setVisible) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            }
            editText.setSelection(Math.min(selectionEnd, length))
        }
    }

    fun fullScreen(window: Window) {
        val decorView: View = window.decorView
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    suspend fun toast(context: Context, str: String, state: Boolean) {
        withContext(Dispatchers.Main) {
            showToast(context, str, mwindow, state)
        }
    }

    fun logd(str: String) {
        Log.d("msg-------------------", "$str~")
    }

    fun drawReoundRect(canvas: Canvas, radius: Int, w: Int, h: Int, paint: Paint) {
        val rectF = RectF(0F, 0F, w.toFloat(), h.toFloat())
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)
    }

    fun getSize(spec: Int): Int {
        when (View.MeasureSpec.getMode(spec)) {
            View.MeasureSpec.EXACTLY -> {
                return View.MeasureSpec.getSize(spec)
            }
            View.MeasureSpec.AT_MOST -> {
                return max(300, View.MeasureSpec.getSize(spec))
            }
            else -> {}
        }
        return 300
    }

    fun drawRect(canvas: Canvas, paint: Paint, w: Int, h: Int) {
        canvas.drawRect(0F, 0F, w.toFloat(), h.toFloat(), paint)
    }

    fun isViewBeTouched(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            v.getLocationInWindow(leftTop)

            val left = leftTop[0]
            val top = leftTop[1]

            val bottom = top + v.getHeight()
            val right = left + v.getWidth()

            return event.x > left && event.x < right && event.y > top && event.y < bottom
        }
        return false
    }

    fun bitmapToDrawable(resID: Int, view: View, xPx: Float, yPx: Float): Drawable {
        val bitmap = Utils.resizeBitmap(
            BitmapFactory.decodeResource(view.resources, resID),
            Utils.dpToPixels(Utils.pxToDp(100F, view.resources).toInt(), view.context).toInt(),
            Utils.dpToPixels(Utils.pxToDp(60F, view.resources).toInt(), view.context).toInt()
        )
        return BitmapDrawable(bitmap)
    }


    fun closeKeyboard(view: View) {
        val imm: InputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun drawBaseLine(canvas: Canvas?, paint: Paint, w: Int, h: Int) {
        drawXbaseLine(canvas, paint, w.toFloat(), h.toFloat())
        drawYbaseLine(canvas, paint, w.toFloat(), h.toFloat())
    }

    fun drawXbaseLine(canvas: Canvas?, paint: Paint, w: Float, h: Float) {
        canvas!!.drawLine(0F, h / 2, w, h / 2, paint)
    }

    fun drawYbaseLine(canvas: Canvas?, paint: Paint, w: Float, h: Float) {
        canvas!!.drawLine(w / 2, 0F, w / 2, h, paint)
    }

    fun dpToPixels(value: Int, context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics
    )

    fun pxToPixels(value: Int, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            value.toFloat(),
            context.resources.displayMetrics
        )
    }

    fun PixelsTodp(value: Int, context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, value.toFloat(), context.resources.displayMetrics
    )


    fun transViewToBitmap(view: View): Drawable {
        view.measure(view.measuredWidth, view.measuredHeight)
        val w = view.measuredWidth
        val h = 10
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)
        view.layout(0, 0, w, h)
        view.draw(canvas)
        return BitmapDrawable(bmp)
    }

    fun bitmapScaleDrawable(
        resources: Resources,
        X: Float,
        Y: Float,
        bitmap: Bitmap,
    ): Drawable {
        val matrix = Matrix()
        val width = bitmap.width
        val height = bitmap.height
        matrix.postScale(X, Y)
        val reBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        return reBitmap.toDrawable(resources)
    }

    fun bitmapMarixScale(
        resources: Resources,
        res: Int,
        X: Float,
        Y: Float,
        context: Context
    ): Bitmap {
        val matrix = Matrix()
        var width = 0
        var height = 0
        val t = BitmapFactory.decodeResource(resources, res).also {
            width = it.width
            height = it.height
            matrix.postScale(X, Y)
        }
        val reBitmap = Bitmap.createBitmap(t, 0, 0, width, height, matrix, true)

        return reBitmap
    }

    fun bitmapResizeFloat(bitmap: Bitmap, x: Float, y: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(x, y)

        var size = bitmap.byteCount
        var newbitmap = bitmap
        var width = newbitmap.width
        var height = newbitmap.height
        while (size > 200000) { //70kb以下
            newbitmap = Bitmap.createBitmap(newbitmap, 0, 0, width, height, matrix, true)
            size = newbitmap.byteCount
            width = newbitmap.width
            height = newbitmap.height
        }
        return newbitmap
    }


    fun dpToPx(dp: Float, resources: Resources): Float {
        val density = resources.displayMetrics.density
        return density * dp + 0.5f
    }

    fun pxToDp(px: Float, resources: Resources): Float {
        val density = resources.displayMetrics.density
        return (px - 0.5f) / density
    }


    fun dip2px(mContext: Context, dpValue: Float): Int {
        val scale = mContext.resources
            .displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(mContext: Context, pxValue: Float): Int {
        val scale = mContext.resources
            .displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    private fun getScreenSize(mContext: Context): IntArray {
        val dm = mContext
            .resources.displayMetrics
        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels
        return intArrayOf(screenWidth, screenHeight)
    }

    fun getStatusBarHeight(mContext: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = field[obj].toString().toInt()
            statusBarHeight = mContext.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    fun getScreenWidth(mContext: Context): Int {
        val screen = getScreenSize(mContext)
        return screen[0]
    }

    fun getScreenHeight(mContext: Context): Int {
        val screen = getScreenSize(mContext)
        return screen[1]
    }

    fun resizeBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap {

        val width = bitmap.width

        val height = bitmap.height

        val newWidth = w

        val newHeight = h

        val scaleWight = (newWidth) / width.toFloat()

        val scaleHeight = (newHeight) / height.toFloat()

        val matrix = Matrix()

        matrix.postScale(scaleWight, scaleHeight)

        val res = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)

        return res

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowTimeString(): String {
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val t = LocalDateTime.now()
        val nowTime = dtf.format(t)
        val Time = nowTime.filter { it in '0'..'9' }
        return Time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowTimeStringymd(): String {
        val parttern = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
        val time = LocalDateTime.now()
        return time.format(parttern)
    }
}