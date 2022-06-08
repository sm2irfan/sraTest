var engagementId;
var engagementFormId;
var engagementData;
var sessEmpData;
var commentsData;
var trackingData;

var engagementFormOffWrkGrpApprovalViewModel = {
	
		currentCommentIdComputed: ko.observable(''),
		currentCommentValueComputed: ko.observable(''),
		currentUserRole: ko.observable(),

		engagementStatusName: ko.observable(),
		engagementStatusId: ko.observable(),
		engagementStatuses : ko.observable(),
		showGenerateCertificateButton: ko.observable(false),
		
		saveOrUpdateEngagement : function() {
			var readyToSubmit = engagementFormOffWrkGrpApprovalViewModel.checkErrors();
			if (readyToSubmit) {
				engagementFormOffWrkGrpApprovalViewModel.saveEngagementData();
			}
			alert("Engagement Form Successfully Updated");
		},
		
		saveEngagementData : function() {
			var jsonVal = ko.toJSON(engagementFormOffWrkGrpApprovalViewModel);
			var parsed = JSON.parse(jsonVal);
			//console.log(jsonVal);
			var trackingDataAll = [];
			$.each(parsed, function( index, value ) {
				if (index.indexOf("createTracking") > -1 ) {
					itemTrackingId = index.substring(0, index.indexOf("$"));
					indexRemain1 = index.substring(index.indexOf("$")+1, index.length);
					itemTrackingField = indexRemain1.substring(0, indexRemain1.indexOf("$"));
					trackingoldname = index.replace("createTracking","old");
					oldValue = engagementFormOffWrkGrpApprovalViewModel[trackingoldname]();
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
				        engagementFormOffWrkGrpApprovalViewModel[trackingoldname](newValue);
					}
				}
				if (index.indexOf("Computed") > -1 
						|| index.indexOf("error") > -1 
						|| index.indexOf("currentUserRole") > -1 
						|| index.indexOf("engagementStatus") > -1) {
					delete parsed[index];
				}
			});
			engagementFormOffWrkGrpApprovalViewModel.updateEngagementStatus(engagementId);
			engagementFormOffWrkGrpApprovalViewModel.updateEngagementForm(engagementId, ko.toJSON(parsed));
			trackingDataReload = createTracking(JSON.stringify(trackingDataAll));
			engagementFormOffWrkGrpApprovalViewModel.reloadTrackingData(trackingDataReload);
		},
		
		loadItemCommentsAndTrackingById: function(id) {
			var commentsForItem = $.map(commentsData, function (commentVal) {
				if (id == commentVal.commentItem) {
					if (sessEmpData.isInfosecUser == "Y") {
						if (commentVal.infosecViewed == "N") {
							engagementFormOffWrkGrpApprovalViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					} else {
						if (commentVal.requestorViewed == "N") {
							engagementFormOffWrkGrpApprovalViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					}
					return new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
				}
            });
			engagementFormOffWrkGrpApprovalViewModel[id+'ItemCommentsData'+computedParamName](commentsForItem);
			
			var trackingForItem = $.map(trackingData, function (trackingVal) {
				if (id == trackingVal.trackingItem) {
					return new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				}
            });
			engagementFormOffWrkGrpApprovalViewModel[id+'ItemTrackingData'+computedParamName](trackingForItem);
		},
		
		reloadCommentData: function(commentData, ItemCommentId) {
			var myMap = new Map();
			$.map(commentData, function (commentVal) {
				if (ItemCommentId == commentVal.commentItem) {
					var reloadcommentval = new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
					var id = commentVal.commentItem;
					if (myMap.get(id) == null) {
						engagementFormOffWrkGrpApprovalViewModel[id+'ItemCommentsData'+computedParamName]([]);
						myMap.set(id,id);
					}
					engagementFormOffWrkGrpApprovalViewModel[id+'ItemCommentsData'+computedParamName].push(reloadcommentval);
				}
            });
		},
		
		reloadTrackingData: function(trackingData) {
			var myMap = new Map();
			$.map(trackingData, function (trackingVal) {
				var reloadtrackingval = new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				var id = trackingVal.trackingItem;
				if (myMap.get(id) == null) {
					engagementFormOffWrkGrpApprovalViewModel[id+'ItemTrackingData'+computedParamName]([]);
					myMap.set(id,id);
				}
				engagementFormOffWrkGrpApprovalViewModel[id+'ItemTrackingData'+computedParamName].push(reloadtrackingval);
            });
		},
		
		checkErrors: function() {
	        if (engagementFormOffWrkGrpApprovalViewModel.errors().length === 0) {
	            return true;
	        }
	        else {
	            alert('Please check the errors.');
	            engagementFormOffWrkGrpApprovalViewModel.errors.showAllMessages();
	            return false;
	        }
	    },
		
		addCommentId: function(id) {
			engagementFormOffWrkGrpApprovalViewModel.currentCommentIdComputed(id);
		},
		
		updateComment: function() {
			//alert(engagementFormOffWrkGrpApprovalViewModel.currentCommentIdComputed());
			//alert(engagementFormOffWrkGrpApprovalViewModel.currentCommentValueComputed());
			var ItemCommentId = engagementFormOffWrkGrpApprovalViewModel.currentCommentIdComputed();
			var ItemCommentVal = engagementFormOffWrkGrpApprovalViewModel.currentCommentValueComputed();
			engagementFormOffWrkGrpApprovalViewModel.currentCommentIdComputed('');
			engagementFormOffWrkGrpApprovalViewModel.currentCommentValueComputed('');
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
			engagementFormOffWrkGrpApprovalViewModel.reloadCommentData(commentsData, ItemCommentId);
		},
		
		updateCommentViewed: function(id) {
			if (engagementFormOffWrkGrpApprovalViewModel[id+'IsNewCommentForUser'+computedParamName]()) {
				var isInfosecUser = (sessEmpData.isInfosecUser == "Y")?"Y":"N";
				updateCommentViewed(engagementId, engagementFormId, id, isInfosecUser);
				engagementFormOffWrkGrpApprovalViewModel[id+'IsNewCommentForUser'+computedParamName](false);
			}
		},


		updateEngagementStatus: function (engagementId){
			// json body
			engagementData.engagementStatus = getNameFromOptions(engagementFormOffWrkGrpApprovalViewModel.engagementStatusId(), engagementStatusOptions);
			if(engagementData.engagementStatus == 'CLOSED' || engagementData.engagementStatus=='CANCELLED'){
				engagementData.completedDatetime=+new Date();
			}
			var engagementBody = JSON.stringify(engagementData);
			updateEngagement(engagementId, engagementBody);
		},
	
	    updateEngagementForm: function (engagementId, jsonData){
	        var engagementFormBody = JSON.stringify({
	            "engagementFormName": "OWG",
	            "formData": jsonData
	        });
	        updateEngagementForm(engagementId, "OWG", engagementFormBody);
	    },

	    goNext: function () {
	    	var readyToSubmit = engagementFormOffWrkGrpApprovalViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormOffWrkGrpApprovalViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormAppPfSecurity.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormAppPfSecurity.html");
	        }
	    },

	    goPrev: function () {
	    	var readyToSubmit = engagementFormOffWrkGrpApprovalViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormOffWrkGrpApprovalViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormEntRepDatabase.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormEntRepDatabase.html");
	        }
	    },

	    navigateTo: function (data, event){
	    	var readyToSubmit = engagementFormOffWrkGrpApprovalViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormOffWrkGrpApprovalViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace($(event.target).attr('value')+"?engagementId=" + engagementId);
	        } else {
	            window.location.replace($(event.target).attr('value'));
	        }
	    },

		uploadedFileList: ko.observableArray([]),
		getUploadedFilesList: function() {
			engagementFormOffWrkGrpApprovalViewModel.uploadedFileList.removeAll();
			var fileList = getUploadedFilesForEngagement(engagementId, "OWG");
			for(var i= 0; i<fileList.length; i++) {
				engagementFormOffWrkGrpApprovalViewModel.uploadedFileList.push(fileList[i]);
			}
		},
		downloadUploadedFile : function(data, event) {
			window.location = "/infosec-api/engagements/"+engagementId+"/engagement-forms/"+"OWG"+"/file/"+data;
		},

		downloadZipFile: function() {
			window.location = "/infosec-api/engagements/"+engagementId+"/file";
		},

		submitEngagement : function() {

			if(engagementId!=null) {
				engagementFormOffWrkGrpApprovalViewModel.engagementStatusId(getIdFromOptions('NEW', engagementStatusOptions));
				engagementData.requestedDatetime = +new Date();
				engagementFormOffWrkGrpApprovalViewModel.saveEngagementData();
				window.location.replace("engagementList.html");
			}
			else {
				alert("Please Create An Engagement First");
			}
		},

		checkEngagementStatus : function(obj, event) {
			if(getNameFromOptions(obj.engagementStatusId(), engagementStatusOptions)=='CLOSED'){
				engagementFormOffWrkGrpApprovalViewModel.showGenerateCertificateButton(true);
			} else {
				engagementFormOffWrkGrpApprovalViewModel.showGenerateCertificateButton(false);
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

	engagementFormOffWrkGrpApprovalViewModel.engagementStatuses(engagementStatusOptions);

	engagementId = getURLParamEngagementIdValue();
    var engagementFormData = null;
    
    if(engagementId!=null) {
        // go and fetch the form data for the given engagement id
    	sessEmpData =  getSessionEmpData();
        engagementData = getEngagement(engagementId);
        engagementFormData = getEngagementForm(engagementId, "OWG");
        engagementFormId = engagementFormData.engagementFormId;
        commentsData = getComments(engagementId, engagementFormId);
        trackingData = getTracking(engagementId, engagementFormId);
        //Get Form Template and Data
        var engagementFormFormTemplate = engagementFormData.formTemplate;
        var engagementFormFormData = engagementFormData.formData;
        // processing dynamic generation
        processEngagementFormJson(JSON.parse(engagementFormFormTemplate), JSON.parse(engagementFormFormData));

		engagementFormOffWrkGrpApprovalViewModel.engagementStatusId(getIdFromOptions(engagementData.engagementStatus, engagementStatusOptions));
		engagementFormOffWrkGrpApprovalViewModel.engagementStatusName(engagementData.engagementStatus);
		if(engagementData.engagementStatus == 'CLOSED'){
			engagementFormOffWrkGrpApprovalViewModel.showGenerateCertificateButton(true);
		}
        engagementFormOffWrkGrpApprovalViewModel.currentUserRole(sessEmpData.userRole);

		//get uploaded files list
		engagementFormOffWrkGrpApprovalViewModel.getUploadedFilesList();
    }
    
}

function processEngagementFormJson(jsontemplate, jsondata) {
	//alert(ko.toJSON(engagementData))
	//alert(ko.toJSON(json))
	parseJsonToBind(jsontemplate, jsondata, "engagementFormOffWrkGrpApprovalViewModel");
	ko.applyBindings(engagementFormOffWrkGrpApprovalViewModel);
	engagementFormOffWrkGrpApprovalViewModel.errors = ko.validation.group(engagementFormOffWrkGrpApprovalViewModel);
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
			url: '/infosec-api/engagements/'+engagementId+'/engagement-forms/OWG/file',
			data: data,
			success: function () {
				alert('File Uploaded Successfully');
				// update file list
				engagementFormOffWrkGrpApprovalViewModel.getUploadedFilesList();
			},
			error: function(error){
				alert('Error While Uploading File');
			}
		});
	});

});

init();
