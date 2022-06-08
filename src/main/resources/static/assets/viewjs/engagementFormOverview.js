function EngagementFormOverViewModel() {

    var self = this;

    // todo : to this common to all view models.
    self.currentUserRole = ko.observable(getUserRole());

    self.projectName = ko.observable().extend({required: true});

    self.requestor = ko.observable(getSessionEmpData().employeeId);

    // when you click the submit button
    self.createEngagement = function () {
        // json body

        if (self.errors().length === 0) {
            var engagementBody = JSON.stringify({
                "projectName": self.projectName(),
                "requestedBy": self.requestor(),
                "engagementStatus": "DRAFT"
            });
            var engagementId = createNewEngagement(engagementBody);
            if (engagementId != null) {
                // create default forms
                // todo get ids in case to be used
                var defaultForms = createDefaultEngagementForms(engagementId);
                if (defaultForms != null) {
                    window.location.replace("engagementFormSummary.html?engagementId=" + engagementId);
                }
            }
        } else {
            alert('Please check the errors.');
            self.errors.showAllMessages();
            return false;
        }
    };


}

//Activates knockout.js
var koEngagementFormOverView = new EngagementFormOverViewModel();
ko.applyBindings(koEngagementFormOverView);
koEngagementFormOverView.errors = ko.validation.group(koEngagementFormOverView);