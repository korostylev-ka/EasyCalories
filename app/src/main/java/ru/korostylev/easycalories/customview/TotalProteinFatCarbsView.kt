package ru.korostylev.easycalories.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import ru.korostylev.easycalories.R
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
    //радиус круга view
    private var radius = 0F
    //точка центра
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)
    //AndroidUtils.dp для перевода dp в px
    private var lineWidth = AndroidUtils.dp(context, 5F).toFloat()
    private var fontSize = AndroidUtils.dp(context, 40F).toFloat()
    private var colors = emptyList<Int>()


    init {
        /*context.withStyledAttributes(attrs, R.styleable.StatsView) {
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            fontSize = getDimension(R.styleable.StatsView_fontSize, fontSize)
            val resId = getResourceId(R.styleable.StatsView_colors, 0)
            colors = resources.getIntArray(resId).toList()
        }*/
    }
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
    //создаем данные в формате списка пар: потребленные нутриенты - изначально рассчитанные
    var data: List<Pair<Float, Float>> = listOf(Pair(150F, 100F), Pair(150F, 100F), Pair(200F, 100F))
        set(value) {
            field = value
            //custom view перерисуется при условии видимости
            invalidate()
        }

    //функция для получения данных для 100% загрузки из 1-х элементов Pair
    fun getSummNutrients(data: List<Pair<Float, Float>>): Float {
        var total = 0f
        data.map {
            total += it.first
        }
        return total
    }
    fun getSummNutrientsFact(data: List<Pair<Float, Float>>): Float {
        var total = 0f
        data.map {
            total += it.second
        }
        return total
    }

    //координаты точки x центра сектора
    fun getPointXOfSector(angle: Float): Float {
        return center.x + sin(((angle) * PI.toFloat() / 180)) * radius/2
    }

    ////координаты точки y центра сектора
    fun getPointYOfSector(angle: Float): Float {
        return center.y +- cos(((angle) * PI.toFloat() / 180)) * radius/2
    }

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

        /*if (data.isEmpty()) {
            return
        }*/
        //чтобы начинать отрисовку окружности сверху
        var startFrom = -90F
        //выясним суммарное значение элементов, для вычисления доли каждого
        val total = getSummNutrients(data)
        val totalFact = getSummNutrientsFact(data)
        //переменная для вывода текста процентов
        var textPercent = 0f
        //переменная для цвета точки
        //var dotColor: Int = 0

        for ((index, value) in data.withIndex()) {
            val angle = 360F * (value.first/total)
            val angleFact = angle * value.second/value.first
            //добавляем к переменной процент от сектора
            textPercent += value.first / total
            paint.style = Paint.Style.FILL
            paintFact.style = Paint.Style.FILL
            //paint.color = ContextCompat.getColor(context, R.color.protein)

            //установка цвета сектора в зависимости от нутриента(теория)
            paint.color = when (index) {
                0 -> ContextCompat.getColor(context, R.color.protein)
                1 -> ContextCompat.getColor(context, R.color.fat)
                else -> ContextCompat.getColor(context, R.color.carbs)
            }
            //установка цвета сектора в зависимости от нутриента(фактические данные)
            paintFact.color = when (index) {
                0 -> ContextCompat.getColor(context, R.color.protein_fill)
                1 -> ContextCompat.getColor(context, R.color.fat_fill)
                else -> ContextCompat.getColor(context, R.color.carbs_fill)
            }
            canvas.drawArc(oval, startFrom,angle, true, paint)
            canvas.drawArc(oval, startFrom,angleFact, true, paintFact)
            startFrom += angle
            //dotColor = colors.get(0)

        }



        //paint.color = randomColor()
        //paint.style = Paint.Style.FILL
        //canvas.drawArc(oval, startFrom,120F, true, paint)

        //paint.color = randomColor()
        //canvas.drawArc(oval, startFrom + 120,180F, true, paint)

        //paint.color = randomColor()
        //canvas.drawArc(oval, startFrom + 300,60F, true, paint)
        //paint.color = randomColor()
        //canvas.drawPoint(center.x + sin(((60) * PI.toFloat() / 180)) * radius/2, center.y +- cos(((60F ) * PI.toFloat() / 180)) * radius/2, paint)

        /*canvas.drawText(
            "%.2f%%".format(textPercent *100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint,
        )*/
        //присваиваем цвет певого сектора для точки
        //paint.color = dotColor
        //рисуем точку для корректировки верхней точки окружности
        //canvas.drawPoint(center.x,center.y - radius,paint)

    }
    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}