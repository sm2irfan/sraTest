var engagementId = null;
function RiskFormViewModel() {

    var self = this;

    self.endUserICCommentsRemain=function(){
    	if(self.endUserICComments()==null) return 500;
    	return 500-self.endUserICComments().length;
    };

    self.sessionEmployeeData = ko.observable(getSessionEmpData());
    self.endUserICComments=ko.observable();
    self.canAddMoreRisk = ko.observable(true);
    self.riskItems = ko.observableArray([]);

    self.toUpdateRiskItem = ko.observable(new Risk('','', '', '', '', '', '', '', '', '', '', '', ''));
    self.newlyAddedIssuesIdentified = ko.observable();
    self.newlyAddedMitigationAction = ko.observable();

    self.engagementStatus = ko.observable();
    self.projectName = ko.observable();

    var infosecResourcesOptions = getInfosecUsers();
    self.infosecResources = ko.observable(infosecResourcesOptions);

    var riskLevelsOptions = [
        {id:'1', name:'LOW'},
        {id:'2', name:'MEDIUM'},
        {id:'3', name:'HIGH'}
    ];
    self.riskLevels = ko.observable(riskLevelsOptions);

    // infosec resource filter
    var riskStatusOptions = [
        {id:'1', name:'NEW'},
        {id:'2', name:'OPEN'},
        {id:'3', name:'MANAGED'},
        {id:'4', name:'CLOSED'},
        {id:'5', name:'MITIGATED'},
        {id:'6', name:'DEVIATION'},
        {id:'7', name:'INFORMATION_REQUESTED'},
        {id:'8', name:'INFORMATION_PROVIDED'}
    ];

    self.riskStatuses = ko.observable(riskStatusOptions);

    // infosec resource filter
    var likelihoodOptions = [
        {id:'1', name:'LOW'},
        {id:'2', name:'MEDIUM'},
        {id:'3', name:'HIGH'}
    ];
    self.likelihoodVals = ko.observable(likelihoodOptions);

    // infosec resource filter
    var impactOptions = [
        {id:'1', name:'LOW'},
        {id:'2', name:'MEDIUM'},
        {id:'3', name:'HIGH'}
    ];
    self.impactVals = ko.observable(impactOptions);

    self.createOrUpdateRisks = function (){

        if(engagementId!=null){

            var risksArr = [];
            for (var i = 0; i < self.riskItems().length; i++) {

                var item = new Object();

                item.riskId = self.riskItems()[i].riskId();
                item.riskName = self.riskItems()[i].riskName();
                item.projectName = self.riskItems()[i].projectName();
                item.infosecResource = self.sessionEmployeeData().employeeId;
                item.likelihood = self.getNameFromOptions(self.riskItems()[i].likelihood(), likelihoodOptions);
                item.impact = self.getNameFromOptions(self.riskItems()[i].impact(), impactOptions);
                item.riskLevel = self.getNameFromOptions(self.riskItems()[i].riskLevel(), riskLevelsOptions);
                item.raisedDatetime = new Date(self.riskItems()[i].raisedDatetime()).getTime();
                item.issuesIdentified = self.riskItems()[i].issuesIdentified();
                item.mitigationAction = self.riskItems()[i].mitigationAction();
                item.riskStatus = self.getNameFromOptions(self.riskItems()[i].riskStatus(), riskStatusOptions);
                item.completedDatetime = new Date(self.riskItems()[i].completedDatetime()).getTime();
                item.riskOwner = self.riskItems()[i].riskOwner();

                risksArr.push(item);
            }
            var riskIds = createRisks(engagementId, risksArr);
            if (riskIds != null) {
                // get all the risks
                self.canAddMoreRisk = ko.observable(true);
                self.getRiskList();
            }
        }
    };

    self.updateRisk = function () {

        console.log(self.toUpdateRiskItem());

        if(engagementId!=null){

            var riskItem = new Object();

            riskItem.riskId = self.toUpdateRiskItem().riskId();
            riskItem.riskName = self.toUpdateRiskItem().riskName();
            riskItem.projectName = self.toUpdateRiskItem().projectName();
            riskItem.infosecResource = self.toUpdateRiskItem().infosecResource();
            riskItem.likelihood = self.getNameFromOptions(self.toUpdateRiskItem().likelihood(), likelihoodOptions);
            riskItem.impact = self.getNameFromOptions(self.toUpdateRiskItem().impact(), impactOptions);
            riskItem.riskLevel = self.getNameFromOptions(self.toUpdateRiskItem().riskLevel(), riskLevelsOptions);
            riskItem.raisedDatetime = new Date(self.toUpdateRiskItem().raisedDatetime()).getTime();
            riskItem.issuesIdentified = self.toUpdateRiskItem().issuesIdentified();
            riskItem.mitigationAction = self.toUpdateRiskItem().mitigationAction();
            riskItem.riskStatus = self.getNameFromOptions(self.toUpdateRiskItem().riskStatus(), riskStatusOptions);
            riskItem.completedDatetime = new Date(self.toUpdateRiskItem().completedDatetime()).getTime();
            riskItem.riskOwner = self.toUpdateRiskItem().riskOwner();

            var updatedRiskId = updateRisk(engagementId, self.toUpdateRiskItem().riskId(), riskItem);

            if(updatedRiskId!=null){
                self.getRiskList();
            }
        }
    };

    self.getRiskList = function () {

        // remove all items from risk list
        self.riskItems.removeAll();

        if(engagementId!=null){

            // go and fetch the form data for the given engagement id
            var engagementData = getEngagement(engagementId);
            self.engagementStatus(engagementData.engagementStatus);
            self.projectName(engagementData.projectName);

            var riskItems = getRisksByEngagementId(engagementId);
            for(var i=0 ; i<riskItems.length; i++) {
                self.riskItems.push(new Risk(
                        riskItems[i].riskId,
                        riskItems[i].riskName,
                        riskItems[i].projectName,
                        riskItems[i].infosecResource,
                        self.getIdFromOptions(riskItems[i].likelihood, likelihoodOptions),
                        self.getIdFromOptions(riskItems[i].impact, impactOptions),
                        self.getIdFromOptions(riskItems[i].riskLevel, riskLevelsOptions),
                        dateFormater(riskItems[i].raisedDatetime),
                        riskItems[i].issuesIdentified,
                        riskItems[i].mitigationAction,
                        self.getIdFromOptions(riskItems[i].riskStatus, riskStatusOptions),
                        dateFormater(riskItems[i].completedDatetime),
                        riskItems[i].riskOwner
                    )
                );
            }
        }
    };

    self.getIdFromOptions = function (name, options) {
        for(var i=0; i<options.length; i++){
            if(options[i].name == name){
                return options[i].id;
            }
        }
    };

    self.getNameFromOptions = function (id, options) {
        for(var i=0; i<options.length; i++){
            if(options[i].id == id){
                return options[i].name;
            }
        }
    };

    self.addNewRisk = function(){
        if(self.canAddMoreRisk() == false) {
            alert("Please Save Risk Before Adding More");
        } else {
            self.riskItems.push(new Risk('','', '', '', '', '', '', '', '', '', '', '', ''));
            self.canAddMoreRisk = ko.observable(false);
        }
    };

    self.navigateTo = function (data, event){
        self.createOrUpdateRisks();
        if(engagementId!=null) {
            window.location.replace($(event.target).attr('value')+"?engagementId=" + engagementId);
        } else {
            window.location.replace($(event.target).attr('value'));
        }
    };

    self.saveRisks = function() {
        if(engagementId!=null) {
            self.createOrUpdateRisks();
            alert("Risk Form Successfully Updated");
        } else {
            alert("Please Create/Select An Engagement");
        }
    };

    self.openIssuesIdentified = function(data, event){
        self.toUpdateRiskItem(data);
    };


    self.openMitigationAction = function(data, event){
        self.toUpdateRiskItem(data);
    };

    self.updateIssuesIdentified = function(){
        if(self.toUpdateRiskItem().issuesIdentified()!=''){
            self.toUpdateRiskItem().issuesIdentified(self.toUpdateRiskItem().issuesIdentified()+'\n\n' + timeConverter(new Date()) + ':' + '\n' + self.newlyAddedIssuesIdentified());
        } else {
            self.toUpdateRiskItem().issuesIdentified(timeConverter(new Date()) + ':' + '\n' + self.newlyAddedIssuesIdentified());
        }

        self.updateRisk();
        $('#issuesIdentifiedModal').modal('hide');
        self.getRiskList();
    };

    self.updateMitigationAction = function(){
        if(self.toUpdateRiskItem().mitigationAction()!=''){
            self.toUpdateRiskItem().mitigationAction(self.toUpdateRiskItem().mitigationAction()+'\n\n' + timeConverter(new Date()) + ':' + '\n' + self.newlyAddedMitigationAction());
        } else {
            self.toUpdateRiskItem().mitigationAction(timeConverter(new Date()) + ':' + '\n' + self.newlyAddedMitigationAction());
        }
        self.updateRisk();
        $('#mitigationActionModal').modal('hide');
        self.getRiskList();
    };


    self.uploadedFileList = ko.observableArray([]);
    self.getUploadedFilesList = function() {
        self.uploadedFileList.removeAll();
        var fileList = getUploadedFilesForEngagement(engagementId, "RISK_FORM");
        for(var i= 0; i<fileList.length; i++) {
            self.uploadedFileList.push(fileList[i]);
        }
    };
    self.downloadUploadedFile = function(data, event) {
        window.location = "/infosec-api/engagements/"+engagementId+"/engagement-forms/"+"RISK_FORM"+"/file/"+data;
    };

    self.downloadZipFile = function() {
        window.location = "/infosec-api/engagements/"+engagementId+"/file";
    };

    // get user role
    self.currentUserRole = ko.observable(getUserRole());
    // check if the url contains engagement id
    engagementId = getURLParamEngagementIdValue();
    // get risk list
    self.getRiskList();
    //get uploaded files list
    self.getUploadedFilesList();
}

var Risk = function(riskId, riskName, projectName, infosecResource, likelihood, impact, riskLevel, raisedDatetime, issuesIdentified, mitigationAction, riskStatus, completedDatetime, riskOwner) {

    var self = this;

    self.riskId = ko.observable(riskId);
    self.riskName = ko.observable(riskName);
    self.projectName = ko.observable(projectName);
    self.infosecResource = ko.observable(infosecResource);
    self.likelihood = ko.observable(likelihood);
    self.impact = ko.observable(impact);
    self.riskLevel = ko.observable(riskLevel);
    self.raisedDatetime = ko.observable(raisedDatetime);
    self.issuesIdentified = ko.observable(issuesIdentified);
    self.mitigationAction = ko.observable(mitigationAction);
    self.riskStatus = ko.observable(riskStatus);
    self.completedDatetime = ko.observable(completedDatetime);
    self.riskOwner = ko.observable(riskOwner);
};

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
            url: '/infosec-api/engagements/'+engagementId+'/engagement-forms/RISK_FORM/file',
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

//Activates knockout.js
var koengageformintfctrctvm = new RiskFormViewModel();
ko.applyBindings(koengageformintfctrctvm);