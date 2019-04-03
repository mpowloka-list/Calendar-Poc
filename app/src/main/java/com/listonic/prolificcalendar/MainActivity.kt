package com.listonic.prolificcalendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView.state().edit()
            .setMinimumDate(LocalDate.of(2019, 4, 1))
            .setMaximumDate(LocalDate.of(2019, 4, 7))
            .commit()

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

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
