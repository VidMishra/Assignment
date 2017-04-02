package com.thb.vidyanand.assignmentthb;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Util.Utility;
import adapter.ImageAdapter;
import database.DataSource;
import models.ImageUriModel;

/**
 * Created by vidyanand on 15/8/15.
 */
public class ImageUploadActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

	private static final String CLASS_TAG = ImageUploadActivity.class.getSimpleName();
	private static final int CAMERA_REQUEST_CODE = 107;
	private static final int GALLERY_REQUEST_CODE = 101;

	private ImageButton mIbAddPhoto;
	private Button mBtnAddMorePhoto;
	private GridView mGvImageGallery;
	private List<String> mListUriPath;
	private Uri mUri;
	private String mUriPath;
	private DataSource mDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_upload);
		initViews();
	}

	/**
	 * Initializing views
	 */
	private void initViews() {
		mListUriPath = new ArrayList<>();
		mDataSource = new DataSource(this);
		mIbAddPhoto = (ImageButton) findViewById(R.id.ib_add_photo);
		mIbAddPhoto.setOnClickListener(this);
		mBtnAddMorePhoto = (Button) findViewById(R.id.btn_add_more_photo);
		mBtnAddMorePhoto.setOnClickListener(this);
		mGvImageGallery = (GridView) findViewById(R.id.gv_image_gallery);
		mGvImageGallery.setOnItemClickListener(this);

		mDataSource.open();
		List<ImageUriModel> listImageUri = mDataSource.getImageUriFromLocalDB();
		mDataSource.close();

		if(listImageUri != null && !listImageUri.isEmpty()) {
			for(int i=0; i<listImageUri.size(); i++) {
				mListUriPath.add(listImageUri.get(i).getImageUri());
			}

			ImageAdapter adapter = new ImageAdapter(this, mListUriPath);
			mIbAddPhoto.setVisibility(View.GONE);
			mGvImageGallery.setVisibility(View.VISIBLE);
			mGvImageGallery.setAdapter(adapter);
			mBtnAddMorePhoto.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case CAMERA_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
						Bitmap bitmapImg = Utility.getBitmap(mUriPath, getApplicationContext());
						previewCameraImage(bitmapImg, false);
				}
				break;

			case GALLERY_REQUEST_CODE:

				if (resultCode == RESULT_OK && null != data) {
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					mUriPath = cursor.getString(columnIndex);
					cursor.close();
					Bitmap bitmapImg = Utility.getBitmap(mUriPath, getApplicationContext());
					previewCameraImage(bitmapImg, false);
				} else {
					Toast.makeText(this, "You did not picked any image!", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}

	/**
	 * Preview camera image
	 * @param bitmap
	 */
	private void previewCameraImage(Bitmap bitmap, boolean isOnItemClick) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		dialog.setContentView(R.layout.layout_preview_image);

		Button btnSave = (Button) dialog.findViewById(R.id.btn_save);
		Button btnRetake = (Button) dialog.findViewById(R.id.btn_retake);
		TextView tvOkay = (TextView) dialog.findViewById(R.id.tv_okay);

		if(isOnItemClick) {
			tvOkay.setVisibility(View.VISIBLE);
			btnSave.setVisibility(View.GONE);
			btnRetake.setVisibility(View.GONE);
		} else {
			tvOkay.setVisibility(View.GONE);
			btnSave.setVisibility(View.VISIBLE);
			btnRetake.setVisibility(View.VISIBLE);
		}

		ImageView ivPreviewImg = (ImageView) dialog.findViewById(R.id.iv_preview);
		ivPreviewImg.setImageBitmap(bitmap);

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mListUriPath.add(mUriPath);
				setImageOnGridView();

				ImageUriModel imageUriModel = new ImageUriModel();
				imageUriModel.setImageUri(mUriPath);
				mDataSource.open();
				mDataSource.insertImageUriToLocalDB(imageUriModel);
				mDataSource.close();
			}
		});

		btnRetake.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureImage();
				dialog.dismiss();
			}
		});

		tvOkay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * Setting images on GridView
	 */
	private void setImageOnGridView() {
		ImageAdapter adapter = new ImageAdapter(this, mListUriPath);
		mIbAddPhoto.setVisibility(View.GONE);
		mGvImageGallery.setVisibility(View.VISIBLE);
		mGvImageGallery.setAdapter(adapter);
		mBtnAddMorePhoto.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_add_photo:
				dialogAddPhoto();
				break;
			case R.id.btn_add_more_photo:
				dialogAddPhoto();
				break;
		}
	}

	/**
	 * Creating dialog to add photo option
	 */
	private void dialogAddPhoto() {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		dialog.setContentView(R.layout.dialog_image_option);
		TextView tvCapture = (TextView) dialog.findViewById(R.id.tv_capture);
		TextView tvGallery = (TextView) dialog.findViewById(R.id.tv_gallery);
		TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);

		tvCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		tvCapture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				captureImage();
			}
		});

		tvGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
			}
		});
		dialog.show();
	}

	/**
	 * Capturing photo To add more photos for perticular seal
	 */
	private void captureImage() {
		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String imageTitle = Utility.getTimeStamp()+ ".png";
		File file = new File(Environment.getExternalStorageDirectory(), imageTitle);
		mUriPath = file.getPath();
		mUri = Uri.fromFile(file);

		Log.i(CLASS_TAG, "Image URI: " + mUri);

		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
		startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			previewCameraImage(Utility.getBitmap(mListUriPath.get(position), this), true);
	}
}
