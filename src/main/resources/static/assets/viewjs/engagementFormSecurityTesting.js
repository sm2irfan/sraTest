var engagementId;
var engagementFormId;
var engagementData;
var sessEmpData;
var commentsData;
var trackingData;

var engagementFormSecurityTestingViewModel = {
	
		currentCommentIdComputed: ko.observable(''),
		currentCommentValueComputed: ko.observable(''),
		currentUserRole: ko.observable(),

		engagementStatusName: ko.observable(),
		engagementStatusId: ko.observable(),
		engagementStatuses : ko.observable(),
		showGenerateCertificateButton: ko.observable(false),
		
		saveOrUpdateEngagement : function() {
			var readyToSubmit = engagementFormSecurityTestingViewModel.checkErrors();
			if (readyToSubmit) {
				engagementFormSecurityTestingViewModel.saveEngagementData();
			}
			alert("Engagement Form Successfully Updated");
		},
		
		saveEngagementData : function() {
			var jsonVal = ko.toJSON(engagementFormSecurityTestingViewModel);
			var parsed = JSON.parse(jsonVal);
			//console.log(jsonVal);
			var trackingDataAll = [];
			$.each(parsed, function( index, value ) {
				if (index.indexOf("createTracking") > -1 ) {
					itemTrackingId = index.substring(0, index.indexOf("$"));
					indexRemain1 = index.substring(index.indexOf("$")+1, index.length);
					itemTrackingField = indexRemain1.substring(0, indexRemain1.indexOf("$"));
					trackingoldname = index.replace("createTracking","old");
					oldValue = engagementFormSecurityTestingViewModel[trackingoldname]();
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
				        engagementFormSecurityTestingViewModel[trackingoldname](newValue);
					}
				}
				if (index.indexOf("Computed") > -1 
						|| index.indexOf("error") > -1 
						|| index.indexOf("currentUserRole") > -1 
						|| index.indexOf("engagementStatus") > -1) {
					delete parsed[index];
				}
			});
			engagementFormSecurityTestingViewModel.updateEngagementStatus(engagementId);
			engagementFormSecurityTestingViewModel.updateEngagementForm(engagementId, ko.toJSON(parsed));
			trackingDataReload = createTracking(JSON.stringify(trackingDataAll));
			engagementFormSecurityTestingViewModel.reloadTrackingData(trackingDataReload);
		},
		
		loadItemCommentsAndTrackingById: function(id) {
			var commentsForItem = $.map(commentsData, function (commentVal) {
				if (id == commentVal.commentItem) {
					if (sessEmpData.isInfosecUser == "Y") {
						if (commentVal.infosecViewed == "N") {
							engagementFormSecurityTestingViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					} else {
						if (commentVal.requestorViewed == "N") {
							engagementFormSecurityTestingViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					}
					return new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
				}
            });
			engagementFormSecurityTestingViewModel[id+'ItemCommentsData'+computedParamName](commentsForItem);
			
			var trackingForItem = $.map(trackingData, function (trackingVal) {
				if (id == trackingVal.trackingItem) {
					return new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				}
            });
			engagementFormSecurityTestingViewModel[id+'ItemTrackingData'+computedParamName](trackingForItem);
		},
		
		reloadCommentData: function(commentData, ItemCommentId) {
			var myMap = new Map();
			$.map(commentData, function (commentVal) {
				if (ItemCommentId == commentVal.commentItem) {
					var reloadcommentval = new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
					var id = commentVal.commentItem;
					if (myMap.get(id) == null) {
						engagementFormSecurityTestingViewModel[id+'ItemCommentsData'+computedParamName]([]);
						myMap.set(id,id);
					}
					engagementFormSecurityTestingViewModel[id+'ItemCommentsData'+computedParamName].push(reloadcommentval);
				}
            });
		},
		
		reloadTrackingData: function(trackingData) {
			var myMap = new Map();
			$.map(trackingData, function (trackingVal) {
				var reloadtrackingval = new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				var id = trackingVal.trackingItem;
				if (myMap.get(id) == null) {
					engagementFormSecurityTestingViewModel[id+'ItemTrackingData'+computedParamName]([]);
					myMap.set(id,id);
				}
				engagementFormSecurityTestingViewModel[id+'ItemTrackingData'+computedParamName].push(reloadtrackingval);
            });
		},
		
		checkErrors: function() {
	        if (engagementFormSecurityTestingViewModel.errors().length === 0) {
	            return true;
	        }
	        else {
	            alert('Please check the errors.');
	            engagementFormSecurityTestingViewModel.errors.showAllMessages();
	            return false;
	        }
	    },
		
		addCommentId: function(id) {
			engagementFormSecurityTestingViewModel.currentCommentIdComputed(id);
		},
		
		updateComment: function() {
			//alert(engagementFormSecurityTestingViewModel.currentCommentIdComputed());
			//alert(engagementFormSecurityTestingViewModel.currentCommentValueComputed());
			var ItemCommentId = engagementFormSecurityTestingViewModel.currentCommentIdComputed();
			var ItemCommentVal = engagementFormSecurityTestingViewModel.currentCommentValueComputed();
			engagementFormSecurityTestingViewModel.currentCommentIdComputed('');
			engagementFormSecurityTestingViewModel.currentCommentValueComputed('');
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
			engagementFormSecurityTestingViewModel.reloadCommentData(commentsData, ItemCommentId);
		},
		
		updateCommentViewed: function(id) {
			if (engagementFormSecurityTestingViewModel[id+'IsNewCommentForUser'+computedParamName]()) {
				var isInfosecUser = (sessEmpData.isInfosecUser == "Y")?"Y":"N";
				updateCommentViewed(engagementId, engagementFormId, id, isInfosecUser);
				engagementFormSecurityTestingViewModel[id+'IsNewCommentForUser'+computedParamName](false);
			}
		},


		updateEngagementStatus: function (engagementId){
			// json body
			engagementData.engagementStatus = getNameFromOptions(engagementFormSecurityTestingViewModel.engagementStatusId(), engagementStatusOptions);
			if(engagementData.engagementStatus == 'CLOSED' || engagementData.engagementStatus=='CANCELLED'){
				engagementData.completedDatetime=+new Date();
			}
			var engagementBody = JSON.stringify(engagementData);
			updateEngagement(engagementId, engagementBody);
		},


	updateEngagementForm: function (engagementId, jsonData){
	        var engagementFormBody = JSON.stringify({
	            "engagementFormName": "SECURITY_TESTING",
	            "formData": jsonData
	        });
	        updateEngagementForm(engagementId, "SECURITY_TESTING", engagementFormBody);
	    },

	    goNext: function () {
	    	var readyToSubmit = engagementFormSecurityTestingViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormSecurityTestingViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormSend.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormSend.html");
	        }
	    },

	    goPrev: function () {
	    	var readyToSubmit = engagementFormSecurityTestingViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormSecurityTestingViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormSolArchitectureReview.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormSolArchitectureReview.html");
	        }
	    },

	    navigateTo: function (data, event){
	    	var readyToSubmit = engagementFormSecurityTestingViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormSecurityTestingViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace($(event.target).attr('value')+"?engagementId=" + engagementId);
	        } else {
	            window.location.replace($(event.target).attr('value'));
	        }
	    },

		uploadedFileList: ko.observableArray([]),
		getUploadedFilesList: function() {
			engagementFormSecurityTestingViewModel.uploadedFileList.removeAll();
			var fileList = getUploadedFilesForEngagement(engagementId, "SECURITY_TESTING");
			for(var i= 0; i<fileList.length; i++) {
				engagementFormSecurityTestingViewModel.uploadedFileList.push(fileList[i]);
			}
		},
		downloadUploadedFile : function(data, event) {
			window.location = "/infosec-api/engagements/"+engagementId+"/engagement-forms/"+"SECURITY_TESTING"+"/file/"+data;
		},

		downloadZipFile: function() {
			window.location = "/infosec-api/engagements/"+engagementId+"/file";
		},

		submitEngagement : function() {

			if(engagementId!=null) {
				engagementFormSecurityTestingViewModel.engagementStatusId(getIdFromOptions('NEW', engagementStatusOptions));
				engagementData.requestedDatetime = +new Date();
				engagementFormSecurityTestingViewModel.saveEngagementData();
				window.location.replace("engagementList.html");
			}
			else {
				alert("Please Create An Engagement First");
			}
		},

		checkEngagementStatus : function(obj, event) {
			if(getNameFromOptions(obj.engagementStatusId(), engagementStatusOptions)=='CLOSED'){
				engagementFormSecurityTestingViewModel.showGenerateCertificateButton(true);
			} else {
				engagementFormSecurityTestingViewModel.showGenerateCertificateButton(false);
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

	engagementFormSecurityTestingViewModel.engagementStatuses(engagementStatusOptions);

	engagementId = getURLParamEngagementIdValue();
    var engagementFormData = null;
    
    if(engagementId!=null) {
        // go and fetch the form data for the given engagement id
    	sessEmpData =  getSessionEmpData();
        engagementData = getEngagement(engagementId);
        engagementFormData = getEngagementForm(engagementId, "SECURITY_TESTING");
        engagementFormId = engagementFormData.engagementFormId;
        commentsData = getComments(engagementId, engagementFormId);
        trackingData = getTracking(engagementId, engagementFormId);
        //Get Form Template and Data
        var engagementFormFormTemplate = engagementFormData.formTemplate;
        var engagementFormFormData = engagementFormData.formData;
        // processing dynamic generation
        processEngagementFormJson(JSON.parse(engagementFormFormTemplate), JSON.parse(engagementFormFormData));
		engagementFormSecurityTestingViewModel.engagementStatusId(getIdFromOptions(engagementData.engagementStatus, engagementStatusOptions));
		engagementFormSecurityTestingViewModel.engagementStatusName(engagementData.engagementStatus);
		if(engagementData.engagementStatus == 'CLOSED'){
			engagementFormSecurityTestingViewModel.showGenerateCertificateButton(true);
		}
		engagementFormSecurityTestingViewModel.currentUserRole(sessEmpData.userRole);

		//get uploaded files list
		engagementFormSecurityTestingViewModel.getUploadedFilesList();
    }
    
}

function processEngagementFormJson(jsontemplate, jsondata) {
	//alert(ko.toJSON(engagementData))
	//alert(ko.toJSON(json))
	parseJsonToBind(jsontemplate, jsondata, "engagementFormSecurityTestingViewModel");
	ko.applyBindings(engagementFormSecurityTestingViewModel);
	engagementFormSecurityTestingViewModel.errors = ko.validation.group(engagementFormSecurityTestingViewModel);
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
			url: '/infosec-api/engagements/'+engagementId+'/engagement-forms/SECURITY_TESTING/file',
			data: data,
			success: function () {
				alert('File Uploaded Successfully');
				// update file list
				engagementFormSecurityTestingViewModel.getUploadedFilesList();
			},
			error: function(error){
				alert('Error While Uploading File');
			}
		});
	});

});

init();
