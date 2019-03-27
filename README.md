# Convertigo Barcode Scanner Native Plugin#

This plugin allows for native Barcode scans 

### Install ###
Install this plugin like you would any other cordova plugin:
```
#!shell
cordova plugin add https://github.com/CharlesGrimont/cordova-plugin-barcode-convertigo.git@0.3.4
```
### Target Platforms ###
* Android (4.4+)
* TODO: Windows Phone 8.1+

### Usage ###
The plugin creates a global variable in your app: 
```
#!javascript
ConvScannerPlugin
```

This global variable allows for two functions:
```
#!javascript

startNormalScan: function(onSuccessCallback, onCancelCallback)
startQuickScan: function(onSuccessCallback, onCancelCallback)

```
See the specs (and the Android application) to find out where to use which scans. 

Once called, these functions start their respective scan activities and returns either the scan result, or the fact that the user cancelled the scan.


```
#!javascript

var onSuccess = function(result){
    console.log("Result type: " + result.type + " Value: " result.result); 
};

var onCancel = function(){
    console.log("User cancelled the barcode scan."); 
};

ConvScannerPlugin.startQuickScan(onSuccess, onCancel);
```


### Help/Issues ###

* See other side of boat :)