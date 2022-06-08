var engagementId;
var engagementFormId;
var engagementData;
var sessEmpData;
var commentsData;
var trackingData;

var riskAssessmentViewModel = {
	
		currentCommentIdComputed: ko.observable(''),
		currentCommentValueComputed: ko.observable(''),
		currentUserRole: ko.observable(),
		engagementStatusName: ko.observable(),
		engagementStatusId: ko.observable(),
		engagementStatuses : ko.observable(),
		showGenerateCertificateButton: ko.observable(false),
		
		saveOrUpdateEngagement : function() {
			var readyToSubmit = riskAssessmentViewModel.checkErrors();
			if (readyToSubmit) {
				riskAssessmentViewModel.saveEngagementData();
			}
			alert("Engagement Form Successfully Updated");
		},
		
		saveEngagementData : function() {
			var jsonVal = ko.toJSON(riskAssessmentViewModel);
			var parsed = JSON.parse(jsonVal);
			//console.log(jsonVal);
			var trackingDataAll = [];
			$.each(parsed, function( index, value ) {
				if (index.indexOf("createTracking") > -1 ) {
					itemTrackingId = index.substring(0, index.indexOf("$"));
					indexRemain1 = index.substring(index.indexOf("$")+1, index.length);
					itemTrackingField = indexRemain1.substring(0, indexRemain1.indexOf("$"));
					trackingoldname = index.replace("createTracking","old");
					oldValue = riskAssessmentViewModel[trackingoldname]();
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
				        riskAssessmentViewModel[trackingoldname](newValue);
					}
				}
				if (index.indexOf("Computed") > -1 
						|| index.indexOf("error") > -1 
						|| index.indexOf("currentUserRole") > -1 
						|| index.indexOf("engagementStatus") > -1) {
					delete parsed[index];
				}
			});
			riskAssessmentViewModel.updateEngagementStatus(engagementId);
			riskAssessmentViewModel.updateEngagementForm(engagementId, ko.toJSON(parsed));
			trackingDataReload = createTracking(JSON.stringify(trackingDataAll));
			riskAssessmentViewModel.reloadTrackingData(trackingDataReload);
		},
		
		loadItemCommentsAndTrackingById: function(id) {
			var commentsForItem = $.map(commentsData, function (commentVal) {
				if (id == commentVal.commentItem) {
					if (sessEmpData.isInfosecUser == "Y") {
						if (commentVal.infosecViewed == "N") {
							riskAssessmentViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					} else {
						if (commentVal.requestorViewed == "N") {
							riskAssessmentViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					}
					return new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
				}
            });
			riskAssessmentViewModel[id+'ItemCommentsData'+computedParamName](commentsForItem);
			
			var trackingForItem = $.map(trackingData, function (trackingVal) {
				if (id == trackingVal.trackingItem) {
					return new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				}
            });
			riskAssessmentViewModel[id+'ItemTrackingData'+computedParamName](trackingForItem);
		},
		
		reloadCommentData: function(commentData, ItemCommentId) {
			var myMap = new Map();
			$.map(commentData, function (commentVal) {
				if (ItemCommentId == commentVal.commentItem) {
					var reloadcommentval = new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
					var id = commentVal.commentItem;
					if (myMap.get(id) == null) {
						riskAssessmentViewModel[id+'ItemCommentsData'+computedParamName]([]);
						myMap.set(id,id);
					}
					riskAssessmentViewModel[id+'ItemCommentsData'+computedParamName].push(reloadcommentval);
				}
            });
		},
		
		reloadTrackingData: function(trackingData) {
			var myMap = new Map();
			$.map(trackingData, function (trackingVal) {
				var reloadtrackingval = new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				var id = trackingVal.trackingItem;
				if (myMap.get(id) == null) {
					riskAssessmentViewModel[id+'ItemTrackingData'+computedParamName]([]);
					myMap.set(id,id);
				}
				riskAssessmentViewModel[id+'ItemTrackingData'+computedParamName].push(reloadtrackingval);
            });
		},
		
		checkErrors: function() {
	        if (riskAssessmentViewModel.errors().length === 0) {
	            return true;
	        }
	        else {
	            alert('Please check the errors.');
	            riskAssessmentViewModel.errors.showAllMessages();
	            return false;
	        }
	    },
		
		addCommentId: function(id) {
			riskAssessmentViewModel.currentCommentIdComputed(id);
		},
		
		updateComment: function() {
			//alert(riskAssessmentViewModel.currentCommentIdComputed());
			//alert(riskAssessmentViewModel.currentCommentValueComputed());
			var ItemCommentId = riskAssessmentViewModel.currentCommentIdComputed();
			var ItemCommentVal = riskAssessmentViewModel.currentCommentValueComputed();
			riskAssessmentViewModel.currentCommentIdComputed('');
			riskAssessmentViewModel.currentCommentValueComputed('');
			var engagementFormCommentsBody = JSON.stringify({
				"engagementId": engagementId,
				"engagementFormId": engagementFormId,
	            "commentBy": "Prasant Lingamaneni",
	            "commentItem": ItemCommentId,
	            "commentDate": new Date(),
	            "comment": ItemCommentVal,
	            "infosecViewed": (sessEmpData.isInfosecUser == "Y")?"Y":"N",
	    	    "requestorViewed": (sessEmpData.isInfosecUser == "Y")?"N":"Y"
	        });
			commentsData = createComment(engagementFormCommentsBody);
			riskAssessmentViewModel.reloadCommentData(commentsData, ItemCommentId);
		},
		
		updateCommentViewed: function(id) {
			if (riskAssessmentViewModel[id+'IsNewCommentForUser'+computedParamName]()) {
				var isInfosecUser = (sessEmpData.isInfosecUser == "Y")?"Y":"N";
				updateCommentViewed(engagementId, engagementFormId, id, isInfosecUser);
				riskAssessmentViewModel[id+'IsNewCommentForUser'+computedParamName](false);
			}
		},


		updateEngagementStatus: function (engagementId){
			// json body
			engagementData.engagementStatus = getNameFromOptions(riskAssessmentViewModel.engagementStatusId(), engagementStatusOptions);
			if(engagementData.engagementStatus == 'CLOSED' || engagementData.engagementStatus=='CANCELLED'){
				engagementData.completedDatetime=+new Date();
			}
			var engagementBody = JSON.stringify(engagementData);
			updateEngagement(engagementId, engagementBody);
		},


	updateEngagementForm: function (engagementId, jsonData){
	        var engagementFormBody = JSON.stringify({
	            "engagementFormName": "RISK_PRE_APPROVAL_FORM",
	            "formData": jsonData
	        });
	        updateEngagementForm(engagementId, "RISK_PRE_APPROVAL_FORM", engagementFormBody);
	    },

	    navigateTo: function (data, event){
	    	var readyToSubmit = riskAssessmentViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	riskAssessmentViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace($(event.target).attr('value')+"?engagementId=" + engagementId);
	        } else {
	            window.location.replace($(event.target).attr('value'));
	        }
	    },

		uploadedFileList: ko.observableArray([]),
		getUploadedFilesList: function() {
			riskAssessmentViewModel.uploadedFileList.removeAll();
			var fileList = getUploadedFilesForEngagement(engagementId, "RISK_PRE_APPROVAL_FORM");
			for(var i= 0; i<fileList.length; i++) {
				riskAssessmentViewModel.uploadedFileList.push(fileList[i]);
			}
		},
		downloadUploadedFile : function(data, event) {
			window.location = "/infosec-api/engagements/"+engagementId+"/engagement-forms/"+"RISK_PRE_APPROVAL_FORM"+"/file/"+data;
		},

		downloadZipFile: function() {
			window.location = "/infosec-api/engagements/"+engagementId+"/file";
		},

		checkEngagementStatus : function(obj, event) {
			if(getNameFromOptions(obj.engagementStatusId(), engagementStatusOptions)=='CLOSED'){
				riskAssessmentViewModel.showGenerateCertificateButton(true);
			} else {
				riskAssessmentViewModel.showGenerateCertificateButton(false);
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

	riskAssessmentViewModel.engagementStatuses(engagementStatusOptions);

	engagementId = getURLParamEngagementIdValue();
    var engagementFormData = null;
    
    if(engagementId!=null) {
        // go and fetch the form data for the given engagement id
    	sessEmpData =  getSessionEmpData();
        engagementData = getEngagement(engagementId);
        engagementFormData = getEngagementForm(engagementId, "RISK_PRE_APPROVAL_FORM");
        engagementFormId = engagementFormData.engagementFormId;
        commentsData = getComments(engagementId, engagementFormId);
        trackingData = getTracking(engagementId, engagementFormId);
        //Get Form Template and Data
        var engagementFormFormTemplate = engagementFormData.formTemplate;
        var engagementFormFormData = engagementFormData.formData;
        // processing dynamic generation
        processEngagementFormJson(JSON.parse(engagementFormFormTemplate), JSON.parse(engagementFormFormData));
		riskAssessmentViewModel.engagementStatusId(getIdFromOptions(engagementData.engagementStatus, engagementStatusOptions));
		riskAssessmentViewModel.engagementStatusName(engagementData.engagementStatus);
		if(engagementData.engagementStatus == 'CLOSED'){
			riskAssessmentViewModel.showGenerateCertificateButton(true);
		}
		riskAssessmentViewModel.currentUserRole(sessEmpData.userRole);

		//get uploaded files list
		riskAssessmentViewModel.getUploadedFilesList();
    }
    
}

function processEngagementFormJson(jsontemplate, jsondata) {
	//alert(ko.toJSON(engagementData))
	//alert(ko.toJSON(json))
	parseJsonToBind(jsontemplate, jsondata, "riskAssessmentViewModel");
	ko.applyBindings(riskAssessmentViewModel);
	riskAssessmentViewModel.errors = ko.validation.group(riskAssessmentViewModel);
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
			url: '/infosec-api/engagements/'+engagementId+'/engagement-forms/RISK_PRE_APPROVAL_FORM/file',
			data: data,
			success: function () {
				alert('File Uploaded Successfully');
				// update file list
				riskAssessmentViewModel.getUploadedFilesList();
			},
			error: function(error){
				alert('Error While Uploading File');
			}
		});
	});

});

init();
