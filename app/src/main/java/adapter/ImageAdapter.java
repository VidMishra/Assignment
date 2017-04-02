package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.thb.vidyanand.assignmentthb.R;

import java.util.List;

import Util.Utility;

/**
 * Created by vidyanand on 15/8/15.
 */
public class ImageAdapter extends BaseAdapter {

	private List<String> mListImageUri;
	private Context mContext;

	/**
	 * Initialize objects
	 * @param listImageUri
	 */
	public ImageAdapter(Context context, List<String> listImageUri) {
		this.mListImageUri = listImageUri;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if(mListImageUri == null){
			return 0;
		}
		return mListImageUri.size();
	}

	@Override
	public Object getItem(int position) {
		return mListImageUri.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gird_view_layout, viewGroup, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.ivGalleryImage = (ImageView) convertView.findViewById(R.id.iv_grid);
			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.ivGalleryImage.setImageBitmap(Utility.getBitmap(mListImageUri.get(position), mContext));
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivGalleryImage;
	}
}
