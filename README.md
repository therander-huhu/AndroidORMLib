# AndroidORMLib

提供直接向sqlite数据库存入，读取对象的功能，暂时只支持对象的所有属性均为String

## 使用方式

```
    repositories {
        maven { url "https://jitpack.io" }
    }
    compile 'com.github.therander-huhu:AndroidORMLib:0.1.0'
```

```
    TestModel testModel = new TestModel();
    testModel.setProperty1("property1");
    testModel.setProperty2("property2");
    testModel.setProperty3("property3");

    //save the model
    AutoDBHelper.getINSTANCE(this).save(testModel);
    
    //get property from the first saved model
    Strnig p1 = AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getProperty1();
    Strnig p2 = AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getProperty2();
    Strnig p3 = AutoDBHelper.getINSTANCE(this).get(TestModel.class).get(0).getProperty3();
```


