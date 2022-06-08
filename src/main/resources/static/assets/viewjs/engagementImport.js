function EngagementImportViewModel() {

    var self = this;

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
var koEngagementImport = new EngagementImportViewModel();
ko.applyBindings(koEngagementImport);