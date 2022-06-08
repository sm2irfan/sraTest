var engagementId;
var engagementFormId;
var engagementData;
var sessEmpData;
var commentsData;
var trackingData;

var engagementFormInterfaceContractsViewModel = {
	
		currentCommentIdComputed: ko.observable(''),
		currentCommentValueComputed: ko.observable(''),
		currentUserRole: ko.observable(),

		engagementStatusName: ko.observable(),
		engagementStatusId: ko.observable(),
		engagementStatuses : ko.observable(),
		showGenerateCertificateButton: ko.observable(false),
		
		interfaceContractItems: ko.observableArray([new InterfaceContract(1,'','','','','')]),
		addnewinterfaceContractItems: function(){
			engagementFormInterfaceContractsViewModel.interfaceContractItems.push(new InterfaceContract(engagementFormInterfaceContractsViewModel.interfaceContractItems().length+1,'','','','',''));
		},
		deletelastinterfaceContractItems: function(){
			engagementFormInterfaceContractsViewModel.interfaceContractItems.pop();
		},
		
		saveOrUpdateEngagement : function() {
			var readyToSubmit = engagementFormInterfaceContractsViewModel.checkErrors();
			if (readyToSubmit) {
				engagementFormInterfaceContractsViewModel.saveEngagementData();
			}
			alert("Engagement Form Successfully Updated");
		},
		
		saveEngagementData : function() {
			var jsonVal = ko.toJSON(engagementFormInterfaceContractsViewModel);
			var parsed = JSON.parse(jsonVal);
			//console.log(jsonVal);
			var trackingDataAll = [];
			$.each(parsed, function( index, value ) {
				/*
				if (index.indexOf("createTracking") > -1 ) {
					itemTrackingId = index.substring(0, index.indexOf("$"));
					indexRemain1 = index.substring(index.indexOf("$")+1, index.length);
					itemTrackingField = indexRemain1.substring(0, indexRemain1.indexOf("$"));
					trackingoldname = index.replace("createTracking","old");
					oldValue = engagementFormInterfaceContractsViewModel[trackingoldname]();
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
				        engagementFormInterfaceContractsViewModel[trackingoldname](newValue);
					}
				}
				*/
				if (index.indexOf("Computed") > -1 
						|| index.indexOf("error") > -1 
						|| index.indexOf("currentUserRole") > -1 
						|| index.indexOf("engagementStatus") > -1) {
					delete parsed[index];
				}
			});
			engagementFormInterfaceContractsViewModel.updateEngagementStatus(engagementId);
			engagementFormInterfaceContractsViewModel.updateEngagementForm(engagementId, ko.toJSON(parsed));
			//trackingDataReload = createTracking(JSON.stringify(trackingDataAll));
			//engagementFormInterfaceContractsViewModel.reloadTrackingData(trackingDataReload);
		},
		
		loadItemCommentsAndTrackingById: function(id) {
			var commentsForItem = $.map(commentsData, function (commentVal) {
				if (id == commentVal.commentItem) {
					if (sessEmpData.isInfosecUser == "Y") {
						if (commentVal.infosecViewed == "N") {
							engagementFormInterfaceContractsViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					} else {
						if (commentVal.requestorViewed == "N") {
							engagementFormInterfaceContractsViewModel[id+'IsNewCommentForUser'+computedParamName](true);
						}
					}
					return new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
				}
            });
			engagementFormInterfaceContractsViewModel[id+'ItemCommentsData'+computedParamName](commentsForItem);
			
			var trackingForItem = $.map(trackingData, function (trackingVal) {
				if (id == trackingVal.trackingItem) {
					return new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				}
            });
			engagementFormInterfaceContractsViewModel[id+'ItemTrackingData'+computedParamName](trackingForItem);
		},
		
		reloadCommentData: function(commentData, ItemCommentId) {
			var myMap = new Map();
			$.map(commentData, function (commentVal) {
				if (ItemCommentId == commentVal.commentItem) {
					var reloadcommentval = new CommentData(commentVal.commentBy, timeConverter(new Date(commentVal.commentDate)), commentVal.comment);
					var id = commentVal.commentItem;
					if (myMap.get(id) == null) {
						engagementFormInterfaceContractsViewModel[id+'ItemCommentsData'+computedParamName]([]);
						myMap.set(id,id);
					}
					engagementFormInterfaceContractsViewModel[id+'ItemCommentsData'+computedParamName].push(reloadcommentval);
				}
            });
		},
		
		reloadTrackingData: function(trackingData) {
			var myMap = new Map();
			$.map(trackingData, function (trackingVal) {
				var reloadtrackingval = new TrackingData(trackingVal.trackingUser, timeConverter(new Date(trackingVal.trackingDate)), new Array(new TrackingFieldValues(trackingVal.trackingField, trackingVal.oldValue, trackingVal.newValue)));
				var id = trackingVal.trackingItem;
				if (myMap.get(id) == null) {
					engagementFormInterfaceContractsViewModel[id+'ItemTrackingData'+computedParamName]([]);
					myMap.set(id,id);
				}
				engagementFormInterfaceContractsViewModel[id+'ItemTrackingData'+computedParamName].push(reloadtrackingval);
            });
		},
		
		checkErrors: function() {
	        if (engagementFormInterfaceContractsViewModel.errors().length === 0) {
	            return true;
	        }
	        else {
	            alert('Please check the errors.');
	            engagementFormInterfaceContractsViewModel.errors.showAllMessages();
	            return false;
	        }
	    },
		
		addCommentId: function(id) {
			engagementFormInterfaceContractsViewModel.currentCommentIdComputed(id);
		},
		
		updateComment: function() {
			//alert(engagementFormInterfaceContractsViewModel.currentCommentIdComputed());
			//alert(engagementFormInterfaceContractsViewModel.currentCommentValueComputed());
			var ItemCommentId = engagementFormInterfaceContractsViewModel.currentCommentIdComputed();
			var ItemCommentVal = engagementFormInterfaceContractsViewModel.currentCommentValueComputed();
			engagementFormInterfaceContractsViewModel.currentCommentIdComputed('');
			engagementFormInterfaceContractsViewModel.currentCommentValueComputed('');
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
			engagementFormInterfaceContractsViewModel.reloadCommentData(commentsData, ItemCommentId);
		},
		
		updateCommentViewed: function(id) {
			if (engagementFormInterfaceContractsViewModel[id+'IsNewCommentForUser'+computedParamName]()) {
				var isInfosecUser = (sessEmpData.isInfosecUser == "Y")?"Y":"N";
				updateCommentViewed(engagementId, engagementFormId, id, isInfosecUser);
				engagementFormInterfaceContractsViewModel[id+'IsNewCommentForUser'+computedParamName](false);
			}
		},


		updateEngagementStatus: function (engagementId){
			// json body
			engagementData.engagementStatus = getNameFromOptions(engagementFormInterfaceContractsViewModel.engagementStatusId(), engagementStatusOptions);
			if(engagementData.engagementStatus == 'CLOSED' || engagementData.engagementStatus=='CANCELLED'){
				engagementData.completedDatetime=+new Date();
			}
			var engagementBody = JSON.stringify(engagementData);
			updateEngagement(engagementId, engagementBody);
		},
	
	    updateEngagementForm: function (engagementId, jsonData){
	        var engagementFormBody = JSON.stringify({
	            "engagementFormName": "INTERFACE_CONTRACTS",
	            "formData": jsonData
	        });
	        updateEngagementForm(engagementId, "INTERFACE_CONTRACTS", engagementFormBody);
	    },

	    goNext: function () {
	    	var readyToSubmit = engagementFormInterfaceContractsViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormInterfaceContractsViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormSolArchitectureReview.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormSolArchitectureReview.html");
	        }
	    },

	    goPrev: function () {
	    	var readyToSubmit = engagementFormInterfaceContractsViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormInterfaceContractsViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace("engagementFormNtwrkMngdSystems.html?engagementId=" + engagementId);
	        } else {
	            window.location.replace("engagementFormNtwrkMngdSystems.html");
	        }
	    },

	    navigateTo: function (data, event){
	    	var readyToSubmit = engagementFormInterfaceContractsViewModel.checkErrors();
	    	if (!readyToSubmit) {
				return false;
			}
	    	engagementFormInterfaceContractsViewModel.saveEngagementData();
	        if(engagementId!=null) {
	            window.location.replace($(event.target).attr('value')+"?engagementId=" + engagementId);
	        } else {
	            window.location.replace($(event.target).attr('value'));
	        }
	    },

		uploadedFileList: ko.observableArray([]),
		getUploadedFilesList: function() {
			engagementFormInterfaceContractsViewModel.uploadedFileList.removeAll();
			var fileList = getUploadedFilesForEngagement(engagementId, "INTERFACE_CONTRACTS");
			for(var i= 0; i<fileList.length; i++) {
				engagementFormInterfaceContractsViewModel.uploadedFileList.push(fileList[i]);
			}
		},
		downloadUploadedFile : function(data, event) {
			window.location = "/infosec-api/engagements/"+engagementId+"/engagement-forms/"+"INTERFACE_CONTRACTS"+"/file/"+data;
		},

		downloadZipFile: function() {
			window.location = "/infosec-api/engagements/"+engagementId+"/file";
		},

		submitEngagement : function() {

			if(engagementId!=null) {
				engagementFormInterfaceContractsViewModel.engagementStatusId(getIdFromOptions('NEW', engagementStatusOptions));
				engagementData.requestedDatetime = +new Date();
				engagementFormInterfaceContractsViewModel.saveEngagementData();
				window.location.replace("engagementList.html");
			}
			else {
				alert("Please Create An Engagement First");
			}
		},

		checkEngagementStatus : function(obj, event) {
			if(getNameFromOptions(obj.engagementStatusId(), engagementStatusOptions)=='CLOSED'){
				engagementFormInterfaceContractsViewModel.showGenerateCertificateButton(true);
			} else {
				engagementFormInterfaceContractsViewModel.showGenerateCertificateButton(false);
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

function InterfaceContract(ICId, ICSource, ICDestination, ICNetworkType, ICProtocol, ICTypeOfData) {
    this.ICId = ko.observable(ICId);
    this.ICSource = ko.observable(ICSource);
    this.ICDestination = ko.observable(ICDestination);
    this.ICNetworkType = ko.observable(ICNetworkType);
    this.ICProtocol = ko.observable(ICProtocol);
    this.ICTypeOfData = ko.observable(ICTypeOfData);
}


function init() {

	engagementFormInterfaceContractsViewModel.engagementStatuses(engagementStatusOptions);

	engagementId = getURLParamEngagementIdValue();
    var engagementFormData = null;
    
    if(engagementId!=null) {
        // go and fetch the form data for the given engagement id
    	sessEmpData =  getSessionEmpData();
        engagementData = getEngagement(engagementId);
        engagementFormData = getEngagementForm(engagementId, "INTERFACE_CONTRACTS");
        engagementFormId = engagementFormData.engagementFormId;
        //commentsData = getComments(engagementId, engagementFormId);
        //trackingData = getTracking(engagementId, engagementFormId);
        //Get Form Template and Data
        var engagementFormFormTemplate = engagementFormData.formTemplate;
        var engagementFormFormData = engagementFormData.formData;
        // processing dynamic generation
        processEngagementFormJson(JSON.parse(engagementFormFormTemplate), JSON.parse(engagementFormFormData));
		engagementFormInterfaceContractsViewModel.engagementStatusId(getIdFromOptions(engagementData.engagementStatus, engagementStatusOptions));
		engagementFormInterfaceContractsViewModel.engagementStatusName(engagementData.engagementStatus);
		if(engagementData.engagementStatus == 'CLOSED'){
			engagementFormInterfaceContractsViewModel.showGenerateCertificateButton(true);
		}
		engagementFormInterfaceContractsViewModel.currentUserRole(sessEmpData.userRole);

		//get uploaded files list
		engagementFormInterfaceContractsViewModel.getUploadedFilesList();
    }
    
}

function processEngagementFormJson(jsontemplate, jsondata) {
	//alert(ko.toJSON(engagementData))
	//alert(ko.toJSON(json))
	parseJsonToBind(jsontemplate, jsondata, "engagementFormInterfaceContractsViewModel");
	ko.applyBindings(engagementFormInterfaceContractsViewModel);
	engagementFormInterfaceContractsViewModel.errors = ko.validation.group(engagementFormInterfaceContractsViewModel);
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
			url: '/infosec-api/engagements/'+engagementId+'/engagement-forms/INTERFACE_CONTRACTS/file',
			data: data,
			success: function () {
				alert('File Uploaded Successfully');
				// update file list
				engagementFormInterfaceContractsViewModel.getUploadedFilesList();
			},
			error: function(error){
				alert('Error While Uploading File');
			}
		});
	});

});

init();
