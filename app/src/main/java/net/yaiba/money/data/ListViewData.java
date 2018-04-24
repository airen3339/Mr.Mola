package net.yaiba.money.data;

import android.app.Application;

/**
 * Created by benyaiba on 2017/06/09.
 */

public class ListViewData extends Application{

    //用于保存当前一览中的数据位置
    private int firstVisiblePosition; // listView第一个可见的item的位置，即在数据集合中的位置position
    private int firstVisiblePositionTop; // listView第一可见的item距离父布局的top

    //用于保存当前快速查找文本框中的文字内容
    private String quickSearchText;

    private String spinner_data_no;
    private String spinner_data_unit;

    private String mainListSortType;

    public int getFirstVisiblePosition(){
        return this.firstVisiblePosition;
    }
    public void setFirstVisiblePosition(int c){
        this.firstVisiblePosition= c;
    }

    public int getFirstVisiblePositionTop(){
        return this.firstVisiblePositionTop;
    }
    public void setFirstVisiblePositionTop(int c){
        this.firstVisiblePositionTop= c;
    }

    public String getQuickSearchText() {
        return this.quickSearchText;
    }
    public void setQuickSearchText(String s){
        this.quickSearchText = s;
    }


    public  String getSpinner_data_no(){
        return this.spinner_data_no;
    }
    public  void setSpinner_data_no(String s){
        this.spinner_data_no = s;
    }

    public  String getSpinner_data_unit(){
        return this.spinner_data_unit;
    }
    public  void setSpinner_data_unit(String s){
        this.spinner_data_unit = s;
    }

    public  String getMainListSortType(){
        return this.mainListSortType;
    }
    public  void setMainListSortType(String s){
        this.mainListSortType = s;
    }

    @Override
    public void onCreate(){
        firstVisiblePosition = 0;
        firstVisiblePositionTop = 0;
        quickSearchText = "";
        spinner_data_no = "";
        spinner_data_unit ="";
        mainListSortType = "id desc";
        super.onCreate();
    }

}
