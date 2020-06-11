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
    const {isLoggedIn, userEmail, loginLink, logoutLink} = loginStatusResponse;

    if (isLoggedIn) {
        userEmailHolder.innerText = userEmail;
        notLoggedInDiv.hidden = true;
        commentsForm.hidden = false;
        loggedInDiv.hidden = false;
        loggedInLink.href = loginLink;
    } else {
        notLoggedInLink.href = logoutLink;
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

/**
 *  Initializes a map of Morehouse College with custom markers.
 */
 
function initMap() {
    const mapOptions = {
        center: new google.maps.LatLng(33.747, -84.416),
        mapTypeControl: false,
        clickableIcons: true,
        draggable: true,
        maxZoom: 20,
        zoom: 17,
    };

    const map = new google.maps.Map(document.getElementById('map'), mapOptions);
    map.setMapTypeId('roadmap');
    map.setTilt(45);
    
    addMarkerToMap(map, "Tech Towers", new google.maps.LatLng(33.748400, -84.414113), "Tech Towers Info Window");
    addMarkerToMap(map, "Brazeal Hall", new google.maps.LatLng(33.748856, -84.416192), "Brazeal Hall Info Window");
    addMarkerToMap(map, "Brawley Hall", new google.maps.LatLng(33.746881, -84.414399), "Brawley Hall Info Window");
    addMarkerToMap(map, "Leadership Center", new google.maps.LatLng(33.744758, -84.414880), "Leadership Center Info Window");
}

function addMarkerToMap(map, markerTitle, markerLocation, markerInfoWindowContent) {
    const marker = new google.maps.Marker({
        position: markerLocation,
        title: markerTitle,
        map: map
    });

    const markerInfoWindow = new google.maps.InfoWindow({
        content: markerInfoWindowContent
    });

    marker.addListener('click', function() {
        markerInfoWindow.open(map, marker);
        map.setCenter(marker.getPosition());
        map.setZoom(19);
    });
}
