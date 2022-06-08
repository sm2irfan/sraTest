
function timeConverter(UNIX_timestamp){
	  //alert("in timeConverter:" + UNIX_timestamp);
	  //var a = new Date(UNIX_timestamp).toISOString().split('T')[0];
	  var a = new Date(UNIX_timestamp); 
	  var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
	  var year = a.getFullYear();
	  var month = a.getMonth() + 1 < 10 ? '0' + (a.getMonth() + 1) : a.getMonth() + 1;
	  var date = a.getDate() < 10 ? '0' + a.getDate() : a.getDate();
	  var hour = a.getHours() < 10 ? '0' + a.getHours() : a.getHours();
	  var min = a.getMinutes() < 10 ? '0' + a.getMinutes() : a.getMinutes();
	  var sec = a.getSeconds() < 10 ? '0' + a.getSeconds() : a.getSeconds();
	  //var time = year + '/' + month + '/' + date + ' ' + hour + ':' + min + ':' + sec ;
	  var time = year + '/' + month + '/' + date + ' ' + hour + ':' + min ;
	  return time;
}

function dateFormater(UNIX_timestamp) {

	if(UNIX_timestamp == null) {
		return '';
	} else {
		var date = new Date(UNIX_timestamp*1);
		var dateString =  (date.getMonth() + 1)+ '/' + date.getDate() + '/' +date.getFullYear();
		return dateString;
	}
}

function getIdFromOptions (name, options) {
	for(var i=0; i<options.length; i++){
		if(options[i].name == name){
			return options[i].id;
		}
	}
}

function getNameFromOptions (id, options) {
	for(var i=0; i<options.length; i++){
		if(options[i].id == id){
			return options[i].name;
		}
	}
}