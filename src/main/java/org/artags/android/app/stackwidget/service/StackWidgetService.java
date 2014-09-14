/* Copyright (c) 2010-2014 ARTags Project owners (see http://www.artags.org)
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

import android.app.LoaderManager;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;
import org.artags.android.app.stackwidget.ui.widget.AbstractStackWidgetProvider;
import org.artags.android.app.stackwidget.Constants;
import org.artags.android.app.stackwidget.R;
import org.artags.android.app.stackwidget.model.Tag;

public class StackWidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, LoaderManager.LoaderCallbacks<List<Tag>>
{

    private Context mContext;
    private int mAppWidgetId;
    private List<Tag> mTagList;
    private String mUrl;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayOptions;

    public StackRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mUrl = intent.getStringExtra(AbstractStackWidgetProvider.EXTRA_URL);

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .build();

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
        mDisplayOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
    }

    @Override
    public void onCreate()
    {
    }

    @Override
    public void onDestroy()
    {
    }

    @Override
    public int getCount()
    {
        return (mTagList != null) ? mTagList.size() : Constants.MAX_TAGS;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        Log.d(Constants.LOG_TAG, "RemoteView asked for position : " + position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

//        if( mTagList == null )
//        {
//            mTagList = TagService.getTagList( mUrl );
//            for (Tag tag : mTagList)
//            {
//                tag.setBitmap( mImageLoader.loadImageSync(tag.getThumbnailUrl(), mDisplayOptions ));
//            }
//        }
        if (mTagList != null)
        {
            rv.setTextViewText(R.id.text, mTagList.get(position).getText());
            rv.setImageViewBitmap(R.id.thumbnail, mTagList.get(position).getBitmap());
        }
        Bundle extras = new Bundle();
        extras.putInt(AbstractStackWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.thumbnail, fillInIntent);

        // Return the remote views object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public void onDataSetChanged()
    {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
        mTagList = TagService.getTagList(mUrl);
        for (Tag tag : mTagList)
        {
            tag.setBitmap(mImageLoader.loadImageSync(tag.getThumbnailUrl(), mDisplayOptions));
        }
    }

    @Override
    public Loader<List<Tag>> onCreateLoader(int id, Bundle args)
    {
        Log.i(Constants.LOG_TAG, "Tag Loader created ");
        return new TagLoader(mContext, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Tag>> loader, List<Tag> data)
    {
        mTagList = data;
//        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged
        Log.i(Constants.LOG_TAG, "Tag Loader finished ");

    }

    @Override
    public void onLoaderReset(Loader<List<Tag>> loader)
    {
    }

}
