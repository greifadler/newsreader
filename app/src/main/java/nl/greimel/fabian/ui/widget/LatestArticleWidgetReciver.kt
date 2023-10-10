package nl.greimel.fabian.ui.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class LatestArticleWidgetReciver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LatestArticleWidget()
}
