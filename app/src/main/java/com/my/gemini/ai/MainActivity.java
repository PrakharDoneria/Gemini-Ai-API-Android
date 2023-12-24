package com.my.gemini.ai;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
	
	private HashMap<String, Object> map1 = new HashMap<>();
	private String Imput_data = "";
	private HashMap<String, Object> header = new HashMap<>();
	private HashMap<String, Object> params = new HashMap<>();
	private String geminiText = "";
	
	private ArrayList<HashMap<String, Object>> list_map = new ArrayList<>();
	
	private RecyclerView recyclerview1;
	private LinearLayout linear1;
	private EditText edittext1;
	private Button button_submit;
	
	private RequestNetwork Net;
	private RequestNetwork.RequestListener _Net_request_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		recyclerview1 = findViewById(R.id.recyclerview1);
		linear1 = findViewById(R.id.linear1);
		edittext1 = findViewById(R.id.edittext1);
		button_submit = findViewById(R.id.button_submit);
		Net = new RequestNetwork(this);
		
		button_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext1.getText().toString().length() < 2) {
					SketchwareUtil.showMessage(getApplicationContext(), "Please Enter your text ");
				}
				else {
					Imput_data = edittext1.getText().toString();
					
					        header.put("Content-Type", "application/json");
					
					        // Rest of your code...
					        ArrayList<HashMap<String, Object>> contentsList = new ArrayList<>();
					        HashMap<String, Object> partsMap = new HashMap<>();
					        ArrayList<HashMap<String, Object>> partsList = new ArrayList<>();
					        partsMap.put("text", Imput_data);
					        partsList.add(partsMap);
					        HashMap<String, Object> contentMap = new HashMap<>();
					        contentMap.put("parts", partsList);
					        contentsList.add(contentMap);
					        params.put("contents", contentsList);
					
					        Gson gson = new Gson();
					        String jsonParams = gson.toJson(params);
					
					        // Replace "YOUR_API_KEY" with your actual API key
					        String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=API-KEY-HERE";
					
					        Net.setHeaders(header);
					        // Use setParams instead of setRequestBody
					        Net.setParams(params, RequestNetworkController.REQUEST_BODY);
					        Net.startRequestNetwork(RequestNetworkController.POST, geminiApiUrl, "", _Net_request_listener);
					map1 = new HashMap<>();
					map1.put("User", "You");
					map1.put("Text", edittext1.getText().toString());
					list_map.add(map1);
					recyclerview1.setAdapter(new Recyclerview1Adapter(list_map));
					edittext1.setText("");
				}
			}
		});
		
		_Net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				Map<String, Object> geminiMap = new Gson().fromJson(_response, Map.class);
				List<Map<String, Object>> candidates = (List<Map<String, Object>>) geminiMap.get("candidates");
				Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
				List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
				String geminiText = parts.get(0).get("text").toString();
				
				map1 = new HashMap<>();
				map1.put("User", "AI -Gemini-pro");
				map1.put("Text", geminiText);
				list_map.add(map1);
				recyclerview1.setAdapter(new Recyclerview1Adapter(list_map));
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setStackFromEnd(true);
		// layoutManager.setReverseLayout(true);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview1.setLayoutManager(layoutManager);
		
		edittext1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFFFFFFF));
		button_submit.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFF607D8B));
	}
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.cus, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout linear_gpt = _view.findViewById(R.id.linear_gpt);
			final LinearLayout linearuser = _view.findViewById(R.id.linearuser);
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final ImageView image_user = _view.findViewById(R.id.image_user);
			final TextView text_username = _view.findViewById(R.id.text_username);
			final TextView text_response = _view.findViewById(R.id.text_response);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final androidx.cardview.widget.CardView cardview2 = _view.findViewById(R.id.cardview2);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			
			if (_data.get((int)_position).get("User").toString().equals("You")) {
				linearuser.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF69F0AE));
				textview1.setText(list_map.get((int)_position).get("User").toString());
				textview2.setText(list_map.get((int)_position).get("Text").toString());
				linear_gpt.setVisibility(View.GONE);
				linearuser.setVisibility(View.VISIBLE);
			}
			else {
				linear_gpt.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF40C4FF));
				text_username.setText(list_map.get((int)_position).get("User").toString());
				text_response.setText(list_map.get((int)_position).get("Text").toString());
				linearuser.setVisibility(View.GONE);
				linear_gpt.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
