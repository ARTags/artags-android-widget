/* Copyright (c) 2010-2011 ARTags Project owners (see http://www.artags.org)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.artags.android.app.stackwidget.service;

import org.artags.android.app.stackwidget.util.HttpUtils;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import org.artags.android.app.stackwidget.ui.widget.AbstractStackWidgetProvider;
import org.artags.android.app.stackwidget.Constants;
import org.artags.android.app.stackwidget.R;
import org.artags.android.app.stackwidget.model.Tag;
import org.artags.android.app.stackwidget.service.TagService;

public class StackWidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context mContext;
    private int mAppWidgetId;
    private List<Tag> mTagList;
    private String mUrl;

    public StackRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mUrl = intent.getStringExtra( AbstractStackWidgetProvider.EXTRA_URL );
    }

    public void onCreate()
    {
    }

    public void onDestroy()
    {
    }

    public int getCount()
    {
        return Constants.MAX_TAGS;
    }

    public RemoteViews getViewAt(int position)
    {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if( mTagList == null )
        {
            mTagList = TagService.getTagList( mUrl );
            for (Tag tag : mTagList)
            {
                tag.setBitmap(HttpUtils.loadBitmap(tag.getThumbnailUrl()));
            }
        }

        rv.setTextViewText(R.id.text, mTagList.get(position).getText());
        rv.setImageViewBitmap(R.id.thumbnail, mTagList.get(position).getBitmap());

        Bundle extras = new Bundle();
        extras.putInt(AbstractStackWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.thumbnail, fillInIntent);


        // Return the remote views object.
        return rv;
    }

    public RemoteViews getLoadingView()
    {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount()
    {
        return 1;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public boolean hasStableIds()
    {
        return true;
    }

    public void onDataSetChanged()
    {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }

}