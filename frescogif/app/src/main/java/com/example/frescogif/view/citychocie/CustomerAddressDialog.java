package com.example.frescogif.view.citychocie;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.frescogif.R;
import com.example.frescogif.constant.Constant;
import com.example.frescogif.view.citychocie.model.CityModel;
import com.example.frescogif.view.citychocie.model.DistrictModel;
import com.example.frescogif.view.citychocie.model.ProvinceModel;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 自定义dialog for地区选择
 * Created by ShellRay on 2016/5/29.
 */
public class CustomerAddressDialog extends Dialog {
    public CustomerAddressDialog(Context context) {
        super(context);
    }

    public CustomerAddressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder implements OnWheelChangedListener, View.OnClickListener {
        private Context context;
        private OnClickListener onDialogListener;
        private View layoutView;
        private CustomerAddressDialog dialog;

        public Builder(Context context) {
            this.context = context;
        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param
         * @return
         */
        public Builder setOnDialogListener(OnClickListener listener) {
            this.onDialogListener = listener;
            return this;
        }

        public CustomerAddressDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new CustomerAddressDialog(context, R.style.giftDialog);
            layoutView = inflater.inflate(R.layout.address_dialog_bottom_layout, null);
            dialog.addContentView(layoutView, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layoutView);

            setUpViews();
            setUpListener();
            setUpData();
            setBottomView();

            return dialog;
        }

        /**
         * 设置将view放在底部
         */
        private void setBottomView() {
            Window window = dialog.getWindow();
            // 可以在此设置显示动画
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = Constant.displayHeight;
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = LayoutParams.MATCH_PARENT;
            wl.height = LayoutParams.WRAP_CONTENT;
            // 设置显示位置
            dialog.onWindowAttributesChanged(wl);
        }

        private WheelView mViewProvince;
        private WheelView mViewCity;
        private WheelView mViewDistrict;
        private Button mBtnConfirm;
        private Button mBtnCancle;
        /**
         * 所有省
         */
        protected String[] mProvinceDatas;
        /**
         * key - 省 value - 市
         */
        protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
        /**
         * key - 市 values - 区
         */
        protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
        /**
         * key - 市 values - 区的所有县的zipcode
         */
        protected Map<String, Map<String ,String>> mDistrictZipCodeForCityDatasMap = new HashMap<String, Map<String ,String>>();
        /**
         * key - 区 values - 邮编
         */
        protected Map<String, String> mZipcodeProviceDatasMap = new HashMap<String, String>();

        protected Map<String, String> mZipcodeCityDatasMap = new HashMap<String, String>();

//        protected Map<String, String> mZipcodeDistrictDatasMap = new HashMap<String, String>();
        /**
         * 当前省的名称
         */
        protected String mCurrentProviceName;
        /**
         * 当前市的名称
         */
        protected String mCurrentCityName;
        /**
         * 当前区的名称
         */
        protected String mCurrentDistrictName = "";

        /**
         * 当前区的编码
         */
        protected String mCurrentProviceZipCode = "";
        protected String mCurrentCityZipCode = "";
        protected String mCurrentDistrictZipCode = "";

        private void setUpViews() {
            mViewProvince = (WheelView) layoutView.findViewById(R.id.id_province);
            mViewCity = (WheelView) layoutView.findViewById(R.id.id_city);
            mViewDistrict = (WheelView) layoutView.findViewById(R.id.id_district);
            mBtnConfirm = (Button) layoutView.findViewById(R.id.btn_confirm);
            mBtnCancle = (Button) layoutView.findViewById(R.id.btn_cancle);
        }

        private void setUpListener() {
            // 添加change事件
            mViewProvince.addChangingListener(this);
            // 添加change事件
            mViewCity.addChangingListener(this);
            // 添加change事件
            mViewDistrict.addChangingListener(this);
            // 添加onclick事件
            mBtnConfirm.setOnClickListener(this);

            mBtnCancle.setOnClickListener(this);
        }

        private void setUpData() {
            initProvinceDatas();
            mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
            // 设置可见条目数量
            mViewProvince.setVisibleItems(7);
            mViewCity.setVisibleItems(7);
            mViewDistrict.setVisibleItems(7);
            updateCities();
            updateAreas();
        }

        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            // TODO Auto-generated method stub
            if (wheel == mViewProvince) {
                updateCities();
            } else if (wheel == mViewCity) {
                updateAreas();
            } else if (wheel == mViewDistrict) {
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//                mCurrentDistrictZipCode = mZipcodeDistrictDatasMap.get(mCurrentDistrictName);
                Map<String, String> mZipcodeDistrictInCityDatasMap = mDistrictZipCodeForCityDatasMap.get(mCurrentCityName);
                //根据当前城市获取到县的集合，然后得到县的zipCode
                 mCurrentDistrictZipCode = mZipcodeDistrictInCityDatasMap.get(mCurrentDistrictName);
            }
        }

        /**
         * 根据当前的市，更新区WheelView的信息
         */
        private void updateAreas() {
            int pCurrent = mViewCity.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
            mCurrentCityZipCode = mZipcodeCityDatasMap.get(mCurrentCityName);
            //根据当前的城市获取县的集合
            Map<String, String> mZipcodeDistrictInCityDatasMap = mDistrictZipCodeForCityDatasMap.get(mCurrentCityName);
            String[] areas = mDistrictDatasMap.get(mCurrentCityName);

            if (areas == null) {
                areas = new String[]{""};
            }
            mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
            mViewDistrict.setCurrentItem(0);
            //所有的城市下不是都有地区的 所以得判断是否有地区然后显示 mCurrentDistrictName
            //获取到当前城市下是否有地区
            boolean hasNoDistict = mZipcodeDistrictInCityDatasMap.isEmpty();
            if(!hasNoDistict) {
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];//当不滑动区部分时，不初始化这里的话，就会出现默认永远是东城区 区号也是
                //用县的集合来得到当前县的zipCode码
                mCurrentDistrictZipCode = mZipcodeDistrictInCityDatasMap.get(mCurrentDistrictName);
            }else{
                mCurrentDistrictName = "";
                mCurrentDistrictZipCode = "";
            }
        }

        /**
         * 根据当前的省，更新市WheelView的信息
         */
        private void updateCities() {
            int pCurrent = mViewProvince.getCurrentItem();
            mCurrentProviceName = mProvinceDatas[pCurrent];
            mCurrentProviceZipCode = mZipcodeProviceDatasMap.get(mCurrentProviceName);
            String[] cities = mCitisDatasMap.get(mCurrentProviceName);
            if (cities == null) {
                cities = new String[]{""};
            }
            mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
            mViewCity.setCurrentItem(0);
            updateAreas();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    showSelectedResult();
                    onDialogListener.onClick(dialog, BUTTON_NEGATIVE);
                    break;
                case R.id.btn_cancle:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }

        private void showSelectedResult() {
            /*ToastUtil.showCustomerToast(context, "当前选中:" + mCurrentProviceZipCode + "," + mCurrentCityZipCode + ","
                    + mCurrentDistrictZipCode);*/
            Constant.CityInfo.PROVICE_NAME = mCurrentProviceName;
            Constant.CityInfo.CITY_NAME = mCurrentCityName;
            Constant.CityInfo.DISTRICT_NAME = mCurrentDistrictName;
            Constant.CityInfo.ZIP_PROVICE_CODE = mCurrentProviceZipCode;
            Constant.CityInfo.ZIP_CITY_CODE = mCurrentCityZipCode;
            Constant.CityInfo.ZIP_DISTRICT_CODE = mCurrentDistrictZipCode;
        }

        /**
         * 解析省市区的XML数据
         */
        protected void initProvinceDatas() {
            List<ProvinceModel> provinceList = null;
            AssetManager asset = context.getAssets();
            try {
                InputStream input = asset.open("citys_data.xml");
                // 创建一个解析xml的工厂对象
                SAXParserFactory spf = SAXParserFactory.newInstance();
                // 解析xml
                SAXParser parser = spf.newSAXParser();
                XmlParserHandler handler = new XmlParserHandler();
                parser.parse(input, handler);
                input.close();
                // 获取解析出来的数据
                provinceList = handler.getDataList();
                // 初始化默认选中的省、市、区
                if (provinceList != null && !provinceList.isEmpty()) {
                    mCurrentProviceName = provinceList.get(0).getName();
                    mCurrentProviceZipCode = provinceList.get(0).getZipcode();
                    List<CityModel> cityList = provinceList.get(0).getCityList();
                    if (cityList != null && !cityList.isEmpty()) {
                        mCurrentCityName = cityList.get(0).getName();
                        mCurrentCityZipCode = cityList.get(0).getZipcode();
                        List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                        mCurrentDistrictName = districtList.get(0).getName();
                        mCurrentDistrictZipCode = districtList.get(0).getZipcode();
                    }
                }

                mProvinceDatas = new String[provinceList.size()];
                for (int i = 0; i < provinceList.size(); i++) {
                    // 遍历所有省的数据
                    mProvinceDatas[i] = provinceList.get(i).getName();
                    List<CityModel> cityList = provinceList.get(i).getCityList();
                    mZipcodeProviceDatasMap.put(provinceList.get(i).getName(), provinceList.get(i).getZipcode());
                    String[] cityNames = new String[cityList.size()];
                    for (int j = 0; j < cityList.size(); j++) {
                        // 遍历省下面的所有市的数据
                        cityNames[j] = cityList.get(j).getName();
                        List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                        String[] distrinctNameArray = new String[districtList.size()];
                        DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                        mZipcodeCityDatasMap.put(cityList.get(j).getName(), cityList.get(j).getZipcode());
                        //由于是每一个城市下都得创建一个，所以不能提取成全局变量了
                        Map<String, String> mZipcodeDistrictDatasMap = new HashMap<String, String>();
                        //遍历当前城市的县的数据
                        for (int k = 0; k < districtList.size(); k++) {
                            // 遍历市下面所有区/县的数据
                            DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                            // 区/县对于的唯一号码，只保存同一个市的县的zipCode
                            mZipcodeDistrictDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                            distrinctArray[k] = districtModel;
                            distrinctNameArray[k] = districtModel.getName();
                        }
                        //将当前城市 与 县的集合放到一个集合中，为了让后续得到县的code码
                        mDistrictZipCodeForCityDatasMap.put(cityList.get(j).getName(),mZipcodeDistrictDatasMap);
                        // 市-区/县的数据，保存到mDistrictDatasMap
                        mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {

            }
        }

    }

}
