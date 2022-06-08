
var userRole;
function init() {
    // get user role and populate main div
    populateMainDiv();
}

function populateMainDiv() {

    $.getJSON("users_temp.json", function(json) {

        getSessionUserRole();
        if (userRole == 'ADMIN' || userRole == 'SPECIALIST'){
            jQuery("#mainDiv").load("engagementDashboard.html");
        } else if (userRole == 'REQUESTER' || userRole == 'OWNER'){
            jQuery("#mainDiv").load("engagementList.html");
        }
    });
}

// get session user role
function getSessionUserRole() {
    if(userRole == null){
        $.ajax("/infosec-api/getSessionUserRole", {
            type: "get",
            async: false,
            success: function (data) {
                userRole = data;
            },
            error: function (allData) {
                var json = ko.toJSON(allData);
                var parsed = JSON.parse(json);
                userRole = parsed.responseText;
            }
        });
    }
}

// init function
init();
