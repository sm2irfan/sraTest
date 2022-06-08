function EngagementFormSendViewModel() {

    var self = this;

    self.currentUserRole = ko.observable(getUserRole());

    // check if the url contains engagement id
    var engagementId = getURLParamEngagementIdValue();
    var engagementData = null;
    if(engagementId!=null) {
        // go and fetch the form data for the given engagement id
        engagementData = getEngagement(engagementId);
    }

    self.goPrev = function () {
        if(engagementId!=null) {
            window.location.replace("engagementFormSecurityTesting.html?engagementId=" + engagementId);
        } else {
            window.location.replace("engagementFormSecurityTesting.html");
        }
    };

    self.submitEngagement = function (){

        if(engagementId!=null) {
            engagementData.engagementStatus = "NEW";
            engagementData.requestedDatetime = +new Date();
            updateEngagement(engagementId, JSON.stringify(engagementData));
            window.location.replace("engagementList.html");
        }
        else {
            alert("Please Create An Engagement First");
        }
    };
}

//Activates knockout.js
var koengageformsendvm = new EngagementFormSendViewModel();
ko.applyBindings(koengageformsendvm);