package com.listonic.prolificcalendar

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var initialWeeklyCalendarPosition: Pair<Float, Float>? = null

    var initialDaysOfWeekPosition: Pair<Float, Float>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        setupCalendarsCommonTraits(calendarViewMonth)
        setupCalendarsCommonTraits(calendarViewWeek)

        calendarViewMonth.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return day.isAfter(CalendarDay.from(2019, 3, 31))
                        && day.isBefore(CalendarDay.from(2019, 4, 8))
            }

            override fun decorate(view: DayViewFacade) {
                view.addSpan(ForegroundColorSpan(Color.parseColor("#ffffff")))
            }

        })

        calendarViewMonth.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                calendarViewMonth.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val correspondingMonthlyCalendarTile = calendarViewMonth.findDayViewWithText("1")!!
                if(initialWeeklyCalendarPosition == null) {
                    initialWeeklyCalendarPosition = Pair(
                        correspondingMonthlyCalendarTile.x + calendarViewMonth.marginStart,
                        correspondingMonthlyCalendarTile.y + calendarViewMonth.marginTop
                    )
                }

                if(initialDaysOfWeekPosition == null) {
                    initialDaysOfWeekPosition = Pair(
                        days_of_week.x,
                        days_of_week.y
                    )
                }
            }

        })

    }

    private fun setupCalendarsCommonTraits(calendarView: MaterialCalendarView) {

        calendarView.state().edit().setShowWeekDays(false).commit()
        calendarView.topbarVisible = false
        calendarView.setWeekDayFormatter { dayOfWeek ->
            dayOfWeek.name.first().toString()
        }

        calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return true
            }

            override fun decorate(view: DayViewFacade) {

                view.setSelectionDrawable(
                    AppCompatResources.getDrawable(applicationContext, R.drawable.calendar_selector) ?: return
                )
            }

        })
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->

            val correspondingMonthlyCalendarTile = calendarViewMonth.findDayViewWithText("1")!!
            setupMonthlyCalendarAlpha(offset)

            val offsetPrecentage = if (offset == 0) {
                1.toFloat()
            } else {
                (app_bar_layout.totalScrollRange.toFloat() + offset.toFloat()) / app_bar_layout.totalScrollRange.toFloat()
            }

            val initialDaysY = initialWeeklyCalendarPosition?.second ?: return@OnOffsetChangedListener
            val targetDaysY = card.height - correspondingMonthlyCalendarTile.height

            val initialDaysOfWeekY = initialDaysOfWeekPosition?.second ?: return@OnOffsetChangedListener
            val targetDaysOfWeekY = card.height - correspondingMonthlyCalendarTile.height

            calendarViewWeek.y =  (initialDaysY * offsetPrecentage) + (1 - offsetPrecentage) * targetDaysY
            days_of_week.y = initialDaysOfWeekY - offset

            println("OFFSET: $offsetPrecentage of ${app_bar_layout.totalScrollRange}")
        })
    }

    private fun setupMonthlyCalendarAlpha(offset: Int) {
        val alpha = if (offset == 0) {
            1.toFloat()
        } else {
            (app_bar_layout.totalScrollRange.toFloat() + offset.toFloat()) / app_bar_layout.totalScrollRange.toFloat()
        }
        calendarViewMonth.alpha = alpha
    }

    private fun MaterialCalendarView?.findDayViewWithText(text: String): AppCompatCheckedTextView? {
        val pager = this?.getChildAt(1) as? ViewPager ?: return null
        val monthView = pager.getChildAt(0) as? ViewGroup ?: return null
        for (i in (0 until monthView.childCount)) {
            val view = monthView.getChildAt(i)
            if (view is AppCompatCheckedTextView && view.text.toString() == text) {
                return view
            }
        }
        return null
    }
}
