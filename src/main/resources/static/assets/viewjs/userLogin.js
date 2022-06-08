function UserLoginViewModel() {

    var self=this;

    self.username=ko.observable('username');
    self.password=ko.observable('password');

    self.login = function(){
        console.log(self.username());
        alert(self.username());
        alert(self.password());
    };

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
}

//Activates knockout.js
var koUserLogin = new UserLoginViewModel();
ko.applyBindings(koUserLogin);