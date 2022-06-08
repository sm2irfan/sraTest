function ViewModels() {

   // var self = this;

    var vm = {};
    vm.UserLogin = new UserLoginViewModel();
    vm.EngagementDashboard = new EngagementDashboardViewModel();
    ko.applyBindings(vm);
}

//Activates knockout.js
new ViewModels();