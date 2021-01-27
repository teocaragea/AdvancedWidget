package com.example.advancedwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       final  int nrofWidgets=appWidgetIds.length;
       for(int i=0;i<nrofWidgets;i++)
       {
           int appWidgetId=appWidgetIds[i];
           RemoteViews view=new RemoteViews(context.getPackageName(),R.layout.new_app_widget);//conn to widget update

           Intent intent=new Intent(context,NewsService.class);
           intent.putExtra("appWidgetId",appWidgetId);
           context.startService(intent);
           appWidgetManager.updateAppWidget(appWidgetId,view);
       }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

