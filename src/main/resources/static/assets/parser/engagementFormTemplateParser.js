var computedParamName = 'Computed';
var dynamicViewModelName;
var isModalAdded = false;
var engagementFormFormData;

ko.validation.rules.pattern.message = 'Invalid.';

ko.validation.init({
    registerExtenders: true,
    messagesOnModified: true,
    insertMessages: true,
    parseInputAttributes: true,
    messageTemplate: null
}, true);


function parseJsonToBind(jsontemplate, jsondata, viewModel) {
	dynamicViewModelName = viewModel;
	engagementFormFormData = jsondata;
	for (var key in jsontemplate) { 
		parseKeyValueToBind(key, jsontemplate[key]);
	}
}

function parseKeyValueToBind(jsonTemplateKey, jsonTemplateValue) {
	var htmlVal = '';
	if (!isModalAdded) {
		htmlVal += addModal();
		isModalAdded = true;
	}
	htmlVal += parsePanels(jsonTemplateValue);
	$('#'+jsonTemplateKey).append(htmlVal);
}


function parsePanels(panels) {
	var htmlData = '';
	for(var i=0; i<panels.length; i++){
		htmlData += parsePanelContent(panels[i]);
	}
	return htmlData;
}

function parsePanelContent(s) {
	var htmlData = ''
	if(s.paneldefault) {
		htmlData += this.createPanelDefault(s.paneldefault);
    }
    if(s.panelheader) {
    	htmlData += this.createPanelHeader(s.panelheader);
    }
    if(s.panelbody) {
    	htmlData += this.createPanelBody(s.panelbody);
    }
    if (s.breakline) {
    	htmlData += this.createBreakLine();
    }
    return htmlData;
}

function parsePanelDefault(s) {
	var htmlData = '';
	for(var i=0; i<s.length; i++){
	    htmlData += this.parsePanelContent(s[i]);
	}
	return htmlData;
}


function parsePanelContentBody(s) {
	var htmlData = '';
	for(var i=0; i<s.length; i++){
	    htmlData += this.parsePanelContent(s[i]);
	}
	return htmlData;
}

function parsePanelBody(s) {
	var htmlData = '';
	for(var i=0; i<s.length; i++){
	    htmlData += this.parse(s[i]);
	}
	return htmlData;
}

function createPanelDefault(paneldefault) {
	if(paneldefault.dependencynameslist) {
		var showAnneureObsAll = '';
		for (var i=0; i<paneldefault.dependencynameslist.length; i++) {
			var showAnnexureObs = 'show'+paneldefault.dependencynameslist[i]+''+computedParamName
			if (i==0) {
				showAnneureObsAll += ''+showAnnexureObs+'()'
			} else {
				showAnneureObsAll += ' || '+showAnnexureObs+'()'
			}
		}
		return '' +
	    '<div class="panel panel-default" data-bind="visible: '+showAnneureObsAll+'">' +
	    this.parsePanelDefault(paneldefault.value) +
		'</div>' +
		'';
	} else {
		return '' +
	    '<div class="panel panel-default">' +
	    this.parsePanelDefault(paneldefault.value) +
		'</div>' +
		'';
	}
}

function createPanelHeader(panelheader) {
	return '' +
    '<div class="panel-heading">' +
    '<h3 class="panel-title-medium">'+panelheader.value+'</h3>' +
	'</div>' +
	'';
}

function createPanelBody(panelbody) {
	if(panelbody.type == 'paneltext') {
		return '' +
	    '<div class="panel-body">' +
	    panelbody.value +
		'</div>' +
		'';
	}  else if(panelbody.type == 'panelcontent') {
		return '' +
	    '<div class="panel-body">' +
	    this.parsePanelContentBody(panelbody.value) +
		'</div>' +
		'';
	}  else if(panelbody.type == 'paneltextarea') {
		return '' +
	    '<div class="panel-body">' +
	    this.processFields(panelbody.value) +
		'</div>' +
		'';
	} else if(panelbody.type == 'paneltable') {
		if (panelbody.dependencynameslist) {
			var showAnneureObsAll = '';
			for (var i=0; i<panelbody.dependencynameslist.length; i++) {
				var showAnnexureObs = 'show'+panelbody.dependencynameslist[i]+''+computedParamName
				if (i==0) {
					showAnneureObsAll += ''+showAnnexureObs+'()'
				} else {
					showAnneureObsAll += ' || '+showAnnexureObs+'()'
				}
			}
			return '' +
		    '<div class="panel-body" data-bind="visible: '+showAnneureObsAll+'">' +
		    this.parsePanelBody(panelbody.value) +
			'</div>' +
			'';
		} else {
			return '' +
		    '<div class="panel-body">' +
		    this.parsePanelBody(panelbody.value) +
			'</div>' +
			'';
		}
	} else if(panelbody.type == 'panelrows') {
		return '' +
	    '<div class="panel-body">' +
	    this.parsePanelBody(panelbody.value) +
		'</div>' +
		'';
	}
}

function parse(s) {
	let S = '';
    if (s.rows) {
      for (let i in s.rows) {
        S += this.createRow(s.rows[i]);
      }
    }
    if (s.columns) {
      for (let i in s.columns) {
        S += this.createColumn(s.columns[i]);
      }
    }
    if(s.tables){
    	for (let i in s.tables) {
			S += this.addInputTable(
				s.tables[i].width,
				s.tables[i].headers,
				s.tables[i].rows
			);
    	}

	}
    if(s.looptables){
    	for (let i in s.looptables) {
			S += this.addInputLoopTable(
				s.looptables[i].width,
				s.looptables[i].headers,
				s.looptables[i].paramname,
				s.looptables[i].rows
			);
    	}

	}
    if (s.breakline) {
        S += this.createBreakLine();
    }
    if (s.looprows) {
        for (let i in s.rows) {
          S += this.createRow(s.rows[i]);
        }
    }
    return S;
}

function createBreakLine() {
	return '' +
	'<br>' +
	'';
}

function createHeaderRow(r) {
	return '' +
    '<div class="panel-body">' +
    '<div class="row">' + this.parse(r) + '</div>' +
	'</div>' +
	'';
}


function createRow(r) {
	if (r.rowheader) {
		return '' +
		this.createHeaderRow(r);
		'';
	} else {
		return '' +
		'<div class="row">' + this.parse(r) + '</div>' +
		'';
	}
}

function createColumn(c) {
	if (c.type) {
		return '' +
		this.processFields(c);
		'';
	} else {
		return '' +
		'<div class="col-md-'+c.width+'">' + this.parse(c) + '</div>';
		'';
	}
}

function processFields(f) {
	switch (f.type) {
    case 'label':
    	return '' +
    	addInputlabel(f.width, f.labelname);
    	'';
    case 'text':
    	return '' +
    	addInputText(f.width, f.paramname, f.required);
    	'';
	case 'date':
		return '' +
		addInputDate(f.width, f.paramname, f.required);
		'';
	case 'select':
		return '' +
		addInputSelect(f.width, f.paramname, f.options, f.enableforroles);
		'';
	case 'textarea':
		return '' +
		addInputTextAreaWithLength(f.width, f.rows, f.paramname, f.length, f.required);
		'';
	case 'panellabel':
		return '' +
		addPanelLabel(f.width, f.labelname);
		'';
	}
}

function addInputlabel(width, name) {
	return '' +
	'<div class="col-md-'+width+'">' +
	'<label class="label-custom">'+name+'</label>' +
	'</div>' +
	'';
}

function addPanelLabel(width, name) {
	return '' +
	'<div class="col-md-'+width+'">' +
	name +
	'</div>' +
	'';
}

function addInputText(width, name, required) {
	addTextToViewModel(name, required);
	return '' +
	'<div class="col-md-'+width+'">' +
	'<input id="'+name+'" name="'+name+'" type="text" class="form-control" data-bind="value: '+name+'" >' +
	'</div>' +
	'';
}

function addInputDate(width, name, required) {
	addDateToViewModel(name, required);
	return '' +
	'<div class="col-md-'+width+'">' +
	'<div class="input-group date" id="datetimepicker" data-provide="datepicker" data-date-format="mm/dd/yyyy" data-date-autoclose="true" data-date-orientation = "bottom left">' +
	    '<input id="'+name+'" name="'+name+'" type="text" class="form-control" data-bind="value: '+name+'" >' +
	    '<div class="input-group-addon">' +
	        '<span class="glyphicon glyphicon-th"></span>' +
	    '</div>' +
	'</div>' +
	'</div>' +
	'';
}

function addInputSelect(width, name, options, enableforroles) {
	addSelectToViewModel(name);
	var optionsHtml="";
	for (var i=0; i<options.length;i++) {
		optionsHtml+= "<option value=" + options[i].optkey  + ">" +options[i].optval + "</option>"
	}
	/*return '' +
	'<div class="col-md-'+width+'">' +
	'<select id="'+name+'" name="'+name+'" class="form-control select-width-auto" data-bind="value: '+name+'" title="Please select">' +
	' '+ optionsHtml + ' ' +
	'</select>' +
	'</div>' +
	'';*/
	if (enableforroles) {
		return '' +
		'<div class="col-md-'+width+'">' +
		'<select id="'+name+'"' +
		'name="'+name+'"' +
		'class="form-control select-width-auto"' +
		'data-bind="enable: (currentUserRole() == '+"'ADMIN'"+' || currentUserRole() == '+"'SPECIALIST'"+'), value: '+name+'"' +
		'data-toggle="tooltip" title="Please select">' +
		' '+ optionsHtml +' ' +
		'</select>' +
		'</div>' +
		'';
	} else {
		return '' +
		'<div class="col-md-'+width+'">' +
		'<select id="'+name+'"' +
		'name="'+name+'"' +
		'class="form-control select-width-auto"' +
		'data-bind="value: '+name+'"' +
		'data-toggle="tooltip" title="Please select">' +
		' '+ optionsHtml +' ' +
		'</select>' +
		'</div>' +
		'';
	}
}

function addInputRadio(radioObj, name, trackingid, dependencyname) {
	var radioHtml= '';	
	// For dependency Annexure. Assuming to show annexure only for Yes Options
	if (dependencyname) {
		var dependencyFnName = 'chng'+dependencyname+'Visible';
		var dependencyObsName = 'show'+dependencyname+''+computedParamName;
		addDependencyFnToViewModel(dependencyFnName, dependencyObsName);
		addRadioAndDependencyToViewModel(name, trackingid, dependencyObsName);
		for (var i=0; i<radioObj.length; i++) {
			if (radioObj[i] == 'Yes') {
				radioHtml+= '<label class="btn btn-info notActive" data-bind="css: { active: '+name+'() == '+"'"+radioObj[i]+"'"+'}, validationOptions: { insertMessages: false} ">';
				radioHtml+= '<input type="radio" name='+name+' id='+name+' value="'+radioObj[i]+'" data-bind="checked: '+name+', click: function() {'+dependencyFnName+'(true)}">'+radioObj[i] ;
				radioHtml+= '</label>' ;
			} else {
				radioHtml+= '<label class="btn btn-info notActive" data-bind="css: { active: '+name+'() == '+"'"+radioObj[i]+"'"+'}, validationOptions: { insertMessages: false} ">';
				radioHtml+= '<input type="radio" name='+name+' id='+name+' value="'+radioObj[i]+'" data-bind="checked: '+name+', click: function() {'+dependencyFnName+'(false)}">'+radioObj[i] ;
				radioHtml+= '</label>' ;
			}
		}
	} else {
		addRadioToViewModel(name, trackingid);
		for (var i=0; i<radioObj.length; i++) {
			radioHtml+= '<label class="btn btn-info notActive" data-bind="css: { active: '+name+'() == '+"'"+radioObj[i]+"'"+'}, validationOptions: { insertMessages: false} ">';
			radioHtml+= '<input type="radio" name='+name+' id='+name+' value="'+radioObj[i]+'" data-bind="checked: '+name+'">'+radioObj[i] ;
			radioHtml+= '</label>' ;
		}
	}
	return ''+
		'<div id="custRadioBtn" class="btn-group">' +
		radioHtml+
		'</div>'+
		'';
}

function addInputTextAreaWithLength(width, rows, name, length, required){
	addTextAreaWithLengthToViewModel(name, required);
	return ''+
	'<div class="col-md-'+width+'">' +
        '<textarea maxlength='+length+'" id='+name+' name='+name+' rows="'+rows+'" class="form-control" data-bind="textInput: '+name+'"></textarea>' +
        //'(<span data-bind="text: '+name+'Remain()" style="color:blue"></span> characters remaining.)' +
    '</div>' +
	'';
}

function addInputTextArea(rows, name, trackingid){
	addTextAreaToViewModel(name, trackingid);
	return ''+
			'<textarea rows="'+rows+'" class="form-control" data-bind="value: '+name+'"></textarea>';
			'';
}

function addInputTable(columnWidths, headers, columnValues){

	var columnWidthsHtml = '';
	var headerHtml = '';
	var rowValuesHtml = '';

	for (var i=0; i<columnWidths.length;i++) {
		columnWidthsHtml+='<col class="col-md-'+columnWidths[i]+'">';
	}

	for (var i=0; i<headers.length;i++) {
		headerHtml+='<th>'+headers[i]+'</th>';
	}

	for (var i=0; i<columnValues.length; i++) {
		var trackingid;
		rowValuesHtml += '<tr>';
		for(var j=0; j<columnValues[i].length; j++){
			if(columnValues[i][j].type == 'text'){
				rowValuesHtml += '<td><p><span class="panel-span-medium">'+columnValues[i][j].value+'</span></p></td>';
			} if(columnValues[i][j].type == 'textandcomments'){
				trackingid = columnValues[i][j].id;
				rowValuesHtml += '<td><p><span class="panel-span-medium">'+columnValues[i][j].value+'</span></p>'+addCommentsAndTracking(columnValues[i][j].id)+'</td>';
			} else if (columnValues[i][j].type == 'radio'){
				rowValuesHtml += '<td>'+this.addInputRadio(columnValues[i][j].options, columnValues[i][j].paramname, trackingid, columnValues[i][j].dependencyname)+'</td>';
			} else if (columnValues[i][j].type == 'textarea'){
				rowValuesHtml += '<td>'+this.addInputTextArea(columnValues[i][j].rows, columnValues[i][j].paramname, trackingid)+'</td>';
			}
		}
		rowValuesHtml += '</tr>';
	}

	return ''+
		'<table class="table table-bordered">'+
		'<colgroup>'+
		 columnWidthsHtml+
		'</colgroup>'+
		'<thead>'+
		'<tr>'+
		 headerHtml+
		'</tr>'+
		'</thead>'+
		 rowValuesHtml+
		'</table>'+
		'';
}

function addInputLoopTable(columnWidths, headers, paramname, columnValues){

	addloopTableParamToViewModel(paramname);
	var columnWidthsHtml = '';
	var headerHtml = '';
	var rowValuesHtml = '';

	for (var i=0; i<columnWidths.length;i++) {
		columnWidthsHtml+='<col class="col-md-'+columnWidths[i]+'">';
	}

	for (var i=0; i<headers.length;i++) {
		headerHtml+='<th>'+headers[i]+'</th>';
	}

	rowValuesHtml += '<tbody data-bind="foreach: '+paramname+'">';
	for (var i=0; i<columnValues.length; i++) {
		rowValuesHtml += '<tr>';
		for(var j=0; j<columnValues[i].length; j++){
			if(columnValues[i][j].type == 'textarea'){
				rowValuesHtml += '<td><textarea rows="2" class="form-control" data-bind="value: '+columnValues[i][j].paramname+'"></textarea></td>';
			}
		}
		rowValuesHtml += '</tr>';
	}
	rowValuesHtml += '</tbody>';

	return ''+
		'<table class="table table-bordered">'+
		'<colgroup>'+
		 columnWidthsHtml+
		'</colgroup>'+
		'<thead>'+
		'<tr>'+
		 headerHtml+
		'</tr>'+
		'</thead>'+
		 rowValuesHtml+
		'</table>'+
		this.addButtons(paramname) +
		'';
}

function addButtons(paramname) {
	return ''+
	'<div class="col-md-2">' +
	'<button class="btn btn-primary" type="button" data-bind="click: addnew'+paramname+'">Add</button>' +
	'&nbsp;' +
	'<button class="btn btn-primary" type="button" data-bind="click: deletelast'+paramname+'">Delete</button>' +
	'</div>'+
	'';
}

function addCommentsAndTracking(id) {
	if(id == undefined) return '';
	addCommentsAndTrackingToViewModel(id);
	return '' +
	'<p>' +
    '<a class="link" data-toggle="collapse" href="#'+id+'ItemComments" role="button" aria-expanded="false" aria-controls="collapseExample" onclick="$('+"'#"+id+'ItemTracking'+"'"+').collapse('+"'hide'"+')" data-bind="click: function() { updateCommentViewed('+"'"+id+"'"+') }">' +
      '<i class="fa fa-comment-o"></i> Comments ' +
    '</a>' +
    '<span class="badge badge-light" data-bind="visible: '+id+'IsNewCommentForUser'+computedParamName+'">New</span>' +
    '&nbsp;| &nbsp;' +
    '<a class="link" data-toggle="collapse" href="#'+id+'ItemTracking" role="button" aria-expanded="false" aria-controls="collapseExample" onclick="$('+"'#"+id+'ItemComments'+"'"+').collapse('+"'hide'"+')">' +
      '<i class="fa fa-crosshairs"></i> Tracking' +
    '</a>' +
    '</p>' +
    
    '<div data-bind="visible: loadItemCommentsAndTrackingById('+"'"+id+"'"+') || true">' +

	    '<div class="collapse" id="'+id+'ItemComments">' +
		    '<div data-bind="foreach: '+id+'ItemCommentsData'+computedParamName+'">' +
			    '<div class="media">' +
			      '<div class="media-left">' +
			        '<a href="#">' +
			          '<i class="fa fa-user-circle-o"></i>' +
			        '</a>' +
			      '</div>' +
			      '<div class="media-body">' +
			        '<h5 class="media-heading"><b><span data-bind="text: commentBy"></span></b>&nbsp; <span data-bind="text: commentDate"></span></h5>' +
			        '<p><span data-bind="text: commentValue"></p>' +
			      '</div>' +
			    '</div>' +
		    '</div>' +
		    '<div class="media" style="margin-top: 0px;">' +
		        '<div class="media-left">' +
		          '<a href="#">' +
		            '&nbsp;&nbsp;&nbsp;&nbsp;' +
		          '</a>' +
		        '</div>' +
		        '<div class="media-body">' +
		          '<button class="btn btn-primary btn-sm" type="button" data-toggle="modal" data-target="#engFormModalCenter" data-bind="click: $root.addCommentId('+"'"+id+"'"+')">Add Comment</button>' +
		        '</div>' +
	        '</div>' +
	    '</div>' +

	    '<div class="collapse" id="'+id+'ItemTracking">' +
		    '<div class="card card-body">' +
		      '<table class="table table-sm">' +
		        '<colgroup>' +
		        '<col class="col-md-1">' +
		        '<col class="col-md-3">' +
		        '<col class="col-md-4">' +
		        '<col class="col-md-4">' +
		        '</colgroup>' +
		        '<thead style="color: #00A3AD; background-color:#FFFFFF ">' +
		          '<tr>' +
		            '<th scope="col">#</th>' +
		            '<th scope="col">Field</th>' +
		            '<th scope="col">Original Value</th>' +
		            '<th scope="col">New Value</th>' +
		          '</tr>' +
		        '</thead>' +
		        '<tbody>' +
		        '<!--ko foreach: '+id+'ItemTrackingData'+computedParamName+' --!>' +
		        '<td' +
		          '<tr>' +
		            '<th scope="row" colspan="3">' +
		              '<div class="media">' +
		                '<div class="media-left">' +
		                  '<a href="#">' +
		                    '<i class="fa fa-user-circle-o"></i>' +
		                  '</a>' +
		                '</div>' +
		                '<div class="media-body">' +
		                '<h5 class="media-heading"><b><span data-bind="text: trackingBy"></span></b>&nbsp; <span data-bind="text: trackingDate"></span></h5>' +
		                '</div>' +
		              '</div>' +
		            '</th>' +       
		          '</tr>' +
		          '<tr data-bind="foreach: trackingFieldValues">' +
		          	'<td>&nbsp;</td>' +
		            '<td><span data-bind="text: trackingField"></span></td>' +
		            '<td><span data-bind="text: trackingOrgValue"></span></td>' +
		            '<td><span data-bind="text: trackingNewValue"></span></td>' +
		          '</tr>' +
		        '</td>' +
		        '<!-- /ko --> '+
		        '</tbody>' +
		      '</table>' +
		    '</div>' +
	    '</div>' +
	    
	'</div>' +
	'';
}


function addModal() {
	return ''+
	'<div class="panel-heading">' +
    '<div class="modal fade" id="engFormModalCenter" tabindex="-1" role="dialog" aria-labelledby="engFormModalCenterTitle" aria-hidden="true">' +
      '<div class="modal-dialog modal-dialog-centered" role="document">' +
        '<div class="modal-content">' +
          '<div class="modal-header">' +
            '<h5 class="modal-title" id="exampleModalCenterTitle">Add New Comment</h5>' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
              '<span aria-hidden="true">&times;</span>' +
            '</button>' +
          '</div>' +
          '<div class="modal-body">' +
          '<form>' +
              '<div class="form-group">' +
                '<label for="message-text" class="col-form-label">Comment:</label>' +
                '<textarea class="form-control" id="message-text" data-bind="value : currentCommentValueComputed"></textarea>' +
              '</div>' +
            '</form>' +
          '</div>' +
          '<div class="modal-footer">' +
            '<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>' +
            '<button type="button" class="btn btn-primary" data-dismiss="modal" data-bind="click: updateComment">Add</button>' +
          '</div>' +
        '</div>' +
      '</div>' +
    '</div>' +
   '</div>' +
	'';
}

function addTextToViewModel(name, required) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		if (required) {
			this[dynamicViewModelName][name] = ko.observable(value).extend({required:true});
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
	} else {
		if (required) {
			this[dynamicViewModelName][name] = ko.observable().extend({required:true});
		} else {
			this[dynamicViewModelName][name] = ko.observable();
		}
	}
}

function addRadioToViewModel(name, trackingid) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		if(trackingid) {
			this[dynamicViewModelName][name] = ko.observable(value);
			trackingold = trackingid +"$"+"Radio"+"$"+"old"+"$"+computedParamName;
			this[dynamicViewModelName][trackingold] = ko.observable(value);
			tracking = trackingid +"$"+"Radio"+"$"+"createTracking"+"$"+computedParamName;
			this[dynamicViewModelName][tracking] = ko.computed(function() { 
				return this[dynamicViewModelName][name]();
			})
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
	} else {
		if(trackingid) {
			this[dynamicViewModelName][name] = ko.observable();
			trackingold = trackingid +"$"+"Radio"+"$"+"old"+"$"+computedParamName;
			this[dynamicViewModelName][trackingold] = ko.observable('');
			tracking = trackingid +"$"+"Radio"+"$"+"createTracking"+"$"+computedParamName;
			this[dynamicViewModelName][tracking] = ko.computed(function() { 
				return this[dynamicViewModelName][name]();
			})
		} else {
			this[dynamicViewModelName][name] = ko.observable();
		}
	}
}

function addTextAreaToViewModel(name, trackingid) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		if(trackingid) {
			this[dynamicViewModelName][name] = ko.observable(value);
			trackingold = trackingid +"$"+"Comment"+"$"+"old"+"$"+computedParamName;
			this[dynamicViewModelName][trackingold] = ko.observable(value);
			tracking = trackingid +"$"+"Comment"+"$"+"createTracking"+"$"+computedParamName;
			this[dynamicViewModelName][tracking] = ko.computed(function() { 
				return this[dynamicViewModelName][name]();
			})
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
	} else {
		if(trackingid) {
			this[dynamicViewModelName][name] = ko.observable();
			trackingold = trackingid +"$"+"Comment"+"$"+"old"+"$"+computedParamName;
			this[dynamicViewModelName][trackingold] = ko.observable('');
			tracking = trackingid +"$"+"Comment"+"$"+"createTracking"+"$"+computedParamName;
			this[dynamicViewModelName][tracking] = ko.computed(function() { 
				return this[dynamicViewModelName][name]();
			})
		} else {
			this[dynamicViewModelName][name] = ko.observable();
		}
	}
}

function addDateToViewModel(name, required) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		if (required) {
			this[dynamicViewModelName][name] = ko.observable(value).extend({required:true});
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
	} else {
		if (required) {
			this[dynamicViewModelName][name] = ko.observable().extend({required:true});
		} else {
			this[dynamicViewModelName][name] = ko.observable();
		}
	}
}

function addSelectToViewModel(name) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		this[dynamicViewModelName][name] = ko.observable(value);
	} else {
		this[dynamicViewModelName][name] = ko.observable();
	}
}

function addCommentsAndTrackingToViewModel(id) {
	var idCommentsArray = id+'ItemCommentsData'+computedParamName;
	var idTrackingArray = id+'ItemTrackingData'+computedParamName;
	var idIsNewCommentForUser = id+'IsNewCommentForUser'+computedParamName;
	this[dynamicViewModelName][idCommentsArray] = ko.observableArray([]);
	this[dynamicViewModelName][idTrackingArray] = ko.observableArray([]);
	this[dynamicViewModelName][idIsNewCommentForUser] = ko.observable(false);
}

function addTextAreaWithLengthToViewModel(name, required) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		if (required) {
			this[dynamicViewModelName][name] = ko.observable(value).extend({required:true});
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
	} else {
		if (required) {
			this[dynamicViewModelName][name] = ko.observable(value).extend({required:true});
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
	}
	var remainVal = name+'Remain';
	this[dynamicViewModelName][remainVal] = function() { 
    	if(window[dynamicViewModelName][name]()==null) return 500;
    	return 500-window[dynamicViewModelName][name]().length;
    }
}

function addloopTableParamToViewModel(name) {
	var value = '';
	if(engagementFormFormData != undefined) {
		var value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		this[dynamicViewModelName][name] = ko.observableArray(value);
	}
	
}


function addRadioAndDependencyToViewModel(name, trackingid, dependencyobs) {
	var value = '';
	if(engagementFormFormData != undefined) {
		value = engagementFormFormData[name];
	}
	if (value != null && value != '') {
		if(trackingid) {
			this[dynamicViewModelName][name] = ko.observable(value);
			trackingold = trackingid +"$"+"Radio"+"$"+"old"+"$"+computedParamName;
			this[dynamicViewModelName][trackingold] = ko.observable(value);
			tracking = trackingid +"$"+"Radio"+"$"+"createTracking"+"$"+computedParamName;
			this[dynamicViewModelName][tracking] = ko.computed(function() { 
				return this[dynamicViewModelName][name]();
			})
		} else {
			this[dynamicViewModelName][name] = ko.observable(value);
		}
		var isDependencySet = window[dynamicViewModelName][dependencyobs]();
		if (!isDependencySet) {
			if (value == 'Yes') {
				this[dynamicViewModelName][dependencyobs](true);
			} else {
				this[dynamicViewModelName][dependencyobs](false);
			}
		}
	} else {
		if(trackingid) {
			this[dynamicViewModelName][name] = ko.observable();
			trackingold = trackingid +"$"+"Radio"+"$"+"old"+"$"+computedParamName;
			this[dynamicViewModelName][trackingold] = ko.observable('');
			tracking = trackingid +"$"+"Radio"+"$"+"createTracking"+"$"+computedParamName;
			this[dynamicViewModelName][tracking] = ko.computed(function() { 
				return this[dynamicViewModelName][name]();
			})
		} else {
			this[dynamicViewModelName][name] = ko.observable();
		}
	}
	
}

function addDependencyFnToViewModel(dependencyfn, dependencyobs) {
	var isAlreadyDefined = (typeof ko.unwrap(this[dynamicViewModelName][dependencyobs]) !== 'undefined')
	if(!isAlreadyDefined) {
		this[dynamicViewModelName][dependencyobs] = ko.observable(false);
		this[dynamicViewModelName][dependencyfn] = function(val) { 
	    	window[dynamicViewModelName][dependencyobs](val);
	    }
	}
}

