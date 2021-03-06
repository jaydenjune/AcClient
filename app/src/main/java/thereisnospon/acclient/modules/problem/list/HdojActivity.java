package thereisnospon.acclient.modules.problem.list;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import thereisnospon.acclient.R;
import thereisnospon.acclient.base.activity.AppBarActivity;
import thereisnospon.acclient.base.activity.SearchBarActivity;
import thereisnospon.acclient.event.Arg;
import thereisnospon.acclient.modules.problem.detail.ShowProblemActivity;
import thereisnospon.acclient.modules.problem.list.search.SearchProblemFragment;
import thereisnospon.acclient.modules.settings.Settings;
import thereisnospon.acclient.utils.SpUtil;
import thereisnospon.acclient.utils.net.request.PostRequest;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * @author  thereisnospon
 * 显示首页
 * Created by yzr on 16/6/5.
 */
public final class HdojActivity extends SearchBarActivity {
	private static final String TAG = "HdojActivity";
	private boolean first = true;

	public static void showInstance(@NonNull Activity cxt) {
		Intent intent = new Intent(cxt, HdojActivity.class);
		intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY);
	}

	private static String SAVE_FIRST="save_first";

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVE_FIRST, first);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState == null) {
			return;
		}
		first = savedInstanceState.getBoolean(SAVE_FIRST);
	}



	@Override
	protected void setupContent(@NonNull FrameLayout contentLayout) {
		setupFragment(contentLayout.getId(), HdojProblemFragment.newInstance());
		showMessageInfo();
	}

	private void showMessageInfo(){

		if(SpUtil.getInstance().getBoolean(SpUtil.FIRST_VISIT)){
		Handler handler=new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				showMessageDialog();
			}
		},1000);
		SpUtil.getInstance().putBoolean(SpUtil.FIRST_VISIT,false);
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		setupFragment(SearchProblemFragment.newInstance(query));
		Logger.d(query);
		return false;
	}


	@Override
	protected void inflateSearchMenu(@NonNull Menu menu) {
		getMenuInflater().inflate(R.menu.problem_list_menu, menu);
	}




	private void showPageDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this)
		                          .inflate(R.layout.alert_goto_page, null);
		final EditText editText = (EditText) view.findViewById(R.id.to_page);
		AlertDialog dialog = builder.setTitle(R.string.search_problem_by_page)
		                            .setView(view)
		                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			                            @Override
			                            public void onClick(DialogInterface dialog, int which) {
				                            goToPage(editText.getText()
				                                             .toString());
			                            }
		                            })
		                            .setNegativeButton(android.R.string.cancel, null)
		                            .setCancelable(true)
		                            .create();
		dialog.show();
	}




	private void showMessageDialog(){

		AlertDialog.Builder builder=new AlertDialog.Builder(this);

		builder.setTitle(R.string.warm_tips_title)
				.setMessage(R.string.warm_tips_message)
				.setPositiveButton(R.string.Go, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						goToPage("11");
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				})
				.show();

	}


	private void goToPage(String str) {
		try {
			int page = Integer.parseInt(str);
			setupFragment(HdojProblemFragment.newInstance(page));
		} catch (NumberFormatException e) {
			showShortSnackbar(R.string.search_by_wrong_page);
		}
	}


	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	private void showIdDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this)
		                          .inflate(R.layout.alert_goto_page, null);
		final EditText editText = (EditText) view.findViewById(R.id.to_page);
		AlertDialog dialog = builder.setTitle(R.string.search_by_problem_id)
		                            .setView(view)
		                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			                            @Override
			                            public void onClick(DialogInterface dialog, int which) {
				                            gotToId(editText.getText()
				                                            .toString());
			                            }
		                            })
		                            .setNegativeButton(android.R.string.cancel, null)
		                            .setCancelable(true)
		                            .create();
		dialog.show();
	}

	private void gotToId(String str) {
		try {
			int id = Integer.parseInt(str);
			Intent intent = new Intent(this, ShowProblemActivity.class);
			intent.putExtra(Arg.LOAD_PROBLEM_DETAIL, id);
			startActivity(intent);
		} catch (NumberFormatException e) {
			Toast.makeText(this, R.string.search_by_wrong_id, Toast.LENGTH_SHORT)
			     .show();
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_problem_go_page:
				showPageDialog();
				break;
			case R.id.menu_problem_go_id:
				showIdDialog();
				break;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (first && Settings.getInstance()
			                     .ifExitConfirm()) {
				first = false;
				showShortSnackbar(R.string.search_pressback_return, android.R.string.ok, new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


}
