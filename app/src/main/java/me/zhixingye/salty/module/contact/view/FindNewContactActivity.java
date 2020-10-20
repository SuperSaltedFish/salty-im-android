package me.zhixingye.salty.module.contact.view;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.salty.protos.UserProfile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.base.component.BasicActivity;
import me.zhixingye.base.listener.SimpleTextWatcher;
import me.zhixingye.base.view.SpacesItemDecoration;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.contact.contract.FindNewContactContract;
import me.zhixingye.salty.module.contact.presenter.FindNewContactPresenter;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.adapter.MaybeKnowAdapter;


public class FindNewContactActivity
        extends BasicActivity
        implements FindNewContactContract.View {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FindNewContactActivity.class);
        context.startActivity(intent);
    }

    private ConstraintLayout mClScan;

    private EditText mEtSearch;
    private RecyclerView mRecyclerView;
    private TextView mTvMyPhoneNumber;
    private ImageView mIvSearchIcon;
    private ImageView mIvEnterIcon;
    private ConstraintLayout mClCreateGroup;
    private MaybeKnowAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_find_new_contact;
    }

    protected void init(Bundle savedInstanceState) {
        mClScan = findViewById(R.id.mClScan);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mEtSearch = findViewById(R.id.mEtSearch);
        mTvMyPhoneNumber = findViewById(R.id.mTvMyPhoneNumber);
        mClCreateGroup = findViewById(R.id.mClCreateGroup);
        mIvSearchIcon = findViewById(R.id.mIvSearchIcon);
        mIvEnterIcon = findViewById(R.id.mIvEnterIcon);
        mAdapter = new MaybeKnowAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorAccent)));
        setToolbarId(R.id.mDefaultToolbar,true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) AndroidHelper.dip2px(8), SpacesItemDecoration.HORIZONTAL));

        mClScan.setOnClickListener(mOnClickListener);
        mClCreateGroup.setOnClickListener(mOnClickListener);
        mTvMyPhoneNumber.setOnClickListener(mOnClickListener);

        setInputListener();
        setData();
    }

    private void setInputListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    hideSoftKeyboard();
                    String searchText = mEtSearch.getText().toString();
                    if (!TextUtils.isEmpty(searchText)) {
                        getPresenter().searchUser(searchText);
                    }
                }
                return true;
            }
        });

        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mIvSearchIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                } else {
                    mIvSearchIcon.setImageTintList(ColorStateList.valueOf(Color.argb(196, 255, 255, 255)));
                }
            }

        });

        mEtSearch.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mIvEnterIcon.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void setData() {
        List<Object> s = new ArrayList<>();
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        s.add(new Object());
        mAdapter.submitList(s);
//        UserEntity user = AppClient.getInstance().getUserManager().getCurrentUser();
//        mTvMyPhoneNumber.setText(String.format(Locale.getDefault(), "%s:%s", getString(R.string.myPhoneNumber), user.getTelephone()));
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    public void showStrangerProfile(UserProfile user) {
        StrangerProfileActivity.startActivityByUserId(this, user.getUserId());
    }

    @Override
    public void showContactProfile(UserProfile user) {

    }

    @Override
    public void showSearchNotExist() {

    }

    @NonNull
    @Override
    public FindNewContactContract.Presenter createPresenterImpl() {
        return new FindNewContactPresenter();
    }

    @Override
    public void onPresenterBound() {

    }


//    @Override
//    protected void onRequestPermissionsResult(int requestCode, boolean isSuccess, String[] deniedPermissions) {
//        if(isSuccess){
//            Intent intent = new Intent(FindNewContactActivity.this, QrCodeScanActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    private final View.OnClickListener mOnMyPhoneClickListener = new OnOnlySingleClickListener() {
//        @Override
//        public void onSingleClick(View v) {
//            Intent intent = new Intent(FindNewContactActivity.this, MyQRCodeActivity.class);
//            intent.putExtra(MyQRCodeActivity.INTENT_EXTRA_QR_TYPE, MyQRCodeActivity.QR_CODE_TYPE_USER);
//            startActivity(intent);
//        }
//    };

//    private final View.OnClickListener mOnCreateGroupClickListener = new OnOnlySingleClickListener() {
//        @Override
//        public void onSingleClick(View v) {
//            startActivity(new Intent(FindNewContactActivity.this, CreateGroupActivity.class));
//        }
//    };

//    private final View.OnClickListener mOnScanLayoutClickListener = new OnOnlySingleClickListener() {
//        @Override
//        public void onSingleClick(View v) {
//            requestPermissionsInCompatMode(new String[]{Manifest.permission.CAMERA},0);
//        }
//    };

}
