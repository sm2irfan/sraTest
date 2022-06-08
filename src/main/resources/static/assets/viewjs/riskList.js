function RiskListViewModel() {

    var self = this;

	self.currentUserRole = ko.observable(getUserRole());
	self.engagementId = ko.observable('');
	self.riskId = ko.observable('');
	self.projectName = ko.observable('');
	self.businessOwner = ko.observable('');
	self.infosecResource = ko.observable('');
	self.riskStatus = ko.observable('');

	// infosec resource filter
	var engagements = new Array();

	// get engagement list
	self.getEngagementIds = function() {

		$.ajax("/infosec-api/engagements", {
			type: "get",
			async: false,
			success: function (data) {
				var json = ko.toJSON(data);
				var parsed = JSON.parse(json);
				for(var i=0;i<parsed.length;i++){
					var obj = new Object();
					engagements[parsed[i].engagementId] = parsed[i].projectName;
				}
			},
			error: function (allData) {
			}
		});
	};
	self.getEngagementIds();


	self.riskItems = ko.observableArray([]);
	self.getRiskList = function(){

		// remove all items from risk list
		self.riskItems.removeAll();

		var riskItems = getRisks(self.engagementId(), self.riskId(), self.businessOwner(), self.projectName(), self.infosecResource(), self.riskStatus());
		for (var i = 0; i < riskItems.length; i++) {
			self.riskItems.push(new Risk(
					riskItems[i].engagementId,
					riskItems[i].riskId,
					engagements[riskItems[i].engagementId],
					riskItems[i].infosecResource,
					(riskItems[i].raisedDatetime!=null?timeConverter(+riskItems[i].raisedDatetime):null),
					riskItems[i].riskStatus,
					(riskItems[i].completedDatetime!=null?timeConverter(+riskItems[i].completedDatetime):null),
					riskItems[i].riskOwner,
					riskItems[i].riskName,
					riskItems[i].riskLevel)
			);
		}
	};
	self.getRiskList();

	self.navigateTo = function (data, event){
		window.location.replace($(event.target).attr('value')+"?engagementId=" + data.engagementId());
	};

	self.searchRisks = function() {
		self.getRiskList();
	};

	// infosec resource filter
	var riskStatusOptions = [
		{id:'', name:'ALL'},
		{id:'OPEN', name:'OPEN'},
		{id:'CLOSED', name:'CLOSED'},
		{id:'MITIGATED', name:'MITIGATED'},
		{id:'DEVIATION', name:'DEVIATION'},
		{id:'NEW', name:'NEW'}
	];
	self.riskStatuses = ko.observable(riskStatusOptions);
}

//Activates knockout.js
var korisklstvm = new RiskListViewModel();
ko.applyBindings(korisklstvm);


function Engagement (engId, riskList) {

	var self = this;

	self.engId = engId;
	self.riskList = ko.observableArray([]);
	self.riskList = riskList;
}

function Risk (engagementId, riskId, projectName, infosecResource, raisedDatetime, riskStatus, completedDatetime, riskOwner, riskName, riskLevel) {

	var self = this;

	self.engagementId = ko.observable(engagementId);
	self.riskId = ko.observable(riskId);
	self.projectName = ko.observable(projectName);
	self.infosecResource = ko.observable(infosecResource);
	self.raisedDatetime = ko.observable(raisedDatetime);
	self.riskStatus = ko.observable(riskStatus);
	self.completedDatetime = ko.observable(completedDatetime);
	self.riskOwner = ko.observable(riskOwner);
	self.riskName = ko.observable(riskName);
	self.riskLevel = ko.observable(riskLevel);
}

// for engagement pagination
$(document).ready(function() {
	// Setup - add a text input to each footer cell
	$('#riskAll tfoot th').each( function () {
		var title = $(this).text();
		$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
	} );

	// DataTable
	var table = $('#riskAll').DataTable();

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