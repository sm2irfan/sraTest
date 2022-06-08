var engagementId;
var engagementFormId;
var engagementData;
var sessEmpData;
var commentsData;
var trackingData;

var engagementFormSummaryViewModel = {
		
		currentCommentIdComputed: ko.observable(''),
		currentCommentValueComputed: ko.observable(''),
		showStaticInput: ko.observable(false),
		currentUserRole: ko.observable(),

		engagementStatusName: ko.observable(),
		engagementStatusId: ko.observable(),
		engagementStatuses : ko.observable(),
		
		projectName: ko.observable(),
	    engagementDate: ko.observable(),
	    requestor: ko.observable(),
	    infosecResource: ko.observable(),
	    infosecResources: ko.observable(),
		showGenerateCertificateButton: ko.observable(false),
	
		uploadedFileList: ko.observableArray([]),
		getUploadedFilesList: function() {
			engagementFormSummaryViewModel.uploadedFileList.removeAll();
			var fileList = getUploadedFilesForEngagement(engagementId, "SUMMARY");
			for(var i= 0; i<fileList.length; i++) {
				engagementFormSummaryViewModel.uploadedFileList.push(fileList[i]);
			}
		},
		
		saveOrUpdateEngagement : function() {
			var readyToSubmit = engagementFormSummaryViewModel.checkErrors();
			if (readyToSubmit) {
				engagementFormSummaryViewModel.saveEngagementData();
			}
			alert("Engagement Form Successfully Updated");
		},

		saveEngagementData : function() {
			//startSpinner();
			var jsonVal = ko.toJSON(engagementFormSummaryViewModel);
			var parsed = JSON.parse(jsonVal);
			//console.log(jsonVal);
			var trackingDataAll = [];
			$.each(parsed, function( index, value ) {
				if (index.indexOf("createTracking") > -1 ) {
					itemTrackingId = index.substring(0, index.indexOf("$"));
					indexRemain1 = index.substring(index.indexOf("$")+1, index.length);
					itemTrackingField = indexRemain1.substring(0, indexRemain1.indexOf("$"));
					trackingoldname = index.replace("createTracking","old");
					oldValue = engagementFormSummaryViewModel[trackingoldname]();
					newValue = value;
					if (oldValue != 'undefined' && newValue != 'undefined' && oldValue != newValue) {
						trackingDataAll.push({
							"engagementId": engagementId,
							"engagementFormId": engagementFormId,
				            "trackingUser": sessEmpData.firstName + " " +sessEmpData.lastName,
				            "trackingItem": itemTrackingId,
				            "trackingDate": new Date(),
				            "trackingField": itemTrackingField,
				            "oldValue": oldValue,
				            "newValue": newValue
				        })
				        engagementFormSummaryViewModel[trackingoldname](newValue);
					}
				}
				if (index.indexOf("Computed") > -1 
						|| index.indexOf("error") > -1 
						|| index.indexOf("currentUserRole") > -1 
						|| index.indexOf("infosecResources") > -1 
						|| index.indexOf("engagementStatus") > -1 
						|| index.indexOf("showStaticInput") > -1 ) {
					delete parsed[index];
				}
			});
			engagementFormSummaryViewModel.updateEngagement(engagementId);
			engagementFormSummaryViewModel.updateEngagementForm(engagementId, ko.toJSON(parsed));
			trackingDataReload = createTracking(JSON.stringify(trackingDataAll));
			engagementFormSummaryViewModel.reloadTrackingData(trackingDataReload);
		//	stopSpinner();
		},
		
		loadItemCommentsAndTrackingById: function(id) {
			var commentsForItem = $.map(commentsData, function (commentVal) {
				if (id == commentVal.commentItem) {
					if (sessEmpData.isInfosecUser == "Y") {
						if (commentVal.infosecViewed == "N") {
							engagementFormSummaryViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					} else {
						if (commentVal.requestorViewed == "N") {
							engagementFormSummaryViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					}
					return new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
				}
            });
			engagementFormSummaryViewModel[id+'ItemCommentsData'+computedParamName](commentsForItem);
			
			var trackingForItem = $.map(trackingData, function (trackingVal) {
				if (id == trackingVal.trackingItem) {
					return new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				}
            });
			engagementFormSummaryViewModel[id+'ItemTrackingData'+computedParamName](trackingForItem);
		},
		
		reloadCommentData: function(commentData, ItemCommentId) {
			var myMap = new Map();
			$.map(commentData, function (commentVal) {
				if (ItemCommentId == commentVal.commentItem) {
					var reloadcommentval = new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
					var id = commentVal.commentItem;
					if (myMap.get(id) == null) {
						engagementFormSummaryViewModel[id+'ItemCommentsData'+computedParamName]([]);
						myMap.set(id,id);
					}
					engagementFormSummaryViewModel[id+'ItemCommentsData'+computedParamName].push(reloadcommentval);
				}
            });
		},
		
		reloadTrackingData: function(trackingData) {
			var myMap = new Map();
			$.map(trackingData, function (trackingVal) {
				var reloadtrackingval = new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				var id = trackingVal.trackingItem;
				if (myMap.get(id) == null) {
					engagementFormSummaryViewModel[id+'ItemTrackingData'+computedParamName]([]);
					myMap.set(id,id);
				}
				engagementFormSummaryViewModel[id+'ItemTrackingData'+computedParamName].push(reloadtrackingval);
            });
		},
		
		checkErrors: function() {
	        if (engagementFormSummaryViewModel.errors().length === 0) {
	            return true;
	        }
	        else {
	            alert('Please check the errors.');
	            engagementFormSummaryViewModel.errors.showAllMessages();
	            return false;
	        }
	    },
		
		addCommentId: function(id) {
			engagementFormSummaryViewModel.currentCommentIdComputed(id);
		},
		
		updateComment: function() {
			//alert(engagementFormSummaryViewModel.currentCommentIdComputed());
			//alert(engagementFormSummaryViewModel.currentCommentValueComputed());
			var ItemCommentId = engagementFormSummaryViewModel.currentCommentIdComputed();
			var ItemCommentVal = engagementFormSummaryViewModel.currentCommentValueComputed();
			engagementFormSummaryViewModel.currentCommentIdComputed('');
			engagementFormSummaryViewModel.currentCommentValueComputed('');
			var engagementFormCommentsBody = JSON.stringify({
				"engagementId": engagementId,
				"engagementFormId": engagementFormId,
	            "commentBy": sessEmpData.firstName + " " +sessEmpData.lastName,
	            "commentItem": ItemCommentId,
	            "commentDate": new Date(),
	            "comment": ItemCommentVal,
	            "infosecViewed": (sessEmpData.isInfosecUser == "Y")?"Y":"N",
	            "requestorViewed": (sessEmpData.isInfosecUser == "Y")?"N":"Y"
	        });
			commentsData = createComment(engagementFormCommentsBody);
			engagementFormSummaryViewModel.reloadCommentData(commentsData, ItemCommentId);
		},
		
		updateCommentViewed: function(id) {
			if (engagementFormSummaryViewModel[id+'IsNewCommentForUser'+computedParamName]()) {
				var isInfosecUser = (sessEmpData.isInfosecUser == "Y")?"Y":"N";
				updateCommentViewed(engagementId, engagementFormId, id, isInfosecUser);
				engagementFormSummaryViewModel[id+'IsNewCommentForUser'+computedParamName](false);
			}
		},
	
	    updateEngagement: function (engagementId){
	        // json body
			engagementData.engagementStatus = getNameFromOptions(engagementFormSummaryViewModel.engagementStatusId(), engagementStatusOptions);
			engagementData.assignedTo = engagementFormSummaryViewModel.infosecResource();
			if(engagementData.engagementStatus == 'CLOSED' || engagementData.engagementStatus=='CANCELLED'){
				engagementData.completedDatetime=+new Date();
			}
			var engagementBody = JSON.stringify(engagementData);
	        updateEngagement(engagementId, engagementBody);
	    },
	
	    updateEngagementForm: function (engagementId, jsonData){
	        var engagementFormBody = JSON.stringify({
	            "engagementFormName": "SUMMARY",
	            "formData": jsonData
	        });
	        updateEngagementForm(engagementId, "SUMMARY", engagementFormBody);
	    },
	
	    goNext: function () {
	    	var readyToSubmit = engagementFormSummaryViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormSummaryViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormSolutionReview.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormSolutionReview.html");
	        }
	    },
	
	    navigateTo: function (data, event){
	    	var readyToSubmit = engagementFormSummaryViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormSummaryViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace($(event.target).attr('value')+"?engagementId=" + engagementId);
	        } else {
	            window.location.replace($(event.target).attr('value'));
	        }
	    },

		downloadUploadedFile : function(data, event) {
			window.location = "/infosec-api/engagements/"+engagementId+"/engagement-forms/"+"SUMMARY"+"/file/"+data;
		},

		downloadZipFile: function() {
			window.location = "/infosec-api/engagements/"+engagementId+"/file";
		},

		submitEngagement : function() {

			if(engagementId!=null) {
				engagementFormSummaryViewModel.engagementStatusId(getIdFromOptions('NEW', engagementStatusOptions));
				//engagementData.engagementStatus = "NEW";
				engagementData.requestedDatetime = +new Date();
				engagementFormSummaryViewModel.saveEngagementData();
				window.location.replace("engagementList.html");
			}
			else {
				alert("Please Create An Engagement First");
			}
		},

		checkEngagementStatus : function(obj, event) {
			if(getNameFromOptions(obj.engagementStatusId(), engagementStatusOptions)=='CLOSED'){
				engagementFormSummaryViewModel.showGenerateCertificateButton(true);
			} else {
				engagementFormSummaryViewModel.showGenerateCertificateButton(false);
			}
		},

		generateEngagementCertificate : function() {
			if(generateEngagementCertificate(engagementId)!=null){
				alert("Certificate Successfully Generated");
			}
		}
};



function TrackingData(trackingBy, trackingDate, trackingFieldValues) {
	this.trackingBy = ko.observable(trackingBy);
	this.trackingDate = ko.observable(trackingDate);
	this.trackingFieldValues = ko.observableArray(trackingFieldValues);
}

function TrackingFieldValues(trackingField, trackingOrgValue, trackingNewValue) {
	this.trackingField = ko.observable(trackingField);
	this.trackingOrgValue = ko.observable(trackingOrgValue);
	this.trackingNewValue = ko.observable(trackingNewValue);
}

function CommentData(commentBy, commentDate, commentValue) {
	this.commentBy = ko.observable(commentBy);
	this.commentDate = ko.observable(commentDate);
	this.commentValue = ko.observable(commentValue);
}


function init() {

	//startSpinner();

	engagementFormSummaryViewModel.engagementStatuses(engagementStatusOptions);

	engagementId = getURLParamEngagementIdValue();
    var engagementFormData = null;
    
    if(engagementId!=null) {
        // go and fetch the form data for the given engagement id
    	
    	sessEmpData =  getSessionEmpData();
    	var infosecUsersList = getInfosecUsers();
        engagementData = getEngagement(engagementId);
        engagementFormData = getEngagementForm(engagementId, "SUMMARY");
        engagementFormId = engagementFormData.engagementFormId;
        commentsData = getComments(engagementId, engagementFormId);
        trackingData = getTracking(engagementId, engagementFormId);
        //Get Form Template and Data
        var engagementFormFormTemplate = engagementFormData.formTemplate;
        var engagementFormFormData = engagementFormData.formData;
        // processing dynamic generation
        processEngagementFormJson(JSON.parse(engagementFormFormTemplate), JSON.parse(engagementFormFormData));
        
        // Applying bindings after viewmodel initialization
		infosecUsersList.splice(0, 0, {"lastName" : "", "firstName" : ""});
        engagementFormSummaryViewModel.infosecResources(infosecUsersList);
        engagementFormSummaryViewModel.engagementStatusId(getIdFromOptions(engagementData.engagementStatus, engagementStatusOptions));
		if(engagementData.engagementStatus == 'CLOSED'){
			engagementFormSummaryViewModel.showGenerateCertificateButton(true);
		}
		engagementFormSummaryViewModel.engagementStatusName(engagementData.engagementStatus);
        engagementFormSummaryViewModel.currentUserRole(sessEmpData.userRole);
        engagementFormSummaryViewModel.projectName(engagementData.projectName);
        engagementFormSummaryViewModel.engagementDate(engagementData.requestedDatetime!=null?timeConverter(+engagementData.requestedDatetime):null);
        engagementFormSummaryViewModel.requestor(engagementData.requestedBy);
		engagementFormSummaryViewModel.infosecResource(engagementData.assignedTo);

        engagementFormSummaryViewModel.showStaticInput(true);

		//get uploaded files list
		engagementFormSummaryViewModel.getUploadedFilesList();

		//stopSpinner();
		//$(':input').prop('disabled',true);
    }
}

function processEngagementFormJson(jsontemplate, jsondata) {
	//alert(ko.toJSON(engagementData))
	//alert(ko.toJSON(json))
	parseJsonToBind(jsontemplate, jsondata, "engagementFormSummaryViewModel");
	ko.applyBindings(engagementFormSummaryViewModel);
	engagementFormSummaryViewModel.errors = ko.validation.group(engagementFormSummaryViewModel);
}

// file form submit
$(function () {
	$('form#fileUpload').on('submit', function (e) {
		e.preventDefault();
		// Get form
		var form = $('form#fileUpload')[0];
		// Create an FormData object
		var data = new FormData(form);
		$.ajax({
			type: 'post',
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			cache: false,
			url: '/infosec-api/engagements/'+engagementId+'/engagement-forms/SUMMARY/file',
			data: data,
			success: function () {
				alert('File Uploaded Successfully');
				// update file list
				engagementFormSummaryViewModel.getUploadedFilesList();
			},
			error: function(error){
				alert('Error While Uploading File');
			}
		});
	});

});

$(function () {
	$('#datetimepicker').datepicker({
		startDate:new Date()
	});
});

init();