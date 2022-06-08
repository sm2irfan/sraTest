function EngagementListViewModel() {

    var self = this;

	self.engageList = ko.observableArray([]);
	// changing visibility for different types of engagements

	self.currentUserRole = ko.observable(getUserRole());

	self.currentEmpData = ko.observable();
	self.getSessionEmpData = function () {
		if(self.currentEmpData() == null){
			$.ajax("/infosec-api/getSessionEmpData", {
				type: "get",
				async: false,
				success: function (employeeObj) {
					self.currentEmpData(new Employee(
							employeeObj.employeeId,
							employeeObj.firstName,
							employeeObj.lastName,
							employeeObj.position,
							employeeObj.email,
							employeeObj.company,
							employeeObj.imgUrl)
					);
				},
				error: function (allData) {
				}
			});
		}
		if(self.currentEmpData() == null){
			self.errorPage();
		}
	};
	self.getSessionEmpData();

	// form get engagements url based on type of role
	var engagementsURL='';
	if("REQUESTER" == self.currentUserRole()){
		engagementsURL = "/infosec-api/engagements?requestedBy="+self.currentEmpData().employeeId();
	} else if ("SPECIALIST" == self.currentUserRole()){
		engagementsURL = "/infosec-api/engagements";
	} else if ("ADMIN" == self.currentUserRole()) {
		engagementsURL = "/infosec-api/engagements";
	}

	// get engagement list
	self.getEngagementList = function () {
		self.engageList.removeAll();
		$.ajax(engagementsURL, {
			type: "get",
			async: false,
			success: function (data) {
				var json = ko.toJSON(data);
				var parsed = JSON.parse(json);

				// no engagement for the requester, try business owner email id
				if(parsed=='' && self.currentUserRole()=='REQUESTER'){
					engagementsURL = "/infosec-api/engagements?emailId="+self.currentEmpData().email();
					self.getEngagementList();
				}

				for(var i=0;i<parsed.length;i++){
					// filter draft statuses in case user role is not admin and requester
					var addToArray = true;
					if("DRAFT" == parsed[i].engagementStatus){
						if(("ADMIN" != self.currentUserRole() &&  "REQUESTER" != self.currentUserRole())){
							addToArray = false;
						}
					}

					if(addToArray) {
						var engagement = new Engage(
							parsed[i].engagementId,
							parsed[i].projectName,
							parsed[i].requestedBy,
							(parsed[i].requestedDatetime!=null?timeConverter(+parsed[i].requestedDatetime):null),
							parsed[i].engagementStatus,
							(parsed[i].completedDatetime!=null?timeConverter(+parsed[i].completedDatetime):null),
							self.getInfosecUserNameFromEmployeeId(parsed[i].assignedTo),
							parsed[i].riskSet.length,
							// adding html here as not working on html page using knockout
							parsed[i].engagementStatus != 'DRAFT'? '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">Assign</button>':null
						);
						self.engageList.push(engagement);
					}
				}
			},
			error: function (allData) {
				var json = ko.toJSON(allData);
				var parsed = JSON.parse(json);
				self.currentUserRole(parsed.responseText);
			}
		});
		stopSpinner();
	};


	self.selectRow = function (row) {
		self.seletedRow(row);
		window.location.replace("engagementFormSummary.html?engagementId="+self.seletedRow().id());
	};
	self.seletedRow = ko.observable();


	self.engagementId = ko.observable('');
	self.projectName = ko.observable('');
	self.requester = ko.observable('');
	self.infosecResource = ko.observable('');
	self.engagementStatus = ko.observable('');
	self.searchEngagements = function() {

		engagementsURL = "/infosec-api/engagements?";

		if(self.engagementId()!=''){
			engagementsURL = engagementsURL + "engagementId=" + self.engagementId() + '&';
		}
		if(self.requester()!=''){
			engagementsURL = engagementsURL + "requestedBy=" + self.requester() + '&';
		}
		if(self.projectName()!=''){
			engagementsURL = engagementsURL + "projectName=" + self.projectName() + '&';
		}
		if(self.infosecResource()!=''){
			engagementsURL = engagementsURL + "infosecResource=" + self.infosecResource() + '&';
		}
		if(self.engagementStatus()!=''){
			engagementsURL = engagementsURL + "engagementStatus=" + self.engagementStatus();
		}
		// remove last character if its i&
		if(engagementsURL.charAt(engagementsURL.length - 1) == '&' || engagementsURL.charAt(engagementsURL.length - 1) == '?'){
			engagementsURL = engagementsURL.substring(0, engagementsURL.length - 1);
		}
		self.getEngagementList();
	};

	// infosec resource filter
	var engagementStatusOptions = [
		{id:'1', name:'DRAFT'},
		{id:'2', name:'OPEN'},
		{id:'3', name:'CLOSED'},
		{id:'4', name:'CANCELED'},
		{id:'5', name:'ON_HOLD'},
		{id:'6', name:'NEW'}
	];
	self.engagementStatuses = ko.observable(engagementStatusOptions);

	self.navigateTo = function (data, event){
		window.location.replace($(event.target).attr('value')+"?engagementId=" + data.id());
	};

	self.infosecResources = ko.observable(getInfosecUsers());
	self.engagementIdToAssign = ko.observable('');
	self.infosecResourceToAssign = ko.observable('');

	self.assignEngagementId = function(row) {
		self.seletedRow(row);
		self.engagementIdToAssign(self.seletedRow().id());
	};

	self.assignInfosecResourceToEngagement = function() {
		if(self.engagementIdToAssign()!='' && self.infosecResourceToAssign()!=''){

			var engagement = getEngagement(self.engagementIdToAssign());
			engagement.engagementStatus='OPEN';
			engagement.assignedTo=self.infosecResourceToAssign();

			var resp = updateEngagement(self.engagementIdToAssign(), JSON.stringify(engagement));
			if(resp != null){
				self.engagementIdToAssign('');
				self.infosecResourceToAssign('');
				location.reload();
			}
		}
	};

	self.getInfosecUserNameFromEmployeeId = function(employeeId){
		for (var i = 0; i < self.infosecResources().length; i++) {
			if(self.infosecResources()[i].employeeId==employeeId){
				return self.infosecResources()[i].firstName + ' ' + self.infosecResources()[i].lastName;
			}
		}
	};

	self.getInfosecEmployeeIdFromName = function(firstName, lastName){
		for (var i = 0; i < self.infosecResources().length; i++) {
			if(self.infosecResources()[i].firstName()==firstName && self.infosecResources()[i].lastName()==lastName ){
				return self.infosecResources()[i].employeeId();
			}
		}
	};

	self.getEngagementList();
}

//Activates knockout.js
//startSpinner();
var koengagelstvm = new EngagementListViewModel();
ko.applyBindings(koengagelstvm);

function Engage (id, projectName, requestedBy, requestedDatetime, status, completedDatetime, infosecResource, numOfRisks, assignTo) {
	var self = this;
	self.id = ko.observable(id);
	self.projectName = ko.observable(projectName);
	self.requestedBy = ko.observable(requestedBy);
	self.requestedDatetime = ko.observable(requestedDatetime);
	self.status = ko.observable(status);
	self.completedDatetime = ko.observable(completedDatetime);
	self.infosecResource = ko.observable(infosecResource);
	self.numOfRisks = ko.observable(numOfRisks);
	self.assignTo = ko.observable(assignTo);
}

function Employee(employeeId, firstName, lastName, position, email, company, imgUrl){
	var self = this;
	self.employeeId = ko.observable(employeeId);
	self.firstName = ko.observable(firstName);
	self.lastName = ko.observable(lastName);
	self.position = ko.observable(position);
	self.email = ko.observable(email);
	self.company = ko.observable(company);
	//self.imgUrl = ko.observable("https://espresso.sg.singtelgroup.net/mysite/ESP_ALT_PHOTOS_OPTUS/Profile%20Pictures/" + email + ".jpg");
	self.imgUrl = ko.observable(imgUrl);
	self.empData = ko.computed(function(){
		return self.employeeId() + '-' + self.firstName() + " " + self.lastName();
	});
}


// for engagement pagination
$(document).ready(function() {
	// Setup - add a text input to each footer cell
	$('#engageAll tfoot th').each( function () {
		var title = $(this).text();
		$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
	} );

	// DataTable
	var table = $('#engageAll').DataTable( {
		"order": [[ 0, "desc" ]]
	} );

	// Apply the search
	table.columns().every( function () {
		var that = this;

		$( 'input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
					.search( this.value )
					.draw();
			}
		} );
	} );
} );
