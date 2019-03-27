module.exports = {
	scan: (parameters, onSuccessCallback, onCancelCallback)=>{
		cordova.exec(function(result){
			if(result.returnCode == 0 && typeof(onCancelCallback) !== 'undefined') onCancelCallback();
			else 
			{
				if(result.returnCode == -1 && typeof(onSuccessCallback) !== 'undefined')
				{
					var finalResult = {};
					finalResult.type = result.type;
					finalResult.result = result.result;
					onSuccessCallback(finalResult);
				}
			}
		}, 
		function(result){
			console.error("JSON Error ConvBarcodeScanner"); 
		},
		"ConvBarcodeScanner",
		"Scan",parametersAsArray(parameters));
	}
};

parametersAsArray = (parameters)=> {
	let array = [];
	if(parameters != undefined){
		return null;
	}
	else{
		parameters["laserColor"] != undefined ? array.push(parameters["laserColor"]) : array.push(null);
		parameters["laserEnabled"] != undefined ? array.push(parameters["laserEnabled"]) : array.push(null);
		return array;
	}
};