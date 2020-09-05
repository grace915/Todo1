package com.example.todo1;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class TodoWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object

        //위젯 업데이ㅡ를 의뢰할 때  의뢰내용을 저장하는 클래스스
       RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_widget);
       //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /*
    * 위젯이 설치될 때마다 호출되는 함수
    * @param context
    * @param appWidgetManager
    * @param appWidgetIds
    * */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        //RemoeViesService 실행 등록시키는 함수
        Intent serviceIntent = new Intent(context, MyRemoteViewsService.class);
        RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.todo_widget);
        widget.setRemoteAdapter(R.id.widget_listview, serviceIntent);

        //클릭이벤트 인텐트 유보
        //보내기
        appWidgetManager.updateAppWidget(appWidgetIds, widget);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /*
    * 앱 위젯이 최초로 설치되는 순간 호출되는 함수
    * @param context
    * */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    /*
    * 위젯이 제거되는 순간 호출되는 함수
    * @param context
    * */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /*
    * 위젯이 마지막으로 제거되는 순간 호출되는 함수
    * @param context
    * @parsam appWidgetIds
    * */

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}

