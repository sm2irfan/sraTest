
function EngagementDashboardViewModel() {

    var self = this;

	displayEngagementUsersListChart('dahdbaordEngagementListChart');
	displayEngagementBChartMonthly('dahdbaordEngagementBChart');

	self.displayBChart = function (viewModel, event) {
		if(event.target.value == '1'){
			self.displayMonthlyBChart();
		} else if (event.target.value == '2'){
			self.displayWeeklyBChart();
		}
	};

	self.displayMonthlyBChart = function() {
		displayEngagementBChartMonthly('dahdbaordEngagementBChart');
	};

	self.displayWeeklyBChart = function() {
		displayEngagementBChartWeekly('dahdbaordEngagementBChart');
	};

	// Bchart categories
	var displayOptions = [
		{id:'1', name:'Monthly'},
		{id:'2', name:'Weekly'}
	];
	self.engageDashboardDisplayCategory = ko.observable(displayOptions);


	self.displayListChart = function (viewModel, event) {
		if(event.target.value == '1'){
			self.displayAllBChart();
		} else if (event.target.value == '2' || event.target.value == '3' || event.target.value == '4'){
			self.displayInfosecResourceBChart();
		}
	};

	self.displayAllBChart = function() {
		displayEngagementUsersListChart('dahdbaordEngagementListChart');
	};

	self.displayInfosecResourceBChart = function() {
		displayEngagementUsersListChartInfosecResource('dahdbaordEngagementListChart');
	};

	// infosec resource filter
	var infosecResources = [
		{id:'1', name:'All'},
		{id:'2', name:'Luc'},
		{id:'3', name:'Chau'},
		{id:'4', name:'Valeriy'}
	];
	self.engageListFilterCategory = ko.observable(infosecResources);


	// todo : to this common to all view models.
	self.currentUserRole = ko.observable();
	// get session user role
	self.getSessionUserRole = function () {
		if(self.currentUserRole() == null){
			$.ajax("/infosec-api/getSessionUserRole", {
				type: "get",
				async: false,
				success: function (data) {
					self.currentUserRole(data);
				},
				error: function (allData) {
					var json = ko.toJSON(allData);
					var parsed = JSON.parse(json);
					self.currentUserRole(parsed.responseText);
				}
			});
		}
		if(self.currentUserRole() == null){
			//self.errorPage();
		}
	};
	self.getSessionUserRole();

	// End of View Model
}

//Activates knockout.js
var koEngagementDashboard = new EngagementDashboardViewModel();
ko.applyBindings(koEngagementDashboard);

function displayEngagementUsersListChart(div) {
	
	var myChart = {
			colors: ['#BF360C', 
				  '#CDDC39', 
				  '#0D47A1', 
				  '#B71C1C',
				  '#F1C40F'],
			chart: {
		        plotBackgroundColor: null,
		        plotBorderWidth: null,
		        plotShadow: false,
		        type: 'pie',
		        renderTo: div
		    },
		    title: {
		        text: 'Engagement Statuses'
		    },
		    tooltip: {
		        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		    },
		    plotOptions: {
		        pie: {
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: false
		            },
		            showInLegend: true
		        }
		    },
		    series: [{
		        name: 'Status',
		        colorByPoint: true,
		        data: [{
		            name: 'New',
		            y: 61.41,
		            sliced: true,
		            selected: true
		        }, {
		            name: 'Open',
		            y: 11.84
		        }, {
		            name: 'Closed',
		            y: 10.85
		        }, {
		            name: 'Cancel',
		            y: 4.67
		        }, {
		            name: 'OnHold',
		            y: 4.18
		        }]
		    }]
	};

	return new Highcharts.Chart(myChart);
}

function displayEngagementUsersListChartInfosecResource(div) {

	var myChart = {
		colors: ['#BF360C',
			'#CDDC39',
			'#0D47A1',
			'#B71C1C',
			'#F1C40F'],
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			type: 'pie',
			renderTo: div
		},
		title: {
			text: 'Engagement Statuses'
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: false
				},
				showInLegend: true
			}
		},
		series: [{
			name: 'Status',
			colorByPoint: true,
			data: [{
				name: 'New',
				y: 41.41,
				sliced: true,
				selected: true
			}, {
				name: 'Open',
				y: 31.84
			}, {
				name: 'Closed',
				y: 5.85
			}, {
				name: 'Cancel',
				y: 11.67
			}, {
				name: 'OnHold',
				y: 2.18
			}]
		}]
	};

	return new Highcharts.Chart(myChart);
}

function displayEngagementBChartMonthly(div) {
	
	var myChart = {
			colors: ['#BF360C', 
				  '#CDDC39', 
				  '#0D47A1', 
				  '#B71C1C',
				  '#F1C40F',
				  '#1B5E20',
				  '#01579B',
				  '#880E4F', 
				  '#1A237E',
				  '#FF8A65',
				  '#F44336',
				  '#43A047'],
			chart: {
		        type: 'column',
		        renderTo: div
		    },
		    title: {
		        text: 'Dashboard B'
		    },
		    xAxis: {
		        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: 'No of Engagements Handled'
		        },
		        stackLabels: {
		            enabled: true,
		            style: {
		                fontWeight: 'bold',
		                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
		            }
		        }
		    },
		    legend: {
		        align: 'right',
		        x: -30,
		        verticalAlign: 'top',
		        y: 25,
		        floating: true,
		        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
		        borderColor: '#CCC',
		        borderWidth: 1,
		        shadow: false
		    },
		    tooltip: {
		        headerFormat: '<b>{point.x}</b><br/>',
		        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
		    },
		    plotOptions: {
		        column: {
		            stacking: 'normal',
		            dataLabels: {
		                enabled: true,
		                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
		            }
		        }
		    },
		    series: [{
		        name: 'New',
		        data: [5, 3, 4, 7, 2, 3, 4, 6, 22, 1, 1, 1]
		    }, {
		        name: 'Open',
		        data: [2, 2, 3, 2, 1, 1, 12, 1, 2, 3, 4, 9]
		    }, {
		        name: 'Closed',
		        data: [3, 4, 4, 2, 5, 2, 3, 2, 1, 1, 12, 1]
		    }, {
		        name: 'Cancelled',
		        data: [3, 4, 4, 2, 5, 2, 3, 2, 1, 1, 12, 1]
		    }, {
		        name: 'OnHold',
		        data: [3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 2, 3]
		    }]
	};

	return new Highcharts.Chart(myChart);
}

function displayEngagementBChartWeekly(div) {

	var myChart = {
		colors: ['#BF360C',
			'#CDDC39',
			'#0D47A1',
			'#B71C1C',
			'#F1C40F',
			'#1B5E20',
			'#01579B',
			'#880E4F',
			'#1A237E',
			'#FF8A65',
			'#F44336',
			'#43A047'],
		chart: {
			type: 'column',
			renderTo: div
		},
		title: {
			text: 'Dashboard B'
		},
		xAxis: {
			categories: ['W1', 'W2', 'W3', 'W4', 'W5', 'W6', 'W7', 'W8', 'W9', 'W10',
				'W11', 'W12', 'W13', 'W14', 'W15', 'W16', 'W17', 'W18', 'W19', 'W20',
				'W21', 'W22', 'W23', 'W24', 'W25', 'W26', 'W27', 'W28', 'W29', 'W30',
				'W31', 'W32', 'W33', 'W34', 'W35', 'W36', 'W37', 'W38', 'W39', 'W40',
				'W41', 'W42', 'W43', 'W44', 'W45', 'W46', 'W47', 'W48', 'W49', 'W50',
				'W51', 'W52']
		},
		yAxis: {
			min: 0,
			title: {
				text: 'No of Engagements Handled'
			},
			stackLabels: {
				enabled: true,
				style: {
					fontWeight: 'bold',
					color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
				}
			}
		},
		legend: {
			align: 'right',
			x: -30,
			verticalAlign: 'top',
			y: 25,
			floating: true,
			backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
			borderColor: '#CCC',
			borderWidth: 1,
			shadow: false
		},
		tooltip: {
			headerFormat: '<b>{point.x}</b><br/>',
			pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
		},
		plotOptions: {
			column: {
				stacking: 'normal',
				dataLabels: {
					enabled: true,
					color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
				}
			}
		},
		series: [{
			name: 'New',
			data: [5, 3, 4, 7, 2, 3, 4, 6, 22, 1, 1, 1, 5, 3, 4, 7, 2, 3, 4, 6, 22, 1, 1, 1, 5, 3, 4, 7, 2, 3, 4, 6, 22, 1, 1, 1, 5, 3, 4, 7, 2, 3, 4, 6, 22, 1, 1, 1 , 22, 1, 1, 1]
		}, {
			name: 'Open',
			data: [2, 2, 3, 2, 1, 1, 12, 6, 22, 1, 2, 2, 3, 2, 1, 1, 12, 6, 22, 1, 2, 2, 3, 2, 1, 1, 12, 6, 22, 1, 2, 2, 3, 2, 1, 1, 12, 6, 22, 1, 2, 2, 3, 2, 1, 1, 12, 6, 22, 1, 5, 6]
		}, {
			name: 'Closed',
			data: [3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 5, 6]
		}, {
			name: 'Cancelled',
			data: [3, 4, 4, 2, 5, 2, 3, 8, 9, 8, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 3, 4, 4, 2, 5, 2, 3, 5, 2, 3,3, 4, 4, 2, 5, 2, 3, 5, 2, 3, 2, 4]
		}, {
			name: 'OnHold',
			data: [3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 3, 4, 4, 2, 5, 7, 9]
		}]
	};

	return new Highcharts.Chart(myChart);
}