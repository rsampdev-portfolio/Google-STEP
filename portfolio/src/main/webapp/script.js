// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 *  Gets the comments from the server and adds them to the page.
 */

async function getComments() {
    var servletURL = "/data";

    var maxComments = document.getElementById("max-comments").value;

    if (maxComments != null && maxComments >= 1) {
        servletURL = `/data?max-comments=${maxComments}`;
    }

    const response = await fetch(servletURL);
    const comments = await response.json();

	const commentsList = document.getElementById("comments-container");

	commentsList.innerHTML = "";

    comments.forEach(comment => {
		const content = `[${comment.email}]: ${comment.text}`;

    	commentsList.appendChild(createListElement(content));
    });
}

/**
 *  Deletes and removes all comments from the page.
 */

async function deleteComments() {
    var servletURL = "/delete-data";

    await fetch(servletURL, {
        method: "POST"
    });

    await getComments();
}

/**
 *  Shows or hides the comments form based on the user's login status.
 *  
 *  Also shows a link to either login or logout
 *  based on the current user's login status. 
 */

async function showCommentsForm() {
    const notLoggedInLink = document.getElementById("not-logged-in-link");
    const userEmailHolder = document.getElementById("user-email-holder");
    const notLoggedInDiv = document.getElementById("not-logged-in-div");
    const loggedInLink = document.getElementById("logged-in-link");
    const commentsForm = document.getElementById("comments-form");
    const loggedInDiv = document.getElementById("logged-in-div");

    notLoggedInDiv.hidden = false;
    commentsForm.hidden = true;
    loggedInDiv.hidden = true;

    const servletURL = "/check-login";
    const loginStatusResponseObject = await fetch(servletURL);
    const loginStatusResponse = await loginStatusResponseObject.json();

    const loginStatus = loginStatusResponse.loginStatus;
    const email = loginStatusResponse.email;
    const link = loginStatusResponse.link;

    if (loginStatus == true) {
        userEmailHolder.innerText = email;
        notLoggedInDiv.hidden = true;
        commentsForm.hidden = false;
        loggedInDiv.hidden = false;
        loggedInLink.href = link;
    } else if (loginStatus == false) {
        notLoggedInLink.href = link;
    }
}

/**
 *	Creates an <li> element containing text.
 */
 
function createListElement(text) {
  	const liElement = document.createElement("li");
	liElement.innerText = text;
  	return liElement;
}

var map;

/**
 *  Initializes a map of Morehouse College with custom markers.
 */
 
function initMap() {
    const morehouseLocation = new google.maps.LatLng(33.747, -84.416);
    const brazealHallLocation = new google.maps.LatLng(33.748856, -84.416192);
    const techTowersLocation = new google.maps.LatLng(33.748400, -84.414113);
    const brawleyHallLocation = new google.maps.LatLng(33.746881, -84.414399);
    const leadershipCenterLocation = new google.maps.LatLng(33.744758, -84.414880);

    const mapOptions = {
        center: morehouseLocation,
        clickableIcons: true,
        mapTypeControl: false,
        draggable: true,
        maxZoom: 20,
        zoom: 17,
    };

    map = new google.maps.Map(document.getElementById('map'), mapOptions);
    map.setMapTypeId('roadmap');
    map.setTilt(45);

    const brazealHallMarker = new google.maps.Marker({
        position: brazealHallLocation,
        title: "Brazeal Hall",
        map: map
    });

    var brazealHallMarkerInfowindow = new google.maps.InfoWindow({
        content: "Brazeal Hall Info Window"
    });

    brazealHallMarker.addListener('click', function() {
        brazealHallMarkerInfowindow.open(map, brazealHallMarker);
        map.setCenter(brazealHallMarker.getPosition());
        map.setZoom(19);
    });

    const techTowersMarker = new google.maps.Marker({
        position: techTowersLocation,
        title: "Tech Towers",
        map: map
    });

    var techTowersMarkerInfowindow = new google.maps.InfoWindow({
        content: "Tech Towers Info Window"
    });

    techTowersMarker.addListener('click', function() {
        techTowersMarkerInfowindow.open(map, techTowersMarker);
        map.setCenter(techTowersMarker.getPosition());
        map.setZoom(19);
    });

    const brawleyHallMarker = new google.maps.Marker({
        position: brawleyHallLocation,
        title: "Brawley Hall",
        map: map
    });

    var brawleyHallMarkerInfowindow = new google.maps.InfoWindow({
        content: "Brawley Hall Info Window"
    });

    brawleyHallMarker.addListener('click', function() {
        brawleyHallMarkerInfowindow.open(map, brawleyHallMarker);
        map.setCenter(brawleyHallMarker.getPosition());
        map.setZoom(19);
    });

    const leadershipCenterMarker = new google.maps.Marker({
        position: leadershipCenterLocation,
        title: "Leadership Center",
        map: map
    });

    var leadershipCenterMarkerInfowindow = new google.maps.InfoWindow({
        content: "Leadership Center Info Window"
    });

    leadershipCenterMarker.addListener('click', function() {
        leadershipCenterMarkerInfowindow.open(map, leadershipCenterMarker);
        map.setCenter(leadershipCenterMarker.getPosition());
        map.setZoom(19);
    });
}
