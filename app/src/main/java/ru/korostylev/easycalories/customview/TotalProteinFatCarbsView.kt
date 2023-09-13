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
    //Пара лимит-факт
    var data: Pair<NutrientsEntity, NutrientsEntity> = Pair(NutrientsEntity(0, 100F, 100F, 100F, 0F), NutrientsEntity(0, 0F, 0F, 0F, 0F))
        set(value) {
            field = value
            //custom view перерисуется при условии видимости
            invalidate()
        }

    //функция для получения данных для 100%
    fun getSummNutrients(): Float {
        var total = 0F
        with(data.first) {
            total = proteins + fats + carbs
        }
        return total
    }
    fun getSummNutrientsFact(): Float {
        var total = 0F
        with(data.second) {
            total = proteins + fats + carbs
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
        //чтобы начинать отрисовку окружности сверху
        var startFrom = -90F
        var angle = 0F
        var angleFact = 0F
        //выясним суммарное значение элементов, для вычисления доли каждого
        val total = getSummNutrients()
        val totalFact = getSummNutrientsFact()
        //переменная для вывода текста процентов
        var textPercent = 0f
        //переменная для цвета точки
        //var dotColor: Int = 0
        angle = 360F * (data.first.proteins/total)
        if (data.first.proteins == 0F) {
            angleFact = angle * data.second.proteins
        } else angleFact = angle * data.second.proteins/data.first.proteins

        //добавляем к переменной процент от сектора
        textPercent += data.first.proteins / total
        paint.style = Paint.Style.FILL
        paintFact.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.protein)
        paintFact.color = ContextCompat.getColor(context, R.color.protein_fill)
        canvas.drawArc(oval, startFrom,angle, true, paint)
        canvas.drawArc(oval, startFrom,angleFact, true, paintFact)
        startFrom += angle

        angle = 360F * (data.first.fats/total)
        if (data.first.fats == 0F) {
            angleFact = angle * data.second.fats
        } else angleFact = angle * data.second.fats/data.first.fats
        println("angle $angleFact")
        textPercent += data.first.fats / total
        paint.style = Paint.Style.FILL
        paintFact.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.fat)
        paintFact.color = ContextCompat.getColor(context, R.color.fat_fill)
        canvas.drawArc(oval, startFrom,angle, true, paint)
        canvas.drawArc(oval, startFrom,angleFact, true, paintFact)
        startFrom += angle

        angle = 360F * (data.first.carbs/total)
        if (data.first.carbs == 0F) {
            angleFact = angle * data.second.carbs
        } else angleFact = angle * data.second.carbs/data.first.carbs
        textPercent += data.first.carbs / total
        paint.style = Paint.Style.FILL
        paintFact.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.carbs)
        paintFact.color = ContextCompat.getColor(context, R.color.carbs_fill)
        canvas.drawArc(oval, startFrom,angle, true, paint)
        canvas.drawArc(oval, startFrom,angleFact, true, paintFact)
        startFrom += angle

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