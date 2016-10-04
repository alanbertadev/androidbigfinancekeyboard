# Big Finance Keyboard - Android Library
A big keyboard for entering financial values

# Travis CI Status [![Build Status](https://travis-ci.org/alanbertadev/androidbigfinancekeyboard.svg?branch=master)](https://travis-ci.org/alanbertadev/androidbigfinancekeyboard)

# Latest Distribution: [ ![Download](https://api.bintray.com/packages/alanbertadev/Tools/BigFinanceKeyboard/images/download.svg) ](https://bintray.com/alanbertadev/Tools/BigFinanceKeyboard/_latestVersion)

# How to get started!

1) Add dl.bintray.com to your repositories and include the aar library as a dependency in your build.gradle
```Gradle
    allprojects {
        repositories {
            jcenter()
            maven {
                url  "https://dl.bintray.com"
            }
        }
    }

    dependencies {
        compile 'alanbertadev.Tools:bigfinancekeyboard-release:0.2.2@aar'
    }
```

2) Add the following to your AndroidManifest.xml
```XML
        <activity
            android:name="com.alanbertadev.bigfinancekeyboard.BigFinanceKeyboard"
            android:label="@string/app_name"
            android:theme="@style/noAnimTheme">
        </activity>
```

3) Invoke the keyboard in your desired Activity
```
    Intent bfkIntent = new Intent(getActivity(), BigFinanceKeyboard.class);
    startActivityForResult(bfkIntent, 0);
```

4)Receive the result on your Activity's onActivityResult method invocation
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String financeKeyboardResult = extras.getString(BigFinanceKeyboard.INTENT_EXTRA_KEY);
        }
    }
```