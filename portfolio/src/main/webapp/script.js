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
 *  Shows of hides the comments form based on the user's login status.
 */

async function showCommentsForm() {
    const commentsForm = document.getElementById("comments-form");
    commentsForm.hidden = true;

    const servletURL = "/check-login";
    const loggedInResponse = await fetch(servletURL);
    const loggedIn = loggedInResponse.headers.get("loggedIn");

    if (loggedIn == "true") {
        commentsForm.hidden = false;
    } else if (loggedIn == "false") {
        const loginLinkHTML = await loggedInResponse.text();
        commentsForm.innerHTML = "";
        commentsForm.innerHTML = loginLinkHTML;
        commentsForm.hidden = false;
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

function initMap() {
    var morehouseLocation = new google.maps.LatLng(33.747, -84.416);

    var mapOptions = {
        center: morehouseLocation,
        clickableIcons: true,
        mapTypeControl: false,
        draggable: true,
        maxZoom: 20,
        zoom: 16,
    };

    map = new google.maps.Map(document.getElementById('map'), mapOptions);
    map.setMapTypeId('roadmap');
    map.setTilt(45);
}
