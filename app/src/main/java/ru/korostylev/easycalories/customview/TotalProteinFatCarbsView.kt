package ru.korostylev.easycalories.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class TotalProteinFatCarbsView @JvmOverloads constructor(
    context: Context,
    //набор аттрибутов, которые можно передать через xml
    attrs: AttributeSet? = null,
    //стиль аттрибутов по умолчанию
    defStyleAttr: Int = 0,
    //стиль по умолчанию
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var emptyValue = NutrientsEntity(0, 9F, 4F, 9F, 108F)
    //радиус круга view
    private var radius = 0F
    //точка центра
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)
    //AndroidUtils.dp для перевода dp в px
    private var lineWidth = AndroidUtils.dp(context, 5F).toFloat()
    private var fontSize = AndroidUtils.dp(context, 40F).toFloat()
    private var colors = emptyList<Int>()

    //кисть для линии
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //стиль отрисовки
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        //скругление краев
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND

    }
    //кисть для линии
    private val paintFact = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //стиль отрисовки
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        //скругление краев
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND

    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //заливка
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }
    //Пара лимит-факт
    var data = emptyValue
        set(value) {
            with (value) {
                if (this.proteins == 0F && this.fats == 0F && this.carbs == 0F) {
                    field = emptyValue.copy(id=value.id)
                } else {
                    field = value
                }
            }

            //custom view перерисуется при условии видимости
            invalidate()
        }

    //функция для получения данных для 100%
    fun getSummNutrients(): Float {
        var total = 0F
        with(data) {
            total = proteins + fats + carbs
        }
        return total
    }

    //координаты точки x центра сектора
    fun getPointXOfSector(angle: Float): Float {
        return center.x + sin(((angle) * PI.toFloat() / 180)) * radius
    }

//    fun getPointXOfSector(angle: Float): Float {
//        return  -radius * sin((angle) * 180 / PI.toFloat())
//    }

    //координаты точки y центра сектора
    fun getPointYOfSector(angle: Float): Float {
        return center.y - cos(((angle) * PI.toFloat() / 180)) * radius
    }

//    fun getPointYOfSector(angle: Float): Float {
//        return -radius * cos((angle) * 180 / PI.toFloat())
//    }

    //переопределяем метод изменения размеров
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //делим на 2, чтобы получить радиус ,а не диаметр. Далее делаем отступ
        radius = (min(w, h) / 2F - lineWidth / 2) / 1.5F
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            //рассчитываем грани
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas) {
        //чтобы начинать отрисовку окружности сверху
        var startFrom = -90F
        var angle = 0F
        val total = getSummNutrients()
        //переменная для вывода текста процентов
        var textPercent = 0f
        //переменная для цвета точки
        //var dotColor: Int = 0
        angle = 360F * (data.proteins * 4 / data.calories)
        //добавляем к переменной процент от сектора
        //textPercent += data.proteins / total
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.protein_fill)
        canvas.drawArc(oval, startFrom,angle, true, paint)
        startFrom += angle
        angle = 360F * (data.fats * 9 /data.calories)
        //textPercent += data.fats / total
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.fat_fill)
        canvas.drawArc(oval, startFrom,angle, true, paint)
        startFrom += angle
        angle = 360F * (data.carbs * 4 / data.calories)
        //textPercent += data.carbs / total
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.carbs_fill)
        canvas.drawArc(oval, startFrom,angle, true, paint)
//        canvas.drawText(
//            "%.2f%%".format(textPercent *100),
//            getPointXOfSector(angle),
//            getPointYOfSector(angle),
//            textPaint,
//        )
        startFrom += angle
    }
    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())

}