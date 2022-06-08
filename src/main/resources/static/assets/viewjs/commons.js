var spinnerOpts = {
    lines: 13, // The number of lines to draw
    length: 38, // The length of each line
    width: 17, // The line thickness
    radius: 45, // The radius of the inner circle
    scale: 1, // Scales overall size of the spinner
    corners: 1, // Corner roundness (0..1)
    color: '#000', // CSS color or array of colors
    fadeColor: 'transparent', // CSS color or array of colors
    speed: 1, // Rounds per second
    rotate: 0, // The rotation offset
    animation: 'spinner-line-fade-quick', // The CSS animation name for the lines
    direction: 1, // 1: clockwise, -1: counterclockwise
    zIndex: 2e9, // The z-index (defaults to 2000000000)
    className: 'spinner', // The CSS class to assign to the spinner
    top: '50%', // Top position relative to parent
    left: '50%', // Left position relative to parent
    shadow: '0 0 1px transparent', // Box-shadow for the lines
    position: 'relative' // Element positioning
};

var spinner = new Spinner(spinnerOpts);

startSpinner = function () {
    spinner.spin(document.getElementById('spinning_wheel'));
};

stopSpinner = function () {
    spinner.stop();
};

var engagementStatusOptions = [
    {id: '1', name: 'DRAFT'},
    {id: '2', name: 'OPEN'},
    {id: '3', name: 'CLOSED'},
    {id: '4', name: 'CANCELLED'},
    {id: '5', name: 'ON_HOLD'},
    {id: '6', name: 'NEW'}
];

navigateToEngagement = function (engagementId) {
    alert(engagementId.id);
    //window.location.replace("engagementFormSummary.html");
};

setCookie = function (cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
};

deleteCookie = function (cname) {
    var d = new Date();
    d.setTime(d.getTime() - (1 * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + ";" + expires + ";path=/";
};

getCookie = function (cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
};

checkCookie = function (cname) {
    var val = self.getCookie(cname);
    if (val != "") {
        return true;
    }
    return false;
};

getURLParamEngagementIdValue = function () {

    var paramsString = window.location.toString().split("?")[1];
    if (paramsString != null) {
        var paramValues = paramsString.split("&");
        var paramValue = paramValues.toString().split("=");
        return paramValue[1];
    }
    return null;
};


//create new engagement
createNewEngagement = function (engagementBody) {

    var val = null;
    $.ajax("/infosec-api/engagements", {
        type: "post",
        async: false,
        contentType: 'application/json',
        data: engagementBody,
        success: function (engagementId) {
            console.log("Engagement Successfully Created");
            val = engagementId;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
    return val;
};

//create new engagement
updateEngagement = function (engagementId, engagementBody) {
    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId, {
        type: "put",
        async: false,
        contentType: 'application/json',
        data: engagementBody,
        success: function (engagementId) {
            val = 'success';
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};

// get engagement list
/*getEngagementList = function() {

    var val=null;
    $.ajax(engagementsURL, {
        type: "get",
        async: false,
        success: function (data) {
            var json = ko.toJSON(data);
            var val = JSON.parse(json);

/!*            for(var i=0;i<parsed.length;i++){
                var engagement = new EngageList(
                    parsed[i].engagementId,
                    parsed[i].projectName,
                    parsed[i].requestedBy,
                    timeConverter(+parsed[i].requestedDatetime),
                    parsed[i].engagementStatus,
                    timeConverter(+parsed[i].completedDatetime),
                    parsed[i].assignedTo,
                    10
                );
                self.engageList.push(engagement);
            }*!/
        },
        error: function (allData) {
            var json = ko.toJSON(allData);
            var parsed = JSON.parse(json);
            self.currentUserRole(parsed.responseText);
        }
    });
    return val;
};*/


//get engagement
getEngagement = function (engagementId) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId, {
        type: "get",
        async: false,
        contentType: 'application/json',
        success: function (engagement) {
            val = engagement;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};


//create new engagement form
createNewEngagementForm = function (engagementId, engagementFormBody) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId + "/engagement-forms", {
        type: "post",
        async: false,
        contentType: 'application/json',
        data: engagementFormBody,
        success: function (engagementId, textStatus, jQxhr) {
            console.log("Form Successfully Created");
            val = "Engagement Form Successfully Created";
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};


//create default engagement forms
createDefaultEngagementForms = function (engagementId) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId + "/default-engagement-forms", {
        type: "post",
        async: false,
        contentType: 'application/json',
        data: '["SUMMARY","SOLUTION_REVIEW","DATA_PRIVACY","EA_REPO_MEGA","TSSR","PLATFORM_SECURITY","CLOUD_THIRD_PARTY_HOSTING","CUSTOMER_FACING_APPLICATION","SECURITY_TOOLS","NETWORK_MANAGED_SYSTEMS","INTERFACE_CONTRACTS","SOLUTION_ARCHITECTURE","SECURITY_TESTING","RISK_PRE_APPROVAL_FORM"]',
        success: function (engagementId, textStatus, jQxhr) {
            console.log("Form Successfully Created");
            val = "Engagement Form Successfully Created";
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};


//create new engagement form
updateEngagementForm = function (engagementId, formType, engagementFormBody) {
    $.ajax("/infosec-api/engagements/" + engagementId + "/engagement-forms/" + formType, {
        type: "put",
        async: false,
        contentType: 'application/json',
        data: engagementFormBody,
        success: function (engagementId, textStatus, jQxhr) {
            console.log("Form Successfully updated");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
};

//create new engagement form
getEngagementForm = function (engagementId, formType) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId + "/engagement-forms/" + formType, {
        type: "get",
        async: false,
        contentType: 'application/json',
        success: function (engagementForm) {
            val = engagementForm;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
    return val;
};


getUserRole = function () {

    var userRole = null;
    $.ajax("/infosec-api/getSessionUserRole", {
        type: "get",
        async: false,
        success: function (data) {
            userRole = data;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return userRole;
};


// post risk list
createRisks = function (engagementId, risks) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId + "/risks", {
        type: "put",
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(risks),
        success: function (riskIds, textStatus, jQxhr) {
            val = riskIds;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};


updateRisk = function (engagementId, riskId, risks) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId + "/risks/" + riskId, {
        type: "put",
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(risks),
        success: function (riskId, textStatus, jQxhr) {
            console.log("Risk updated for risk id:" + riskId);
            val = riskId;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};

getRisksByEngagementId = function (engagementId) {

    var val = null;
    $.ajax("/infosec-api/engagements/" + engagementId + "/risks", {
        type: "get",
        async: false,
        contentType: 'application/json',
        success: function (engagement) {
            val = engagement;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};


getRisks = function (engagementId, riskId, businessOwner, projectName, infosecResource, riskStatus) {

    var riskURL = "/infosec-api/engagements/risks?";

    if (engagementId != '') {
        riskURL = riskURL + "engagementId=" + engagementId + '&';
    }
    if (riskId != '') {
        riskURL = riskURL + "riskId=" + riskId + '&';
    }
    if (businessOwner != '') {
        riskURL = riskURL + "businessOwner=" + businessOwner + '&';
    }
    if (projectName != '') {
        riskURL = riskURL + "projectName=" + projectName + '&';
    }
    if (infosecResource != '') {
        riskURL = riskURL + "infosecResource=" + infosecResource + '&';
    }
    if (riskStatus != '') {
        riskURL = riskURL + "riskStatus=" + riskStatus;
    }
    // remove last character if its i&
    if (riskURL.charAt(riskURL.length - 1) == '&' || riskURL.charAt(riskURL.length - 1) == '?') {
        riskURL = riskURL.substring(0, riskURL.length - 1);
    }

    var val = null;
    $.ajax(riskURL, {
        type: "get",
        async: false,
        contentType: 'application/json',
        success: function (riskList) {
            val = riskList;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};

//create new comment
createComment = function (engagementFormCommentsBody) {

    var val = '';
    $.ajax("/infosec-api/commentstracking/comments", {
        type: "post",
        async: false,
        contentType: 'application/json',
        data: engagementFormCommentsBody,
        success: function (commentsData) {
            val = commentsData;
            console.log("Comment Successfully Added");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
    return val;
};

//get comments
getComments = function (engagementId, engagementFormId) {

    console.log('engagementFormId' + engagementFormId)
    console.log('engagementId' + engagementId)

    var val = '';
    var url = "/infosec-api/commentstracking/comments?engagementId=" + engagementId.replaceAll('#','') + "&engagementFormId=" + '1';
    console.log('URL ' + url)
    $.ajax(url, {
        type: "get",
        async: false,
        contentType: 'application/json',
        success: function (commentsData) {
            val = commentsData;
            console.log("Comments Successfully retuned");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
    return val;
};

//create new comment
updateCommentViewed = function (engagementId, engagementFormId, commentItem, isInfosecUser) {
    var url = "/infosec-api/commentstracking/updateCommentViewed?engagementId=" + engagementId + "&engagementFormId=" + engagementFormId + "&commentItem=" + commentItem + "&isInfosecUser=" + isInfosecUser;
    $.ajax(url, {
        type: "put",
        async: false,
        contentType: 'application/json',
        success: function () {
            console.log("Comment View Successfully Updated");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
};

//create new tracking
createTracking = function (engagementFormTrackingBody) {

    var val = '';
    $.ajax("/infosec-api/commentstracking/tracking", {
        type: "post",
        async: false,
        contentType: 'application/json',
        data: engagementFormTrackingBody,
        success: function (trackingData) {
            val = trackingData;
            console.log("Tracking Successfully Created");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
    return val;
};

//get tracking
getTracking = function (engagementId, engagementFormId) {

    var val = '';
    var url = "/infosec-api/commentstracking/tracking?engagementId=" + engagementId.replaceAll("#", '') + "&engagementFormId=" + '1';
    $.ajax(url, {
        type: "get",
        async: false,
        contentType: 'application/json',
        success: function (trackingData) {
            val = trackingData;
            console.log("Tracking Successfully retuned");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
            return null;
        }
    });
    return val;
};

//get Infosec Users
getInfosecUsers = function () {

    var infosecResources = null;
    $.ajax("/infosec-api/getInfosecUsers", {
        type: "get",
        async: false,
        success: function (data) {
            infosecResources = data;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return infosecResources;
};

getSessionEmpData = function () {

    var employeeInfo = null;
    $.ajax("/infosec-api/getSessionEmpData", {
        type: "get",
        async: false,
        success: function (data) {
            employeeInfo = data;
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return employeeInfo;
};

function openAlert(message) {
    let div = document.getElementById('myInfosecModal');
    div.innerHTML = '' +
        '<div class="modal-dialog modal-sm">' +
        '<!-- Modal content-->' +
        '<div class="modal-content">' +
        '<div class="modal-body">' +
        '<h4><p>' + message + '</p></h4>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '';
    $("#myInfosecModal").modal();
};

uploadFileForEngagementForm = function (engagementId, formType, fileData) {

    var val = '';

    $.ajax("/infosec-api/engagements/" + engagementId + "/engagement-forms/" + formType + "/file-upload", {
        type: "post",
        async: false,
        contentType: false, // NEEDED, DON'T OMIT THIS (requires jQuery 1.6+)
        processData: false, // NEEDED, DON'T OMIT THIS
        data: fileData,
        success: function (engagementId, textStatus, jQxhr) {
            console.log("File Successfully Uploaded");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });

    return val;
};


getUploadedFilesForEngagement = function (engagementId, formType) {

    var val = '';
    $.ajax("/infosec-api/engagements/" + engagementId + "/engagement-forms/" + formType + "/files", {
        type: "get",
        async: false,
        success: function (engagementFormFileList) {
            val = engagementFormFileList;
            console.log("Engagement Form File List : " + engagementFormFileList);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};


downloadUploadedFile = function (engagementId, formType, fileName) {

    var val = '';

    var url = "/infosec-api/engagements/" + engagementId + "/engagement-forms/" + formType + "/file/" + fileName;

    alert(url);

    $.ajax("/infosec-api/engagements/" + engagementId + "/engagement-forms/" + formType + "/file/" + fileName, {
        type: "get",
        async: false,
        success: function () {

        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};

getEmployeeIdByFirstNameAndLastName = function (fName, lName, options) {

    for (var i = 0; i < options.length; i++) {
        if (options[i].firstName == fName && options[i].lastName == lName) {
            return options[i].employeeId;
        }
    }
};


getFirstNameAndLastNameByEmployeeId = function (employeeId, options) {

    for (var i = 0; i < options.length; i++) {
        if (options[i].employeeId == employeeId) {
            alert(options[i].firstName);
            return options[i].firstName + ' ' + options[i].lastName;
        }
    }
};

(function () {
    window.alert = function () {
        $("#alertModal .modal-body").text(arguments[0]);
        $("#alertModal").modal('show');
    };
})();


generateEngagementCertificate = function (engagementId) {

    var val = '';
    $.ajax("/infosec-api/engagements/" + engagementId + "/certificate", {
        type: "post",
        async: false,
        contentType: 'application/json',
        data: null,
        success: function (data, textStatus, xhr) {
            if (xhr == 200) {
                val = 'Engagement Certificate Successfully Generated';
            }
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
    return val;
};