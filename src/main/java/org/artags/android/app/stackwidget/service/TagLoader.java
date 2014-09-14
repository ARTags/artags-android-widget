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

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;
import org.artags.android.app.stackwidget.model.Tag;

/**
 *
 * @author pierre
 */
public class TagLoader extends AsyncTaskLoader<List<Tag>>
{
    String mUrl;
    
    public TagLoader(Context context , String url )
    {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Tag> loadInBackground()
    {
        return TagService.getTagList( mUrl );
    }
    
    
}
