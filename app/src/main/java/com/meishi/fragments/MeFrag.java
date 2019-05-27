package com.meishi.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.activities.EditActivity;
import com.meishi.activities.LoginActivity;
import com.meishi.activities.PrivateComActivity;
import com.meishi.contract.EditProFileContract;
import com.meishi.contract.MeContract;
import com.meishi.contract.UploadContract;
import com.meishi.helpers.DatabaseHelper;
import com.meishi.infobean.MeInfo;
import com.meishi.presenter.EditProfileDealer;
import com.meishi.presenter.MeDealer;
import com.meishi.presenter.UploadDealer;
import com.meishi.utils.FileUtils;
import com.meishi.utils.TimeUtils;
import com.meishi.utils.UriUtils;
import com.meishi.views.TextBar;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFrag extends Fragment implements MeContract.MeView, EditProFileContract.EditProfileView, UploadContract.UploadView {
    private static final int REQUEST_NICKNAME_CODE = 4;
    private static final int REQUEST_DESC_CODE = 5;


    //请求识别码分别为（本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;
    //相机拍摄的头像文件
    private static final File USER_ICON = new File(FileUtils.getInstance().getUserIconFolder(),"user_icon1.jpg");
    private MeInfo meInfo = null;
    private MeContract.MePresenter mePresenter;
    private EditProFileContract.EditProfilePresenter editProfilePresenter;
    private UploadContract.UploadPresenter uploadPresenter;
    private boolean isProfileChanged = false;
    private DatabaseHelper databaseHelper;

    @BindView(R.id.iv_headPic) ImageView headPic;
    @BindView(R.id.tb_nickname) TextBar tb_nickname;
    @BindView(R.id.tb_phone) TextBar tb_phone;
    @BindView(R.id.tb_sex)TextBar tb_sex;
    @BindView(R.id.tb_birth)TextBar tb_birth;
    @BindView(R.id.tb_desc)TextBar tb_desc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,container,false);
        ButterKnife.bind(this,view);
        mePresenter = new MeDealer(MyApplication.getContext(),this);
        editProfilePresenter = new EditProfileDealer(MyApplication.getContext(),this);
        uploadPresenter = new UploadDealer(MyApplication.getContext(),this);
        databaseHelper = new DatabaseHelper(MyApplication.getContext());
        init();
        Log.d("onCreateView", "onCreateView: file isExist: "+USER_ICON.exists());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mePresenter.connectView(this);
        editProfilePresenter.connectView(this);
        uploadPresenter.connectView(this);
//        if (isProfileChanged){
//            isProfileChanged = false;
//            init();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mePresenter.destoyView();
        editProfilePresenter.destroyView();
        uploadPresenter.destroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //用户没有进行有效的设置操作
        switch (requestCode){
            case CODE_CAMERA_REQUEST:
                if(USER_ICON.exists()){
                    photoClip(Uri.fromFile(USER_ICON));
                }
                break;

            case CODE_PHOTO_REQUEST:
                if(data != null){
                    //复制一份照片到指定我们创建的uri
                    File path_pre = new File(UriUtils.getpath(this.getContext(),data.getData()));
                    int i = FileUtils.getInstance().CopySdcardFile(path_pre,USER_ICON);
                    Log.d("CopySdcardFile", "CopySdcardFile: "+i);
                    photoClip(data.getData());
                }
                break;

            case CODE_PHOTO_CLIP://裁剪成功
                // 将照片上传到服务器
                if(data != null) {
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        Bitmap photo = bundle.getParcelable("data");
//                        headPic.setImageBitmap(photo);
                        //上传Bitmap
                        uploadPresenter.uploadPic(photo);
                    }
                }
                break;

            case REQUEST_NICKNAME_CODE://裁剪成功
                if(resultCode == 0){//点击保存
                    Log.d("REQUEST_NICKNAME_CODE", "onActivityResult: "+data.getStringExtra(AppConstants.EDIT_CONTENT));
                    editProfilePresenter.editPrifile(AppConstants.EDIT_NICKNAME,data.getStringExtra(AppConstants.EDIT_CONTENT));
                }
                break;

            case REQUEST_DESC_CODE://裁剪成功
                if(resultCode == 0){//点击保存
                    Log.d("REQUEST_DESC_CODE", "onActivityResult: "+data.getStringExtra(AppConstants.EDIT_CONTENT));
                    editProfilePresenter.editPrifile(AppConstants.EDIT_DESC,data.getStringExtra(AppConstants.EDIT_CONTENT));
                }
                break;
        }
    }

    /**
     * 更换头像
     */
    @OnClick(R.id.rl_headPic)
    public void changeHeadPic(){
        //选择拍摄图片来获取头像还是直接在相册获取图像
        new AlertDialog.Builder(this.getContext()).setTitle("获取头像")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setItems(new String[]{"打开相册", "拍摄照片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                getPicFromLocal();
                                break;

                            case 1:

                                getPicFromCamera();
                                break;

                            default:

                                break;

                        }
                    }
                }).show();
    }


    @OnClick(R.id.tb_nickname)
    public void editUsername(){
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("title",tb_nickname.getIntroText());
        intent.putExtra("profile",tb_nickname.getContentText());
        this.startActivityForResult(intent,REQUEST_NICKNAME_CODE);
    }

    @OnClick(R.id.tb_sex)
    public void changeSex(){
        final String items[] = {"男", "女"};
        final int[] sex = {0};
        if (!tb_sex.getContentText().toString().equals("男")){
            sex[0] = 1;
        }
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setIcon(R.mipmap.me)//设置标题的图片
                .setTitle("更改性别")//设置对话框的标题
                .setSingleChoiceItems(items, sex[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex[0] = which;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        tb_sex.setContentText(items[sex[0]]);
                        editProfilePresenter.editPrifile(AppConstants.EDIT_SEX,sex[0]+"");
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    @OnClick(R.id.tb_birth)
    public void changeBirthdate(){
        String[] dates = tb_birth.getContentText().toString().split("-");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                tb_birth.setContentText(year+"-"+(month+1)+"-"+dayOfMonth);
                String date = year+"-"+(month+1)+"-"+dayOfMonth;
                editProfilePresenter.editPrifile(AppConstants.EDIT_BIRTHDATE,date);
            }
        },Integer.parseInt(dates[0]),Integer.parseInt(dates[1])-1,Integer.parseInt(dates[2]));
        datePickerDialog.show();
    }

    @OnClick(R.id.tb_desc)
    public void changeDesc(){
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("title",tb_desc.getIntroText());
        intent.putExtra("profile",tb_desc.getContentText());
        this.startActivityForResult(intent,REQUEST_DESC_CODE);
    }

    @OnClick(R.id.tb_comHistory)
    public void comHistory(){
        Intent intent = new Intent(this.getActivity(), PrivateComActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bt_logout)
    public void logout(){
        databaseHelper.deleteDatas();
        Intent intent = new Intent(this.getContext(), LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.writeToSP(AppConstants.TOKEN,"");
        startActivity(intent);

        ((Activity)getContext()).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }

    private void init(){
        //显示个人资料
//        initData();

        //获取资料
        mePresenter.getProfile();

        //设置监听器
    }


    private void initData(){
        if (meInfo != null) {
            //设置头像
            if (meInfo.getHeadpicurl() != null) {
                Glide.with(getContext())
                        .load(meInfo.getHeadpicurl())
                        .into(headPic);
            }
            //设置用户名
            if(meInfo.getNickname() != null){
                tb_nickname.setContentText(meInfo.getNickname());
            }

            //设置手机号码
            if(meInfo.getPhone() != null){
                tb_phone.setContentText(meInfo.getPhone());
            }

            //设置性别
            String[] sex = {"男","女"};
            tb_sex.setContentText(sex[meInfo.getSex()]);

            tb_birth.setContentText(TimeUtils.getStringTime(meInfo.getBirthdate()));

            if(meInfo.getDescription() != null){
                tb_desc.setContentText(meInfo.getDescription());
            }

        }else{
//            failUpdate();//获取失败
        }
    }

    /**
     * 从本机相册获取照片
     */
    private void getPicFromLocal(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(intent,CODE_PHOTO_REQUEST);//获取图片后返回结果
    }

    /**
     * 通过相机拍摄并存入设置的对应路径中
     */
    private void getPicFromCamera(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //指定调用相机拍照后照片的存储路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//安卓N及以上对uri的使用要特殊设置
            Uri contentUri = FileProvider.getUriForFile(getContext(), AppConstants.PROVIDER,USER_ICON);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,contentUri);
        }else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(USER_ICON));
        }
        startActivityForResult(intent,CODE_CAMERA_REQUEST);
    }

    /**
     * 图片裁剪
     *
     * @param uri 图片链接
     */
    private void photoClip(Uri uri){
        //调用系统自带的图片裁剪
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
//        intent.setDataAndType(uri,"image/*");
        //将crop设置为true是设置开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop","true");
        //aspectX、aspectY是宽高的比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);

        /*outputX、outpuY是裁剪照片宽高
         *不宜将值设置过高
         * 否则超过Binder机制的缓存大小的1M限制会报错TransactionTooLargeException
         */
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//安卓N及以上对uri的使用要特殊设置
            System.out.println("uri1: "+uri);
            Uri contentUri = FileProvider.getUriForFile(getContext(), AppConstants.PROVIDER,USER_ICON);
            intent.setDataAndType(contentUri,"image/*");
            System.out.println("contentUri: "+contentUri);
        }else {
            System.out.println("uri2: "+uri);
            intent.setDataAndType(uri,"image/*");
        }
        //添加临时权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        this.startActivityForResult(intent,CODE_PHOTO_CLIP);
    }

    @Override
    public void update(MeInfo data) {
        meInfo = data;
        initData();
    }

    @Override
    public void failUpdate() {
        Toast.makeText(getContext(),"获取个人资料失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successEdit() {

        //刷新资料
//        isProfileChanged = true;
        init();
    }

    @Override
    public void failEdit() {
        Toast.makeText(this.getContext(),"修改失败",Toast.LENGTH_SHORT).show();
    }


    //上传照片成功
    @Override
    public void successUpload(String msg, String url) {
        editProfilePresenter.editPrifile(AppConstants.EDIT_HEADPIC,url);
    }
    //上传照片失败
    @Override
    public void failUpload(String msg) {
        Toast.makeText(getContext(),"msg",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logout(String msg) {
        Toast.makeText(this.getContext(),msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.writeToSP(AppConstants.TOKEN,"");
        startActivity(intent);

        ((Activity)getContext()).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }
}
