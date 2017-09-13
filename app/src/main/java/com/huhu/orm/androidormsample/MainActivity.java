package com.huhu.orm.androidormsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.huhu.orm.database.AutoDBHelper;
import com.huhu.orm.database.TestModel;

import junit.framework.Test;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestModel testModel = new TestModel();
        testModel.setProperty1("property1");
        testModel.setProperty2("property2");
        testModel.setProperty3("property3");
        testModel.setDoublePro(12.34);
        testModel.setIntPro(14);

        AutoDBHelper.getINSTANCE(this).save(testModel);
        Toast.makeText(this, AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getProperty1()
                + AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getProperty2()
                + AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getProperty3()
                + AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getDoublePro()
                + " " + AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getIntPro(), Toast.LENGTH_LONG).show();
    }
}
