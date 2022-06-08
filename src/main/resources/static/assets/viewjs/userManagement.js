function UserManagementViewModel() {

    // get session user role
    self.getSessionUserRole = function () {
        if(self.currentUserRole() == null){
            $.ajax("/infosec-api/getSessionUserRole", {
                type: "get",
                async: false,
                success: function (allData) {
                    var json = ko.toJSON(allData);
                    var parsed = JSON.parse(json);
                    self.currentUserRole(parsed.responseText);
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
        //setSessionUser(self.currentUserRole());
        //alert("After:" + self.currentUserRole());
    };

    // get session emp data
    self.getSessionEmpData = function () {
        //alert("Calling getSessionEmpData " + self.currentEmpData());
        if(self.currentEmpData() == null){
            //alert("Calling currentEmpData is null ");
            $.ajax("/live-streaming-api/getSessionEmpData", {
                type: "get",
                async: false,
                success: function (allData) {
                    var json = ko.toJSON(allData);
                    var parsed = JSON.parse(json);
                    //alert(parsed.employeeId);
                    self.currentEmpData(
                        new Employee(
                            parsed.employeeId,
                            parsed.firstName,
                            parsed.lastName,
                            parsed.position,
                            parsed.email,
                            parsed.company,
                            parsed.imgUrl)
                    );
                },
                error: function (allData) {
                    var json = ko.toJSON(allData);
                    var parsed = JSON.parse(json);
                    //alert(parsed.employeeId);
                    self.currentEmpData(
                        new Employee(
                            parsed.employeeId,
                            parsed.firstName,
                            parsed.lastName,
                            parsed.position,
                            parsed.email,
                            parsed.company,
                            parsed.imgUrl)
                    );
                }
            });
        }
        if(self.currentEmpData() == null){
            self.errorPage();
        }
        alert("After:" + self.currentEmpData().firstName());
    };
}

//Activates knockout.js
var koUserManagementForm = new UserManagementViewModel();
ko.applyBindings(koUserManagementForm);


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